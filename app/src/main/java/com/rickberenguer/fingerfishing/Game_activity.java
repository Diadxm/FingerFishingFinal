package com.rickberenguer.fingerfishing;

import android.graphics.Camera;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.IOException;
import java.util.Random;

public class Game_activity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView3;
    private ProgressBar catchProgressBar;
    private SeekBar catchBar;

    volatile boolean running = false;


    private TextView fishText;

    private ImageView fishImage;

    public Camera cam;

    private ImageView catchBlock;

    private float speed = 10;

    int screenWidth;
    int screenHeight;

    private int imageSize;

    private boolean caught = false;

    private boolean playing;
    private boolean clicked = false;
    private Handler myHandler;
    private Button button1;
    private Button button2;

    private boolean moveUp;
    private boolean moveDown;

    private boolean fishTouch = false;
    private boolean makingMove = false;

    private int moveTimer;

    private boolean moveOne;
    private boolean moveTwo;

    private boolean fishAtBottom;
    private boolean fishAtTop;

    private boolean fishCaught;
    private boolean fishLost;

    //setting up switching game stuff
    private boolean catchingFish = false;
    private boolean startCatching = false;

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

    private int playerX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_activity);

        //find out the width and height of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        scaleImageSize();

        fishImage = (ImageView)findViewById(R.id.Fish);
        fishImage.setX(screenWidth * 0.5f);
        fishImage.setY(screenHeight * 0.75f);
        fishImage.getLayoutParams().height = imageSize;
        fishImage.getLayoutParams().width = imageSize;
        fishImage.setVisibility(View.INVISIBLE);

        catchBlock = (ImageView)findViewById(R.id.catchBlock);
        catchBlock.setX(screenWidth * 0.5f);
        catchBlock.setY(screenHeight - imageSize);
        catchBlock.getLayoutParams().height = imageSize;
        catchBlock.getLayoutParams().width = imageSize;
        catchBlock.setVisibility(View.INVISIBLE);

        //Log.d("screenHeight", "" + screenHeight);

        //Creating object of fish, for name and size of caught fish
        //Object fish = new Fish();
        //((Fish) fish).CreateFish();
        //fishText.setText(((Fish) fish).NameOfFish());
        /////////////////////////////////

        //Erick-Hobbs/////////////
        //////////////////////////
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
        sound2 = soundPool.load(this, R.raw.fishingtheme,1);

        fishingPoleImage = (ImageView)findViewById(R.id.fishingPole);

        castBar = (ProgressBar)findViewById(R.id.CastBar);

        //////////////////////////
        //End of Erick-Hobbs//////
        //////////////////////////
        catchProgressBar = (ProgressBar) findViewById(R.id.catchProgressBar);
        catchProgressBar.setProgress(20);
        catchProgressBar.setVisibility(View.INVISIBLE);
        running = true;
        playing = true;

        button1 = (Button)findViewById(R.id.buttonDown);
        button2 = (Button)findViewById(R.id.buttonUp);
        button1.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.INVISIBLE);




        //Now make all the buttons listen for 'clicks'.
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        myHandler = new Handler() {
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                ////////
                //Start catching game here
                ///////
                if (startCatching){
                    fishCatchCheck();
                    collisionCheck();
                    moveFish();

                    if (moveUp && !moveDown){
                        moveBlockUp();
                    } else if (moveDown && !moveUp){
                        moveBlockDown();
                    }
                    moveTimer();
                    if (moveTimer >= 30){
                        moveTimer = 0;
                        makingMove = false;
                        moveOne = false;
                        moveTwo = false;
                    }
                }

              if (cast){
                    releaseRod();

                }

                myHandler.sendEmptyMessageDelayed(0, 40);
            }
        };
        myHandler.sendEmptyMessage(0);
    }

    //On click for buttons in catch side
    @Override
    public void onClick(View v) {
            switch (v.getId()){

                case R.id.buttonDown:
                    moveUp = false;
                    moveDown = true;
                    break;

                case R.id.buttonUp:
                    moveUp = true;
                    moveDown = false;
                    break;

                    default:
                        clicked = false;
                        break;
            }
    }
    //On touch event for casting rod
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
            break;
        case MotionEvent.ACTION_CANCEL:
        break;
    }
            // return true to tell OS event has been consumed, do not pass to other event listeners
        return true;
    }

    //timer method to reset makingmove for the fish
    private void moveTimer(){
        if (makingMove){
            moveTimer++;
        }
    }

    //get a random number, which case will do a certain move as up or down.
    public void moveFish(){
        if(!makingMove && moveTimer == 0 && !fishCaught){
            Random randInt = new Random();
            int ourRandom;

            ourRandom = randInt.nextInt(2);
            switch(ourRandom) {
                case 0:
                    makingMove = true;
                    moveOne = true;
                    break;
                //////
                case 1:
                    moveTwo = true;
                    break;
                default:
                    break;
            }
        }

        if (moveOne && !fishAtTop){
            if (fishAtTop){
                moveTimer = 0;
                moveOne = false;
            }
            else{
                fishImage.setY(fishImage.getY() - speed * 0.8f);
                Log.d("Test", "MoveOne");
            }
        }
        if(moveTwo && !fishAtBottom){
            if (fishAtBottom){
                moveTimer = 0;
                moveTwo = false;
            }
            else {
                fishImage.setY(fishImage.getY() + speed * 0.8f);
                makingMove = true;
                Log.d("Test", "MoveTwo");
            }
        }
    }

    //this checks the position of the fish and blocks it from leaving the screen
    private void collisionCheck(){
        if (fishImage.getY() >= screenHeight - imageSize){
            fishAtBottom = true;
            fishImage.setY(screenHeight - imageSize);
        }
        else {
            fishAtBottom = false;
        }
        if (fishImage.getY() - imageSize <= 0){
            fishImage.setY(0 + imageSize);
            fishAtTop = true;
        }
        else{
            fishAtTop = false;
        }
    }

    //Need mikes help on moving camera across screen.
    public void moveCamera(){
        //cam.setLocation(400,cam.getLocationY(),cam.getLocationZ());
    }



    // check position of both the fish and the catch block to increase progress on catch.
    //
    private void fishCatchCheck(){
        if (fishImage.getY() < catchBlock.getY() && fishImage.getY() > catchBlock.getY() - imageSize || fishImage.getY() - imageSize < catchBlock.getY() && fishImage.getY() > catchBlock.getY() - imageSize){
            catchProgressBar.incrementProgressBy(1);
            fishTouch = true;

        }

        if (fishImage.getY() < catchBlock.getY() - imageSize || fishImage.getY() - imageSize > catchBlock.getY()){
            catchProgressBar.incrementProgressBy(-1);
            fishTouch = false;
        }

        if (catchProgressBar.getProgress() == 100){
            fishCaught = true;
        }
        if (catchProgressBar.getProgress() == 0){
            fishLost = true;
        }
    }

