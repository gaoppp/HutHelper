package com.gaop.huthelper.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.app.MApplication;
import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.model.rxbus.RxBus;
import com.gaop.huthelper.model.rxbus.event.MainEvent;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.DensityUtils;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.User;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.PicassoEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.functions.Action1;

/**
 * Created by 高沛 on 2016/5/22.
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
    @BindView(R.id.iv_user_headview)
    ImageView ivUserAvatar;

    private User mUser;

    private final int REQUEST_CODE_CHOOSE = 111;
    private final int REQUEST_CODE_CUT = 222;

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
        mUser = MApplication.getUser();
        tvUserNickname.setText(mUser.getUsername());
        tvUserName.setText(mUser.getTrueName());
        tvUserSex.setText(mUser.getSex());
        tvUserSchool.setText(mUser.getDep_name());
        tvUserNum.setText(mUser.getStudentKH());
        tvUserClass.setText(mUser.getClass_name());
        if (!TextUtils.isEmpty(mUser.getHead_pic_thumb())) {
            int width = DensityUtils.dp2px(this, 40);
            Picasso.with(this).load(HttpMethods.BASE_URL + mUser.getHead_pic_thumb()).resize(width, width).into(ivUserAvatar);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
            if (mSelected.size() != 0) {
                Uri u = mSelected.get(0);
                Intent intent = new Intent();
                intent.setAction("com.android.camera.action.CROP");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(u, "image/*");// mUri是已经选择的图片Uri
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);// 裁剪框比例
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 150);// 输出图片大小
                intent.putExtra("outputY", 150);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, REQUEST_CODE_CUT);
            }

        } else if (requestCode == REQUEST_CODE_CUT && resultCode == RESULT_OK) {
            Bitmap bmap = data.getParcelableExtra("data");
            uploadAvatar(bmap);

        } else {
            initData();
        }

    }

    private void uploadAvatar(final Bitmap bitmap) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, os);
        byte[] bytes = os.toByteArray();
        final RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), bytes);
        final MultipartBody.Part photo = MultipartBody.Part.createFormData("file", "01.png", requestFile);
        SubscriberOnNextListener upload = new SubscriberOnNextListener<HttpResult<String>>() {
            @Override
            public void onNext(HttpResult<String> o) {
                if (o.getMsg().equals("ok")) {
                    ToastUtil.showToastShort("修改成功");
                    ivUserAvatar.setImageBitmap(bitmap);
                    mUser.setHead_pic_thumb(o.getData());
                    mUser.setHead_pic("");
                    RxBus.getInstance().send(new MainEvent(3));
                    DBHelper.UpdateUser(mUser);
                } else if ("令牌错误".equals(o.getMsg())) {
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                    startActivity(ImportActivity.class);
                    finish();
                } else {
                    ToastUtil.showToastShort(o.getMsg());
                }
            }
        };
        HttpMethods.getInstance().UploadAvator(new ProgressSubscriber<HttpResult<String>>(upload, UserActivity.this)
                , mUser.getStudentKH(), mUser.getRember_code(), photo);
    }

    @OnClick({R.id.imgbtn_toolbar_back, R.id.rl_user_nickname, R.id.rl_user_password, R.id.rl_user_headview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.rl_user_nickname:
                Bundle mbundle = new Bundle();
                mbundle.putSerializable("user", mUser);
                mbundle.putString("title", "修改昵称");
                startActivityForResult(ChangeNickNameActivity.class, mbundle, 111);
                break;
            case R.id.rl_user_password:
                Bundle bundle = new Bundle();
                bundle.putInt("type", WebViewActivity.TYPE_CHANGE_PW);
                startActivity(WebViewActivity.class, bundle);
                break;
            case R.id.rl_user_headview:
                RxPermissions.getInstance(this)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean granted) {
                                if(granted){
                                    //请求成功
                                    Picker.from(UserActivity.this)
                                            .count(1)
                                            .enableCamera(true)
                                            .setEngine(new PicassoEngine())
                                            .forResult(REQUEST_CODE_CHOOSE);
                                }else{
                                    // 请求失败回收当前服务
                                   ToastUtil.showToastShort("请求权限失败");

                                }
                            }
                        });

                break;
        }
    }


}
