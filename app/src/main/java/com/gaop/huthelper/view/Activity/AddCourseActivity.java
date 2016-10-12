package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelper.utils.DensityUtils;
import com.gaop.huthelper.utils.ScreenUtils;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.Lesson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加课程
 * Created by gaop on 16-9-12.
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

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_addcourse;
    }

    @Override
    public void doBusiness(Context mContext) {

        ButterKnife.bind(this);
        tvToolbarTitle.setText("添加课程");
        rvWeekchoose.setAdapter(new MyGridViewAdapter(AddCourseActivity.this));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
            img.setBackgroundResource(R.color.gray);
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
