package com.wxz.ebook.tool.bookFactory;

import android.content.Context;

import com.wxz.ebook.bean.BookInfoBean;

public abstract class Book {

    public abstract void init(Context context, BookInfoBean bookInfoBean);

    public abstract String getThisChapterName();

    public abstract String getLastChapterName();

    public abstract String getMextChapterName();

    public abstract float getThisPercent();

    public abstract float getLastPercent();

    public abstract float getMextPercent();

    public abstract String getThisChapterText();

    public abstract String getLastChapterText();

    public abstract String getMextChapterText();

    public abstract int getChapterIndex();

    public abstract void setChapterIndex(int index);

    public abstract int getPageIndex();

    public abstract void setPageIndex(int index);

    public abstract int getBookType();

    public abstract int getChapterNum();

    public abstract int getPageNum();

    public abstract void setPageNum(int num);

    public abstract BookInfoBean getBookInfoBean();
}
