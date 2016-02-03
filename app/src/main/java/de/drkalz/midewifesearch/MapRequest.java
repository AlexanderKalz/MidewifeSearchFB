package de.drkalz.midewifesearch;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MapRequest extends FragmentActivity implements OnMapReadyCallback {

    final String[] address = new String[1];
    public String newDate;
    public String requestUID;
    Double lat, lng;
    CalendarView dateOfBirth;
    CheckBox sendRequest;
    User user;
    private GoogleMap mMap;
    private String requesterID;
    private Firebase refRequest;

    public void createOrUpdateRequest(boolean create) {

        Request newRequest = new Request();
        newRequest.setRequesterID(requesterID);
        newRequest.setDateOfBirth(newDate);
        newRequest.setMidwifeID("");
        newRequest.setGeoLocation(new GeoLocation(lat, lng));

        if (create) {
            refRequest.push().setValue(newRequest, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError == null) {
                        Toast.makeText(getApplicationContext(), "Request gesendet", Toast.LENGTH_SHORT).show();
                        requestUID = firebase.getKey();
                    }
                }
            });
        } else {
            refRequest.child(requestUID).setValue(newRequest, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError == null) {
                        Toast.makeText(getApplicationContext(), "Request wurde aktualisiert", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_request);

        Intent i = getIntent();
        requesterID = i.getStringExtra("userUID");
        requestUID = "";
        lat = 0.0;
        lng = 0.0;

        dateOfBirth = (CalendarView) findViewById(R.id.cv_dateOfBirth);
        sendRequest = (CheckBox) findViewById(R.id.cb_sendRequest);

        refRequest = new Firebase("https://midwife-search.firebaseio.com/Request");
        Query query = refRequest.orderByChild(requesterID).limitToFirst(1);
        refRequest.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Request knownRequest = snapshot.getValue(Request.class);
                        if (knownRequest.getMidwifeID().isEmpty()) {
                            requestUID = snapshot.getKey();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
                            try {
                                Date date = sdf.parse(knownRequest.getDateOfBirth());
                                dateOfBirth.setDate(date.getTime());
                                sendRequest.setChecked(true);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase refUser = new Firebase("https://midwife-search.firebaseio.com/Users").child(requesterID);
        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    address[0] = user.getStreet() + " " +
                            user.getCity() + " " +
                            user.getCountry() + " " +
                            user.getZip();

                    Geocoder geoCoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geoCoder.getFromLocationName(address[0], 1);
                        if (addressList != null && addressList.size() > 0) {
                            lat = addressList.get(0).getLatitude();
                            lng = addressList.get(0).getLongitude();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    LatLng requesterPos = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(requesterPos));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(requesterPos, 12));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        dateOfBirth.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                newDate = String.valueOf(dayOfMonth) + "/"
                        + String.valueOf(month + 1) + "/"
                        + String.valueOf(year);
                if (sendRequest.isChecked() && !requestUID.isEmpty()) {
                    createOrUpdateRequest(false);
                } else if (sendRequest.isChecked() && !requestUID.isEmpty()) {
                    createOrUpdateRequest(true);
                }
            }
        });

        sendRequest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && requestUID.isEmpty()) {
                    createOrUpdateRequest(true);
                } else if (isChecked && !requestUID.isEmpty()) {
                    createOrUpdateRequest(false);
                } else {
                    refRequest.child(requestUID).removeValue();
                    Toast.makeText(getApplicationContext(), "Request wurde gelöscht", Toast.LENGTH_SHORT).show();
                    requestUID = "";
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
