package com.wxz.ebook.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxz.ebook.R;
import com.wxz.ebook.bean.BookInfoBean;
import com.wxz.ebook.tool.ComparatorBookInfo;
import com.wxz.ebook.tool.utils.FileHelper;
import com.wxz.ebook.view.activity.ReadPageActivity;
import com.wxz.ebook.view.adapter.BookCaseAdapter;
import com.wxz.ebook.view.adapter.BookCaseViewAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BookShelfFragment extends Fragment {
    FileHelper helper;
    List<BookInfoBean> bookInfoBeans;
    RecyclerView bookCase;
    BookCaseAdapter adapter;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.content_book_shelf,container,false);
        context = view.getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //context.setSupportActionBar(toolbar);

        bookCase = Objects.requireNonNull(getActivity()).findViewById(R.id.book_case_list);
        GridLayoutManager manager = new GridLayoutManager(context,3);
        bookCase.setLayoutManager(manager);

        helper =new FileHelper(context);
        bookInfoBeans = helper.getAll(null,"date DESC");//DESC,ASC
        adapter = new BookCaseAdapter(bookInfoBeans);
        bookCase.setAdapter(adapter);

        adapter.setOnItemClickListener(new BookCaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(context, ReadPageActivity.class);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            BookInfoBean book = (BookInfoBean) data.getSerializableExtra("rBookInfoBean");
            for (int i=0;i<bookInfoBeans.size();i++){
                if(bookInfoBeans.get(i).id.equals(book.id)){
                    bookInfoBeans.set(i,book);
                }
            }
            ComparatorBookInfo comparatorBookInfo = new ComparatorBookInfo();
            Collections.sort(bookInfoBeans,comparatorBookInfo);
            adapter.notifyDataSetChanged();
        }
        if(requestCode == 1){
            BookInfoBean book = (BookInfoBean) data.getSerializableExtra("rBookInfoBean");
            bookInfoBeans.add(book);
            ComparatorBookInfo comparatorBookInfo = new ComparatorBookInfo();
            Collections.sort(bookInfoBeans,comparatorBookInfo);
            adapter.notifyDataSetChanged();
        }
    }
}
