package com.wxz.ebook.tool.bookFactory;

public class BookConcreteFactory extends BookFactory {
    @Override
    public <T extends Book> T createRead(Class<T> clz) {
        Book read = null;
        String className = clz.getName();
        try {
            read = (Book) Class.forName(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) read;
    }
}
