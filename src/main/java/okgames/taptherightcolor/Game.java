package okgames.taptherightcolor;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class Game extends AppCompatActivity {

    private TextView colorName;
    private TextView scoreView;
    private TextView tapToStart;
    private RelativeLayout colorScreen;
    private ImageView checkmark;

    private int score;
    private boolean running;
    private String randomColor;
    private boolean correctColor;
    private boolean isReady = false;
    private boolean once;

    private CountDownTimer timer;
    private long speed;
    private String difficulty;
    private long cap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);

        colorName = (TextView) findViewById(R.id.colorName);
        scoreView = (TextView) findViewById(R.id.scoreCounter);
        tapToStart = (TextView) findViewById(R.id.tapStart);
        colorScreen = (RelativeLayout) findViewById(R.id.colorScreen);
        checkmark = (ImageView) findViewById(R.id.checkmark);

        Bundle b = getIntent().getExtras();
        difficulty = b.getString("difficulty");

        tapToStart.setGravity(Gravity.CENTER);

        if (difficulty.equals("easy")) {
            cap = 1000;
            tapToStart.setText("Tap To Start");
        } else if (difficulty.equals("medium")) {
            cap = 800;
            tapToStart.setText("Tap To Start");
        } else {
            cap = 600;
            tapToStart.setText("Tap Only The Right Color Name, Not The Color Of The Text\n" +
                    "Tap To Start");
        }

        colorScreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isReady && event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.i("Touch Event - Ready", "Down");
                    isReady = true;

                    timer = new CountDownTimer(3 * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            long seconds = millisUntilFinished / 1000;
                            if (seconds == 1) {
                                tapToStart.setText("Go!");
                            } else {
                                tapToStart.setText("Ready?");
                            }
                        }

                        @Override
                        public void onFinish() {
                            tapToStart.setText("");
                            correctColor = false;
                            running = true;
                            once = false;
                            startGame();
                        }
                    };

                    timer.start();
                }
                if (running) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (!once) {
                            Log.i("Touch Event", "Down");
                            handleScore(randomColor);
                        }
                    }
                }
                return false;
            }
        });
    }

    private void handleScore(String color) {
        Log.i("HandleScore", color);
        if (colorName.getText().equals(color)) {
            Log.i("HandlerScore", "Adding 1");
            score++;
            scoreView.setText(Integer.toString(score));
            correctColor = true;
            once = true;
            checkmark.setVisibility(View.VISIBLE);
        } else {
            Log.i("HandlerScore", "Incorrect color");
            endGame();
        }
    }

    private void changeColorText() {
        String colorToMatch = getRandomColor();
        colorName.setText(colorToMatch);
        Log.i("Color to match", colorToMatch);
    }

    private void changeColorScreen() {
        randomColor = getRandomColor();
        Log.i("Color name Start", randomColor);
        colorScreen.setBackgroundColor(Color.parseColor(randomColor));

        if (difficulty.equals("medium")) {
            changeColorText();
        }

        if (difficulty.equals("hard")) {
            changeColorText();
            colorName.setTextColor(Color.parseColor(getRandomColor()));
        }

    }

    private void startGame() {
        changeColorText();
        changeColorScreen();
        speed = 1800;
        createTimer();
    }

    private void createTimer() {
        timer = new CountDownTimer(speed , 1000) { // create new timer
            @Override
            public void onTick(long millisUntilFinished) {
                if (!running) {
                    cancel();
                }
            }

            @Override
            public void onFinish() {
                changeColorScreen();
                if (running) {
                    if (correctColor) {
                        changeColorText();
                        changeSpeed();
                        correctColor = false;
                        once = false;
                        checkmark.setVisibility(View.INVISIBLE);
                        createTimer();
                        Log.i("CreateTimer", speed + "");
                    } else {
                        start();
                    }
                }

            }
        };

        timer.start();
    }

    private String getRandomColor() {
        Random random = new Random();
        int num = random.nextInt(9) + 1;

        switch(num) {
            case 1:
                return "BLACK";
            case 2:
                return "BLUE";
            case 3:
                return "CYAN";
            case 4:
                return "GRAY";
            case 5:
                return "GREEN";
            case 6:
                return "CYAN";
            case 7:
                return "MAGENTA";
            case 8:
                return "RED";
            case 9:
                return "YELLOW";
        }
        return "WHITE";
    }

    private void changeSpeed() {
        if (difficulty.equals("easy")) {
            speed -= 50;
            if (speed <= cap) {
                speed = 1000;
            }
            return;
        }

        if (difficulty.equals("medium")) {
            speed -= 75;
            if (speed <= cap) {
                speed = 800;
            }
            return;
        }

        if (difficulty.equals("hard")) {
            speed -= 100;
            if (speed <= cap) {
                speed = 600;
            }
        }
    }

    private void endGame() {
        running = false;

        AlertDialog alertDialog = new AlertDialog.Builder(Game.this).create();
        alertDialog.setMessage("Game Over");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Try again",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Log.i("EndGame", "Restarting game");
                        recreate();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Exit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // pass bundle for high score?
                        Log.i("EndGame", "Returning to main menu");
                        finish();
                    }
                });
        alertDialog.show();
        Log.i("EndGame", "Creating alert dialog");
    }

}
