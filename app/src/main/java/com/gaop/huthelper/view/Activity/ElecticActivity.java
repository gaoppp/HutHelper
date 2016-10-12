package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gaop.huthelper.Model.Electric;
import com.gaop.huthelper.R;
import com.gaop.huthelper.jiekou.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop1 on 2016/7/23.
 */
public class ElecticActivity extends BaseActivity {


    @BindView(R.id.num1)
    EditText num1;
    @BindView(R.id.num2)
    EditText num2;
    @BindView(R.id.btn_get)
    Button btnGet;
    @BindView(R.id.imgbtn_toolbar_back)
    ImageButton imgbtnToolbarBack;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_electric;
    }


    @Override
    public void doBusiness(final Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("电费查询");
        num1.setText(PrefUtil.getString(ElecticActivity.this, "Lou", ""));
        num2.setText(PrefUtil.getString(ElecticActivity.this, "Hao", ""));
    }


    private void getdata() {
        CommUtil.hideSoftInput(ElecticActivity.this);
        SubscriberOnNextListener getElectricData = new SubscriberOnNextListener<Electric>() {
            @Override
            public void onNext(Electric o) {
                PrefUtil.setString(ElecticActivity.this, "Lou", num1.getText().toString());
                PrefUtil.setString(ElecticActivity.this, "Hao", num2.getText().toString());
                String result = null;
                try {
                    String num1 = o.getOddl();
                    String num2 = o.getPrize();
                    result = "\n剩余 " + num1 + " 度\n余额 " + num2 + " 元";
                } catch (Exception e) {
                    result = "\n无结果。请输入正确栋号、寝室号。如35 522";
                } finally {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ElecticActivity.this);
                    builder.setTitle("");
                    builder.setMessage(result);
                    builder.setNegativeButton("确认",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }
            }

//                @Override
//                public void onError(Electric electric) {
//                    super();
//                }
        };
        HttpMethods.getInstance().getElecticData(
                new ProgressSubscriber<Electric>(getElectricData, ElecticActivity.this),
                num1.getText().toString(), num2.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.imgbtn_toolbar_back, R.id.btn_get})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.btn_get:
                if (num1.getText().toString() != "" && num2.getText().toString() != "") {
                    if (fastClick()) {
                        getdata();
                    }
                } else {
                    ToastUtil.showToastShort("数据未填写完整");
                }

                break;
        }
    }
}
