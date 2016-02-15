package de.drkalz.midwifesearch.Pregnants;

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
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
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

import de.drkalz.midwifesearch.R;
import de.drkalz.midwifesearch.User;

public class MapRequest extends FragmentActivity implements OnMapReadyCallback {

    final String[] address = new String[1];
    Date newDate;
    Double lat, lng;
    CalendarView dateOfBirth;
    CheckBox sendRequest;
    private GoogleMap mMap;
    private String requesterID;
    private Firebase refRequest;
    private GeoFire geoFire;

    public void saveRequest() {

        Request newRequest = new Request();
        newRequest.setDateOfBirth(newDate);
        newRequest.setMidwifeID("");

        refRequest.child(requesterID).setValue(newRequest, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError == null) {
                        Toast.makeText(getApplicationContext(), "Request gesendet", Toast.LENGTH_SHORT).show();
                        geoFire.setLocation(requesterID, new GeoLocation(lat, lng));
                    }
                }
            });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_request);

        Intent i = getIntent();
        requesterID = i.getStringExtra("userUID");
        lat = 0.0;
        lng = 0.0;

        dateOfBirth = (CalendarView) findViewById(R.id.cv_dateOfBirth);
        sendRequest = (CheckBox) findViewById(R.id.cb_sendRequest);

        refRequest = new Firebase("https://midwife-search.firebaseio.com/Request");
        geoFire = new GeoFire(new Firebase("https://midwife-search.firebaseio.com/Location/Requests"));

        Query query = refRequest.orderByChild(requesterID).limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Request knownRequest = snapshot.getValue(Request.class);
                        if (knownRequest.getMidwifeID().isEmpty()) {
                            geoFire.getLocation(requesterID, new LocationCallback() {
                                @Override
                                public void onLocationResult(String key, GeoLocation location) {
                                    lat = location.latitude;
                                    lng = location.longitude;
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                }
                            });

                            newDate = knownRequest.getDateOfBirth();
                            dateOfBirth.setDate(knownRequest.getDateOfBirth().getTime());
                            sendRequest.setChecked(true);
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
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Mein Standort"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        dateOfBirth.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String puffer = String.valueOf(dayOfMonth) + "/" + String.valueOf(month)
                        + "/" + String.valueOf(year);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    newDate = dateFormat.parse(puffer);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (sendRequest.isChecked()) {
                    saveRequest();
                }
            }
        });

        sendRequest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    saveRequest();

                } else {
                    refRequest.child(requesterID).removeValue();
                    geoFire.removeLocation(requesterID);
                    Toast.makeText(getApplicationContext(), "Request wurde gelöscht", Toast.LENGTH_SHORT).show();
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
