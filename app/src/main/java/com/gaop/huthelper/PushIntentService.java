package com.gaop.huthelper;


import android.content.Context;
import android.content.Intent;

import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelperdao.Notice;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.common.AgooConstants;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 高沛 on 16-9-14.
 * 友盟推送Service
 */
public class PushIntentService extends UmengMessageService {

    private static final String TAG = PushIntentService.class.getName();

    @Override
    public void onMessage(Context context, Intent intent) {
        try {
            //可以通过MESSAGE_BODY取得消息体
            String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            Notice notice=new Notice();
            notice.setContent(msg.text);
            notice.setTitle(msg.title);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
            notice.setTime(formatter.format(new Date()));
            DBHelper.insertNotice(notice);

            // 对完全自定义消息的处理方式，点击或者忽略
            boolean isClickOrDismissed = true;
            if (isClickOrDismissed) {
                //完全自定义消息的点击统计
                UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
            } else {
                //完全自定义消息的忽略统计
                UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
            }
//
//            // 使用完全自定义消息来开启应用服务进程的示例代码
//            // 首先需要设置完全自定义消息处理方式
//            // mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
//            // code to handle to start/stop service for app process
//            JSONObject json = new JSONObject(msg.custom);
//            String topic = json.getString("topic");
//            UmLog.d(TAG, "topic=" + topic);
//            if (topic != null && topic.equals("appName:startService")) {
//                // 在友盟portal上新建自定义消息，自定义消息文本如下
//                //{"topic":"appName:startService"}
//                if (Helper.isServiceRunning(context, NotificationService.class.getName()))
//                    return;
//                Intent intent1 = new Intent();
//                intent1.setClass(context, NotificationService.class);
//                context.startService(intent1);
//            } else if (topic != null && topic.equals("appName:stopService")) {
//                // 在友盟portal上新建自定义消息，自定义消息文本如下
//                //{"topic":"appName:stopService"}
//                if (!Helper.isServiceRunning(context,NotificationService.class.getName()))
//                    return;
//                Intent intent1 = new Intent();
//                intent1.setClass(context, NotificationService.class);
//                context.stopService(intent1);
//            }
        } catch (Exception e) {
           // UmLog.e(TAG, e.getMessage());
        }
    }
}
