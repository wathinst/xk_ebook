package com.wxz.ebook.tool.bookFactory;

public abstract class BookFactory {
    public abstract <T extends Book> T createRead(Class<T> clz);
}
