package de.drkalz.midwifesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import de.drkalz.midwifesearch.Midwifes.MidwifeArea;
import de.drkalz.midwifesearch.Midwifes.Service;
import de.drkalz.midwifesearch.Midwifes.SetBlockedTime;
import de.drkalz.midwifesearch.Pregnants.MapRequest;

public class MainActivity extends AppCompatActivity {

    final StartApplication sApp = StartApplication.getInstance();
    Firebase ref;

    ImageButton ibArea;
    ImageButton ibTime;
    ImageButton ibService;
    ImageButton ibSearch;
    Button buLogout;
    TextView tvUser;

    public void doSomeAction(View view) {

        int i = Integer.parseInt(view.getTag().toString());

        switch (i) {
            case 1:
                if (sApp.isMidwife()) {
                    Intent intent = new Intent(MainActivity.this, SetBlockedTime.class);
                    intent.putExtra("userUID", sApp.getAuthData().getUid());
                    startActivity(intent);
                    finish();
                } else {

                }
                break;
            case 2:
                if (sApp.isMidwife()) {
                    Intent intent = new Intent(MainActivity.this, MidwifeArea.class);
                    intent.putExtra("userUID", sApp.getAuthData().getUid());
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, MapRequest.class);
                    intent.putExtra("userUID", sApp.getAuthData().getUid());
                    startActivity(intent);
                    finish();
                }
                break;
            case 3:
                if (sApp.isMidwife()) {
                    Intent intent = new Intent(MainActivity.this, Service.class);
                    intent.putExtra("userUID", sApp.getAuthData().getUid());
                    startActivity(intent);
                    finish();
                } else {

                }
                break;

            case 4:
                if (sApp.isMidwife()) {

                } else {

                }
                break;
            case 5:
                ref.unauth();
                sApp.setAuthData(null);
                sApp.setUserEmail("");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ibArea = (ImageButton) findViewById(R.id.ib_Area);
        ibTime = (ImageButton) findViewById(R.id.ib_Abwesenheit);
        ibService = (ImageButton) findViewById(R.id.ib_Service);
        ibSearch = (ImageButton) findViewById(R.id.ib_search);
        buLogout = (Button) findViewById(R.id.bu_logout);
        tvUser = (TextView) findViewById(R.id.tv_User);
        sApp.setMidwife(false);

        Firebase.setAndroidContext(this);
        ref = new Firebase("https://midwife-search.firebaseio.com/");

        sApp.setAuthData(ref.getAuth());

        if (sApp.getAuthData() != null) {
            Firebase users = new Firebase("https://midwife-search.firebaseio.com/Users");
            Query query = users.child(sApp.getAuthData().getUid());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User currentUser = dataSnapshot.getValue(User.class);
                        tvUser.setText("- " + currentUser.getFirstname() + " " + currentUser.getLastname() + " -");
                        sApp.setUserEmail(currentUser.geteMail());
                        sApp.setFullUserName(currentUser.getFirstname() + " " + currentUser.getLastname());
                        if (currentUser.getIsMidwife() == false) {
                            sApp.setMidwife(false);
                        } else {
                            sApp.setMidwife(true);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } else {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvUser.setText("- " + sApp.getFullUserName() + " -");
    }
}
