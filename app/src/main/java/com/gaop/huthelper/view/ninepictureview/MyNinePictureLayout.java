package com.gaop.huthelper.view.ninepictureview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;

import com.gaop.huthelper.R;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.view.activity.ShowImgActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gaop1 on 2016/12/21.
 */

public class MyNinePictureLayout extends PictureLayout {
    private static final String TAG = "MyNinePictureLayout";
    protected static final int MAX_W_H_RATIO = 3;
    public MyNinePictureLayout(Context context) {
        super(context);
    }

    public MyNinePictureLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean displayOneImage(final RatioImageView imageView, String url, final int parentWidth) {
        Picasso.with(context).load(HttpMethods.BASE_URL + url).placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                int newW;
                int newH;
                if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                    newW = parentWidth / 2;
                    newH = newW * 5 / 3;
                } else if (h < w) {//h:w = 2:3
                    newW = parentWidth * 2 / 3;
                    newH = newW * 2 / 3;
                } else {//newH:h = newW :w
                    newW = parentWidth / 2;
                    newH = h * newW / w;
                }
                setOneImageLayoutParams(imageView, newW, newH);
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                imageView.setBackground(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                imageView.setBackground(placeHolderDrawable);
            }
        });
        return false;
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        Picasso.with(context).load(HttpMethods.BASE_URL + url).placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error).into(imageView);
    }

    @Override
    protected void onClickImage(int position, String url, List<String> urlList) {
        Bundle mBundle = new Bundle();
        mBundle.putStringArrayList("urls", (ArrayList<String>) urlList);
        mBundle.putInt("curr", position);
        Intent intent = new Intent();
        intent.setClass(context, ShowImgActivity.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
}
