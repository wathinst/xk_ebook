package com.wxz.ebook.bean;

import java.io.Serializable;
import java.util.List;

public class BookChapter extends Base {

    /**
     * _id : 57076a0f326011945ee8613f
     * link : http://book.my716.com/getBooks.aspx?method=chapterList&bookId=857368
     * source : my176
     * name : 176小说
     * book : 55eef8b27445ad27755670b9
     * chapters : [{"title":"第一章 黄山真君和九洲一号群","link":"http://book.my716.com/getBooks.aspx?method=content&bookId=857368&chapterFile=U_857368_201806221650277655_6423_1.txt","time":0,"chapterCover":"","totalpage":0,"partsize":0,"order":0,"currency":0,"unreadble":false,"isVip":false},{"title":"第二章 且待本尊算上一卦","link":"http://book.my716.com/getBooks.aspx?method=content&bookId=857368&chapterFile=U_857368_201806221650278118_6475_2.txt","time":0,"chapterCover":"","totalpage":0,"partsize":0,"order":0,"currency":0,"unreadble":false,"isVip":false},{"title":"第三章 一张丹方","link":"http://book.my716.com/getBooks.aspx?method=content&bookId=857368&chapterFile=U_857368_201806221650279498_5732_3.txt","time":0,"chapterCover":"","totalpage":0,"partsize":0,"order":0,"currency":0,"unreadble":false,"isVip":false}]
     */

    public String _id;
    public String link;
    public String source;
    public String name;
    public String book;
    public List<ChaptersBean> chapters;

    public static class ChaptersBean implements Serializable {
        /**
         * title : 第一章 黄山真君和九洲一号群
         * link : http://book.my716.com/getBooks.aspx?method=content&bookId=857368&chapterFile=U_857368_201806221650277655_6423_1.txt
         * time : 0
         * chapterCover :
         * totalpage : 0
         * partsize : 0
         * order : 0
         * currency : 0
         * unreadble : false
         * isVip : false
         */

        public String title;
        public String link;
        public int time;
        public String chapterCover;
        public int totalpage;
        public int partsize;
        public int order;
        public int currency;
        public boolean unreadble;
        public boolean isVip;
    }
}
