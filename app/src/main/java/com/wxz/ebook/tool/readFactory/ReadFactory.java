package com.wxz.ebook.tool.readFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import com.wxz.ebook.bean.BookBean;
import com.wxz.ebook.config.ReadPageConfig;
import com.wxz.ebook.tool.utils.AppUtils;
import com.wxz.ebook.tool.utils.DensityUtil;
import com.wxz.ebook.tool.utils.ScreenUtils;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadFactory {
    private Bitmap frontBitmap,backBitmap;//纸张前面图像，纸张背面图像
    private Canvas frontCanvas,backCanvas;//纸张前面画布，纸张背面画布
    private int phoneWidth, phoneHeight;//屏幕宽度，屏幕高度
    private int padL,padR,padT,padB;
    private Paint frontPaint,backPaint;
    private Paint frontTitlePaint,backTitlePaint;
    private BookBean bookBean;
    private ReadViewTool readViewTool;
    private int textSize;
    private int textColor;
    private Matrix matrix;
    private Bitmap bgr;
    private String strBuf;
    private ReadPageConfig readPageConfig;
    private DecimalFormat decimalFormat;
    private SimpleDateFormat dateFormat;
    private int timeLen;
    private int battery;
    private float percent = 0.00f;

    private int battery_width;
    private int battery_height;
    private int battery_head_width;
    private int battery_head_height;
    private int battery_inside_margin;


    @SuppressLint("SimpleDateFormat")
    public ReadFactory() {
        readPageConfig = new ReadPageConfig();
        textSize = readPageConfig.textSize;
        readPageConfig.setModeIndex(readPageConfig.pageTheme);
        textColor = readPageConfig.textColor;
        bgr = readPageConfig.getModeBitmap(readPageConfig.pageTheme);

        decimalFormat = new DecimalFormat("#0.00");
        dateFormat = new SimpleDateFormat("HH:mm");

        initPaint();
        batteryInit();

        timeLen = (int) frontTitlePaint.measureText("00:00");

        DisplayMetrics dm = AppUtils.getResource().getDisplayMetrics();
        phoneWidth = dm.widthPixels;
        phoneHeight = dm.heightPixels;
        frontBitmap = Bitmap.createBitmap(phoneWidth, phoneHeight, Bitmap.Config.ARGB_8888);
        frontCanvas = new Canvas(frontBitmap);
        backBitmap = Bitmap.createBitmap(phoneWidth, phoneHeight, Bitmap.Config.ARGB_8888);
        backCanvas = new Canvas(backBitmap);

        bookBean = new BookBean();
        bookBean.bookName="";
        readViewTool = new ReadViewTool();
        matrix = new Matrix();
        setPadding(DensityUtil.dp2px(AppUtils.getAppContext(),20),DensityUtil.dp2px(AppUtils.getAppContext(),16),
                DensityUtil.dp2px(AppUtils.getAppContext(),30), DensityUtil.dp2px(AppUtils.getAppContext(),30));
        setChapterStr("");
        setBattery(battery);
    }

    private void initPaint(){
        frontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        frontPaint.setTextSize(textSize);
        frontPaint.setAntiAlias(true);
        frontPaint.setColor(textColor);

        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setTextSize(textSize);
        backPaint.setAntiAlias(true);
        backPaint.setColor(setColorAlpha(textColor));

        frontTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        frontTitlePaint.setTextSize(DensityUtil.sp2px(AppUtils.getAppContext(),12));
        frontTitlePaint.setColor(Color.parseColor("#444444"));

        backTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backTitlePaint.setTextSize(DensityUtil.sp2px(AppUtils.getAppContext(),12));
        backTitlePaint.setColor(setColorAlpha(Color.parseColor("#444444")));
    }

    private void setPadding(int padL,int padR,int padT,int padB){
        this.padL = padL;
        this.padR = padR;
        this.padT = padT;
        this.padB = padB;
    }


    public void setPageTheme(int index){
        readPageConfig.setModeIndex(index);
        bgr = readPageConfig.getModeBitmap(index);
        textColor = readPageConfig.textColor;
        frontPaint.setColor(textColor);
        backPaint.setColor(setColorAlpha(textColor));
        textSize = readPageConfig.textSize;
        frontPaint.setTextSize(textSize);
        backPaint.setTextSize(textSize);
        readPageConfig.saveReadPageConfig();
        createChapterStr();
    }

    public void setBookName(String str){
        bookBean.bookName = str;
    }

    public void setChapterStr(String str){
        strBuf = str;
        createChapterStr();
    }

    private void createChapterStr(){
        readViewTool.init();
        readViewTool.setStrCaptal(textSize,textColor);
        int lineWidth = 2*textSize;
        for(int i=0;i<strBuf.length();i++){
            String subStr;
            if(i < strBuf.length()-1){
                subStr  = strBuf.substring(i, i + 1);
            }else {
                subStr = strBuf.substring(i);
            }
            int fontWidth = (int)frontPaint.measureText(subStr);
            lineWidth = lineWidth + fontWidth;
            if (subStr.equals("\n")){
                boolean b = readViewTool.addPage(phoneHeight -padT-padB,textSize,true);
                readViewTool.addLine(0, b);
                readViewTool.setStrCaptal(textSize,textColor);
                lineWidth = 2*textSize;
            }else if(lineWidth < phoneWidth - padR -padL){
                readViewTool.addStrArr(subStr,fontWidth,lineWidth-fontWidth,textColor);
            }else{
                readViewTool.addPage(phoneHeight -padT-padB,textSize,false);
                readViewTool.addLine(phoneWidth - padR -padL-lineWidth+fontWidth,false);
                lineWidth = fontWidth;
                readViewTool.addStrArr(subStr,lineWidth,0,textColor);
            }
        }
        readViewTool.addEnd(phoneHeight -padT-padB,textSize);
        bookBean.pageModels = readViewTool.getPageModels();
    }

    private void setMatrix(){
        float bitmapWidth = bgr.getWidth();
        float bitmapHeight = bgr.getHeight();
        float scaleX = phoneWidth / bitmapWidth;
        float scaleY = phoneHeight / bitmapHeight;
        matrix = new Matrix();
        matrix.postTranslate(0, 0);
        matrix.preScale(scaleX, scaleY);
    }

    public void readDraw(){
        setMatrix();
        frontBitmap = Bitmap.createBitmap(phoneWidth, phoneHeight, Bitmap.Config.ARGB_8888);
        frontCanvas = new Canvas(frontBitmap);
        backBitmap = Bitmap.createBitmap(phoneWidth, phoneHeight, Bitmap.Config.ARGB_8888);
        backCanvas = new Canvas(backBitmap);
        frontCanvas.drawBitmap(bgr,matrix,frontPaint);
        backCanvas.drawBitmap(bgr,matrix,frontPaint);

        frontCanvas.drawText(bookBean.bookName, padL, ScreenUtils.dpToPxInt(20), frontTitlePaint);
        backCanvas.drawText(bookBean.bookName, padL, ScreenUtils.dpToPxInt(20), backTitlePaint);

        BookBean.PageModel page = bookBean.pageModels.get(bookBean.index);
        float lineHeight = textSize + padT - 4;
        float pSpacing;
        int pNum =page.lineModels.size();
        if(pNum <= 1){
            pSpacing = 0;
        }else {
            pSpacing = page.lineDiff/(float)(pNum-1);
        }
        for(int i = 0;i<page.lineModels.size();i++){
            BookBean.PageModel.LineModel line= page.lineModels.get(i);
            if (i>0) lineHeight += textSize * page.lineModels.get(i-1).scaleH + pSpacing;
            int num =line.stringList.size();
            float spacing;
            if(num <= 1){
                spacing = 0;
            }else {
                spacing = line.strDiff/(float)(num-1);
            }
            for (int j=0;j<num;j++){
                frontPaint.setColor(line.strColors.get(j));
                frontCanvas.drawText(line.stringList.get(j), line.strX.get(j)+ padL+ j*spacing,
                        lineHeight, frontPaint);
                backPaint.setColor(setColorAlpha(line.strColors.get(j)));
                backCanvas.drawText(line.stringList.get(j), line.strX.get(j)+ padL+ j*spacing,
                        lineHeight, backPaint);
            }
        }
        // 绘制提示内容
        drawBattery(frontCanvas,frontTitlePaint,phoneWidth-padR-battery_width-battery_head_width,
                phoneHeight - ScreenUtils.dpToPxInt(19));
        drawBattery(backCanvas,backTitlePaint,phoneWidth-padR-battery_width-battery_head_width,
                phoneHeight - ScreenUtils.dpToPxInt(19));

        String mTime = dateFormat.format(new Date());
        frontCanvas.drawText(mTime, phoneWidth-padR-battery_width-battery_head_width - timeLen - ScreenUtils.dpToPxInt(4),
                phoneHeight - ScreenUtils.dpToPxInt(10), frontTitlePaint);
        backCanvas.drawText(mTime, phoneWidth-padR-battery_width-battery_head_width - timeLen- ScreenUtils.dpToPxInt(4),
                phoneHeight - ScreenUtils.dpToPxInt(10), backTitlePaint);

        frontCanvas.drawText(decimalFormat.format(percent) + "%", padL,
                phoneHeight - ScreenUtils.dpToPxInt(10), frontTitlePaint);
        backCanvas.drawText(decimalFormat.format(percent) + "%", padL,
                phoneHeight - ScreenUtils.dpToPxInt(10), backTitlePaint);
    }

    private int setColorAlpha(int color){
        int c = color | 0x18000000;
        c = c & 0x18FFFFFF;
        return c;
    }

    public void setFontSize(int size){
        textSize = size;
        frontPaint.setTextSize(textSize);
        backPaint.setTextSize(textSize);
        readPageConfig.textSize = textSize;
        readPageConfig.saveReadPageConfig();
        createChapterStr();
    }

    public int getFontSize(){
        return textSize;
    }

    public void setPageIndex(int index){
        if (index>=0 && index<bookBean.pageModels.size()){
            bookBean.index = index;
        }
    }

    public int getPageSize(){
        return bookBean.pageModels.size();
    }

    public Bitmap getFrontBitmap() {
        return frontBitmap;
    }

    public Bitmap getBackBitmap(){
        return backBitmap;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    private void batteryInit(){
        battery_width = ScreenUtils.dpToPxInt(18);
        battery_height = ScreenUtils.dpToPxInt(9);
        battery_head_width = ScreenUtils.dpToPxInt(3);
        battery_head_height = ScreenUtils.dpToPxInt(2);
        battery_inside_margin = ScreenUtils.dpToPxInt(2);
    }

    private void drawBattery(Canvas canvas,Paint paint,int batteryX,int batteryY){
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ScreenUtils.dpToPxInt(1));
        @SuppressLint("DrawAllocation") Rect rect = new Rect(batteryX, batteryY,
                batteryX + battery_width - battery_head_width, batteryY + battery_height);
        canvas.drawRect(rect, paint);

        float power_percent = battery / 100.0f;
        paint.setStyle(Paint.Style.FILL);
        //画电量
        if(power_percent != 0) {
            int p_left = batteryX + battery_inside_margin;
            int p_top = batteryY + battery_inside_margin;
            int p_right = batteryX + battery_inside_margin + (int)((battery_width - 2*battery_inside_margin - battery_head_width) * power_percent);
            int p_bottom = batteryY + battery_height - battery_inside_margin;
            @SuppressLint("DrawAllocation") Rect rect2 = new Rect(p_left, p_top, p_right , p_bottom);
            canvas.drawRect(rect2, paint);
        }

        //画电池头
        int h_left = batteryX + battery_width - battery_head_width;
        int h_top = batteryY + battery_head_height;
        int h_right = batteryX + battery_width;
        int h_bottom = batteryY + battery_height - battery_head_height;
        @SuppressLint("DrawAllocation") Rect rect3 = new Rect(h_left, h_top, h_right, h_bottom);
        canvas.drawRect(rect3, paint);

        paint.setStrokeWidth(0);
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }


    public void recycle() {
        if (backBitmap != null && !backBitmap.isRecycled()) {
            backBitmap.recycle();
            backBitmap = null;
        }

        if (frontBitmap != null && !frontBitmap.isRecycled()) {
            frontBitmap.recycle();
            frontBitmap = null;
        }
    }
}
