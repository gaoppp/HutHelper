package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop1 on 2016/9/3.
 */
public class ShowImgActivity extends BaseActivity implements GestureDetector.OnGestureListener {

    @BindView(R.id.iv_showImg)
    ImageView ivShowImg;
    @BindView(R.id.fraglayout_showImg)
    FrameLayout fraglayoutShowImg;

    private GestureDetector detector;

    private List<String> urls;
    private int curr;
    private final static int VERTICALMINISTANCE = 200; //表示向左滑动的最小距离
    private final static int MINVELOCITY = 10;  //表示向左滑动的最小的加速度

    @Override
    public void initParms(Bundle parms) {

        urls = parms.getStringArrayList("urls");
        curr = parms.getInt("curr");

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_showimg;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        setAllowFullScreen(true);
        detector = new GestureDetector(this);
        Picasso.with(ShowImgActivity.this).load(HttpMethods.BASE_URL + urls.get(curr))
                .into(ivShowImg);
//        ivShowImg.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.detector.onTouchEvent(event);
    }

    private void right() {
        if (curr - 1 >= 0) {
            --curr;
            startAnimation();
            Picasso.with(ShowImgActivity.this).load(HttpMethods.BASE_URL + urls.get(curr))
                    .into(ivShowImg);
        } else {
            ToastUtil.showToastShort("已经是第一张");
        }
    }

    private void left() {
        if (curr < urls.size()-1) {
            ++curr;
            startAnimation();
            Picasso.with(ShowImgActivity.this).load(HttpMethods.BASE_URL + urls.get(curr))
                    .into(ivShowImg);
        } else {
            ToastUtil.showToastShort("已经是最后一张");
        }
    }

//    @OnClick({R.id.iv_showImg, R.id.fraglayout_showImg})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.iv_showImg:
//                break;
//            case R.id.fraglayout_showImg:
//                finish();
//                break;
//        }
//    }


//    @OnClick(R.id.iv_showImg)
//    public void onClick() {
//       // finish();
//    }

    /**
     * 滑动页面动画
     */
    private void startAnimation() {
        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.4f);
        alphaAnim.setDuration(400);
        alphaAnim.setInterpolator(new LinearInterpolator());
        alphaAnim.setFillAfter(false);
        ivShowImg.startAnimation(alphaAnim);

        AlphaAnimation alphaAnim1 = new AlphaAnimation(0.4f, 1.0f);
        alphaAnim1.setDuration(400);
        alphaAnim1.setInterpolator(new LinearInterpolator());
        alphaAnim1.setFillAfter(false);
        ivShowImg.startAnimation(alphaAnim);

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
       /*// 根据velocityX（x方向上的加速度），velocityY（y方向上的加速度）
        // 根据两个速度的大小比较可以判断出手势是左右侧滑还是上下滑动
		if (Math.abs(velocityX) > Math.abs(velocityY) ) {
			//表示
		}*/

        //大于设定的最小滑动距离并且在水平/竖直方向速度绝对值大于设定的最小速度，则执行相应方法（表示向左滑动）
        if (e1.getX() - e2.getX() > VERTICALMINISTANCE && Math.abs(velocityX) > MINVELOCITY) {
            //左滑一系列的操作
            left();
        }

        //表示右划
        if (e2.getX() - e1.getX() > VERTICALMINISTANCE && Math.abs(velocityX) > MINVELOCITY) {
            //右滑一系列的操作
            right();
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
