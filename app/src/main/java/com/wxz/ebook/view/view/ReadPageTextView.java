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

public class ReadPageTextView extends FrameLayout {

    private ImageView text_paper,text_white,text_purple,text_michelin,text_gray;
    private LinearLayout text_view,text_part;
    private ProgressBar text_progressBar;
    private TextView text_up_chapter,text_dn_chapter;
    private Listener listener;

    public ReadPageTextView(@NonNull Context context) {
        this(context,null);
    }

    public ReadPageTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ReadPageTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ReadPageTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.read_page_text,this,true);
        initView();
    }

    private void initView() {
        text_paper= findViewById(R.id.read_page_text_paper);
        text_white= findViewById(R.id.read_page_text_white);
        text_purple= findViewById(R.id.read_page_text_purple);
        text_michelin= findViewById(R.id.read_page_text_michelin);
        text_gray= findViewById(R.id.read_page_text_gray);
        text_view = findViewById(R.id.read_book_text_view);
        text_part = findViewById(R.id.read_book_text_part);
        text_progressBar = findViewById(R.id.read_text_progressBar);
        text_up_chapter = findViewById(R.id.read_text_up_chapter);
        text_dn_chapter = findViewById(R.id.read_text_dn_chapter);
        setOnClick();
        text_part.setVisibility(GONE);
    }

    public void setOnClick(){
        text_paper.setOnClickListener(new ReadPageTextClick());
        text_white.setOnClickListener(new ReadPageTextClick());
        text_purple.setOnClickListener(new ReadPageTextClick());
        text_michelin.setOnClickListener(new ReadPageTextClick());
        text_gray.setOnClickListener(new ReadPageTextClick());
    }

    public void disShow(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(text_view, "translationY", 0, text_view.getHeight());
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                text_part.setVisibility(GONE);
                listener.setDisShowed();
            }
        });
        animator.start();
    }
    public void show(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(text_view, "translationY",text_view.getHeight(),0);
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                text_part.setVisibility(VISIBLE);
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
        void setTextOnClick(View v);

        void setShowed();

        void setDisShowed();
    }


    public void setClickListener(Listener listener){
        this.listener = listener;
    }

    private class ReadPageTextClick implements OnClickListener{

        @Override
        public void onClick(View v) {
            listener.setTextOnClick(v);
        }
    }
}