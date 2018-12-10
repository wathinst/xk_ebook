package com.wxz.ebook.tool.sql;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wxz.ebook.R;
import com.wxz.ebook.bean.BookInfoBean;
import com.wxz.ebook.bean.SearchBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchHistoryHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="xkSearchHistory.db";//数据库名称
    private static final int SCHEMA_VERSION=1;//版本号,则是升级之后的,升级方法请看onUpgrade方法里面的判断
    private Context context;

    public SearchHistoryHelper(Context context) {
        super(context, DATABASE_NAME, null,SCHEMA_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(context.getString(R.string.searchCreateSql));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public List<SearchBean> getWhere(String where, String orderBy) {//返回表中的数据,where是调用时候传进来的搜索内容,orderby是设置中传进来的列表排序类型
        StringBuilder buf=new StringBuilder(context.getString(R.string.searchSelectStr));

        if (where!=null) {
            buf.append(" WHERE ");
            buf.append(where);
        }

        if (orderBy!=null) {
            buf.append(" ORDER BY ");
            buf.append(orderBy);
        }
        @SuppressLint("Recycle") Cursor cursor = (getReadableDatabase().rawQuery(buf.toString(), null));
        return getSearchHistoryBeans(cursor);
    }

    public List<SearchBean> getAll(String where, String orderBy) {//返回表中的数据,where是调用时候传进来的搜索内容,orderby是设置中传进来的列表排序类型
        StringBuilder buf=new StringBuilder(context.getString(R.string.searchSelectStr));

        if (where!=null) {
            buf.append(" WHERE ");
            buf.append(where);
        }

        if (orderBy!=null) {
            buf.append(" ORDER BY ");
            buf.append(orderBy);
        }
        @SuppressLint("Recycle") Cursor cursor = (getReadableDatabase().rawQuery(buf.toString(), null));
        return getSearchHistoryBeans(cursor);
    }

    public void insert(SearchBean bean) {
        ContentValues cv=new ContentValues();

        Date date = new Date();

        cv.put("title", bean.title);
        cv.put("date", date.getTime());

        long id = getWritableDatabase().insert("xkSearchHistory", "title", cv);
        bean.id = (int) id;
    }

    public void update(SearchBean bean) {
        ContentValues cv=new ContentValues();
        String[] args={String.valueOf(bean.id)};

        Date date = new Date();

        cv.put("title", bean.title);
        cv.put("date", date.getTime());

        getWritableDatabase().update("xkSearchHistory", cv, "_id=?", args);
    }

    public void delete(SearchBean bean) {
        ContentValues cv=new ContentValues();
        String[] args={String.valueOf(bean.id)};
        getWritableDatabase().delete("xkSearchHistory","_id=?", args);
    }

    public void deleteById(int id) {
        String[] args={String.valueOf(id)};
        getWritableDatabase().delete("xkSearchHistory","_id=?", args);
    }

    public void deleteByTitle(String title) {
        String[] args={title};
        getWritableDatabase().delete("xkSearchHistory","title=?", args);
    }

    public void deletaAll(){
        String[] args={"*"};
        getWritableDatabase().delete("xkSearchHistory","_id=?", args);
    }

    private List<SearchBean> getSearchHistoryBeans(Cursor cursor){
        List<SearchBean> beans = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("_id");
                int titleIndex = cursor.getColumnIndex("title");
                int dateIndex = cursor.getColumnIndex("date");
                do {
                    SearchBean bean = new SearchBean();
                    bean.id = cursor.getInt(idIndex);
                    bean.title = cursor.getString(titleIndex);
                    bean.date = cursor.getLong(dateIndex);
                    beans.add(bean);
                } while (cursor.moveToNext());
            }
        }
        assert cursor != null;
        cursor.close();
        return beans;
    }
}
