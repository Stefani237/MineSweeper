package com.example.dell.minesweeper;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.dell.minesweeper.Logic.Game;
import com.example.dell.minesweeper.Logic.Board;
import com.example.dell.minesweeper.Logic.Tile;


public class TileAdapter extends BaseAdapter {

    private Board mBoard;
    private Game mGame;
    private Context mContext;
    private TileView tileView;

    public TileAdapter(Context context, Game game) {

        mGame = game;
        mBoard = mGame.getmBoard();
        mContext = context;

    }

    @Override
    public int getCount() {
        return mBoard.getBoardSize();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        tileView = (TileView) convertView;

        if (tileView == null) {
            tileView = new TileView(mContext);
            Log.v("Tile Adapter", "creating new view for index " + position);
        } else {
            Log.e("Tile Adapter", "RECYCLING view for index " + position);
        }

        if (mGame.ismGameOver() == true && mGame.ismWon() == false) {
            fallingTiles(tileView, position);
        }


        // pressed:
        if (mBoard.getTileByPosition(position).isPressed()) {
            if (mBoard.getTileByPosition(position).hasMine()) { // lose
                //startLoseAnimation(tileView);
                startAnimation(tileView, 0);
            } else { // number
                tileView.text.setText(mBoard.getTileByPosition(position).toString());
                tileView.setBackgroundColor(Color.WHITE);
            }
            if (mGame.ismWon()) {
                tileView.text.setText("");
                startAnimation(tileView, 1);
            }
        }
        // not pressed:
        else {
            if (mBoard.getTileByPosition(position).getIsFlagged()) {  // flagged
                tileView.setBackgroundResource(R.drawable.gold);
            } else { // close tile:
                tileView.setBackgroundColor(Color.GRAY);
                tileView.text.setText("");
            }
            if (mGame.ismWon()) {
                startAnimation(tileView, 1);
            }
        }


        return tileView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mBoard.getTileByPosition(position);
    }

    public void fallingTiles(final TileView tileView, final int position) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                if (position % 4 == 0)
                    tileView.setTranslationY(value);
                else if (position % 4 == 1)
                    tileView.setTranslationY(-value);
                else if (position % 4 == 2)
                    tileView.setTranslationX(value);
                else if (position % 4 == 3)
                    tileView.setTranslationX(-value);

                tileView.setRotation(5);
            }
        });

        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(2500);
        valueAnimator.start();
    }


    public void startAnimation(final TileView tileView, int flag) {
        // flag = 0 : lose // flag = 1 : win
        if (flag == 0)
            tileView.setBackgroundResource(R.drawable.explosion);
        else
            tileView.setBackgroundResource(R.drawable.coins);

        AnimationDrawable animationDrawable = (AnimationDrawable) tileView.getBackground();

        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
        } else {
            animationDrawable.stop();
            animationDrawable.start();
        }
    }

}
