package com.rickberenguer.fingerfishing;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;

public class lostFishActivity extends AppCompatActivity {

    private Button castAgainButton;
    private Button mainMenuButton;

    int screenWidth;
    int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_fish);

        //find out the width and height of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        castAgainButton = (Button) findViewById(R.id.castAgain2);
        castAgainButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openGame_activity();
            }
        });

        mainMenuButton = (Button) findViewById(R.id.mainMenu2);
        mainMenuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openMainActivity();
            }
        });
    }

    public void openGame_activity(){
        Intent intent = new Intent(this, Game_activity.class);
        startActivity(intent);
        finish();
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
