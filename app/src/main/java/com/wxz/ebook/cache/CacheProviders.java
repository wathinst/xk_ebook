package com.wxz.ebook.cache;

import android.content.Context;
import java.util.Objects;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

public class CacheProviders {

    private static CacheProvidersService cacheProvidersService;

    public synchronized static CacheProvidersService getUserCache(Context context) {
        if (cacheProvidersService == null) {
            cacheProvidersService = new RxCache.Builder()
                    .persistence(Objects.requireNonNull(context.getExternalCacheDir()), new GsonSpeaker())//缓存文件的配置、数据的解析配置
                    .using(CacheProvidersService.class);//这些配置对应的缓存接口
        }
        return cacheProvidersService;
    }
}
