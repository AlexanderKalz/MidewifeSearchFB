package de.drkalz.midewifesearch.Midwifes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import de.drkalz.midewifesearch.MainActivity;
import de.drkalz.midewifesearch.R;

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

        addServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServicePortfolio servicePortfolio = new ServicePortfolio();
                final Intent i = getIntent();

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

                Firebase refService = new Firebase("https://midwife-search.firebaseio.com/ServicePortfolio");
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
