package com.gaop.huthelper.view.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.gaop.huthelper.utils.PrefUtil;

/**
 * 引导页Activity
 * Created by gaop1 on 2016/8/31.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!PrefUtil.getBoolean(SplashActivity.this,"isLoadUser",false)){
            startActivity(new Intent(SplashActivity.this,ImportActivity.class));
            finish();
        }else{
            mainhandler.sendEmptyMessageDelayed(0, 800);
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
