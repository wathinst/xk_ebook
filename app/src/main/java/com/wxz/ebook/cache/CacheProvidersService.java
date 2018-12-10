package com.wxz.ebook.cache;

import com.wxz.ebook.bean.AutoComplete;
import com.wxz.ebook.bean.BookChapter;
import com.wxz.ebook.bean.BookDetail;
import com.wxz.ebook.bean.BookMixAToc;
import com.wxz.ebook.bean.BookSummary;
import com.wxz.ebook.bean.BookUpdated;
import com.wxz.ebook.bean.BooksByCats;
import com.wxz.ebook.bean.ChapterRead;
import com.wxz.ebook.bean.HotReview;
import com.wxz.ebook.bean.HotWord;
import com.wxz.ebook.bean.Recommend;
import com.wxz.ebook.bean.SearchDetail;

import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;
import okhttp3.ResponseBody;
import retrofit2.Call;

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
    Observable<HotWord> getHotWord(Observable<HotWord> hotWordObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<AutoComplete> getAutoComplete(Observable<AutoComplete> autoCompleteObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<SearchDetail> getSearchResult(Observable<SearchDetail> searchDetailObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<ResponseBody> getImg(Observable<ResponseBody> responseBodyObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<BookChapter> getBookChapter(Observable<BookChapter> bookChapterObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<List<BookUpdated.Updated>> getBookUpdated(Observable<List<BookUpdated.Updated>> bookUpdatedObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<List<BookSummary.SummaryBean>> getBookSummary(Observable<List<BookSummary.SummaryBean>> bookSummaryObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);
}
