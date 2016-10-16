package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.NoticeAdapter;
import com.gaop.huthelperdao.Notice;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeActivity extends BaseActivity {


    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    private ListView mListView;
    private TextView mTextView;


    @Override
    public void initParms(Bundle parms) {

    }

    List<Notice> noticeList;

    @Override
    public int bindLayout() {
        return R.layout.activity_notice;
    }

    @Override
    public void doBusiness(Context mContext) {
        initView();
    }

    public void initView() {
        ButterKnife.bind(this);
        mListView = (ListView) findViewById(R.id.lv_notice);
        mTextView = (TextView) findViewById(R.id.tv_notice_empty);
        tvToolbarTitle.setText("通知公告");
        noticeList = DBHelper.getNoticeDao();
        Collections.reverse(noticeList);
        noticeList.add(addNotice());
        mListView.setEmptyView(mTextView);
        mListView.setAdapter(new NoticeAdapter(NoticeActivity.this, noticeList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("notice", noticeList.get(position));
                startActivity(NoticeItemActivity.class, bundle);
            }
        });

    }

    private Notice addNotice() {

        Notice notice = new Notice();
        notice.setTime("2016.10.12");
        notice.setTitle("工大助手更新");
        notice.setContent("工大助手更新日志\n1.工大导航,工大助手统一认证\n2.新增在线作业功能\n3.电费查询优化\n4.校历优化\n5.课程表优化,支持自定义增删课程\n6.二手市场,说说同步工大导航\n7.图书馆接口整理中,目前仅内网可用\n" +
                "\n8.增加实验课表,在课程表中查看"+"\n祝使用愉快。\n工大助手团队");
        return notice;
    }


    @OnClick(R.id.imgbtn_toolbar_back)
    public void onClick() {
        finish();
    }
}
