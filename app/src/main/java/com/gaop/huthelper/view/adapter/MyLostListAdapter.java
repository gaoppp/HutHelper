package com.gaop.huthelper.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.model.entity.Lose;
import com.gaop.huthelper.R;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.view.activity.ImportActivity;
import com.gaop.huthelperdao.User;

import java.util.List;

import static com.gaop.huthelper.utils.ToastUtil.showToastShort;

/**
 * Created by gaop on 16-11-10.
 */

public class MyLostListAdapter extends RecyclerView.Adapter<MyLostListAdapter.ViewHolder> {
    public List<Lose> loseList;
    private Context context;
    private User user;

    public MyLostListAdapter(Context context, List<Lose> datas) {
        this.loseList = datas;
        this.context = context;
        user = DBHelper.getUserDao().get(0);
    }

    //创建新View，被LayoutManager所调用
    @Override
    public MyLostListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mylose, viewGroup, false);
        MyLostListAdapter.ViewHolder vh = new MyLostListAdapter.ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final MyLostListAdapter.ViewHolder viewHolder, final int position) {
        final Lose lose = loseList.get(position);
       // Log.e( "onBindViewHolder: ",lose.getUsername()+" "+lose.getContent()+" "+lose.getTime() );
        viewHolder.ItemAuthor.setText(lose.getUsername());
        viewHolder.ItemContent.setText(lose.getContent());
        viewHolder.ItemTime.setText(lose.getCreated_on());
        viewHolder.ItemName.setText(lose.getTit());

        String s = lose.getTime().substring(0,10);
        viewHolder.ItemGetTime.setText(s);
        viewHolder.ItemGetPlace.setText(lose.getLocate());
        StringBuilder connect = new StringBuilder();
        if (!TextUtils.isEmpty(lose.getPhone()))
            connect.append("电话").append(lose.getPhone()).append(" ");
        if (!TextUtils.isEmpty(lose.getQq()))
            connect.append("QQ").append(lose.getQq()).append(" ");
        if (!TextUtils.isEmpty(lose.getWechat()))
            connect.append("微信").append(lose.getWechat()).append(" ");

        viewHolder.ItemConnect.setText(connect);
        List<String> images = lose.getPics();

        viewHolder.ItemIamges.setVisibility(View.VISIBLE);
        if (images != null && images.size() != 0) {
            viewHolder.ItemIamges.setAdapter(new LoseImageAdapter(context, images));
        }

   viewHolder.ItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(position);
            }
        });


    }

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确认移除该条失物招领吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteLost(position);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void deleteLost(final int position) {

        SubscriberOnNextListener<HttpResult> subscriberOnNextListener = new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult o) {
                if (o.getMsg().equals("ok")) {
                    loseList.remove(position);
                    notifyDataSetChanged();
                } else if ("令牌错误".equals(o.getMsg())) {
                    context.startActivity(new Intent(context, ImportActivity.class));
                    showToastShort("账号异地登录，请重新登录");
                } else
                    showToastShort(o.getMsg());
            }
        };
        HttpMethods.getInstance().deleteLose(
                new ProgressSubscriber<HttpResult>(subscriberOnNextListener, context)
                , user.getStudentKH(), user.getRember_code(), loseList.get(position).getId());
    }


    //获取数据的数量
    @Override
    public int getItemCount() {
        return loseList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ItemAuthor;
        public TextView ItemTime;
        public TextView ItemGetTime;
        public TextView ItemGetPlace;
        public TextView ItemContent;
        public TextView ItemConnect;
        public GridView ItemIamges;
        public Button ItemDelete;
        public TextView ItemName;

        public ViewHolder(View view) {
            super(view);
            ItemName = (TextView) view.findViewById(R.id.tv_item_mylose_what);
            ItemGetTime = (TextView) view.findViewById(R.id.tv_item_mylose_when);
            ItemContent = (TextView) view.findViewById(R.id.tv_item_mylose_content);
            ItemTime = (TextView) view.findViewById(R.id.tv_item_mylose_createtime);
            ItemConnect = (TextView) view.findViewById(R.id.tv_item_mylose_connect);
            ItemGetPlace = (TextView) view.findViewById(R.id.tv_item_mylose_where);
            ItemAuthor = (TextView) view.findViewById(R.id.tv_item_mylose_username);
            ItemIamges = (GridView) view.findViewById(R.id.gv_item_mylose_image);
            ItemDelete = (Button) view.findViewById(R.id.btn_item_mylose_delete);
        }
    }

}
