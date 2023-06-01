package com.example.videostreamingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PaymentPlansActivity extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;
    private ImageView imageview1;
    private ImageView imageview2;
    private TextView currentlySelectedTextView;
    private ImageView currentlySelectedImageView;
    private ImageButton previousButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_plans);


        textView1 = findViewById(R.id.clicktextunlock1);
        textView2 = findViewById(R.id.clicktextunlock2);
        imageview1 = findViewById(R.id.clickcircle1);
        imageview2 = findViewById(R.id.clickcircle2);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView clickedTextView = (TextView) v;
                ImageView correspondingImageView = getCorrespondingImageView(clickedTextView);
                if (clickedTextView == currentlySelectedTextView) {
                    return;
                }
                if (currentlySelectedTextView != null) {
                    currentlySelectedTextView.setBackgroundResource(R.drawable.unlockplanroundborder);
                    currentlySelectedImageView.setImageResource(R.drawable.greylightcircle);
                }

                clickedTextView.setBackgroundResource(R.drawable.unlockplanroundborderselected);
                correspondingImageView.setImageResource(R.drawable.pinkboldcircle);

                currentlySelectedTextView = clickedTextView;
                currentlySelectedImageView = correspondingImageView;
            }
        };

        textView1.setOnClickListener(clickListener);
        textView2.setOnClickListener(clickListener);

        previousButton = (ImageButton) findViewById(R.id.previousscreenbutton);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackToUnlockVideoActivity();
            }
        });

    }

    private ImageView getCorrespondingImageView(TextView textView) {
        if (textView == textView1) {
            return imageview1;
        } else if (textView == textView2) {
            return imageview2;
        }
        return null;
    }
    public void openBackToUnlockVideoActivity() {
        Intent intent4 = new Intent(this, UnlockVideoActivity.class);
        startActivity(intent4);
    }
}
