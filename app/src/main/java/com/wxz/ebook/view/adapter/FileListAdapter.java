package com.wxz.ebook.view.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxz.ebook.R;
import com.wxz.ebook.bean.DocBean;
import com.wxz.ebook.tool.DateUnit;
import com.wxz.ebook.tool.SizeUnit;

import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

    private List<DocBean> docBeans;
    private OnItemClickListener onItemClickListener;
    public FileListAdapter(List<DocBean> docBeans){
        this.docBeans = docBeans;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_file_list,parent,false);
        ViewHolder holder;
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if(docBeans !=null&& docBeans.size()>0){
            holder.text.setVisibility(View.GONE);
            holder.item.setVisibility(View.VISIBLE);
            holder.name.setText(docBeans.get(position).getName());
            SizeUnit sizeUnit = new SizeUnit();
            holder.size.setText(sizeUnit.getSizeStr(docBeans.get(position).getSize()));
            DateUnit dateUnit = new DateUnit();
            holder.date.setText(dateUnit.getDateStr(docBeans.get(position).getDateModified()));
            if( onItemClickListener!= null){
                holder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onClick(position);
                    }
                });
            }
        }else {
            holder.text.setVisibility(View.VISIBLE);
            holder.item.setVisibility(View.GONE);
            holder.text.setText("扫描中……");
        }
    }

    @Override
    public int getItemCount() {
        if(docBeans !=null&& docBeans.size()>0){
            return docBeans.size();
        }else {
            return 1;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
         TextView name,size,date,item_imported,text;
         LinearLayout item;
         CheckBox item_import;
         ViewHolder(View view) {
             super(view);
             item = view.findViewById(R.id.search_file_list_item);
             name = view.findViewById(R.id.search_file_list_item_name);
             size = view.findViewById(R.id.search_file_list_item_size);
             date = view.findViewById(R.id.search_file_list_item_date);
             item_import = view.findViewById(R.id.search_file_list_item_import);
             item_imported = view.findViewById(R.id.search_file_list_item_imported);
             text = view.findViewById(R.id.search_file_list_item_text);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener = onItemClickListener;
    }
}
