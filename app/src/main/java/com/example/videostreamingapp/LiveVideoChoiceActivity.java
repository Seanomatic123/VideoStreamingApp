package com.example.videostreamingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.agora.rtc.Constants;

public class LiveVideoChoiceActivity extends AppCompatActivity {
    int channelRole;
    public static final String channelMessage = "com.example.videostreamingapp.CHANNEL";
    public static final String profileMessage = "com.example.videostreamingapp.PROFILE";

    private ImageButton backtomainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_video_choice);

        int permissionRequestCamera = 0;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
            PackageManager.PERMISSION_GRANTED) {
                  ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO
                        },
                        permissionRequestCamera);
        }

        backtomainButton = (ImageButton) findViewById(R.id.backscreenButton);
        backtomainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackToMainScreenActivity();
            }
        });

    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (view.getId() == R.id.hostchoice) {
            if (checked) {
                channelRole = Constants.CLIENT_ROLE_BROADCASTER;
            }
        } else if (view.getId() == R.id.audiencechoice) {
            if (checked) {
                channelRole = Constants.CLIENT_ROLE_AUDIENCE;
            }
        }
    }
    public void onSubmit(View view) {
        EditText channel = (EditText) findViewById(R.id.enterchannelname);
        String channelName = channel.getText().toString();
        Intent intent6 = new Intent(this, LiveVideoActivity.class);
        intent6.putExtra(channelMessage, channelName);
        intent6.putExtra(profileMessage, channelRole);
        startActivity(intent6);
    }

    public void openBackToMainScreenActivity() {
        Intent intent2 = new Intent(this, MainActivity.class);
        startActivity(intent2);
    }

}