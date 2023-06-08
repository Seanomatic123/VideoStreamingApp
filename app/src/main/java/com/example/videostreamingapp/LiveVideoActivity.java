package com.example.videostreamingapp;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;



import io.agora.rtc2.*;
import io.agora.rtc2.video.VideoCanvas;
import io.agora.rtc2.video.VideoEncoderConfiguration;

import com.banuba.android.sdk.ext.agora.BanubaExtensionManager;

import java.util.Arrays;
import java.util.List;

public class LiveVideoActivity extends AppCompatActivity {


    private io.agora.rtc2.RtcEngine mRtcEngine;
    private String channelName;
    private int channelRole;

    private RtcEngineConfig rtcEngineConfig;



    private boolean isFrontCamera = true;

    private int effectIndex = -1;

    BanubaExtensionManager banubaExtensionManager = BanubaExtensionManager.INSTANCE;


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

    private static class rtcEngineExtensionObserver implements IMediaExtensionObserver {
        @Override
        public void onEvent(String provider, String extension, String key, String value) {

        }

        @Override
        public void onStarted(String provider, String extension) {

        }

        @Override
        public void onStopped(String provider, String extension) {

        }

        @Override
        public void onError(String provider, String extension, int error, String message) {

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
        enableBanubaExtension(true);
        Button applyEffectButton = (Button) findViewById(R.id.applyeffects);
        applyEffectButton.setOnClickListener(this::showEffects);

    }


    private void enableBanubaExtension(boolean enable) {
        mRtcEngine.enableExtension(
                BanubaExtensionManager.BANUBA_PROVIDER_NAME,
                BanubaExtensionManager.BANUBA_EXTENSION_NAME,
                enable,
                Constants.MediaSourceType.PRIMARY_CAMERA_SOURCE
        );
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(() -> setupRemoteVideo(uid));
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            runOnUiThread(() -> onRemoteUserLeft());
        }

        @Override
        public void onUserMuteVideo(final int uid, final boolean muted) {
            runOnUiThread(() -> onRemoteUserVideoMuted(uid, muted));
        }
    };

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
        FrameLayout container = (FrameLayout) findViewById(R.id.ownvideoview);
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

    private void setupRemoteVideo(int uid) {
        FrameLayout container = (FrameLayout) findViewById(R.id.othervideoview);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
    }

    private void onRemoteUserLeft() {
        FrameLayout container = (FrameLayout) findViewById(R.id.othervideoview);
        container.removeAllViews();
    }

    private void initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine();
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
        mRtcEngine.setClientRole(channelRole);
        setupVideoProfile();
        setupLocalVideo();

        banubaExtensionManager.initialize(
                getApplicationContext(),
                getApplicationContext().getResources().getString(R.string.banuba_client_token),
                mRtcEngine
        );
        joinChannel();
    }

    private void initializeAgoraEngine() {
        try {
            configureRtc2Engine();
            mRtcEngine = RtcEngine.create(rtcEngineConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureRtc2Engine() {
        rtcEngineConfig = new RtcEngineConfig();
        rtcEngineConfig.mContext = getApplicationContext();
        rtcEngineConfig.mAppId = rtcEngineConfig.mContext.getResources().getString(R.string.private_app_id);
        rtcEngineConfig.mEventHandler = mRtcEventHandler;
        rtcEngineConfig.addExtension(BanubaExtensionManager.BANUBA_PLUGIN_NAME);
        rtcEngineConfig.mExtensionObserver = new rtcEngineExtensionObserver();
    }


    private void setupVideoProfile() {
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_1280x720,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
        mRtcEngine.enableVideo();
    }

    private void setupLocalVideo() {
        FrameLayout container = (FrameLayout) findViewById(R.id.ownvideoview);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0));

    }

    private void toggleEffect() {
        effectIndex++;
        if (effectIndex >= availableEffects.size()) {
            effectIndex = -1;
        }
    }
    private String getCurrentEffect() {
        if (effectIndex == -1) {
            return "";
        } else {
            return availableEffects.get(effectIndex);
        }
    }

    public void showEffects(View view) {
        toggleEffect();
        String effect = getCurrentEffect();
        banubaExtensionManager.loadEffectFromAssets(effect);
        enableBanubaExtension(true);
    }


    private void joinChannel() {
        mRtcEngine.joinChannel(null, channelName, "Optional Data", 0);
    }

    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leaveChannel();
        banubaExtensionManager.destroy();
        enableBanubaExtension(false);
        RtcEngine.destroy();
        mRtcEngine = null;
    }

    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
        isFrontCamera = !isFrontCamera;
        banubaExtensionManager.enableMirroring(isFrontCamera);
    }

    public void onEndCallClicked(View view) {
        finish();
    }


}





