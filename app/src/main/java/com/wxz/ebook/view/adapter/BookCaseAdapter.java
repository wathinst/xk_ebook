package com.wxz.ebook.view.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxz.ebook.R;
import com.wxz.ebook.bean.BookInfoBean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class BookCaseAdapter extends RecyclerView.Adapter<BookCaseAdapter.ViewHolder> {

    private List<BookInfoBean> bookInfoBeans;
    private OnItemClickListener onItemClickListener;
    private String fileTypeStr[] = {"TXT","DOC","DOCX","PDF","EPUB"};
    public BookCaseAdapter(List<BookInfoBean> bookInfoBeans){
        this.bookInfoBeans = bookInfoBeans;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_case,parent,false);
        ViewHolder holder;
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if(bookInfoBeans !=null&& bookInfoBeans.size()>0){
            if (bookInfoBeans.get(position).bookTybe == 1){
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(bookInfoBeans.get(position).imgPath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap  = BitmapFactory.decodeStream(fis);
                if (bitmap!=null){
                    holder.bookImg.setImageBitmap(bitmap);
                    holder.bookUpdata.setVisibility(View.INVISIBLE);
                    if (bookInfoBeans.get(position).isUpdatad == 1){
                        holder.bookUpdata.setVisibility(View.VISIBLE);
                    }else {
                        holder.bookUpdata.setVisibility(View.GONE);
                    }
                }
            }else {
                holder.bookTitle.setText(bookInfoBeans.get(position).name);
                holder.bookType.setText(fileTypeStr[bookInfoBeans.get(position).fileTybe]);
                holder.bookUpdata.setVisibility(View.INVISIBLE);
            }
            if( onItemClickListener!= null){
                holder.bookView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onClick(position);
                    }
                });
                holder.bookView.setOnLongClickListener(new View.OnLongClickListener() {
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
        ImageView bookImg,bookUpdata;
        TextView bookTitle,bookType;
        View bookView;
        ViewHolder(View view) {
            super(view);
            bookImg = view.findViewById(R.id.book_case_image);
            bookUpdata = view.findViewById(R.id.book_case_updata);
            bookTitle = view.findViewById(R.id.book_case_title);
            bookType = view.findViewById(R.id.book_case_type);
            bookView = view.findViewById(R.id.book_case_view);
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
