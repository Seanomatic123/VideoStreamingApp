package com.example.videostreamingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileDetailActivity extends AppCompatActivity {


    private ImageButton profilePreviousButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        String name = getIntent().getStringExtra("NAME");
        String email = getIntent().getStringExtra("EMAIL");
        String description = getIntent().getStringExtra("DESCRIPTION");
        int image = getIntent().getIntExtra("IMAGE", 0);

        TextView nameTextView = findViewById(R.id.textProfileNameView);
        TextView emailTextView = findViewById(R.id.textProfileEmailView);
        TextView descriptionTextView = findViewById(R.id.textProfileDescriptionView);
        ImageView imageProfileView = findViewById(R.id.imageProfileView);

        nameTextView.setText(name);
        emailTextView.setText(email);
        descriptionTextView.setText(description);
        imageProfileView.setImageResource(image);

        profilePreviousButton = (ImageButton) findViewById(R.id.profilePrevScreen);
        profilePreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackRecyclerViewActivity();
            }
        });
    }

    public void openBackRecyclerViewActivity() {
        Intent intent = new Intent(this, RecyclerViewActivity.class);
        startActivity(intent);
    }

}