package com.gaop.huthelper.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.model.entity.LinksBean;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelper.view.adapter.VideoItemRVAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gaop.huthelper.utils.CommUtil.coverColor;

public class VideoItemActivity extends BaseActivity {


    @BindView(R.id.iv_videoitem_bg)
    ImageView ivVideoitemBg;
    @BindView(R.id.iv_videoitem_img)
    ImageView ivVideoitemImg;
    @BindView(R.id.rl_videoitem)
    RecyclerView rlVideoitem;

    private LinksBean linksBean;
    private String url;

    @Override
    public void initParms(Bundle parms) {
        if (parms != null) {
            url = parms.getString("url");
            linksBean = (LinksBean) parms.getSerializable("link");
        } else {
            ToastUtil.showToastShort("数据异常");
            finish();
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_video_item;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(linksBean.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        VideoItemRVAdapter adapter = new VideoItemRVAdapter(mContext, linksBean.getVedioList());
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = url + linksBean.getVedioList().get(position).getUrl();
                if (CommUtil.isWifiConnected(VideoItemActivity.this)) {
                    startPlay(path);
                } else {
                    showDialog(path);
                }
            }
        });
        rlVideoitem.setLayoutManager(new GridLayoutManager(this, 3));
        rlVideoitem.setAdapter(adapter);
        Picasso.with(mContext).load(url + linksBean.getImg()).into(ivVideoitemImg);
        Picasso.with(mContext).load(url + linksBean.getImg()).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 5;//直接设置它的压缩率，表示1/2
//                Bitmap bitmap =BitmapFactory.de
//                Bitmap bitmap = BitmapFactory.decodeResource(VideoItemActivity.this.getResources(), source, options);
                Bitmap bitmap = (CommUtil.blurBitmap(VideoItemActivity.this, source));
                bitmap = coverColor(bitmap, 0x21000000);
                source.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return "blur";
            }
        }).into(ivVideoitemBg);

    }



    private void startPlay(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("video_path", url);
        startActivity(VideoViewActivity.class, bundle);
    }

    private void showDialog(final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoItemActivity.this);
        builder.setMessage("当前非WIFI网络，是否继续播放？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startPlay(url);
                dialog.dismiss();

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

}
