package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.utils.DateUtil;
import com.gaop.huthelper.view.adapter.AutoRVAdapter;
import com.gaop.huthelper.view.adapter.ViewHolder;
import com.gaop.huthelperdao.Explesson;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 实验课表Fragment
 * Created by 高沛 on 16-10-16.
 */

public class ExpLessonFragment extends Fragment {

    private static final String TAG = "ExpLessonFragment";
    @BindView(R.id.rv_explesson)
    RecyclerView rvExplesson;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    int mNum; //页号
    int currWeek;

    public static ExpLessonFragment newInstance(int num) {
        ExpLessonFragment fragment = new ExpLessonFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这里简单的用num区别标签，其实具体应用中可以使用真实的fragment对象来作为叶片
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    /**
     * 为Fragment加载布局时调用
     **/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_explesson, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        currWeek = DateUtil.getNowWeek();
        initData();

    }

    private void initData() {
        List<Explesson> list = DBHelper.getExpLessons();
        if (list.size() != 0) {
            Collections.sort(list);
            if (mNum == 0) {
                Iterator<Explesson> it = list.iterator();
                while (it.hasNext()) {
                    Explesson explesson = it.next();
                    if (Integer.valueOf(explesson.getWeeks_no()) < currWeek || (Integer.valueOf(explesson.getWeeks_no())) == currWeek && DateUtil.getWeekOfToday() > Integer.valueOf(explesson.getWeek())) {
                        it.remove();
                    }
                }
            } else if (mNum == 1) {
                Iterator<Explesson> it = list.iterator();
                while (it.hasNext()) {
                    Explesson explesson = it.next();
                    if (!(Integer.valueOf(explesson.getWeeks_no()) < currWeek || (Integer.valueOf(explesson.getWeeks_no())) == currWeek && DateUtil.getWeekOfToday() > Integer.valueOf(explesson.getWeek()))) {
                        it.remove();
                    }
                }
            }
            if(list.size()==0){
                rvExplesson.setVisibility(View.GONE);
                rlEmpty.setVisibility(View.VISIBLE);
                return;
            }
            ExpLessonAdapter adapter = new ExpLessonAdapter(getActivity(), list, true);
            rvExplesson.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rvExplesson.setAdapter(adapter);
        } else {
            rvExplesson.setVisibility(View.GONE);
            rlEmpty.setVisibility(View.VISIBLE);
        }
    }

    class ExpLessonAdapter extends AutoRVAdapter {

        public ExpLessonAdapter(Context context, List<Explesson> list, boolean isrecyable) {
            super(context, list, isrecyable);
        }

        @Override
        public int onCreateViewHolder(int viewType) {
            return R.layout.item_explesson;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Explesson explesson = (Explesson) list.get(position);

            TextView tvLesson = holder.getTextView(R.id.tv_expitem_lesson);
            if (TextUtils.isEmpty(explesson.getObj())) {
                tvLesson.setText(explesson.getLesson());
            } else {
                tvLesson.setText(explesson.getLesson()+"-"+explesson.getObj());
            }

            holder.getTextView(R.id.tv_expitem_realtime).setText(explesson.getWeeks_no() + "周周" + getWeekNum(explesson.getWeek())
                    + " " + explesson.getReal_time());
            holder.getTextView(R.id.tv_expitem_place).setText(explesson.getLocate());
            holder.getTextView(R.id.tv_expitem_teacher).setText(explesson.getTeacher());

        }
    }
    private String getWeekNum(String num){
        switch (num){
            case "1":
                return "一";
            case "2":
                return "二";
            case "3":
                return "三";
            case "4":
                return "四";
            case "5":
                return "五";
            case "6":
                return "六";
            case "7":
                return "日";

        }
        return num;
    }
}
