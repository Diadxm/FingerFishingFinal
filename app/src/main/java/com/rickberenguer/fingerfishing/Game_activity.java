package com.rickberenguer.fingerfishing;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.os.Bundle;
import android.view.Display;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;

import java.io.IOException;


public class Game_activity extends AppCompatActivity{

    ////Erick-Hobbs///
    /////////////////
    int screenWidth;
    int screenHeight;
    ImageView fishingPoleImage;

    ProgressBar castBar;

    private int startTx;
    private int movingTx;
    private int endTx;
    private boolean cast = false;
    private float power;

    private float rotation;

    private SoundPool soundPool;
    private int sound1;
    private int sound2;

    //////////////////////
    /////////////////////

    TextView fishText;
    private Handler myHandler;
    private int playerX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_activity);

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

        sound1 = soundPool.load(this, R.raw.ropeswoosh,1);
        sound2 = soundPool.load(this, R.raw.loon,1);

        myHandler = new Handler() {
            public void handleMessage(Message msg){
                super.handleMessage(msg);



                if (cast){
                    releaseRod();

                }
                myHandler.sendEmptyMessageDelayed(0, 40);
            }
        };
        myHandler.sendEmptyMessage(0);


        ////Erick-Hobbs///
        /////////////////

        //find out the width and height of the screen//
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        ////////////////
        ////////////////

        fishText = (TextView)findViewById(R.id.FishName);
        Object fish = new Fish();
        ((Fish) fish).CreateFish();
        fishText.setText(((Fish) fish).NameOfFish());
        fishingPoleImage = (ImageView)findViewById(R.id.fishingPole);

        castBar = (ProgressBar)findViewById(R.id.CastBar);
    }

    ////Erick-Hobbs///
    /////////////////

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int action = event.getActionMasked();

        switch(action){
        case MotionEvent.ACTION_DOWN:
            startTx = (int)event.getRawX();
            Log.d("start", "" + startTx);
        break;
        case MotionEvent.ACTION_MOVE:
            movingTx = (int)event.getRawX();
            castRod();

            break;
        case MotionEvent.ACTION_UP:
            startTx = 0;
            movingTx = 0;
            cast = true;
            playSound();
            break;
        case MotionEvent.ACTION_CANCEL:
        break;
    }
            // return true to tell OS event has been consumed, do not pass to other event listeners
        return true;
    }

    private void castRod(){
        if (movingTx < startTx){
            endTx = ((movingTx - startTx) * -1);
            Log.d("end touch", "" +movingTx);
            rotation--;
            setPower();
            fishingPoleImage.setRotation(rotation);

        }
    }

    private void releaseRod(){
        rotation++;
        fishingPoleImage.setRotation(rotation);
        if (fishingPoleImage.getRotation() > 45){
            rotation = 45;
        }
    }

    private void setProgress(){
        castBar.setProgress((int)power);
    }

    private void setPower(){
        setProgress();
        if (endTx < (screenWidth * 0.1f)){
            power = 10;
            if(rotation < -4.5f){
                rotation = -4.5f;
            }
        }
        if (endTx > (screenWidth * 0.1f) && endTx < (screenWidth * 0.2f)){
            power = 20;
            if(rotation > -4.5f && rotation < -9f){
                rotation = -9f;
            }
        }
        if ( endTx > (screenWidth * 0.2f) && endTx < (screenWidth * 0.3f)){
            power = 30;
            if(rotation > -9f && rotation < -13.5f){
                rotation = -13.5f;
            }
        }
        if (endTx > (screenWidth * 0.3f) && endTx < (screenWidth * 0.4f)){
            power = 40;
            if(rotation > -13.5f && rotation < -18f){
                rotation = -18f;
            }
        }
        if (endTx > (screenWidth * 0.4f) && endTx < (screenWidth * 0.5f)){
            power = 50;
            if(rotation > -18f && rotation < -22.5f){
                rotation = -22.5f;
            }
        }
        if (endTx > (screenWidth * 0.5f) && endTx < (screenWidth * 0.6f)){
            power = 60;
            if(rotation > -22.5f && rotation < -25f){
                rotation = -25f;
            }
        }
        if (endTx > (screenWidth * 0.6f) && endTx < (screenWidth * 0.7f)){
            power = 70;
            if(rotation > -25f && rotation < -29.5f){
                rotation = -29.5f;
            }
        }
        if (endTx > (screenWidth * 0.7f) && endTx < (screenWidth * 0.8f)){
            power = 80;
            if(rotation > -29.5f && rotation < -34f){
                rotation = -34f;
            }
        }
        if (endTx > (screenWidth * 0.8f) && endTx < (screenWidth * 0.9f)){
            power = 90;
            if(rotation > -34f && rotation < -38.5f){
                rotation = -38.5f;
            }
        }
        if (endTx > screenWidth * 0.9f){
            power = 100;
            if(rotation > -38.5f && rotation < -45f){
                rotation = -45f;
            }
        }
        if (fishingPoleImage.getRotation() < -45f){
            rotation = -45f;
            fishingPoleImage.setRotation(rotation);
        }
    }

    public void playSound(){
            soundPool.play(sound1, 1, 1, 0, 0, 1);
            soundPool.play(sound2, 1, 1, 0, 1, 1);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }
}
