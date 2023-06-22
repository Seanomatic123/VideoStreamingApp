package com.example.videostreamingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.FrameLayout;

public class TopGiftersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_gifters);

        FrameLayout frameLayout1 = findViewById(R.id.frameLayout);
        FrameLayout frameLayout2 = findViewById(R.id.frameLayout2);
        FrameLayout frameLayout3 = findViewById(R.id.frameLayout3);

        frameLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getApplicationContext().getResources().getString(R.string.rank1gifter) + " has gifted you " + getApplicationContext().getResources().getString(R.string.rank1giftercurrency));
            }
        });

        frameLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getApplicationContext().getResources().getString(R.string.rank2gifter) + " has gifted you " + getApplicationContext().getResources().getString(R.string.rank2giftercurrency));
            }
        });

        frameLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getApplicationContext().getResources().getString(R.string.rank3gifter) + " has gifted you " + getApplicationContext().getResources().getString(R.string.rank3giftercurrency));
            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}