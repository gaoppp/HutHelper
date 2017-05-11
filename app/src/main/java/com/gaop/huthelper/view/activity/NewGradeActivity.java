package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelper.view.CirclePie;
import com.gaop.huthelper.view.ui.LineChartView;
import com.gaop.huthelperdao.Grade;
import com.gaop.huthelperdao.Ranking;
import com.gaop.huthelperdao.User;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by 高沛 on 2017/4/28.
 */

public class NewGradeActivity extends BaseActivity {

    private static final String TAG = "NewGradeActivity";
    @BindView(R.id.pie_grade_xf)
    CirclePie pieGradeXf;
    @BindView(R.id.tv_grade_avgjd)
    TextView tvGradeAvgjd;
    @BindView(R.id.tv_grade_nopassnum)
    TextView tvGradeNopassnum;
    @BindView(R.id.radio_group_segmented_control)
    RadioGroup radioGroupSegmentedControl;
    @BindViews({R.id.radio_grade_xq, R.id.radio_grade_xn})
    List<RadioButton> segmentedControls;
    @BindView(R.id.viewpager_grade)
    ViewPager viewpagerGrade;
    @BindView(R.id.scroll_grade_body)
    ScrollView scrollGradeBody;
    @BindView(R.id.tv_grade_empty)
    TextView tvGradeEmpty;

    private List<Ranking> xqBjRanking;
    private List<Ranking> xnBjRanking;
    private List<Ranking> xqNjRanking;
    private List<Ranking> xnNjRanking;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_grade;
    }


    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        if (!PrefUtil.getBoolean(NewGradeActivity.this, "isLoadGrade", false)) {
            loadGrade();
        } else {
            initData();
        }
    }

    private void initData() {
        //获取grade
        Grade grade = DBHelper.getGradeDao().get(0);
        if (grade.getAllNum() == null || grade.getAllNum() == 0) {
            scrollGradeBody.setVisibility(View.GONE);
            tvGradeEmpty.setVisibility(View.VISIBLE);
        } else {
            xqBjRanking = DBHelper.getBjAndXqRanking();
            xnBjRanking = DBHelper.getBjAndXnRanking();
            xqNjRanking = DBHelper.getNjAndXqRanking();
            xnNjRanking = DBHelper.getNjAndXnRanking();
            tvGradeAvgjd.setText("综合绩点   " + (float) (Math.round(grade.getAvgJd() * 100)) / 100);
            tvGradeNopassnum.setText("总挂科数   " + grade.getNoPassNum());
            pieGradeXf.setCurrNum(grade.getAllsf(), grade.getAllsf() - grade.getNopassxf());
            viewpagerGrade.setAdapter(pagerAdapter);
            viewpagerGrade.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    segmentedControls.get(position).setChecked(true);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        viewpagerGrade.setCurrentItem(0);
        segmentedControls.get(0).setChecked(true);
    }

    private void loadGrade() {
        final User user = DBHelper.getUserDao().get(0);
        SubscriberOnNextListener getGradeData = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String o) {
                if ("ok".equals(o)) {
                    loadRanking(user);
                } else if ("令牌错误".equals(o)) {
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                    startActivity(ImportActivity.class);
                } else
                    ToastUtil.showToastShort(o);
            }
        };
        HttpMethods.getInstance().getGradeData(NewGradeActivity.this,
                new ProgressSubscriber<String>(getGradeData, NewGradeActivity.this), user
        );
    }

    private void loadRanking(User user) {
        SubscriberOnNextListener getRankingData = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String o) {
                if ("ok".equals(o)) {
                    initData();
                } else if ("令牌错误".equals(o)) {
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                    startActivity(ImportActivity.class);
                } else
                    ToastUtil.showToastShort(o);
            }
        };
        HttpMethods.getInstance().getRankingData(NewGradeActivity.this,
                new ProgressSubscriber<String>(getRankingData, NewGradeActivity.this), user);
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.e(TAG, "instantiateItem: " + position);
            LineChartView view = new LineChartView(NewGradeActivity.this);
            if (position == 0) {
                view.setRankingData(xqBjRanking, xqNjRanking, true);
            } else {
                view.setRankingData(xnBjRanking, xnNjRanking, false);
            }
            container.addView(view);
            return view;
        }
    };


    @OnClick({R.id.imgbtn_toolbar_back, R.id.imgbtn_toolbar_refresh, R.id.btn_grade_showall})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.imgbtn_toolbar_refresh:
                loadGrade();
                break;
            case R.id.btn_grade_showall:
                if (PrefUtil.getBoolean(NewGradeActivity.this, "isLoadGrade", false)) {
                    startActivity(GradeListActivity.class);
                } else {
                    ToastUtil.showToastShort("暂未导入成绩");
                }
                break;
        }
    }

    //radio监听
    @OnCheckedChanged({R.id.radio_grade_xq, R.id.radio_grade_xn})
    public void setRadioGroupSegmentedControl(RadioButton radioButton, boolean isch) {
        if (isch) {
            viewpagerGrade.setCurrentItem(segmentedControls.indexOf(radioButton));
        }

    }

}
