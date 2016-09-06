package com.gaop.huthelper.view.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.Spinner;

import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.AutoRVAdapter;
import com.gaop.huthelper.adapter.ViewHolder;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.PicassoEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;

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

    private final int REQUEST_CODE_CHOOSE=111;
    private Bitmap bmp;                      //导入临时图片
    private ArrayList<Bitmap> imageItem;
    private AddGoodsAdapter addGoodsAdapter;

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
        imageItem.add(bmp);
        rvAddgoodsImglist.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        addGoodsAdapter=new AddGoodsAdapter(this,imageItem);
        addGoodsAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    dialog(position);
                }else if (imageItem.size() >= 6) {
                    ToastUtil.showToastShort("最多选择5张图片");
                } else if (position == 0) {
                    Picker.from(AddGoodsActivity.this)
                            .count(5 - imageItem.size() + 1)
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> mSelected= PicturePickerUtils.obtainResult(data);
            for (Uri u : mSelected) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), u);
                    imageItem.add(CommUtil.compressImage(bitmap));
                  //  Log.e("ga1",CommUtil.getBitmapSize(bitmap)+"");
                   // Log.e("ga",CommUtil.getBitmapSize(CommUtil.compressImage(bitmap))+"");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            addGoodsAdapter.notifyDataSetChanged();
        }
    }
    /**
    *Dialog对话框提示用户删除操作
    *position为删除图片位置
    */

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddGoodsActivity.this);
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

    @OnClick(R.id.iv_addgoods_ok)
    public void onClick() {
        if (fastClick()) {

        }
    }

    class AddGoodsAdapter extends AutoRVAdapter{

        public AddGoodsAdapter(Context context, List<Bitmap> list) {
            super(context, list);
        }

        @Override
        public int onCreateViewHolder(int viewType) {
            return R.layout.item_addgoods_img;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.getImageView(R.id.iv_addgoodsitem_img).setImageBitmap((Bitmap)list.get(position));

        }
    }

}
