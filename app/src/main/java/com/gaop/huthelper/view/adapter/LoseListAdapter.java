package com.gaop.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaop.huthelper.model.entity.Lose;
import com.gaop.huthelper.R;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.utils.DensityUtils;
import com.gaop.huthelper.utils.ScreenUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 高沛 on 16-11-22.
 */

public class LoseListAdapter extends RecyclerView.Adapter<LoseListAdapter.ViewHolder> {

    public List<Lose> list;
    private Context context;

    public LoseListAdapter(Context context, List<Lose> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_lose_new, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    public final int[] bg={R.drawable.bg_list,R.drawable.bg2_list,R.drawable.bg3_list,R.drawable.bg4_list};
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Lose lose = (Lose) list.get(position);
        viewHolder.ItemContent.setText(lose.getTit()+"\n"+lose.getLocate()+"\n"+lose.getContent());
        viewHolder.ItemAuthor.setText(lose.getUsername());
        viewHolder.ItemTime.setText(lose.getCreated_on());
        List<String> images = lose.getPics();
        viewHolder.ItemIamge.setVisibility(View.GONE);

        int num=(int)(Math.random()*4);
        viewHolder.ItemMain.setBackgroundResource(bg[num]);

        if (images != null && images.size() != 0) {
            viewHolder.ItemIamge.setVisibility(View.VISIBLE);
            viewHolder.ItemIamge.setAdapter(new SmallBitmapGridViewAdapter(context, images));
        }
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return list.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ItemContent;
        public TextView ItemAuthor;
        public TextView ItemTime;
        public GridView ItemIamge;
        public LinearLayout ItemMain;

        public ViewHolder(View view) {
            super(view);
            ItemContent = (TextView) view.findViewById(R.id.tv_item_lose_content);
            ItemTime = (TextView) view.findViewById(R.id.tv_item_lose_time);
            ItemAuthor = (TextView) view.findViewById(R.id.tv_item_lose_author);
            ItemIamge = (GridView) view.findViewById(R.id.gv_item_lose_img);
            ItemMain= (LinearLayout) view.findViewById(R.id.ll_item_lose_main);
        }
    }


    public class SmallBitmapGridViewAdapter extends BaseAdapter {
        Context context;
        List<String> list;

        public SmallBitmapGridViewAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size()>=2?2:list.size();
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
            img.setPadding(2, 2, 2, 2);
            img.setTag(list.get(position));
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);

            int width = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 117);// 获取屏幕宽度
            int height = 0;
            width = width / 4;// 对当前的列数进行设置imgView的宽度
            height = width;
            img.setLayoutParams(new AbsListView.LayoutParams(width, height));
            if (list.get(position).equals(img.getTag())) {
                Picasso.with(context).load(HttpMethods.BASE_URL + list.get(position)).resize(width, width).centerCrop().placeholder(R.drawable.img_loading)
                        .error(R.drawable.img_error).into(img);
            }
            return img;
        }
    }
}
