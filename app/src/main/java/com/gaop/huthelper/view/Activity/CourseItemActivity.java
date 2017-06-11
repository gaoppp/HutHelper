package com.gaop.huthelper.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.utils.DensityUtils;
import com.gaop.huthelper.utils.ScreenUtils;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.Lesson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;




/**
 * 添加课程
 * Created by 高沛 on 16-9-12.
 */
public class CourseItemActivity extends BaseActivity {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.imgbtn_toolbar_edit)
    ImageButton imgbtnToolbarEdit;
    @BindView(R.id.imgbtn_toolbar_delete)
    ImageButton imgbtnToolbarDelete;
    @BindView(R.id.imgbtn_toolbar_ok)
    ImageButton imgbtnToolbarOk;
    @BindView(R.id.tv_course_message)
    TextView tvCourseMessage;
    @BindView(R.id.gv_course_weeks)
    GridView gvCourseWeeks;
    @BindView(R.id.tv_course_name)
    TextView tvCourseName;
    @BindView(R.id.et_course_name)
    EditText etCourseName;
    @BindView(R.id.tv_course_time)
    TextView tvCourseTime;
    @BindView(R.id.spinner_course_week)
    Spinner spinnerCourseWeek;
    @BindView(R.id.spinner_course_num)
    Spinner spinnerCourseNum;
    @BindView(R.id.tv_course_classroom)
    TextView tvCourseClassroom;
    @BindView(R.id.et_course_classroom)
    EditText etCourseClassroom;
    @BindView(R.id.tv_course_teacher)
    TextView tvCourseTeacher;
    @BindView(R.id.et_course_teacher)
    EditText etCourseTeacher;
    @BindView(R.id.ll_course_time)
    LinearLayout llCourseTime;

    private boolean[] weeklist = new boolean[20];

    private final int EDIT_COURSE = 0;
    private static final int ADD_COURSE = 1;
    private static final int SHOW_COURSE = 2;

    private int type;
    private Lesson lesson;
    private List<Long> idList = new ArrayList<>();

    @Override
    public void initParms(Bundle parms) {
        if (parms != null) {
            type = parms.getInt("type");
            if (type == SHOW_COURSE) {
                lesson = (Lesson) parms.getSerializable("lesson");
            }
        } else {
            type = ADD_COURSE;
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_addcourse;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        if (type == SHOW_COURSE) {
            tvToolbarTitle.setText("课程详情");
            setShowCourse();
            setCourseData();
        } else {
            tvToolbarTitle.setText("添加课程");
            setEditCourse();
        }
        gvCourseWeeks.setAdapter(new MyGridViewAdapter(CourseItemActivity.this));
    }

    private void setCourseData() {
        tvCourseName.setText(lesson.getName());
        tvCourseTime.setText("周" + lesson.getXqj() + " " + lesson.getDjj() +" "+ (lesson.getDjj() + 1) + "节");
        tvCourseTeacher.setText(lesson.getTeacher());
        tvCourseClassroom.setText(lesson.getRoom());

        //因为一节课在不连续的周数上课 会拆成几个课程记录 所以这里要遍历找出这节课的所有ID
        List<Lesson> list = DBHelper.getLessonDao();
        for (Lesson l : list) {
            if (l.getName().equals(lesson.getName()) && l.getXqj().equals(lesson.getXqj()) && lesson.getDjj().equals(l.getDjj())) {
                idList.add(l.getId());
                String[] ws = lesson.getIndex().split(" ");
                for (String s : ws) {
                    if (!TextUtils.isEmpty(s)) {
                        int num = Integer.valueOf(s);
                        if (num < 20)
                            weeklist[num - 1] = true;
                    }
                }
            }
        }
    }

    private void editCourseData() {
        etCourseName.setText(lesson.getName());
        spinnerCourseWeek.setSelection(lesson.getXqj() - 1, true);
        spinnerCourseNum.setSelection((lesson.getDjj() + 1) / 2 - 1, true);
        etCourseTeacher.setText(lesson.getTeacher());
        etCourseClassroom.setText(lesson.getRoom());
    }

    //展示数据模式
    private void setShowCourse() {
        tvCourseMessage.setText("周数");

        etCourseName.setVisibility(View.GONE);
        etCourseClassroom.setVisibility(View.GONE);
        etCourseTeacher.setVisibility(View.GONE);
        llCourseTime.setVisibility(View.GONE);

        tvCourseClassroom.setVisibility(View.VISIBLE);
        tvCourseName.setVisibility(View.VISIBLE);
        tvCourseTeacher.setVisibility(View.VISIBLE);
        tvCourseTime.setVisibility(View.VISIBLE);

        imgbtnToolbarOk.setVisibility(View.GONE);
        imgbtnToolbarDelete.setVisibility(View.VISIBLE);
        imgbtnToolbarEdit.setVisibility(View.VISIBLE);

    }

    //编辑数据模式
    private void setEditCourse() {
        tvCourseMessage.setText("周数   点击下方选择");
        etCourseName.setVisibility(View.VISIBLE);
        etCourseClassroom.setVisibility(View.VISIBLE);
        etCourseTeacher.setVisibility(View.VISIBLE);
        llCourseTime.setVisibility(View.VISIBLE);

        tvCourseClassroom.setVisibility(View.GONE);
        tvCourseName.setVisibility(View.GONE);
        tvCourseTeacher.setVisibility(View.GONE);
        tvCourseTime.setVisibility(View.GONE);

        imgbtnToolbarOk.setVisibility(View.VISIBLE);
        imgbtnToolbarDelete.setVisibility(View.GONE);
        imgbtnToolbarEdit.setVisibility(View.GONE);
    }

    @OnClick({R.id.imgbtn_toolbar_back, R.id.imgbtn_toolbar_edit, R.id.imgbtn_toolbar_delete, R.id.imgbtn_toolbar_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.imgbtn_toolbar_edit:
                type = EDIT_COURSE;
                tvToolbarTitle.setText("编辑课程");
                setEditCourse();
                editCourseData();
                break;
            case R.id.imgbtn_toolbar_delete:
                deleteCourse();
                break;
            case R.id.imgbtn_toolbar_ok:
                if (fastClick()) {
                    try {
                        if (!etCourseName.getText().toString().equals("")) {
                            Lesson lesson = new Lesson();
                            int courseweek = spinnerCourseWeek.getSelectedItemPosition() + 1;
                            int coursenum = spinnerCourseNum.getSelectedItemPosition() + 1;
                            lesson.setXqj(courseweek);
                            lesson.setDjj(coursenum * 2 - 1);
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < weeklist.length; i++) {
                                if (weeklist[i])
                                    sb.append(" ").append(i + 1);
                            }
                            lesson.setIndex(sb.toString());
                            lesson.setName(etCourseName.getText().toString());
                            lesson.setTeacher(etCourseTeacher.getText().toString());
                            lesson.setRoom(etCourseClassroom.getText().toString());
                            if (type == EDIT_COURSE) {
                                DBHelper.deleteLessonById(idList);
                                lesson.setAddbyuser(false);
                                DBHelper.insertLesson(lesson);
                                ToastUtil.showToastShort("编辑成功");
                            } else {
                                lesson.setAddbyuser(true);
                                DBHelper.insertLesson(lesson);
                                ToastUtil.showToastShort("添加成功");
                            }
                            finish();
                        } else {
                            ToastUtil.showToastShort("请填写课程名");
                        }
                        type = SHOW_COURSE;
                    } catch (Exception e) {
                        ToastUtil.showToastShort("添加出现错误");
                    }
                }
                break;
        }
    }

    protected void deleteCourse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CourseItemActivity.this);
        builder.setMessage("确认移除该课程吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DBHelper.deleteLessonById(idList);
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

    public class MyGridViewAdapter extends BaseAdapter {
        Context context;

        boolean[] list;

        public MyGridViewAdapter(Context context) {
            this.context = context;
            this.list = weeklist;
        }

        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public Object getItem(int position) {
            return list[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            final Button img = new Button(context);
            int width = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 45);// 获取屏幕宽度
            int height = 0;
            width = width / 5;// 对当前的列数进行设置imgView的宽度
            height = width * 5 / 6;
            if (!weeklist[position]) {
                img.setBackgroundResource(R.color.new_grty);
            } else {
                img.setBackgroundResource(R.color.colorPrimary);
            }
            img.setText("" + (position + 1));
            img.setTextColor(CourseItemActivity.this.getResources().getColor(R.color.white));
            AbsListView.LayoutParams layout = new AbsListView.LayoutParams(width, height);
            img.setLayoutParams(layout);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == SHOW_COURSE)
                        return;
                    if (weeklist[position]) {
                        img.setBackgroundResource(R.color.new_grty);
                        weeklist[position] = false;
                    } else if (!weeklist[position]) {
                        img.setBackgroundResource(R.color.colorPrimary);
                        weeklist[position] = true;
                    }
                }
            });
            return img;
        }
    } 
}
