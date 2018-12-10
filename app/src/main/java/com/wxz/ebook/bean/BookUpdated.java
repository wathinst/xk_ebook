package com.wxz.ebook.bean;

import java.io.Serializable;
import java.util.List;

public class BookUpdated extends Base {

    /**
     * _id : 5a2025f53d9ddf9a0407fcf5
     * author : 随散飘风
     * allowMonthly : true
     * referenceSource : default
     * updated : 2018-12-08T02:31:35.216Z
     * chaptersCount : 833
     * lastChapter : 翻天 第八百三十三章   黑炎火凤
     */

    public List<Updated> updateds;

    public static class Updated implements Serializable {
        public String _id;
        public String author;
        public boolean allowMonthly;
        public String referenceSource;
        public String updated;
        public int chaptersCount;
        public String lastChapter;
    }
}
