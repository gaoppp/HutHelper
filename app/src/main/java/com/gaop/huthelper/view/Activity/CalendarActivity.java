package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gaop.huthelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaop1 on 2016/9/2.
 */
public class CalendarActivity extends BaseActivity {
    @BindView(R.id.toolba_calendar)
    Toolbar toolbaCalendar;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_calendar;
    }



    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        toolbaCalendar.setTitle("校历");
        setSupportActionBar(toolbaCalendar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbaCalendar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



}
