package com.gaop.huthelper.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;

/**
 * Created by gaop on 16-9-12.
 */
public class MyChooseButton extends View implements DialogInterface.OnClickListener {

    private boolean isCheck;


    public MyChooseButton(Context context) {
        super(context);
    }

    public MyChooseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyChooseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
