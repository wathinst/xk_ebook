package com.wxz.ebook.view.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wxz.ebook.R;

import java.util.List;

public class BookSearchWordAdapter extends RecyclerView.Adapter<BookSearchWordAdapter.MyHolder> {

    private List<String> tags;
    private OnItemClickListener onItemClickListener;

    public BookSearchWordAdapter(List<String> tags) {
        this.tags = tags;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_search_word, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.item_title.setText(tags.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView item_title;
        public MyHolder(View itemView) {
            super(itemView);
            item_title = itemView.findViewById(R.id.item_book_search_word_title);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener = onItemClickListener;
    }
}
