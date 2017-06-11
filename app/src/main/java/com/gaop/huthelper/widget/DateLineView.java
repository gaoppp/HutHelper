package com.gaop.huthelper.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.gaop.huthelper.model.entity.Holiday;
import com.gaop.huthelper.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 高沛 on 2017/6/9.
 */

public class DateLineView extends View {

    private static final String TAG = "DateLineView";
    private Context context;
    private List<Holiday> holiDayList = new ArrayList<>();

    private Paint linePaint = new Paint();
    private Paint circlePaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    private boolean shouldUpdate;
    private Bitmap fullImage;
    private int sp10;

    public DateLineView(Context context) {
        this(context, null);
    }

    public DateLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        sp10 = DensityUtils.sp2px(context, 10);
        initPaint();
        Holiday day = new Holiday();
        day.setDate("2017.09.12");
        day.setName("端午 0天");
        holiDayList.add(day);
        day = new Holiday();
        day.setDate("2017.09.12");
        day.setName("毕业 10天");
        holiDayList.add(day);
        day = new Holiday();
        day.setDate("2017.09.12");
        day.setName("实习 11天");
        holiDayList.add(day);
        day = new Holiday();
        day.setDate("2017.09.12");
        day.setName("暑假 17天");
        holiDayList.add(day);

    }

    private void initPaint() {
        //虚线
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(3);
        PathEffect effects = new DashPathEffect(new float[]{8, 8}, 0);
        linePaint.setPathEffect(effects);
        //文本
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(sp10);
        textPaint.setColor(Color.WHITE);
        //圆点
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas ca) {

        if (fullImage == null || shouldUpdate) {
            if (holiDayList == null || holiDayList.size() == 0) {
                setVisibility(GONE);
                return;
            } else {
                setVisibility(VISIBLE);
            }
            fullImage = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(fullImage);
            canvas.drawColor(Color.TRANSPARENT);
            //画虚线
            canvas.drawLine(0, getHeight() / 2, this.getWidth(), getHeight() / 2, linePaint);

            Rect rect = new Rect();
            textPaint.getTextBounds("2017.05.30", 0, "2017.05.30".length(), rect);
            float timeWidth = rect.width();
            float timeHeight = rect.height();
            int dp7 = DensityUtils.dp2px(context, 8f);

            float eachPaddintWidth = (getWidth() - 4 * timeWidth) / 3;
            for (int i = 0; i < holiDayList.size(); i++) {
                Holiday day = holiDayList.get(i);
                canvas.drawCircle((float) (eachPaddintWidth * i + timeWidth * (i + 0.5)), getHeight() / 2, 10, circlePaint);
                canvas.drawText(day.getDate().toString(), eachPaddintWidth * i + timeWidth * i, getHeight() / 2 - dp7, textPaint);
                Rect dateRect = new Rect();
                textPaint.getTextBounds(day.getName(), 0, day.getName().length(), dateRect);
                float nameWidth = dateRect.width();
                float nameHeight = dateRect.height();
                canvas.drawText(holiDayList.get(i).getName(), (float) (eachPaddintWidth * i + timeWidth * (i + 0.5) - nameWidth / 2), getHeight() / 2 + dp7 + nameHeight, textPaint);
            }
            shouldUpdate = false;
        }
        ca.drawBitmap(fullImage, 0, 0, null);
    }

    public void setDateLineDate(List<Holiday> list) {
        this.holiDayList = list;
        shouldUpdate = true;
        requestLayout();
    }
}