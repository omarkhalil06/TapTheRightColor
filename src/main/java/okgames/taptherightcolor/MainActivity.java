package okgames.taptherightcolor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button easy;
    private Button medium;
    private Button hard;
    private TextView title;

    private int easyHighscore;
    private int mediumHighscore;
    private int hardHighscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // you can change to have loading screen in beginning by ...
        // setContentView(R.layout.loading_screen);
        setContentView(R.layout.activity_main);

        title = (TextView) findViewById(R.id.gameTitle);
        title.setTextColor(Color.BLACK);

        easy = (Button) findViewById(R.id.easyButton);
        medium = (Button) findViewById(R.id.mediumButton);
        hard = (Button) findViewById(R.id.hardButton);

        easyHighscore = 0;
        mediumHighscore = 0;
        hardHighscore = 0;

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("difficulty", "easy");
                bundle.putInt("highscore", easyHighscore);
                Intent intent = new Intent(MainActivity.this, Game.class);
                intent.putExtras(bundle);
                Log.i("Button pressed", "Easy");
                startActivityForResult(intent, 1);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("difficulty", "medium");
                bundle.putInt("highscore", mediumHighscore);
                Intent intent = new Intent(MainActivity.this, Game.class);
                intent.putExtras(bundle);
                Log.i("Button pressed", "Medium");
                startActivityForResult(intent, 1);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("difficulty", "hard");
                bundle.putInt("highscore", hardHighscore);
                Intent intent = new Intent(MainActivity.this, Game.class);
                intent.putExtras(bundle);
                Log.i("Button pressed", "hard");
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("request code", requestCode + "");
        Log.i("result code", resultCode + "");
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String difficulty = data.getStringExtra("difficulty");
                int score = data.getIntExtra("highscore", 1);
                Log.i("difficulty", difficulty);
                Log.i("new highscore", score + "");
                if (difficulty.equals("easy")) {
                    easyHighscore = score;
                } else if (difficulty.equals("medium")) {
                    mediumHighscore = score;
                } else {
                    hardHighscore = score;
                }
            }
        }
    }
}
