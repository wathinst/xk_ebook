package com.wxz.ebook.view.acticity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wxz.ebook.R;
import com.wxz.ebook.asyncTask.ReadAsyncTask;
import com.wxz.ebook.bean.ChapterListBean;
import com.wxz.ebook.ui.curlUI.CurlPage;
import com.wxz.ebook.ui.curlUI.CurlView;
import com.wxz.ebook.tool.ReadFactory;
import com.wxz.ebook.tool.StrRWBuffer;
import com.wxz.ebook.view.view.ChapterListView;
import com.wxz.ebook.view.view.ReadPageSetView;

import java.io.File;
import java.io.IOException;

public class ReadPageActivity extends AppCompatActivity implements ReadAsyncTask.ReadInterface{

    private CurlView curlView;
    private StrRWBuffer buffer;
    private ChapterListBean bean;
    private ReadFactory readFactory;
    private TextView book_loading;
    private ChapterListView chapterListView;
    private ReadPageSetView readPageSetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除title
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);//去掉Activity上面的状态栏
        setContentView(R.layout.activity_read_page);
        initView();

        readFactory = new ReadFactory(this);
        String fileName = getSDPath() +"/" + "ebook.xkr";
        try {
            buffer = new StrRWBuffer(fileName);
            buffer.newReadMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ReadAsyncTask readAsyncTask = new ReadAsyncTask();
        readAsyncTask.setReadInterface(this);
        readAsyncTask.execute(buffer);

        readFactory.setBg(BitmapFactory.decodeResource(this.getResources(),R.drawable.paper));
        readFactory.setFontSize(36);

        curlView.setPageProvider(new PageProvider());
        curlView.setSizeChangedObserver(new SizeChangedObserver());
        curlView.setCurrentIndex(0);
        curlView.setBackgroundColor(0xFF202830);

        readPageSetView.setOnClickListener(new ReadPageSetView.Listener() {
            @Override
            public void setListOnClick(View v) {
                readPageSetView.disShow();
                chapterListView.show();

            }
        });
    }

    private void initView(){
        curlView = findViewById(R.id.read_curl_page_view);
        book_loading = findViewById(R.id.read_view_book_loading);
        chapterListView = findViewById(R.id.read_page_chapter_list_view);
        readPageSetView = findViewById(R.id.read_page_set_view);
    }

    @Override
    public void onPause() {
        super.onPause();
        curlView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        curlView.onResume();
    }

    private String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        assert sdDir != null;
        return sdDir.toString();
    }

    @Override
    public void setChapterList(final ChapterListBean chapterListBean) {
        bean = chapterListBean;
        chapterListView.setName(bean.name);
        chapterListView.setDate(bean.listBeans);
        chapterListView.setDurChapter(10);
        curlView.setOnClickListener(new CurlView.Listener() {
            @Override
            public void setOnClick() {
                readPageSetView.show();
            }
        });
        chapterListView.setOnClickListener(new ChapterListView.Listener() {
            @Override
            public void setListOnClick(int position) {
                String str = buffer.getChapterStr(bean.listBeans.get(position));
                readFactory.setChapterStr(str);
                curlView.setCurrentIndex(0);
                chapterListView.disShow(position);
            }
        });
    }

    @Override
    public void setChapterStr(String str) {
        book_loading.setVisibility(View.GONE);
        readFactory.setChapterStr(str);
        curlView.setCurrentIndex(0);
    }

    private class PageProvider implements CurlView.PageProvider{

        @Override
        public int getPageCount() {
            return readFactory.getPageSize();
        }

        @Override
        public void updatePage(CurlPage page, int width, int height, int index) {
            readFactory.setPageIndex(index);
            readFactory.readDraw();
            Bitmap front = readFactory.getFrontBitmap();
            Bitmap back = readFactory.getBackBitmap();
            page.setTexture(front, CurlPage.SIDE_FRONT);
            page.setTexture(back, CurlPage.SIDE_BACK);
        }
    }

    private class SizeChangedObserver implements CurlView.SizeChangedObserver {

        @Override
        public void onSizeChanged(int width, int height) {
            if (width > height) {
                curlView.setViewMode(CurlView.SHOW_TWO_PAGES);
                curlView.setMargins(.0f, .0f, .0f, .0f);
            } else {
                curlView.setViewMode(CurlView.SHOW_ONE_PAGE);
                curlView.setMargins(.0f, .0f, .0f, .0f);
            }
        }
    }


}
