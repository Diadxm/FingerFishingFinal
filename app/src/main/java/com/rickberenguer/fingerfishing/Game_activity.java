package com.rickberenguer.fingerfishing;

import android.graphics.Camera;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

public class Game_activity extends AppCompatActivity implements View.OnClickListener {

    //Erick-Hobbs
    //////////////
    private TextView textView3;
    private ProgressBar catchProgressBar;
    private SeekBar catchBar;

    volatile boolean running = false;
    //////////////////////
    /////////////////////

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



                myHandler.sendEmptyMessageDelayed(0, 40);
            }
        };
        myHandler.sendEmptyMessage(0);
    }

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

}