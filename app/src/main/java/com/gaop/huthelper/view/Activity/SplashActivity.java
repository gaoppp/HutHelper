package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.app.MApplication;
import com.gaop.huthelper.db.DBHelper;

/**
 * 引导页Activity
 * Created by 高沛 on 2016/8/31.
 */
public class SplashActivity extends BaseActivity {


    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.act_splash;
    }

    private final int[] bgs = {R.mipmap.bg_start_1, R.mipmap.bg_start_2, R.mipmap.bg_start_3, R.mipmap.bg_start_4, R.mipmap.bg_start_5};

    @Override
    public void doBusiness(Context mContext) {
        ImageView ivMain = (ImageView) findViewById(R.id.iv_splash);
        int bgRes = bgs[(int) (Math.random() * 4)];//随机获取背景色
        ivMain.setImageResource(bgRes);
        if (DBHelper.getUserDao().size() == 0) {
            startActivity(new Intent(SplashActivity.this, ImportActivity.class));
            finish();
        } else {
            MApplication.setUser(DBHelper.getUserDao().get(0));
            //动画效果启动
//            ScaleAnimation animation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            animation.setDuration(2000);//设置动画持续时间
//            animation.setFillAfter(true);
//            animation.setFillBefore(false);
//            animation.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                    finish();
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//            ivMain.startAnimation(animation);

            //普通启动
             mainhandler.sendEmptyMessageDelayed(0, 1000);
        }
    }

    private Handler mainhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    };

}
