package com.wxz.ebook.tool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import com.wxz.ebook.bean.BookBean;

public class ReadFactory {
    private Context context;
    private Bitmap frontBitmap,backBitmap;
    private Canvas frontCanvas,backCanvas;
    private int phoneWidth, phoneHeight;
    private int padL,padR,padT,padB;
    private Paint paint;
    private BookBean bookBean;
    private ReadViewTool readViewTool;
    private int fontSize;
    private int textColor = Color.BLACK;
    private Matrix matrix;
    private Bitmap bgr;
    private String strBuf;


    public ReadFactory(Context context) {
        this.context = context;
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        phoneWidth = dm.widthPixels;
        phoneHeight = dm.heightPixels;
        frontBitmap = Bitmap.createBitmap(phoneWidth, phoneHeight, Bitmap.Config.ARGB_8888);
        frontCanvas = new Canvas(frontBitmap);
        backBitmap = Bitmap.createBitmap(phoneWidth, phoneHeight, Bitmap.Config.ARGB_8888);
        backCanvas = new Canvas(backBitmap);
        paint = new Paint();
        fontSize = 24;
        paint.setTextSize(fontSize);
        paint.setAntiAlias(true);
        bookBean = new BookBean();
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
        readViewTool.setStrCaptal(fontSize,textColor);
        int lineWidth = 2*fontSize;
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
                readViewTool.addPage(phoneHeight -padT-padB,fontSize);
                readViewTool.addLine(0);
                readViewTool.setStrCaptal(fontSize,textColor);
                lineWidth = 2*fontSize;
            }else if(lineWidth < phoneWidth - padR -padL){
                readViewTool.addStrArr(subStr,fontWidth,lineWidth-fontWidth,textColor);
            }else{
                readViewTool.addPage(phoneHeight -padT-padB,fontSize);
                readViewTool.addLine(phoneWidth - padR -padL-lineWidth+fontWidth);
                lineWidth = fontWidth;
                readViewTool.addStrArr(subStr,lineWidth,0,textColor);
            }
        }
        readViewTool.addEnd(phoneHeight -padT-padB,fontSize);
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
        frontBitmap = Bitmap.createBitmap(phoneWidth, phoneHeight, Bitmap.Config.ARGB_8888);
        frontCanvas = new Canvas(frontBitmap);
        backBitmap = Bitmap.createBitmap(phoneWidth, phoneHeight, Bitmap.Config.ARGB_8888);
        backCanvas = new Canvas(backBitmap);
        frontCanvas.drawBitmap(bgr,matrix,paint);
        backCanvas.drawBitmap(bgr,matrix,paint);
        BookBean.PageModel page = bookBean.pageModels.get(bookBean.index);
        for(int i = 0;i<page.lineModels.size();i++){
            BookBean.PageModel.LineModel line= page.lineModels.get(i);
            int num =line.stringList.size();
            float spacing;
            if(num == 0){
                spacing = 0;
            }else {
                spacing = line.strDiff/(float)(num-1);
            }
            for (int j=0;j<num;j++){
                paint.setColor(line.strColors.get(j));
                frontCanvas.drawText(line.stringList.get(j), line.strX.get(j)+ padL+ j*spacing,
                        (i + 1) * fontSize * 1.5f + padT - 4, paint);
                paint.setColor(setColorAlpha(line.strColors.get(j)));
                backCanvas.drawText(line.stringList.get(j), line.strX.get(j)+ padL+ j*spacing,
                        (i + 1) * fontSize * 1.5f + padT - 4, paint);
                paint.setColor(line.strColors.get(j));
            }
        }
    }

    private int setColorAlpha(int color){
        int c = color | 0x18000000;
        c = c & 0x18FFFFFF;
        return c;
    }

    public void setFontSize(int size){
        fontSize = size;
        paint.setTextSize(fontSize);
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
