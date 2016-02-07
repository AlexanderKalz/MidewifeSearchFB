package de.drkalz.midewifesearch.Midwifes;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
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

public class MidwifeArea extends FragmentActivity implements OnMapReadyCallback {

    ImageButton saveArea, addArea;
    Double lat, lng;
    TextView tvStreet, tvCity, tvCountry, tvZip, tvRadius;
    EditText etStreet, etCity, etCountry, etZip, etRadius;
    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> streetList = new ArrayList<>();
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

    public void addArea(View view) {

        saveArea.setVisibility(View.VISIBLE);
        addArea.setVisibility(View.INVISIBLE);
        tvStreet.setVisibility(View.VISIBLE);
        tvCity.setVisibility(View.VISIBLE);
        tvCountry.setVisibility(View.VISIBLE);
        tvZip.setVisibility(View.VISIBLE);
        tvRadius.setVisibility(View.VISIBLE);
        etStreet.setVisibility(View.VISIBLE);
        etCity.setVisibility(View.VISIBLE);
        etCountry.setVisibility(View.VISIBLE);
        etZip.setVisibility(View.VISIBLE);
        etRadius.setVisibility(View.VISIBLE);

        saveArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etStreet.equals("")) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie einen Straßennahmen + Hausnummer ein.", Toast.LENGTH_LONG).show();
                    return;
                } else if (etCity.equals("")) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie eine Stadt ein.", Toast.LENGTH_LONG).show();
                    return;
                } else if (etCountry.equals("")) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie ein Land ein.", Toast.LENGTH_LONG).show();
                    return;
                } else if (etZip.equals("")) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie die Postleitzahl ein.", Toast.LENGTH_LONG).show();
                    return;
                } else if (etRadius.equals("")) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie den Radius an, in dem Sie um die o.g. Adresse Anfragen annehmen möchten.",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {
                    AngebotsGebiet newArea = new AngebotsGebiet();
                    newArea.setStreet(etStreet.getText().toString());
                    newArea.setCity(etCity.getText().toString());
                    newArea.setCountry(etCountry.getText().toString());
                    newArea.setZip(etZip.getText().toString());
                    newArea.setRadiusKM((Double.parseDouble(etRadius.getText().toString())) * 1000.0);

                    String adress = etStreet.getText().toString() + " "
                            + etCity.getText().toString() + " "
                            + etCountry.getText().toString() + " "
                            + etZip.getText().toString();

                    Geocoder geoCoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geoCoder.getFromLocationName(adress, 1);
                        if (addressList != null && addressList.size() > 0) {
                            lat = addressList.get(0).getLatitude();
                            lng = addressList.get(0).getLongitude();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    LatLng requesterPos = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(requesterPos).title(newArea.getStreet()));

                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(lat, lng))
                            .radius(newArea.getRadiusKM())
                            .strokeColor(Color.BLUE));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(requesterPos, getZoomLevel(circle)));


                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_midwife_area);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
        saveArea = (ImageButton) findViewById(R.id.ib_saveArea);
        addArea = (ImageButton) findViewById(R.id.ib_addArea);

        tvStreet.setVisibility(View.INVISIBLE);
        tvCity.setVisibility(View.INVISIBLE);
        tvCountry.setVisibility(View.INVISIBLE);
        tvZip.setVisibility(View.INVISIBLE);
        tvRadius.setVisibility(View.INVISIBLE);
        etStreet.setVisibility(View.INVISIBLE);
        etCity.setVisibility(View.INVISIBLE);
        etCountry.setVisibility(View.INVISIBLE);
        etZip.setVisibility(View.INVISIBLE);
        etRadius.setVisibility(View.INVISIBLE);

        listView = (ListView) findViewById(R.id.lv_Streets);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
    }
}
