package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.R;
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
public class AddCourseActivity extends BaseActivity {


    @BindView(R.id.tv_course_name)
    EditText etCourseName;
    @BindView(R.id.tv_course_teacher)
    EditText etCourseTeacher;
    @BindView(R.id.tv_course_classroom)
    EditText etCourseClassroom;
    @BindView(R.id.rv_weekchoose)
    GridView rvWeekchoose;
    @BindView(R.id.spinner_addcourse_week)
    Spinner spinnerAddcourseWeek;
    @BindView(R.id.spinner_addcourse_num)
    Spinner spinnerAddcourseNum;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    private boolean[] weeklist = new boolean[20];

    private static final int EDIT_COURSE = 0;
    private static final int ADD_COURSE = 1;

    private int type;
    private Lesson lesson;
    private List<Long> idList = new ArrayList<>();

    @Override
    public void initParms(Bundle parms) {
        if (parms != null) {
            type = parms.getInt("type");
            if (type == EDIT_COURSE) {
                lesson = (Lesson) parms.getSerializable("lesson");
            }
        } else {
            type = ADD_COURSE;
        }
    }

    private void initView() {
        etCourseName.setText(lesson.getName());
        etCourseTeacher.setText(lesson.getTeacher());
        etCourseClassroom.setText(lesson.getRoom());
        spinnerAddcourseWeek.setSelection(lesson.getXqj() - 1, true);
        spinnerAddcourseNum.setSelection((lesson.getDjj() + 1) / 2 - 1, true);
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

    @Override
    public int bindLayout() {
        return R.layout.activity_addcourse;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        if (type == EDIT_COURSE) {
            tvToolbarTitle.setText("编辑课程");
            initView();
        } else {
            tvToolbarTitle.setText("添加课程");
        }
        Log.e("tag5", weeklist + "");

        rvWeekchoose.setAdapter(new MyGridViewAdapter(AddCourseActivity.this));
    }


    @OnClick({R.id.imgbtn_toolbar_back, R.id.iv_addcourse_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.iv_addcourse_ok:
                if (fastClick()) {
                    try {
                        if (!etCourseName.getText().toString().equals("")) {
                            Lesson lesson = new Lesson();
                            int courseweek = spinnerAddcourseWeek.getSelectedItemPosition() + 1;
                            int coursenum = spinnerAddcourseNum.getSelectedItemPosition() + 1;
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
                            }else{
                                lesson.setAddbyuser(true);
                            }
                            DBHelper.insertLesson(lesson);
                            ToastUtil.showToastShort("添加成功");
                            finish();
                        } else {
                            ToastUtil.showToastShort("请填写课程名");
                        }
                    } catch (Exception e) {
                        ToastUtil.showToastShort("添加出现错误");
                    }
                }
                break;
        }
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
            int width = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 60);// 获取屏幕宽度
            int height = 0;
            width = width / 5;// 对当前的列数进行设置imgView的宽度
            height = width * 4 / 5;
            if(!weeklist[position]){
                img.setBackgroundResource(R.color.gray);
            }else {
                img.setBackgroundResource(R.color.blue);
            }

            img.setText("" + (position + 1));
            AbsListView.LayoutParams layout = new AbsListView.LayoutParams(width, height);
            img.setLayoutParams(layout);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (weeklist[position]) {
                        img.setBackgroundResource(R.color.gray);
                        weeklist[position] = false;
                    } else if (!weeklist[position]) {
                        img.setBackgroundResource(R.color.blue);
                        weeklist[position] = true;
                    }
                }
            });
            return img;
        }
    }
}
