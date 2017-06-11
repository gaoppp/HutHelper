package com.gaop.huthelper.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 高沛 on 16-9-11.
 * 查看图片ViewPager 对一些可能出现的错误进行了处理
 */
public class ShowImageViewPager extends ViewPager {


        public ShowImageViewPager(Context context) {
            super(context);
        }

        public ShowImageViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }


        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            try {
                return super.onTouchEvent(ev);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
            return false;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }



