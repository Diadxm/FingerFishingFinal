package com.rickberenguer.fingerfishing;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

public class DetectSwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

    ImageView fishingPoleImage;
    //min x axis swipe distance
    private static int MIN_SWIPE_DISTANCE_X = 50;
    //max x axis swipe distance
    private static int MAX_SWIPE_DISTANCE_X = 2000;
    //source activity that display message in text view
    private Game_activity activity = null;
    public Game_activity getActivity() {
        return activity;
    }

    public void setActivity(Game_activity activity) {
        this.activity = activity;
    }
    //method invoked when a swipe gesture happens //
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        //swipe delta value x axis
        float deltaX = e1.getX() - e2.getX();
        //absolute value
        float deltaXAbs = Math.abs(deltaX);

        //when swipe distance between min and max distance value then we treat it as effective swipe
        if((deltaXAbs >= MIN_SWIPE_DISTANCE_X) && (deltaXAbs <= MAX_SWIPE_DISTANCE_X))
        {
            if(deltaX > 0)
            {
                //this.activity.displayMessage("swipe left");

            }else
            {
                //this.activity.displayMessage("swipe right");
            }
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //Log.d("Gesture ", " onDown");
        return true;
    }

     @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //Log.d("Gesture ", " onSingleTapUp");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //Log.d("Gesture ", " onShowPress");
    }


    //single tap on screen
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //this.activity.displayMessage("single tap");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //Log.d("Gesture ", " onLongPress");
    }

}

    /*RotateAnimation rotate = new RotateAnimation(-90, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    rotate.setDuration(1000);
    rotate.setInterpolator(new LinearInterpolator());
    fishingPoleImage.startAnimation(rotate);
    fishingPoleImage.setVisibility(View.GONE);*/
