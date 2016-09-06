package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gaop.huthelper.R;

public class AboutActivity extends BaseActivity {
    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_about;
    }



    @Override
    public void doBusiness(Context mContext) {
        Toolbar mToolbar= (Toolbar) findViewById(R.id.toolba_about);
        mToolbar.setTitle("关于我们");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tv=(TextView)findViewById(R.id.tv_email);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("mailto:gaopdev@gmail.com");
                String[] email = {"gaopdev@gmail.com"};
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
                intent.putExtra(Intent.EXTRA_SUBJECT, "邮件主题"); // 主题
                intent.putExtra(Intent.EXTRA_TEXT, "邮件正文"); // 正文
                startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
