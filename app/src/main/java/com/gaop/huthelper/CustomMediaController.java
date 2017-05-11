package com.gaop.huthelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.view.activity.VideoViewActivity;

import io.vov.vitamio.widget.MediaController;

/**
 * Created by gaop1 on 2017/4/8.
 */

public class CustomMediaController extends MediaController {

    private Context context;
    private AudioManager audioManager;
    private GestureDetector gestureDetector;
    private ImageView mediacontroller_next;
    private ImageView mediacontroller_previous;
    private TextView content;
    private VideoViewActivity activity;
    private float brightness = -1f;


    private int maxVolume;
    private int volume = -1;

    public CustomMediaController(Context context) {
        super(context);
        this.context = context;
        this.activity= (VideoViewActivity) context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        gestureDetector = new GestureDetector( context,new VolumeBrightnesGestureListener());

    }
    @Override
    protected View makeControllerView() {
       View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(getResources().getIdentifier("media_controller", "layout",context.getPackageName()), this);
        ImageView btn_back = (ImageView) view.findViewById(R.id.btn_mediacontroll_back);
        TextView btn_cut = (TextView) view.findViewById(R.id.tv_mediacontroll_cut);
        TextView btn_share = (TextView) view.findViewById(R.id.tv_mediacontroll_share);
        content = (TextView) view.findViewById(R.id.tv_mediacontroll_content);
        mediacontroller_next= (ImageView) view.findViewById(R.id.iv_mediacontroller_next);
        mediacontroller_previous= (ImageView) view.findViewById(R.id.iv_mediacontroller_previous);
        //mediacontroller_pause= (ImageView) view.findViewById(R.id.iv_mediacontroller_play_pause);
        //currenttime= (TextView) view.findViewById(R.id.tv_mediacontroller_time_current);
        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        btn_cut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap currentFrame = activity.getCurrentFrame();
                CommUtil.saveImageToGallery(context,currentFrame);
            }
        });
        btn_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.share();
            }
        });
        mediacontroller_previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.reverseVideo();
            }
        });
        mediacontroller_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.speedVideo();
            }
        });
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //LogUtils.i(LogUtils.LOG_TAG, "onTouchEvent");
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        volume = -1;
                        brightness = -1f;
                        content.setVisibility(GONE);
                        if(isShowing()){
                            hide();
                            return true;
                        }

                        break;
                }
                return false;
            }
        });

        return view;
    }


    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
               content.setVisibility(View.GONE);
            }
        }
    };
    private void endGesture() {
        volume = -1;
        brightness = -1f;
        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 000);
    }
     class VolumeBrightnesGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = ((Activity) context).getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();
            if (mOldX > windowWidth * 4.0 / 5) {
               onVolumeSlide((mOldY - y) / windowHeight);
                return true;
            } else if (mOldX < windowWidth / 5.0) {
                onBrightnessSlide((mOldY - y) / windowHeight);
                return true;
            }
            return false;
        }
    }
    private void onVolumeSlide(float percent){
        if(volume==-1){
            volume=audioManager.getStreamVolume(audioManager.STREAM_MUSIC);
            if(volume<0)
                volume=0;
            content.setVisibility(View.VISIBLE);
        }
        int index = (int) (percent * maxVolume) + volume;
        if (index > maxVolume)
            index = maxVolume;
        else if (index < 0)
            index = 0;
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        content.setText("音量:"+(int)(index*100/maxVolume)+"%");
    }
    private void onBrightnessSlide(float percent) {
        if (brightness < 0) {
            brightness = ((Activity)context).getWindow().getAttributes().screenBrightness;
            if (brightness <= 0.00f)
                brightness = 0.50f;
            if (brightness < 0.01f)
               brightness = 0.01f;
            content.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = ((Activity) getContext()).getWindow().getAttributes();
        lpa.screenBrightness = brightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        ((Activity) context).getWindow().setAttributes(lpa);

        content.setText("亮度:"+(int)(lpa.screenBrightness*100)+"%");
    }
}
