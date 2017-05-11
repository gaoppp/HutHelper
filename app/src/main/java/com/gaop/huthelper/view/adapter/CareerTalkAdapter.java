package com.gaop.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.model.entity.CareerTalk;

import java.util.List;

/**
 * Created by yzy on 2017/4/13.
 */

public class CareerTalkAdapter extends RecyclerView.Adapter<CareerTalkAdapter.ViewHolder> implements View.OnClickListener{
    public List<CareerTalk> talkdatas;
    private Context context;
    private CareerTalk careerTalk;

    private OnCareerTalkItemClickListener mOnCareertalkItemClickListener=null;
    public void setOnItemClickListener(OnCareerTalkItemClickListener listener){
        this.mOnCareertalkItemClickListener=listener;
    }

    public CareerTalkAdapter(Context context,List<CareerTalk> list){
        this.talkdatas=list;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_careertalk_list, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        careerTalk=talkdatas.get(position);

//        String[] data=careerTalk.getHoldtime().split(" ");
//        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
//        Date da=null;
//        try {
//            da=sp.parse(data[0]);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Bitmap ic= HttpMethods.getInstance()

        holder.companyView.setText(careerTalk.getCompany());
        holder.addressView.setText(careerTalk.getUniversityShortName()+"   "+careerTalk.getAddress());
        holder.holdTimeView.setText(careerTalk.getHoldtime());
        holder.logoView.setImageResource(R.drawable.au_hut);
        //holder.weekView.setText(DateUtil.getWeekOfDate(da));
//        if (careerTalk.getIs_official()==1){
//            holder.isOfficialBtn.setVisibility(View.VISIBLE);
//        }else {
//            holder.isOfficialBtn.setVisibility(View.INVISIBLE);
//        }

    }



    @Override
    public int getItemCount() {
        return talkdatas.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnCareertalkItemClickListener!=null){
            mOnCareertalkItemClickListener.onItemClick(view,careerTalk);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView logoView;
        public TextView companyView;
        public TextView addressView;
        public TextView holdTimeView;
       // public TextView isOfficialBtn;
       // public TextView weekView;

        public ViewHolder(View itemView) {
            super(itemView);
            logoView=(ImageView)itemView.findViewById(R.id.careertalk_log);
            companyView=(TextView)itemView.findViewById(R.id.careertalk_company);
            addressView=(TextView)itemView.findViewById(R.id.careertalk_address);
            holdTimeView=(TextView)itemView.findViewById(R.id.careertalk_holdtime);
//            isOfficialBtn=(TextView)itemView.findViewById(R.id.careertalk_is_official);
//            weekView=(TextView)itemView.findViewById(R.id.careertalk_week);
//            isSaveBtn=(ImageButton)itemView.findViewById(R.id.careertalk_is_save);
        }
    }

    public static interface OnCareerTalkItemClickListener{
        void onItemClick(View view, CareerTalk careerTalk);
    }





}
