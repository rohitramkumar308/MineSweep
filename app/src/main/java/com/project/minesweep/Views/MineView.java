package com.project.minesweep.Views;

import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.project.minesweep.Activities.MainActivity;
import com.project.minesweep.Helpers.DBHelper;
import com.project.minesweep.R;

public class MineView extends View {
    private static Context context;
    private float width; // width of one tile
    private float height; // height of one tile
    private int selX; // X index of selection
    private int selY; // Y index of selection
    public static int opRedColor = 255;
    public static int opBlueColor = 255;
    public static int opGreenColor = 255;
    public static int clRedColor = 0;
    public static int clBlueColor = 0;
    public static int clGreenColor = 0;
    public static int numRow = 16;
    public static int numCol = 16;
    private static DBHelper mydb;
    private static int mineClickedX;
    private static int mineClickedY;
    private int[][] mineMatrix;
    private int nMines;
    private boolean mineClicked = false;
    private boolean winner = false;
    private int flagCount = 0;
    GestureDetector gestureDetector;
    private Paint foreground = new Paint();

    public MineView() {
        super(context);
    }

    public MineView(Context con) {
        super(con);
        context = con;
        gestureDetector = new GestureDetector(con, new GestureListener());
        setFocusable(true);
        setFocusableInTouchMode(true);
        mydb = new DBHelper(context);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        winner = gameWon();
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        MainActivity.numMines.setText("Mines Left \n"
                + String.valueOf(flagCount));
        Cursor highCur = mydb.getData(MineView.numRow, 1);

        int highS = preferences.getInt("highScore_" + numRow, 0);

        if (highCur.getCount() == 0) {
            MainActivity.highScore.setText("HighScore \n" + "N/A");
        } else {
            highCur.moveToFirst();
            highS = highCur.getInt(highCur.getColumnIndex(mydb.COLUMN_TIME));
            int min = 0;
            int sec = 0;
            sec = highS % 60;
            min = highS / 60;
            MainActivity.highScore.setText("HighScore \n" + String.valueOf(min)
                    + ":" + String.valueOf(sec));
        }
        if (!winner) {
            width = getWidth() / numCol;
            height = getHeight() / numRow;
            // Draw the background...
            Paint background = new Paint();
            background.setColor(getResources().getColor(
                    R.color.puzzle_background));
            canvas.drawRect(0, 0, getWidth(), getHeight(), background);

            Paint dark = new Paint();
            dark.setColor(getResources().getColor(R.color.puzzle_dark));

            Paint hilite = new Paint();
            hilite.setColor(getResources().getColor(R.color.puzzle_hilite));

            Paint light = new Paint();
            light.setColor(getResources().getColor(R.color.puzzle_light));
            // Draw the major grid lines
            for (int i = 0; i < numRow; i++) {

                canvas.drawLine(0, i * height, getWidth(), i * height, dark);
            }
            for (int i = 0; i < numCol; i++) {
                canvas.drawLine(i * width, 0, i * width, getHeight(), dark);

            }
            Paint selected = new Paint();
            selected.setColor(getResources().getColor(R.color.puzzle_selected));

            foreground.setColor(Color.BLACK);
            foreground.setStyle(Style.FILL);
            foreground.setTextSize(height * 0.75f);
            foreground.setTextScaleX(width / height);
            foreground.setTextAlign(Paint.Align.CENTER);
            // Draw the number in the center of the tile
            FontMetrics fm = foreground.getFontMetrics();
            // Centering in X: use alignment (and X at midpoint)
            float x = width / 2;
            // Centering in Y: measure ascent/descent first
            float y = height / 2 - (fm.ascent + fm.descent) / 2;

            if (mineClicked) {
                for (int i = 0; i < numCol; i++) {
                    for (int j = 0; j < numRow; j++) {
                        if (mineMatrix[i][j] != -3) {
                            if (mineMatrix[i][j] == -1
                                    || mineMatrix[i][j] == -4) {
                                Bitmap bitmap = BitmapFactory.decodeResource(
                                        getResources(), R.drawable.mines);
                                if (mineClickedX == i && mineClickedY == j) {
                                    bitmap = BitmapFactory.decodeResource(
                                            getResources(), R.drawable.mines_clicked);
                                }
                                bitmap = Bitmap.createScaledBitmap(bitmap,
                                        (int) (width), (int) (height), false);
                                canvas.drawBitmap(bitmap, ((i * width)),
                                        ((j * height)), null);
                            }
                            if (mineMatrix[i][j] == -2) {
                                Bitmap bitmap = BitmapFactory.decodeResource(
                                        getResources(), R.drawable.flags);
                                bitmap = Bitmap.createScaledBitmap(bitmap,
                                        (int) (width), (int) (height), false);
                                canvas.drawBitmap(bitmap, ((i * width)),
                                        ((j * height)), null);
                            }
                            if (mineMatrix[i][j] == -3) {
                                selected.setColor(Color.rgb(opRedColor,
                                        opGreenColor, opBlueColor));
                                canvas.drawRect(i * width + 1, j * height + 1,
                                        (i * width) + width - 1, (j * height)
                                                + height - 1, selected);
                            }
                            if (mineMatrix[i][j] == 0) {
                                selected.setColor(Color.rgb(clRedColor,
                                        clGreenColor, clBlueColor));
                                canvas.drawRect(i * width + 1, j * height + 1,
                                        (i * width) + width - 1, (j * height)
                                                + height - 1, selected);
                            }
                            if (mineMatrix[i][j] != -3 && mineMatrix[i][j] != 0
                                    && mineMatrix[i][j] != -2
                                    && mineMatrix[i][j] != -1
                                    && mineMatrix[i][j] != -4) {
                                selected.setColor(Color.rgb(opRedColor,
                                        opGreenColor, opBlueColor));
                                canvas.drawRect(i * width + 1, j * height + 1,
                                        (i * width) + width - 1, (j * height)
                                                + height - 1, selected);
                                switch (mineMatrix[i][j]) {
                                    case 1:
                                        foreground.setColor(Color.BLUE);
                                        canvas.drawText(
                                                String.valueOf(mineMatrix[i][j]),
                                                ((i * width) + x),
                                                ((j * height) + y), foreground);
                                        break;
                                    case 2:
                                        foreground.setColor(Color.GREEN);
                                        canvas.drawText(
                                                String.valueOf(mineMatrix[i][j]),
                                                ((i * width) + x),
                                                ((j * height) + y), foreground);
                                        break;
                                    case 3:
                                        foreground.setColor(Color.BLACK);
                                        canvas.drawText(
                                                String.valueOf(mineMatrix[i][j]),
                                                ((i * width) + x),
                                                ((j * height) + y), foreground);
                                        break;
                                    case 4:
                                        foreground.setColor(Color.RED);
                                        canvas.drawText(
                                                String.valueOf(mineMatrix[i][j]),
                                                ((i * width) + x),
                                                ((j * height) + y), foreground);
                                        break;
                                    case 5:
                                        foreground.setColor(Color.MAGENTA);
                                        canvas.drawText(
                                                String.valueOf(mineMatrix[i][j]),
                                                ((i * width) + x),
                                                ((j * height) + y), foreground);
                                        break;

                                }

                            }
                        } else {
                            selected.setColor(Color.rgb(opRedColor,
                                    opGreenColor, opBlueColor));
                            canvas.drawRect(i * width + 1, j * height + 1,
                                    (i * width) + width - 1, (j * height)
                                            + height - 1, selected);
                        }

                    }
                }
                Vibrator vibrate = (Vibrator) this.context
                        .getSystemService(Context.VIBRATOR_SERVICE);
                // // Vibrate for 500 milliseconds
                vibrate.vibrate(500);
            } else {

                for (int i = 0; i < numCol; i++) {
                    for (int j = 0; j < numRow; j++) {
                        if (mineMatrix[i][j] == 0 || mineMatrix[i][j] == -1) {
                            selected.setColor(Color.rgb(clRedColor,
                                    clGreenColor, clBlueColor));
                            canvas.drawRect(i * width + 1, j * height + 1,
                                    (i * width) + width - 1, (j * height)
                                            + height - 1, selected);
                        }
                        if (mineMatrix[i][j] != -3 && mineMatrix[i][j] != 0
                                && mineMatrix[i][j] != -2
                                && mineMatrix[i][j] != -1
                                && mineMatrix[i][j] != -4) {
                            selected.setColor(Color.rgb(opRedColor,
                                    opGreenColor, opBlueColor));
                            canvas.drawRect(i * width + 1, j * height + 1,
                                    (i * width) + width - 1, (j * height)
                                            + height - 1, selected);

                            switch (mineMatrix[i][j]) {
                                case 1:
                                    foreground.setColor(Color.BLUE);
                                    canvas.drawText(
                                            String.valueOf(mineMatrix[i][j]),
                                            ((i * width) + x), ((j * height) + y),
                                            foreground);
                                    break;
                                case 2:
                                    foreground.setColor(Color.GREEN);
                                    canvas.drawText(
                                            String.valueOf(mineMatrix[i][j]),
                                            ((i * width) + x), ((j * height) + y),
                                            foreground);
                                    break;
                                case 3:
                                    foreground.setColor(Color.BLACK);
                                    canvas.drawText(
                                            String.valueOf(mineMatrix[i][j]),
                                            ((i * width) + x), ((j * height) + y),
                                            foreground);
                                    break;
                                case 4:
                                    foreground.setColor(Color.RED);
                                    canvas.drawText(
                                            String.valueOf(mineMatrix[i][j]),
                                            ((i * width) + x), ((j * height) + y),
                                            foreground);
                                    break;
                                case 5:
                                    foreground.setColor(Color.MAGENTA);
                                    canvas.drawText(
                                            String.valueOf(mineMatrix[i][j]),
                                            ((i * width) + x), ((j * height) + y),
                                            foreground);
                                    break;

                            }

                        }
                        if (mineMatrix[i][j] == -2 || mineMatrix[i][j] == -4) {

                            Bitmap bitmap = BitmapFactory.decodeResource(
                                    getResources(), R.drawable.flags);
                            bitmap = Bitmap.createScaledBitmap(bitmap,
                                    (int) (width), (int) (height), false);
                            canvas.drawBitmap(bitmap, ((i * width)),
                                    ((j * height)), null);

                        }

                        if (mineMatrix[i][j] == -3) {
                            selected.setColor(Color.rgb(opRedColor,
                                    opGreenColor, opBlueColor));
                            canvas.drawRect(i * width + 1, j * height + 1,
                                    (i * width) + width - 1, (j * height)
                                            + height - 1, selected);
                        }

                    }
                }
            }

        }
        String message;

        if (winner) {
            message = "Congratulations. You WON";
            setHighScore(numRow, MainActivity.minutes * 60
                    + MainActivity.seconds);
            if (highS > MainActivity.minutes * 60 + MainActivity.seconds) {
                Editor editor = preferences.edit();
                editor.putInt("highScore_" + numRow, MainActivity.minutes * 60
                        + MainActivity.seconds);
                editor.commit();
            } else {
                if (highS == 0) {
                    Editor editor = preferences.edit();
                    editor.putInt("highScore_" + numRow, MainActivity.minutes
                            * 60 + MainActivity.seconds);
                    editor.commit();
                }
            }

        } else {
            message = "Sorry. You lost.";
        }
        if ((winner || mineClicked)) {
            // set the title of the Alert Dialog
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            if (winner) {
                alertDialogBuilder.setIcon(R.drawable.happyface);
            } else {
                alertDialogBuilder.setIcon(R.drawable.sadface);
            }

            alertDialogBuilder.setTitle("New Game ?");
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setMessage(message)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    randomizeMines();
                                    MainActivity.seconds = 0;
                                    MainActivity.minutes = 0;
                                    invalidate();
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {

                                    dialog.cancel();
                                }
                            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

        }
    }

