package com.gaop.huthelper.view;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import rx.functions.Action1;


/**
 * Created by 高沛 on 2016/12/23.
 * 下载Service
 */

public class DownloadService extends Service {

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
        url = intent.getStringExtra("url");
        if (TextUtils.isEmpty(url)) {
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
                                startDownload(url);
                            } else {
                                // 请求失败回收当前服务
                                stopSelf();

                            }
                        }
                    });
        }
        return Service.START_STICKY;
    }

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
