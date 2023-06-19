package com.example.videostreamingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import io.agora.rtc2.*;
import io.agora.rtc2.video.VideoCanvas;
import io.agora.rtc2.video.VideoEncoderConfiguration;

import com.banuba.android.sdk.ext.agora.BanubaExtensionManager;


import java.util.Arrays;
import java.util.List;

public class LiveVideoActivity extends AppCompatActivity {

    private static final String TAG = "BanubaExtension";
    private static final int REQUEST_CODE_PERMISSIONS = 1001;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private final List<String> availableEffects = Arrays.asList(
            "360_CubemapEverest_noSound",
            "CartoonOctopus",
            "clubs",
            "dialect",
            "frame1",
            "Graduate",
            "prequel_VE",
            "RainbowBeauty",
            "Regular_blur",
            "Regular_Dawn_of_nature",
            "relook",
            "Sunset",
            "TrollGrandma",
            "video_BG_Metro_sfx",
            "video_BG_RainyCafe",
            "WhooshBeautyFemale"
    );

    private int effectIndex = -1;
    private boolean isJoinedToChannel = false;
    private RtcEngine mRtcEngine;
    private boolean isFrontCamera = true;
    private String channelName;
    private int channelRole;

    private boolean isMakeUpApplied = false;
    BanubaExtensionManager banubaExtensionManager = BanubaExtensionManager.INSTANCE;

    private class AgoraEventHandler extends IRtcEngineEventHandler {
        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(() -> setupRemoteVideo(uid));
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            runOnUiThread(LiveVideoActivity.this::onRemoteUserLeft);
        }

        @Override
        public void onUserMuteVideo(final int uid, final boolean muted) {
            runOnUiThread(() -> onRemoteUserVideoMuted(uid, muted));
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_video);

        Intent intent = getIntent();
        channelName = intent.getStringExtra(LiveVideoChoiceActivity.channelMessage);
        channelRole = intent.getIntExtra(LiveVideoChoiceActivity.profileMessage, -1);
        if(channelRole == -1) {
            Log.e("TAG: ", "No Role");
        }

        initAgoraEngineAndJoinChannel();

        invalidateUiState();

