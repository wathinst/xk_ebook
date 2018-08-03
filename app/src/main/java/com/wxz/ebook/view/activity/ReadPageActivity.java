package com.wxz.ebook.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
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
import com.wxz.ebook.asyncTask.ResolveAsyncTask;
import com.wxz.ebook.bean.BookInfoBean;
import com.wxz.ebook.bean.ChapterListBean;
import com.wxz.ebook.bean.DocBean;
import com.wxz.ebook.config.ReadPageTheme;
import com.wxz.ebook.config.SharedPreferencesUtil;
import com.wxz.ebook.tool.AppUtils;
import com.wxz.ebook.tool.FileHelper;
import com.wxz.ebook.view.ui.curlUI.CurlPage;
import com.wxz.ebook.view.ui.curlUI.CurlView;
import com.wxz.ebook.tool.ReadFactory;
import com.wxz.ebook.tool.StrRWBuffer;
import com.wxz.ebook.view.view.ChapterListView;
import com.wxz.ebook.view.view.CoverView;
import com.wxz.ebook.view.view.ReadPageSetView;
import com.wxz.ebook.view.view.ReadPageTextView;

import java.io.File;
import java.io.IOException;

public class ReadPageActivity extends AppCompatActivity implements ReadAsyncTask.ReadInterface{