    public void randomizeMines() {
        mineClicked = false;
        winner = false;
        mineMatrix = new int[numCol][numRow];
        flagCount = 0;
        nMines = 0;
        Random randomGenerator = new Random();

        for (int i = 0; i < numCol; i++) {
            for (int j = 0; j < numRow; j++) {
                mineMatrix[i][j] = 0;
            }
        }
        for (int i = 0; i < numCol * numRow * 0.2; i++) {

            int xCoord = randomGenerator.nextInt(numCol - 1);
            int yCoord = randomGenerator.nextInt(numRow - 1);
            if (mineMatrix[xCoord][yCoord] == 0) {
                mineMatrix[xCoord][yCoord] = -1;
                nMines++;
            }
        }

        flagCount = nMines;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        selX = (int) Math.min(Math.max((event.getX() / width), 0), 15);
        selY = (int) Math.min(Math.max((event.getY() / height), 0), 15);

        gestureDetector.onTouchEvent(event);

        return true;
    }

    private void recursiveNumDisp(int xCoord, int yCoord) {
        if (mineMatrix[xCoord][yCoord] != -3
                && mineMatrix[xCoord][yCoord] != -2) {
            int cellCount = 0, startX, endX, startY, endY;
            if ((xCoord >= 0 && xCoord <= (numCol - 1))
                    && (yCoord >= 0 && yCoord <= (numRow - 1))) {

                if (xCoord - 1 < 0) {
                    startX = xCoord;
                } else {
                    startX = xCoord - 1;
                }
                if (xCoord + 1 > (numCol - 1)) {
                    endX = xCoord;
                } else {
                    endX = xCoord + 1;
                }
                if (yCoord - 1 < 0) {
                    startY = yCoord;
                } else {
                    startY = yCoord - 1;
                }
                if (yCoord + 1 > (numRow - 1)) {
                    endY = yCoord;
                } else {
                    endY = yCoord + 1;
                }

                for (int i = startX; i <= endX; i++) {
                    for (int j = startY; j <= endY; j++) {
                        if (mineMatrix[i][j] == -1 || mineMatrix[i][j] == -4) {
                            cellCount++;
                        }
                    }
                }
                if (cellCount == 0) {
                    mineMatrix[xCoord][yCoord] = -3;
                    for (int i = startX; i <= endX; i++) {
                        for (int j = startY; j <= endY; j++) {
                            recursiveNumDisp(i, j);
                        }
                    }
                } else {
                    mineMatrix[xCoord][yCoord] = cellCount;

                }

            }
        }

    }

