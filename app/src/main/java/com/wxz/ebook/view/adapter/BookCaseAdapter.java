package com.wxz.ebook.view.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxz.ebook.R;
import com.wxz.ebook.bean.BookInfoBean;
import com.wxz.ebook.ui.bookUI.BookCaseView;

import java.util.List;

public class BookCaseAdapter extends RecyclerView.Adapter<BookCaseAdapter.ViewHolder> {

    private List<BookInfoBean> bookInfoBeans;
    private OnItemClickListener onItemClickListener;
    public BookCaseAdapter(List<BookInfoBean> bookInfoBeans){
        this.bookInfoBeans = bookInfoBeans;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_case_list_item,parent,false);
        ViewHolder holder;
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if(bookInfoBeans !=null&& bookInfoBeans.size()>0){
            holder.bookCaseView.setName(bookInfoBeans.get(position).getName());
            if( onItemClickListener!= null){
                holder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onClick(position);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onLongClick(position);
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if(bookInfoBeans !=null&& bookInfoBeans.size()>0){
            return bookInfoBeans.size();
        }else {
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        BookCaseView bookCaseView;
        ViewHolder(View view) {
            super(view);
            bookCaseView = view.findViewById(R.id.book_case_list_item);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener = onItemClickListener;
    }
}
