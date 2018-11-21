package com.wxz.ebook.asyncTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import com.wxz.ebook.bean.DocBean;

import java.util.ArrayList;
import java.util.List;

public class FileAsyncTask extends AsyncTask<Context,Void,List<DocBean>> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private OnFileAsyncTaskListener listener;

    @Override
    protected List<DocBean> doInBackground(Context... contexts) {
        this.context = contexts[0];
        return queryFiles();
    }

    @Override
    protected void onPostExecute(List<DocBean> docBeanModels) {
        if (listener!=null) {
            listener.fileTaskListener(docBeanModels);
        }
        super.onPostExecute(docBeanModels);
    }

    private List<DocBean> queryFiles(){
        //String bookpath = FileUtils.createRootPath(AppUtils.getAppContext());
        List<DocBean> docBeanModels = new ArrayList<>();
        String[] projection = new String[] { MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATE_MODIFIED
        };
       Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/file"),
                projection,
                MediaStore.Files.FileColumns.SIZE + " >= 1024 AND "+ MediaStore.Files.FileColumns.DATA + " like ? ",
                new String[]{"%.txt"},
                null);

        // 查询后缀名为txt与pdf，并且不位于项目缓存中的文档
        /*Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/file"),
                projection,
                MediaStore.Files.FileColumns.DATA + " not like ? and ("
                        + MediaStore.Files.FileColumns.DATA + " like ? or "
                        + MediaStore.Files.FileColumns.DATA + " like ? or "
                        + MediaStore.Files.FileColumns.DATA + " like ? or "
                        + MediaStore.Files.FileColumns.DATA + " like ? )",
                new String[]{"%" + bookpath + "%",
                        "%" + Constant.SUFFIX_TXT,
                        "%" + Constant.SUFFIX_PDF,
                        "%" + Constant.SUFFIX_EPUB,
                        "%" + Constant.SUFFIX_CHM}, null);*/

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
                int dataIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                int sizeIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
                int dateAddIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED);
                int dataModifiedIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED);
                do {
                    String id = cursor.getString(idIndex);
                    String path = cursor.getString(dataIndex);
                    String size = cursor.getString(sizeIndex);
                    long dateAdd = cursor.getLong(dateAddIndex);
                    long dateModified = cursor.getLong(dataModifiedIndex);
                    DocBean docBean = new DocBean();
                    docBean.setId(id);
                    docBean.setPath(path);
                    docBean.setSize(size);
                    docBean.setDateAdd(dateAdd);
                    docBean.setDateModified(dateModified);
                    int dot=path.lastIndexOf("/");
                    String name=path.substring(dot+1);
                    int dot1=name.lastIndexOf(".");
                    name = name.substring(0,dot1);
                    docBean.setName(name);
                    docBeanModels.add(docBean);
                } while (cursor.moveToNext());
            }
        }
        assert cursor != null;
        cursor.close();
        return docBeanModels;
    }

    public void setOnFileAsyncTaskListener(OnFileAsyncTaskListener listener){
        this.listener=listener;
    }

    public interface OnFileAsyncTaskListener {
        void fileTaskListener(List<DocBean> docBeanModels);
    }
}
