package com.wxz.ebook.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wxz.ebook.R;
import com.wxz.ebook.view.view.BookMarketClassView;
import java.util.List;

public class BookMarketClassAdapter extends RecyclerView.Adapter<BookMarketClassAdapter.MyHolder> {

    private Context context;
    private List<String> stringList;

    public BookMarketClassAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_market_class_view_item, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.bookMarketClassView.setData(stringList.get(position));
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        BookMarketClassView bookMarketClassView;
        public MyHolder(View itemView) {
            super(itemView);
            bookMarketClassView = itemView.findViewById(R.id.book_market_class_view);
            bookMarketClassView.setClickListener(new BookMarketClassView.Listener() {
                @Override
                public void setTitleOnClick(View v) {

                }

                @Override
                public void setItemOnClick(int position) {

                }
            });
        }
    }
}
