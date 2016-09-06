package com.gaop.huthelper.adapter;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.gaop.huthelper.Model.GoodsListItem;
import com.gaop.huthelper.R;
import com.gaop.huthelper.net.HttpMethods;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

/**
 * Created by gaop1 on 2016/7/22.
 */
public class MarketRVAdapter extends RecyclerView.Adapter<MarketRVAdapter.ViewHolder> {
    public List<GoodsListItem> datas;
    private Context context;
    public MarketRVAdapter(Context context,List<GoodsListItem> datas) {
        this.datas = datas;
        this.context=context;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_market_list,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.ItemContent.setText(datas.get(position).getTitle());
        viewHolder.ItemPrice.setText(datas.get(position).getPrize());
        viewHolder.ItemCreate.setText(datas.get(position).getCreated_on());
        Picasso.with(context).load(HttpMethods.BASE_URL + datas.get(position).getImage())
                .into(viewHolder.ItemIamge);
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ItemContent;
        public TextView ItemPrice;
        public TextView ItemCreate;
        public ImageView ItemIamge;
        public ViewHolder(View view){
            super(view);
            ItemContent= (TextView) view.findViewById(R.id.tv_content_marketitem);
            ItemCreate= (TextView) view.findViewById(R.id.tv_crateon_marketitem);
            ItemIamge= (ImageView) view.findViewById(R.id.iv_img_marketitem);
            ItemPrice= (TextView) view.findViewById(R.id.tv_price_marketitem);
        }
    }
}
