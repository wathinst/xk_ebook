package com.wxz.ebook.bean;

import java.io.Serializable;
import java.util.List;

public class ChapterListBean implements Serializable {

    public List<ListBean> listBeans ;
    public String name;

    public static class ListBean implements Serializable {
        public String name;
        public int index;
        public int size;

        public ListBean(String name, int index, int size) {
            this.name = name;
            this.index = index;
            this.size = size;
        }
    }


}
