package com.wxz.ebook.tool;

import com.wxz.ebook.bean.BookInfoBean;

import java.util.Comparator;

public class ComparatorBookInfo implements Comparator<BookInfoBean> {

    @Override
    public int compare(BookInfoBean o1, BookInfoBean o2) {
        return  Long.compare(o2.getDate(),o1.getDate());
    }
}
