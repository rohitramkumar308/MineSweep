package com.project.minesweep.Activities;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.project.minesweep.R;
import com.project.minesweep.Views.MineView;

public class MainActivity extends Activity implements
        android.view.View.OnClickListener {
    private MineView mineV;
    public static TextView numMines;
    public static Activity mainActivity;
    public static TextView highScore;
    private static TextView logoText;
    public static TextView gameTimer;
    public Timer time;
    private static Typeface typeFace;
    public static int seconds = 0;
    public static int minutes = 0;
    private SharedPreferences preferences;
    private static View newButton;
    private static View settingsButton;
    private static View highScoreButton;
    private static View exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logoText = (TextView) findViewById(R.id.logoText);
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/Blox2.ttf");
        logoText.setTypeface(typeFace);
        newButton = findViewById(R.id.new_button);
        newButton.setOnClickListener(this);
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);
        exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
        highScoreButton = findViewById(R.id.high_score);
        highScoreButton.setOnClickListener(this);
        mainActivity = MainActivity.this;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // ...
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_button:
                LinearLayout linearLay = new LinearLayout(getBaseContext());
                linearLay.setOrientation(LinearLayout.VERTICAL);
                LinearLayout lnrOne = new LinearLayout(getBaseContext());
                lnrOne.setOrientation(LinearLayout.HORIZONTAL);
                Button newGame = new Button(getBaseContext());
                newGame.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));

                preferences = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                newGame.setText("New Game");
                newGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View t) {
                        mineV.randomizeMines();
                        MainActivity.seconds = 0;
                        MainActivity.minutes = 0;
                        mineV.invalidate();
                    }
                });

                mineV = new MineView(this);
                mineV.randomizeMines();
                numMines = new TextView(getBaseContext());
                highScore = new TextView(getBaseContext());
                gameTimer = new TextView(getBaseContext());
                time = new Timer();

                time.scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                gameTimer.setText("Time Elapsed  \n"
                                        + String.valueOf(minutes) + ":"
                                        + String.valueOf(seconds));
                                seconds += 1;

                                if (seconds == 60) {
                                    gameTimer.setText("Time Elapsed  \n"
                                            + String.valueOf(minutes) + ":"
                                            + String.valueOf(seconds));

                                    seconds = 00;
                                    minutes = minutes + 1;

                                }

                            }

                        });
                    }

                }, 0, 1000);

                numMines.setPadding(20, 0, 0, 0);
                int highS = preferences.getInt("highScore", 0);
                if (highS == 0) {
                    MainActivity.highScore.setText("HighScore \n" + "00:00");
                } else {

                    int min = 0;
                    int sec = 0;
                    sec = highS % 60;
                    min = highS / 60;
                    MainActivity.highScore.setText("HighScore \n"
                            + String.valueOf(min) + ":" + String.valueOf(sec));
                }
                lnrOne.addView(newGame);
                LinearLayout lnrTwo = new LinearLayout(getBaseContext());
                lnrTwo.setOrientation(LinearLayout.HORIZONTAL);
                lnrTwo.addView(gameTimer);
                lnrTwo.addView(highScore);
                lnrTwo.addView(numMines);
                lnrTwo.setBackgroundColor(Color.parseColor("#000000"));
                lnrOne.setBackgroundColor(Color.parseColor("#000000"));
                linearLay.setBackgroundColor(Color.parseColor("#000000"));
                lnrOne.addView(lnrTwo);
                linearLay.addView(lnrOne);
                linearLay.addView(mineV);
                setContentView(linearLay);
                linearLay.requestFocus();
                break;
            case R.id.high_score:
                Intent highScoreIntent = new Intent(this, HighScoreActivity.class);
                startActivity(highScoreIntent);
                break;
            case R.id.settings_button:
                Intent settingIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingIntent);
                break;

            case R.id.exit_button:
                finish();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        MainActivity.seconds = 0;
        MainActivity.minutes = 0;
        if (time != null) {
            time.cancel();
            time = null;
        }
        setContentView(R.layout.activity_main);
        logoText = (TextView) findViewById(R.id.logoText);
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/Blox2.ttf");
        logoText.setTypeface(typeFace);
        newButton = findViewById(R.id.new_button);
        newButton.setOnClickListener(this);
        exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);
        highScoreButton = findViewById(R.id.high_score);
        highScoreButton.setOnClickListener(this);
    }

}