package com.wxz.ebook.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.wxz.ebook.R;
import com.wxz.ebook.api.BookApi;
import com.wxz.ebook.bean.AutoComplete;
import com.wxz.ebook.bean.SearchBean;
import com.wxz.ebook.bean.SearchDetail;
import com.wxz.ebook.cache.CacheProviders;
import com.wxz.ebook.tool.sql.SearchHistoryHelper;
import com.wxz.ebook.tool.sql.SearchHotHelper;
import com.wxz.ebook.view.adapter.BookSearchHistoryAdapter;
import com.wxz.ebook.view.adapter.BookSearchHotTagAdapter;
import com.wxz.ebook.view.adapter.BookSearchResultAdapter;
import com.wxz.ebook.view.adapter.BookSearchWordAdapter;
import com.wxz.ebook.view.ui.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import okhttp3.OkHttpClient;

public class BookSearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView search_hot;
    private RecyclerView search_history;
    private RecyclerView search_word;
    private RecyclerView search_result;
    private ImageView search_hot_icon;
    private ImageView search_history_icon;
    private NestedScrollView scrollView;
    private List<String> hotTagList,historyList,wordList;
    private BookSearchHotTagAdapter hotTagAdapter;
    private FlowLayoutManager flowLayoutManager;
    private SearchHistoryHelper historyHelper;
    private SearchHotHelper hotHelper;
    private BookSearchHistoryAdapter historyAdapter;
    private BookSearchWordAdapter wordAdapter;
    private BookSearchResultAdapter resultAdapter;
    private List<SearchDetail.SearchBooks> searchBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        //initData();
        //setOnClick();
    }

    private void initView(){
        search_hot = findViewById(R.id.book_search_hot_recycler_view);
        search_history = findViewById(R.id.book_search_history_recycler_view);
        search_word = findViewById(R.id.book_search_word_recycler_view);
        search_result = findViewById(R.id.book_search_result_recycler_view);
        search_hot_icon = findViewById(R.id.book_search_hot_icon);
        search_history_icon = findViewById(R.id.book_search_history_icon);
        scrollView = findViewById(R.id.book_search_body_view);
    }

    private void initData(){
        hotTagList = new ArrayList<>();
        historyList = new ArrayList<>();
        wordList = new ArrayList<>();
        searchBooks = new ArrayList<>();

        hotHelper = new SearchHotHelper(this);
        getHotWordData();

        historyHelper = new SearchHistoryHelper(this);
        getHistoryData();

        hotTagAdapter = new BookSearchHotTagAdapter(hotTagList);
        flowLayoutManager = new FlowLayoutManager(this,true);
        search_hot.setLayoutManager(flowLayoutManager);
        search_hot.setAdapter(hotTagAdapter);

        historyAdapter = new BookSearchHistoryAdapter(historyList);
        search_history.setLayoutManager(new LinearLayoutManager(this));
        search_history.setAdapter(historyAdapter);

        wordAdapter = new BookSearchWordAdapter(wordList);
        search_word.setLayoutManager(new LinearLayoutManager(this));
        search_word.setAdapter(wordAdapter);

        wordAdapter = new BookSearchWordAdapter(wordList);
        search_word.setLayoutManager(new LinearLayoutManager(this));
        search_word.setAdapter(wordAdapter);

        resultAdapter = new BookSearchResultAdapter(this,searchBooks);
        search_result.setLayoutManager(new LinearLayoutManager(this));
        search_result.setAdapter(resultAdapter);
    }

    private void setOnClick(){
        search_hot_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> hotTagTail = new ArrayList<>(hotTagList.subList(0,flowLayoutManager.getHideIndex()));
                List<String> hotTagHead = new ArrayList<>(hotTagList.subList(flowLayoutManager.getHideIndex(), hotTagList.size()));
                hotTagList.clear();
                hotTagList.addAll(hotTagHead);
                hotTagList.addAll(hotTagTail);
                hotTagAdapter.notifyDataSetChanged();
            }
        });
        search_history_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyHelper.deletaAll();
            }
        });
        hotTagAdapter.setOnItemClickListener(new BookSearchHotTagAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                searchView.setQuery(hotTagList.get(position),true);
            }
        });
        historyAdapter.setOnItemClickListener(new BookSearchHistoryAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                searchView.setQuery(historyList.get(position),true);
            }

            @Override
            public void onClearClick(int position) {
                historyHelper.deleteByTitle(historyList.get(position));
                getHistoryData();
                historyAdapter.notifyDataSetChanged();
            }
        });
        wordAdapter.setOnItemClickListener(new BookSearchWordAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                searchView.setQuery(wordList.get(position),true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.book_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_view);
        searchView = (SearchView)searchItem.getActionView();
        //searchView.setIconifiedByDefault(true);
        searchView.onActionViewExpanded();
        searchView.setQueryHint("输入书名或作者名");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                doSearch(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    scrollView.setVisibility(View.VISIBLE);
                    search_word.setVisibility(View.GONE);
                    search_result.setVisibility(View.GONE);
                } else {
                    getAutoCompleteData(s);
                    scrollView.setVisibility(View.GONE);
                    search_result.setVisibility(View.GONE);
                    search_word.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void doSearch(String s){
        SearchBean searchBean = new SearchBean();
        searchBean.title = s;
        historyHelper.insert(searchBean);
        getSearchResultData(s);
        search_result.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        search_word.setVisibility(View.GONE);
        getHistoryData();
        historyAdapter.notifyDataSetChanged();
    }

    private void getHistoryData(){
        historyList.clear();
        List<SearchBean>  searchHistoryBeans = historyHelper.getAll(null,"_id DESC");
        for (int i =0 ; i< searchHistoryBeans.size();i++){
            historyList.add(searchHistoryBeans.get(i).title);
        }
    }

    private void getHotWordData(){
        hotTagList.clear();
        List<SearchBean> searchHotBeans = hotHelper.getAll(null,null);
        for (int i =0 ; i< searchHotBeans.size();i++){
            hotTagList.add(searchHotBeans.get(i).title);
        }
    }

    private void getAutoCompleteData(String complete){
        String titleName = "XKRead_getAutoComplete" + complete;
        Observable<AutoComplete> autoCompleteObservable = BookApi.getInstance(new OkHttpClient()).getAutoComplete(complete);
        CacheProviders.getUserCache(getBaseContext()).getAutoComplete(autoCompleteObservable,new DynamicKey(titleName),new EvictDynamicKey(true))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AutoComplete>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onNext(AutoComplete autoComplete) {
                        wordList.clear();
                        wordList.addAll(autoComplete.keywords);
                        wordAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onError(Throwable e) { }
                    @Override
                    public void onComplete() { }
                });
    }

    private void getSearchResultData(String complete){
        String titleName = "XKRead_getAutoComplete" + complete;
        Observable<SearchDetail> searchDetailObservable = BookApi.getInstance(new OkHttpClient()).getSearchResult(complete);
        CacheProviders.getUserCache(getBaseContext()).getSearchResult(searchDetailObservable,new DynamicKey(titleName),new EvictDynamicKey(true))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onNext(SearchDetail searchDetail) {
                        searchBooks.clear();
                        searchBooks.addAll(searchDetail.books);
                        resultAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onError(Throwable e) { }
                    @Override
                    public void onComplete() { }
                });
    }


}
