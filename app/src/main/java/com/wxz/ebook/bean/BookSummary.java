package com.wxz.ebook.bean;

import java.io.Serializable;
import java.util.List;

public class BookSummary extends Base {

    public List<SummaryBean> Summary;

    public static class SummaryBean implements Serializable {
        /**
         * _id : 598c07eb053febf24b88d72a
         * source : zhuishuvip
         * name : 优质书源
         * link : http://vip.zhuishushenqi.com/toc/598c07eb053febf24b88d72a
         * lastChapter : 卷第十一
         * isCharge : false
         * chaptersCount : 11
         * updated : 2017-12-15T11:51:07.820Z
         * starting : true
         * host : vip.zhuishushenqi.com
         */

        public String _id;
        public String source;
        public String name;
        public String link;
        public String lastChapter;
        public boolean isCharge;
        public int chaptersCount;
        public String updated;
        public boolean starting;
        public String host;
    }
}
