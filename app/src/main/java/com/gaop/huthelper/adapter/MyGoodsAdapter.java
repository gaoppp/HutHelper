package com.gaop.huthelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaop.huthelper.Model.MyGoodsItem;
import com.gaop.huthelper.R;

import java.util.List;

/**
 * 用户商品数据适配器
 * Created by gaop1 on 2016/7/22.
 */
public class MyGoodsAdapter extends RecyclerView.Adapter<MyGoodsAdapter.ViewHolder> {
    public List<MyGoodsItem> datas;
    private Context context;
    public MyGoodsAdapter(Context context, List<MyGoodsItem> datas) {
        this.datas = datas;
        this.context=context;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mygoods,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.ItemContent.setText(datas.get(position).getContent());
        viewHolder.ItemTitle.setText(datas.get(position).getTit());
        viewHolder.ItemCreate.setText(datas.get(position).getCreated_on());
        viewHolder.ItemViewNum.setText(datas.get(position).getView_cnt());
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ItemContent;
        public TextView ItemTitle;
        public TextView ItemCreate;
        public TextView ItemViewNum;
        public ViewHolder(View view){
            super(view);
            ItemContent= (TextView) view.findViewById(R.id.tv_mygoodsitem_content);
            ItemCreate= (TextView) view.findViewById(R.id.tv_mygoodsitem_time);
            ItemTitle= (TextView) view.findViewById(R.id.tv_mygoodsitem_title);
            ItemViewNum= (TextView) view.findViewById(R.id.tv_mygoodsitem_viewnum);
        }
    }
}
