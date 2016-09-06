package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelperdao.User;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by gaop1 on 2016/5/22.
 */
public class UserActivity extends BaseActivity implements View.OnClickListener {
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


    public void getSchoolList() {
//        OkHttpUtils.get().url(getResources().getText(R.string.stuClass).toString()).build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e) {
//                mDiolog.dismiss();
//                Toast.makeText(MyApplication.AppContext, "获取列表失败！", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    List<ClassInf> schoolList = new ArrayList<>();
//                    com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(response);
//                    for (int i = 0; i < jsonArray.size(); i++) {
//                        com.alibaba.fastjson.JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        String s = jsonObject.getString("depName");
//                        schoolList.add(new ClassInf(jsonObject.getString("depNo"),jsonObject.getString("depName")));
//                    }
//                    mDiolog.dismiss();
//                    showChooseDialog(0, schoolList);
//                } catch (com.alibaba.fastjson.JSONException e) {
//                    mDiolog.dismiss();
//                    Toast.makeText(MyApplication.AppContext, "解析数据失败！", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    public void getClassList() {
//        OkHttpUtils.get().url(getResources().getText(R.string.stuClass).toString()).build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e) {
//                mDiolog.dismiss();
//                Toast.makeText(MyApplication.AppContext, "获取列表失败！", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//                try {
//
//                    com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(response);
//                    for (int i = 0; i < jsonArray.size(); i++) {
//                        com.alibaba.fastjson.JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        if ((jsonObject.getString("depName")).equals(mUser.getDepartment())) {
//                            List<ClassInf> classList = new ArrayList<>();
//                            jsonArray=jsonObject.getJSONArray("classes");
//                            for(int j=0;j<jsonArray.size();j++){
//                                jsonObject=jsonArray.getJSONObject(j);
//                                classList.add(new ClassInf(jsonObject.getString("classNo"),jsonObject.getString("className")));
//                            }
//                            mDiolog.dismiss();
//                            showChooseDialog(1, classList);
//                            break;
//                        }
//                    }
//                } catch (com.alibaba.fastjson.JSONException e) {
//                    mDiolog.dismiss();
//                    Toast.makeText(MyApplication.AppContext, "解析数据失败！", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
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
