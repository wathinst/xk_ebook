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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wxz.ebook.R;

public class ReadPageSetView extends FrameLayout {

    private ImageView set_list,set_size,set_set;
    private LinearLayout set_view,set_part;
    private ProgressBar set_progressBar;
    private TextView set_up_chapter,set_dn_chapter;
    private Listener listener;

    public ReadPageSetView(@NonNull Context context) {
        this(context,null);
    }

    public ReadPageSetView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ReadPageSetView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ReadPageSetView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.read_page_set,this,true);
        initView();
    }

    private void initView() {
        set_view = findViewById(R.id.read_book_set_view);
        set_part = findViewById(R.id.read_book_set_part);
        set_list = findViewById(R.id.read_set_list_bn);
        set_size = findViewById(R.id.read_set_size_bn);
        set_set = findViewById(R.id.read_set_set_bn);
        set_progressBar = findViewById(R.id.read_set_progressBar);
        set_up_chapter = findViewById(R.id.read_set_up_chapter);
        set_dn_chapter = findViewById(R.id.read_set_dn_chapter);
        set_list.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setListOnClick(v);
            }
        });
        set_size.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setSizeOnClick(v);
            }
        });
        set_part.setVisibility(GONE);
    }

    public void disShow(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(set_view, "translationY", 0, set_view.getHeight());
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                set_part.setVisibility(GONE);
                listener.setDisShowed();
            }
        });
        animator.start();
    }
    public void show(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(set_view, "translationY", set_view.getHeight(),0);
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                set_part.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                listener.setShowed();
            }
        });
        animator.start();
    }

    public interface Listener{
        void setListOnClick(View v);

        void setSizeOnClick(View v);

        void setShowed();

        void setDisShowed();
    }


    public void setOnClickListener(Listener listener){
        this.listener = listener;
    }
}