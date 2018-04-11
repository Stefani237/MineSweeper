package com.example.dell.minesweeper;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class TableFragment extends Fragment {
    private final int NAME_COLUMN = 1;
    private final int TIME_COLUMN = 2;
    private final int LATITUDE_COLUMN = 4;
    private final int LONGITUDE_COLUMN = 5;

    private final int RELAX = 0;
    private final int CHALLENGE = 1;
    private final int MASTER = 2;

    private final int WIDTH = 40;
    private final int HIGHT = 8;



    private DatabaseHelper mDatabaseHelper;
    private Cursor cursor;
    private int mDifficulty = 0;
    private View view;
    private TableLayout titles;
    private TableLayout tableLayout;
    private RadioGroup radioGroup;
    private RadioButton radioButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_table, container, false);
        tableLayout = (TableLayout) view.findViewById(R.id.record_table);
        titles = (TableLayout) view.findViewById(R.id.table_titles);
        radioButton = (RadioButton) view.findViewById(R.id.relax_table);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group_table);


        mDatabaseHelper = new DatabaseHelper(getContext());
        cursor = mDatabaseHelper.getAllData(mDifficulty);
        //
        createDatabaseTable();
        selectRecordTable();



        return view;
    }

    public TableFragment() {
        // Required empty public constructor
    }


    private void setTextView(TextView textView, int flag) { // 0 = name; 1 = time
        if(flag == 0) {
            String name = cursor.getString(NAME_COLUMN);
            textView.setText(name);
            //textView.setPadding(WIDTH - name.length()/2, HIGHT, WIDTH - name.length(), HIGHT);
        }
        else {
            int time = cursor.getInt(TIME_COLUMN);
            textView.setText(String.format("%02d:%02d", time / 60, time % 60));
           // textView.setPadding(WIDTH, HIGHT, WIDTH, HIGHT);
        }
    }

    public void selectRecordTable() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // int selectedButton = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) group.findViewById(checkedId);

                switch (radioButton.getId()) {
                    case R.id.relax_table:
                        mDifficulty = RELAX;
                        break;

                    case R.id.challenge_table:
                        mDifficulty = CHALLENGE;
                        break;

                    case R.id.master_table:
                        mDifficulty = MASTER;
                        break;

                    default:
                        mDifficulty = RELAX;
                }

                tableLayout.removeAllViewsInLayout();
                cursor = mDatabaseHelper.getAllData(mDifficulty);
                createDatabaseTable();
            }

        });
    }

    private void createDatabaseTable() {
        int count = 1;
        while(cursor.moveToNext()) {
            TableRow tableRow = new TableRow(getActivity().getApplicationContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            // add number:
            TextView textView = new TextView(getActivity().getApplicationContext());
            textView.setText(count + ".");
            tableRow.addView(textView);
            count++;

            // add name:
            TextView name = new TextView(getActivity().getApplicationContext());
            name.setPadding(WIDTH - "Null".length()/2, HIGHT, WIDTH - "Null".length()/2, HIGHT);
            setTextView(name, 0);
            tableRow.addView(name);

            // add time:
            TextView time = new TextView(getActivity().getApplicationContext());
            time.setPadding(WIDTH - "Null".length()/2, HIGHT, WIDTH - "Null".length()/2, HIGHT);
            setTextView(time, 1);
            tableRow.addView(time);

            // add location:

            TextView location = new TextView(getActivity().getApplicationContext());
            location.setPadding(WIDTH - "Null".length()/2, HIGHT, WIDTH - "Null".length()/2, HIGHT);
            String x = cursor.getInt(LATITUDE_COLUMN) + " ";
            String y = cursor.getInt(LONGITUDE_COLUMN) + "";
            location.setText(x + y);
            tableRow.addView(location);


            tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        }

    }

}