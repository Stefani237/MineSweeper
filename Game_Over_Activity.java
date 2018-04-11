package com.example.dell.minesweeper;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;


public class Game_Over_Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    final static String BUNDLE_KEY = "bundle_key";
    final static String DIFFICULTY_KEY = "difficulty_key";


    private Bundle bundle;
    private boolean isWon;
    private int difficulty;
    private boolean dbEmpty;
    private int time;
    private double lat;
    private double lng;
    private TextView text;
    private EditText editText;
    private ImageView image;
    private Button newGameBT;
    private Button chgDifficultyBT;
    private Button confirmBT;
    DatabaseHelper myDb;
    private boolean isInserted = false;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game__over_);

        extractDataFromBundle();

        myDb = new DatabaseHelper(this);
        text = (TextView) findViewById(R.id.win_or_lose_message);
        image = (ImageView) findViewById(R.id.win);
        newGameBT = (Button) findViewById(R.id.new_game);
        chgDifficultyBT = (Button) findViewById(R.id.change_difficulty);
        confirmBT = (Button) findViewById(R.id.confirm);
        editText = (EditText) findViewById(R.id.enter_name);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();


        if (isWon) {
            text.setText("You Win!");
            image.setBackgroundResource(R.drawable.win);

            Cursor cursor = myDb.getAllData(difficulty);

            if (myDb.isEmpty() || cursor.getCount() < 10) { // there is room in db
                // make entering name visible:
                editText.setVisibility(View.VISIBLE);
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                confirmBT.setVisibility(View.VISIBLE);
                addData();
            } else if (cursor.getCount() == 10) { // checks if current time is better then last time in db
                cursor.moveToLast();
                if (time < cursor.getInt(2)) {
                    myDb.deleteData(cursor.getInt(0)); // delete last row
                    editText.setVisibility(View.VISIBLE);
                    confirmBT.setVisibility(View.VISIBLE);
                    addData();
                }

            }
        } else {
            text.setText("You Lose!");
            image.setBackgroundResource(R.drawable.lose);
        }

        newGameBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Game_Over_Activity.this, Board_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(DIFFICULTY_KEY, difficulty);
                intent.putExtra(BUNDLE_KEY, bundle);
                startActivity(intent);
            }
        });

        chgDifficultyBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Game_Over_Activity.this, Home_Activity.class));
            }
        });


    }

    public void addData() { // insert player's name to database
        confirmBT.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                       // Cursor cursor = myDb.getAllData(difficulty);
                        isInserted = myDb.insertData(editText.getText().toString(), time, lat, lng, difficulty);
                        if (isInserted == true) {
                            Toast.makeText(Game_Over_Activity.this, "Record saved", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(Game_Over_Activity.this, "Record not saved", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            this.mLocation = lastLocation;
            Log.e("location: ", lastLocation.getLatitude() + ", " + lastLocation.getLongitude());
        }
        if (mLocation == null) {}

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
      //  AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    private void extractDataFromBundle() { // from Board_Activity
        Intent intent = getIntent();
        bundle = intent.getBundleExtra(Board_Activity.BUNDLE_KEY);
        isWon = bundle.getBoolean(Board_Activity.WIN_LOSE_KEY);
        difficulty = bundle.getInt(Board_Activity.DIFFICULTY_KEY);
        time = bundle.getInt(Board_Activity.TIME_KEY);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