    private boolean gameWon() {

        for (int i = 0; i < numCol; i++) {
            for (int j = 0; j < numRow; j++) {
                if (mineMatrix[i][j] == 0 || mineMatrix[i][j] == -2) {
                    return false;
                }
            }
        }
        return true;
    }

    private class GestureListener extends
            GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (!mineClicked && !gameWon()) {
                if (mineMatrix[selX][selY] == -1) {
                    mineClicked = true;
                    mineClickedX = selX;
                    mineClickedY = selY;
                } else {
                    recursiveNumDisp(selX, selY);

                }
            }
            invalidate();
            return true;
        }

        // event when single tap occurs
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (!mineClicked && !gameWon()) {
                if (mineMatrix[selX][selY] == -2) {
                    flagCount++;
                    mineMatrix[selX][selY] = 0;
                } else if (mineMatrix[selX][selY] == -4) {
                    flagCount++;
                    mineMatrix[selX][selY] = -1;
                } else if (mineMatrix[selX][selY] == -1) {
                    flagCount--;
                    mineMatrix[selX][selY] = -4;
                } else {
                    if (mineMatrix[selX][selY] == 0) {
                        mineMatrix[selX][selY] = -2;
                        flagCount--;
                    }

                }
            }
            winner = gameWon();
            invalidate();
            return true;
        }
    }

    private void setHighScore(int gridSize, int time) {

        Cursor checkHighScore = mydb.getData(gridSize);
        int place = 1;
        int tmpTime = 0;
        if (checkHighScore.getCount() == 0) {
            mydb.insertHighScore(gridSize, 1, time);
        } else {
            checkHighScore.moveToFirst();
            while (!checkHighScore.isAfterLast()) {
                tmpTime = checkHighScore.getInt(checkHighScore.getColumnIndex(mydb.COLUMN_TIME));
                if (time < tmpTime) {
                    place = checkHighScore.getInt(checkHighScore.getColumnIndex(mydb.COLUMN_PLACE));
                    mydb.updateHighScore(gridSize, place, time);
                    break;
                }
                checkHighScore.moveToNext();
            }
            if (checkHighScore.getCount() < 5) {
                mydb.insertHighScore(gridSize, checkHighScore.getCount() + 1, time);

            }
            mydb.close();
        }


    }


}
