package com.example.dell.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        new Thread(){
            public void run(){
                try {
                    sleep(2000);
                    startHomeActivity();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    public void startHomeActivity() { // starts Home_Activity
        Intent intent = new Intent(this, Home_Activity.class);
        startActivity(intent);
    }
}
