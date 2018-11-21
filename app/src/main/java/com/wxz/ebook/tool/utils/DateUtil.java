package com.wxz.ebook.tool.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DateUtil {
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

    public String getTimeStr(String s){
        Calendar calendar = Calendar.getInstance();//获取当前日历对象
        String res="";
        try {
            long newsTime=dateToStamp1(s);
            long nowTime=calendar.getTimeInMillis();
            long subTime=nowTime-newsTime;
            res=String.valueOf(subTime);
            if (subTime<60000){
                res="刚刚";
            }
            if(subTime>=60000&&subTime<3600000){
                res=String.valueOf((int)(subTime/60000))+"分钟前";
            }else if(subTime>=3600000&&subTime<86400000){
                res=String.valueOf((int)(subTime/3600000))+"小时前";
            }else if(subTime>=86400000&&subTime<2592000000L){
                res=String.valueOf((int)(subTime/86400000))+"天前";
            }else if(subTime>=2592000000L&&subTime<31104000000L){
                res=String.valueOf((int)(subTime/2592000000L))+"月前";
            }else if(subTime>=31104000000L){
                res=stampToDate(String.valueOf(newsTime),"MM-dd HH:mm");
            }
            return res;
        } catch (ParseException e) {
            e.printStackTrace();
            return res;
        }
    }

    public String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    public long dateToStamp1(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = simpleDateFormat.parse(s);
        return date.getTime();
    }

    public static String stampToDate(String s,String d){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(d);
        long lt = Long.valueOf(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public Boolean getTime(String s){
        Calendar calendar = Calendar.getInstance();//获取当前日历对象
        boolean status=false;
        if(!s.isEmpty()) {
            long tokenTime = Long.parseLong(s);
            long nowTime = calendar.getTimeInMillis();
            long subTime = nowTime - tokenTime;
            if (subTime > 7000000) {
                status = true;
            }
        }
        return status;
    }

    public String getNowTime(){
        Calendar calendar = Calendar.getInstance();//获取当前日历对象
        long nowTime=calendar.getTimeInMillis();
        return String.valueOf(nowTime);
    }
}