        findViewById(R.id.joinButton).setOnClickListener(v -> joinChannel());
        findViewById(R.id.applyTeethButton).setOnClickListener(v -> {
            isMakeUpApplied = !isMakeUpApplied;
            banubaExtensionManager.loadEffectFromAssets("Makeup");
            if(isMakeUpApplied) {
                banubaExtensionManager.evalJs("Teeth.whitening(1)");
            }
            else {
                banubaExtensionManager.evalJs("Teeth.clear()");
            }
        });
        findViewById(R.id.applyEffectButton).setOnClickListener(v -> {
            toggleEffect();
            String effectName = getCurrentEffect();
            Log.d(TAG, "Prepare effect = " + effectName);
            banubaExtensionManager.loadEffectFromAssets(effectName);
        });
        enableBanubaExtension(true);



    }
    private void initAgoraEngineAndJoinChannel() {
        RtcEngineConfig rtcEngineConfig = new RtcEngineConfig();
            rtcEngineConfig.mContext = getApplicationContext();
            rtcEngineConfig.mAppId = rtcEngineConfig.mContext.getResources().getString(R.string.private_app_id);
            rtcEngineConfig.addExtension(BanubaExtensionManager.BANUBA_PLUGIN_NAME);
            rtcEngineConfig.mEventHandler = new AgoraEventHandler();
            rtcEngineConfig.mExtensionObserver = new rtcEngineExtensionObserver();
        try {
            mRtcEngine = RtcEngine.create(rtcEngineConfig);
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.setClientRole(channelRole);
            mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                    VideoEncoderConfiguration.VD_1280x720,
                    VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                    VideoEncoderConfiguration.STANDARD_BITRATE,
                    VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
            mRtcEngine.enableVideo();
            mRtcEngine.enableAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void enableBanubaExtension(boolean enable) {
        mRtcEngine.enableExtension(
                BanubaExtensionManager.BANUBA_PROVIDER_NAME,
                BanubaExtensionManager.BANUBA_EXTENSION_NAME,
                enable,
                Constants.MediaSourceType.PRIMARY_CAMERA_SOURCE
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        startRtcAndExtensionOr(() -> {
            Log.d(TAG, "Request permissions");
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        });
    }
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        mRtcEngine.stopPreview();
        leaveChannel();
    }
    @Override
    protected void onResume() {
        super.onResume();
        banubaExtensionManager.resume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        banubaExtensionManager.pause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        banubaExtensionManager.destroy();
        enableBanubaExtension(false);
        RtcEngine.destroy();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        startRtcAndExtensionOr(() -> {
            showToast("Please grant permission and try again");
            finish();
        });
    }
    private void startRtcAndExtensionOr(Runnable deniedBlock) {
        if (checkAllPermissionsGranted()) {
            startPreviewAndExtension();
        } else {
            deniedBlock.run();
        }
    }
    private boolean checkAllPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    private void startPreviewAndExtension() {
        Log.d(TAG, "Start local preview");
        if(channelRole == Constants.CLIENT_ROLE_BROADCASTER){
            setupLocalVideo();
            mRtcEngine.startPreview();
            banubaExtensionManager.initialize(
                    getApplicationContext(),
                    getApplicationContext().getResources().getString(R.string.banuba_client_token),
                    mRtcEngine
            );

        }

    }
    private void setupRemoteVideo(int uid) {
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        mRtcEngine.setupRemoteVideo(new VideoCanvas(view, VideoCanvas.RENDER_MODE_FIT, uid));
        ViewGroup remoteVideoContainer = findViewById(R.id.othervideoview);
        remoteVideoContainer.addView(view);
    }
    private void onRemoteUserLeft() {
        ViewGroup container = findViewById(R.id.othervideoview);
        container.removeAllViews();
    }
    private void setupLocalVideo() {
        SurfaceView surfaceView = new SurfaceView(this);
        surfaceView.setZOrderMediaOverlay(true);
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView,
                                                    VideoCanvas.RENDER_MODE_FIT,
                                                0));
        mRtcEngine.setLocalRenderMode(
                Constants.RENDER_MODE_HIDDEN,
                Constants.VIDEO_MIRROR_MODE_DISABLED
        );
        ViewGroup localVideoContainer = findViewById(R.id.ownvideoview);
        localVideoContainer.removeAllViews();
        localVideoContainer.addView(surfaceView);
    }
    private void toggleEffect() {
        effectIndex++;
        if (effectIndex >= availableEffects.size()) {
            effectIndex = -1;
        }
        invalidateUiState();
    }
    private void invalidateUiState() {
        int nextEffectIndex = effectIndex + 1;
        if (nextEffectIndex >= availableEffects.size()) {
            ((Button)findViewById(R.id.applyEffectButton)).setText("Cancel effect");
        } else {
            ((Button)findViewById(R.id.applyEffectButton)).setText("Apply " + availableEffects.get(nextEffectIndex));
        }

        if (isJoinedToChannel) {
            findViewById(R.id.joinButton).setVisibility(View.GONE);
            findViewById(R.id.headerpreview).setVisibility(View.GONE);
            findViewById(R.id.leaveButton).setVisibility(View.VISIBLE);
            findViewById(R.id.headerjoined).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.joinButton).setVisibility(View.VISIBLE);
            findViewById(R.id.headerpreview).setVisibility(View.VISIBLE);
            findViewById(R.id.leaveButton).setVisibility(View.GONE);
            findViewById(R.id.headerjoined).setVisibility(View.GONE);
        }
    }
    private String getCurrentEffect() {
        if (effectIndex == -1) {
            return "";
        } else {
            return availableEffects.get(effectIndex);
        }
    }
    private void joinChannel() {
        if (!isJoinedToChannel) {
            Log.d(TAG, "Join to Agora channel = " + channelName);
            isJoinedToChannel = true;
            mRtcEngine.joinChannel(null, channelName, null, 0);
            invalidateUiState();
        }
    }
    private void leaveChannel() {
        if (isJoinedToChannel) {
            Log.d(TAG, "Leave Agora channel");
            isJoinedToChannel = false;
            mRtcEngine.leaveChannel();
            removeRemoteVideo();
            invalidateUiState();
        }
    }
    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
        isFrontCamera = !isFrontCamera;
        banubaExtensionManager.enableMirroring(isFrontCamera);
    }
    public void onLocalAudioMuteClicked(View view) {
        ImageView muteMicView = (ImageView) view;
        if (muteMicView.isSelected()) {
            muteMicView.setSelected(false);
            muteMicView.clearColorFilter();
        } else {
            muteMicView.setSelected(true);
            muteMicView.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY);
        }

        mRtcEngine.muteLocalAudioStream(muteMicView.isSelected());

    }
    private void onRemoteUserVideoMuted(int uid, boolean muted) {
        FrameLayout container = (FrameLayout) findViewById(R.id.othervideoview);

        SurfaceView surfaceView = (SurfaceView) container.getChildAt(0);

        Object tag = surfaceView.getTag();
        if (tag != null && (Integer) tag == uid) {
            surfaceView.setVisibility(muted ? View.GONE : View.VISIBLE);
        }
    }
    public void onLocalVideoMuteClicked(View view) {
        ImageView muteVidView = (ImageView) view;
        RelativeLayout container = (RelativeLayout) findViewById(R.id.ownvideoview);
        SurfaceView surfaceView = (SurfaceView) container.getChildAt(0);

        if (muteVidView.isSelected()) {
            muteVidView.setSelected(false);
            muteVidView.clearColorFilter();
            container.setVisibility(View.VISIBLE);
            surfaceView.setZOrderMediaOverlay(false);
        }
        else {
            muteVidView.setSelected(true);
            muteVidView.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY);
            container.setVisibility(View.GONE);
        }

        mRtcEngine.muteLocalVideoStream(muteVidView.isSelected());

    }
    private void removeRemoteVideo() {
        ViewGroup remoteVideoContainer = findViewById(R.id.othervideoview);
        remoteVideoContainer.removeAllViews();
    }
    public void onEndCallClicked(View view) {
        finish();
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private class rtcEngineExtensionObserver implements IMediaExtensionObserver {
        @Override
        public void onEvent(String provider, String extension, String key, String value) {
            Log.d(TAG, "Extension event: " + provider + "." + extension + " key:" + key +
                    "value:" + value);
        }

        @Override
        public void onStarted(String provider, String extension) {
            if (BanubaExtensionManager.BANUBA_PROVIDER_NAME.equals(provider) &&
                    BanubaExtensionManager.BANUBA_EXTENSION_NAME.equals(extension)) {
                runOnUiThread(() -> showToast("Banuba extension started!"));
            }
        }

        @Override
        public void onStopped(String provider, String extension) {
            if (BanubaExtensionManager.BANUBA_PROVIDER_NAME.equals(provider) &&
                    BanubaExtensionManager.BANUBA_EXTENSION_NAME.equals(extension)) {
                runOnUiThread(() -> showToast("Banuba extension stopped!"));
            }
        }

        @Override
        public void onError(String provider, String extension, int error, String message) {
            Log.e(TAG, "Extension error: " + provider + "." + extension + " code:"
                    + error + " error:" + message);
        }
    }


}