package com.gaop.huthelper.view.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.Lesson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop1 on 2016/7/29.
 */
public class CourseItemActivity extends BaseActivity {

    @BindView(R.id.tv_course_name)
    TextView tvCourseName;
    @BindView(R.id.tv_course_teacher)
    TextView tvCourseTeacher;
    @BindView(R.id.tv_course_time)
    TextView tvCourseTime;
    @BindView(R.id.tv_course_week)
    TextView tvCourseWeek;

    Lesson lesson;
    @BindView(R.id.tv_course_classroom)
    TextView tvCourseClassroom;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;


    private List<Long> IdList = new ArrayList<>();

    @Override
    public void initParms(Bundle parms) {
        lesson = (Lesson) parms.getSerializable("lesson");
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_courseitem;
    }


    @Override
    public void doBusiness(Context mContext) {

        ButterKnife.bind(this);
        tvToolbarTitle.setText("课程详情");
        tvCourseName.setText(lesson.getName());
        tvCourseTeacher.setText(lesson.getTeacher());
        tvCourseClassroom.setText(lesson.getRoom());
        tvCourseTime.setText("周" + lesson.getXqj() + " " + lesson.getDjj() + (lesson.getDjj() + 1) + "节");
        List<Lesson> list = DBHelper.getLessonDao();
        StringBuilder week = new StringBuilder();
        for (Lesson l : list) {
            if (l.getName().equals(lesson.getName()) && l.getXqj().equals(lesson.getXqj()) && lesson.getDjj().equals(l.getDjj())) {
                week.append(" ").append(l.getQsz()).append("-").append(l.getJsz());
                IdList.add(l.getId());
            }
        }
        week.append("周");
        if (!TextUtils.isEmpty(lesson.getDsz()))
            week.append("(").append(lesson.getDsz()).append(")");
        tvCourseWeek.setText(week);
        if (TextUtils.isEmpty(week)) {
            tvCourseWeek.setText(lesson.getIndex());
        }
    }


    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CourseItemActivity.this);
        builder.setMessage("确认移除该课程吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DBHelper.deleteLessonById(IdList);
                ToastUtil.showToastShort("删除成功！");
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    @OnClick({R.id.imgbtn_toolbar_back, R.id.iv_courseitem_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.iv_courseitem_delete:
                dialog();
                break;
        }
    }
}
