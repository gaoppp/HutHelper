package com.gaop.huthelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.gaop.huthelperdao.CourseGrade;

import java.util.List;

/**
 * Created by gaop1 on 2016/7/9.
 */
public abstract class AutoRVAdapter extends RecyclerView.Adapter<AutoRVAdapter.RVHolder> {
    public List<?> list;
    public Context context;

    public AutoRVAdapter(Context context, List<?> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(onCreateViewHolder(viewType), null);
        return new RVHolder(view);
    }

    public abstract int onCreateViewHolder(int viewType);


    @Override
    public void onViewRecycled(RVHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final RVHolder holder, final int position) {

        onBindViewHolder(holder.getViewHolder(), position);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null, v, holder.getPosition(), holder.getItemId());
                }
            });
        }
        if(onItemLongClickListener!=null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(null, v, holder.getPosition(), holder.getItemId());
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private AdapterView.OnItemClickListener onItemClickListener;

    private AdapterView.OnItemLongClickListener onItemLongClickListener;

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener=onItemLongClickListener;
    }

    public abstract void onBindViewHolder(ViewHolder holder, int position);


    public class RVHolder extends RecyclerView.ViewHolder {


        private ViewHolder viewHolder;

        public RVHolder(View itemView) {
            super(itemView);
            viewHolder = ViewHolder.getViewHolder(itemView);
        }


        public ViewHolder getViewHolder() {
            return viewHolder;
        }

    }

}
