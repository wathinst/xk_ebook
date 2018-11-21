package com.wxz.ebook.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxz.ebook.R;
import com.wxz.ebook.view.adapter.BookMarketClassAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookMarketFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookMarketClassAdapter adapter;
    private List<String > stringList;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.content_book_market,container,false);
        context = view.getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //context.setSupportActionBar(toolbar);

        initView();
        initData();
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        adapter = new BookMarketClassAdapter(context,stringList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BookMarketClassAdapter.OnItemClickListener() {
            @Override
            public void setTitleOnClick(int index, View v) {

            }

            @Override
            public void setItemOnClick(int index, int position) {

            }
        });
    }

    private void initView(){
        recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.book_market_class_recyclerView);
    }

    private void initData(){
        stringList  = new ArrayList<>();
        stringList.add("都市");
        stringList.add("军事");
        stringList.add("科幻");
        stringList.add("玄幻");
        stringList.add("仙侠");
        stringList.add("职场");
    }
}
