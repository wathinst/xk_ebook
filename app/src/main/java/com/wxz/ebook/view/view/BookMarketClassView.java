package com.wxz.ebook.view.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wxz.ebook.R;
import com.wxz.ebook.api.BookApi;
import com.wxz.ebook.bean.BooksByCats;
import com.wxz.ebook.cache.CacheProviders;
import com.wxz.ebook.view.activity.BookDetailsActivity;
import com.wxz.ebook.view.adapter.BookMarketSubListAdapter;
import com.wxz.ebook.view.ui.recyclerUI.BetterRecyclerView;
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
import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;

public class BookMarketClassView extends FrameLayout {
    private LinearLayout title;
    private BetterRecyclerView recyclerView;
    private BookMarketSubListAdapter adapter;
    private List<BooksByCats.BooksBean> booksBeans;
    private TextView title_text;
    public BookMarketClassView(@NonNull Context context) {
        this(context,null);
    }

    public BookMarketClassView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BookMarketClassView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BookMarketClassView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.book_market_class_view,this,true);
        initView();
        booksBeans = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),HORIZONTAL,false);
        recyclerView.setLayoutManager(manager);
        adapter = new BookMarketSubListAdapter(getContext(),booksBeans);
        recyclerView.setAdapter(adapter);
        setOnClick();
    }

    private void initView() {
        title = findViewById(R.id.book_market_class_view_title);
        title_text = findViewById(R.id.book_market_class_view_title_text);
        recyclerView = findViewById(R.id.book_market_view_recycler_view);
    }

    public void setOnClick(){
        title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        adapter.setOnItemClickListener(new BookMarketSubListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getContext(), BookDetailsActivity.class);
                intent.putExtra("book_id",booksBeans.get(position)._id);
                intent.putExtra("book_img_url",booksBeans.get(position).cover);
                intent.putExtra("book_title",booksBeans.get(position).title);
                getContext().startActivity(intent);
            }
        });
    }

    public void setData(String str){
        title_text.setText(str);
        String titleName = "male" + "-" + "hot" + "-" + str + "-" + "" + "-" + "0" + "-" + "20";
        Observable<BooksByCats> books = BookApi.getInstance(new OkHttpClient()).
                getBooksByCats("male","hot",str,"",0,20);
        CacheProviders.getUserCache(getContext())
                .getRepos(books,new DynamicKey(titleName),new EvictDynamicKey(false))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BooksByCats>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(BooksByCats booksByCats) {
                        booksBeans.addAll(booksByCats.books.subList(0,10));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }
}