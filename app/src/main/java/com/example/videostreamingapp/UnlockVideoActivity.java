package com.example.videostreamingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class UnlockVideoActivity extends AppCompatActivity {

    private ImageButton backButton;
    private Button unlockContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_video);

        backButton = (ImageButton) findViewById(R.id.imageButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackToMainActivity();
            }
        });

        unlockContent = (Button) findViewById(R.id.buttonunlock1);
        unlockContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openPaymentPlansActivity(); }
        });
    }

        public void openBackToMainActivity() {
            Intent intent2 = new Intent(this, MainActivity.class);
            startActivity(intent2);
        }
        public void openPaymentPlansActivity() {
            Intent intent3 = new Intent(this, PaymentPlansActivity.class);
            startActivity(intent3);
        }
}