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

        Intent intent = getIntent();

        ItemModel itemModel = intent.getParcelableExtra("ITEM");

        String name = itemModel.getName();
        String email = itemModel.getEmail();
        int image = itemModel.getImage();
        String description = itemModel.getDescription();

        TextView nameTextView = findViewById(R.id.textProfileNameView);
        TextView emailTextView = findViewById(R.id.textProfileEmailView);
        ImageView imageProfileView = findViewById(R.id.imageProfileView);
        TextView descriptionTextView = findViewById(R.id.textProfileDescriptionView);

        nameTextView.setText(name);
        emailTextView.setText(email);
        imageProfileView.setImageResource(image);
        descriptionTextView.setText(description);

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