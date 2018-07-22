package com.wxz.ebook.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.wxz.ebook.R;
import com.wxz.ebook.bean.ChapterListBean;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

public class ChapAdapter extends EasyRVAdapter<ChapterListBean.ListBean> {

    private int currentChapter;

    public ChapAdapter(Context context, List<ChapterListBean.ListBean> list, int currentChapter) {
        super(context, list, R.layout.read_list_nav_item);
        this.currentChapter = currentChapter;
    }

    @Override
    protected void onBindData(EasyRVHolder viewHolder, int position, ChapterListBean.ListBean item) {
        TextView name = viewHolder.getView(R.id.read_book_list_name);
        name.setText(item.name);
        if (currentChapter == position) {
            name.setTextColor(ContextCompat.getColor(mContext, R.color.fontSelect));
        }else {
            name.setTextColor(ContextCompat.getColor(mContext, R.color.fontBlack));
        }
    }

    public void setCurrentChapter(int chapter) {
        currentChapter = chapter;
        notifyDataSetChanged();
    }
}
