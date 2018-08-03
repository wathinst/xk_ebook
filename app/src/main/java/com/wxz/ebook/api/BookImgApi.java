package com.wxz.ebook.api;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookImgApi {
    public static BookImgApi instance;

    private BookApiService service;

    public BookImgApi(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://statics.zhuishushenqi.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(okHttpClient)
                .build();
        service = retrofit.create(BookApiService.class);
    }

    public static BookImgApi getInstance(OkHttpClient okHttpClient) {
        if (instance == null)
            instance = new BookImgApi(okHttpClient);
        return instance;
    }

    public Observable<ResponseBody> getImg(String imgUrl){
        return instance.service.getImg(imgUrl);
    }
}
