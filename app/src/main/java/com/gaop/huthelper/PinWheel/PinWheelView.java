package com.gaop.huthelper.PinWheel;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.gaop.huthelper.R;


/**
 * Created by gaop1 on 2016/4/22.
 */
public class PinWheelView extends FrameLayout {
    private PinWheel mPinWheel;
    public PinWheelView(Context context) {
        super(context);
    }

    public PinWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PinWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View view= LayoutInflater.from(getContext()).inflate(R.layout.pinwheelview,null);
        LayoutParams layoutParams=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity= Gravity.CENTER;
        mPinWheel= (PinWheel) view.findViewById(R.id.pinwheel);
        addView(view,layoutParams);
        startAnim(200);

    }
    private void startAnim(long delay){
        if(delay>0){
            this.postDelayed(mAnimRunable,delay);
        }
    }
    private Runnable mAnimRunable = new Runnable() {
        @Override
        public void run() {
            rotate();
        }
    } ;
    private void rotate(){
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(mPinWheel,"rotation",0,-360);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setDuration(800);
        rotateAnim.setRepeatCount(-1);
        rotateAnim.start();
    }
}
