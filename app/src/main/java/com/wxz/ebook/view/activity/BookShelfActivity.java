package com.wxz.ebook.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.wxz.ebook.R;
import com.wxz.ebook.bean.BookBean;
import com.wxz.ebook.bean.BookInfoBean;
import com.wxz.ebook.tool.ComparatorBookInfo;
import com.wxz.ebook.tool.FileHelper;
import com.wxz.ebook.view.adapter.BookCaseAdapter;

import java.util.Collections;
import java.util.List;

public class BookShelfActivity extends AppCompatActivity {

    FileHelper helper;
    List<BookInfoBean> bookInfoBeans;
    RecyclerView bookCase;
    BookCaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_shelf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bookCase = findViewById(R.id.book_case_list);
        GridLayoutManager manager = new GridLayoutManager(this,3);
        bookCase.setLayoutManager(manager);

        helper =new FileHelper(this);
        bookInfoBeans = helper.getAll(null,"date DESC");//DESC,ASC
        adapter = new BookCaseAdapter(bookInfoBeans);
        bookCase.setAdapter(adapter);

        adapter.setOnItemClickListener(new BookCaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(BookShelfActivity.this, ReadPageActivity.class);
                intent.putExtra("bookInfoBean", bookInfoBeans.get(position));
                startActivityForResult(intent,2);
            }
            @Override
            public void onLongClick(int position) {
                BookInfoBean bookBean = bookInfoBeans.get(position);
                helper.delete(bookBean);
                bookInfoBeans.remove(bookBean);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(BookShelfActivity.this, SearchFileActivity.class);
            startActivityForResult(intent,1);
            return true;
        }else if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            BookInfoBean book = (BookInfoBean) data.getSerializableExtra("rBookInfoBean");
            Log.e("name",book.getName());
            for (int i=0;i<bookInfoBeans.size();i++){
                if(bookInfoBeans.get(i).getId().equals(book.getId())){
                    bookInfoBeans.set(i,book);
                }
            }
            ComparatorBookInfo comparatorBookInfo = new ComparatorBookInfo();
            Collections.sort(bookInfoBeans,comparatorBookInfo);
            adapter.notifyDataSetChanged();
        }
        if(requestCode == 1){
            BookInfoBean book = (BookInfoBean) data.getSerializableExtra("rBookInfoBean");
            Log.e("name",book.getName());
            bookInfoBeans.add(book);
            ComparatorBookInfo comparatorBookInfo = new ComparatorBookInfo();
            Collections.sort(bookInfoBeans,comparatorBookInfo);
            adapter.notifyDataSetChanged();
        }
    }

}