//this scales image size by getting the screen size and setting image.
    private void scaleImageSize(){
        if (screenWidth <= 480){
            imageSize = 112;
        }
        if (screenWidth <= 540 && screenWidth > 480){
            imageSize = 126;
        }
        if (screenWidth <= 720 && screenWidth > 540){
            imageSize = 168;
        }
        if (screenWidth <= 1080 && screenWidth > 720){
            imageSize = 250;
        }
    }

    // moves catch block up
    private void moveBlockUp(){
            catchBlock.setY(catchBlock.getY() - speed);
        if (catchBlock.getY() < 0){
            catchBlock.setY(0);
        }
    }

    // moves catch block down
    private void moveBlockDown(){
            catchBlock.setY(catchBlock.getY() + speed);
        if (catchBlock.getY() > screenHeight - imageSize){
            catchBlock.setY(screenHeight - imageSize);
        }
    }

    ///////
    //This starts the casting game, run this game first to establish a release
    ////////
    private void handleCastGame(){

    }

    //////
    //This starts the catching game, setting everything to visable and will turn catchgame to true
    //////
    private void handleCatchGame(){
        if (catchingFish){
            fishImage.setVisibility(View.VISIBLE);
            catchBlock.setVisibility(View.VISIBLE);
            catchProgressBar.setVisibility(View.VISIBLE);
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
        }
    }

    /////////////
    //Erick-Hobbs Casting rod methods
    /////////////
    private void castRod(){
        if (movingTx < startTx){
            endTx = ((movingTx - startTx) * -1);
            Log.d("end touch", "" +movingTx);
            rotation--;
            setPower();
            fishingPoleImage.setRotation(rotation);
            soundPool.play(sound1, 1, 1, 0, 0, 0.5f);
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
        soundPool.play(sound2, 1, 1, 0, 0, 1);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }
    ///////////////////
    //end of Erick-Hobbs Casting rod methods

}