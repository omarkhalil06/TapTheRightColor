package okgames.taptherightcolor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Game.java allows the user to begin playing the game
 * on a set difficulty chosen by the user.
 *
 * @author Omar Khalil
 * @version 1.0
 * @since 2017-02-09
 */

public class Game extends AppCompatActivity {

    private TextView colorName;
    private TextView scoreView;
    private TextView tapToStart;
    private RelativeLayout colorScreen;
    private ImageView checkmark;
    private CountDownTimer timer;

    private int highscore;
    private int score;
    private String difficulty;
    private String randomColor;
    private boolean running;
    private boolean correctColor;
    private boolean isReady = false;
    private boolean once;
    private long speed;
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
        highscore = b.getInt("highscore");

        tapToStart.setGravity(Gravity.CENTER);

        setup();

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

    /**
     * Checks if the color matches the name of the specified color.
     * Adds 1 to score if it is right, otherwise end the game.
     *
     * @param color Color of the screen in String format
     */
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

    /**
     * Changes the name of the color to be matched.
     *
     */
    private void changeColorText() {
        String colorToMatch = getRandomColor();
        colorName.setText(colorToMatch);
        Log.i("Color to match", colorToMatch);
    }

    /**
     * Changes the background color. If difficulty is medium, the name of the
     * color to be matched will change along with the background color. If
     * difficulty is hard, the name of the color as well as the color of the text
     * will change along with the background color.
     *
     */
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

    /**
     * Starts the game with an initial speed in which the background color
     * changes.
     *
     */
    private void startGame() {
        changeColorText();
        changeColorScreen();
        speed = 1800;
        createTimer();
    }

    /**
     * Creates a new timer with a given speed.
     *
     */
    private void createTimer() {
        timer = new CountDownTimer(speed , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!running) {
                    cancel();
                }
            }

            @Override
            public void onFinish() {
                if (running) {
                    changeColorScreen();
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

    /**
     * A color is randomly generated for the beckground color.
     *
     * @return String Color
     */
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

    /**
     * Speed change as user keeps tapping the right color, making the
     * game more difficult. Scales differently with certain difficulties.
     *
     */
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

    /**
     * Creates a new dialog containing the score and current highscore of the game.
     * User can try again or exit from the activity and return to the main menu.
     *
     */
    private void endGame() {
        running = false;
        if (score > highscore) {
            highscore = score;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Game.this);
        View view = getLayoutInflater().inflate(R.layout.gameover_dialog, null);
        TextView gameoverScore = (TextView) view.findViewById(R.id.gameoverScore);
        TextView gameoverHighscore = (TextView) view.findViewById(R.id.gameoverHighscore);
        Button tryAgainButton = (Button) view.findViewById(R.id.gameoverTryAgainButton);
        Button exitButton = (Button) view.findViewById(R.id.gameoverExitButton);

        alertDialogBuilder.setView(view);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        gameoverScore.setText(score + "");
        gameoverHighscore.setText(highscore + "");

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("EndGame", "Restarting game");
                isReady = false;
                score = 0;
                colorScreen.setBackgroundColor(Color.parseColor("WHITE"));
                colorName.setText("");
                scoreView.setText("0");
                setup();
                dialog.dismiss();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("EndGame", "Returning to main menu");
                Bundle bundle = new Bundle();
                bundle.putString("difficulty", difficulty);
                bundle.putInt("highscore", highscore);
                Intent intent = new Intent(Game.this, MainActivity.class);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
                dialog.dismiss();
            }
        });

        Log.i("EndGame", "Creating alert dialog");
    }

    /**
     * Sets up speed cap and "tap to start" text.
     *
     */
    private void setup() {
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
    }

}
