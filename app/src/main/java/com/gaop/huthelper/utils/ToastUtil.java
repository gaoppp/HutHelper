package com.gaop.huthelper.utils;

import android.content.Context;
import android.widget.Toast;

import com.gaop.huthelper.MApplication;

/**
 * Created by gaop1 on 2016/7/25.
 */
public class ToastUtil {

    private static Context context= MApplication.AppContext;

    public static void showToastLong(String msg){
        showToast(context,msg,Toast.LENGTH_LONG);
    }

    public static void showToastLong(int msg){
        showToast(context,""+msg,Toast.LENGTH_LONG);
    }

    public static void showToastShort(String msg){
        showToast(context,msg,Toast.LENGTH_SHORT);
    }

    public static void showToastShort(int msg){
        showToast(context,""+msg,Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context,String msg,int durstion){
        Toast.makeText(context,msg,durstion).show();
    }
}
