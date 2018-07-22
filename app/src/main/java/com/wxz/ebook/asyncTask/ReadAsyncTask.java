package com.wxz.ebook.asyncTask;

import android.os.AsyncTask;

import com.wxz.ebook.bean.ChapterListBean;
import com.wxz.ebook.tool.StrRWBuffer;

public class ReadAsyncTask extends AsyncTask<StrRWBuffer,ChapterListBean,String> {

    private ReadInterface readInterface;

    @Override
    protected String doInBackground(StrRWBuffer... strRWBuffers) {
        StrRWBuffer buffer = strRWBuffers[0];
        ChapterListBean bean = buffer.getChapterList();
        publishProgress(bean);
        return buffer.getChapterStr(bean.listBeans.get(10));
    }

    @Override
    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        if (str!=null){
            readInterface.setChapterStr(str);
        }
    }

    @Override
    protected void onProgressUpdate(ChapterListBean... values) {
        super.onProgressUpdate(values);
        if (values[0] != null){
            readInterface.setChapterList(values[0]);
        }
    }

    public void setReadInterface(ReadInterface readInterface) {
        this.readInterface = readInterface;
    }

    public interface ReadInterface{
        public void setChapterList(ChapterListBean chapterListBean);

        public void setChapterStr(String str);
    }
}
