package com.maya.memories.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.maya.memories.R;
import com.maya.memories.constants.Constants;
import com.maya.memories.models.VideoItem;
import com.maya.memories.utils.Logger;
import com.maya.memories.utils.Utility;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

public class VideoViewActivity extends AppCompatActivity implements UniversalVideoView.VideoViewCallback
{
    private static final String TAG = "VideoViewActivity";
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private String VIDEO_URL;

    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    View mVideoLayout;
    ImageView mStart;
    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;
    VideoItem videoItem;
    SharedPreferences PREFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            PREFS = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
            videoItem = (VideoItem) getIntent().getSerializableExtra("VideoItem");
            VIDEO_URL = videoItem.videoUrl;
            setContentView(R.layout.activity_video_view);
            mVideoLayout = findViewById(R.id.video_layout);
            mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
            mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
            mVideoView.setMediaController(mMediaController);
            setVideoAreaSize();
            mVideoView.setVideoViewCallback(this);
            mStart = (ImageView) findViewById(R.id.start);
            mMediaController.setVisibility(View.GONE);
            mStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStart.setVisibility(View.GONE);
                    mMediaController.setVisibility(View.VISIBLE);
                    if (mSeekPosition > 0) {
                        mVideoView.seekTo(mSeekPosition);
                    }
                    mVideoView.start();
                    mMediaController.setTitle("Trending.....");
                    Utility.addlogs(VIDEO_URL, "video", PREFS.getString(Constants.USER_UUID, ""));
                }
            });

            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Logger.d(TAG, "onCompletion ");
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d(TAG, "onPause ");
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            Logger.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            mVideoView.pause();
        }
    }


    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                mVideoView.setVideoPath(VIDEO_URL);
                mVideoView.requestFocus();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Logger.d(TAG, "onSaveInstanceState Position=" + mVideoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        Logger.d(TAG, "onRestoreInstanceState Position=" + mSeekPosition);
    }


    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);

        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
        }

        switchTitleBar(!isFullscreen);
    }

    private void switchTitleBar(boolean show)
    {
//        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
//        if (supportActionBar != null) {
//            if (show) {
//                supportActionBar.show();
//            } else {
//                supportActionBar.hide();
//            }
//        }
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        Logger.d(TAG, "onPause UniversalVideoView callback");
    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {
        Logger.d(TAG, "onStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {
        Logger.d(TAG, "onBufferingStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {
        Logger.d(TAG, "onBufferingEnd UniversalVideoView callback");
    }

    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

}