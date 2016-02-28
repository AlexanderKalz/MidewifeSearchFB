package de.drkalz.midwifesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

import de.drkalz.midwifesearch.Midwifes.Service;
import de.drkalz.midwifesearch.Pregnants.MapRequest;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageButton registerUser = (ImageButton) findViewById(R.id.fab);
        getSupportActionBar().hide();

        final Boolean[] isMidwife = {false};

        final EditText cuFirstname = (EditText) findViewById(R.id.et_firstname);
        final EditText cuName = (EditText) findViewById(R.id.et_name);
        final EditText cuStreet = (EditText) findViewById(R.id.et_street);
        final EditText cuCity = (EditText) findViewById(R.id.et_city);
        final EditText cuCountry = (EditText) findViewById(R.id.et_country);
        final EditText cuZip = (EditText) findViewById(R.id.et_zip);
        final EditText cuTelefon = (EditText) findViewById(R.id.et_telefon);
        final EditText cuMobil = (EditText) findViewById(R.id.et_mobil);
        final EditText cuHomepage = (EditText) findViewById(R.id.et_homepage);
        final EditText cuEmail = (EditText) findViewById(R.id.et_email);
        final EditText cuPassword = (EditText) findViewById(R.id.et_password);
        final Switch cuIsMidwife = (Switch) findViewById(R.id.sw_isMidwife);

        final Intent i = getIntent();
        final boolean changeData = i.getBooleanExtra("changeData", false);
        cuEmail.setText(i.getStringExtra("userEmail"));
        cuPassword.setText(i.getStringExtra("userPassword"));

        if (changeData) {
            String userUid = i.getStringExtra("userUID");
            Firebase users = new Firebase("https://midwife-search.firebaseio.com/Users").child(userUid);
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User currentUser = dataSnapshot.getValue(User.class);
                        cuFirstname.setText(currentUser.getFirstname());
                        cuName.setText(currentUser.getLastname());
                        cuStreet.setText(currentUser.getStreet());
                        cuZip.setText(currentUser.getZip());
                        cuCity.setText(currentUser.getCity());
                        cuCountry.setText(currentUser.getCountry());
                        cuTelefon.setText(currentUser.getTelefon());
                        cuMobil.setText(currentUser.getMobil());
                        cuHomepage.setText(currentUser.getHomepage());
                        cuIsMidwife.setChecked(currentUser.getIsMidwife());
                        cuEmail.setText(currentUser.geteMail());
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }

        cuIsMidwife.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isMidwife[0] = true;
                    cuIsMidwife.setText("Ja");

                } else {
                    isMidwife[0] = false;
                    cuIsMidwife.setText("Nein");
                }
            }
        });

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cuFirstname.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Vorname nicht eingetragen!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cuName.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Nachname nicht eingetragen!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cuStreet.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Straße nicht eingetragen!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cuCity.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Stadt nicht eingetragen!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cuCountry.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Land nicht eingetragen!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cuZip.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Postleitzahl nicht eingetragen!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cuTelefon.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Festnetz nicht eingetragen!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cuEmail.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Email (Benutzername) nicht eingetragen!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cuPassword.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Passwort nicht eingetragen!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String eMail = cuEmail.getText().toString();
                String passWord = cuPassword.getText().toString();

                final Firebase ref = new Firebase("https://midwife-search.firebaseio.com/");

                if (!changeData) {
                    ref.createUser(eMail, passWord, new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> newUser) {
                            Toast.makeText(getApplicationContext(), "Sie wurden registriert", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                        }
                    });
                }

                ref.authWithPassword(eMail, passWord, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        User newUser = new User();
                        newUser.setFirstname(cuFirstname.getText().toString());
                        newUser.setLastname(cuName.getText().toString());
                        newUser.setStreet(cuStreet.getText().toString());
                        newUser.setCity(cuCity.getText().toString());
                        newUser.setCountry(cuCountry.getText().toString());
                        newUser.setZip(cuZip.getText().toString());
                        newUser.setTelefon(cuTelefon.getText().toString());
                        newUser.setMobil(cuMobil.getText().toString());
                        newUser.setHomepage(cuHomepage.getText().toString());
                        newUser.setMidwife(isMidwife[0]);
                        newUser.seteMail(cuEmail.getText().toString());

                        ref.child("Users").child(authData.getUid()).setValue(newUser);

                        if (!changeData) {
                            if (isMidwife[0]) {
                                Intent i = new Intent(getApplicationContext(), Service.class);
                                i.putExtra("userUID", authData.getUid());
                                startActivity(i);
                                finish();
                            } else {
                                Intent i = new Intent(getApplicationContext(), MapRequest.class);
                                i.putExtra("userUID", authData.getUid());
                                startActivity(i);
                                finish();
                            }
                        } else {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                    }
                });
            }
        });
    }
}
