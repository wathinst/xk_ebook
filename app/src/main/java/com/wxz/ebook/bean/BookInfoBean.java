package com.wxz.ebook.bean;

import java.io.Serializable;

public class BookInfoBean implements Serializable {
    public String id;  //书籍编号
    public int readIndex; //当前阅读的页码
    public int pageIndex; //当前阅读的章节
    public String name; //书籍名称
    public String path; //本地书籍路径地址
    public long date; //阅读日期
    public int bookTybe; //书籍类型 0:本地书籍；1：在线书籍
    public String bookId; //在线书籍编号
    public String imgPath; //在线书籍图像地址
    public int fileTybe; //本地书籍类型
    public int pageSize; //当前章节的页面总数
    public long updated; //在线书籍更新日期
    public int chaptersCount; //在线书籍章节数
    public String bookSummaryId; //在线书籍源编号
    public int isUpdatad;  //是否有更新
}
