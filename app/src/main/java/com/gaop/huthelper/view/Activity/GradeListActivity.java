package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.AutoRVAdapter;
import com.gaop.huthelper.adapter.ViewHolder;
import com.gaop.huthelper.utils.ScreenUtils;
import com.gaop.huthelperdao.CourseGrade;
import com.gaop.huthelperdao.Trem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop1 on 2016/8/31.
 */
public class GradeListActivity extends BaseActivity {

    List<CourseGrade> lessonList;
    List<Trem> tremList;
    @BindView(R.id.tv_choose_grade)
    TextView tvChoose;
    @BindView(R.id.rv_gradelist)
    RecyclerView rvGradelist;
    private int currChoose = 0;
    /**
     * 选择选择列表弹出窗口
     */
    protected PopupWindow weekListWindow;
    /**
     * 显示选择列表的listview
     */
    protected ListView weekListView;
    /**
     * 选择列表弹出窗口的layout
     */
    protected View popupWindowLayout;
    private RVAdapter adapter;


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
        Log.e("d", "d22");
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
            adapter = new RVAdapter(GradeListActivity.this, lessonList);
            rvGradelist.setLayoutManager(new LinearLayoutManager(GradeListActivity.this, LinearLayoutManager.VERTICAL, false));
            rvGradelist.setAdapter(adapter);
        }
        Log.e("d", "d");

    }


    /**
     * 设置成绩选择列表PopupWindows
     *
     * @param parent 相对位置View
     */
    private void showChooseListWindows(View parent) {
        int width = ScreenUtils.getScreenWidth(GradeListActivity.this) / 2;
        if (weekListWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popupWindowLayout = layoutInflater.inflate(R.layout.popupwindow_coursetable, null);
            weekListView = (ListView) popupWindowLayout.findViewById(R.id.lv_weekchoose_coursetable);
            final List<String> list = new ArrayList<>();
            list.add("所有成绩");
            for (Trem t : tremList) {
                list.add(t.getContent());
            }
            weekListView.setAdapter(new ChooseListAdapter(GradeListActivity.this, list));
            weekListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    weekListWindow.dismiss();
                    tvChoose.setText(list.get(position));
                    currChoose = position;
                    changeList(position);
                }
            });
            weekListWindow = new PopupWindow(popupWindowLayout, width, width + 100);
        }
        weekListWindow.setFocusable(true);
        //设置点击外部可消失
        weekListWindow.setOutsideTouchable(true);
        weekListWindow.setBackgroundDrawable(new BitmapDrawable());
        weekListWindow.showAsDropDown(parent, -(width - parent.getWidth()) / 2, 0);
    }

    private void changeList(int trem) {
        if (trem == 0) {
            adapter.ChangeList(lessonList);
        } else {
            Trem trem1 = tremList.get(trem - 1);
            List<CourseGrade> lessonL = new ArrayList<>();
            for (CourseGrade l : lessonList) {
                if (trem1.getXQ().equals(l.getXQ()) && trem1.getXN().equals(l.getXN())) {
                    lessonL.add(l);
                }
            }
            adapter.ChangeList(lessonL);
        }
    }


    @OnClick({R.id.imgbtn_toolbar_back, R.id.tv_choose_grade})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.tv_choose_grade:
                showChooseListWindows(tvChoose);
                break;
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

        public void ChangeList(List<?> list) {
            this.list = list;
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
            holder.setTextView(R.id.tv_lesson_jd, ("绩点" + grade.getJD()));
            holder.setTextView(R.id.tv_lesson_score, "" + cj);
            holder.setTextView(R.id.tv_lesson_xf, "学分：" + grade.getXF());
            holder.setTextView(R.id.tv_lesson_xq, "学期：" + grade.getXN() + "年第" + grade.getXQ() + "学期");

            if (cj < 60) {
                holder.getImageView(R.id.iv_lesson_ispass).setVisibility(View.VISIBLE);
            } else
                holder.getImageView(R.id.iv_lesson_ispass).setVisibility(View.GONE);
            if ("1".equals(grade.getCXBJ())) {
                holder.getTextView(R.id.tv_lesson_iscx).setVisibility(View.VISIBLE);
            } else
                holder.getTextView(R.id.tv_lesson_iscx).setVisibility(View.GONE);
        }
    }


    class ChooseListAdapter extends BaseAdapter {

        Context context;
        List<String> data;

        public ChooseListAdapter(Context context, List<String> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_weeklist, null);
            } else {
                view = convertView;
            }
            TextView textView = (TextView) view.findViewById(R.id.tv_weeklist_item);
            if (position == currChoose) {
                textView.setBackgroundResource(R.color.blue);
                textView.setTextColor(getResources().getColor(R.color.white));
            } else {
                textView.setBackgroundResource(R.drawable.btn_weekitem);
                textView.setTextColor(getResources().getColor(R.color.black));
            }
            textView.setText(data.get(position));
            return view;
        }
    }

}
