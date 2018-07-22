package com.wxz.ebook.tool;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DateUnit {
    public String getDateStr(long time){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time*1000);
        return  sdf.format(date);
    }

    public String getNowStr(long time){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time*1000);
        return  sdf.format(date);
    }

    public String getRandomFileName() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date();
        String str = sdf.format(date);
        Random random = new Random();
        int ranNum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        return str + ranNum;// 当前时间
    }
}
