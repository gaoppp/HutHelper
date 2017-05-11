package com.gaop.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.utils.ButtonUtils;
import com.gaop.huthelper.view.CustomItemTouchHelper;
import com.gaop.huthelperdao.Menu;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by gaop1 on 2017/4/10.
 */

public class MenuRVAdapter extends RecyclerView.Adapter<MenuRVAdapter.ViewHolder> implements CustomItemTouchHelper.ItemTouchHelperAdapter {
    public List<Menu> datas;
    private Context context;
    private boolean isEdit;

    public MenuRVAdapter(Context context, List<Menu> datas) {
        this.datas = datas;
        this.context = context;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_editmenulist, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    /**
     * 图片索引
     * 0--图书馆 1--课程表   2--考试   3--成绩   4--作业   5--二手   6--说说   7--电费   8--薪水
     * 9--实验课  10--校历 11--失物  12--视频 13--全部 14--宣讲会
     */
    public final int[] PIC_INDEX = {R.mipmap.ic_library, R.mipmap.ic_curriculum, R.mipmap.ic_exam, R.mipmap.ic_score
            , R.mipmap.ic_homework, R.mipmap.ic_secondhand, R.mipmap.ic_social, R.mipmap.ic_elec, R.mipmap.ic_salary
            , R.mipmap.ic_lab, R.mipmap.ic_calendar, R.mipmap.ic_lost, R.mipmap.ic_vedio, R.mipmap.ic_more, R.mipmap.ic_xuanjianghui};

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Menu item = datas.get(position);
        viewHolder.tvTitle.setText(item.getTitle());
        Picasso.with(context).load(PIC_INDEX[item.getPicId()]).into(viewHolder.ivPic);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(null, v, viewHolder.getPosition(), viewHolder.getItemId());
            }
        });
        if (isEdit) {
            viewHolder.ivEdit.setVisibility(View.VISIBLE);
            if (item.getIsMain()) {
                viewHolder.ivEdit.setImageResource(R.mipmap.ic_edit_delete);
            } else {
                viewHolder.ivEdit.setImageResource(R.mipmap.ic_editadd);
            }
            viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ButtonUtils.isFastDoubleClick(-1, 800))
                        return;
                    editClickListener.onEditClick(v, viewHolder.getPosition());
                }
            });
        } else {
            viewHolder.ivEdit.setVisibility(View.GONE);
        }
    }

    public void removeItem(int position) {
        datas.remove(position);
        notifyItemRemoved(position);
    }

    public void insertItem(int posito, Menu menu) {
        datas.add(posito, menu);
        notifyItemInserted(posito);
    }

    @Override
    public void onItemDismiss(int position) {
        datas.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 拖拽移动时回调
     *
     * @param from
     * @param to
     */
    @Override
    public void onItemMove(int from, int to) {
        Collections.swap(datas, from, to);
        notifyItemMoved(from, to);
    }

    private AdapterView.OnItemClickListener onItemClickListener;
    private EditClickLister editClickListener;

    public void setOnEditClickListenter(EditClickLister listenter) {
        editClickListener = listenter;
    }

    public interface EditClickLister {
        void onEditClick(View v, int postion);
    }

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    /**
     * 设置编辑/完成模式
     *
     * @param isEdit
     */
    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageView ivEdit;
        public ImageView ivPic;

        public ViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_menu_title);
            ivEdit = (ImageView) view.findViewById(R.id.iv_menu_edit);
            ivPic = (ImageView) view.findViewById(R.id.iv_menu_pic);
        }
    }
}


