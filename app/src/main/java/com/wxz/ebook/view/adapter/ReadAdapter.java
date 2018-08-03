package com.wxz.ebook.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wxz.ebook.R;
import com.wxz.ebook.bean.ChapterListBean;

import java.util.List;

public class ReadAdapter extends RecyclerView.Adapter<ReadAdapter.MyHolder> {

    private Context context;
    private int currentChapter;
    private List<ChapterListBean.ListBean> listBeans;
    private OnItemClickListener onItemClickListener;
    private int textColor;

    public ReadAdapter(Context context, List<ChapterListBean.ListBean> listBeans) {
        this.context = context;
        this.listBeans = listBeans;
        currentChapter = 0;
        textColor = ContextCompat.getColor(context, R.color.fontBlack);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.read_list_nav_item, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.read_text.setText(listBeans.get(position).name);
        if (currentChapter == position) {
            holder.read_text.setTextColor(ContextCompat.getColor(context, R.color.fontSelect));
        }else {
            holder.read_text.setTextColor(textColor);
        }
        if( onItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listBeans.size();
    }

    public void setCurrentChapter(int chapter) {
        currentChapter = chapter;
        notifyDataSetChanged();
    }

    public void setChapTextColor(int textColor){
        this.textColor = textColor;
    }

    public int getCurrentChapter(){
        return currentChapter;
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView read_text;
        public MyHolder(View itemView) {
            super(itemView);
            read_text = itemView.findViewById(R.id.read_book_list_name);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener = onItemClickListener;
    }
}
