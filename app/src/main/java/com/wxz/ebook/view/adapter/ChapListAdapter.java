package com.wxz.ebook.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.util.AsyncListUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wxz.ebook.R;
import com.wxz.ebook.bean.ChapterListBean;

public class ChapListAdapter extends RecyclerView.Adapter<ChapListAdapter.ViewHolder> {

    private Context context;
    private AsyncListUtil<ChapterListBean.ListBean> asyncListUtil;
    private OnItemClickListener onItemClickListener;
    public ChapListAdapter(Context context, AsyncListUtil<ChapterListBean.ListBean> asyncListUtil){
        this.context = context;
        this.asyncListUtil = asyncListUtil;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.read_list_nav_item,parent,false);
        ViewHolder holder;
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ChapterListBean.ListBean bean = asyncListUtil.getItem(position);
        if(bean!=null){
            holder.name.setText(bean.name);
            //holder.name.setTextColor(context.getResources().getColor(R.color.fontSelect));
            if( onItemClickListener!= null){
                holder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onClick(position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return asyncListUtil.getItemCount();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.read_book_list_name);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener = onItemClickListener;
    }
}
