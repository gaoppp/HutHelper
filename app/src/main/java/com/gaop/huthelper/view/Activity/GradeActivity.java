package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelper.jiekou.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;
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
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop1 on 2016/8/1.
 */
public class GradeActivity extends BaseActivity {

    @BindView(R.id.toolba_scorer)
    Toolbar toolbaScorer;
    @BindView(R.id.iv_update_score)
    ImageView ivUpdateScore;
    @BindView(R.id.btn_grade_showall)
    Button btnGradeShowall;
    @BindView(R.id.tv_grade_jd)
    TextView tvGradeJd;
    @BindView(R.id.tv_grade_avgfs)
    TextView tvGradeAvgfs;
    @BindView(R.id.tv_grade_nopassnum)
    TextView tvGradeNopassnum;
    @BindView(R.id.tv_grade_pie)
    TextView tvGradePie;
    @BindView(R.id.scroll_grade_body)
    ScrollView scrollGradeBody;
    @BindView(R.id.tv_score_empty)
    TextView tvScoreEmpty;


    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_grade;
    }


    @Override
    public void doBusiness(Context mContext) {

        ButterKnife.bind(this);
        toolbaScorer.setTitle("成绩");
        setSupportActionBar(toolbaScorer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbaScorer.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (!PrefUtil.getBoolean(GradeActivity.this, "isLoadGrade", false)) {
            LoadGrade();

        } else {
            InitData();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.iv_update_score, R.id.btn_grade_showall})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_update_score:
                if (fastClick()) {
                    LoadGrade();
                }
                break;
            case R.id.btn_grade_showall:
                if (fastClick()) {
                    startActivity(GradeListActivity.class);
                }
                break;
        }
    }

    private void InitData() {
        //获取grade
        Grade grade = DBHelper.getGradeDao().get(0);
        if (grade == null) {
            scrollGradeBody.setVisibility(View.GONE);
            tvScoreEmpty.setVisibility(View.VISIBLE);
        } else {
            tvGradeAvgfs.setText("" + (float) (Math.round(grade.getAvgScore() * 100)) / 100);
            tvGradeJd.setText("" + (float) (Math.round(grade.getAvgJd() * 100)) / 100);
            tvGradeNopassnum.setText(grade.getNoPassNum() + "/" + grade.getAllNum());

            List<CourseGrade> gradeList = DBHelper.getCourseGradeDao();
            List<Trem> tremList = DBHelper.getTremDao();
            //Collections.sort(tremList, Collator.getInstance(java.util.Locale.CHINA));
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


            PieGraph pg = (PieGraph) findViewById(R.id.graph_grade_pie);

            pg.removeSlices();
            PieSlice slice = new PieSlice();
            slice.setColor(Color.parseColor("#99CC00"));
            slice.setValue(grade.getGetsf());
            pg.addSlice(slice);
            slice = new PieSlice();
            slice.setColor(Color.parseColor("#FFBB33"));
            slice.setValue(grade.getNopassxf());
            pg.addSlice(slice);
            tvGradePie.setText("未获得学分/总学分:" + grade.getNopassxf() + "/" + grade.getAllsf());


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

            String[] colors = {"#99CC00", "#FFBB33", "#7cbd7b", "#777878", "#F24949", "#a15679", "#8DCE3F", "#b9a571"};
            ArrayList<Bar> points = new ArrayList<Bar>();
            for (Trem t : tremList) {
                Bar d = new Bar();
                Random random = new Random();
                int i = Math.abs(random.nextInt()) % 8;
                d.setColor(Color.parseColor(colors[i]));
                d.setName(t.getContent());
                d.setValue((float) (Math.round(allJd.get(t.getContent()) / xf.get(t.getContent()) * 100)) / 100);
                points.add(d);
            }
            BarGraph g = (BarGraph) findViewById(R.id.graph_grade_bar);

            g.setBars(points);
            g.setUnit(" ");
        }


    }

    private void LoadGrade() {
        User user = DBHelper.getUserDao().get(0);
        SubscriberOnNextListener getGradeData = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String o) {
                if ("ok".equals(o))
                    InitData();
                else if ("令牌错误".equals(o)) {
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                    startActivity(ImportActivity.class);
                } else
                    ToastUtil.showToastShort(o);
            }
        };
        HttpMethods.getInstance().getGradeData(GradeActivity.this,
                new ProgressSubscriber<String>(getGradeData, GradeActivity.this), user.getStudentKH(),
                user.getRember_code()
        );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
