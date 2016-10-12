package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_user_nickname)
    TextView tvUserNickname;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_sex)
    TextView tvUserSex;
    @BindView(R.id.tv_user_num)
    TextView tvUserNum;
    @BindView(R.id.tv_user_school)
    TextView tvUserSchool;
    @BindView(R.id.tv_user_class)
    TextView tvUserClass;
    @BindView(R.id.tv_user_password)
    TextView tvUserPassword;
    private User mUser;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_user;
    }


    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("个人信息");
        initData();
    }
    private void initData() {
        mUser = DBHelper.getUserDao().get(0);
        tvUserNickname.setText(mUser.getUsername());
        tvUserName.setText(mUser.getTrueName());
        tvUserSex.setText(mUser.getSex());
        tvUserSchool.setText(mUser.getDep_name());
        tvUserNum.setText(mUser.getStudentKH());
        tvUserClass.setText(mUser.getClass_name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initData();
    }

    @OnClick({R.id.imgbtn_toolbar_back,R.id.rl_user_nickname, R.id.rl_user_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.rl_user_nickname:
                Bundle mbundle=new Bundle();
                mbundle.putSerializable("user",mUser);
                mbundle.putString("title","修改昵称");
                startActivityForResult(ChangeNickNameActivity.class,mbundle,111);
                break;
            case R.id.rl_user_password:
                Bundle bundle = new Bundle();
                bundle.putInt("type", WebViewActivity.TYPE_CHANGE_PW);
                startActivity(WebViewActivity.class, bundle);
                break;
        }
    }
}
