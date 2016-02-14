package de.drkalz.midwifesearch.Midwifes;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import de.drkalz.midwifesearch.R;
import de.drkalz.midwifesearch.StartApplication;

public class MidwifeArea extends FragmentActivity implements OnMapReadyCallback {

    final StartApplication sApp = StartApplication.getInstance();
    ImageButton saveButton, addButton;
    Double lat, lng;
    int positionOfItem;
    TextView tvStreet, tvCity, tvCountry, tvZip, tvRadius;
    EditText etStreet, etCity, etCountry, etZip, etRadius;
    ListView listView;
    GridLayout gridLayout;
    RelativeLayout rlChangeDeleteArea;
    ArrayAdapter arrayAdapter;
    ArrayList<String> streetList = new ArrayList<>();
    ArrayList<String> areaUID = new ArrayList<>();
    ArrayList<AngebotsGebiet> areaList = new ArrayList<>();
    ArrayList<Circle> circles = new ArrayList<>();
    ArrayList<Marker> markers = new ArrayList<>();
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
        return zoom;
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
            rlChangeDeleteArea.setVisibility(View.INVISIBLE);
            etStreet.setText("");
            etCountry.setText("");
            etCity.setText("");
            etZip.setText("");
            etRadius.setText("");
        }
    }

    protected void drawLocationCircle(AngebotsGebiet newArea, int pos) {
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
        if (pos > -1) {
            markers.get(pos).isVisible();
            markers.get(pos).remove();
            circles.get(pos).remove();
        }
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(newArea.getStreet()));
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(lat, lng))
                .radius(newArea.getRadiusInM())
                .strokeColor(Color.BLUE));
        if (pos == -1) {
            markers.add(marker);
            circles.add(circle);
        } else {
            markers.set(pos, marker);
            circles.set(pos, circle);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, getZoomLevel(circle) - 1));
    }

    public void addArea(final AngebotsGebiet uebergabeGebiet) {

        boolean addOrUpdate;

        addOrUpdate = true;

        if (uebergabeGebiet != null) {
            etStreet.setText(uebergabeGebiet.getStreet());
            etCity.setText(uebergabeGebiet.getCity());
            etCountry.setText(uebergabeGebiet.getCountry());
            etZip.setText(uebergabeGebiet.getZip());
            etRadius.setText(String.valueOf(uebergabeGebiet.getRadiusInM() / 1000));
            addOrUpdate = false;
        }

        switchViews(true);

        final boolean finalAddOrUpdate = addOrUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etStreet.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie einen Straßennahmen + Hausnummer ein.", Toast.LENGTH_LONG).show();
                } else if (etCity.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie eine Stadt ein.", Toast.LENGTH_LONG).show();
                } else if (etCountry.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie ein Land ein.", Toast.LENGTH_LONG).show();
                } else if (etZip.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie die Postleitzahl ein.", Toast.LENGTH_LONG).show();
                } else if (etRadius.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Bitte geben Sie den Radius an, in dem Sie um die o.g. Adresse Anfragen annehmen möchten.",
                            Toast.LENGTH_LONG).show();
                } else {
                    final AngebotsGebiet gebiet = new AngebotsGebiet();
                    gebiet.setStreet(etStreet.getText().toString());
                    gebiet.setCity(etCity.getText().toString());
                    gebiet.setCountry(etCountry.getText().toString());
                    gebiet.setZip(etZip.getText().toString());
                    gebiet.setRadiusInM((Double.parseDouble(etRadius.getText().toString())) * 1000.0);

                    if (finalAddOrUpdate) {
                        ref.child(sApp.getAuthData().getUid()).push().setValue(gebiet, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if (firebaseError == null) {
                                    areaList.add(gebiet);
                                    areaUID.add(firebase.getKey());
                                    streetList.add(gebiet.getStreet() + ", "
                                            + gebiet.getZip() + " "
                                            + gebiet.getCity() + "\nRadius: "
                                            + Double.toString(gebiet.getRadiusInM() / 1000) + " km");
                                    geoFire.setLocation(firebase.getKey(), new GeoLocation(lat, lng));
                                    drawLocationCircle(gebiet, -1);
                                }
                            }
                        });
                    } else {
                        Firebase updateRef = ref.child(sApp.getAuthData().getUid());
                        updateRef.child(areaUID.get(positionOfItem)).setValue(gebiet);
                        areaList.set(positionOfItem, gebiet);
                        streetList.set(positionOfItem, gebiet.getStreet() + ", "
                                + gebiet.getZip() + " "
                                + gebiet.getCity() + "\nRadius: "
                                + Double.toString(gebiet.getRadiusInM() / 1000) + " km");
                        drawLocationCircle(gebiet, positionOfItem);
                    }
                    arrayAdapter.notifyDataSetChanged();
                    switchViews(false);
                }
            }
        });
    }

    protected void deleteArea(View view) {
        int i = Integer.parseInt(view.getTag().toString());
        Firebase changeRef = ref.child(sApp.getAuthData().getUid());
        if (i == 20) {
            rlChangeDeleteArea.setVisibility(View.INVISIBLE);
            changeRef.child(areaUID.get(positionOfItem)).removeValue();
            geoFire.removeLocation(areaUID.get(positionOfItem));
            areaList.remove(positionOfItem);
            areaUID.remove(positionOfItem);
            streetList.remove(positionOfItem);
            arrayAdapter.notifyDataSetChanged();
        }
        switchViews(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_midwife_area);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ref = new Firebase("https://midwife-search.firebaseio.com/MidwifeArea");
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
        rlChangeDeleteArea = (RelativeLayout) findViewById(R.id.rl_ChangeDeleteArea);

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
                        streetList.add(newItem.getStreet() + ", "
                                + newItem.getZip() + " "
                                + newItem.getCity() + "\nRadius: "
                                + Double.toString(newItem.getRadiusInM() / 1000) + " km");
                        areaList.add(item.getValue(AngebotsGebiet.class));
                        drawLocationCircle(newItem, -1);
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
                addArea(showItem);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                rlChangeDeleteArea.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                addButton.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArea(null);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }
}
