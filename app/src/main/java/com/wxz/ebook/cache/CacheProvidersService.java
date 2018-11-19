package com.wxz.ebook.cache;

import com.wxz.ebook.bean.BookDetail;
import com.wxz.ebook.bean.BookMixAToc;
import com.wxz.ebook.bean.BooksByCats;
import com.wxz.ebook.bean.ChapterRead;
import com.wxz.ebook.bean.HotReview;
import com.wxz.ebook.bean.Recommend;

import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;
import okhttp3.ResponseBody;

public interface CacheProvidersService {
    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<BooksByCats> getRepos(Observable<BooksByCats> booksByCatsObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<BookDetail> getBookDetails(Observable<BookDetail> bookDetailObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<BookMixAToc> getBookMixAToc(Observable<BookMixAToc> bookMixATocObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<HotReview> getHotReview(Observable<HotReview> hotReviewObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Recommend> getRecommendBook(Observable<Recommend> recommendObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<ChapterRead> getChapterRead(Observable<ChapterRead> chapterReadObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<ResponseBody> getImg(Observable<ResponseBody> responseBodyObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);
}
