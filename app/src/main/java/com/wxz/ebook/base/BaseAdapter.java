package com.wxz.ebook.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxz.ebook.tool.bookFactory.Book;

import java.util.List;

/*********************************************************************************
 *                    The 996ICU License (996ICU)
 *                      Version 0.1, March 2019
 *
 *   PACKAGE is distributed under LICENSE with the following restriction:
 *
 *   The above license is only granted to entities that act in concordance
 *   with local labor laws. In addition, the following requirements must be
 *   observed:
 *
 *   * The licensee must not, explicitly or implicitly, request or schedule
 *     their employees to work more than 45 hours in any single week.
 *   * The licensee must not, explicitly or implicitly, request or schedule
 *     their employees to be at work consecutively for 10 hours.
 *   *********************************************************************************
 *                             类信息
 *
 *   开发者：  wxz
 *   日  期：  2019年04月11日
 *   描  述：    
 *   *********************************************************************************/
public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {

    protected Context context;
    protected List<? extends BaseModle> mDatas;
    protected int layoutId;
    protected LayoutInflater layoutInflater;
    private int variableId;
    private OnItemClickListener onItemClickListener;

    public BaseAdapter(Context context, List<? extends BaseModle> mDatas, int layoutId, int variableId) {
        this.context = context;
        this.mDatas = mDatas;
        this.layoutId = layoutId;
        this.variableId = variableId;
    }

    public List<? extends BaseModle> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<? extends BaseModle> mDatas) {
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),layoutId,parent,false);
        ViewHolder holder = new ViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (mDatas != null && mDatas.size() > 0){
            holder.binding.setVariable(variableId,mDatas.get(position));
            loadData(holder,position,mDatas.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null){
                        onItemClickListener.onClick(mDatas.get(position),position);
                    }
                }
            });
        }else {
            noData(holder,position);
        }
        holder.binding.executePendingBindings();
    }

    protected void loadData(@NonNull ViewHolder holder, final int position, BaseModle data){
    }

    protected void noData(@NonNull ViewHolder holder, final int position){

    }



    @Override
    public int getItemCount() {
        return mDatas == null || mDatas.size() == 0 ? 1 : mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public ViewHolder(View view) {
            super(view);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onClick(Object data , int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
