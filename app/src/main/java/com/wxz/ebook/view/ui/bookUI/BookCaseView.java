package com.wxz.ebook.view.ui.bookUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wxz.ebook.R;
import com.wxz.ebook.bean.BookBean;
import com.wxz.ebook.tool.ReadViewTool;

import java.util.List;

public class BookCaseView extends View {
    int viewWidth,viewHeight,textWidth,textHeight,readWidth,readHeight,imgWidth,imgHeight,drawHeight;
    int textColor;
    int paddingLeft,paddingTop,paddingRight,paddingBottom;
    int img,fileType;
    boolean upFlag,isSelect;
    private int fontSize;
    private String name;
    private String fileTypeStr[] = {"TXT","DOC","DOCX","PDF","EPUB"};
    private Paint mPaint;
    private Bitmap bitmap;
    private Matrix matrix;
    private ReadViewTool nameTool,typeTool;
    private List<BookBean.PageModel.LineModel> nameLines,typeLines;

    public BookCaseView(Context context) {
        super(context);
        init();
    }

    public BookCaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initCustomAttrs(context, attrs);
        init();
    }

    public BookCaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomAttrs(context, attrs);
        init();
    }

    /**
     获取自定义属性
     */
    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BookCaseView);
        img = ta.getResourceId(R.styleable.BookCaseView_img,R.drawable.cover_default_new);
        fileType = ta.getInt(R.styleable.BookCaseView_fileType,0);
        isSelect = ta.getBoolean(R.styleable.BookCaseView_isSelect,false);
        upFlag = ta.getBoolean(R.styleable.BookCaseView_upFlag,false);
        ta.recycle();
    }

    private void init() {
        mPaint = new Paint();
        textColor = getResources().getColor(R.color.fontWhite);
        //textColor = 0xFFFFFFFF;
        mPaint.setColor(textColor);
        fontSize = 24;
        mPaint.setTextSize(fontSize);
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        bitmap  = BitmapFactory.decodeResource(this.getResources(),img);
        imgWidth = bitmap.getWidth();
        imgHeight = bitmap.getHeight();
        nameTool = new ReadViewTool();
        typeTool = new ReadViewTool();
    }
    public void setName(String name){
        this.name = name;
        requestLayout();
        invalidate();
    }

    private void setMatrix(int width,int height){
        float bitmapWidth = bitmap.getWidth();
        float bitmapHeight = bitmap.getHeight();
        float scaleX = width / bitmapWidth;
        float scaleY = height / bitmapHeight;
        matrix = new Matrix();
        matrix.preScale(scaleX, scaleY);
        matrix.postTranslate(paddingLeft, paddingTop);
    }

    private List<BookBean.PageModel.LineModel> getStrLine(String str,ReadViewTool readTool, int width){
        readTool.lineInit();
        int lineWidth = 0;
        for(int i=0;i<str.length();i++){
            String subStr;
            if(i < str.length()-1){
                subStr  = str.substring(i, i + 1);
            }else {
                subStr = str.substring(i);
            }
            int fontWidth = (int)mPaint.measureText(subStr);
            lineWidth = lineWidth + fontWidth;
            if(lineWidth < width){
                readTool.addStrArr(subStr,fontWidth,lineWidth-fontWidth,textColor);
            }else{
                readTool.addLine(width-lineWidth+fontWidth,false);
                lineWidth = fontWidth;
                readTool.addStrArr(subStr,lineWidth,0,textColor);
            }
        }
        readTool.addLine(width-lineWidth,false);
        return readTool.getLineModels();
    }

    private void drawText(Canvas canvas, List<BookBean.PageModel.LineModel> textLines, int y, int mode){
        for(int i = 0;i<textLines.size();i++){
            BookBean.PageModel.LineModel line= textLines.get(i);
            int num =line.stringList.size();
            float spacing;
            if(num == 0){
                spacing = 0;
            }else {
                spacing = line.strDiff/mode;
            }
            for (int j=0;j<num;j++){
                mPaint.setColor(Color.WHITE);
                canvas.drawText(line.stringList.get(j), line.strX.get(j)+ spacing + paddingLeft +8,
                        (i + 1) * fontSize * 1.5f + paddingTop - 4 + y, mPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();

        viewWidth = widthSize;
        readWidth = viewWidth - paddingLeft - paddingRight;
        readHeight = readWidth*4/3;
        viewHeight = readHeight + paddingTop + paddingBottom;
        textWidth = imgWidth + paddingLeft + paddingRight;
        textHeight = imgWidth*4/3 + paddingTop + paddingBottom;

        int width;
        int height ;
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = textWidth;
            height = textHeight;
            drawHeight = imgWidth*4/3;
            setMatrix(imgWidth,drawHeight);
            mPaint.setTextSize(28);
            nameLines =getStrLine(name,nameTool,imgWidth - 16);
            mPaint.setTextSize(24);
            typeLines =getStrLine(fileTypeStr[fileType],typeTool,imgWidth - 16);
        } else {
            width = viewWidth;
            height = viewHeight;
            drawHeight = readHeight;
            setMatrix(readWidth,readHeight);
            mPaint.setTextSize(28);
            nameLines =getStrLine(name,nameTool,readWidth - 16);
            mPaint.setTextSize(24);
            typeLines =getStrLine(fileTypeStr[fileType],typeTool,readWidth - 16);
        }


        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap,matrix,mPaint);
        mPaint.setTextSize(28);
        drawText(canvas,nameLines,8,2);
        mPaint.setTextSize(24);
        drawText(canvas,typeLines,drawHeight-(int)(fontSize*1.5) - 8,1);
    }
}
