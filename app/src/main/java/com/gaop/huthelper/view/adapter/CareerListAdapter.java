package com.gaop.huthelper.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.model.entity.CareerTalk;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;

/**
 * Created by 高沛 on 2017/4/21.
 */

public class CareerListAdapter extends LoadMoreAdapter<CareerTalk> {

    private Context mContext;
    private LifecycleTransformer lifecycleTransformer;

    public CareerListAdapter(Context context, ArrayList<CareerTalk> dataList, @NonNull LifecycleTransformer mTransformer) {
        super(context, dataList, mTransformer);
        this.mContext = context;
        this.lifecycleTransformer = mTransformer;
        this.dataList=dataList;
    }

    @Override
    protected void bindHolder(RecyclerView.ViewHolder viewholder, int position) {
        CareerTalk careerTalk=dataList.get(position);
        ViewHolder holder = (ViewHolder) viewholder;

        holder.companyView.setText(careerTalk.getCompany());
        holder.addressView.setText(careerTalk.getUniversityShortName()+"   "+careerTalk.getAddress());
        holder.holdTimeView.setText(careerTalk.getHoldtime());
        Picasso.with(mContext).load(careerTalk.getLogoUrl()).into(holder.logoView);
    }

    @Override
    protected RecyclerView.ViewHolder createViewholder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_careertalk_list, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getViewType(int position) {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView logoView;
        public TextView companyView;
        public TextView addressView;
        public TextView holdTimeView;

        public ViewHolder(View itemView) {
            super(itemView);
            logoView = (ImageView) itemView.findViewById(R.id.careertalk_log);
            companyView = (TextView) itemView.findViewById(R.id.careertalk_company);
            addressView = (TextView) itemView.findViewById(R.id.careertalk_address);
            holdTimeView = (TextView) itemView.findViewById(R.id.careertalk_holdtime);
//            isOfficialBtn = (TextView) itemView.findViewById(R.id.careertalk_is_official);
//            weekView = (TextView) itemView.findViewById(R.id.careertalk_week);
//            isSaveBtn=(ImageButton)itemView.findViewById(R.id.careertalk_is_save);
        }
    }
}
