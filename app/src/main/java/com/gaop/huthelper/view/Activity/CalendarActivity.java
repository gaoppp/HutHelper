package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.gaop.huthelper.Model.HoliDay;
import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.AutoRVAdapter;
import com.gaop.huthelper.adapter.ViewHolder;
import com.gaop.huthelper.utils.DateUtil;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by gaop1 on 2016/9/2.
 */
public class CalendarActivity extends BaseActivity {
    @BindView(R.id.toolba_calendar)
    Toolbar toolbaCalendar;
    @BindView(R.id.rv_calendar_date)
    RecyclerView rvCalendarDate;
    @BindView(R.id.iv_calendar)
    ImageView ivCalendar;

    private List<HoliDay> holiDays;

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
        PhotoViewAttacher attacher = new PhotoViewAttacher(ivCalendar);
        attacher.update();
        holiDays = new ArrayList<>();
        HoliDay holiDay = new HoliDay("中秋节", new Date(2016 - 1900, 8, 15));
        //int days = DateUtil.getIntervalDays(new Date(), holiDay.getTime());
        addHoliday(holiDay);
        holiDay = new HoliDay("国庆节", new Date(2016 - 1900, 9, 1));
        addHoliday(holiDay);
        holiDay = new HoliDay("四级考试", new Date(2016 - 1900, 11, 17));
        addHoliday(holiDay);
        holiDay = new HoliDay("考研", new Date(2016 - 1900, 11, 24));
        addHoliday(holiDay);
        holiDay = new HoliDay("寒假", new Date(2017 - 1900, 0, 9));
        addHoliday(holiDay);

        rvCalendarDate.setLayoutManager(new LinearLayoutManager(CalendarActivity.this, LinearLayoutManager.HORIZONTAL, false));

        DateListAdapter adapter = new DateListAdapter(CalendarActivity.this, holiDays);
        rvCalendarDate.setAdapter(adapter);
    }

    private void addHoliday(HoliDay holiDay) {
        int days = DateUtil.getIntervalDays(new Date(), holiDay.getTime());
        if (days >= 0) {
            holiDays.add(holiDay);
        }
    }


    @OnClick({R.id.rv_calendar_date, R.id.iv_calendar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rv_calendar_date:
                break;
            case R.id.iv_calendar:
                break;
        }
    }

    class DateListAdapter extends AutoRVAdapter {

        public DateListAdapter(Context context, List<HoliDay> list) {
            super(context, list);
        }

        @Override
        public int onCreateViewHolder(int viewType) {
            return R.layout.item_time_day;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int days = DateUtil.getIntervalDays(new Date(), holiDays.get(position).getTime());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
            holder.getTextView(R.id.show_time).setText(formatter.format(holiDays.get(position).getTime()));
            holder.getTextView(R.id.date_title).setText(holiDays.get(position).getName() + "\n" + days + "天");
        }

    }
}
