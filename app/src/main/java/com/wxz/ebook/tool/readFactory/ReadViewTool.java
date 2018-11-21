package com.wxz.ebook.tool.readFactory;

import com.wxz.ebook.bean.BookBean;

import java.util.ArrayList;
import java.util.List;

public class ReadViewTool {
    private List<BookBean.PageModel> pageModels;
    private List<BookBean.PageModel.LineModel> lineModels;
    private List<String> stringList;
    private List<Integer> strWidths;
    private List<Integer> strXs;
    private List<Integer> strColors;
    private int lineNum;
    private float lineHeight;

    public void init(){
        pageModels = new ArrayList<>();
        lineModels = new ArrayList<>();
        stringList = new ArrayList<>();
        strWidths = new ArrayList<>();
        strXs = new ArrayList<>();
        strColors = new ArrayList<>();
        lineNum = 0;
        lineHeight = 0;
    }

    public void lineInit(){
        lineModels = new ArrayList<>();
        stringList = new ArrayList<>();
        strWidths = new ArrayList<>();
        strXs = new ArrayList<>();
        strColors = new ArrayList<>();
        lineNum = 0;
        lineHeight = 0;
    }

    public void addStrArr(String subStr,int strWidth,int strX,int strColor){
        stringList.add(subStr);
        strWidths.add(strWidth);
        strXs.add(strX);
        strColors.add(strColor);
    }

    public void setStrCaptal(int strWidth,int strColor){
        for(int i=0;i<2;i++){
            stringList.add("");
            strWidths.add(strWidth);
            strXs.add(i*strWidth);
            strColors.add(strColor);
        }
    }

    public void addLine(int strDiff, boolean b){
        BookBean.PageModel.LineModel lineModel =new BookBean.PageModel.LineModel(stringList,strWidths,strXs,strDiff,strColors,b?2.0f:1.5f);
        lineModels.add(lineModel);
        stringList = new ArrayList<>();
        strWidths = new ArrayList<>();
        strXs = new ArrayList<>();
        strColors = new ArrayList<>();
    }

    private float lastHeight;
    public boolean addPage(int readHeight, int fontSize, boolean b){
        lineNum++;
        //lineHeight += fontSize;
        if(lineHeight + fontSize > readHeight){
            BookBean.PageModel pageModel =new BookBean.PageModel(lineModels,readHeight-lineHeight+lastHeight);
            pageModels.add(pageModel);
            lineModels = new ArrayList<>();
            lineNum = 1;
            lineHeight = fontSize* 1.5f;
            lastHeight = fontSize * 0.5f;
            return false;
        }else {
            if (b){
                lastHeight = fontSize;
                lineHeight += fontSize * 2.0f;
            }else {
                lastHeight = fontSize * 0.5f;
                lineHeight += fontSize * 1.5f;
            }
            return true;
        }
    }

    public void addEnd(int readHeight,int fontSize){
        addPage(readHeight,fontSize,false);
        addLine(0,false);
        BookBean.PageModel pageModel =new BookBean.PageModel(lineModels,0);
        pageModels.add(pageModel);
        lineNum = 1;
        lineHeight = fontSize* 1.5f;
    }

    public List<BookBean.PageModel> getPageModels() {
        return pageModels;
    }

    public void setPageModels(List<BookBean.PageModel> pageModels) {
        this.pageModels = pageModels;
    }

    public List<BookBean.PageModel.LineModel> getLineModels() {
        return lineModels;
    }

    public void setLineModels(List<BookBean.PageModel.LineModel> lineModels) {
        this.lineModels = lineModels;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public List<Integer> getStrWidths() {
        return strWidths;
    }

    public void setStrWidths(List<Integer> strWidths) {
        this.strWidths = strWidths;
    }

    public List<Integer> getStrXs() {
        return strXs;
    }

    public void setStrXs(List<Integer> strXs) {
        this.strXs = strXs;
    }

    public List<Integer> getStrColors() {
        return strColors;
    }

    public void setStrColors(List<Integer> strColors) {
        this.strColors = strColors;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }
}
