package de.drkalz.midwifesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private void callRegisterActivity(String email, String password) {
        Intent i = new Intent(getApplicationContext(), Register.class);
        i.putExtra("userEmail", email);
        i.putExtra("userPassword", password);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final StartApplication sApp = StartApplication.getInstance();

        final EditText userEmail = (EditText) findViewById(R.id.et_email);
        final EditText userPassword = (EditText) findViewById(R.id.et_password);
        final Button loginButton = (Button) findViewById(R.id.bu_login);
        TextView registerUser = (TextView) findViewById(R.id.tv_register);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                Firebase ref = new Firebase("https://midwife-search.firebaseio.com/");
                ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Firebase refUser = new Firebase("https://midwife-search.firebaseio.com/Users");
                        Query queryUser = refUser.child(authData.getUid());
                        queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Toast.makeText(getApplicationContext(), "User " + email + " logged in",
                                            Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    callRegisterActivity(email, password);
                                }
                            }
                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        if (firebaseError.getCode() == FirebaseError.USER_DOES_NOT_EXIST) {
                            callRegisterActivity(email, password);
                        } else {
                            Toast.makeText(getApplicationContext(), "Fehler: \n" + firebaseError.getDetails(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRegisterActivity("", "");
            }
        });

    }

}
