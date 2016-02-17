package de.drkalz.midwifesearch.Midwifes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import de.drkalz.midwifesearch.MainActivity;
import de.drkalz.midwifesearch.R;

public class Service extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        ImageButton addServices = (ImageButton) findViewById(R.id.ib_add);
        getSupportActionBar().hide();

        final CheckBox cb_belegeburt = (CheckBox) findViewById(R.id.cb_beleggeburt);
        final CheckBox cb_geburthge = (CheckBox) findViewById(R.id.cb_geburthge);
        final CheckBox cb_geburtsvorbereitung = (CheckBox) findViewById(R.id.cb_geburtsvorbereitung);
        final CheckBox cb_hausgeburt = (CheckBox) findViewById(R.id.cb_hausgeburt);
        final CheckBox cb_schwangerenvorsorge = (CheckBox) findViewById(R.id.cb_schwangerenvorsorge);
        final CheckBox cb_rueckbildung = (CheckBox) findViewById(R.id.cb_rueckbildung);
        final CheckBox cb_wochenbett = (CheckBox) findViewById(R.id.cb_wochenbett);
        final CheckBox cb_english = (CheckBox) findViewById(R.id.cb_englisch);
        final CheckBox cb_french = (CheckBox) findViewById(R.id.cb_french);
        final CheckBox cb_spanish = (CheckBox) findViewById(R.id.cb_spanish);
        final CheckBox cb_german = (CheckBox) findViewById(R.id.cb_german);

        final Intent i = getIntent();
        final Firebase refService = new Firebase("https://midwife-search.firebaseio.com/ServicePortfolio");

        refService.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ServicePortfolio currentPortfolio = item.getValue(ServicePortfolio.class);
                        cb_belegeburt.setChecked(currentPortfolio.isBeleggeburt());
                        cb_geburthge.setChecked(currentPortfolio.isGeburt_hge());
                        cb_geburtsvorbereitung.setChecked(currentPortfolio.isGeburtsvorbereitung());
                        cb_hausgeburt.setChecked(currentPortfolio.isHausgeburt());
                        cb_schwangerenvorsorge.setChecked(currentPortfolio.isSchwangerenvorsorge());
                        cb_rueckbildung.setChecked(currentPortfolio.isRueckbildungskurs());
                        cb_wochenbett.setChecked(currentPortfolio.isWochenbetreueung());
                        cb_english.setChecked(currentPortfolio.isEnglish());
                        cb_french.setChecked(currentPortfolio.isFrench());
                        cb_spanish.setChecked(currentPortfolio.isSpanish());
                        cb_german.setChecked(currentPortfolio.isGerman());
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        addServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServicePortfolio servicePortfolio = new ServicePortfolio();
                servicePortfolio.setBeleggeburt(cb_belegeburt.isChecked());
                servicePortfolio.setGeburt_hge(cb_geburthge.isChecked());
                servicePortfolio.setGeburtsvorbereitung(cb_geburtsvorbereitung.isChecked());
                servicePortfolio.setHausgeburt(cb_hausgeburt.isChecked());
                servicePortfolio.setRueckbildungskurs(cb_rueckbildung.isChecked());
                servicePortfolio.setWochenbetreueung(cb_wochenbett.isChecked());
                servicePortfolio.setSchwangerenvorsorge(cb_schwangerenvorsorge.isChecked());
                servicePortfolio.setEnglish(cb_english.isChecked());
                servicePortfolio.setFrench(cb_french.isChecked());
                servicePortfolio.setSpanish(cb_spanish.isChecked());
                servicePortfolio.setGerman(cb_german.isChecked());

                refService.child(i.getStringExtra("userUID")).setValue(servicePortfolio,
                        new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError == null) {
                            Toast.makeText(getApplicationContext(), "Services gespeichert!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Services NICHT gespechert! \n" + firebaseError.getDetails(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
