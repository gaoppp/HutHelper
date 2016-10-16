package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.Model.ExamData;
import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.AutoRVAdapter;
import com.gaop.huthelper.adapter.ViewHolder;
import com.gaop.huthelper.jiekou.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.Exam;
import com.gaop.huthelperdao.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop on 16-10-14.
 */

public class ExamActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.rv_exam)
    RecyclerView rvExam;
    @BindView(R.id.tv_exam_empty)
    TextView tvExamEmpty;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_exam;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("考试时间");
        if (!PrefUtil.getBoolean(ExamActivity.this, "isLoadExam", false)) {
            LoadExam();
        } else {
            InitData();
        }
    }

    private void LoadExam() {
        User user = DBHelper.getUserDao().get(0);
        SubscriberOnNextListener getExamData = new SubscriberOnNextListener<ExamData>() {
            @Override
            public void onNext(ExamData o) {
                if (o.getCode() == 100)
                    InitData();
                else
                    ToastUtil.showToastShort(o.getMessage());
            }
        };
        HttpMethods.getInstance().GetExamData(ExamActivity.this,
                new ProgressSubscriber<ExamData>(getExamData, ExamActivity.this),
                user.getStudentKH());
    }

    private void InitData() {
        List<Exam> list = DBHelper.getExam();
        //Collections.sort(list);
        if (list.size() != 0) {
            ExamAdapter adapter = new ExamAdapter(ExamActivity.this, list);
            rvExam.setLayoutManager(new LinearLayoutManager(ExamActivity.this, LinearLayoutManager.VERTICAL, false));
            rvExam.setAdapter(adapter);
        } else {
            tvExamEmpty.setVisibility(View.VISIBLE);
            rvExam.setVisibility(View.GONE);

        }
    }

    @OnClick({R.id.imgbtn_toolbar_back, R.id.iv_exam_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.iv_exam_update:
                LoadExam();
                break;
        }
    }

    class ExamAdapter extends AutoRVAdapter {

        public ExamAdapter(Context context, List<Exam> list) {
            super(context, list);
        }

        @Override
        public int onCreateViewHolder(int viewType) {
            return R.layout.item_exam;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Exam exam = (Exam) list.get(position);
            holder.getTextView(R.id.tv_examitem_name).setText(exam.getCourseName());
            holder.getTextView(R.id.tv_examitem_when).setText(exam.getWeek_Num() + "周 " + exam.getStarttime() + "-" + exam.getEndTime());
            if (exam.getIsset().equals("0"))
                holder.getTextView(R.id.tv_examitem_how).setText("计划中");
            else if (exam.getIsset().equals("1"))
                holder.getTextView(R.id.tv_examitem_how).setText("执行中");
            holder.getTextView(R.id.tv_examitem_where).setText(exam.getRoomName());

        }
    }
}
