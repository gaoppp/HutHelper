package com.gaop.huthelper.view.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.ChoosePicAdapter;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.PicassoEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by gaop on 16-9-11.
 */
public class AddSayActivity extends BaseActivity {
    @BindView(R.id.et_addsay_content)
    EditText etAddsayContent;
    @BindView(R.id.rv_addsay_imglist)
    RecyclerView rvAddsayImglist;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    private Bitmap bmp;
    private List<Bitmap> imageItem;
    private ChoosePicAdapter adapter;
    private StringBuilder imageString = new StringBuilder();
    /**
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    ProgressDialog dialog;

    private final int REQUEST_CODE_CHOOSE = 111;
    private AtomicInteger count;


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
        tvToolbarTitle.setText("发说说");
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_addpic);
        imageItem = new ArrayList<Bitmap>();
        imageItem.add(bmp);
        rvAddsayImglist.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
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
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

            List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
            dialog = new ProgressDialog(AddSayActivity.this);
            dialog.show();
            dialog.setMessage("正在处理图片");
            dialog.setCancelable(false);
            count = new AtomicInteger(mSelected.size());
            for (Uri u : mSelected) {
                Luban.get(AddSayActivity.this)
                        .load(CommUtil.uri2File(AddSayActivity.this, u))                     //传人要压缩的图片
                        .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                        .setCompressListener(new OnCompressListener() { //设置回调
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(File file) {
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                                imageItem.add(bitmap);
                                count.decrementAndGet();
                                if (count.get() == 0) {
                                    dialog.dismiss();
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("error", e.toString());
                                count.decrementAndGet();
                                if (count.get() == 0) {
                                    dialog.dismiss();
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }).launch();    //启动压缩
            }
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
                            dialog.dismiss();
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
                    setResult(333);
                    finish();
                } else if (stringHttpResult.getMsg().equals("令牌错误")) {
                    dialog.dismiss();
                    startActivity(ImportActivity.class);
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                } else {
                    dialog.dismiss();
                    ToastUtil.showToastShort(stringHttpResult.getMsg());
                }

            }
        }, user, etAddsayContent.getText().toString(), imageString.toString());
    }


    @OnClick({R.id.imgbtn_toolbar_back, R.id.iv_addsay_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.iv_addsay_ok:
                if (fastClick()) {
                    if (etAddsayContent.getText().toString().equals("")) {
                        ToastUtil.showToastShort("请填写内容");
                    } else {
                        dialog = new ProgressDialog(AddSayActivity.this);
                        dialog.show();
                        uploadImg(1);
                    }
                }
                break;
        }
    }
}
