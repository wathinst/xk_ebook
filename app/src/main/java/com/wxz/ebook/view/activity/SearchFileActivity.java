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
import com.wxz.ebook.asyncTask.FileAsyncTask;
import com.wxz.ebook.bean.DocBean;
import com.wxz.ebook.view.adapter.FileListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFileActivity extends AppCompatActivity implements FileAsyncTask.OnFileAsyncTaskListener{

    List<DocBean> docBeanList;
    RecyclerView recyclerView;
    FileListAdapter adapter;
    FileAsyncTask fileAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.file_list);
        docBeanList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FileListAdapter(docBeanList);
        recyclerView.setAdapter(adapter);
        fileAsyncTask = new FileAsyncTask();
        fileAsyncTask.execute(this);
        fileAsyncTask.setOnFileAsyncTaskListener(this);
        adapter.setOnItemClickListener(new FileListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(SearchFileActivity.this, ReadPageActivity.class);
                intent.putExtra("docBean", docBeanList.get(position));
                startActivityForResult(intent,1);
                finish();
                //startActivity(intent);
            }
        });

    }

    @Override
    public void fileTaskListener(List<DocBean> docBeanModels) {
        this.docBeanList.addAll(docBeanModels);
        adapter.notifyDataSetChanged();
    }
}
