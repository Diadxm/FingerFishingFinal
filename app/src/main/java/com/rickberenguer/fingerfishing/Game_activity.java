package com.rickberenguer.fingerfishing;

import android.graphics.Point;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.ImageView;


public class Game_activity extends AppCompatActivity {

    ////Erick-Hobbs///
    /////////////////
    int screenWidth;
    int screenHeight;
    ImageView fishingPoleImage;

    //used to display swipe or tap status info
    private TextView textView = null;
    //gesture detector compat instance
    private GestureDetectorCompat gestureDetectorCompat = null;
    //////////////////////
    /////////////////////

    TextView fishText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_activity);

        ////Erick-Hobbs///
        /////////////////

        //find out the width and height of the screen//
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // get text view
        textView = (TextView)findViewById(R.id.swipe_direction);

        // gesture listener object
        DetectSwipeGestureListener gestureListener = new DetectSwipeGestureListener();
        // Set activity in the listener
        gestureListener.setActivity(this);

        //gesture detector with the gesture listener
        gestureDetectorCompat = new GestureDetectorCompat(this, gestureListener);

        ////////////////
        ////////////////

        fishText = (TextView)findViewById(R.id.FishName);
        Object fish = new Fish();
        ((Fish) fish).CreateFish();
        fishText.setText(((Fish) fish).NameOfFish());
    }

    ////Erick-Hobbs///
    /////////////////
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // pass activity on touch event to the gesture detector
        gestureDetectorCompat.onTouchEvent(event);
        // return true to tell OS event has been consumed, do not pass to other event listeners
        return true;
    }

    public void displayMessage(String message)
    {
        if(textView!=null)
        {
            // Display text in the text view.
            textView.setText(message);
        }
    }
    ////////////////
    ////////////////
}
