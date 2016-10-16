package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.AutoRVAdapter;
import com.gaop.huthelper.adapter.ViewHolder;
import com.gaop.huthelper.jiekou.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.DateUtil;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.Explesson;
import com.gaop.huthelperdao.User;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop on 16-10-9.
 */

public class ExpLessonActivity extends BaseActivity {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.rv_explesson)
    RecyclerView rvExplesson;
    @BindView(R.id.tv_explesson_empty)
    TextView tvExplessonEmpty;
    private int currweek = 0;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_explesson;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("实验课表");
        currweek = DateUtil.getNowWeek();
        if (!PrefUtil.getBoolean(ExpLessonActivity.this, "isLoadExpLesson", false)) {
            LoadExpLesson();
        } else {
            InitData();
        }
    }

    @OnClick({R.id.imgbtn_toolbar_back, R.id.iv_explesson_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.iv_explesson_update:
                LoadExpLesson();
                break;
        }
    }

    private void LoadExpLesson() {
        User user = DBHelper.getUserDao().get(0);
        SubscriberOnNextListener getGradeData = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String o) {
                if ("ok".equals(o))
                    InitData();
                else if ("令牌错误".equals(o)) {
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                    startActivity(ImportActivity.class);
                } else
                    ToastUtil.showToastShort(o);
            }
        };
        HttpMethods.getInstance().GetExpLessons(ExpLessonActivity.this,
                new ProgressSubscriber<String>(getGradeData, ExpLessonActivity.this),
                user.getStudentKH(), user.getRember_code());
    }

    private void InitData() {
        List<Explesson> list = DBHelper.getExpLessons();
        Collections.sort(list);
        if (list.size() != 0) {
            ExpLessonAdapter adapter = new ExpLessonAdapter(ExpLessonActivity.this, list, true);
            rvExplesson.setLayoutManager(new LinearLayoutManager(ExpLessonActivity.this, LinearLayoutManager.VERTICAL, false));
            rvExplesson.setAdapter(adapter);
        } else {
            tvExplessonEmpty.setVisibility(View.VISIBLE);
            rvExplesson.setVisibility(View.GONE);

        }

    }

    class ExpLessonAdapter extends AutoRVAdapter {

        public ExpLessonAdapter(Context context, List<Explesson> list, boolean isrecyable) {
            super(context, list, isrecyable);
        }

        @Override
        public int onCreateViewHolder(int viewType) {
            return R.layout.item_explesson;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Explesson explesson = (Explesson) list.get(position);
            TextView tvLesson = holder.getTextView(R.id.tv_expitem_lesson);
            TextView tvFinish = holder.getTextView(R.id.tv_expitem_finish);
            tvLesson.setText(explesson.getLesson());
            TextView tvObj = holder.getTextView(R.id.tv_expitem_obj);
            holder.getTextView(R.id.tv_expitem_realtime).setText(explesson.getWeeks_no() + "周 星期" + explesson.getWeek()
                    + " " + explesson.getReal_time());
            holder.getTextView(R.id.tv_expitem_place).setText(explesson.getLocate());
            holder.getTextView(R.id.tv_expitem_timesum).setText(explesson.getPeriod() + "学时");
            holder.getTextView(R.id.tv_expitem_teacher).setText(explesson.getTeacher());
            if (TextUtils.isEmpty(explesson.getObj())) {
                tvObj.setVisibility(View.GONE);
            } else {
                tvObj.setText(explesson.getObj());
            }
            if (Integer.valueOf(explesson.getWeeks_no()) < currweek || (Integer.valueOf(explesson.getWeeks_no())) == currweek && DateUtil.getWeekOfToday() > Integer.valueOf(explesson.getWeek())) {
                tvFinish.setVisibility(View.VISIBLE);
            } else {
                tvFinish.setVisibility(View.GONE);
            }
        }
    }
}
