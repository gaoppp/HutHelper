package com.gaop.huthelper.view;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.DateUtil;
import com.gaop.huthelperdao.Lesson;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    RemoteViews views;
    static int xqj=DateUtil.getWeekOfToday();//判断星期几
    public static boolean yema=true;

    private final List<Lesson> list= DBHelper.getLessonDao();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context,NewAppWidget.class);
            Intent intent1=new Intent(context,NewAppWidget.class);
            Intent intentUp=new Intent(context,NewAppWidget.class);
            Intent intentDown=new Intent(context,NewAppWidget.class);
            intent.setAction("next");
            intent1.setAction("before");
            intentUp.setAction("up");
            intentDown.setAction("down");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            PendingIntent pendingIntent1=PendingIntent.getBroadcast(context,0,intent1,0);
            PendingIntent pendingIntentUp=PendingIntent.getBroadcast(context,0,intentUp,0);
            PendingIntent pendingIntentDown=PendingIntent.getBroadcast(context,0,intentDown,0);
            views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views.setTextViewText(R.id.mode1_xqj,"星期一");
            views=chushi(views);
            views.setOnClickPendingIntent(R.id.model_xqj_next, pendingIntent);
            views.setOnClickPendingIntent(R.id.mode1_xqj_before,pendingIntent1);
            views.setOnClickPendingIntent(R.id.model_up,pendingIntentUp);
            views.setOnClickPendingIntent(R.id.model_down,pendingIntentDown);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        String xx="error";
        switch (intent.getAction()){
            case "next":
                xx=whatToday(true,1);
                yema=false;
                views=fanYe(views);
                break;
            case "before":
                xx=whatToday(false,1);
                yema=false;
                views=fanYe(views);
                break;
            case "up":
                views=fanYe(views);
                views=chushi(views);
                xx=whatToday(false,0);
                break;
            case "down":
                views=fanYe(views);
                views=chushi(views);
                xx=whatToday(false,0);
                break;

        }


            views.setTextViewText(R.id.mode1_xqj,xx);
            views=chushi(views);
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            //相当于获得所有本程序创建的appwidget
            ComponentName componentName = new ComponentName(context,NewAppWidget.class);
            //更新appwidget
            appWidgetManager.updateAppWidget(componentName, views);



        super.onReceive(context, intent);
    }



    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created


    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

//初始化整个课表的显示 显示出正确的课
    //x 就是views
    public RemoteViews chushi(RemoteViews x) {
        int i;
        Lesson l;

        x.setTextViewText(R.id.model_12_name, "没课！！！");
        x.setTextViewText(R.id.model_12_place, "   ");
        x.setTextViewText(R.id.model_34_name, "没课！！！");
        x.setTextViewText(R.id.model_34_place, "   ");
        x.setTextViewText(R.id.model_56_name, "没课！！！");
        x.setTextViewText(R.id.model_56_place, "   ");


        for (i = 0; i < list.size(); i++) {

            l = list.get(i);
            boolean isHave= CommUtil.ifHaveCourse(l, DateUtil.getNowWeek()) ;
            if (l.getXqj() == xqj && yema&&isHave) {
                switch (l.getDjj()) {
                    case 1:
                        x.setTextViewText(R.id.model_12_name, l.getName());
                        x.setTextViewText(R.id.model_12_place, l.getRoom());
                        break;
                    case 3:
                        x.setTextViewText(R.id.model_34_name, l.getName());
                        x.setTextViewText(R.id.model_34_place, l.getRoom());
                        break;
                    case 5:
                        x.setTextViewText(R.id.model_56_name, l.getName());
                        x.setTextViewText(R.id.model_56_place, l.getRoom());
                        break;
                }
            } else if (l.getXqj() == xqj && !yema&&isHave) {
                switch (l.getDjj()) {
                    case 7:
                        x.setTextViewText(R.id.model_12_name, l.getName());
                        x.setTextViewText(R.id.model_12_place, l.getRoom());
                        break;
                    case 9:
                        x.setTextViewText(R.id.model_34_name, l.getName());
                        x.setTextViewText(R.id.model_34_place, l.getRoom());
                        break;
                    default:
                        x.setTextViewText(R.id.model_56_name, "   ");
                        x.setTextViewText(R.id.model_56_place, "   ");
                }

            }
        }
            return x;
        }

    //判断当前显示应该是星期几，
    //x 加减 判定符  ss 是否触发自动加减一的设定。
    public String whatToday(boolean x,int ss){
        String xx="erorr";
        if(x){
            if (ss==1){
                xqj++;
                if (xqj>7)xqj=xqj-7;
            }

            switch (xqj){
                case 1:
                    xx="星期一";
                    break;
                case 2:
                    xx="星期二";
                    break;
                case 3:
                    xx="星期三";
                    break;
                case 4:
                    xx="星期四";
                    break;
                case 5:
                    xx="星期五";
                    break;
                case 6:
                    xx="星期六";
                    break;
                case 7:
                    xx="星期天";
                    break;
            }
        }else {
            if (ss==1){
                xqj--;
                if (xqj<=0)xqj=7;
            }

            switch (xqj){
                case 1:
                    xx="星期一";
                    break;
                case 2:
                    xx="星期二";
                    break;
                case 3:
                    xx="星期三";
                    break;
                case 4:
                    xx="星期四";
                    break;
                case 5:
                    xx="星期五";
                    break;
                case 6:
                    xx="星期六";
                    break;
                case 7:
                    xx="星期天";
                    break;
            }
        }

        return xx;
    }


  //就是翻页  把上三节置换成后三节。
    public RemoteViews fanYe(RemoteViews x){
        if (yema){
            x.setTextViewText(R.id.model_12,"7-8");
            x.setTextViewText(R.id.model_34,"9-10");
            x.setTextViewText(R.id.model_56,"  ");
            yema=false;
        }else {
            x.setTextViewText(R.id.model_12,"1-2");
            x.setTextViewText(R.id.model_34,"3-4");
            x.setTextViewText(R.id.model_56,"5-6");
            yema=true;
        }

        return x;

    }



}

