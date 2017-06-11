package com.gaop.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaop.huthelper.model.entity.OfferInfo;
import com.gaop.huthelper.R;

import java.util.List;

/**
 * Created by 高沛 on 16-11-14.
 */

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.ViewHolder> {
    public List<OfferInfo> offerInfoList;
    private Context context;

    public OfferListAdapter(Context context, List<OfferInfo> datas) {
        this.offerInfoList = datas;
        this.context = context;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public OfferListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_offer, viewGroup, false);
        OfferListAdapter.ViewHolder vh = new OfferListAdapter.ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final OfferListAdapter.ViewHolder viewHolder, final int position) {
        final OfferInfo info = offerInfoList.get(position);
        viewHolder.ItemCompany.setText(info.getCompany());
        viewHolder.ItemTime.setText(info.getTime());
        viewHolder.ItemPositon.setText(info.getPosition());
        viewHolder.ItemCity.setText(info.getCity());
        viewHolder.ItemScore.setText(""+info.getScore());
        viewHolder.ItemNumber.setText("浏览量： "+info.getNumber());
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return offerInfoList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ItemCompany;
        public TextView ItemTime;
        public TextView ItemPositon;
        public TextView ItemCity;
        public TextView ItemNumber;
        public TextView ItemScore;

        public ViewHolder(View view) {
            super(view);
            ItemCompany = (TextView) view.findViewById(R.id.tv_offer_item_company);
            ItemTime = (TextView) view.findViewById(R.id.tv_offer_item_time);
            ItemPositon = (TextView) view.findViewById(R.id.tv_offer_item_position);
            ItemCity = (TextView) view.findViewById(R.id.tv_offer_item_city);
            ItemNumber = (TextView) view.findViewById(R.id.tv_offer_item_number);
            ItemScore = (TextView) view.findViewById(R.id.tv_offer_item_score);
        }
    }

}