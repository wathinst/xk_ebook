package com.wxz.ebook.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.wxz.ebook.BR;
import com.wxz.ebook.R;
import com.wxz.ebook.base.BaseAdapter;
import com.wxz.ebook.base.BaseModle;
import com.wxz.ebook.databinding.ItemSearchFileListBinding;
import com.wxz.ebook.mvvm.modle.SearchFileModle;

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
public class SearchFileAdapter extends BaseAdapter {

    private List<SearchFileModle> mDatas;
    public SearchFileAdapter(Context context, List<SearchFileModle> mDatas) {
        super(context, mDatas, R.layout.item_search_file_list, BR.file);
        this.mDatas = mDatas;
    }

    @Override
    protected void loadData(@NonNull ViewHolder holder, int position , BaseModle data) {
        ItemSearchFileListBinding binding = (ItemSearchFileListBinding)holder.getBinding();
        binding.searchFileListItemText.setVisibility(View.GONE);
        binding.searchFileListItem.setVisibility(View.VISIBLE);
    }

    @Override
    protected void noData(@NonNull ViewHolder holder, int position) {
        ItemSearchFileListBinding binding = (ItemSearchFileListBinding)holder.getBinding();
        binding.setFile(new SearchFileModle());
        binding.searchFileListItemText.setVisibility(View.VISIBLE);
        binding.searchFileListItem.setVisibility(View.GONE);
        binding.searchFileListItemText.setText("扫描中……");
    }
}
