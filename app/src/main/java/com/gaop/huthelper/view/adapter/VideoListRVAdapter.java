package com.gaop.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.model.entity.LinksBean;
import com.gaop.huthelper.utils.DensityUtils;
import com.gaop.huthelper.utils.ScreenUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 视频专栏数据适配器
 * Created by 高沛 on 2017/4/9.
 */
public class VideoListRVAdapter extends RecyclerView.Adapter<VideoListRVAdapter.ViewHolder> {
    public List<LinksBean> datas;
    private Context context;
    private String url;

    public VideoListRVAdapter(Context context, List<LinksBean> datas,String url) {
        this.datas = datas;
        this.context = context;
        this.url=url;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
      //  int num=(int)(Math.random()*4);
       // viewHolder.ItemMain.setBackgroundResource(bg[num]);
        viewHolder.ItemTitle.setText(datas.get(position).getName());
        viewHolder.ItemTotal.setText("共"+datas.get(position).getVedioList().size()+"集");
        int width = (ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 45)) / 2;// 获取屏幕宽度
        int height = width*3/4;
        Picasso.with(context).load(url + datas.get(position).getImg()).resize(width, height).centerCrop()
                .into(viewHolder.ItemIamge);
    }

    //public final int[] bg={R.drawable.bg_list_empty,R.drawable.bg2_list_empty,R.drawable.bg3_list_empty,R.drawable.bg4_list_empty};


    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas==null?0:datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ItemTitle;
        public TextView ItemTotal;
        public ImageView ItemIamge;
       // public LinearLayout ItemMain;

        public ViewHolder(View view) {
            super(view);
            ItemTitle = (TextView) view.findViewById(R.id.tv_title_video);
            ItemTotal = (TextView) view.findViewById(R.id.tv_total_video);
            ItemIamge = (ImageView) view.findViewById(R.id.iv_img_video);
           // ItemMain=(LinearLayout) view.findViewById(R.id.ll_video_list);
        }
    }


}
