package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gaop.huthelper.CustomDate;
import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.DateUtil;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ScreenUtils;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelper.view.CourseTable;
import com.gaop.huthelperdao.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 课程表
 * Created by 高沛 on 2016/7/25.
 */
public class CourseTableActivity extends BaseActivity {


    @BindView(R.id.fl_coursetable)
    FrameLayout flCoursetable;

    @BindView(R.id.tv_chooseweek_coursetable)
    TextView tvChoose;

    @BindView(R.id.iv_coursetable_update)
    ImageView ivCoursetableUpdate;

    CourseTable table;
    int CurrWeek = 0;
    int chooseNum;

    /**
     * 选择周数弹出窗口
     */
    protected PopupWindow weekListWindow;
    /**
     * 显示周数的listview
     */
    protected ListView weekListView;
    /**
     * 选择周数弹出窗口的layout
     */
    protected View popupWindowLayout;

    private CustomDate mClickDate;

    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_coursetable;
    }


    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        CurrWeek = DateUtil.getNowWeek();
        chooseNum = CurrWeek - 1;
        if (!PrefUtil.getBoolean(CourseTableActivity.this, "isLoadCourseTable", false)) {
            getCourseTableData();
        } else {
            setView();
        }
    }

    private void setView() {
        table = new CourseTable();
        table.setmContext(this);
        tvChoose.setText("第" + CurrWeek + "周");
        //设置选中日期
        if (mClickDate != null) {
            CustomDate mShowDate = mClickDate;
            int curMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);
            //获取周日
            if (mShowDate.day - mShowDate.week + 7 > curMonthDays) {
                if (mShowDate.month == 12) {
                    mShowDate.month = 1;
                    mShowDate.year += 1;
                } else {
                    mShowDate.month += 1;
                }
                mShowDate.day = (mShowDate.day - mShowDate.week - 1) + 7 - curMonthDays;
            } else {
                mShowDate.day = mShowDate.day - mShowDate.week + 7;
            }
            table.setShowDate(mShowDate);
        }

        this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_coursetable, table).commit();

    }

    @OnClick({R.id.tv_chooseweek_coursetable, R.id.iv_coursetable_update, R.id.imgbtn_toolbar_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_chooseweek_coursetable:
                if (fastClick())
                    showWeekListWindows(tvChoose);
                break;
            case R.id.iv_coursetable_update:
                getCourseTableData();
                break;
            case R.id.imgbtn_toolbar_back:
                //finish();
                startActivity(MainActivity.class);
                finish();
                break;
         /*   case R.id.iv_coursetable_explesson:
                startActivity(ExpLessonActivity.class);
                break;*/
        }


    }

    @Override
    public void onBackPressed() {
        startActivity(MainActivity.class);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        table.initDate();
    }

    /**
     * 设置周数列表PopupWindows
     * @param parent 相对位置View
     */
    private void showWeekListWindows(View parent) {
        if (!PrefUtil.getBoolean(CourseTableActivity.this, "isLoadCourseTable", false)) {
            ToastUtil.showToastShort("暂未导入课表");
            return;
        }
        int width = ScreenUtils.getScreenWidth(CourseTableActivity.this) / 2;
        if (weekListWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popupWindowLayout = layoutInflater.inflate(R.layout.popupwindow_coursetable, null);
            weekListView = (ListView) popupWindowLayout.findViewById(R.id.lv_weekchoose_coursetable);
            final List<String> weekList = new ArrayList<>();
            for (int i = 1; i <= 25; i++) {
                if (i == CurrWeek) {
                    weekList.add("第" + i + "周(本周)");
                } else
                    weekList.add("第" + i + "周");
            }

            weekListView.setAdapter(new WeekListAdapter(CourseTableActivity.this, weekList));
            weekListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    weekListWindow.dismiss();
                    tvChoose.setText(weekList.get(position));
                    chooseNum = position;
                    table.changeWeek(position + 1, DateUtil.getNextSunday(DateUtil.addDate(new Date(), (position + 1 - CurrWeek) * 7)));
                }
            });
            weekListWindow = new PopupWindow(popupWindowLayout, width, width + 100);
        }

        weekListView.post(new Runnable() {
            @Override
            public void run() {
                weekListView.smoothScrollToPosition(chooseNum);
            }
        });
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        weekListWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        weekListWindow.setFocusable(true);
        //设置点击外部可消失
        weekListWindow.setOutsideTouchable(true);
        weekListWindow.setBackgroundDrawable(new BitmapDrawable());
        weekListWindow.showAsDropDown(parent, -(width - parent.getWidth()) / 2, 20);
    }

    private void getCourseTableData() {
        User user = DBHelper.getUserDao().get(0);
        SubscriberOnNextListener getLessonData = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String o) {
                if ("ok".equals(o))
                    setView();
                else if ("令牌错误".equals(o)) {
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                    startActivity(ImportActivity.class);
                } else
                    ToastUtil.showToastShort(o);
            }
        };
        HttpMethods.getInstance().getCourseTable(CourseTableActivity.this,
                new ProgressSubscriber<String>(getLessonData, CourseTableActivity.this), user.getStudentKH(),
                user.getRember_code()
        );
    }

    class WeekListAdapter extends BaseAdapter {
        Context context;
        List<String> data;

        public WeekListAdapter(Context context, List<String> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_weeklist, null);
            } else {
                view = convertView;
            }
            TextView textView = (TextView) view.findViewById(R.id.tv_weeklist_item);
            if (position == table.CurrWeek - 1) {
                textView.setBackgroundResource(R.color.colorPrimary);
                textView.setTextColor(getResources().getColor(R.color.white));
            } else {
                textView.setBackgroundResource(R.color.transparent);
                textView.setTextColor(getResources().getColor(R.color.dull_grey));
            }
            textView.setText(data.get(position));
            return view;
        }
    }


}
