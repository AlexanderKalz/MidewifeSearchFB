package de.drkalz.midewifesearch.Midwifes;

import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import de.drkalz.midewifesearch.R;
import de.drkalz.midewifesearch.StartApplication;

public class MidwifeArea extends FragmentActivity implements OnMapReadyCallback {

    final StartApplication sApp = StartApplication.getInstance();
    ImageButton saveButton, addButton;
    Double lat, lng;
    TextView tvStreet, tvCity, tvCountry, tvZip, tvRadius;
    EditText etStreet, etCity, etCountry, etZip, etRadius;
    ListView listView;
    GridLayout gridLayout;
    ArrayAdapter arrayAdapter;
    ArrayList<String> streetList = new ArrayList<>();
    ArrayList<String> areaUID = new ArrayList<>();
    ArrayList<AngebotsGebiet> areaList = new ArrayList<>();
    private GoogleMap mMap;
    private Firebase ref;
    private GeoFire geoFire;

    public int getZoomLevel(Circle circle) {
        int zoom = 12;
        if (circle != null) {
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoom = (int) (16 - Math.log(scale) / Math.log(2));
        }
        int zoomLevel = zoom;
        return zoomLevel;
    }

    protected void switchViews(boolean switchOn) {
        if (switchOn) {
            listView.setVisibility(View.INVISIBLE);
            addButton.setVisibility(View.INVISIBLE);
            saveButton.setVisibility(View.VISIBLE);
            gridLayout.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.INVISIBLE);
            gridLayout.setVisibility(View.INVISIBLE);
            etStreet.setText("");
            etCountry.setText("");
            etCity.setText("");
            etZip.setText("");
            etRadius.setText("");
        }
    }

    protected void drawLocationCircle(AngebotsGebiet newArea) {
        String address = newArea.getStreet() + " " + newArea.getCity() + " "
                + newArea.getCountry() + " " + newArea.getZip();

        Geocoder geoCoder = new Geocoder(getApplicationContext());
        try {
            List<Address> addressList = geoCoder.getFromLocationName(address, 1);
            if (addressList != null && addressList.size() > 0) {
                lat = addressList.get(0).getLatitude();
                lng = addressList.get(0).getLongitude();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LatLng latLng = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(latLng).title(newArea.getStreet()));

        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(lat, lng))
                .radius(newArea.getRadiusKM())
                .strokeColor(Color.BLUE));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, getZoomLevel(circle) - 1));
    }

    public void addArea(View view) {
        switchViews(true);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etStreet.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie einen Straßennahmen + Hausnummer ein.", Toast.LENGTH_LONG).show();
                    return;
                } else if (etCity.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie eine Stadt ein.", Toast.LENGTH_LONG).show();
                    return;
                } else if (etCountry.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie ein Land ein.", Toast.LENGTH_LONG).show();
                    return;
                } else if (etZip.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie die Postleitzahl ein.", Toast.LENGTH_LONG).show();
                    return;
                } else if (etRadius.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie den Radius an, in dem Sie um die o.g. Adresse Anfragen annehmen möchten.",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {
                    final AngebotsGebiet gebiet = new AngebotsGebiet();
                    gebiet.setStreet(etStreet.getText().toString());
                    gebiet.setCity(etCity.getText().toString());
                    gebiet.setCountry(etCountry.getText().toString());
                    gebiet.setZip(etZip.getText().toString());
                    gebiet.setRadiusKM((Double.parseDouble(etRadius.getText().toString())) * 1000.0);

                    drawLocationCircle(gebiet);

                    ref.child(sApp.getAuthData().getUid()).push().setValue(gebiet, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError == null) {
                                areaList.add(gebiet);
                                areaUID.add(firebase.getKey());
                                streetList.add(gebiet.getStreet() + ", " + gebiet.getZip() + " " + gebiet.getCity());
                                geoFire.setLocation(firebase.getKey(), new GeoLocation(lat, lng));
                                arrayAdapter.notifyDataSetChanged();
                                switchViews(false);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_midwife_area);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ref = new Firebase("https://midwife-search.firebaseio.com/MidewifeArea");
        geoFire = new GeoFire(new Firebase("https://midwife-search.firebaseio.com/Location/Areas"));

        tvStreet = (TextView) findViewById(R.id.tv_streetOfArea);
        tvCity = (TextView) findViewById(R.id.tv_cityOfArea);
        tvCountry = (TextView) findViewById(R.id.tv_countryOfArea);
        tvZip = (TextView) findViewById(R.id.tv_zipOfArea);
        tvRadius = (TextView) findViewById(R.id.tv_radius);
        etStreet = (EditText) findViewById(R.id.et_streetOfArea);
        etCity = (EditText) findViewById(R.id.et_cityOfArea);
        etCountry = (EditText) findViewById(R.id.et_countryOfArea);
        etZip = (EditText) findViewById(R.id.et_zipOfArea);
        etRadius = (EditText) findViewById(R.id.et_radius);
        saveButton = (ImageButton) findViewById(R.id.ib_saveArea);
        addButton = (ImageButton) findViewById(R.id.ib_addArea);
        gridLayout = (GridLayout) findViewById(R.id.gl_Area);

        listView = (ListView) findViewById(R.id.lv_Streets);
        switchViews(false);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, streetList);
        listView.setAdapter(arrayAdapter);

        ref.child(sApp.getAuthData().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                areaUID.clear();
                streetList.clear();
                areaList.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        areaUID.add(item.getKey());
                        AngebotsGebiet newItem = item.getValue(AngebotsGebiet.class);
                        streetList.add(newItem.getStreet() + ", " + newItem.getZip() + " " + newItem.getCity());
                        areaList.add(item.getValue(AngebotsGebiet.class));
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AngebotsGebiet showItem = areaList.get(position);
                drawLocationCircle(showItem);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
                alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_info).setTitle("Ändern oder Löschen?")
                        .setMessage("Möchten Sie den Eintrag ändern oder löschen?")
                        .setPositiveButton("Ändern", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Löschen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
    }
}
