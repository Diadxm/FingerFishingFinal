package com.rickberenguer.fingerfishing;

import android.content.Intent;
import android.graphics.Camera;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.CountDownTimer;
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
import java.util.Random;
import java.util.concurrent.Delayed;

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
    private int sound1;//rod swoosh
    private boolean soundPlaying = false;
    private MediaPlayer mediaPlayer;

    //////////////////////
    /////////////////////

    ///// bobber
    private ImageView bobber;

    //// Background
    private ImageView backGround;
    private int bgMove;
    private boolean movingBackground;
    public boolean winLoss = false;
    private int bgMinus = -5;

    //// ends casting game
    private boolean finishedCasting = false;

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
        fishImage.getLayoutParams().height = imageSize;
        fishImage.getLayoutParams().width = imageSize;
        fishImage.setVisibility(View.INVISIBLE);

        catchBlock = (ImageView)findViewById(R.id.catchBlock);
        catchBlock.getLayoutParams().height = imageSize;
        catchBlock.getLayoutParams().width = imageSize;
        catchBlock.setVisibility(View.INVISIBLE);

        bobber = (ImageView)findViewById(R.id.Bobber);
        bobber.setX(0);
        bobber.setY(0);
        bobber.setVisibility(View.INVISIBLE);

        backGround = (ImageView)findViewById(R.id.Background);
        backGround.getLayoutParams().height = screenHeight;



        //Creating object of fish, for name and size of caught fish
        //Object fish = new Fish();
        //((Fish) fish).CreateFish();
        //fishText.setText(((Fish) fish).NameOfFish());

        //Erick-Hobbs/////////////
        //////////////////////////
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .build();
//            soundPool = new SoundPool.Builder()
//                    .setMaxStreams(5)
//                    .setAudioAttributes(audioAttributes)
//                    .build();
//        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
//        }

        sound1 = soundPool.load(this, R.raw.ropeswoosh,1);
        //mediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic);
        //mediaPlayer.setLooping(true);
        //mediaPlayer.start();

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
                    handleCatchGame();
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
                //////////
                //Start Casting Game Here
                //////////
                if (!startCatching){
                    if (cast){
                        releaseRod();

                    }
                    moveBackground();
                    switchToCatchGame();

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
        break;
        case MotionEvent.ACTION_MOVE:
            movingTx = (int)event.getRawX();
            castRod();

            break;
        case MotionEvent.ACTION_UP:
            startTx = 0;
            movingTx = 0;
            endTx = 0;
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

    /*private void loadWinLoss(){
        if(winLoss){

            if (fishCaught){
                fishCaught = true;
                startCatching = false;
                catchingFish = false;
                openCaughtFishActivity();
            }
            if (fishLost){
                fishLost = false;
                startCatching = false;
                catchingFish = false;
                openLostFishActivity();
            }
            winLoss = false;
        }

    }*/

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
            //load new activity for caught
            //winLoss = true;
            //loadWinLoss();
            startCatching = false;
            catchingFish = false;

            Intent intent = new Intent(this, caughtFishActivity.class);
            startActivity(intent);
        }
        if (catchProgressBar.getProgress() == 0){
            fishLost = true;
            //load new activity for losing fish
            //winLoss = true;
            //loadWinLoss();
            startCatching = false;
            catchingFish = false;

            Intent intent = new Intent(this, lostFishActivity.class);
            startActivity(intent);
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
        bobber.setVisibility(View.INVISIBLE);
        castBar.setVisibility(View.INVISIBLE);
    }

    //////
    //This starts the catching game, setting everything to visable and will turn catchgame to true
    //////
    private void handleCatchGame(){
        if (!catchingFish){
            fishImage.setX(bobber.getX() - (imageSize * 0.5f));
            fishImage.setY(screenWidth * 0.7f);
            catchBlock.setX(bobber.getX() - (imageSize * 0.5f));
            catchBlock.setY(bobber.getY());
            handleCastGame();
            catchingFish = true;
        }
            backGround.setVisibility(View.VISIBLE);
            fishImage.setVisibility(View.VISIBLE);
            catchBlock.setVisibility(View.VISIBLE);
            catchProgressBar.setVisibility(View.VISIBLE);
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
    }

    /////////////
    //Erick-Hobbs Casting rod methods
    /////////////
    private void castRod(){
        if (movingTx < startTx){
            endTx = ((movingTx - startTx) * -1);
            rotation--;
            setPower();
            fishingPoleImage.setRotation(rotation);

        }
        soundPlaying = false;
    }

    private void releaseRod(){
        rotation = rotation + 5;
        fishingPoleImage.setRotation(rotation);

        throwBobber();
        if (fishingPoleImage.getRotation() > 0){
            rotation = 0;
            cast = false;
            castBar.setProgress((int)0);
            fishingPoleImage.setVisibility(View.INVISIBLE);
        }
    }

    private void throwBobber(){
        bobber.setY(screenHeight * 0.4f);
        bobber.setX(0);
        bobber.setVisibility(View.VISIBLE);


        if(power <= 10){
            movingBackground = true;
        }
        if (power == 20){
            movingBackground = true;
        }
        if (power == 30){
            movingBackground = true;
        }
        if (power == 40){
            movingBackground = true;
        }

        if (power == 50){
            movingBackground = true;
        }
        if (power == 60){
            movingBackground = true;
        }
        if (power == 70){
            movingBackground = true;
        }

        if (power == 80){
            movingBackground = true;
        }
        if (power == 90){
            movingBackground = true;
        }
        if (power == 100){
            movingBackground = true;
        }
        if (fishingPoleImage.getRotation() > 45){
            rotation = 45;

        }

        if (soundPlaying == false){
            soundPlaying = true;
            soundPool.play(sound1, 1, 1, 0, 0, 1);

        }
    }

    private void setProgress(){
        castBar.setProgress((int)power);
    }

    private void setPower(){
        setProgress();
        if (endTx < (screenWidth * 0.1f)){
            power = 10;
            if(rotation < -9.0f){
                rotation = -9.0f;
            }
        }
        if (endTx > (screenWidth * 0.1f) && endTx < (screenWidth * 0.2f)){
            power = 20;
            if(rotation > -9.0f && rotation < -18.0f){
                rotation = -18.0f;
            }
        }
        if ( endTx > (screenWidth * 0.2f) && endTx < (screenWidth * 0.3f)){
            power = 30;
            if(rotation > -18.0f && rotation < -27.0f){
                rotation = -27.0f;
            }
        }
        if (endTx > (screenWidth * 0.3f) && endTx < (screenWidth * 0.4f)){
            power = 40;
            if(rotation > -27.0f && rotation < -36.0f){
                rotation = -36.0f;
            }
        }
        if (endTx > (screenWidth * 0.4f) && endTx < (screenWidth * 0.5f)){
            power = 50;
            if(rotation > -36.0f && rotation < -45f){
                rotation = -45f;
            }
        }
        if (endTx > (screenWidth * 0.5f) && endTx < (screenWidth * 0.6f)){
            power = 60;
            if(rotation > -45f && rotation < -54f){
                rotation = -54f;
            }
        }
        if (endTx > (screenWidth * 0.6f) && endTx < (screenWidth * 0.7f)){
            power = 70;
            if(rotation > -54f && rotation < -63f){
                rotation = -63f;
            }
        }
        if (endTx > (screenWidth * 0.7f) && endTx < (screenWidth * 0.8f)){
            power = 80;
            if(rotation > -63f && rotation < -72f){
                rotation = -72f;
            }
        }
        if (endTx > (screenWidth * 0.8f) && endTx < (screenWidth * 0.9f)){
            power = 90;
            if(rotation > -72f && rotation < -81f){
                rotation = -81f;
            }
        }
        if (endTx > screenWidth * 0.9f){
            power = 100;
            if(rotation > -81f && rotation < -90f){
                rotation = -45f;
            }
        }
        if (fishingPoleImage.getRotation() < -90f){
            rotation = -90f;
            fishingPoleImage.setRotation(rotation);
        }
    }

    private void moveBackground(){
        if (movingBackground){
            bgMove = (bgMove - (int)(screenWidth * 0.025f));
            bobber.setX(bobber.getX() + screenWidth * 0.025f);
            if (bobber.getX() >= screenWidth * .5f){
                bobber.setX(screenWidth * 0.5f);
            }
            if (power == 10){
                if (bgMove > -backGround.getLayoutParams().width * 0.1f){
                    backGround.setX(bgMove);
                }
                else if (bgMove <= -backGround.getLayoutParams().width * 0.1f){
                    bgMove = -(int)(backGround.getLayoutParams().width * 0.1f);
                    movingBackground = false;
                    finishedCasting = true;
                }
            }
            if (power == 20){
                if (bgMove > -backGround.getLayoutParams().width * 0.2f){
                    backGround.setX(bgMove);
                }
                else if (bgMove <= -backGround.getLayoutParams().width * 0.2f){
                    bgMove = -(int)(backGround.getLayoutParams().width * 0.2f);
                    movingBackground = false;
                    finishedCasting = true;
                }
            }
            else if (power == 30){
                if (bgMove > -backGround.getLayoutParams().width * 0.3f){
                    backGround.setX(bgMove);
                }
                else if (bgMove <= -backGround.getLayoutParams().width * 0.3f){
                    bgMove = -(int)(backGround.getLayoutParams().width * 0.3f);
                    movingBackground = false;
                    finishedCasting = true;
                }
            }
            else if (power == 40){
                if (bgMove > -backGround.getLayoutParams().width * 0.4f){
                    backGround.setX(bgMove);
                }
                else if (bgMove <= -backGround.getLayoutParams().width * 0.4f){
                    bgMove = -(int)(backGround.getLayoutParams().width * 0.4f);
                    movingBackground = false;
                    finishedCasting = true;
                }
            }
            else if (power == 50){
                if (bgMove > - backGround.getLayoutParams().width * 0.5f){
                    backGround.setX(bgMove);
                }
                else if (bgMove <= -backGround.getLayoutParams().width * 0.5f){
                    bgMove = -(int)(backGround.getLayoutParams().width * 0.5f);
                    movingBackground = false;
                    finishedCasting = true;
                }
            }
            else if (power == 60){
                if (bgMove > - backGround.getLayoutParams().width * 0.6f){
                    backGround.setX(bgMove);
                }
                else if (bgMove <= -backGround.getLayoutParams().width * 0.6f){
                    bgMove = -(int)(backGround.getLayoutParams().width * 0.6f);
                    movingBackground = false;
                    finishedCasting = true;
                }
            }
            else if (power == 70){
                if (bgMove > - backGround.getLayoutParams().width * 0.625f){
                    backGround.setX(bgMove);
                }
                else if (bgMove <= -backGround.getLayoutParams().width * 0.625f){
                    bgMove = -(int)(backGround.getLayoutParams().width * 0.625f);
                    movingBackground = false;
                    finishedCasting = true;
                }
            }
            else if (power == 80){
                if (bgMove > -backGround.getLayoutParams().width * 0.675f){
                    backGround.setX(bgMove);
                }
                else if (bgMove <= -backGround.getLayoutParams().width * 0.675f){
                    bgMove = -(int)(backGround.getLayoutParams().width * 0.675f);
                    movingBackground = false;
                    finishedCasting = true;
                }
            }
            else if (power == 90){
                if (bgMove > -backGround.getLayoutParams().width * 0.725f){
                    backGround.setX(bgMove);
                }
                else if (bgMove <= -backGround.getLayoutParams().width * 0.725f){
                    bgMove = -(int)(backGround.getLayoutParams().width * 0.725f);
                    movingBackground = false;
                    finishedCasting = true;
                }
            }
            else if (power == 100){
                if (bgMove > -backGround.getLayoutParams().width * 0.75f){
                    backGround.setX(bgMove);
                }
                else if (bgMove <= -backGround.getLayoutParams().width * 0.75f){
                    bgMove = -(int)(backGround.getLayoutParams().width * 0.75f);
                    movingBackground = false;
                    finishedCasting = true;
                }
            }
        }
    }

    private void switchToCatchGame(){
        if (finishedCasting){
            startCatching = true;
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        //mediaPlayer.release();
        //finish();

    }

    /*public void openCaughtFishActivity(){
        Intent intent = new Intent(this, caughtFishActivity.class);
        startActivity(intent);
    }

    public void openLostFishActivity(){
        Intent intent = new Intent(this, lostFishActivity.class);
        startActivity(intent);
    }*/

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //soundPool.release();
        //soundPool = null;
    }
    ///////////////////
    //end of Erick-Hobbs Casting rod methods

}