package com.gaop.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.model.entity.LinksBean;

import java.util.List;

/**
 * Created by gaop1 on 2017/4/9.
 */

public class VideoItemRVAdapter extends RecyclerView.Adapter<VideoItemRVAdapter.ViewHolder> {
    public List<LinksBean.VedioListBean> datas;
    private Context context;

    public VideoItemRVAdapter(Context context, List<LinksBean.VedioListBean> datas) {
        this.datas = datas;
        this.context = context;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        int num = (int) (Math.random() * 4);
        viewHolder.ItemMain.setBackgroundResource(bg[num]);
        viewHolder.ItemContent.setText(datas.get(position).getTitle());
        viewHolder.ItemNum.setText("第" + (position + 1) + "集");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(null, v, viewHolder.getPosition(), viewHolder.getItemId());
            }
        });
    }

    public final int[] bg = {R.drawable.bg_list_empty, R.drawable.bg2_list_empty, R.drawable.bg3_list_empty, R.drawable.bg4_list_empty};

    private AdapterView.OnItemClickListener onItemClickListener;


    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ItemContent;
        public TextView ItemNum;
        public LinearLayout ItemMain;

        public ViewHolder(View view) {
            super(view);
            ItemContent = (TextView) view.findViewById(R.id.tv_videoitem_content);
            ItemNum = (TextView) view.findViewById(R.id.tv_videoitem_num);
            ItemMain = (LinearLayout) view.findViewById(R.id.ll_videoitem_list);
        }
    }


}