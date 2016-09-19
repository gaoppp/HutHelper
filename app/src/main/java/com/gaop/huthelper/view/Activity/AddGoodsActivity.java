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
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.AutoRVAdapter;
import com.gaop.huthelper.adapter.ChoosePicAdapter;
import com.gaop.huthelper.adapter.ViewHolder;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.DensityUtils;
import com.gaop.huthelper.utils.ScreenUtils;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by gaop1 on 2016/9/2.
 */
public class AddGoodsActivity extends BaseActivity {
    @BindView(R.id.iv_addgoods_ok)
    ImageView ivAddgoodsOk;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner_fl)
    Spinner spinnerFl;
    @BindView(R.id.spinner_xj)
    Spinner spinnerXj;
    @BindView(R.id.et_addgoods_nowprice)
    EditText etAddgoodsNowprice;
    @BindView(R.id.et_addgoods_oldprice)
    EditText etAddgoodsOldprice;
    @BindView(R.id.et_addgoods_title)
    EditText etAddgoodsTitle;
    @BindView(R.id.et_addgoods_content)
    EditText etAddgoodsContent;
    @BindView(R.id.rv_addgoods_imglist)
    RecyclerView rvAddgoodsImglist;
    @BindView(R.id.et_addgoods_phone)
    EditText etAddgoodsPhone;
    @BindView(R.id.et_addgoods_qq)
    EditText etAddgoodsQq;
    @BindView(R.id.et_addgoods_wechat)
    EditText etAddgoodsWechat;


    private final int REQUEST_CODE_CHOOSE = 111;

    private Bitmap bmp;                      //导入临时图片
    private ArrayList<Bitmap> imageItem;
    private ChoosePicAdapter addGoodsAdapter;
    private ArrayList<Integer> imageOption;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_addgoods;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        toolbar.setTitle("添加商品");
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
        imageOption = new ArrayList<>();
        imageItem.add(bmp);
        imageOption.add(100);
        rvAddgoodsImglist.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        addGoodsAdapter = new ChoosePicAdapter(this, imageItem);
        addGoodsAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    dialog(position);
                } else if (imageItem.size() >= 5) {
                    ToastUtil.showToastShort("最多选择4张图片");
                } else if (position == 0) {
                    if (!CommUtil.isGrantExternalRW(AddGoodsActivity.this)) {
                        ToastUtil.showToastShort("未能获取读取文件权限");
                        return;
                    }
                    Picker.from(AddGoodsActivity.this)
                            .count(4 - imageItem.size() + 1)
                            .enableCamera(true)
                            .setEngine(new PicassoEngine())
                            .forResult(REQUEST_CODE_CHOOSE);

                }
            }

        });
        rvAddgoodsImglist.setAdapter(addGoodsAdapter);
    }

    //获取图片路径 响应startActivityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

            List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
            dialog = new ProgressDialog(AddGoodsActivity.this);
            dialog.show();
            dialog.setMessage("正在处理图片");
            dialog.setCancelable(false);
            Observable.from(mSelected).map(new Func1<Uri, Integer>() {
                @Override
                public Integer call(Uri filePath) { // 参数类型 String

                    int option = 50;
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        option = CommUtil.compressImage(bitmap);
                        imageItem.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("fd",option+"");
                    return option; // 返回类型 Bitmap
                }
            }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
                @Override
                public void onCompleted() {
                    dialog.dismiss();
                    addGoodsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {
                    dialog.dismiss();
                    ToastUtil.showToastShort("导入图片出错");

                }

                @Override
                public void onNext(Integer filePath) {
                    imageOption.add(filePath);
                }
            });
        }
    }

    /**
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    ProgressDialog dialog;
    final StringBuilder stringBuilder = new StringBuilder();

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddGoodsActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                imageOption.remove(position);
                addGoodsAdapter.notifyDataSetChanged();
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

    @OnClick(R.id.iv_addgoods_ok)
    public void onClick() {
        if (fastClick()) {
            if (etAddgoodsPhone.getText().toString().equals("") && etAddgoodsQq.getText().toString().equals("")
                    && etAddgoodsWechat.getText().toString().equals("")) {
                ToastUtil.showToastShort("手机，QQ，微信必填一项");
            } else if (TextUtils.isEmpty(spinnerFl.getSelectedItem().toString()) || TextUtils.isEmpty(spinnerXj.getSelectedItem().toString())) {
                ToastUtil.showToastShort("请选择商品分类及成色");
            } else if (imageItem.size() == 1) {
                ToastUtil.showToastShort("至少上传一张图片");
            } else {
                dialog = new ProgressDialog(AddGoodsActivity.this);
                dialog.show();
                uploadImg(1);

            }
        }
    }


    private void uploadImg(final int i) {
        if (i + 1 > imageItem.size()) {
            addGoods();
            return;
        }
        dialog.setCancelable(false);
        dialog.setMessage("正在上传第" + i + "张图片");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        imageItem.get(i).compress(Bitmap.CompressFormat.PNG, imageOption.get(i), os);
        byte[] bytes = os.toByteArray();
        Log.e(TAG, bytes.length+ "   " +imageOption.get(i));
        final RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), bytes);
        final MultipartBody.Part photo = MultipartBody.Part.createFormData("file", "01.png", requestFile);


        HttpMethods.getInstance().UploadFile(
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
                            if (!stringBuilder.equals(""))
                                stringBuilder.append("//");
                            stringBuilder.append(o.getData());
                            uploadImg(i + 1);
                        } else {
                            dialog.dismiss();
                            ToastUtil.showToastShort("第" + i + "张图片上传失败:" + o.getMsg());
                        }
                    }
                }, "file", photo);

    }


    private void addGoods() {
        int goodsclass = spinnerFl.getSelectedItemPosition() + 1;
        int goodsquil = spinnerXj.getSelectedItemPosition() + 1;
        dialog.setMessage("正在发布商品");
        User user = DBHelper.getUserDao().get(0);
        HttpMethods.getInstance().AddGoods(new Subscriber<HttpResult<String>>() {
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
                                                       startActivity(ImportActivity.class);
                                                       ToastUtil.showToastShort("账号异地登录，请重新登录");
                                                   } else {
                                                       dialog.dismiss();
                                                       ToastUtil.showToastShort(stringHttpResult.getMsg());
                                                   }

                                               }
                                           }, user, etAddgoodsTitle.getText().toString(), etAddgoodsContent.getText().toString(),
                etAddgoodsNowprice.getText().toString(), etAddgoodsOldprice.getText().toString(), goodsclass, goodsquil, stringBuilder.toString(),
                etAddgoodsPhone.getText().toString(), etAddgoodsQq.getText().toString(), etAddgoodsWechat.getText().toString());
    }


}
