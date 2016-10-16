package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gaop.huthelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop on 16-10-14.
 */

public class MoreActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_more;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("更多");
    }


    @OnClick({R.id.imgbtn_toolbar_back, R.id.imgbtn_time_maincontent, R.id.imgbtn_electric_maincontent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.imgbtn_time_maincontent:
                startActivity(CalendarActivity.class);
                break;
            case R.id.imgbtn_electric_maincontent:
                startActivity(ElecticActivity.class);
                break;
        }
    }
}
