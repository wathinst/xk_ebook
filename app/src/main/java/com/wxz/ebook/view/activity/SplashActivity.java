package com.wxz.ebook.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.wxz.ebook.R;
import com.wxz.ebook.api.BookApi;
import com.wxz.ebook.bean.HotWord;
import com.wxz.ebook.bean.SearchBean;
import com.wxz.ebook.cache.CacheProviders;
import com.wxz.ebook.tool.sql.SearchHotHelper;
import com.wxz.ebook.tool.utils.AppUtils;
import com.wxz.ebook.tool.utils.DateUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import okhttp3.OkHttpClient;

public class SplashActivity extends AppCompatActivity {
    private static long SPLASH_LENGTH = 1000;
    private Handler handler = new Handler();
    private SearchHotHelper hotHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppUtils.init(getApplicationContext());

        hotHelper = new SearchHotHelper(AppUtils.getAppContext());
        List<SearchBean> beans = hotHelper.getAll(null,null);
        if (beans!=null){
            if(beans.size() > 0 ){
                DateUtil dateUtil = new DateUtil();
                if (!dateUtil.isToday(beans.get(0).date)){
                    getHotWordData();
                }
            }else {
                getHotWordData();
            }
        }else {
            getHotWordData();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_LENGTH);//1秒后跳转
    }

    private void getHotWordData(){
        hotHelper.deleteAll();
        String titleName = "XKRead_hotWord";
        Observable<HotWord> hotWordObservable = BookApi.getInstance(new OkHttpClient()).getHotWord();
        CacheProviders.getUserCache(getBaseContext()).getHotWord(hotWordObservable,new DynamicKey(titleName),new EvictDynamicKey(true))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotWord>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onNext(HotWord hotWord) {
                        for (int i=0;i<hotWord.hotWords.size();i++){
                            SearchBean bean = new SearchBean();
                            bean.id = i;
                            bean.title = hotWord.hotWords.get(i);
                            hotHelper.insert(bean);
                        }
                    }
                    @Override
                    public void onError(Throwable e) { }
                    @Override
                    public void onComplete() { }
                });
    }
}
