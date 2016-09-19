package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelperdao.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop1 on 2016/5/22.
 */
public class UserActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.btn_user_changepw)
    Button btnUserChangepw;
    private User mUser;
    private TextView mName, mSex, mNum, mSchool, mClass;
    private Toolbar mToolbar;
    private RelativeLayout mRl_name, mRl_sex, mRl_num, mRl_school, mRl_class;


    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_user;
    }


    @Override
    public void doBusiness(Context mContext) {
        initView();
        initData();
    }

    private void initView() {
        mName = (TextView) findViewById(R.id.tv_user_name);
        mSchool = (TextView) findViewById(R.id.tv_user_school);
        mSex = (TextView) findViewById(R.id.tv_user_sex);
        mClass = (TextView) findViewById(R.id.tv_user_class);
        mNum = (TextView) findViewById(R.id.tv_user_num);
        mToolbar = (Toolbar) findViewById(R.id.toolba_user);
        mToolbar.setTitle("个人信息");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRl_class = (RelativeLayout) findViewById(R.id.rl_user_class);
        mRl_name = (RelativeLayout) findViewById(R.id.rl_user_name);
        mRl_num = (RelativeLayout) findViewById(R.id.rl_user_num);
        mRl_sex = (RelativeLayout) findViewById(R.id.rl_user_sex);
        mRl_school = (RelativeLayout) findViewById(R.id.rl_user_school);
        mRl_school.setOnClickListener(this);
        mRl_sex.setOnClickListener(this);
        mRl_num.setOnClickListener(this);
        mRl_class.setOnClickListener(this);
        mRl_name.setOnClickListener(this);

    }

    private void initData() {
        mUser = DBHelper.getUserDao().get(0);

        mName.setText(mUser.getTrueName());
        mSex.setText(mUser.getSex());
        mSchool.setText(mUser.getDep_name());
        mNum.setText(mUser.getStudentKH());
        mClass.setText(mUser.getClass_name());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_user_name:
                break;
            case R.id.rl_user_sex:
                break;
            case R.id.rl_user_num:
                break;
            case R.id.rl_user_school:
                // getSchoolList();
                break;
            case R.id.rl_user_class:

                //getClassList();
                break;


        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_user_changepw)
    public void onClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("type", WebViewActivity.TYPE_CHANGE_PW);
        startActivity(WebViewActivity.class, bundle);
    }

//    private void showChooseDialog(final int type, final List<ClassInf> list) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        final String[] strings = new String[list.size()];
//        for(int i=0;i<strings.length;i++){
//            strings[i]=list.get(i).getClassName();
//        }
//        if (type == 0) {
//            builder.setTitle("选择学院");
//        } else if (type == 1) {
//            builder.setTitle("选择班级");
//        }
//        builder.setItems(strings, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (type == 0) {
//                    mUser.setDepNo(list.get(which).getClassNo());
//                    mUser.setDepartment(list.get(which).getClassName());
//                    mUser.updateAll();
//
//                } else if (type == 1) {
//                    mUser.setClassNo(list.get(which).getClassNo());
//                    mUser.setClassname(list.get(which).getClassName());
//                    mUser.updateAll();
//                }
//                initData();
//            }
//        });
//        builder.show();
//    }

}
