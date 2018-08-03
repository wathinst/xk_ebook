package com.wxz.ebook.tool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;

import com.wxz.ebook.bean.BookBean;
import com.wxz.ebook.config.ReadPageConfig;

public class ReadFactory {
    private Bitmap frontBitmap,backBitmap;
    private Canvas frontCanvas,backCanvas;
    private int phoneWidth, phoneHeight;
    private int padL,padR,padT,padB;
    private Paint paint;
    private BookBean bookBean;
    private ReadViewTool readViewTool;
    private int textSize;
    private int textColor;
    private Matrix matrix;
    private Bitmap bgr;
    private String strBuf;
    private ReadPageConfig readPageConfig;


    public ReadFactory() {
        readPageConfig = new ReadPageConfig();
        textSize = readPageConfig.textSize;
        readPageConfig.setModeIndex(readPageConfig.pageTheme);
        textColor = readPageConfig.textColor;
        bgr = readPageConfig.getModeBitmap(readPageConfig.pageTheme);

        DisplayMetrics dm = AppUtils.getResource().getDisplayMetrics();
        phoneWidth = dm.widthPixels;
        phoneHeight = dm.heightPixels;
        frontBitmap = Bitmap.createBitmap(phoneWidth, phoneHeight, Bitmap.Config.ARGB_8888);
        frontCanvas = new Canvas(frontBitmap);
        backBitmap = Bitmap.createBitmap(phoneWidth, phoneHeight, Bitmap.Config.ARGB_8888);
        backCanvas = new Canvas(backBitmap);
        paint = new Paint();
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        bookBean = new BookBean();
        bookBean.bookName="";
        readViewTool = new ReadViewTool();
        matrix = new Matrix();
        setPadding(phoneWidth/22,phoneWidth/22, phoneHeight /22, phoneHeight /22);
        setChapterStr("");
    }

    public void setPadding(int padL,int padR,int padT,int padB){
        this.padL = padL;
        this.padR = padR;
        this.padT = padT;
        this.padB = padB;
    }


    public void setPageTheme(int index){
        readPageConfig.setModeIndex(index);
        bgr = readPageConfig.getModeBitmap(index);
        textColor = readPageConfig.textColor;
        paint.setTextSize(textColor);
        readPageConfig.saveReadPageConfig();
    }

    public void setBookName(String str){
        bookBean.bookName = str;
    }

    public void setBg(Bitmap bg){
        bgr = bg;
        setMatrix();
    }

    public void setChapterStr(String str){
        strBuf = str;
        createChapterStr();
    }

    public void createChapterStr(){
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
            int fontWidth = (int)paint.measureText(subStr);
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
        paint.setColor(textColor);
        frontBitmap = Bitmap.createBitmap(phoneWidth, phoneHeight, Bitmap.Config.ARGB_8888);
        frontCanvas = new Canvas(frontBitmap);
        backBitmap = Bitmap.createBitmap(phoneWidth, phoneHeight, Bitmap.Config.ARGB_8888);
        backCanvas = new Canvas(backBitmap);
        frontCanvas.drawBitmap(bgr,matrix,paint);
        backCanvas.drawBitmap(bgr,matrix,paint);

        paint.setTextSize(24);
        paint.setColor(Color.parseColor("#444444"));
        frontCanvas.drawText(bookBean.bookName, padL,
                32, paint);
        paint.setColor(setColorAlpha(Color.parseColor("#444444")));
        backCanvas.drawText(bookBean.bookName, padL,
                32, paint);

        paint.setTextSize(textSize);
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
            if (i>0){
                lineHeight += textSize * page.lineModels.get(i-1).scaleH + pSpacing;
            }
            int num =line.stringList.size();
            float spacing;
            if(num <= 1){
                spacing = 0;
            }else {
                spacing = line.strDiff/(float)(num-1);
            }
            for (int j=0;j<num;j++){
                paint.setColor(line.strColors.get(j));
                frontCanvas.drawText(line.stringList.get(j), line.strX.get(j)+ padL+ j*spacing,
                        lineHeight, paint);
                paint.setColor(setColorAlpha(line.strColors.get(j)));
                backCanvas.drawText(line.stringList.get(j), line.strX.get(j)+ padL+ j*spacing,
                        lineHeight, paint);
            }
        }
    }

    private int setColorAlpha(int color){
        int c = color | 0x18000000;
        c = c & 0x18FFFFFF;
        return c;
    }

    public void setFontSize(int size){
        textSize = size;
        paint.setTextSize(textSize);
        readPageConfig.textSize = textSize;
        readPageConfig.saveReadPageConfig();
        createChapterStr();
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
}
