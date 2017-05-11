package com.gaop.huthelper.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelperdao.Notice;

import java.util.List;


/**
 * 通知数据适配器
 * Created by gaop1 on 2016/5/6.
 */
public class NoticeAdapter extends BaseAdapter
{

    private LayoutInflater mInflater;

    List<Notice> notices;

    public NoticeAdapter(Context context, List<Notice> notices) {
        mInflater = LayoutInflater.from(context);
        this.notices=notices;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_notice, null);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_noticeitem_time);
            viewHolder.Title = (TextView) convertView.findViewById(R.id.tv_noticeitem_title);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.time.setText(notices.get(position).getTime());
        viewHolder.Title.setText(notices.get(position).getTitle());
        return convertView;
    }


    class ViewHolder {
        TextView Title;
        TextView time;
    }

}
