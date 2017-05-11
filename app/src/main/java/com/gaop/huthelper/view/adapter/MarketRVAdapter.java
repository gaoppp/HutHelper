package com.gaop.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.model.entity.GoodsListItem;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.utils.DensityUtils;
import com.gaop.huthelper.utils.ScreenUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 二手市场数据适配器
 * Created by gaop1 on 2016/7/22.
 */
public class MarketRVAdapter extends RecyclerView.Adapter<MarketRVAdapter.ViewHolder> {
    public List<GoodsListItem> datas;
    private Context context;

    public MarketRVAdapter(Context context, List<GoodsListItem> datas) {
        this.datas = datas;
        this.context = context;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_market_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
       // int num=(int)(Math.random()*4);
       // viewHolder.ItemMain.setBackgroundResource(bg[num]);
        viewHolder.ItemContent.setText(datas.get(position).getTitle());
        viewHolder.ItemPrice.setText("￥" + datas.get(position).getPrize());
        viewHolder.ItemCreate.setText(datas.get(position).getCreated_on());
        int width = (ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 45)) / 2;// 获取屏幕宽度
        int height = width;

        Picasso.with(context).load(HttpMethods.BASE_URL + datas.get(position).getImage()).resize(width, height).centerCrop()
                .into(viewHolder.ItemIamge);
    }

   // public final int[] bg={R.drawable.bg_list_empty,R.drawable.bg2_list_empty,R.drawable.bg3_list_empty,R.drawable.bg4_list_empty};


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
       // public LinearLayout ItemMain;

        public ViewHolder(View view) {
            super(view);
            ItemContent = (TextView) view.findViewById(R.id.tv_content_marketitem);
            ItemCreate = (TextView) view.findViewById(R.id.tv_crateon_marketitem);
            ItemIamge = (ImageView) view.findViewById(R.id.iv_img_marketitem);
            ItemPrice = (TextView) view.findViewById(R.id.tv_price_marketitem);
          //  ItemMain=(LinearLayout) view.findViewById(R.id.ll_market_list);
        }
    }


}