    private CoverView coverView;
    private CurlView curlView;
    private StrRWBuffer buffer;
    private ChapterListBean bean;
    private ReadFactory nextFactory,lastFactory,readFactory;
    private TextView book_loading;
    private ChapterListView chapterListView;
    private ReadPageSetView readPageSetView;
    private ReadPageTextView readPageTextView;
    private BookInfoBean mBookInfoBean;
    private FileHelper helper;
    private ReadAsyncTask readAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除title
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);//去掉Activity上面的状态栏
        setContentView(R.layout.activity_read_page);
        AppUtils.init(this);
        SharedPreferencesUtil.init(this,"xkEBookRead",Context.MODE_PRIVATE);
        initView();
        initFactory();

        Intent intent =getIntent();
        ResolveAsyncTask asyncTask = new ResolveAsyncTask(this);
        helper =new FileHelper(this);
        DocBean docBean= (DocBean) intent.getSerializableExtra("docBean");
        mBookInfoBean = (BookInfoBean) intent.getSerializableExtra("bookInfoBean");

        if(docBean != null){
            asyncTask.execute(docBean.getPath(),docBean.getSize());
            asyncTask.setOnResolveAsyncTaskListener(new ResolveAsyncTask.OnResolveAsyncTaskListener() {
                @Override
                public void resolveTaskListener(BookInfoBean bookInfoBean) {
                    helper.insert(bookInfoBean);
                    mBookInfoBean = bookInfoBean;
                    initPage(mBookInfoBean);
                }
            });
        }
        if(mBookInfoBean != null){
            initPage(mBookInfoBean);
        }


        curlView.setPageProvider(new PageProvider(this));
        curlView.setSizeChangedObserver(new SizeChangedObserver());
        curlView.setCurrentIndex(0);
        curlView.setBackgroundColor(0xFF202830);

        setOnClick();
    }


    private void setOnClick(){
        readPageSetView.setOnClickListener(new ReadPageSetView.Listener() {
            @Override
            public void setListOnClick(View v) {
                readPageSetView.disShow();
                chapterListView.show();
                coverView.show();
            }

            @Override
            public void setSizeOnClick(View v) {
                readPageSetView.disShow();
                readPageTextView.show();
            }

            @Override
            public void setShowed() {
                curlView.setOnTouchSettingFlag(true);
            }

            @Override
            public void setDisShowed() {
                curlView.setOnTouchSettingFlag(false);
            }
        });

        readPageTextView.setClickListener(new ReadPageTextView.Listener() {
            @Override
            public void setTextOnClick(View v) {
                switch (v.getId()){
                    case R.id.read_page_text_paper:
                        Log.e("theme","0");
                        setPageTheme(ReadPageTheme.THEME_PAPER);
                        curlView.updateCurl();
                        break;
                    case R.id.read_page_text_white:
                        setPageTheme(ReadPageTheme.THEME_WHITE);
                        curlView.updateCurl();
                        break;
                    case R.id.read_page_text_purple:
                        setPageTheme(ReadPageTheme.THEME_PURPLE);
                        curlView.updateCurl();
                        break;
                    case R.id.read_page_text_michelin:
                        setPageTheme(ReadPageTheme.THEME_MICHELIN);
                        curlView.updateCurl();
                        break;
                    case R.id.read_page_text_gray:
                        setPageTheme(ReadPageTheme.THEME_GRAY);
                        curlView.updateCurl();
                        break;
                    default:
                        break;
                }
                readPageTextView.disShow();
            }

            @Override
            public void setShowed() {
                curlView.setOnTouchSettingFlag(true);
            }

            @Override
            public void setDisShowed() {
                curlView.setOnTouchSettingFlag(false);
            }
        });
    }

    private void initFactory(){
        readFactory = new ReadFactory();
        nextFactory = new ReadFactory();
        lastFactory = new ReadFactory();
    }

    private void setTextSize(int textSize){
        AppUtils.init(this);
        SharedPreferencesUtil.init(this,"xkEBookRead",Context.MODE_PRIVATE);
        readFactory.setFontSize(textSize);
        nextFactory.setFontSize(textSize);
        lastFactory.setFontSize(textSize);
    }

    private void setPageTheme(int pageTheme){
        AppUtils.init(this);
        SharedPreferencesUtil.init(this,"xkEBookRead",Context.MODE_PRIVATE);
        readFactory.setPageTheme(pageTheme);
        nextFactory.setPageTheme(pageTheme);
        lastFactory.setPageTheme(pageTheme);
        chapterListView.upBackground();
    }

    private void initView(){
        curlView = findViewById(R.id.read_curl_page_view);
        coverView = findViewById(R.id.read_page_cover_view);
        book_loading = findViewById(R.id.read_view_book_loading);
        chapterListView = findViewById(R.id.read_page_chapter_list_view);
        readPageSetView = findViewById(R.id.read_page_set_view);
        readPageTextView = findViewById(R.id.read_page_text_view);
        readAsyncTask = new ReadAsyncTask();
        readAsyncTask.setReadInterface(this);
    }

    private void initPage(BookInfoBean bookInfoBean){
        String fileName = getSDPath() +"/" + "ebook.xkr";
        if (bookInfoBean != null){
            fileName = bookInfoBean.getPath();
        }
        try {
            buffer = new StrRWBuffer(fileName);
            buffer.newReadMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        readAsyncTask.execute(buffer);
    }

    private void setDateReadFactory(int index){
        String str = "";
        if (buffer != null && bean != null){
            str = buffer.getChapterStr(bean.listBeans.get(index));
            readFactory.setBookName(bean.listBeans.get(index).name);
        }
        readFactory.setChapterStr(str);
    }

    private void setDateLastFactory(int index){
        lastFactory = new ReadFactory();
        String str = "";
        if(buffer != null && bean!= null && index > 0){
            str = buffer.getChapterStr(bean.listBeans.get(index-1));
            lastFactory.setBookName(bean.listBeans.get(index-1).name);
        }
        lastFactory.setChapterStr(str);
    }

    private void setDateNextFactory(int index){
        nextFactory = new ReadFactory();
        String str = "";
        if(buffer != null && bean!= null && index + 1 < bean.listBeans.size()){
            str = buffer.getChapterStr(bean.listBeans.get(index+1));
            nextFactory.setBookName(bean.listBeans.get(index+1).name);
        }
        nextFactory.setChapterStr(str);
    }

    public void setDataFactory(int index,int readIndex) {
        curlView.setNewCurl();
        setDateReadFactory(index);
        setDateLastFactory(index);
        setDateNextFactory(index);
        readFactory.readDraw();
        curlView.setCurrentIndex(readIndex);
    }



    public void setDataFactory(boolean b) {//true +1 false -1
        if (b){
            lastFactory = readFactory;
            readFactory = nextFactory;
            mBookInfoBean.setPageIndex(mBookInfoBean.getPageIndex()+1);
            setDateNextFactory(mBookInfoBean.getPageIndex());
            helper.update(mBookInfoBean);
        }else {
            nextFactory = readFactory;
            readFactory = lastFactory;
            mBookInfoBean.setPageIndex(mBookInfoBean.getPageIndex()-1);
            setDateNextFactory(mBookInfoBean.getPageIndex());
            helper.update(mBookInfoBean);
        }
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

    @Override
    public void onBackPressed() {
        if (curlView.getCurrentIndex() >= readFactory.getPageSize()){
            mBookInfoBean.setPageIndex(curlView.getCurrentIndex()+1);
            mBookInfoBean.setReadIndex(0);
        }else {
            mBookInfoBean.setReadIndex(curlView.getCurrentIndex());
        }
        helper.update(mBookInfoBean);
        Log.e("page",String.valueOf(mBookInfoBean.getPageIndex()));
        Log.e("read",String.valueOf(mBookInfoBean.getReadIndex()));
        Intent rIntent = new Intent();
        rIntent.putExtra("rBookInfoBean",mBookInfoBean);
        setResult(2,rIntent);
        super.onBackPressed();
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
        chapterListView.setDurChapter(mBookInfoBean.getPageIndex());
        setDataFactory(mBookInfoBean.getPageIndex(),mBookInfoBean.getReadIndex());
        book_loading.setVisibility(View.GONE);
        curlView.setOnClickListener(new CurlView.Listener() {
            @Override
            public void setOnClick() {
                readPageSetView.show();
            }

            @Override
            public void setOnSettingClick() {
                readPageSetView.disShow();
                readPageTextView.disShow();
            }
        });
        chapterListView.setOnClickListener(new ChapterListView.Listener() {
            @Override
            public void setListOnClick(int position) {
                mBookInfoBean.setPageIndex(position);
                setDataFactory(mBookInfoBean.getPageIndex(),0);
                chapterListView.disShow(position);
                coverView.disShow();
            }

            @Override
            public void setOtherOnClick(View v) {
                chapterListView.disShow();
                coverView.disShow();
            }
        });
    }


    private class PageProvider implements CurlView.PageProvider{

        private Context context;

        public PageProvider(Context context) {
            this.context = context;
        }

        @Override
        public int getPageCount() {
            return readFactory.getPageSize();
        }

        @Override
        public void updatePage(CurlPage page, int width, int height, int index) {
            if (index < 0){
                lastFactory.setPageIndex(lastFactory.getPageSize()-1);
                lastFactory.readDraw();
                Bitmap front = lastFactory.getFrontBitmap();
                Bitmap back = lastFactory.getBackBitmap();
                page.setTexture(front, CurlPage.SIDE_FRONT);
                page.setTexture(back, CurlPage.SIDE_BACK);
            }
            if (index >= 0 && index < readFactory.getPageSize()){
                readFactory.setPageIndex(index);
                readFactory.readDraw();
                Bitmap front = readFactory.getFrontBitmap();
                Bitmap back = readFactory.getBackBitmap();
                page.setTexture(front, CurlPage.SIDE_FRONT);
                page.setTexture(back, CurlPage.SIDE_BACK);
            }
            if (index >= readFactory.getPageSize()){
                nextFactory.setPageIndex(0);
                nextFactory.readDraw();
                Bitmap front = nextFactory.getFrontBitmap();
                Bitmap back = nextFactory.getBackBitmap();
                page.setTexture(front, CurlPage.SIDE_FRONT);
                page.setTexture(back, CurlPage.SIDE_BACK);
            }
        }

        @Override
        public int nextChapter() {
            setDataFactory(true);
            chapterListView.setDurChapter(mBookInfoBean.getPageIndex());
            return 0;
        }

        @Override
        public int lastChapter() {
            setDataFactory(false);
            chapterListView.setDurChapter(mBookInfoBean.getPageIndex());
            return readFactory.getPageSize()-1;
        }

        @Override
        public boolean isNextChapter() {
            return mBookInfoBean.getPageIndex() + 1 >= bean.listBeans.size();
        }

        @Override
        public boolean isLastChapter() {
            return mBookInfoBean.getPageIndex() <= 0;
        }

        @Override
        public void defaultPage(CurlPage page, int width, int height) {
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Bitmap background = BitmapFactory.decodeResource(context.getResources(),R.drawable.paper);
            Canvas canvas = new Canvas(bitmap);
            float scaleX = width / background.getWidth();
            float scaleY = height / background.getHeight();
            Paint paint = new Paint();
            Matrix matrix = new Matrix();
            matrix.postTranslate(0, 0);
            matrix.preScale(scaleX, scaleY);
            canvas.drawBitmap(background,matrix,paint);
            page.setTexture(bitmap, CurlPage.SIDE_FRONT);
            page.setTexture(bitmap, CurlPage.SIDE_BACK);
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
