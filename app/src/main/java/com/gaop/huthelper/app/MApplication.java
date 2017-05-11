package com.gaop.huthelper.app;

import android.content.Context;
import android.text.TextUtils;

import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.db.DaoManager;
import com.gaop.huthelperdao.DaoMaster;
import com.gaop.huthelperdao.DaoSession;
import com.gaop.huthelperdao.Notice;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by 高沛 on 2016/7/23.
 */
public class MApplication extends TinkerApplication {
    public static final String APP_NAME = "gdzs";
    public static boolean isDebug = true;
    public static Context AppContext;
    public static DaoMaster daoMaster;
    public static DaoSession daoSession;
    public static DaoManager daoManager;

    public static final String TAG="Application";

    public MApplication() {
        //注册热修复
        super(ShareConstants.TINKER_ENABLE_ALL, "com.gaop.huthelper.app.MyApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();
        daoManager = DaoManager.getInstance(getApplicationContext());

        PushAgent mPushAgent = PushAgent.getInstance(this);
       //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token

            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        UmengMessageHandler messageHandler = new UmengMessageHandler(){
            @Override
            public void handleMessage(Context context, UMessage uMessage) {
                if("notification".equals(uMessage.display_type)) {
                    this.dealWithNotificationMessage(context, uMessage);

                    Notice notice=new Notice();
                    notice.setContent(uMessage.text);
                    notice.setTitle(uMessage.title);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                    notice.setTime(formatter.format(new Date()));
                    if(uMessage.extra!=null) {
                        for (Map.Entry<String, String> entry : uMessage.extra.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            if(!(TextUtils.isEmpty(key)||TextUtils.isEmpty(value))){
                            if (key.equals("type")) {
                                if (value.equals("url")) {
                                    notice.setType("url");
                                } else if (value.equals("html")) {
                                    notice.setType("html");
                                }
                            }}
                        }
                    }
                    DBHelper.insertNotice(notice);
                } else if("custom".equals(uMessage.display_type)) {
                    UTrack.getInstance(context).setClearPrevMessage(false);
                    this.dealWithCustomMessage(context, uMessage);
                }


            }

        };
        mPushAgent.setMessageHandler(messageHandler);
        //初始化Q5内核
        QbSdk.initX5Environment(getApplicationContext(),  null);
    }
    /**
     * 取得DaoMaster
     *
     * @param context
     * @return daoMaster
     */
    public static DaoMaster getDaoMaster(Context context) {
        DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, "person.db", null);
        daoMaster = new DaoMaster(helper.getWritableDatabase());
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @param context
     * @return daoSession
     */
    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
