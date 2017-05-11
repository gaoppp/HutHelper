package com.gaop.huthelper.view.activity;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.R;
import com.gaop.huthelper.view.adapter.ChoosePicAdapter;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.utils.ButtonUtils;
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
 * 添加失物招领
 * Created by 高沛 on 16-11-10.
 */

public class AddLoseActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.et_addlose_title)
    EditText etAddloseTitle;
    @BindView(R.id.tv_addlose_time)
    TextView tvAddloseTime;
    @BindView(R.id.et_addlose_place)
    EditText etAddlosePlace;
    @BindView(R.id.et_addlose_phone)
    EditText etAddlosePhone;
    @BindView(R.id.et_addlose_qq)
    EditText etAddloseQq;
    @BindView(R.id.et_addlose_wechat)
    EditText etAddloseWechat;
    @BindView(R.id.et_addlose_content)
    EditText etAddloseContent;
    @BindView(R.id.gv_addlose_imglist)
    RecyclerView rvAddloseImglist;
    @BindView(R.id.rl_addlose)
    RelativeLayout rlAddlose;
//    @BindView(R.id.tv_toolbar_title)
//    TextView tvToolbarTitle;
//    @BindView(R.id.rv_addlose_imglist)
//    RecyclerView rvAddloseImglist;
//    @BindView(R.id.et_addlose_title)
//    EditText etAddloseTitle;
//    @BindView(R.id.et_addlose_time)
//    TextView etAddloseTime;
//    @BindView(R.id.et_addlose_place)
//    EditText etAddlosePlace;
//    @BindView(R.id.et_addlose_content)
//    EditText etAddloseContent;
//    @BindView(R.id.et_addlose_phone)
//    EditText etAddlosePhone;
//    @BindView(R.id.et_addlose_qq)
//    EditText etAddloseQq;
//    @BindView(R.id.et_lose_wechat)
//    EditText etLoseWechat;


    private AtomicInteger count;

    private final int REQUEST_CODE_CHOOSE = 111;

    private ArrayList<Bitmap> imageItem;
    private ChoosePicAdapter addGoodsAdapter;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_addlose;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        tvToolbarTitle.setText("添加失物");
        Bitmap bmp;
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_addpic);
        imageItem = new ArrayList<Bitmap>();
        imageItem.add(bmp);
        rvAddloseImglist.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        addGoodsAdapter = new ChoosePicAdapter(this, imageItem);
        addGoodsAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    dialog(position);
                } else if (imageItem.size() >= 5) {
                    ToastUtil.showToastShort("最多选择4张图片");
                } else if (position == 0) {
                    if (!CommUtil.isGrantExternalRW(AddLoseActivity.this)) {
                        ToastUtil.showToastShort("未能获取读取文件权限");
                        return;
                    }
                    Picker.from(AddLoseActivity.this)
                            .count(4 - imageItem.size() + 1)
                            .enableCamera(true)
                            .setEngine(new PicassoEngine())
                            .forResult(REQUEST_CODE_CHOOSE);

                }
            }

        });
        rvAddloseImglist.setAdapter(addGoodsAdapter);
    }

    //获取图片路径 响应startActivityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

            final List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
            dialog = new ProgressDialog(AddLoseActivity.this);
            dialog.show();
            dialog.setMessage("正在处理图片");
            dialog.setCancelable(false);
            count = new AtomicInteger(mSelected.size());
            for (Uri u : mSelected) {
                Luban.get(AddLoseActivity.this)
                        .load(CommUtil.uri2File(AddLoseActivity.this, u))                     //传人要压缩的图片
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
                                    addGoodsAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("error", e.toString());
                                count.decrementAndGet();
                                if (count.get() == 0) {
                                    dialog.dismiss();
                                    addGoodsAdapter.notifyDataSetChanged();
                                }
                            }
                        }).launch();    //启动压缩
            }

        }
    }

    private void initDateChoose() {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.diolog_datechoose, null);//获取自定义布局
        builder.setView(layout);
        builder.setIcon(null);//设置标题图标
        builder.setTitle(null);//设置标题内容
        final DatePicker datePicker = (DatePicker) layout.findViewById(R.id.datePicker);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                StringBuffer sb = new StringBuffer();
                sb.append(String.format("%d-%02d-%02d",
                        datePicker.getYear(),
                        datePicker.getMonth() + 1,
                        datePicker.getDayOfMonth()));
                tvAddloseTime.setText(sb);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final android.support.v7.app.AlertDialog dlg = builder.create();
        dlg.show();
    }


    /**
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    ProgressDialog dialog;
    final StringBuilder stringBuilder = new StringBuilder();

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddLoseActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
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


    private void uploadImg(final int i) {
        if (i + 1 > imageItem.size()) {
            addLose();
            return;
        }
        dialog.setCancelable(false);
        dialog.setMessage("正在上传第" + i + "张图片");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        imageItem.get(i).compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] bytes = os.toByteArray();
        final RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), bytes);
        final MultipartBody.Part photo = MultipartBody.Part.createFormData("file", "01.png", requestFile);
        HttpMethods.getInstance().UploadLoseImag(
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


    private void addLose() {
        dialog.setMessage("正在发布失物招领");
        User user = DBHelper.getUserDao().get(0);
        HttpMethods.getInstance().addLose(new Subscriber<HttpResult<String>>() {
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
                                          }, user, etAddloseTitle.getText().toString(), etAddlosePlace.getText().toString(),
                tvAddloseTime.getText().toString(), etAddloseContent.getText().toString(), stringBuilder.toString(),
                etAddlosePhone.getText().toString(), etAddloseQq.getText().toString(), etAddloseWechat.getText().toString());
    }


    @OnClick({R.id.imgbtn_toolbar_back,R.id.imgbtn_toolbar_ok, R.id.tv_addlose_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.imgbtn_toolbar_ok:
                if (!ButtonUtils.isFastDoubleClick()) {
                    if (tvAddloseTime.getText().toString().equals("") || etAddlosePlace.getText().toString().equals("")||etAddloseContent.getText().toString().equals("")) {
                        ToastUtil.showToastShort("请填写完整");
                        return;
                    }
                    if (etAddlosePhone.getText().toString().equals("") && etAddloseQq.getText().toString().equals("")
                            && etAddloseWechat.getText().toString().equals("")) {
                        ToastUtil.showToastShort("手机，QQ，微信必填一项");
                    } else {
                        dialog = new ProgressDialog(AddLoseActivity.this);
                        dialog.show();
                        uploadImg(1);
                    }
                }
                break;
            case R.id.tv_addlose_time:
                initDateChoose();
                break;
        }
    }

}
