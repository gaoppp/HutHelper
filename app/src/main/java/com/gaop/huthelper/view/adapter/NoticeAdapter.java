package com.gaop.huthelper.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.view.activity.NoticeItemActivity;
import com.gaop.huthelper.view.activity.WebViewActivity;
import com.gaop.huthelperdao.Notice;

import java.util.List;


/**
 * 通知数据适配器
 * Created by gaop1 on 2016/5/6.
 */
public class NoticeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    List<Notice> notices;

    public NoticeAdapter(Context context, List<Notice> notices) {
        mInflater = LayoutInflater.from(context);
        this.notices = notices;
        this.context = context;
    }

    @Override
    public int getCount() {
        return notices.size();
    }

    @Override
    public Object getItem(int position) {
        return notices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_notice, null);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_noticeitem_time);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_noticeitem_title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.tv_noticeitem_content);
            viewHolder.showNotice = (Button) convertView.findViewById(R.id.btn_noticeitem_show);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.time.setText(notices.get(position).getTime());
        viewHolder.title.setText(notices.get(position).getTitle());
        viewHolder.content.setText(notices.get(position).getContent());
        viewHolder.showNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = notices.get(position).getType();
                if ("url".equals(type)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", notices.get(position).getContent());
                    bundle.putInt("type", WebViewActivity.TYPE_NOTICE);
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notice", notices.get(position));
                    Intent intent = new Intent(context, NoticeItemActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }


    class ViewHolder {
        TextView title;
        TextView time;
        TextView content;
        Button showNotice;
    }

}
