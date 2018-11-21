package com.wxz.ebook.tool.bookFactory;

public abstract class Book {

    public abstract String getThisChapterName();

    public abstract String getLastChapterName();

    public abstract String getMestChapterName();

    public abstract String getThisChapterText();

    public abstract String getLastChapterText();

    public abstract String getMestChapterText();

    public abstract int getChapterIndex();

    public abstract void setChapterIndex(int index);

    public abstract int getPageIndex();

    public abstract void setPageIndex(int index);
}
