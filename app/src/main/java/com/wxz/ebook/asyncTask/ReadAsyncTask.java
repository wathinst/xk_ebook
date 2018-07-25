package com.wxz.ebook.asyncTask;

import android.os.AsyncTask;

import com.wxz.ebook.bean.ChapterListBean;
import com.wxz.ebook.tool.StrRWBuffer;

public class ReadAsyncTask extends AsyncTask<StrRWBuffer,Void,ChapterListBean> {

    private ReadInterface readInterface;

    @Override
    protected ChapterListBean doInBackground(StrRWBuffer... strRWBuffers) {
        StrRWBuffer buffer = strRWBuffers[0];
        if (buffer!=null){
            return buffer.getChapterList();
        }else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(ChapterListBean chapterListBean) {
        super.onPostExecute(chapterListBean);
        if (chapterListBean!=null){
            readInterface.setChapterList(chapterListBean);
        }
    }

    public void setReadInterface(ReadInterface readInterface) {
        this.readInterface = readInterface;
    }

    public interface ReadInterface{
        public void setChapterList(ChapterListBean chapterListBean);
    }
}
