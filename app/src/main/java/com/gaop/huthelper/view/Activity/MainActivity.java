package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.Model.Say;
import com.gaop.huthelper.Model.UpdateMsg;
import com.gaop.huthelper.R;
import com.gaop.huthelper.jiekou.SayListAPI;
import com.gaop.huthelper.jiekou.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.User;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
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

    @BindView(R.id.rl_bg_maincontent)
    RelativeLayout rlBgMaincontent;
    @BindView(R.id.imgbtn_notice_maincontent)
    ImageButton imgbtnNoticeMaincontent;
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
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;
    @BindView(R.id.tv_nav_name)
    TextView tvNavName;
    @BindView(R.id.tv_nav_update)
    TextView tvNavUpdate;
    @BindView(R.id.tv_nav_manage)
    TextView tvNavManage;
    @BindView(R.id.tv_nav_share)
    TextView tvNavShare;
    @BindView(R.id.tv_nav_fback)
    TextView tvNavFback;
    @BindView(R.id.tv_nav_logout)
    TextView tvNavLogout;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

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
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.hideOverflowMenu();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


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
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String[]>() {
                    @Override
                    public void call(String data[]) {
                        tvCourseMaincontent.setText(data[0]);
                        tvDateMaincontent.setText(data[1]);
                        tvNavName.setText(data[2]);
                    }
                });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @OnClick({R.id.tv_nav_name, R.id.tv_nav_update, R.id.tv_nav_manage, R.id.tv_nav_share, R.id.tv_nav_fback, R.id.tv_nav_logout, R.id.imgbtn_notice_maincontent, R.id.imgbtn_course_maincontent, R.id.imgbtn_book_maincontent, R.id.imgbtn_score_maincontent, R.id.imgbtn_class_maincontent, R.id.imgbtn_date_maincontent, R.id.imgbtn_time_maincontent, R.id.imgbtn_electric_maincontent, R.id.imgbtn_public_maincontent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_notice_maincontent:

                break;
            case R.id.imgbtn_course_maincontent:
                startActivity(CourseTableActivity.class);
                break;
            case R.id.imgbtn_book_maincontent:

                break;
            case R.id.imgbtn_score_maincontent:
                startActivity(GradeActivity.class);
                break;
            case R.id.imgbtn_class_maincontent:
                startActivity(SayActivity.class);
                break;
            case R.id.imgbtn_date_maincontent:
                startActivity(CalendarActivity.class);
                break;
            case R.id.imgbtn_time_maincontent:
                ToastUtil.showToastShort("暂未推出");
                break;
            case R.id.imgbtn_electric_maincontent:
                startActivity(ElecticActivity.class);
                break;
            case R.id.imgbtn_public_maincontent:
                startActivity(MarketActivity.class);
                break;
            case R.id.tv_nav_name:
                startActivity(UserActivity.class);
                break;
            case R.id.tv_nav_update:
                checkUpdate();
                break;
            case R.id.tv_nav_manage:
                startActivity(AboutActivity.class);
                break;
            case R.id.tv_nav_share:
                share();
                break;
            case R.id.tv_nav_fback:
                startActivity(FeedBackActivity.class);
                break;
            case R.id.tv_nav_logout:
                startActivity(ImportActivity.class);
                break;
        }
    }

    private void checkUpdate() {
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
                            ToastUtil.showToastShort("已经是最新版本了");
                        }

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
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
