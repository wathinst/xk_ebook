package com.wxz.ebook.cache;

import com.wxz.ebook.bean.BooksByCats;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;
import okhttp3.ResponseBody;

public interface CacheProvidersService {
    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<BooksByCats> getRepos(Observable<BooksByCats> booksByCatsObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    Observable<ResponseBody> getImg(Observable<ResponseBody> responseBodyObservable, DynamicKey userName, EvictDynamicKey evictDynamicKey);
}
