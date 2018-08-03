package com.wxz.ebook.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wxz.ebook.R;
import com.wxz.ebook.bean.BooksByCats;
import com.wxz.ebook.view.adapter.BookMarketListAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookMarketDetailActivity extends AppCompatActivity {

    private List<BooksByCats.BooksBean> booksBeans;
    private RecyclerView recyclerView;
    private BookMarketListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_market_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        booksBeans = new ArrayList<>();
        recyclerView = findViewById(R.id.book_market_detail_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new BookMarketListAdapter(this,booksBeans);
        recyclerView.setAdapter(adapter);
        Intent intent =getIntent();
        BooksByCats booksByCats = (BooksByCats) intent.getSerializableExtra("booksByCats");
        if (booksByCats != null){
            booksBeans.addAll(booksByCats.books);
            adapter.notifyDataSetChanged();
        }
    }

}
