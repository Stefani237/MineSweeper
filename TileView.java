package com.example.dell.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TileView extends LinearLayout{

    public TextView text;

    public TileView(Context context) {
        super(context);

        this.setOrientation(VERTICAL);

        text = new TextView(context);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        text.setLayoutParams(layoutParams);


        text.setTextSize(20);
        text.setTextColor(Color.BLACK);
        text.setGravity(Gravity.CENTER);


        this.addView(text);

    }
}
