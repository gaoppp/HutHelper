package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.net.HttpMethods;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by gaop1 on 2016/9/3.
 */
public class ShowImgActivity extends BaseActivity {


    @BindView(R.id.fraglayout_showImg)
    RelativeLayout fraglayoutShowImg;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;


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
        tvToolbarTitle.setText((curr + 1) + "/" + urls.size());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(curr);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               tvToolbarTitle.setText((position + 1) + "/" + urls.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(ShowImgActivity.this);
            final PhotoViewAttacher attacher = new PhotoViewAttacher(view);
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
            container.addView(view);
            return view;
        }
    };



    @OnClick(R.id.imgbtn_toolbar_back)
    public void onClick() {
        finish();
    }
}
