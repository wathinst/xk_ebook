package com.wxz.ebook.view.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wxz.ebook.bean.BookDetail;
import com.wxz.ebook.view.fragment.BookDetailsListFragment;
import com.wxz.ebook.view.fragment.BookDetailsSummaryFragment;

import java.util.ArrayList;
import java.util.List;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private List<String> tabTitles;
    private List<Fragment> fragmentList;
    private BookDetail bookDetail;

    public SectionsPagerAdapter(FragmentManager fm, BookDetail bookDetail) {
        super(fm);
        this.bookDetail = bookDetail;
        tabTitles = new ArrayList<>();
        tabTitles.add("简介");
        tabTitles.add("目录("+bookDetail.chaptersCount+")");
        fragmentList = new ArrayList<>();
        fragmentList.add(new BookDetailsSummaryFragment(bookDetail));
        fragmentList.add(BookDetailsListFragment.newInstance(bookDetail._id));
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
