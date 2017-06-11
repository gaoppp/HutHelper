package com.gaop.huthelper.view.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaop.huthelper.CustomMediaController;
import com.gaop.huthelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;


/**
 * Created by gaop1 on 2017/4/8.
 */

public class VideoViewActivity extends BaseActivity {
    private static final String TAG = "VideoViewActivity";
    private static final String VIDEO_PATH = "video_path";
    @BindView(R.id.surface_view)
    VideoView videoView;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.tv_loading)
    TextView tvLoading;
    CustomMediaController mediaController;
    private String videoPath;
    ObjectAnimator ojectAnimator;
    @BindView(R.id.loading_layout)
    LinearLayout loadingLayout;
    private long currentPosition=0L;
    private boolean needResume;

    @Override
    public void initParms(Bundle parms) {
        if (!LibsChecker.checkVitamioLibs(this)){
           return;
        }
        setAllowFullScreen(true);
        if (parms != null && parms.containsKey(VIDEO_PATH)) {
            videoPath = parms.getString(VIDEO_PATH);
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.ativity_videoview;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        mediaController = new CustomMediaController(this);
        videoView.requestFocus();
        videoView.setBufferSize(1024 * 1024);
        videoView.setVideoChroma(MediaPlayer.VIDEOCHROMA_RGB565);
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(videoPath);

    }

    @Override
    protected void onResume() {
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
        preparePlayVideo();
    }

    private void preparePlayVideo() {
        startLoadingAnimator();
       videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                stopLoadingAnimator();
                if (currentPosition > 0) {
                    videoView.seekTo(currentPosition);
                } else {
                    mediaPlayer.setPlaybackSpeed(1.0f);
                }
                startPlay();
            }
        });
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
                switch (arg1) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //开始缓存，暂停播放
                        Log.d(TAG, "开始缓存");
                        startLoadingAnimator();
                        if (videoView.isPlaying()) {
                            stopPlay();
                            needResume = true;
                        }
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //缓存完成，继续播放
                        stopLoadingAnimator();
                        if (needResume) startPlay();
                        Log.d(TAG, "缓存完成");
                        break;
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        //显示 下载速度
                       Log.d(TAG, "download rate:" + arg2);
//                        tvLoading.setText(arg2);
                        break;
                }
                return true;
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d(TAG, "onError:  what=" + what);
                return false;
            }
        });
        videoView.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
            }
        });
       videoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.d(TAG, "onBufferingUpdate:  percent" + percent);
            }
        });
    }
    private void startLoadingAnimator() {
        if (ojectAnimator == null) {
            ojectAnimator = ObjectAnimator.ofFloat(ivLoading, "rotation", 0f, 360f);
        }
        loadingLayout.setVisibility(View.VISIBLE);

        ojectAnimator.setDuration(1000);
        ojectAnimator.start();
    }

    private void stopLoadingAnimator() {
        loadingLayout.setVisibility(View.GONE);
        ojectAnimator.cancel();
    }

    private void startPlay() {
        videoView.start();
    }

    private void stopPlay() {
        videoView.pause();
    }

    public void onPause() {
        super.onPause();
        currentPosition = videoView.getCurrentPosition();
        videoView.pause();
    }
    public void share(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "来自工大助手的视频分享：" + videoPath);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "视频分享"));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoView!=null){
            videoView.stopPlayback();
            videoView = null;
        }
    }

    /**
     * 获取视频当前帧
     * @return
     */
    public Bitmap getCurrentFrame() {
        if(videoView!=null){
            MediaPlayer mediaPlayer = videoView.getmMediaPlayer();
            return  mediaPlayer.getCurrentFrame();
        }
        return null;
    }
    /**
     * 快退(每次都快进视频总时长的1%)
     */
    public void speedVideo() {
        if(videoView!=null){
            long duration = videoView.getDuration();
            long currentPosition = videoView.getCurrentPosition();
            long goalduration=currentPosition+duration/10;
            if(goalduration>=duration){
                videoView.seekTo(duration);
            }else{
                videoView.seekTo(goalduration);
            }
        }
    }

    /**
     * 快退(每次都快退视频总时长的1%)
     */
    public void reverseVideo() {
        if(videoView!=null){
            long duration = videoView.getDuration();
            long currentPosition = videoView.getCurrentPosition();
            long goalduration=currentPosition-duration/10;
            if(goalduration<=0){
                videoView.seekTo(0);
            }else{
                videoView.seekTo(goalduration);
            }
        }
    }
    /**
     * 设置屏幕的显示大小
     */
    public void setVideoPageSize(int currentPageSize) {
        if(videoView!=null){
            videoView.setVideoLayout(currentPageSize,0);
        }
    }
}
