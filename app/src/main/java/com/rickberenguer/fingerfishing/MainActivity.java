package com.rickberenguer.fingerfishing;


import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button hiScoreButton;
    private Button playButton;

    int screenWidth;
    int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
