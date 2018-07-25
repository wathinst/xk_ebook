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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxz.ebook.R;
import com.wxz.ebook.bean.ChapterListBean;
import com.wxz.ebook.view.adapter.ReadAdapter;

import java.util.ArrayList;
import java.util.List;

public class CoverView extends FrameLayout {
    private View cover_view,cover_part;

    public CoverView(@NonNull Context context) {
        this(context,null);
    }

    public CoverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CoverView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CoverView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.read_page_cover,this,true);
        initView();
        cover_part.setVisibility(GONE);
    }

    private void initView() {
        cover_part = findViewById(R.id.read_book_cover_part);
        cover_view =findViewById(R.id.read_book_cover_view);
    }
    public void show() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(cover_view, "alpha",0,1);
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                cover_part.setVisibility(VISIBLE);
            }
        });
        animator.start();
    }

    public void disShow(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(cover_view, "alpha", 1, 0);
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                cover_part.setVisibility(GONE);
            }
        });
        animator.start();
    }
}