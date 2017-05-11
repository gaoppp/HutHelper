package com.gaop.huthelper.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;

import com.gaop.huthelper.R;

import java.util.List;

/**
 * 图片选择适配器
 * Created by gaop on 16-9-11.
 */
public class ChoosePicAdapter extends AutoRVAdapter {

    public ChoosePicAdapter(Context context, List<Bitmap> list) {
        super(context, list);
    }

    @Override
    public int onCreateViewHolder(int viewType) {
        return R.layout.item_addgoods_img;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getImageView(R.id.iv_addgoodsitem_img).setImageBitmap((Bitmap) list.get(position));
    }
}