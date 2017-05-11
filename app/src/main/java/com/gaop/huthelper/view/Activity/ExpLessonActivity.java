package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.User;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 实验课表
 * Created by 高沛 on 16-10-9.
 */

public class ExpLessonActivity extends FragmentActivity {

    @BindView(R.id.vPager)
    ViewPager viewPager;
    @BindView(R.id.imgbtn_toolbar_back)
    ImageButton imgbtnToolbarBack;
    @BindView(R.id.radio_group_segmented_control)
    RadioGroup radioGroupSegmentedControl;
    @BindViews({R.id.radio_nofinish, R.id.radio_finish})
    List<RadioButton> segmentedControls;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    /**
     * 未使用BaseActivity 需要处理状态栏/导航栏
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_explesson);
        ButterKnife.bind(this);
        tvToolbarTitle.setText("实验课表");
        doBusiness(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void doBusiness(Context mContext) {

        if (!PrefUtil.getBoolean(ExpLessonActivity.this, "isLoadExpLesson", false)) {
            viewPager.setVisibility(View.GONE);
            loadExpLesson();
        } else {
            initData();
        }
    }

    @OnClick({R.id.imgbtn_toolbar_back, R.id.imgbtn_toolbar_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.imgbtn_toolbar_refresh:
                loadExpLesson();
                break;

        }
    }

    //radio监听
    @OnCheckedChanged({R.id.radio_nofinish, R.id.radio_finish})
    public void setRadioGroupSegmentedControl(RadioButton radioButton, boolean isch) {
        if (isch) {
            viewPager.setCurrentItem(segmentedControls.indexOf(radioButton));
        }

    }

    //导入数据
    private void loadExpLesson() {
        User user = DBHelper.getUserDao().get(0);
        SubscriberOnNextListener getGradeData = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String o) {
                if ("ok".equals(o)) {
                    viewPager.setVisibility(View.VISIBLE);
                    initData();
                } else if ("令牌错误".equals(o)) {
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                    startActivity(new Intent(ExpLessonActivity.this, ImportActivity.class));
                    finish();
                } else {
                    ToastUtil.showToastShort(o);
                    finish();
                }
            }
        };
        HttpMethods.getInstance().GetExpLessons(ExpLessonActivity.this,
                new ProgressSubscriber<String>(getGradeData, ExpLessonActivity.this),
                user.getStudentKH(), user.getRember_code());
    }

    /**
     * 初始化界面
     */
    private void initData() {
        final Fragment[] fragemts = new Fragment[2];
        fragemts[0] = ExpLessonFragment.newInstance(0);
        fragemts[1] = ExpLessonFragment.newInstance(1);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragemts[position];
            }

            @Override
            public int getCount() {
                return fragemts.length;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        viewPager.setCurrentItem(0);
        segmentedControls.get(0).setChecked(true);
    }
}
