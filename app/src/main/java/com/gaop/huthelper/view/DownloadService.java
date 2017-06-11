package com.gaop.huthelper.view;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.gaop.huthelper.app.MApplication;
import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.model.entity.UpdateMsg;
import com.gaop.huthelper.model.network.api.UpdateAPI;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by 高沛 on 2016/12/23.
 * 检查更新及下载更新Service
 */

public class DownloadService extends Service {

    public static final int TYPE_OPEN_UPDATE = 0;//打开时检查更新
    public static final int TYPE_CHECK_UPDATE = 1;//手动检查更新

    private BroadcastReceiver receiver;

    private DownloadManager dm;
    private long enqueue;
    private String url;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            stopSelf();
            return START_NOT_STICKY;
        }

        int updateType = intent.getIntExtra("type", 1);
        try {
            final int versionCode = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
            loadUpdateData(versionCode, updateType);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return Service.START_STICKY;
    }

    private void loadUpdateData(int code, int type) {
        final int versionCode = code;
        final int updateType = type;

        UpdateAPI updateAPI = HttpMethods.getRetrofit().create(UpdateAPI.class);
        updateAPI.getUpdateData(MApplication.getUser().getStudentKH(), versionCode + "")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<UpdateMsg>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort("检查更新出错，请检查网络！");
                    }

                    @Override
                    public void onNext(HttpResult<UpdateMsg> result) {
                        if (result.getMsg().equals("ok")) {
                            //保存最新的api接口
                            saveApiData(result.getData());
                            if (result.getData().getVersionNum() > versionCode) {
                                showUpdateDialog(result.getData());
                            } else {
                                if (updateType == TYPE_CHECK_UPDATE)
                                    ToastUtil.showToastShort("已经是最新版本了");
                            }
                        } else {
                            if (updateType == TYPE_CHECK_UPDATE)
                                ToastUtil.showToastShort(result.getMsg());
                        }
                    }
                });
    }

    private void saveApiData(UpdateMsg msg) {
        PrefUtil.setString(getApplication(), "library", "http://" + msg.getApi_base_address().getLibrary());
        PrefUtil.setString(getApplication(), "test_plan", "http://" + msg.getApi_base_address().getTest_plan());
    }

    private void showUpdateDialog(UpdateMsg msg) {
        final UpdateMsg updateMsg = msg;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("有新版本");
        builder.setMessage(updateMsg.getData());
        builder.setPositiveButton("下载",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dealHasUpdate(updateMsg.getUrl());
                    }
                });
        builder.setNegativeButton("忽略",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        stopSelf();
                    }
                });
        builder.show();
    }

    private void dealHasUpdate(String url) {
        final String updateUrl = url;
        if (TextUtils.isEmpty(updateUrl)) {
            stopSelf();
        } else {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    install(context);
                    stopSelf();
                }
            };
            registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            //下载需要写SD卡权限, targetSdkVersion>=23 需要动态申请权限
            RxPermissions.getInstance(this)
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean granted) {
                            if (granted) {
                                //请求成功
                                startDownload(updateUrl);
                            } else {
                                // 请求失败回收当前服务
                                stopSelf();
                            }
                        }
                    });
        }
    }

    /**
     * 安装apk
     *
     * @param context
     */
    public static void install(Context context) {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                , "HutHelper.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri =
                    FileProvider.getUriForFile(context, "com.gaop.huthelper.fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    /**
     * 开始下载
     *
     * @param url 下载链接
     */
    private void startDownload(String url) {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setMimeType("application/vnd.android.package-archive");
        //设置下载存放的文件夹和文件名字
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "HutHelper.apk");
        //设置下载时或者下载完成时，通知栏是否显示
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("工大助手更新");
        //执行下载，并返回任务唯一id
        enqueue = dm.enqueue(request);
    }
}
