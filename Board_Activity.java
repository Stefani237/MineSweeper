


package com.example.dell.minesweeper;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.minesweeper.Logic.Game;

import java.util.Timer;
import java.util.TimerTask;



public class Board_Activity extends AppCompatActivity implements ServiceMotion.MotionListener{
    static String BUNDLE_KEY = "bundle_key";
    static String WIN_LOSE_KEY = "win";
    static String DIFFICULTY_KEY = "difficulty";
    static String TIME_KEY = "time";

    private boolean gameStarted = false;
    private GridView gridView;
    private Game game;
    private Bundle bundle;
    private int difficulty;
    private TextView TV_timer;
    private TextView TV_mines;
    private int timeCounter = 0;
    private Timer timer;
    private static int minesCount;
    private MediaPlayer mediaPlayer;
    private ServiceMotion.LocalBinder mLocalBinder;
    private boolean isBound = false;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLocalBinder = (ServiceMotion.LocalBinder)service;
            mLocalBinder.registerListener(Board_Activity.this);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            isBound = false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_);

        extractDataFromBundle();
        timerStart();
        game = new Game(difficulty);
        minesCount = game.getmBoard().getNumOfMines();
        TV_mines = (TextView) findViewById(R.id.mine_num);
        TV_mines.setText("X" + minesCount);
        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setNumColumns(game.getmBoard().getmCols());
        gridView.setAdapter(new TileAdapter(getApplicationContext(), game));
        startService();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!game.getmBoard().getTile(position / game.getmBoard().getmCols(), position % game.getmBoard().getmRows()).getIsFlagged()) {
                    game.play(position);
                    gameStarted = true;

                }
                if (game.ismGameOver()) {
                    if(!game.ismWon())
                        playSound(0);
                    else
                        playSound(1);
                    new Thread() {
                        public void run() {
                            try {
                                timer.cancel();
                                sleep(2000);
                                startGameOverActivity();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }.start();

                }
                ((TileAdapter)gridView.getAdapter()).notifyDataSetChanged();

            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                game.getmBoard().toggleFlagMark(position/ game.getmBoard().getmCols(), position% game.getmBoard().getmRows());
                ((TileAdapter)gridView.getAdapter()).notifyDataSetChanged();
                return true;
            }
        });

    }
    @Override
    protected void onResume() {
        Intent intent = new Intent(this, ServiceMotion.class);
        this.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (isBound){
            unbindService(mServiceConnection);
            isBound = false;
        }
        super.onPause();
    }

    private void startService() {
        Intent intent = new Intent(this, ServiceMotion.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }


    private void playSound(int flag) {
        if(flag == 0) // lose
            mediaPlayer = MediaPlayer.create(Board_Activity.this, R.raw.explosionsound);
        else // win
            mediaPlayer = MediaPlayer.create(Board_Activity.this, R.raw.jackpotsound);

        mediaPlayer.start();
    }



    private void extractDataFromBundle() { // from Home_Activity
        Intent intent = getIntent();
        bundle = intent.getBundleExtra(Home_Activity.BUNDLE_KEY);
        difficulty = bundle.getInt(Home_Activity.DIFFICULTY_KEY);
        Toast.makeText(this, "Long press to mark bomb", Toast.LENGTH_LONG).show();
    }

    public void startGameOverActivity() { // starts Game_Over_Activity
        Intent intent = new Intent(this, Game_Over_Activity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(WIN_LOSE_KEY, game.ismWon());
        bundle.putInt(DIFFICULTY_KEY, difficulty);
        bundle.putInt(TIME_KEY, timeCounter);
        intent.putExtra(BUNDLE_KEY, bundle);
        timer.cancel();
        startActivity(intent);

    }

    public void timerStart() {

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            TV_timer = (TextView) findViewById(R.id.Timer);
                            TV_timer.setText(String.format("%02d:%02d", timeCounter / 60, timeCounter % 60));
                            timeCounter++;
                        }
                    });
                }
            }, 1000, 1000);



    }


    public void positionChanged() {
            Toast.makeText(this, "WARNING! Mines are added, return to the initial position.", Toast.LENGTH_LONG).show();

            if (game.boardFullOfMines() == false) {
                game.getmBoard().addMoreMine();
                minesCount++;
                TV_mines.setText("X" + minesCount);
            }
            else {
                return;

            }


            if (game.getmBoard().allTilesClosed() == false) {
                game.getmBoard().closeTile();

            }
            else {
                if(game.ismWon()) {
                  getApplicationContext().unbindService(mServiceConnection);
                }
                return;
            }

            ((TileAdapter) gridView.getAdapter()).notifyDataSetChanged();

    }




}
