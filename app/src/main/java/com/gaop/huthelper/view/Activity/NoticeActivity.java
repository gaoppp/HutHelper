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
        notice.setTime(getString(R.string.updateNoticeTime));
        notice.setTitle(getString(R.string.updateNoticeTitle));
        notice.setContent(getString(R.string.updateNoticeContent));
        return notice;
    }


    @OnClick(R.id.imgbtn_toolbar_back)
    public void onClick() {
        finish();
    }
}
