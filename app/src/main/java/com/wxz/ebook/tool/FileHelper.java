package com.wxz.ebook.tool;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wxz.ebook.R;
import com.wxz.ebook.bean.BookInfoBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="file.db";//数据库名称
    private static final int SCHEMA_VERSION=1;//版本号,则是升级之后的,升级方法请看onUpgrade方法里面的判断
    private Context context;

    public FileHelper(Context context) {
        super(context, DATABASE_NAME, null,SCHEMA_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(context.getString(R.string.fileCreateSql));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion==1 && newVersion==2) {//升级判断从1到2
            db.execSQL(context.getString(R.string.fileUpV2Sql));
        }
    }

    public List<BookInfoBean> getWhere(String where, String orderBy) {//返回表中的数据,where是调用时候传进来的搜索内容,orderby是设置中传进来的列表排序类型
        StringBuilder buf=new StringBuilder("SELECT _id, name, readIndex,  pageIndex, path, date FROM xkr");

        if (where!=null) {
            buf.append(" WHERE ");
            buf.append(where);
        }

        if (orderBy!=null) {
            buf.append(" ORDER BY ");
            buf.append(orderBy);
        }
        @SuppressLint("Recycle") Cursor cursor = (getReadableDatabase().rawQuery(buf.toString(), null));
        return getListBooks(cursor);
    }

    public List<BookInfoBean> getAll(String where, String orderBy) {//返回表中的数据,where是调用时候传进来的搜索内容,orderby是设置中传进来的列表排序类型
        StringBuilder buf=new StringBuilder("SELECT _id, name, readIndex,  pageIndex, path, date FROM xkr");

        if (where!=null) {
            buf.append(" WHERE ");
            buf.append(where);
        }

        if (orderBy!=null) {
            buf.append(" ORDER BY ");
            buf.append(orderBy);
        }
        @SuppressLint("Recycle") Cursor cursor = (getReadableDatabase().rawQuery(buf.toString(), null));
        return getListBooks(cursor);
    }

    public void insert(BookInfoBean bookInfoBean) {
        ContentValues cv=new ContentValues();

        cv.put("name", bookInfoBean.getName());
        cv.put("readIndex", bookInfoBean.getReadIndex());
        cv.put("pageIndex", bookInfoBean.getPageIndex());
        cv.put("path", bookInfoBean.getPath());
        cv.put("date", bookInfoBean.getDate());

        long id = getWritableDatabase().insert("xkr", "name", cv);
        bookInfoBean.setId(String.valueOf(id));
    }

    public void update(BookInfoBean bookInfoBean) {
        ContentValues cv=new ContentValues();
        String[] args={bookInfoBean.getId()};

        Date date = new Date();

        cv.put("name", bookInfoBean.getName());
        cv.put("readIndex", bookInfoBean.getReadIndex());
        cv.put("pageIndex", bookInfoBean.getPageIndex());
        cv.put("path", bookInfoBean.getPath());
        cv.put("date", date.getTime());

        getWritableDatabase().update("xkr", cv, "_id=?", args);
    }

    public void delete(BookInfoBean bookInfoBean) {
        ContentValues cv=new ContentValues();
        String[] args={bookInfoBean.getId()};
        getWritableDatabase().delete("xkr","_id=?", args);
    }

    private List<BookInfoBean> getListBooks(Cursor cursor){
        List<BookInfoBean> bookInfoBeans = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("_id");
                int indexIndex = cursor.getColumnIndex("readIndex");
                int pageIndex = cursor.getColumnIndex("pageIndex");
                int nameIndex = cursor.getColumnIndex("name");
                int pathIndex = cursor.getColumnIndex("path");
                int dateIndex = cursor.getColumnIndex("date");
                do {
                    String id = cursor.getString(idIndex);
                    int index = cursor.getInt(indexIndex);
                    int page = cursor.getInt(pageIndex);
                    String path = cursor.getString(pathIndex);
                    String name = cursor.getString(nameIndex);
                    long date = cursor.getLong(dateIndex);
                    BookInfoBean bookInfoBean = new BookInfoBean();
                    bookInfoBean.setId(id);
                    bookInfoBean.setName(name);
                    bookInfoBean.setReadIndex(index);
                    bookInfoBean.setPageIndex(page);
                    bookInfoBean.setPath(path);
                    bookInfoBean.setDate(date);
                    bookInfoBeans.add(bookInfoBean);
                } while (cursor.moveToNext());
            }
        }
        assert cursor != null;
        cursor.close();
        return bookInfoBeans;
    }
}
