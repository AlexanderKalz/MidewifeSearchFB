package de.drkalz.midwifesearch.Midwifes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.drkalz.midwifesearch.MainActivity;
import de.drkalz.midwifesearch.R;

public class SetBlockedTime extends AppCompatActivity {

    final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    ArrayList<String> savedBlock = new ArrayList<>();
    ArrayList<String> savedUID = new ArrayList<>();
    TextView headline;
    ArrayAdapter arrayAdapter;
    ListView showBlockedDates;
    EditText startDate, stopDate;
    Button addBlock;
    ImageButton saveButton, endActivity;
    Date startOfBlock, endOfBlock;
    Firebase ref;
    boolean addItem;
    int positionOfItem;

    private void createDate() {
        try {
            startOfBlock = sdf.parse(startDate.getText().toString());
            endOfBlock = sdf.parse(stopDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void saveItem(View view) {
        if (addItem) {
            updateDatabase(1);
        } else {
            updateDatabase(2);
        }
        startDate.setVisibility(View.INVISIBLE);
        startDate.setText("");
        stopDate.setVisibility(View.INVISIBLE);
        stopDate.setText("");
        addBlock.setVisibility(View.VISIBLE);
        headline.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        arrayAdapter.notifyDataSetChanged();
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void updateDatabase(int toDo) {
        BlockedTime newBlock = new BlockedTime();

        switch (toDo) {
            // Create item
            case 1:
                createDate();
                newBlock.setStartOfBlock(startOfBlock);
                newBlock.setEndOfBlock(endOfBlock);
                ref.push().setValue(newBlock, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError == null) {
                            Toast.makeText(getApplicationContext(), "gespeichert", Toast.LENGTH_LONG).show();
                            savedBlock.add("Start: " + sdf.format(startOfBlock) + " - Ende: " + sdf.format(endOfBlock));
                            savedUID.add(firebase.getKey());
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                });
                break;
            // update item
            case 2:
                createDate();
                newBlock.setStartOfBlock(startOfBlock);
                newBlock.setEndOfBlock(endOfBlock);
                Firebase updateRef = ref.child(savedUID.get(positionOfItem));
                updateRef.setValue(newBlock);
                updateRef.child("endOfBlock").setValue(endOfBlock);
                savedBlock.set(positionOfItem, "Start: " + sdf.format(startOfBlock) + " - Ende: " + sdf.format(endOfBlock));
                arrayAdapter.notifyDataSetChanged();
                break;
            // delete item
            case 3:
                ref.child(savedUID.get(positionOfItem)).removeValue();
                savedBlock.remove(positionOfItem);
                savedUID.remove(positionOfItem);
                arrayAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_blocked_time);

        Intent inputIntent = getIntent();

        headline = (TextView) findViewById(R.id.tv_headline);
        showBlockedDates = (ListView) findViewById(R.id.lv_BlockedDates);
        startDate = (EditText) findViewById(R.id.et_start);
        stopDate = (EditText) findViewById(R.id.et_stop);
        addBlock = (Button) findViewById(R.id.bu_addTime);
        saveButton = (ImageButton) findViewById(R.id.ib_Save);
        saveButton.setVisibility(View.INVISIBLE);
        endActivity = (ImageButton) findViewById(R.id.ib_goBack);

        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, savedBlock);
        showBlockedDates.setAdapter(arrayAdapter);

        ref = new Firebase("https://midwife-search.firebaseio.com/BlockedTime").child(inputIntent.getStringExtra("userUID"));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot items : dataSnapshot.getChildren()) {
                        BlockedTime newItem = items.getValue(BlockedTime.class);
                        savedBlock.add("Start: " + sdf.format(newItem.getStartOfBlock()) + " - Ende: " + sdf.format(newItem.getEndOfBlock()));
                        savedUID.add(items.getKey());
                    }
                } else {
                    savedBlock.clear();
                    savedUID.clear();
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "Fehler: " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        showBlockedDates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Query queryRef = ref.orderByKey().startAt(savedUID.get(position)).endAt(savedUID.get(position));
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                BlockedTime blockedTime = item.getValue(BlockedTime.class);
                                startOfBlock = blockedTime.getStartOfBlock();
                                endOfBlock = blockedTime.getEndOfBlock();
                            }
                            startDate.setText(sdf.format(startOfBlock));
                            stopDate.setText(sdf.format(endOfBlock));
                            headline.setVisibility(View.INVISIBLE);
                            startDate.setVisibility(View.VISIBLE);
                            stopDate.setVisibility(View.VISIBLE);
                            addBlock.setVisibility(View.INVISIBLE);
                            saveButton.setVisibility(View.VISIBLE);
                            addItem = false;
                            positionOfItem = position;
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        });

        showBlockedDates.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                positionOfItem = position;
                updateDatabase(3);
                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });

        addBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headline.setVisibility(View.INVISIBLE);
                startDate.setVisibility(View.VISIBLE);
                stopDate.setVisibility(View.VISIBLE);
                addBlock.setVisibility(View.INVISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                addItem = true;
            }
        });

        endActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
