package com.wxz.ebook.tool;

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

    public void init(){
        pageModels = new ArrayList<>();
        lineModels = new ArrayList<>();
        stringList = new ArrayList<>();
        strWidths = new ArrayList<>();
        strXs = new ArrayList<>();
        strColors = new ArrayList<>();
        lineNum = 0;
    }

    public void lineInit(){
        lineModels = new ArrayList<>();
        stringList = new ArrayList<>();
        strWidths = new ArrayList<>();
        strXs = new ArrayList<>();
        strColors = new ArrayList<>();
        lineNum = 0;
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

    public void addLine(int strDiff){
        BookBean.PageModel.LineModel lineModel =new BookBean.PageModel.LineModel(stringList,strWidths,strXs,strDiff,strColors);
        lineModels.add(lineModel);
        stringList = new ArrayList<>();
        strWidths = new ArrayList<>();
        strXs = new ArrayList<>();
        strColors = new ArrayList<>();
    }

    public void addPage(int readHeight,int fontSize){
        lineNum++;
        if(lineNum*fontSize*1.5 > readHeight){
            BookBean.PageModel pageModel =new BookBean.PageModel(lineModels,0);
            pageModels.add(pageModel);
            lineModels = new ArrayList<>();
            lineNum = 1;
        }
    }

    public void addEnd(int readHeight,int fontSize){
        addPage(readHeight,fontSize);
        addLine(0);
        BookBean.PageModel pageModel =new BookBean.PageModel(lineModels,0);
        pageModels.add(pageModel);
        lineNum = 1;
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
