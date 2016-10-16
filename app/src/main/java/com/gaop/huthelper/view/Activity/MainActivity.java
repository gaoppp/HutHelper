package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.Model.UpdateMsg;
import com.gaop.huthelper.R;
import com.gaop.huthelper.jiekou.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelper.view.lib.DragLayout;
import com.gaop.huthelperdao.Notice;
import com.gaop.huthelperdao.User;
import com.umeng.common.inter.ITagManager;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.tag.TagManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by gaop1 on 2016/8/2.
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.iv_mainbg)
    ImageView ivMainbg;
    @BindView(R.id.tv_course_maincontent)
    TextView tvCourseMaincontent;
    @BindView(R.id.tv_date_maincontent)
    TextView tvDateMaincontent;

    @BindView(R.id.imgbtn_menusetting)
    ImageButton imgbtnMenusetting;
    @BindView(R.id.rl_bg_maincontent)
    RelativeLayout rlBgMaincontent;
    @BindView(R.id.imgbtn_notice_maincontent)
    TextView imgbtnNoticeMaincontent;
    @BindView(R.id.imgbtn_course_maincontent)
    ImageButton imgbtnCourseMaincontent;
    @BindView(R.id.imgbtn_book_maincontent)
    ImageButton imgbtnBookMaincontent;
    @BindView(R.id.imgbtn_score_maincontent)
    ImageButton imgbtnScoreMaincontent;
    @BindView(R.id.imgbtn_class_maincontent)
    ImageButton imgbtnClassMaincontent;
    @BindView(R.id.imgbtn_date_maincontent)
    ImageButton imgbtnDateMaincontent;
    @BindView(R.id.imgbtn_time_maincontent)
    ImageButton imgbtnTimeMaincontent;
    @BindView(R.id.imgbtn_electric_maincontent)
    ImageButton imgbtnElectricMaincontent;
    @BindView(R.id.imgbtn_public_maincontent)
    ImageButton imgbtnPublicMaincontent;
    @BindView(R.id.tv_nav_name)
    TextView tvNavName;
    @BindView(R.id.tv_tongzhi_contont)
    TextView tvTZcontent;
    @BindView(R.id.tv_tongzhi_title)
    TextView tvTZtitle;

    @BindView(R.id.tv_tongzhi_time)
    TextView getTvTZtime;
    @BindView(R.id.iv_nav_set)
    ImageView ivNavSet;
    @BindView(R.id.drawer_layout)
    DragLayout mDragLayout;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        User user = DBHelper.getUserDao().get(0);
        tvNavName.setText(user.getTrueName());
    }

    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        mDragLayout = (DragLayout) findViewById(R.id.drawer_layout);
        mDragLayout.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onDrag(float percent) {

            }
        });


        Observable.create(new Observable.OnSubscribe<String[]>() {
            @Override
            public void call(Subscriber<? super String[]> subscriber) {
                String[] next = new String[3];
                next[0] = CommUtil.getNextClass(MainActivity.this);
                next[1] = CommUtil.getData();
                User user = DBHelper.getUserDao().get(0);
                if (user != null)
                    next[2] = user.getTrueName();
                else
                    next[2] = "助手";
                subscriber.onNext(next);

                if (user != null) {
                    PushAgent mPushAgent = PushAgent.getInstance(MainActivity.this);
                    mPushAgent.getTagManager().add(new TagManager.TCallBack() {
                        @Override
                        public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                            //  Log.e(TAG, "onMessage11: "+isSuccess );
                        }
                    }, user.getDep_name(), user.getClass_name());

                    mPushAgent.addAlias(user.getStudentKH(), "学号", new UTrack.ICallBack() {
                        @Override
                        public void onMessage(boolean isSuccess, String message) {
                            // Log.e(TAG, "onMessage: "+isSuccess+message );
                        }
                    });
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String[]>() {
                    @Override
                    public void call(String data[]) {
                        List<Notice> notices = DBHelper.getNoticeDao();
                        if (notices.size() != 0) {
                            final Notice notice = notices.get(0);
                            tvTZtitle.setText(notice.getTitle());
                            getTvTZtime.setText(notice.getTime());
                            tvTZcontent.setText(notice.getContent());
                            tvTZcontent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("notice", notice);
                                    startActivity(NoticeItemActivity.class, bundle);
                                }
                            });
                        } else {
                            tvTZcontent.setText("暂时没有通知");
                        }
                        tvCourseMaincontent.setText(data[0]);
                        tvDateMaincontent.setText(data[1]);
                        tvNavName.setText(data[2]);
                    }
                });
        checkUpdate(false);
    }


    @OnClick({R.id.tv_nav_name, R.id.rl_nav_update, R.id.rl_nav_manage, R.id.rl_nav_share, R.id.rl_nav_fback, R.id.rl_nav_logout,
            R.id.imgbtn_notice_maincontent, R.id.imgbtn_course_maincontent, R.id.imgbtn_book_maincontent, R.id.imgbtn_score_maincontent,
            R.id.imgbtn_class_maincontent, R.id.imgbtn_shiyan_maincontent, R.id.imgbtn_time_maincontent, R.id.imgbtn_kaoshi_maincontent, R.id.imgbtn_public_maincontent, R.id.imgbtn_menusetting, R.id.iv_nav_set, R.id.imgbtn_ceshi_maincontent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_menusetting:
                mDragLayout.open();
                break;
            case R.id.imgbtn_notice_maincontent:
                startActivity(NoticeActivity.class);
                break;
            case R.id.imgbtn_course_maincontent:
                startActivity(CourseTableActivity.class);
                break;
            case R.id.imgbtn_book_maincontent:
                Bundle bundle = new Bundle();
                bundle.putInt("type", WebViewActivity.TYPE_LIB);
                startActivity(WebViewActivity.class, bundle);
                break;
            case R.id.imgbtn_score_maincontent:
                startActivity(GradeActivity.class);
                break;
            case R.id.imgbtn_class_maincontent:
                startActivity(SayActivity.class);
                break;
            case R.id.imgbtn_shiyan_maincontent:
                startActivity(ExpLessonActivity.class);
                break;
            case R.id.imgbtn_time_maincontent:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("type", WebViewActivity.TYPE_EXAM);
                startActivity(WebViewActivity.class, bundle1);
                break;
            case R.id.imgbtn_kaoshi_maincontent:
                startActivity(ExamActivity.class);
                break;
            case R.id.imgbtn_public_maincontent:
                startActivity(MarketActivity.class);
                break;
            case R.id.iv_nav_set:
                startActivity(UserActivity.class);
                break;
            case R.id.rl_nav_update:
                checkUpdate(true);
                break;
            case R.id.rl_nav_manage:
                startActivity(AboutActivity.class);
                break;
            case R.id.rl_nav_share:
                share();
                break;
            case R.id.rl_nav_fback:
                startActivity(FeedBackActivity.class);
                break;
            case R.id.rl_nav_logout:
                startActivity(ImportActivity.class);
                break;
            case R.id.imgbtn_ceshi_maincontent:
                startActivity(ExamActivity.class);
                //ToastUtil.showToastShort("更多内容正在开发中");
                break;

        }
    }

    private void checkUpdate(final boolean showe) {
        SubscriberOnNextListener getUpdateData = new SubscriberOnNextListener<HttpResult<UpdateMsg>>() {
            @Override
            public void onNext(final HttpResult<UpdateMsg> o) {
                if (o.getMsg().equals("ok")) {
                    try {
                        int versionCode = MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), 0).versionCode;
                        if (o.getData().getVersionNum() > versionCode) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("有新版本");
                            builder.setMessage(o.getData().getData());
                            builder.setPositiveButton("下载",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            Intent intent = new Intent();
                                            intent.setAction("android.intent.action.VIEW");
                                            Uri content_url = Uri.parse(o.getData().getUrl());
                                            intent.setData(content_url);
                                            startActivity(intent);
                                        }
                                    });
                            builder.setNegativeButton("忽略",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                        }
                                    });
                            builder.show();
                        } else {
                            if (showe)
                                ToastUtil.showToastShort("已经是最新版本了");
                        }

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (showe)
                        ToastUtil.showToastShort(o.getMsg());
                }
            }
        };
        HttpMethods.getInstance().checkUpdate(
                new ProgressSubscriber<HttpResult<UpdateMsg>>
                        (getUpdateData, MainActivity.this));

    }

    private void share() {
        SubscriberOnNextListener getUpdateData = new SubscriberOnNextListener<HttpResult<UpdateMsg>>() {
            @Override
            public void onNext(HttpResult<UpdateMsg> o) {
                if (o.getMsg().equals("ok")) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                    intent.putExtra(Intent.EXTRA_TEXT, "工大助手下载链接：" + o.getData().getUrl());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(intent, "分享"));
                } else {
                    ToastUtil.showToastShort("获取下载链接失败" + o.getMsg());
                }
            }
        };
        HttpMethods.getInstance().checkUpdate(
                new ProgressSubscriber<HttpResult<UpdateMsg>>
                        (getUpdateData, MainActivity.this));
    }
}
