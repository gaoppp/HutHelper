package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.model.entity.OfferInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop on 16-11-15.
 */

public class OfferItemActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_offeritem_company)
    TextView tvOfferitemCompany;
    @BindView(R.id.tv_offeritem_position)
    TextView tvOfferitemPosition;
    @BindView(R.id.tv_offeritem_city)
    TextView tvOfferitemCity;
    @BindView(R.id.tv_offeritem_salary)
    TextView tvOfferitemSalary;
    @BindView(R.id.tv_offeritem_score)
    TextView tvOfferitemScore;
    @BindView(R.id.tv_offeritem_time)
    TextView tvOfferitemTime;
    @BindView(R.id.tv_offeritem_remark)
    TextView tvOfferitemRemark;
    @BindView(R.id.tv_offeritem_number)
    TextView tvOfferitemNumber;

    OfferInfo offerInfo;

    @Override
    public void initParms(Bundle parms) {
        offerInfo = (OfferInfo) parms.getSerializable("offerinfo");
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_offeritem;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("详情");
        if (offerInfo != null) {
            tvOfferitemCity.setText(offerInfo.getCity());
            tvOfferitemCompany.setText(offerInfo.getCompany());
            tvOfferitemNumber.setText(offerInfo.getNumber() + "");
            tvOfferitemPosition.setText(offerInfo.getPosition());
            tvOfferitemRemark.setText(offerInfo.getRemark());
            tvOfferitemSalary.setText(offerInfo.getSalary());
            tvOfferitemScore.setText(offerInfo.getScore() + "");
            tvOfferitemTime.setText(offerInfo.getTime());
        }
    }

    @OnClick(R.id.imgbtn_toolbar_back)
    public void onClick() {
        finish();
    }
}
