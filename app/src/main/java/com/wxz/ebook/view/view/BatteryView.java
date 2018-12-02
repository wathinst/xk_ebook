package com.wxz.ebook.view.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.wxz.ebook.tool.utils.ScreenUtils;

public class BatteryView extends View {

    private int mPower = 100;
    private int battery_width;
    private int battery_height;

    private int battery_head_width;
    private int battery_head_height;

    private int battery_inside_margin;
    private int batteryColor;

    public BatteryView(Context context) {
        super(context);
        batteryColor = Color.WHITE;
    }

    public BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        batteryColor = Color.WHITE;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            battery_width = ScreenUtils.dpToPxInt(25);
        }else {
            battery_width = widthSize;
        }
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            battery_height = ScreenUtils.dpToPxInt(12);
        }else {
            battery_height = heightSize;
        }
        battery_head_width = ScreenUtils.dpToPxInt(3);
        battery_head_height = ScreenUtils.dpToPxInt(2);
        battery_inside_margin = ScreenUtils.dpToPxInt(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //先画外框
        @SuppressLint("DrawAllocation") Paint paint = new Paint();
        paint.setColor(batteryColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ScreenUtils.dpToPxInt(2));

        @SuppressLint("DrawAllocation") Rect rect = new Rect(0, 0,
                 battery_width - battery_head_width, battery_height);
        canvas.drawRect(rect, paint);

        float power_percent = mPower / 100.0f;

        @SuppressLint("DrawAllocation") Paint paint2 = new Paint(paint);
        paint2.setColor(batteryColor);
        paint2.setStyle(Paint.Style.FILL);
        //画电量
        if(power_percent != 0) {
            int p_left = battery_inside_margin;
            int p_top = battery_inside_margin;
            int p_right = battery_inside_margin + (int)((battery_width - 2*battery_inside_margin - battery_head_width) * power_percent);
            int p_bottom = battery_height - battery_inside_margin;
            @SuppressLint("DrawAllocation") Rect rect2 = new Rect(p_left, p_top, p_right , p_bottom);
            canvas.drawRect(rect2, paint2);
        }

        //画电池头
        int h_left = battery_width - battery_head_width;
        int h_top = battery_head_height;
        int h_right = battery_width;
        int h_bottom = battery_height - battery_head_height;
        @SuppressLint("DrawAllocation") Rect rect3 = new Rect(h_left, h_top, h_right, h_bottom);
        canvas.drawRect(rect3, paint2);
    }

    public void setColor(int batteryColor){
        this.batteryColor = batteryColor;
    }

    public void setPower(int power) {
        mPower = power;
        if(mPower < 0) {
            mPower = 0;
        }
        invalidate();
    }
}
