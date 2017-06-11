package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.model.entity.ExamData;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.DateUtil;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelper.view.adapter.AutoRVAdapter;
import com.gaop.huthelper.view.adapter.ViewHolder;
import com.gaop.huthelperdao.Exam;
import com.gaop.huthelperdao.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 考试查询
 * Created by 高沛 on 16-10-14.
 */

public class ExamActivity extends BaseActivity {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.rv_exam)
    RecyclerView rvExam;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.ll_exam_root)
    LinearLayout rootView;

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
        tvToolbarTitle.setText("考试查询");
        //判断是否导入数据
        if (!PrefUtil.getBoolean(ExamActivity.this, "isLoadExam", false)) {
            loadExam();
        } else {
            initData();
        }
    }

    /**
     * 导入数据
     */
    private void loadExam() {
        User user = DBHelper.getUserDao().get(0);
        SubscriberOnNextListener getExamData = new SubscriberOnNextListener<ExamData>() {
            @Override
            public void onNext(ExamData o) {
                if (o.getCode() == 100)
                    initData();
                else
                    ToastUtil.showToastShort(o.getMessage());
            }
        };
        HttpMethods.getInstance().GetExamData(ExamActivity.this,
                new ProgressSubscriber<ExamData>(getExamData, ExamActivity.this),
                user.getStudentKH());
    }

    /**
     * 初始化界面
     */
    private void initData() {
        List<Exam> list = DBHelper.getExam();
        if (list.size() != 0) {
            ExamAdapter adapter = new ExamAdapter(ExamActivity.this, list);
            rvExam.setLayoutManager(new LinearLayoutManager(ExamActivity.this, LinearLayoutManager.VERTICAL, false));
            rvExam.setAdapter(adapter);
        } else {
            rlEmpty.setVisibility(View.VISIBLE);
            rvExam.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.imgbtn_toolbar_back, R.id.imgbtn_toolbar_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.imgbtn_toolbar_refresh:
                loadExam();
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
            holder.getTextView(R.id.tv_examitem_where).setText(exam.getRoomName());

            if (exam.getStarttime() == null && exam.getEndTime() == null) {
                holder.getTextView(R.id.tv_examitem_day).setText(exam.getWeek_Num() + "周 ");
                if(!TextUtils.isEmpty(exam.getWeek_Num())) {
                    int weeknum = Integer.parseInt(exam.getWeek_Num());
                    int currweek = DateUtil.getNowWeek();
                    if (weeknum > currweek) {
                        holder.getTextView(R.id.tv_examitem_countdown).setText("剩余"+(weeknum-currweek) + "周");
                    } else if (weeknum == currweek) {
                        holder.getTextView(R.id.tv_examitem_countdown).setText("本周");
                    } else {
                        holder.getTextView(R.id.tv_examitem_countdown).setText("已结束");
                    }
                    return;
                }
                holder.getTextView(R.id.tv_examitem_countdown).setText("无");
            } else {
                Date begindate = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    begindate = sdf.parse(exam.getStarttime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                    if (begindate != null) {
                        int num = DateUtil.getIntervalDays(new Date(), begindate) + 1;
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                        if (num >= 1) {
                            if (formatter.format(begindate).equals(formatter.format(new Date()))) {
                                holder.getTextView(R.id.tv_examitem_countdown).setText("今天");
                            } else {
                                holder.getTextView(R.id.tv_examitem_countdown).setText("剩余"+(num) + "天");
                            }
                        } else {
                            holder.getTextView(R.id.tv_examitem_countdown).setText("已结束");
                        }

                    }
                    String date = exam.getStarttime().substring(0, 10);
                    String begin = exam.getStarttime().substring(11, 16);
                    String end = exam.getEndTime().substring(11, 16);
                    //holder.getTextView(R.id.tv_examitem_when).setText(exam.getWeek_Num() + "周/" + date + "/" + begin + "-" + end);
                    holder.getTextView(R.id.tv_examitem_day).setText(date);
                    holder.getTextView(R.id.tv_examitem_time).setText("("+exam.getWeek_Num()+"周 "+ begin + "-" + end+")");

            }


        }
    }
}
