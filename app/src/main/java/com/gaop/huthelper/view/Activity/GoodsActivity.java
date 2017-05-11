package com.gaop.huthelper.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.view.adapter.LoseImageAdapter;
import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.model.entity.Goods;
import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;


/**
 * 商品详情Activity
 * Created by 高沛 on 2016/9/2.
 */
public class GoodsActivity extends BaseActivity {

    String id;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_item_mygoods_content)
    TextView tvItemMygoodsContent;
    @BindView(R.id.gv_item_mygoods_image)
    GridView gvItemMygoodsImage;
    @BindView(R.id.tv_item_mygoods_price)
    TextView tvItemMygoodsPrice;
    @BindView(R.id.tv_item_mygoods_quality)
    TextView tvItemMygoodsQuality;
    @BindView(R.id.tv_item_mygoods_connect)
    TextView tvItemMygoodsConnect;
    @BindView(R.id.tv_item_mygoods_username)
    TextView tvItemMygoodsUsername;
    @BindView(R.id.tv_item_mygoods_createtime)
    TextView tvItemMygoodsCreatetime;
    @BindView(R.id.btn_item_mylose_delete)
    Button btnItemMyloseDelete;
    @BindView(R.id.rl_mygoods)
    RelativeLayout rlMygoods;

    private boolean delete;

    private User user;

    public final int[] bg={R.drawable.bg_list_empty,R.drawable.bg2_list_empty,R.drawable.bg3_list_empty,R.drawable.bg4_list_empty};

    @Override
    public void initParms(Bundle parms) {
        id = parms.getString("id");
        if (TextUtils.isEmpty(id)) {
            ToastUtil.showToastShort("获取信息失败！");
            finish();
        }
        delete=parms.getBoolean("delete",false);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_goods;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("商品详情");
        user = DBHelper.getUserDao().get(0);
        int num=(int)(Math.random()*4);
        rlMygoods.setBackgroundResource(bg[num]);
        getGoodsData();
    }

    private void setView(final Goods goods) {

        tvItemMygoodsCreatetime.setText(goods.getCreated_on());//创建时间
        tvItemMygoodsContent.setText(goods.getContent());//内容
        tvItemMygoodsQuality.setText(goods.getAttr());//几成新
        tvItemMygoodsUsername.setText(goods.getUser().getUsername());//用户名
        tvItemMygoodsPrice.setText("￥" + goods.getPrize());

        StringBuilder connect = new StringBuilder();
        if (!TextUtils.isEmpty(goods.getPhone()))
            connect.append("电话").append(goods.getPhone()).append(" ");
        if (!TextUtils.isEmpty(goods.getQq()))
            connect.append("QQ").append(goods.getQq()).append(" ");
        if (!TextUtils.isEmpty(goods.getWechat()))
            connect.append("微信").append(goods.getWechat()).append(" ");

        tvItemMygoodsConnect.setText(connect);
        List<String> images = goods.getPics();
        if (images != null && images.size() != 0) {
            gvItemMygoodsImage.setAdapter(new LoseImageAdapter(GoodsActivity.this, images));
        }
        if(delete){
            btnItemMyloseDelete.setVisibility(View.VISIBLE);
            btnItemMyloseDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog(goods.getId());
                }
            });
        }
    }

    private void getGoodsData() {

        User user = DBHelper.getUserDao().get(0);
        if (user == null) {
            ToastUtil.showToastShort("获取信息失败！");
            finish();
        }

        Subscriber<HttpResult<Goods>> subscriber = new Subscriber<HttpResult<Goods>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof java.net.ConnectException) {
                    //网络连接错误
                    ToastUtil.showToastShort("网络连接失败！");
                } else if (e instanceof java.net.SocketTimeoutException) {
                    //连接超时
                    ToastUtil.showToastShort("连接超时！");
                } else if (e instanceof retrofit2.adapter.rxjava.HttpException) {
                    ToastUtil.showToastShort("服务器错误");
                } else {
                    ToastUtil.showToastShort("数据解析错误");
                }
            }

            @Override
            public void onNext(HttpResult<Goods> o) {
                if (o.getMsg().equals("ok")) {
                    setView(o.getData());
                } else if (o.getMsg().equals("令牌错误")) {
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                    startActivity(ImportActivity.class);
                    finish();
                } else {
                    ToastUtil.showToastShort(o.getMsg());
                    finish();
                }
            }
        };
        HttpMethods.getInstance().getGoodsContent(
                subscriber,
                user.getStudentKH(), user.getRember_code(), id);
    }

    protected void dialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GoodsActivity.this);
        builder.setMessage("确认移除该商品吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteGoods(id);
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

    private void deleteGoods(final String id) {

        SubscriberOnNextListener<HttpResult> subscriberOnNextListener = new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult o) {
                if (o.getMsg().equals("ok")) {
                    ToastUtil.showToastShort("删除成功");
                    finish();
                } else if ("令牌错误".equals(o.getMsg())) {
                    startActivity(ImportActivity.class);
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                } else
                    ToastUtil.showToastShort(o.getMsg());
            }
        };
        HttpMethods.getInstance().deleteGoods(
                new ProgressSubscriber<HttpResult>(subscriberOnNextListener, GoodsActivity.this)
                , user.getStudentKH(), user.getRember_code(), id);
    }

    @OnClick(R.id.imgbtn_toolbar_back)
    public void onClick() {
        finish();
    }


}
