package com.gaop.huthelper.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.model.entity.Say;
import com.gaop.huthelper.model.entity.SayLikeCache;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.ninepictureview.MyNinePictureLayout;
import com.gaop.huthelper.utils.AnimationTools;
import com.gaop.huthelper.utils.DensityUtils;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelper.view.activity.ImportActivity;
import com.gaop.huthelper.view.activity.MySayListActivity;
import com.gaop.huthelper.view.activity.ShowImgActivity;
import com.gaop.huthelperdao.User;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

import static com.gaop.huthelper.utils.ToastUtil.showToastShort;


/**
 * 说说数据适配器
 * Created by 高沛 on 2016/7/22.
 */
public class SayRVAdapter extends LoadMoreAdapter<Say> {

    // public List<Say> sayList;
    //  private Context context;
    private User user;
    private AddComment addComment;

    public SayRVAdapter(Context context, ArrayList<Say> dataList, @NonNull LifecycleTransformer mTransformer) {
        super(context, dataList, mTransformer);
        user = DBHelper.getUserDao().get(0);
    }


    @Override
    public int getViewType(int position) {
        return 0;
    }

//    //创建新View，被LayoutManager所调用
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_say_list, viewGroup, false);
//        ViewHolder vh = new ViewHolder(view);
//        return vh;
//    }

    @Override
    protected void bindHolder(RecyclerView.ViewHolder holder,final int position) {
        final Say say = dataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.ItemAuthor.setText(say.getUsername());
        //        viewHolder.Itemzannum.setText("" + say.getLikes());
        if (!TextUtils.isEmpty(say.getHead_pic())) {
            int width = DensityUtils.dp2px(context, 50);
            Picasso.with(context).load(HttpMethods.BASE_URL + say.getHead_pic_thumb()).resize(width, width).into(viewHolder.Itemavatar);
        } else {
            Picasso.with(context).load(R.mipmap.avatar_default).into(viewHolder.Itemavatar);
        }
        viewHolder.ItemContent.setText(say.getContent());
        viewHolder.ItemTime.setText(say.getCreated_on());
        viewHolder.ItemXy.setText(say.getDep_name());
        viewHolder.ItemLikeNum.setText("" + say.getLikes());

        List<String> images = say.getPics();
        viewHolder.ItemIamges.setUrlList(images);

        if (say.getUser_id().equals(user.getUser_id())) {
            viewHolder.Itemcandelete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.Itemcandelete.setVisibility(View.GONE);
        }
        say.setIslike(SayLikeCache.isHave(say.getId()));
        // 取出bean中当记录状态是否为true，是的话则给img设置focus点赞图片
        if (say.islike()) {
            viewHolder.ItemLike.setImageResource(R.mipmap.ic_like);
        } else {
            viewHolder.ItemLike.setImageResource(R.mipmap.ic_unlike);
        }
        //点赞点击事件设置
        viewHolder.ItemLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (say.islike()) {
                    ((ImageView) v).setImageResource(R.mipmap.ic_unlike);
                    say.setIslike(false);
                    say.setLikes(say.getLikes() - 1);
                    SayLikeCache.removeLike(say.getId());
                } else {
                    ((ImageView) v).setImageResource(R.mipmap.ic_like);
                    say.setIslike(true);
                    say.setLikes(say.getLikes() + 1);
                    SayLikeCache.addLike(say.getId());
                }
                viewHolder.ItemLikeNum.setText("" + say.getLikes());
                likeSay(viewHolder, say);
                AnimationTools.scale(v);
            }
        });
        //查看作者点击事件
        viewHolder.ItemAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("user_id", say.getUser_id());
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(context, MySayListActivity.class);
                context.startActivity(intent);
            }
        });
        //评论设置
        int num = say.getComments().size();
        viewHolder.ItemCommentNum.setText("" + num);
        if (num == 0) {
            viewHolder.Itemcomment.setVisibility(View.GONE);
            viewHolder.ItemLine.setVisibility(View.GONE);
            viewHolder.ItemCommentNum.setText("" + 0);
        } else {
            viewHolder.Itemcomment.setVisibility(View.VISIBLE);
            viewHolder.ItemCommentNum.setText("" + num);
            viewHolder.ItemLine.setVisibility(View.VISIBLE);
            ScrollLiearLayoutManager l = new ScrollLiearLayoutManager(context);
            l.setScrollEnabled(false);
            viewHolder.Itemcomment.setLayoutManager(l);
            viewHolder.Itemcomment.setAdapter(new commentsAdapter(context, say.getComments()));
        }
        //添加评论点击事件设置
        viewHolder.ItemAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addComment != null)
                    addComment.addComment(position, say);
                else
                    ToastUtil.showToastShort("error");
            }
        });

        //删除点击事件设置
        viewHolder.Itemcandelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(position);
            }
        });
        //头像点击事件
        viewHolder.Itemavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("url", say.getHead_pic());
                Intent intent = new Intent(context, ShowImgActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder createViewholder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_say_list, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

//    //将数据与界面进行绑定的操作
//    @Override
//    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
//
//
//    }

    private void likeSay(final ViewHolder holder, final Say say) {
        HttpMethods.getInstance().likeSay(new Subscriber<HttpResult>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                //showToastShort("点赞失败！请检查网络后重试！");
                //不处理
            }

            @Override
            public void onNext(HttpResult o) {
                if (o.getMsg().equals("令牌错误")) {
                    showToastShort("账号异地登录，请重新登录");
                    context.startActivity(new Intent(context, ImportActivity.class));
                }
            }
        }, user.getStudentKH(), user.getRember_code(), say.getId());
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ItemContent;//内容
        public TextView ItemAuthor;//作者
        public TextView ItemTime;//时间
        public TextView ItemXy;//学院
        public MyNinePictureLayout ItemIamges;//图片
        public ImageView Itemcandelete;//删除按钮
        public RecyclerView Itemcomment;//评论布局
        public ImageView ItemAddComment;//添加评论
        public ImageView ItemLike;//点赞
        public TextView ItemLikeNum;//点赞数
        public TextView ItemCommentNum;//评论数
        public ImageView Itemavatar;//头像
        public ImageView ItemLine;

        public ViewHolder(View view) {
            super(view);
            ItemContent = (TextView) view.findViewById(R.id.tv_item_saycontent);
            ItemTime = (TextView) view.findViewById(R.id.tv_item_say_time);
            ItemXy = (TextView) view.findViewById(R.id.tv_item_say_xy);
            ItemAuthor = (TextView) view.findViewById(R.id.tv_item_sayauthor);
            ItemIamges = (MyNinePictureLayout) view.findViewById(R.id.rv_item_sayimg);
            Itemcandelete = (ImageView) view.findViewById(R.id.iv_item_deletesay);
            Itemcomment = (RecyclerView) view.findViewById(R.id.rv_say_comments);
            Itemavatar = (ImageView) view.findViewById(R.id.iv_item_say_avatar);
            ItemAddComment = (ImageView) view.findViewById(R.id.iv_say_item_addcommit);
            ItemCommentNum = (TextView) view.findViewById(R.id.tv_say_item_commitnum);
            ItemLike = (ImageView) view.findViewById(R.id.iv_say_item_like);
            ItemLikeNum = (TextView) view.findViewById(R.id.tv_say_item_likenum);
            ItemLine = (ImageView) view.findViewById(R.id.iv_item_say);
        }
    }

    //设置添加评论接口回调
    public void setAddComment(AddComment comment) {
        this.addComment = comment;
    }

    public interface AddComment {
        void addComment(final int position, final Say say);
    }

    //确认删除提示
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

    /**
     * 删除说说
     * TODO MVP处理，从Adapter中转移出去
     *
     * @param position
     */
    private void deleteSay(final int position) {

        SubscriberOnNextListener<HttpResult> subscriberOnNextListener = new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult o) {
                if (o.getMsg().equals("ok")) {
                    dataList.remove(position);
                    notifyDataSetChanged();
                } else if ("令牌错误".equals(o.getMsg())) {
                    context.startActivity(new Intent(context, ImportActivity.class));
                    showToastShort("账号异地登录，请重新登录");
                } else
                    showToastShort(o.getMsg());
            }
        };
        HttpMethods.getInstance().deleteSay(
                new ProgressSubscriber<HttpResult>(subscriberOnNextListener, context)
                , user.getStudentKH(), user.getRember_code(), dataList.get(position).getId());
    }

    /**
     * 评论数据适配器
     * 实现TextView部分文本不同颜色，部分文本设置点击事件
     */
    public class commentsAdapter extends AutoRVAdapter {

        public commentsAdapter(Context context, List<Say.CommentsBean> list) {
            super(context, list);
        }

        @Override
        public int onCreateViewHolder(int viewType) {
            return R.layout.item_saycomments;
        }

        @Override
        public void onBindViewHolder(com.gaop.huthelper.view.adapter.ViewHolder holder, int position) {
            final Say.CommentsBean bean = (Say.CommentsBean) list.get(position);
            if (bean == null) {
                return;
            } else {
                if (bean.getUsername() == null)
                    bean.setUsername("null");
            }
            int namelength = bean.getUsername() == null ? 0 : bean.getUsername().length();

            SpannableString spanString = new SpannableString(bean.getUsername() + "：" + bean.getComment());

            spanString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View arg0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id", bean.getUser_id());
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(context, MySayListActivity.class);
                    context.startActivity(intent);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            }, 0, namelength + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            spanString.setSpan(new ForegroundColorSpan(0xff1ddbcd), 0, namelength + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.getTextView(R.id.tv_comment_content).setText(spanString);
            holder.getTextView(R.id.tv_comment_content).setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public class ScrollLiearLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public ScrollLiearLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            return isScrollEnabled && super.canScrollVertically();
        }
    }
}
