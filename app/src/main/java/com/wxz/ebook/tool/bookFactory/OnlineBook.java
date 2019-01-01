package com.wxz.ebook.tool.bookFactory;

import android.content.Context;
import android.util.Log;
import com.wxz.ebook.api.BookApi;
import com.wxz.ebook.bean.BookBToc;
import com.wxz.ebook.bean.BookInfoBean;
import com.wxz.ebook.bean.BookMixAToc;
import com.wxz.ebook.bean.BookSummary;
import com.wxz.ebook.bean.ChapterListBean;
import com.wxz.ebook.bean.ChapterRead;
import com.wxz.ebook.cache.CacheProviders;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import okhttp3.OkHttpClient;

public class OnlineBook extends Book {

    private BookInfoBean bookInfoBean;
    private BookBToc mBookBToc;
    private BookMixAToc mBookMixAToc;
    private Context context;
    private OnlineBookListener listener;
    private ChapterListBean bean;
    private boolean isBookBToc = false;

    @Override
    public void init(Context context,BookInfoBean bookInfoBean) {
        this.context = context;
        this.bookInfoBean = bookInfoBean;
        this.bookInfoBean.isUpdatad = 0;
        if (bookInfoBean.bookSummaryId != null){
            if( bookInfoBean.bookSummaryId.isEmpty()){
                getSummary();
            }else {
                if (isBookBToc){
                    getBookBToc();
                }else {
                    getBookMixAToc();
                }
            }
        }else{
            getSummary();
        }

    }

    private void getSummary(){
        String titleName = "bookSummary" + bookInfoBean.bookId;
        Observable<List<BookSummary.SummaryBean>> bookSummaryObservable = BookApi.getInstance(new OkHttpClient())
                .getBookSummary(bookInfoBean.bookId);
        CacheProviders.getUserCache(context)
                .getBookSummary(bookSummaryObservable,new DynamicKey(titleName),new EvictDynamicKey(false))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookSummary.SummaryBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onNext(List<BookSummary.SummaryBean> summaryBeans) {
                        if (summaryBeans!=null && summaryBeans.size()>0){
                            bookInfoBean.bookSummaryId = summaryBeans.get(0)._id;
                            if (isBookBToc){
                                getBookBToc();
                            }else {
                                getBookMixAToc();
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("bookSummaryId6",e.getMessage());
                    }
                    @Override
                    public void onComplete() { }
                });
    }

