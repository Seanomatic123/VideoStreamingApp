package com.example.videostreamingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    private Button unlockVideo;
    private ImageButton liveVideoButton;

    private Button recyclerViewButtonPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        liveVideoButton = (ImageButton) findViewById(R.id.livevideobutton1);
        liveVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLiveVideoChoiceActivity();
            }
        });

        unlockVideo = (Button) findViewById(R.id.button);
        unlockVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUnlockVideoActivity();
            }
        });

        recyclerViewButtonPress = (Button) findViewById(R.id.recyclerViewButton);
        recyclerViewButtonPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openRecyclerViewActivity();}
        });

    }

    public void openUnlockVideoActivity() {
        Intent intent1 = new Intent(this, UnlockVideoActivity.class);
        startActivity(intent1);
    }
    public void openLiveVideoChoiceActivity() {
        Intent intent5 = new Intent(this, LiveVideoChoiceActivity.class);
        startActivity(intent5);
    }
    public void openRecyclerViewActivity() {
        Intent intent7 = new Intent(this, RecyclerViewActivity.class);
        startActivity(intent7);
    }
}