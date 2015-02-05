package com.project.minesweep.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;

import com.jesusm.holocircleseekbar.HoloCircleSeekBar;
import com.project.minesweep.R;
import com.project.minesweep.Views.MineView;

public class SettingsActivity extends Activity {
    private int redColor;
    private int blueColor;
    private int greenColor;
    private static HoloCircleSeekBar picker;
    private static TextView colorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        picker = (HoloCircleSeekBar) findViewById(R.id.picker);
        picker.getValue();
        View someView = findViewById(R.id.settingsLayout);
        colorText = (TextView) findViewById(R.id.colorText);
        // Find the root view
        final SeekBar redBar = (SeekBar) findViewById(R.id.seekBar1);
        final SeekBar greenBar = (SeekBar) findViewById(R.id.seekBar2);
        final SeekBar blueBar = (SeekBar) findViewById(R.id.seekBar3);
        final Switch colorSwitch = (Switch) findViewById(R.id.setcolor);
        colorSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    colorSwitch.setText("Open");
                    greenBar.setProgress(MineView.opGreenColor);
                    redBar.setProgress(MineView.opRedColor);
                    blueBar.setProgress(MineView.opBlueColor);
                    colorText.setBackgroundColor(Color.rgb(redColor,
                            greenColor, blueColor));

                } else {
                    colorSwitch.setText("Close");
                    greenBar.setProgress(MineView.clGreenColor);
                    redBar.setProgress(MineView.clRedColor);
                    blueBar.setProgress(MineView.clBlueColor);
                    colorText.setBackgroundColor(Color.rgb(redColor,
                            greenColor, blueColor));
                }
            }
        });
        redBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                redColor = progress;
                colorText.setBackgroundColor(Color.rgb(redColor,
                        greenColor, blueColor));
                if (colorSwitch.isChecked())
                    MineView.opRedColor = progress;
                else
                    MineView.clRedColor = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                redColor = seekBar.getProgress();
                colorText.setBackgroundColor(Color.rgb(redColor,
                        greenColor, blueColor));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                redColor = seekBar.getProgress();
                colorText.setBackgroundColor(Color.rgb(redColor,
                        greenColor, blueColor));
            }
        });

        blueBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                blueColor = seekBar.getProgress();
                colorText.setBackgroundColor(Color.rgb(redColor,
                        greenColor, blueColor));
                if (colorSwitch.isChecked())
                    MineView.opBlueColor = progress;
                else
                    MineView.clBlueColor = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                blueColor = seekBar.getProgress();
                colorText.setBackgroundColor(Color.rgb(redColor,
                        greenColor, blueColor));

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                blueColor = seekBar.getProgress();
                colorText.setBackgroundColor(Color.rgb(redColor,
                        greenColor, blueColor));

            }
        });

        greenBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                greenColor = seekBar.getProgress();
                colorText.setBackgroundColor(Color.rgb(redColor,
                        greenColor, blueColor));
                if (colorSwitch.isChecked())
                    MineView.opGreenColor = progress;
                else
                    MineView.clGreenColor = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                greenColor = seekBar.getProgress();
                colorText.setBackgroundColor(Color.rgb(redColor,
                        greenColor, blueColor));

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                greenColor = seekBar.getProgress();
                colorText.setBackgroundColor(Color.rgb(redColor,
                        greenColor, blueColor));
            }
        });

//        gridSize.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal,
//                                      int newVal) {
//                // do something here
//                gridText.setText(newVal + " x " + newVal);
//                MineView.numRow = newVal;
//                MineView.numCol = newVal;
//            }
//        });

        if (colorSwitch.isChecked()) {
            colorSwitch.setText("Open");
            greenBar.setProgress(MineView.opGreenColor);
            redBar.setProgress(MineView.opRedColor);
            blueBar.setProgress(MineView.opBlueColor);
            colorText.setBackgroundColor(Color.rgb(redColor,
                    greenColor, blueColor));
        } else {
            colorSwitch.setText("Close");
            greenBar.setProgress(MineView.clGreenColor);
            redBar.setProgress(MineView.clRedColor);
            blueBar.setProgress(MineView.clBlueColor);
            colorText.setBackgroundColor(Color.rgb(redColor,
                    greenColor, blueColor));
        }

    }

    @Override
    public void onBackPressed() {
        MineView.numRow = picker.getValue();
        MineView.numCol = picker.getValue();
        finish();
    }
}

