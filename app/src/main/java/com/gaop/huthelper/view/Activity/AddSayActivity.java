package com.gaop.huthelper.view.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.ChoosePicAdapter;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by gaop on 16-9-11.
 */
public class AddSayActivity extends BaseActivity {
    @BindView(R.id.iv_addsay_ok)
    ImageView ivAddsayOk;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_addsay_content)
    EditText etAddsayContent;
    @BindView(R.id.rv_addsay_imglist)
    RecyclerView rvAddsayImglist;
    private Bitmap bmp;
    private List<Bitmap> imageItem;
    private ChoosePicAdapter adapter;
    private StringBuilder imageString=new StringBuilder();
    /**
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    ProgressDialog dialog;

    private final int REQUEST_CODE_CHOOSE = 111;


    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_addsay;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        toolbar.setTitle("写说说");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_add_black_48dp);
        imageItem = new ArrayList<Bitmap>();
        imageItem.add(bmp);
        rvAddsayImglist.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        adapter = new ChoosePicAdapter(this, imageItem);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    dialog(position);
                } else if (imageItem.size() >= 5) {
                    ToastUtil.showToastShort("最多选择4张图片");
                } else if (position == 0) {
                    if (!CommUtil.isGrantExternalRW(AddSayActivity.this)) {
                        ToastUtil.showToastShort("未能获取读取文件权限");
                        return;
                    }
                    Picker.from(AddSayActivity.this)
                            .count(4 - imageItem.size() + 1)
                            .enableCamera(true)
                            .setEngine(new PicassoEngine())
                            .forResult(REQUEST_CODE_CHOOSE);
                }

            }

        });
        rvAddsayImglist.setAdapter(adapter);

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == 1) {
//            for (int i = 0; i < permissions.length; i++) {
//                String permission = permissions[i];
//                int grantResult = grantResults[i];
//
//                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
//                        //授权成功后的逻辑
//                        ...
//                    } else {
//                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_CODE);
//                    }
//                }
//            }
//        }
//    }

    //获取图片路径 响应startActivityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> mSelected = PicturePickerUtils.obtainResult(data);

            dialog = new ProgressDialog(AddSayActivity.this);
            dialog.show();
            dialog.setMessage("正在处理图片");
            dialog.setCancelable(false);
            Observable.from(mSelected).map(new Func1<Uri, Bitmap>() {
                @Override
                public Bitmap call(Uri filePath) { // 参数类型 String
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return CommUtil.compressImage(bitmap); // 返回类型 Bitmap
                }}).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Bitmap>() {
                @Override
                public void onCompleted() {
                    dialog.dismiss();
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {
                    dialog.dismiss();
                    ToastUtil.showToastShort("导入图片出错");

                }
                @Override
                public void onNext(Bitmap bitmap) {
                    imageItem.add(CommUtil.compressImage(bitmap));
                }
            });
        }
    }

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddSayActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                adapter.notifyDataSetChanged();
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
    @OnClick(R.id.iv_addsay_ok)
    public void onClick() {
        if (fastClick()) {
            if (etAddsayContent.getText().toString().equals("") ) {
                ToastUtil.showToastShort("请填写内容");
            } else {
                dialog = new ProgressDialog(AddSayActivity.this);
                dialog.show();
                uploadImg(1);
            }
        }
    }

    private void uploadImg(final int i) {
        if (i + 1 > imageItem.size()) {
            addSay();
            return;
        }
        dialog.setCancelable(false);
        dialog.setMessage("正在上传第" + i + "张图片");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        imageItem.get(i).compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] bytes = os.toByteArray();
        final RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), bytes);
        final MultipartBody.Part photo = MultipartBody.Part.createFormData("file", "01.png", requestFile);
        HttpMethods.getInstance().UploadSayImag(
                new Subscriber<HttpResult<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.toString());
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(HttpResult<String> o) {
                        if (o.getMsg().equals("ok")) {
                            if (!imageString.equals(""))
                                imageString.append("//");
                            imageString.append(o.getData());
                            uploadImg(i + 1);
                        } else {
                            ToastUtil.showToastShort("第" + i + "张图片上传失败:" + o.getMsg());
                        }
                    }
                }, "file", photo);

    }


    private void addSay() {
        dialog.setMessage("正在发布说说");
        User user = DBHelper.getUserDao().get(0);
        HttpMethods.getInstance().addSay(new Subscriber<HttpResult<String>>() {
                                               @Override
                                               public void onCompleted() {

                                               }

                                               @Override
                                               public void onError(Throwable e) {
                                                   dialog.dismiss();
                                                   ToastUtil.showToastShort("上传出错" + e.toString());
                                               }

                                               @Override
                                               public void onNext(HttpResult<String> stringHttpResult) {
                                                   if (stringHttpResult.getMsg().equals("ok")) {
                                                       dialog.dismiss();
                                                       ToastUtil.showToastShort("发布成功");
                                                   }else if(stringHttpResult.getMsg().equals("令牌错误")){
                                                       startActivity(ImportActivity.class);
                                                       ToastUtil.showToastShort("账号异地登录，请重新登录");
                                                   }else
                                                       ToastUtil.showToastShort(stringHttpResult.getMsg());

                                               }
                                           }, user,etAddsayContent.getText().toString(),imageString.toString());
    }


}
