package com.wxz.ebook.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import com.wxz.ebook.tool.utils.AppUtils;
import com.wxz.ebook.view.activity.AboutActivity;
import com.wxz.ebook.view.activity.JoinActivity;
import com.wxz.ebook.view.activity.ReadPageActivity;
import com.wxz.ebook.view.activity.SettingActivity;

import java.util.ArrayList;
import java.util.List;

public class BookMineListAdapter extends RecyclerView.Adapter<BookMineListAdapter.MyHolder> {

    private List<String> tags;
    private List<Bitmap> bitmaps;

    public BookMineListAdapter() {
        String mine_names[] = {"我的书籍","我的消息","设置中心","关于校咖","问题反馈","加入我们"};
        int mine_icons[] ={R.mipmap.ic_my_books,
                R.mipmap.ic_my_message,
                R.mipmap.ic_my_setting,
                R.mipmap.ic_about,
                R.mipmap.ic_feedback,
                R.mipmap.ic_my_mail};
        tags = new ArrayList<>();
        bitmaps = new ArrayList<>();
        for (int i=0;i<6;i++){
            tags.add(mine_names[i]);
            bitmaps.add(BitmapFactory.decodeResource(AppUtils.getResource(),mine_icons[i]));
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_mine, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.item_title.setText(tags.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position  == 2){
                    Intent intent = new Intent(holder.itemView.getContext(), SettingActivity.class);
                    holder.itemView.getContext().startActivity(intent);
                }else if(position  == 3){
                    Intent intent = new Intent(holder.itemView.getContext(), AboutActivity.class);
                    holder.itemView.getContext().startActivity(intent);
                }else if(position  == 5){
                    Intent intent = new Intent(holder.itemView.getContext(), JoinActivity.class);
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });
        holder.item_icon.setImageBitmap(bitmaps.get(position));
        if (position == tags.size()-1){
            holder.item_line.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView item_title;
        ImageView item_icon;
        View item_line;
        public MyHolder(View itemView) {
            super(itemView);
            item_title = itemView.findViewById(R.id.item_book_mine_title);
            item_icon = itemView.findViewById(R.id.item_book_mine_icon);
            item_line = itemView.findViewById(R.id.item_book_mine_line);
        }
    }
}
