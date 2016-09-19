package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.AutoRVAdapter;
import com.gaop.huthelper.adapter.ViewHolder;
import com.gaop.huthelperdao.CourseGrade;
import com.gaop.huthelperdao.Trem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaop1 on 2016/8/31.
 */
public class GradeListActivity extends BaseActivity {
    List<CourseGrade> lessonList;
    List<Trem> tremList;
    @BindView(R.id.toolbar_gradelist)
    Toolbar toolbarGradelist;
    @BindView(R.id.rv_gradelist)
    RecyclerView rvGradelist;
    RVAdapter adapter;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_gradelist;
    }


    @Override
    public void doBusiness(Context mContext) {

        ButterKnife.bind(this);
        toolbarGradelist.setTitle("所有成绩");
        setSupportActionBar(toolbarGradelist);
        toolbarGradelist.hideOverflowMenu();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarGradelist.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lessonList = DBHelper.getCourseGradeDao();
        tremList = DBHelper.getTremDao();
        if (lessonList != null && tremList != null) {
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
            adapter=new RVAdapter(GradeListActivity.this, lessonList);
            rvGradelist.setLayoutManager(new LinearLayoutManager(GradeListActivity.this,LinearLayoutManager.VERTICAL,false));
            rvGradelist.setAdapter(adapter);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0,0,0,"所有");
        int size=tremList.size();
        for (int i=0;i<size;i++) {
            menu.add(i+1,i+1,i+1,tremList.get(i).getContent());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        changeList(item.getItemId());
        return true;

    }

    private void changeList(int trem) {
        if (trem==0) {
            adapter.ChangeList(lessonList);
        } else {
            Trem trem1=tremList.get(trem-1);
            List<CourseGrade> lessonL = new ArrayList<>();
            for (CourseGrade l : lessonList) {
                if (trem1.getXQ().equals(l.getXQ()) && trem1.getXN().equals(l.getXN())) {
                    lessonL.add(l);
                }
            }
           adapter.ChangeList(lessonL);
        }
    }


    public class RVAdapter extends AutoRVAdapter {

        public RVAdapter(Context context, List<CourseGrade> list) {
            super(context, list);
            this.list = list;
        }


        @Override
        public int onCreateViewHolder(int viewType) {
            return R.layout.item_gradelist;
        }

        public void ChangeList(List<?> list){
            this.list=list;
            notifyDataSetChanged();
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            CourseGrade grade = ((CourseGrade) list.get(position));
            float cj;
            if (grade.getBKCJ() != null)
                cj = Math.max(Float.valueOf(grade.getZSCJ()), Float.valueOf(grade.getBKCJ()));
            else
                cj = Float.valueOf(grade.getZSCJ());
            holder.setTextView(R.id.tv_lesson_name, grade.getKCMC());
            holder.setTextView(R.id.tv_lesson_jd, ("绩点"+grade.getJD()));
            holder.setTextView(R.id.tv_lesson_score, ""+cj);
            holder.setTextView(R.id.tv_lesson_xf, "学分："+grade.getXF());
            holder.setTextView(R.id.tv_lesson_xq, "学期："+grade.getXN()+"年第"+grade.getXQ()+"学期");

            if (cj<60) {
                holder.getImageView(R.id.iv_lesson_ispass).setVisibility(View.VISIBLE);
            } else
                holder.getImageView(R.id.iv_lesson_ispass).setVisibility(View.GONE);
            if ("1".equals(grade.getCXBJ())) {
                holder.getTextView(R.id.tv_lesson_iscx).setVisibility(View.VISIBLE);
            } else
                holder.getTextView(R.id.tv_lesson_iscx).setVisibility(View.GONE);
        }
    }
}
