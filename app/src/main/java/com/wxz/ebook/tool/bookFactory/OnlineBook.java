package com.wxz.ebook.tool.bookFactory;

import android.content.Context;
import android.util.Log;
import com.wxz.ebook.api.BookApi;
import com.wxz.ebook.bean.BookInfoBean;
import com.wxz.ebook.bean.BookMixAToc;
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
    private BookMixAToc mBookMixAToc;
    private Context context;
    private OnlineBookListener listener;

    @Override
    public void init(Context context,BookInfoBean bookInfoBean) {
        this.context = context;
        this.bookInfoBean = bookInfoBean;
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
                        listener.getChapterList(mixATocToChapterList(bookMixAToc));
                    }
                    @Override
                    public void onError(Throwable e) { }
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
                    bookMixAToc.mixToc.chapters.get(i).title, i, 0);
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
        if (mBookMixAToc != null){
            str = mBookMixAToc.mixToc.chapters.get(bookInfoBean.pageIndex).title;
        }
        return str;
    }

    @Override
    public String getLastChapterName() {
        String str = "";
        if(mBookMixAToc!= null && bookInfoBean.pageIndex > 0){
            str = mBookMixAToc.mixToc.chapters.get(bookInfoBean.pageIndex - 1).title;
        }
        return str;
    }

    @Override
    public String getMextChapterName() {
        String str = "";
        if (mBookMixAToc!= null && bookInfoBean.pageIndex + 1 < mBookMixAToc.mixToc.chapters.size()){
            str = mBookMixAToc.mixToc.chapters.get(bookInfoBean.pageIndex + 1).title;
        }
        return str;
    }

    @Override
    public String getThisChapterText() {
        String str = "";
        if (mBookMixAToc != null){
            //String titleName1 = "ChapterRead"+ bookInfoBean.bookId + "no" +
                  //  mBookMixAToc.mixToc.chapters.get(bookInfoBean.pageIndex).id;
            String titleName1 = mBookMixAToc.mixToc.chapters.get(bookInfoBean.pageIndex).link;
            Observable<ChapterRead> chapterReadObservable = BookApi.getInstance(new OkHttpClient())
                    .getChapterRead(mBookMixAToc.mixToc.chapters.get(bookInfoBean.pageIndex).link);
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
                            listener.getThisChapterText(chapterRead.chapter.body);
                        }
                        @Override
                        public void onError(Throwable e) {}
                        @Override
                        public void onComplete() {}
                    });
        }
        return str;
    }

    @Override
    public String getLastChapterText() {
        String str = "";
        if(mBookMixAToc!= null && bookInfoBean.pageIndex > 0){
            //String titleName1 = "ChapterRead"+ bookInfoBean.bookId + "no" +
                    //mBookMixAToc.mixToc.chapters.get(bookInfoBean.pageIndex - 1).id;
            String titleName1 = mBookMixAToc.mixToc.chapters.get(bookInfoBean.pageIndex - 1).link;
                    Observable<ChapterRead> chapterReadObservable = BookApi.getInstance(new OkHttpClient())
                    .getChapterRead(mBookMixAToc.mixToc.chapters.get(bookInfoBean.pageIndex  - 1).link);
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
                            listener.getLastChapterText(chapterRead.chapter.body);
                        }
                        @Override
                        public void onError(Throwable e) {}
                        @Override
                        public void onComplete() {}
                    });
        }
        return str;
    }

    @Override
    public String getMextChapterText() {
        String str = "";
        if (mBookMixAToc!= null && bookInfoBean.pageIndex + 1 < mBookMixAToc.mixToc.chapters.size()){
            //String titleName1 = "ChapterRead"+ bookInfoBean.bookId + "no" +
                   // mBookMixAToc.mixToc.chapters.get(bookInfoBean.pageIndex + 1).id;
            String titleName1 = mBookMixAToc.mixToc.chapters.get(bookInfoBean.pageIndex + 1).link;
            Observable<ChapterRead> chapterReadObservable = BookApi.getInstance(new OkHttpClient())
                    .getChapterRead(mBookMixAToc.mixToc.chapters.get(bookInfoBean.pageIndex + 1).link);
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
                            listener.getMestChapterText(chapterRead.chapter.body);
                        }
                        @Override
                        public void onError(Throwable e) {}
                        @Override
                        public void onComplete() {}
                    });
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
        return mBookMixAToc.mixToc.chapters.size();
    }

    @Override
    public BookInfoBean getBookInfoBean() {
        return bookInfoBean;
    }


}
