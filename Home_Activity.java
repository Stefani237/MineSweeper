package com.example.dell.minesweeper;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;



public class  Home_Activity extends AppCompatActivity {
    private final int RELAX = 0;
    private final int CHALLENGE = 1;
    private final int MASTER = 2;

    final static String BUNDLE_KEY = "bundle_key";
    final static String DIFFICULTY_KEY = "difficulty_key";

    private int difficulty;
    private Button start_button;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private SharedPreferences prefRecords;
    private SharedPreferences prefDifficulty;
    private Button table_button;
    private Button map_button;
    private boolean tableOn = false;
    private boolean mapOn = false;
    private TableFragment tableFragment;
    private MapFragment mapFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        showRecords();
        selectDifficulty();
        buttonNextActivity();
    }

    public void showRecords() {
        table_button = (Button) findViewById(R.id.table_button);
        map_button = (Button) findViewById(R.id.map_button);

        table_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager tableManager = getSupportFragmentManager();
                FragmentTransaction tableTransaction = tableManager.beginTransaction();

                if(tableOn == false) {
                    tableFragment = new TableFragment();
                    tableTransaction.add(R.id.fragment_container, tableFragment);
                    tableTransaction.addToBackStack(null);
                    tableTransaction.commit();
                    tableOn = true;
                }
                else {
                    tableTransaction.remove(tableFragment);
                    tableTransaction.commit();
                    tableOn = false;
                }
            }
        });

        map_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                FragmentManager mapManager = getSupportFragmentManager();
                FragmentTransaction mapTransaction = mapManager.beginTransaction();

                if(mapOn == false) {
                    mapFragment = new MapFragment();
                    mapTransaction.add(R.id.fragment_container, mapFragment);
                    mapTransaction.addToBackStack(null);
                    mapTransaction.commit();
                    mapOn = true;
                }
                else {
                    mapTransaction.remove(mapFragment);
                    mapTransaction.commit();
                    mapOn = false;
                }
            }
        });
    }

    public void buttonNextActivity() {
        start_button =  (Button) findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBoardActivity();
            }
        });
    }

    public void startBoardActivity() { // starts Board_Activity
        Intent intent = new Intent(this, Board_Activity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(DIFFICULTY_KEY, difficulty);
        intent.putExtra(BUNDLE_KEY, bundle);
        startActivity(intent);
    }

    public void selectDifficulty() {
        prefDifficulty = getPreferences(Context.MODE_PRIVATE);

        int i = prefDifficulty.getInt(getString(R.string.pressed) ,0);
        if(RELAX == i){
            radioButton = (RadioButton) findViewById(R.id.relax_level);
            difficulty = RELAX;
        }else if(CHALLENGE == i){
            radioButton = (RadioButton) findViewById(R.id.challenge_level);
            difficulty = CHALLENGE;
        }else{
            radioButton = (RadioButton) findViewById(R.id.master_level);
            difficulty = MASTER;
        }
        radioButton.setChecked(true);

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group , int checkedId) {
                radioButton = (RadioButton) group.findViewById(checkedId);
                SharedPreferences.Editor editor = prefDifficulty.edit();

                switch (radioButton.getId()) {
                    case R.id.relax_level:
                        editor.putInt(getString(R.string.pressed),RELAX);
                        difficulty = RELAX;
                        break;

                    case R.id.challenge_level:
                        editor.putInt(getString(R.string.pressed),CHALLENGE);
                        difficulty = CHALLENGE;
                        break;

                    case R.id.master_level:
                        editor.putInt(getString(R.string.pressed),MASTER);
                        difficulty = MASTER;
                        break;
                }
                editor.commit();
            }
        });
    }


}

