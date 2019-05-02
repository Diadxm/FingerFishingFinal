package com.rickberenguer.fingerfishing;

import android.graphics.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class Game_activity extends AppCompatActivity {

    TextView fishText;

    public Camera cam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_activity);

        fishText = (TextView)findViewById(R.id.FishName);

        Object fish = new Fish();

        ((Fish) fish).CreateFish();

        fishText.setText(((Fish) fish).NameOfFish());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
