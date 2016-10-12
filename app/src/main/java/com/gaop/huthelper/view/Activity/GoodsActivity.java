package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.Model.Goods;
import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.AutoRVAdapter;
import com.gaop.huthelper.adapter.ViewHolder;
import com.gaop.huthelper.jiekou.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop1 on 2016/9/2.
 */
public class GoodsActivity extends BaseActivity {

    @BindView(R.id.tv_name_goods)
    TextView tvNameGoods;
    @BindView(R.id.tv_createon_goods)
    TextView tvCreateonGoods;
    @BindView(R.id.tv_oldprice_goods)
    TextView tvOldpriceGoods;
    @BindView(R.id.tv_newprice_goods)
    TextView tvNewpriceGoods;
    @BindView(R.id.tv_title_goods)
    TextView tvTitleGoods;
    @BindView(R.id.tv_quality)
    TextView tvQuality;
    @BindView(R.id.tv_connectway_goods)
    TextView tvConnectwayGoods;
    @BindView(R.id.tv_content_goods)
    TextView tvContentGoods;
    @BindView(R.id.rv_imglist_goods)
    RecyclerView rvImglistGoods;

    String id;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_line_goods1)
    ImageView ivLineGoods1;
    @BindView(R.id.quality)
    TextView quality;
    @BindView(R.id.connectway)
    TextView connectway;
    @BindView(R.id.iv_line_goods)
    ImageView ivLineGoods;
    @BindView(R.id.textView)
    TextView textView;

    @Override
    public void initParms(Bundle parms) {
        id = parms.getString("id");
        if (TextUtils.isEmpty(id)) {
            ToastUtil.showToastShort("获取信息失败！");
            finish();
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_goods;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("商品详情");
        getGoodsData();
    }

    private void setView(final Goods goods) {
        tvTitleGoods.setText(goods.getTit());//标题
        tvCreateonGoods.setText(goods.getCreated_on());//创建时间
        tvContentGoods.setText(goods.getContent());//内容
        tvQuality.setText(goods.getAttr());//几成新
        tvNameGoods.setText(goods.getUser().getUsername());//用户名
        tvNewpriceGoods.setText("￥" + goods.getPrize());
        tvOldpriceGoods.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        tvOldpriceGoods.setText("￥" + goods.getPrize_src());
        String contentway = "";
        if (!TextUtils.isEmpty(goods.getPhone())) {
            contentway += "手机:" + goods.getPhone() + "\n";
        }
        if (!TextUtils.isEmpty(goods.getQq())) {
            contentway += "QQ:" + goods.getQq() + "\n";
        }
        if (!TextUtils.isEmpty(goods.getWechat())) {
            contentway += "微信:" + goods.getWechat() + "\n";
        }
        tvConnectwayGoods.setText(contentway);
        Adapter adapter = new Adapter(GoodsActivity.this, goods.getPics());
        rvImglistGoods.setLayoutManager(new LinearLayoutManager(GoodsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        //rvImglistGoods.addItemDecoration(new DividerItemDecoration(GoodsActivity.this, DividerItemDecoration.HORIZONTAL_LIST));
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("urls", (ArrayList<String>) goods.getPics_src());
                bundle.putInt("curr", position);
                startActivity(ShowImgActivity.class, bundle);
            }
        });
        rvImglistGoods.setAdapter(adapter);
    }

    private void getGoodsData() {
        User user = DBHelper.getUserDao().get(0);
        if (user == null) {
            ToastUtil.showToastShort("获取信息失败！");
            finish();
        }
        SubscriberOnNextListener getData = new SubscriberOnNextListener<HttpResult<Goods>>() {
            @Override
            public void onNext(HttpResult<Goods> o) {
                if (o.getMsg().equals("ok")) {
                    setView(o.getData());
                } else if (o.getMsg().equals("令牌错误")) {
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                    startActivity(ImportActivity.class);
                } else {
                    ToastUtil.showToastShort(o.getMsg());
                    finish();
                }


            }
        };
        HttpMethods.getInstance().getGoodsContent(
                new ProgressSubscriber<HttpResult<Goods>>(getData, GoodsActivity.this),
                user.getStudentKH(), user.getRember_code(), id);
    }

    @OnClick(R.id.imgbtn_toolbar_back)
    public void onClick() {
        finish();
    }

    class Adapter extends AutoRVAdapter {

        public Adapter(Context context, List<String> list) {
            super(context, list);
        }

        @Override
        public int onCreateViewHolder(int viewType) {
            return R.layout.item_goods_img;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Picasso.with(context).load(HttpMethods.BASE_URL + list.get(position))
                    //.transform(new CropSquareTransformation())
                    //.resize((ScreenUtils.getScreenWidth(context)- DensityUtils.dp2px(context,20)),12)
                    // .onlyScaleDown()
                    // .centerCrop()
                    .into(holder.getImageView(R.id.img_goods));
        }
    }
}
