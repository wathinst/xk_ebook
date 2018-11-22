package com.wxz.ebook.tool.bookFactory;

import com.wxz.ebook.bean.ChapterListBean;

public interface OnlineBookListener {
    public void getThisChapterText(String text);
    public void getLastChapterText(String text);
    public void getMestChapterText(String text);
    public void getChapterList(ChapterListBean bean);
}
