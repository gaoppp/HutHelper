package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gaop.huthelper.R;
import com.gaop.huthelper.model.entity.Electric;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelper.view.ElectricDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 电费查询
 * TODO 注释代码为空调使用情况数据 暂时没有api
 * Created by 高沛 on 2016/7/23.
 */
public class ElecticActivity extends BaseActivity {
    @BindView(R.id.et_electric_lou)
    EditText etElectricLou;
    @BindView(R.id.et_electric_hao)
    EditText etElectricHao;

//    @BindView(R.id.radio_group_electric)
//    RadioGroup radioGroupElectric;
//    @BindView(R.id.rl_ele_choose)
//    RelativeLayout rlEleChoose;
//    @BindView(R.id.elebar_electric)
//    BarListView elebarElectric;

//    @BindView(R.id.tv_electric_wd)
//    TextView tvElectricWd;//温度
//    @BindView(R.id.tv_electric_tianqi)
//    TextView tvElectricTianqi;

//    @BindView(R.id.radio_electric_open)
//    RadioButton radioElectricOpen;
//    @BindView(R.id.radio_electric_unopen)
//    RadioButton radioElectricUnopen;

//
//    @BindView(R.id.num1)
//    EditText num1;
//    @BindView(R.id.num2)
//    EditText num2;
//    @BindView(R.id.btn_get)
//    Button btnGet;
//    @BindView(R.id.imgbtn_toolbar_back)
//    ImageButton imgbtnToolbarBack;
//    @BindView(R.id.tv_toolbar_title)
//    TextView tvToolbarTitle;

    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_electric_new;
    }


    @Override
    public void doBusiness(final Context mContext) {
        ButterKnife.bind(this);
        // getWeather();
//        List<BarListView.Bar> barList = new ArrayList<>();
//        BarListView.Bar bar = elebarElectric.new Bar();
//        bar.setName("开了");
//        bar.setNum(1000);
//
//        BarListView.Bar bar2 = elebarElectric.new Bar();
//        bar2.setName("没开");
//        bar2.setNum(1500);
//        barList.add(bar);
//        barList.add(bar2);
//
//        elebarElectric.setBars(barList);
//        etElectricLou.setText(PrefUtil.getString(ElecticActivity.this, "Lou", ""));
//        etElectricHao.setText(PrefUtil.getString(ElecticActivity.this, "Hao", ""));
    }


    @OnClick({R.id.imgbtn_toolbar_back, R.id.btn_electric_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.btn_electric_ok:
                if (etElectricLou.getText().toString() != "" && etElectricHao.getText().toString() != "") {
                    if (fastClick()) {
                        getData();
                    }
                } else {
                    ToastUtil.showToastShort("数据未填写完整");
                }
                break;
        }
    }

//    /**
//     * 获取天气数据
//     */
//    private void getWeather() {
//        SubscriberOnNextListener getWeatherData = new SubscriberOnNextListener<WeatherData>() {
//            @Override
//            public void onNext(WeatherData o) {
//                if(o.getError_code()==0) {
//                    tvElectricTianqi.setText("株洲 | " + o.getResult().getData().getRealtime().getWeather().getInfo());
//                    tvElectricWd.setText(o.getResult().getData().getRealtime().getWeather().getTemperature() + "°");
//                }else {
//                    ToastUtil.showToastShort("获取天气失败");
//                }
//            }
//
//        };
//        HttpMethods.getInstance().getWeather(
//                new ProgressSubscriber<WeatherData>(getWeatherData, ElecticActivity.this));
//    }

    /**
     * 获取电费数据
     */
    private void getData() {
        CommUtil.hideSoftInput(ElecticActivity.this);
        SubscriberOnNextListener getElectricData = new SubscriberOnNextListener<Electric>() {
            @Override
            public void onNext(Electric o) {
                PrefUtil.setString(ElecticActivity.this, "Lou", etElectricLou.getText().toString());
                PrefUtil.setString(ElecticActivity.this, "Hao", etElectricHao.getText().toString());
                if (o == null) {
                    ToastUtil.showToastShort("无结果。请输入正确栋号、寝室号。如35 522");
                } else {
                    ElectricDialog dialog = new ElectricDialog(ElecticActivity.this, o);
                    dialog.show();
                }
            }

//                @Override
//                public void onError(Electric electric) {
//                    super();
//                }
        };
        HttpMethods.getInstance().getElecticData(
                new ProgressSubscriber<Electric>(getElectricData, ElecticActivity.this),
                etElectricLou.getText().toString(), etElectricHao.getText().toString());
    }


}