    private void getBookMixAToc(){
        String titleName = "bookDetailsList" + bookInfoBean.bookId;
        Observable<BookMixAToc> bookMixATocObservable = BookApi.getInstance(new OkHttpClient())
                .getBookMixAToc(bookInfoBean.bookId,"chapter");
        CacheProviders.getUserCache(context)
                .getBookMixAToc(bookMixATocObservable,new DynamicKey(titleName),new EvictDynamicKey(false))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookMixAToc>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onNext(BookMixAToc bookMixAToc) {
                        mBookMixAToc = bookMixAToc;
                        bean = mixATocToChapterList(bookMixAToc);
                        listener.getChapterList(bean);
                    }
                    @Override
                    public void onError(Throwable e) { }
                    @Override
                    public void onComplete() { }
                });
    }

    private void getBookBToc(){
        String titleName = "bookDetailsList" + bookInfoBean.bookSummaryId;
        Observable<BookBToc> bookBTocObservable = BookApi.getInstance(new OkHttpClient())
                .getBookBToc(bookInfoBean.bookSummaryId,"chapters");
        CacheProviders.getUserCache(context)
                .getBookBToc(bookBTocObservable,new DynamicKey(titleName),new EvictDynamicKey(false))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookBToc>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(BookBToc bookBToc) {
                        mBookBToc = bookBToc;
                        bean = bookBTocToChapterList(bookBToc);
                        listener.getChapterList(bean);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("getBookBToc",e.getMessage());
                    }
                    @Override
                    public void onComplete() { }
                });
    }

    private ChapterListBean mixATocToChapterList(BookMixAToc bookMixAToc){
        ChapterListBean bean = new ChapterListBean();
        bean.name = bookInfoBean.name;
        List<ChapterListBean.ListBean> listBeans = new ArrayList<>();
        for (int i=0;i<bookMixAToc.mixToc.chapters.size();i++){
            ChapterListBean.ListBean listBean = new ChapterListBean.ListBean(
                    bookMixAToc.mixToc.chapters.get(i).title, i, bookMixAToc.mixToc.chapters.get(i).link,
                    false);
            listBeans.add(listBean);
        }
        bean.listBeans = listBeans;
        return bean;
    }

    private ChapterListBean bookBTocToChapterList(BookBToc bookBToc){
        ChapterListBean bean = new ChapterListBean();
        bean.name = bookInfoBean.name;
        List<ChapterListBean.ListBean> listBeans = new ArrayList<>();
        for (int i=0;i<bookBToc.chapters.size();i++){
            ChapterListBean.ListBean listBean = new ChapterListBean.ListBean(
                    bookBToc.chapters.get(i).title, i, bookBToc.chapters.get(i).link,bookBToc.chapters.get(i).isVip);
            listBeans.add(listBean);
        }
        bean.listBeans = listBeans;
        return bean;
    }

    public void setOnlineBookListener(OnlineBookListener listener){
        this.listener = listener;
    }

    @Override
    public String getThisChapterName() {
        String str = "";
        if (bean != null){
            str = bean.listBeans .get(bookInfoBean.pageIndex).name;
        }
        return str;
    }

    @Override
    public String getLastChapterName() {
        String str = "";
        if(bean!= null && bookInfoBean.pageIndex > 0){
            str = bean.listBeans.get(bookInfoBean.pageIndex-1).name;
        }
        return str;
    }

    @Override
    public String getMextChapterName() {
        String str = "";
        if (bean!= null && bookInfoBean.pageIndex + 1 < bean.listBeans.size()){
            str = bean.listBeans.get(bookInfoBean.pageIndex+1).name;
        }
        return str;
    }

    @Override
    public float getThisPercent() {
        float percent = 0.00f;
        if (bean != null){
            percent = (float)bookInfoBean.pageIndex*100/bean.listBeans.size();
        }
        return percent;
    }

    @Override
    public float getLastPercent() {
        float percent = 0.00f;
        if(bean!= null && bookInfoBean.pageIndex > 0){
            percent = (float) (bookInfoBean.pageIndex-1)*100/bean.listBeans.size();
        }
        return percent;
    }

    @Override
    public float getMextPercent() {
        float percent = 0.00f;
        if (bean!= null && bookInfoBean.pageIndex + 1 < bean.listBeans.size()){
            percent = (float)(bookInfoBean.pageIndex+1)*100/bean.listBeans.size();
        }
        return percent;
    }

    @Override
    public String getThisChapterText() {
        String str = "";
        if (bean != null){
            //String titleName1 = "ChapterRead"+ bookInfoBean.bookId + "no" +
                  //  mBookBToc.mixToc.chapters.get(bookInfoBean.pageIndex).id;
            if(bean.listBeans.get(bookInfoBean.pageIndex).isVip){
                listener.getThisChapterText("该章节是会员专属，建议换源继续阅读。\n" +
                        "支持正版可升级会员，至于如何升级？\n" +
                        "校咖阅读没有提供接口，所以你想支持也支持不了……");
            }else {
                String titleName1 = bean.listBeans.get(bookInfoBean.pageIndex).link;
                Observable<ChapterRead> chapterReadObservable = BookApi.getInstance(new OkHttpClient())
                        .getChapterRead(bean.listBeans.get(bookInfoBean.pageIndex).link);
                CacheProviders.getUserCache(context)
                        .getChapterRead(chapterReadObservable,new DynamicKey(titleName1),new EvictDynamicKey(false))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ChapterRead>() {
                            @Override
                            public void onSubscribe(Disposable d) { }
                            @Override
                            public void onNext(ChapterRead chapterRead) {
                                Log.e("testThis",String.valueOf(bookInfoBean.pageIndex));
                                if (isBookBToc){
                                    listener.getThisChapterText(chapterRead.chapter.cpContent);
                                }else {
                                    listener.getThisChapterText(chapterRead.chapter.body);
                                }
                            }
                            @Override
                            public void onError(Throwable e) {}
                            @Override
                            public void onComplete() {}
                        });
            }
        }
        return str;
    }

    @Override
    public String getLastChapterText() {
        String str = "";
        if(bean!= null && bookInfoBean.pageIndex > 0){
            if(bean.listBeans.get(bookInfoBean.pageIndex - 1).isVip){
                listener.getThisChapterText("该章节是会员专属，建议换源继续阅读。\n" +
                        "支持正版可升级会员，至于如何升级？\n" +
                        "校咖阅读没有提供接口，所以你想支持也支持不了……");
            }else {
                String titleName1 = bean.listBeans.get(bookInfoBean.pageIndex - 1).link;
                Observable<ChapterRead> chapterReadObservable = BookApi.getInstance(new OkHttpClient())
                        .getChapterRead(bean.listBeans.get(bookInfoBean.pageIndex  - 1).link);
                CacheProviders.getUserCache(context)
                        .getChapterRead(chapterReadObservable,new DynamicKey(titleName1),new EvictDynamicKey(false))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ChapterRead>() {
                            @Override
                            public void onSubscribe(Disposable d) { }
                            @Override
                            public void onNext(ChapterRead chapterRead) {
                                Log.e("testLast",String.valueOf(bookInfoBean.pageIndex - 1));
                                if (isBookBToc){
                                    listener.getLastChapterText(chapterRead.chapter.cpContent);
                                }else {
                                    listener.getLastChapterText(chapterRead.chapter.body);
                                }
                            }
                            @Override
                            public void onError(Throwable e) {}
                            @Override
                            public void onComplete() {}
                        });
            }
        }
        return str;
    }

    @Override
    public String getMextChapterText() {
        String str = "";
        if (bean!= null && bookInfoBean.pageIndex + 1 < bean.listBeans.size()){
            if(bean.listBeans.get(bookInfoBean.pageIndex + 1).isVip){
                listener.getThisChapterText("该章节是会员专属，建议换源继续阅读。\n" +
                        "支持正版可升级会员，至于如何升级？\n" +
                        "校咖阅读没有提供接口，所以你想支持也支持不了……");
            }else {
                String titleName1 = bean.listBeans.get(bookInfoBean.pageIndex + 1).link;
                Observable<ChapterRead> chapterReadObservable = BookApi.getInstance(new OkHttpClient())
                        .getChapterRead(bean.listBeans.get(bookInfoBean.pageIndex + 1).link);
                CacheProviders.getUserCache(context)
                        .getChapterRead(chapterReadObservable,new DynamicKey(titleName1),new EvictDynamicKey(false))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ChapterRead>() {
                            @Override
                            public void onSubscribe(Disposable d) { }
                            @Override
                            public void onNext(ChapterRead chapterRead) {
                                Log.e("testMext",String.valueOf(bookInfoBean.pageIndex + 1));
                                if (isBookBToc){
                                    listener.getMestChapterText(chapterRead.chapter.cpContent);
                                }else {
                                    listener.getMestChapterText(chapterRead.chapter.body);
                                }
                            }
                            @Override
                            public void onError(Throwable e) {}
                            @Override
                            public void onComplete() {}
                        });
            }
        }
        return str;
    }

    @Override
    public int getChapterIndex() {
        return bookInfoBean.pageIndex;
    }

    @Override
    public void setChapterIndex(int index) {
        bookInfoBean.pageIndex = index;
    }

    @Override
    public int getPageIndex() {
        return bookInfoBean.readIndex;
    }

    @Override
    public void setPageIndex(int index) {
        bookInfoBean.readIndex = index;
    }

    @Override
    public int getBookType() {
        return bookInfoBean.bookTybe;
    }

    @Override
    public int getChapterNum() {
        return bean.listBeans.size();
    }

    @Override
    public int getPageNum() {
        return bookInfoBean.pageSize;
    }

    @Override
    public void setPageNum(int num) {
        bookInfoBean.pageSize = num;
    }

    @Override
    public BookInfoBean getBookInfoBean() {
        return bookInfoBean;
    }


}
