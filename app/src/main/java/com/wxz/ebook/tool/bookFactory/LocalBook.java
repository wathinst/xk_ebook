package com.wxz.ebook.tool.bookFactory;

import android.content.Context;
import android.os.Environment;
import com.wxz.ebook.bean.BookInfoBean;
import com.wxz.ebook.bean.ChapterListBean;
import com.wxz.ebook.tool.readFactory.StrRWBuffer;
import java.io.File;
import java.io.IOException;

public class LocalBook extends Book {
    private StrRWBuffer buffer;
    private BookInfoBean bookInfoBean;
    private ChapterListBean bean;

    public void init(Context context, BookInfoBean bookInfoBean){
        this.bookInfoBean = bookInfoBean;
        initPage(bookInfoBean);
    }

    public void setChapterListBean(ChapterListBean bean){
        this.bean = bean;
    }

    public BookInfoBean getBookInfoBean() {
        return bookInfoBean;
    }

    private void initPage(BookInfoBean bookInfoBean){
        String fileName = getSDPath() +"/" + "ebook.xkr";
        if (bookInfoBean != null){
            fileName = bookInfoBean.path;
        }
        try {
            buffer = new StrRWBuffer(fileName);
            buffer.newReadMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        assert sdDir != null;
        return sdDir.toString();
    }

    public StrRWBuffer getBuffer() {
        return buffer;
    }

    /**
     * 获取本章的章节名称
     * @return 章节名称
     */
    @Override
    public String getThisChapterName() {
        String str = "";
        if (buffer != null && bean != null){
            str = bean.listBeans.get(bookInfoBean.pageIndex).name;
        }
        return str;
    }

    /**
     * 获取上一章的章节名称
     * @return 章节名称
     */
    @Override
    public String getLastChapterName() {
        String str = "";
        if(buffer != null && bean!= null && bookInfoBean.pageIndex > 0){
            str = bean.listBeans.get(bookInfoBean.pageIndex-1).name;
        }
        return str;
    }

    /**
     * 获取下一章的章节名称
     * @return 章节名称
     */
    @Override
    public String getMextChapterName() {
        String str = "";
        if (buffer != null && bean!= null && bookInfoBean.pageIndex + 1 < bean.listBeans.size()){
            str = bean.listBeans.get(bookInfoBean.pageIndex+1).name;
        }
        return str;
    }

    @Override
    public float getThisPercent() {
        float percent = 0.00f;
        if (buffer != null && bean != null){
            percent = (float)bookInfoBean.pageIndex*100/bean.listBeans.size();
        }
        return percent;
    }

    @Override
    public float getLastPercent() {
        float percent = 0.00f;
        if(buffer != null && bean!= null && bookInfoBean.pageIndex > 0){
            percent = (float) (bookInfoBean.pageIndex-1)*100/bean.listBeans.size();
        }
        return percent;
    }

    @Override
    public float getMextPercent() {
        float percent = 0.00f;
        if (buffer != null && bean!= null && bookInfoBean.pageIndex + 1 < bean.listBeans.size()){
            percent = (float)(bookInfoBean.pageIndex+1)*100/bean.listBeans.size();
        }
        return percent;
    }


    /**
     * 获取本章的文本内容
     * @return 文本内容
     */
    @Override
    public String getThisChapterText() {
        String str = "";
        if (buffer != null && bean != null){
            str = buffer.getChapterStr(bean.listBeans.get(bookInfoBean.pageIndex));
        }
        return str;
    }

    /**
     * 获取本上一章的文本内容
     * @return 文本内容
     */
    @Override
    public String getLastChapterText() {
        String str = "";
        if(buffer != null && bean!= null && bookInfoBean.pageIndex > 0){
            str = buffer.getChapterStr(bean.listBeans.get(bookInfoBean.pageIndex-1));
        }
        return str;
    }


    /**
     * 获取下一章的文本内容
     * @return 文本内容
     */
    @Override
    public String getMextChapterText() {
        String str = "";
        if (buffer != null && bean!= null && bookInfoBean.pageIndex + 1 < bean.listBeans.size()){
            str = buffer.getChapterStr(bean.listBeans.get(bookInfoBean.pageIndex+1));
        }
        return str;
    }

    @Override
    public int getChapterIndex() {
        return bookInfoBean.pageIndex;
    }

    @Override
    public void setChapterIndex(int index) {
        bookInfoBean.pageIndex = index;
    }

    @Override
    public int getPageIndex() {
        return bookInfoBean.readIndex;
    }

    @Override
    public void setPageIndex(int index) {
        bookInfoBean.readIndex = index;
    }

    @Override
    public int getBookType() {
        return bookInfoBean.bookTybe;
    }

    @Override
    public int getChapterNum() {
        return bean.listBeans.size();
    }

    @Override
    public void setPageNum(int num) {
        bookInfoBean.pageSize = num;
    }

    @Override
    public int getPageNum() {
        return bookInfoBean.pageSize;
    }
}
