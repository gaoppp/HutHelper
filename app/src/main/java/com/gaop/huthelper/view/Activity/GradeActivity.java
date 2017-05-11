package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelper.view.BarListView;
import com.gaop.huthelper.view.CirclePie;
import com.gaop.huthelperdao.CourseGrade;
import com.gaop.huthelperdao.Grade;
import com.gaop.huthelperdao.Trem;
import com.gaop.huthelperdao.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop1 on 2016/8/1.
 */
public class GradeActivity extends BaseActivity {

    @BindView(R.id.pie_grade_xf)
    CirclePie pieGradeXf;
    @BindView(R.id.bar_grade_jd)
    BarListView barGradeJd;
    @BindView(R.id.tv_grade_nopassnum)
    TextView tvGradeNopassnum;
    @BindView(R.id.tv_grade_avgjd)
    TextView tvGradeAvgjd;
    @BindView(R.id.scroll_grade_body)
    ScrollView scrollGradeBody;
    @BindView(R.id.tv_grade_empty)
    TextView tvGradeEmpty;

    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_grade_new;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        if (!PrefUtil.getBoolean(GradeActivity.this, "isLoadGrade", false)) {
            loadGrade();
        } else {
            initData();
        }
    }

    private void initData() {
        //获取grade
        Grade grade = DBHelper.getGradeDao().get(0);
        if (grade.getAllNum() == null || grade.getAllNum() == 0) {
            scrollGradeBody.setVisibility(View.GONE);
            tvGradeEmpty.setVisibility(View.VISIBLE);
        } else {

            tvGradeAvgjd.setText("综合绩点     " + (float) (Math.round(grade.getAvgJd() * 100)) / 100);
            tvGradeNopassnum.setText("总挂科数     "+grade.getNoPassNum());

            List<CourseGrade> gradeList = DBHelper.getCourseGradeDao();
            List<Trem> tremList = DBHelper.getTremDao();

            Collections.sort(tremList, new Comparator<Trem>() {
                public int compare(Trem list1, Trem list2) {
                    if (list1.getXN().compareTo(list2.getXN()) > 0) {
                        return 1;
                    } else if (list1.getXN().equals(list2.getXN())) {
                        if (Integer.valueOf(list1.getXQ()) > Integer.valueOf(list2.getXQ())) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                }
            });

            pieGradeXf.setCurrNum(grade.getAllsf(),grade.getAllsf()-grade.getNopassxf());
            Map<String, Float> xf = new HashMap<>();
            Map<String, Float> allJd = new HashMap<>();
            for (Trem t : tremList) {
                xf.put(t.getContent(), 0F);
                allJd.put(t.getContent(), 0F);
            }
            for (CourseGrade g : gradeList) {
                for (Trem t : tremList) {
                    if (g.getXQ().equals(t.getXQ()) && g.getXN().equals(t.getXN())) {
                        allJd.put(t.getContent(), allJd.get(t.getContent()) +
                                Float.valueOf(g.getXF()) * Float.valueOf(g.getJD()));
                        xf.put(t.getContent(), xf.get(t.getContent()) + Float.valueOf(g.getXF()));
                    }
                }
            }

            ArrayList<BarListView.Bar> points = new ArrayList<BarListView.Bar>();
            for (Trem t : tremList) {
                BarListView.Bar d = barGradeJd.new Bar();
                d.setName(t.getXN() + "第" + t.getXQ() + "学期");
                d.setNum((float)(Math.round(allJd.get(t.getContent()) / xf.get(t.getContent()) * 100)) / 100);
                points.add(d);
            }
            barGradeJd.setMaxValue(5F);
            barGradeJd.setBars(points);
        }


    }

    private void loadGrade() {
        User user = DBHelper.getUserDao().get(0);
        SubscriberOnNextListener getGradeData = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String o) {
                if ("ok".equals(o))
                    initData();
                else if ("令牌错误".equals(o)) {
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                    startActivity(ImportActivity.class);
                } else
                    ToastUtil.showToastShort(o);
            }
        };
        HttpMethods.getInstance().getGradeData(GradeActivity.this,
                new ProgressSubscriber<String>(getGradeData, GradeActivity.this), user
        );
    }

    @OnClick({R.id.imgbtn_toolbar_back, R.id.btn_grade_showall,R.id.imgbtn_toolbar_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.btn_grade_showall:
                if (PrefUtil.getBoolean(GradeActivity.this, "isLoadGrade", false)) {
                    startActivity(GradeListActivity.class);
                } else {
                    ToastUtil.showToastShort("暂未导入成绩");
                }
                break;
            case R.id.imgbtn_toolbar_refresh:
                loadGrade();
                break;

        }
    }

}
