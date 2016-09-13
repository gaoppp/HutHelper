package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.view.ShowImageViewPager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by gaop1 on 2016/9/3.
 */
public class ShowImgActivity extends BaseActivity
       // implements GestureDetector.OnGestureListener
{

//    @BindView(R.id.iv_showImg)
//    ImageView ivShowImg;
    @BindView(R.id.fraglayout_showImg)
    RelativeLayout fraglayoutShowImg;
    @BindView(R.id.tv_imgviewnum)
    TextView tvImgviewnum;
    @BindView(R.id.toolar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;



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
        //setAllowFullScreen(true);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvImgviewnum.setText((curr+1)+"/"+urls.size());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(curr);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvImgviewnum.setText((position+1)+"/"+urls.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
//
//    private void loadImag(int curr){
//        Picasso.with(ShowImgActivity.this).load(HttpMethods.BASE_URL + urls.get(curr))
//                .into(ivShowImg, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        mAttacher.update();
//                    }
//
//                    @Override
//                    public void onError() {
//
//                    }
//                });
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return this.detector.onTouchEvent(event);
//    }

//    private void right() {
//        if (curr - 1 >= 0) {
//            --curr;
//            startAnimation();
//            tvImgviewnum.setText((curr+1)+"/"+urls.size());
//            loadImag(curr);
//        } else {
//            ToastUtil.showToastShort("已经是第一张");
//        }
//    }
//
//    private void left() {
//        if (curr < urls.size() - 1) {
//            ++curr;
//            startAnimation();
//            tvImgviewnum.setText((curr+1)+"/"+urls.size());
//            loadImag(curr);
//
//        } else {
//            ToastUtil.showToastShort("已经是最后一张");
//        }
//    }



//    /**
//     * 滑动页面动画
//     */
//    private void startAnimation() {
//        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.4f);
//        alphaAnim.setDuration(400);
//        alphaAnim.setInterpolator(new LinearInterpolator());
//        alphaAnim.setFillAfter(false);
//        ivShowImg.startAnimation(alphaAnim);
//
//        AlphaAnimation alphaAnim1 = new AlphaAnimation(0.4f, 1.0f);
//        alphaAnim1.setDuration(400);
//        alphaAnim1.setInterpolator(new LinearInterpolator());
//        alphaAnim1.setFillAfter(false);
//        ivShowImg.startAnimation(alphaAnim);
//
//    }

//    @Override
//    public boolean onDown(MotionEvent e) {
//        return false;
//    }
//
//    @Override
//    public void onShowPress(MotionEvent e) {
//
//    }
//
//    @Override
//    public boolean onSingleTapUp(MotionEvent e) {
//        return false;
//    }
//
//    @Override
//    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        return false;
//    }
//
//    @Override
//    public void onLongPress(MotionEvent e) {
//
//    }
//
//    @Override
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//       /*// 根据velocityX（x方向上的加速度），velocityY（y方向上的加速度）
//        // 根据两个速度的大小比较可以判断出手势是左右侧滑还是上下滑动
//		if (Math.abs(velocityX) > Math.abs(velocityY) ) {
//			//表示
//		}*/
//
//        //大于设定的最小滑动距离并且在水平/竖直方向速度绝对值大于设定的最小速度，则执行相应方法（表示向左滑动）
//        if (e1.getX() - e2.getX() > VERTICALMINISTANCE && Math.abs(velocityX) > MINVELOCITY) {
//            //左滑一系列的操作
//            left();
//        }
//
//        //表示右划
//        if (e2.getX() - e1.getX() > VERTICALMINISTANCE && Math.abs(velocityX) > MINVELOCITY) {
//            //右滑一系列的操作
//            right();
//        }
//        return false;
//    }


   PagerAdapter pagerAdapter=new PagerAdapter() {
       @Override
       public int getCount() {
           return urls.size();
       }

       @Override
       public boolean isViewFromObject(View view, Object object) {
           return view==object;
       }

       @Override
       public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((View) object);
       }

       @Override
       public Object instantiateItem(ViewGroup container, int position) {
           ImageView view=new ImageView(ShowImgActivity.this);
           final PhotoViewAttacher attacher=new PhotoViewAttacher(view);
           Picasso.with(ShowImgActivity.this).load(HttpMethods.BASE_URL + urls.get(position))
                   .placeholder(R.drawable.img_loading).error(R.drawable.img_error)
                   .into(view, new Callback() {
                       @Override
                       public void onSuccess() {
                           attacher.update();
                       }

                       @Override
                       public void onError() {

                       }
                   });
          // loadImag(position);
           container.addView(view);
           return view;
       }
   };

}
