package com.wxz.ebook.view.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxz.ebook.R;
import com.wxz.ebook.bean.ChapterListBean;
import com.wxz.ebook.config.ReadPageConfig;
import com.wxz.ebook.config.SharedPreferencesUtil;
import com.wxz.ebook.tool.utils.AppUtils;
import com.wxz.ebook.view.adapter.ReadAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChapterListView extends FrameLayout {

    private TextView book_name;
    private View other;
    private RecyclerView nav_list;
    private CoordinatorLayout nav_view;
    private LinearLayout nav_part;
    private LinearLayout nav_context;
    private ReadAdapter readAdapter;
    private List<ChapterListBean.ListBean> list;
    private Listener listener;
    private ReadPageConfig readPageConfig;

    public ChapterListView(@NonNull Context context) {
        this(context,null);
    }

    public ChapterListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ChapterListView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChapterListView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.read_list_nav,this,true);
        initView();
        AppUtils.init(getContext());
        SharedPreferencesUtil.init(getContext(),"xkEBookRead",Context.MODE_PRIVATE);
        readPageConfig = new ReadPageConfig();
        readPageConfig.setModeIndex(readPageConfig.pageTheme);
        nav_context.setBackground(readPageConfig.getModeDrawable(readPageConfig.pageTheme));
        readAdapter.setChapTextColor(readPageConfig.textColor);
        nav_part.setVisibility(GONE);
    }

    private void initView() {
        other = findViewById(R.id.read_book_nav_other);
        list = new ArrayList<>();
        book_name =findViewById(R.id.read_book_name);
        nav_list = findViewById(R.id.read_book_list_recycler);
        nav_list.setLayoutManager(new LinearLayoutManager(getContext()));
        book_name.setText("");
        readAdapter = new ReadAdapter(getContext(),list);
        nav_list.setAdapter(readAdapter);
        nav_view = findViewById(R.id.read_book_nav_view);
        nav_part = findViewById(R.id.read_book_nav_part);
        nav_context = findViewById(R.id.read_book_nav_context);
        other.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setOtherOnClick(v);
            }
        });
        readAdapter.setOnItemClickListener(new ReadAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                listener.setListOnClick(position);
            }
        });
    }

    public void setDurChapter(int durChapter) {
        if(durChapter >= 0 && durChapter < list.size()){
            readAdapter.setCurrentChapter(durChapter);
            ((LinearLayoutManager) nav_list.getLayoutManager()).scrollToPositionWithOffset(durChapter,0);
        }
    }

    public void show() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(nav_view, "translationX", -nav_view.getWidth(),0);
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                nav_part.setVisibility(VISIBLE);
            }
        });
        animator.start();
    }

    public void show(int durChapter){
        if(durChapter >= 0 && durChapter < list.size()){
            readAdapter.setCurrentChapter(durChapter);
            ((LinearLayoutManager) nav_list.getLayoutManager()).scrollToPositionWithOffset(durChapter,0);
        }
        show();
    }

    public void disShow(int durChapter){
        if(durChapter >= 0 && durChapter < list.size()){
            readAdapter.setCurrentChapter(durChapter);
            ((LinearLayoutManager) nav_list.getLayoutManager()).scrollToPositionWithOffset(durChapter,0);
        }
        disShow();
    }

    public void disShow(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(nav_view, "translationX", 0, -nav_view.getWidth());
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                nav_part.setVisibility(GONE);
            }
        });
        animator.start();
    }

    public void setDate(List<ChapterListBean.ListBean> listBeans){
        list.clear();
        list.addAll(listBeans);
        readAdapter.notifyDataSetChanged();
    }

    public void setName(String name){
        book_name.setText(name);
    }

    public void upBackground(){
        AppUtils.init(getContext());
        SharedPreferencesUtil.init(getContext(),"xkEBookRead",Context.MODE_PRIVATE);
        readPageConfig = new ReadPageConfig();
        readPageConfig.setModeIndex(readPageConfig.pageTheme);
        nav_context.setBackground(readPageConfig.getModeDrawable(readPageConfig.pageTheme));
        readAdapter.setChapTextColor(readPageConfig.textColor);
    }



    public interface Listener{

        void setListOnClick(int position);

        void setOtherOnClick(View v);
    }

    public void setOnClickListener(Listener listener){
        this.listener = listener;
    }
}