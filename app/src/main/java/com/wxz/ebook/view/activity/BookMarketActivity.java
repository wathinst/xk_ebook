package com.wxz.ebook.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wxz.ebook.R;
import com.wxz.ebook.api.BookApi;
import com.wxz.ebook.bean.BooksByCats;
import com.wxz.ebook.cache.CacheProviders;
import com.wxz.ebook.view.adapter.BookMarketListAdapter;
import com.wxz.ebook.view.adapter.BookMarketSubListAdapter;
import com.wxz.ebook.view.view.BookMarketClassView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import okhttp3.OkHttpClient;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;

public class BookMarketActivity extends AppCompatActivity {

    private BookMarketClassView city_class;
    private BookMarketClassView fantasy_class;
    private BookMarketClassView xianXia_class;
    private BookMarketClassView military_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_market);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
        initData();
        setClick();
    }

    private void initView(){
        city_class = findViewById(R.id.book_market_class_view_city);
        fantasy_class = findViewById(R.id.book_market_class_view_fantasy);
        xianXia_class = findViewById(R.id.book_market_class_view_xianXia);
        military_class = findViewById(R.id.book_market_class_view_military);
    }

    private void initData(){
        city_class.setData("都市");
        fantasy_class.setData("玄幻");
        xianXia_class.setData("仙侠");
        military_class.setData("军事");
    }

    private void setClick(){
        city_class.setClickListener(new BookMarketClassView.Listener() {
            @Override
            public void setTitleOnClick(View v) {

            }

            @Override
            public void setItemOnClick(int position) {

            }
        });
        fantasy_class.setClickListener(new BookMarketClassView.Listener() {
            @Override
            public void setTitleOnClick(View v) {

            }

            @Override
            public void setItemOnClick(int position) {

            }
        });
        xianXia_class.setClickListener(new BookMarketClassView.Listener() {
            @Override
            public void setTitleOnClick(View v) {

            }

            @Override
            public void setItemOnClick(int position) {

            }
        });
        military_class.setClickListener(new BookMarketClassView.Listener() {
            @Override
            public void setTitleOnClick(View v) {

            }

            @Override
            public void setItemOnClick(int position) {

            }
        });
    }

}
