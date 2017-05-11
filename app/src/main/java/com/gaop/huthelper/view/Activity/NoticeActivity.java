package com.gaop.huthelper.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelper.view.adapter.NoticeAdapter;
import com.gaop.huthelper.utils.ToastUtil;
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
    NoticeAdapter noticeAdapter;

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
        noticeAdapter = new NoticeAdapter(NoticeActivity.this, noticeList);
        mListView.setAdapter(noticeAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = noticeList.get(position).getType();
                if (type == null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notice", noticeList.get(position));
                    startActivity(NoticeItemActivity.class, bundle);
                } else if (type.equals("url")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", noticeList.get(position).getContent());
                    bundle.putInt("type", WebViewActivity.TYPE_NOTICE);
                    startActivity(WebViewActivity.class, bundle);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notice", noticeList.get(position));
                    startActivity(NoticeItemActivity.class, bundle);
                }

            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == (noticeList.size() - 1)) {
                    ToastUtil.showToastShort("更新日志不能删除");
                } else {
                    dialog(position);
                }
                return true;
            }
        });

    }

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NoticeActivity.this);
        builder.setMessage("确认移除该通知吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DBHelper.deleteNoticeByid(noticeList.get(position).getId());
                noticeList.remove(position);
                noticeAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private Notice addNotice() {

        Notice notice = new Notice();
        notice.setTime("2017.4.30");
        notice.setTitle("工大助手更新");
        notice.setContent("工大助手0.8.8更新日志\n1.修复考试查询\n2.添加宣讲会、视频专栏\n3.新增成绩排名\n4.主页应用可自定义\n0.8.7更新日志\n1.修复bug\n2.说说点赞\n0.8.6更新日志\n1.全新界面\n2.新增头像\n之前版本\n1.工大导航,工大助手统一认证\n2.新增在线作业功能\n3.电费查询优化\n4.校历优化\n5.课程表优化,支持自定义增删课程\n6.二手市场,说说同步工大导航\n" +
                "7.增加实验课表,在课程表中\n" + "8.增加考试查询，请及时刷新数据\n" + "9.支持修改昵称和密码\n" + "10.新增校招薪资查询\n11.新增失物招领\n12.添加考试倒计时\n13.优化WebView,降低Crash率 " + "\n祝使用愉快。\n工大助手团队");
        return notice;
    }


    @OnClick(R.id.imgbtn_toolbar_back)
    public void onClick() {
        finish();
    }
}
