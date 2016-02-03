package de.drkalz.midewifesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        ImageButton registerUser = (ImageButton) findViewById(R.id.fab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

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

        Intent i = getIntent();
        cuEmail.setText(i.getStringExtra("userEmail"));
        cuPassword.setText(i.getStringExtra("userPassword"));

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
                ref.createUser(eMail, passWord, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> newUser) {
                        Toast.makeText(getApplicationContext(), "Sie wurden registriert", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), "Sie wurden NICHT registriert \n" + firebaseError.getDetails()
                                , Toast.LENGTH_SHORT).show();
                    }
                });

                ref.authWithPassword(eMail, passWord, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Map<String, String> newUser = new HashMap<>();
                        newUser.put("firstname", cuFirstname.getText().toString());
                        newUser.put("lastname", cuName.getText().toString());
                        newUser.put("street", cuStreet.getText().toString());
                        newUser.put("city", cuCity.getText().toString());
                        newUser.put("country", cuCountry.getText().toString());
                        newUser.put("zip", cuZip.getText().toString());
                        newUser.put("telefon", cuTelefon.getText().toString());
                        newUser.put("mobil", cuMobil.getText().toString());
                        newUser.put("homepage", cuHomepage.getText().toString());
                        newUser.put("isMidwife", isMidwife[0].toString());

                        ref.child("Users").child(authData.getUid()).setValue(newUser);

                        if (isMidwife[0]) {
                            Intent i = new Intent(getApplicationContext(), Service.class);
                            i.putExtra("userUID", authData.getUid());
                            startActivity(i);
                            finish();
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