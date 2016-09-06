package com.gaop.huthelper;

import android.app.Application;
import android.content.Context;

import com.gaop.huthelper.DB.DaoManager;
import com.gaop.huthelperdao.DaoMaster;
import com.gaop.huthelperdao.DaoSession;

/**
 * Created by gaop1 on 2016/7/23.
 */
public class MApplication extends Application {
    public static final String APP_NAME = "gdzs";
    public static boolean isDebug=true;
    public static Context AppContext;
    public static DaoMaster daoMaster;
    public static DaoSession daoSession;
    public static DaoManager daoManager;

    @Override
    public void onCreate() {
        AppContext=getApplicationContext();
        daoManager = DaoManager.getInstance(getApplicationContext());
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
