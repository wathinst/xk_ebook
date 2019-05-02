package com.wxz.ebook.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wxz.ebook.R;
import com.wxz.ebook.databinding.ActivityJoinBinding;
import com.wxz.ebook.mvvm.modle.JoinModle;

public class JoinActivity extends AppCompatActivity {

    private ActivityJoinBinding binding;
    private JoinModle joinModle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join);
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        joinModle = new JoinModle("校咖阅读");
        binding.setJoin(joinModle);
        binding.setOnclick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinModle.setName("韦行志");
                binding.setJoin(joinModle);
            }
        });
    }

}
