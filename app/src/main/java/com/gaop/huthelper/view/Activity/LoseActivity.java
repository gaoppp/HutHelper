package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.gaop.huthelper.model.entity.Lose;
import com.gaop.huthelper.R;
import com.gaop.huthelper.view.adapter.LoseImageAdapter;
import com.gaop.huthelper.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 失物招领详情Activity
 * Created by 高沛 on 16-11-22.
 */

public class LoseActivity extends BaseActivity {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_item_mylose_what)
    TextView tvItemMyloseWhat;
    @BindView(R.id.tv_item_mylose_when)
    TextView tvItemMyloseWhen;
    @BindView(R.id.tv_item_mylose_where)
    TextView tvItemMyloseWhere;
    @BindView(R.id.tv_item_mylose_connect)
    TextView tvItemMyloseConnect;
    @BindView(R.id.tv_item_mylose_content)
    TextView tvItemMyloseContent;
    @BindView(R.id.gv_item_mylose_image)
    GridView gvItemMyloseImage;
    @BindView(R.id.tv_item_mylose_username)
    TextView tvItemMyloseUsername;
    @BindView(R.id.tv_item_mylose_createtime)
    TextView tvItemMyloseCreatetime;
    @BindView(R.id.btn_item_mylose_delete)
    Button btnItemMyloseDelete;

    private Lose lose;

    @Override
    public void initParms(Bundle parms) {
        if (parms != null) {
            lose = (Lose) parms.getSerializable("lose");
        } else {
            ToastUtil.showToastShort("数据错误");
            finish();
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_lose;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("失物详情");
        tvItemMyloseWhat.setText(lose.getTit());
        tvItemMyloseUsername.setText(lose.getUsername());
        String s = lose.getTime().substring(0, 10);
        tvItemMyloseWhen.setText(s);
        tvItemMyloseWhere.setText(lose.getLocate());
        tvItemMyloseContent.setText(lose.getContent());
        tvItemMyloseCreatetime.setText(lose.getCreated_on());
        StringBuilder connect = new StringBuilder();
        if (!TextUtils.isEmpty(lose.getPhone()))
            connect.append("电话").append(lose.getPhone()).append(" ");
        if (!TextUtils.isEmpty(lose.getQq()))
            connect.append("QQ").append(lose.getQq()).append(" ");
        if (!TextUtils.isEmpty(lose.getWechat()))
            connect.append("微信").append(lose.getWechat()).append(" ");
        tvItemMyloseConnect.setText(connect);
        btnItemMyloseDelete.setVisibility(View.GONE);
        List<String> images = lose.getPics();
        if (images != null && images.size() != 0) {
            gvItemMyloseImage.setAdapter(new LoseImageAdapter(LoseActivity.this, images));
        }
    }

    @OnClick({R.id.imgbtn_toolbar_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
        }
    }
}
