package com.rickberenguer.fingerfishing;


import android.content.Intent;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button hiScoreButton;
    private Button playButton;
    private Button quitButton;

    int screenWidth;
    int screenHeight;

    private SoundPool soundPool;
    private int sound1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        sound1 = soundPool.load(this, R.raw.fishingtheme,1);

        //find out the width and height of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        hiScoreButton = (Button) findViewById(R.id.hiScoreButton);
        hiScoreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openHiScoreActivity();
            }
        });

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openGame_activity();
            }
        });

        quitButton = (Button) findViewById(R.id.quitButton);
        quitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
                System.exit(0);
            }
        });

        playSound();
    }

    public void playSound(){

        soundPool.play(sound1, 1, 1, 0, 0, 1);
    }

    public void openHiScoreActivity(){
        Intent intent = new Intent(this, HiScoreActivity.class);
        startActivity(intent);
    }

    public void openGame_activity(){
        Intent intent = new Intent(this, Game_activity.class);
        startActivity(intent);
    }
}
