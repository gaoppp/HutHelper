package com.gaop.huthelper.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.Model.Say;
import com.gaop.huthelper.R;
import com.gaop.huthelper.jiekou.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.DensityUtils;
import com.gaop.huthelper.utils.ScreenUtils;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelper.view.Activity.ImportActivity;
import com.gaop.huthelper.view.Activity.MySayListActivity;
import com.gaop.huthelper.view.Activity.ShowImgActivity;
import com.gaop.huthelper.view.PicureGridView;
import com.gaop.huthelperdao.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * 说说数据适配器
 * Created by gaop1 on 2016/7/22.
 */
public class SayRVAdapter extends RecyclerView.Adapter<SayRVAdapter.ViewHolder> {
    public List<Say> saydatas;
    private Context context;
    private User user;

    public SayRVAdapter(Context context, List<Say> datas) {
        this.saydatas = datas;
        this.context = context;
        user = DBHelper.getUserDao().get(0);
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_say_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final Say say = saydatas.get(position);
        viewHolder.ItemAuthor.setText(say.getUsername());
        //        viewHolder.Itemzannum.setText("" + say.getLikes());
        viewHolder.ItemContent.setText(say.getContent());
        viewHolder.ItemTime.setText(say.getCreated_on());
        viewHolder.ItemXy.setText(say.getDep_name());
        viewHolder.Itemviewcnt.setText(" 浏览(" + say.getView_cnt() + ")");
        List<String> images = say.getPics();
        viewHolder.Itemistop.setVisibility(View.GONE);
        if (saydatas.get(position).getIs_top().equals("1"))
            viewHolder.Itemistop.setVisibility(View.VISIBLE);
        viewHolder.ItemIamges.setVisibility(View.GONE);

        if (images != null && images.size() != 0) {
            viewHolder.ItemIamges.setVisibility(View.VISIBLE);
            int num;//获取当前的图片数目
            num = images.size();
            int col = 1;//默认列数
            if (num == 1) {
                viewHolder.ItemIamges.setNumColumns(1);
                col = 1;
            } else if (num == 2 || num == 4) {
                viewHolder.ItemIamges.setNumColumns(2);
                col = 2;
            } else {
                viewHolder.ItemIamges.setNumColumns(3);
                col = 3;
            }
            viewHolder.ItemIamges.setAdapter(new MyGridViewAdapter(context, images, num, col));
        }


        if (say.getUser_id().equals(user.getUser_id())) {
            viewHolder.Itemcandelete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.Itemcandelete.setVisibility(View.GONE);
        }

//        // 取出bean中当记录状态是否为true，是的话则给img设置focus点赞图片
//        if (saydatas.get(position).islike()) {
//            viewHolder.Itemzan.setImageResource(R.mipmap.ic_like);
//        } else {
//            viewHolder.Itemzan.setImageResource(R.mipmap.ic_unlike);
//
//        }
//
//       viewHolder.Itemzan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(saydatas.get(position).islike()){
//                    ToastUtil.showToastShort("已经赞过了");
//                    return;
//                }
//                ((ImageView)v).setImageResource(R.mipmap.ic_like);
//                saydatas.get(position).setIslike(true);
//                saydatas.get(position).setLikes(saydatas.get(position).getLikes()+1);
//                viewHolder.Itemzannum.setText(""+(saydatas.get(position).getLikes()));
//                likeSay(viewHolder,saydatas.get(position));
//                AnimationTools.scale(v);
//
//
//
//
//            }
//        });
        viewHolder.ItemAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("user_id", saydatas.get(position).getUser_id());
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(context, MySayListActivity.class);
                context.startActivity(intent);
            }
        });
        viewHolder.Itemcandelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(position);

            }
        });
        viewHolder.Itemcomment.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        viewHolder.Itemcomment.setAdapter(new commentsAdapter(context, say.getComments()));

        viewHolder.Itemsubitcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommUtil.hideSoftInput((Activity) context);
                if (viewHolder.Itemaddcomment.getText().toString().equals( "")) {
                    ToastUtil.showToastShort("评论内容不能为空");
                    return;
                }
                addComment(position, say,viewHolder.Itemaddcomment.getText().toString());
            }
        });
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return saydatas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ItemContent;
        public TextView ItemAuthor;
        public TextView ItemTime;
        public TextView ItemXy;
        public PicureGridView ItemIamges;
        //  public TextView Itemzannum;
        // public ImageView Itemzan;
        public TextView Itemistop;
        public TextView Itemcandelete;
        public TextView Itemviewcnt;
        public RecyclerView Itemcomment;
        public EditText Itemaddcomment;
        public Button Itemsubitcomment;

        public ViewHolder(View view) {
            super(view);
            Itemviewcnt = (TextView) view.findViewById(R.id.tv_item_viewnum);
            ItemContent = (TextView) view.findViewById(R.id.tv_item_saycontent);
            ItemTime = (TextView) view.findViewById(R.id.tv_item_say_time);
            ItemXy = (TextView) view.findViewById(R.id.tv_item_say_xy);
            ItemAuthor = (TextView) view.findViewById(R.id.tv_item_sayauthor);
            ItemIamges = (PicureGridView) view.findViewById(R.id.rv_item_sayimg);
//            Itemzan = (ImageView) view.findViewById(R.id.iv_item_sayzan);
//            Itemzannum = (TextView) view.findViewById(R.id.tv_item_sayzannum);
            Itemistop = (TextView) view.findViewById(R.id.tv_item_say_istop);
            Itemcandelete = (TextView) view.findViewById(R.id.tv_item_deletesay);

            Itemcomment = (RecyclerView) view.findViewById(R.id.rv_say_comments);
            Itemaddcomment = (EditText) view.findViewById(R.id.et_addcomment_content);
            Itemsubitcomment = (Button) view.findViewById(R.id.btn_addcomment_submit);
        }
    }

    public void addComment(final int position, final Say say, final String comment) {
        HttpMethods.getInstance().addComment(new Subscriber<HttpResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult o) {

                if (o.getMsg().equals("ok")) {
                    saydatas.get(position).getComments().add(new Say.CommentsBean(user.getUser_id(), user.getUsername(), comment));
                    notifyItemChanged(position);
                    ToastUtil.showToastShort("评论成功");
                } else if (o.getMsg().equals("令牌错误")) {
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                    context.startActivity(new Intent(context, ImportActivity.class));
                } else {
                    ToastUtil.showToastShort(o.getMsg());
//                    holder.Itemzan.setImageResource(R.mipmap.ic_unlike);
//                    say.setLikes(say.getLikes()-1);
//                    say.setIslike(false);
                }
            }
        }, user, say.getId(),comment);
    }

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确认移除该说说吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteSay(position);
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

    private void deleteSay(final int position) {

        SubscriberOnNextListener<HttpResult> subscriberOnNextListener = new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult o) {
                if (o.getMsg().equals("ok")) {
                    saydatas.remove(position);
                    notifyDataSetChanged();
                } else if ("令牌错误".equals(o.getMsg())) {
                    context.startActivity(new Intent(context, ImportActivity.class));
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                } else
                    ToastUtil.showToastShort(o.getMsg());
            }
        };
        HttpMethods.getInstance().deleteSay(
                new ProgressSubscriber<HttpResult>(subscriberOnNextListener, context)
                , user.getStudentKH(), user.getRember_code(), saydatas.get(position).getId());
    }

    public class MyGridViewAdapter extends BaseAdapter {
        Context context;
        int num;
        int col;
        List<String> list;

        public MyGridViewAdapter(Context context, List<String> list, int num, int col) {
            this.context = context;
            this.num = num;
            this.col = col;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            ImageView img = new ImageView(context);
            img.setTag(list.get(position));

            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            int width = ScreenUtils.getScreenWidth(context)- DensityUtils.dp2px(context,12+(col-1)*5);// 获取屏幕宽度
            //Log.i("tag", "width" + width);
            int height = 0;
            width = width / col;// 对当前的列数进行设置imgView的宽度
            height = width;

            img.setLayoutParams(new AbsListView.LayoutParams(width, height));
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle mBundle = new Bundle();
                    mBundle.putStringArrayList("urls", (ArrayList<String>) list);
                    mBundle.putInt("curr", position);
                    Intent intent = new Intent();
                    intent.setClass(context, ShowImgActivity.class);
                    intent.putExtras(mBundle);
                    context.startActivity(intent);
                }
            });
            if (list.get(position).equals(img.getTag())) {
                Picasso.with(context).load(HttpMethods.BASE_URL + list.get(position)).placeholder(R.drawable.img_loading)
                        .error(R.drawable.img_error).into(img);
            }
            return img;
        }
    }

    public class commentsAdapter extends AutoRVAdapter {

        public commentsAdapter(Context context, List<Say.CommentsBean> list) {
            super(context, list);
        }

        @Override
        public int onCreateViewHolder(int viewType) {
            return R.layout.item_saycomments;
        }

        @Override
        public void onBindViewHolder(com.gaop.huthelper.adapter.ViewHolder holder, int position) {
            final Say.CommentsBean bean = (Say.CommentsBean) list.get(position);
            holder.getTextView(R.id.tv_comment_name).setText(bean.getUsername());
            holder.getTextView(R.id.tv_comment_content).setText(bean.getComment());
            holder.getTextView(R.id.tv_comment_name).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id", bean.getUser_id());
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(context, MySayListActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
