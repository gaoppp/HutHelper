package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.model.entity.UpdateMsg;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.model.rxbus.RxBus;
import com.gaop.huthelper.model.rxbus.event.MainEvent;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.DensityUtils;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelper.view.CircleImageView;
import com.gaop.huthelper.view.CustomGirdLayoutManager;
import com.gaop.huthelper.view.DownloadService;
import com.gaop.huthelper.view.adapter.MenuRVAdapter;
import com.gaop.huthelper.view.lib.DragLayout;
import com.gaop.huthelperdao.Menu;
import com.gaop.huthelperdao.Notice;
import com.gaop.huthelperdao.User;
import com.squareup.picasso.Picasso;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by 高沛 on 2016/8/2.
 * this ischange
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.tv_course_maincontent)
    TextView tvCourseMaincontent;
    @BindView(R.id.tv_date_maincontent)
    TextView tvDateMaincontent;

    @BindView(R.id.tv_nav_name)
    TextView tvNavName;
    @BindView(R.id.tv_tongzhi_contont)
    TextView tvTZcontent;
    @BindView(R.id.tv_tongzhi_title)
    TextView tvTZtitle;
    @BindView(R.id.tv_tongzhi_time)
    TextView getTvTZtime;
    @BindView(R.id.drawer_layout)
    DragLayout mDragLayout;
    @BindView(R.id.rl_main_tongzhi)
    RelativeLayout rlNextNotice;

    @BindView(R.id.iv_nav_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.rv_main_menu)
    RecyclerView rvMainMenu;

    MenuRVAdapter adapter;
    private List<Menu> menuItems = new ArrayList<>();

    private long exitTime = 0;
    private Subscription subscription;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        User user = DBHelper.getUserDao().get(0);
        int width = DensityUtils.dp2px(this, 75);
        if(!TextUtils.isEmpty(user.getHead_pic_thumb())){
            Picasso.with(this).load(HttpMethods.BASE_URL + user.getHead_pic_thumb()).resize(width, width).into(ivAvatar);
        }
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
                String[] next = new String[4];
                next[0] = CommUtil.getNextClass(MainActivity.this);
                next[1] = CommUtil.getData();
                User user = DBHelper.getUserDao().get(0);
                if (user != null) {
                    next[2] = user.getTrueName();
                    next[3] = user.getHead_pic_thumb();
                } else {
                    next[2] = "助手";
                    next[3] = "";
                }
                subscriber.onNext(next);

                if (user != null) {
                    PushAgent mPushAgent = PushAgent.getInstance(MainActivity.this);
//                    mPushAgent.getTagManager().add(new TagManager.TCallBack() {
//                        @Override
//                        public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
//
//                        }
//                    }, user.getDep_name(), user.getClass_name());
                    mPushAgent.addAlias(user.getStudentKH(), "学号", new UTrack.ICallBack() {
                        @Override
                        public void onMessage(boolean isSuccess, String message) {
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
                            final Notice notice = notices.get((notices.size() - 1));
                            tvTZtitle.setText(notice.getTitle());
                            getTvTZtime.setText(notice.getTime());
                            tvTZcontent.setText(notice.getContent());
                            rlNextNotice.setOnClickListener(new View.OnClickListener() {
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
                        if (!TextUtils.isEmpty(data[3])) {
                            int width = DensityUtils.dp2px(MainActivity.this, 75);
                            Picasso.with(MainActivity.this).load(HttpMethods.BASE_URL + data[3]).resize(width, width).into(ivAvatar);
                        }

                    }
                });


        subscription = RxBus.getInstance().toObservable(MainEvent.class)
                .subscribe(new Action1<MainEvent>() {
                    @Override
                    public void call(MainEvent event) {
                        if (event.getId() == 1) {
                            initMenu();     //更新菜单
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // TODO: 处理异常
                    }
                });
        initMenu();
        checkUpdate(false);
    }

    private void initMenu() {
        Observable.create(new Observable.OnSubscribe<List<Menu>>() {
            @Override
            public void call(Subscriber<? super List<Menu>> subscriber) {
                if (DBHelper.getMenu().size() == 0) {
                    initMenuList();
                }
                List<Menu> list = DBHelper.getMenuInMainSortByIndex();
                if (list == null)
                    list = new ArrayList<Menu>();
                Menu item = new Menu("com.gaop.huthelper.view.activity.AllActivity", "全部", 13, 100, true, 0);
                list.add(item);
                subscriber.onNext(list);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Menu>>() {
                    @Override
                    public void call(final List<Menu> s) {
//                        menuItems = s;
//                        adapter.notifyDataSetChanged();
                        MenuRVAdapter adapter = new MenuRVAdapter(MainActivity.this, s);
                        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Menu item = s.get(position);
                                try {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("type", item.getMsg());
                                    startActivity(Class.forName(item.getPath()), bundle);
                                } catch (ClassNotFoundException e) {
                                    ToastUtil.showToastShort("找不到该页面~");
                                    e.printStackTrace();
                                }
                            }
                        });
                        rvMainMenu.setLayoutManager(new CustomGirdLayoutManager(MainActivity.this, 4));
                        rvMainMenu.setAdapter(adapter);
                    }
                });
    }


    @OnClick({R.id.tv_course_maincontent, R.id.tv_nav_name, R.id.iv_nav_avatar, R.id.tv_nav_update, R.id.tv_nav_manage, R.id.tv_nav_share, R.id.tv_nav_fback, R.id.tv_nav_logout,
            R.id.tv_notice_maincontent, R.id.imgbtn_menusetting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_course_maincontent:
                tvCourseMaincontent.setText(CommUtil.getNextClass(MainActivity.this));
                break;
            case R.id.imgbtn_menusetting:
                mDragLayout.open();
                break;
            case R.id.tv_notice_maincontent:
                startActivity(NoticeActivity.class);
                break;
            case R.id.tv_nav_name:
                startActivity(UserActivity.class);
                break;
            case R.id.iv_nav_avatar:
                startActivity(UserActivity.class);
                break;
            case R.id.tv_nav_update:
                checkUpdate(true);
                break;
            case R.id.tv_nav_manage:
                startActivity(AboutActivity.class);
                break;
            case R.id.tv_nav_share:
                share();
                break;
            case R.id.tv_nav_fback:
                startActivity(FeedBackActivity.class);
               // startActivity(NewGradeActivity.class);
                break;
            case R.id.tv_nav_logout:
                startActivity(ImportActivity.class);
                break;
        }
    }

    private void checkUpdate(final boolean showe) {
        try {
            User user = DBHelper.getUserDao().get(0);
            final int versionCode = MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), 0).versionCode;
            SubscriberOnNextListener getUpdateData = new SubscriberOnNextListener<HttpResult<UpdateMsg>>() {
                @Override
                public void onNext(final HttpResult<UpdateMsg> o) {
                    if (o.getMsg().equals("ok")) {
                        PrefUtil.setString(getApplication(), "library", "http://" + o.getData().getApi_base_address().getLibrary());
                        PrefUtil.setString(getApplication(), "test_plan", "http://" + o.getData().getApi_base_address().getTest_plan());
                        if (o.getData().getVersionNum() > versionCode) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("有新版本");
                            builder.setMessage(o.getData().getData());
                            builder.setPositiveButton("下载",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            Intent dIntent = new Intent(MainActivity.this, DownloadService.class);
                                            dIntent.putExtra("url", o.getData().getUrl());
                                            startService(dIntent);
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

                    } else {
                        if (showe)
                            ToastUtil.showToastShort(o.getMsg());
                    }
                }
            };
            HttpMethods.getInstance().checkUpdate(
                    new ProgressSubscriber<HttpResult<UpdateMsg>>
                            (getUpdateData, MainActivity.this), user.getStudentKH(), versionCode + "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void share() {
        int versionCode = 0;
        try {
            User user = DBHelper.getUserDao().get(0);
            versionCode = MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), 0).versionCode;

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
                            (getUpdateData, MainActivity.this), user.getStudentKH(), versionCode + "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 判断间隔时间 大于2秒就退出应用
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                String msg = "再按一次返回键退出";
                ToastUtil.showToastShort(msg);
                // 计算两次返回键按下的时间差
                exitTime = System.currentTimeMillis();
            } else {
                // 关闭应用程序
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private void initMenuList() {
        List<Menu> menuItems = new ArrayList<>();
        Menu item = new Menu("com.gaop.huthelper.view.activity.WebViewActivity", "图书馆", 0, 0, true, 1);
        menuItems.add(item);
        item = new Menu("com.gaop.huthelper.view.activity.CourseTableActivity", "课程表", 1, 1, true, 0);
        menuItems.add(item);
        item = new Menu("com.gaop.huthelper.view.activity.ExamActivity", "考试查询", 2, 2, true, 0);
        menuItems.add(item);
        item = new Menu("com.gaop.huthelper.view.activity.NewGradeActivity", "成绩查询", 3, 3, true, 0);
        menuItems.add(item);
        item = new Menu("com.gaop.huthelper.view.activity.WebViewActivity", "网上作业", 4, 4, true, 2);
        menuItems.add(item);
        item = new Menu("com.gaop.huthelper.view.activity.MarketActivity", "二手市场", 5, 5, true, 0);
        menuItems.add(item);
        item = new Menu("com.gaop.huthelper.view.activity.SayActivity", "校园说说", 6, 6, true, 0);
        menuItems.add(item);
        item = new Menu("com.gaop.huthelper.view.activity.ElecticActivity", "电费查询", 7, 7, false, 0);
        menuItems.add(item);
        item = new Menu("com.gaop.huthelper.view.activity.OfferActivity", "校招薪水", 8, 8, false, 0);
        menuItems.add(item);
        item = new Menu("com.gaop.huthelper.view.activity.ExpLessonActivity", "实验课表", 9, 9, true, 0);
        menuItems.add(item);
        item = new Menu("com.gaop.huthelper.view.activity.CalendarActivity", "校历", 10, 10, false, 0);
        menuItems.add(item);
        item = new Menu("com.gaop.huthelper.view.activity.LoseListActivity", "失物招领", 11, 11, true, 0);
        menuItems.add(item);
        item = new Menu("com.gaop.huthelper.view.activity.CareerTalkActivity", "宣讲会", 14, 14, true, 0);
        menuItems.add(item);
        item = new Menu("com.gaop.huthelper.view.activity.VideoListActivity", "视频专栏", 12, 12, true, 0);
        menuItems.add(item);
        DBHelper.insertListMenu(menuItems);
    }

}
