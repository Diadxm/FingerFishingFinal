package com.rickberenguer.fingerfishing;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

import org.w3c.dom.Text;

public class Game_activity extends AppCompatActivity {


    //Erick-Hobbs
    //////////////
    private TextView textView3;
    private ProgressBar catchProgressBar;
    private SeekBar catchBar;
    SwipeButton castSwipe;

    int screenWidth;
    int screenHeight;
    //////////////////////
    /////////////////////


    TextView fishText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_activity);


        //Erick-Hobbs
        //////////////
        //find out the width and height of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        castSwipe = (SwipeButton) findViewById(R.id.castSwipe);
        castSwipe.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                
            }
        });
        //////////////////////
        /////////////////////


        fishText = (TextView)findViewById(R.id.FishName);

        Object fish = new Fish();

        ((Fish) fish).CreateFish();

        fishText.setText(((Fish) fish).NameOfFish());

        //Erick-Hobbs/////////////
        //////////////////////////
        textView3 = (TextView) findViewById(R.id.textView3);
        catchProgressBar = (ProgressBar) findViewById(R.id.catchProgressBar);
        catchBar = (SeekBar) findViewById(R.id.catchBar);

        catchBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                catchProgressBar.setProgress(progress);
                textView3.setText("" + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ////////////////
        ////////////////
    }
}
