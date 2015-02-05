package com.project.minesweep.Activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import com.project.minesweep.Helpers.DBHelper;
import com.project.minesweep.R;
import com.project.minesweep.Views.MineView;

/**
 * Created by rohitramkumar on 10/9/14.
 */
public class HighScoreActivity extends Activity {

    private static DBHelper myDB;
    private TextView firstPlace;
    private TextView secondPlace;
    private TextView thirdPlace;
    private TextView fourthPlace;
    private TextView fifthPlace;
    private TextView gridSizeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);
        myDB = new DBHelper(this);
        firstPlace = (TextView) findViewById(R.id.firstPlace);
        secondPlace = (TextView) findViewById(R.id.secondPlace);
        thirdPlace = (TextView) findViewById(R.id.thirdPlace);
        fourthPlace = (TextView) findViewById(R.id.fourthPlace);
        fifthPlace = (TextView) findViewById(R.id.fifthPlace);
        gridSizeText = (TextView) findViewById(R.id.gridSizeText);
        gridSizeText.setText("Grid Size: " + MineView.numRow + " x " + MineView.numRow);
        Cursor cur = myDB.getData(MineView.numRow);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                int place = cur.getInt(cur.getColumnIndex(myDB.COLUMN_PLACE));
                int tmpTime = cur.getInt(cur.getColumnIndex(myDB.COLUMN_TIME));
                String time = String.valueOf(tmpTime / 60) + ":" + String.valueOf(tmpTime % 60);
                switch (place) {
                    case 1:
                        firstPlace.setText(time);
                        break;
                    case 2:
                        secondPlace.setText(time);
                        break;
                    case 3:
                        thirdPlace.setText(time);
                        break;
                    case 4:
                        fourthPlace.setText(time);
                        break;
                    case 5:
                        fifthPlace.setText(time);
                        break;
                }

                cur.moveToNext();
            }

        }


    }


}
