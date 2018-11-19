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
    private OnItemClickListener onItemClickListener;

    public BookMarketClassAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_market_class_view, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int index) {
        holder.bookMarketClassView.setData(stringList.get(index));
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
        }
    }

    public interface OnItemClickListener{
        void setTitleOnClick(int index,View v);
        void setItemOnClick(int index,int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener = onItemClickListener;
    }
}
