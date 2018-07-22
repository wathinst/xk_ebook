package com.wxz.ebook.asyncTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.wxz.ebook.bean.BookInfoBean;
import com.wxz.ebook.tool.ReadTXTFile;

import java.util.Date;

public class ResolveAsyncTask extends AsyncTask<String,Void,BookInfoBean> {
    private OnResolveAsyncTaskListener listener;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public ResolveAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected BookInfoBean doInBackground(String... strings) {
        BookInfoBean bookInfoBean = new BookInfoBean();
        int dot=strings[0].lastIndexOf("/");
        String name=strings[0].substring(dot+1);
        int dot1=name.lastIndexOf(".");
        name = name.substring(0,dot1);
        ReadTXTFile readTXTFile = new ReadTXTFile(context);
        String filePath = "";
        try {
            filePath = readTXTFile.readTxt(strings[0]);
        }catch (Exception e) {
            e.printStackTrace();
        }
        bookInfoBean.setPageIndex(0);
        bookInfoBean.setReadIndex(0);
        bookInfoBean.setName(name);
        bookInfoBean.setPath(filePath);
        Date date = new Date();
        bookInfoBean.setDate(date.getTime());
        return bookInfoBean;
    }

    @Override
    protected void onPostExecute(BookInfoBean bookInfoBean) {
        if (listener!=null) {
            listener.resolveTaskListener(bookInfoBean);
        }
        super.onPostExecute(bookInfoBean);
    }

    public void setOnResolveAsyncTaskListener(OnResolveAsyncTaskListener listener){
        this.listener=listener;
    }

    public interface OnResolveAsyncTaskListener {
        void resolveTaskListener(BookInfoBean bookInfoBean);
    }

}
