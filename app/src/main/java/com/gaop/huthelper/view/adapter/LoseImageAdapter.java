package com.gaop.huthelper.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.utils.DensityUtils;
import com.gaop.huthelper.view.activity.ShowImgActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaop on 16-11-22.
 */

public class LoseImageAdapter extends BaseAdapter {
    Context context;
    List<String> list;

    public LoseImageAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        ImageView img = new ImageView(context);
        img.setTag(list.get(position));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        int width = DensityUtils.dp2px(context, 100);// 获取屏幕宽度
        int height = 0;
        // 对当前的列数进行设置imgView的宽度
        height = width;
        img.setLayoutParams(new AbsListView.LayoutParams(width, height));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle mBundle = new Bundle();
                mBundle.putStringArrayList("urls", (ArrayList<String>) list);
                mBundle.putInt("curr", position);
                Intent intent = new Intent();
                intent.setClass(context, ShowImgActivity.class);
                intent.putExtras(mBundle);
                context.startActivity(intent);
            }
        });
        if (list.get(position).equals(img.getTag())) {
            Picasso.with(context).load(HttpMethods.BASE_URL + list.get(position)).resize(width, width).centerCrop().placeholder(R.drawable.img_loading)
                    .error(R.drawable.img_error).into(img);
        }
        return img;
    }
}
