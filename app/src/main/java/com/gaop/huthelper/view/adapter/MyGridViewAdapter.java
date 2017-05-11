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
import com.gaop.huthelper.utils.ScreenUtils;
import com.gaop.huthelper.view.activity.ShowImgActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaop on 16-11-10.
 */

public class MyGridViewAdapter extends BaseAdapter {
    Context context;
    int num;
    List<String> list;

    public MyGridViewAdapter(Context context, List<String> list, int num) {
        this.context = context;
        this.num = num;
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
        img.setBackgroundResource(R.color.white);
        img.setTag(list.get(position));
        if(num==1){
            int max=DensityUtils.dp2px(context, 200);
            img.setMaxHeight(max);
            img.setMaxWidth(max);
            img.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT));
        }else{
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            int width = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 35);// 获取屏幕宽度
            int height = 0;
            width = width / 3;// 对当前的列数进行设置imgView的宽度
            height = width;
            img.setLayoutParams(new AbsListView.LayoutParams(width, height));
        }

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
            Picasso.with(context).load(HttpMethods.BASE_URL + list.get(position)).placeholder(R.drawable.img_loading)
                    .error(R.drawable.img_error).into(img);
        }
        return img;
    }
}