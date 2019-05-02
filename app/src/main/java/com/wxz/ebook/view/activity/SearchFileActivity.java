package com.wxz.ebook.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.wxz.ebook.R;
import com.wxz.ebook.asyncTask.FileAsyncTask;
import com.wxz.ebook.base.BaseAdapter;
import com.wxz.ebook.bean.DocBean;
import com.wxz.ebook.mvvm.modle.SearchFileModle;
import com.wxz.ebook.mvvm.vm.SearchFileVm;
import com.wxz.ebook.view.adapter.FileListAdapter;
import com.wxz.ebook.view.adapter.SearchFileAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFileActivity extends AppCompatActivity implements FileAsyncTask.OnFileAsyncTaskListener{

    //List<DocBean> docBeanList;
    List<SearchFileModle> modles;
    RecyclerView recyclerView;
    //FileListAdapter adapter;
    private SearchFileAdapter adapter;
    FileAsyncTask fileAsyncTask;
    private SearchFileVm vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vm = new SearchFileVm();

        recyclerView = findViewById(R.id.file_list);
        //docBeanList = new ArrayList<>();
        modles = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //adapter = new FileListAdapter(docBeanList);
        adapter = new SearchFileAdapter(this,modles);
        recyclerView.setAdapter(adapter);
        fileAsyncTask = new FileAsyncTask();
        fileAsyncTask.execute(this);
        fileAsyncTask.setOnFileAsyncTaskListener(this);
        /*adapter.setOnItemClickListener(new FileListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(SearchFileActivity.this, ReadPageActivity.class);
                intent.putExtra("docBean", docBeanList.get(position));
                startActivityForResult(intent,1);
                finish();
                //startActivity(intent);
            }
        });*/
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(Object data, int position) {
                SearchFileModle modle = (SearchFileModle)data;
                Log.e("BaseAdapter",modle.getName());
            }
        });

    }

    @Override
    public void fileTaskListener(List<DocBean> docBeanModels) {
        modles.clear();
        modles.addAll(vm.getModleList(docBeanModels));
        adapter.notifyDataSetChanged();
    }
}
