package com.wxz.ebook.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
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
import com.wxz.ebook.tool.bookFactory.Book;
import com.wxz.ebook.tool.bookFactory.BookConcreteFactory;
import com.wxz.ebook.tool.bookFactory.BookFactory;
import com.wxz.ebook.tool.bookFactory.LocalBook;
import com.wxz.ebook.tool.bookFactory.OnlineBook;
import com.wxz.ebook.tool.bookFactory.OnlineBookListener;
import com.wxz.ebook.tool.utils.AppUtils;
import com.wxz.ebook.tool.utils.DensityUtil;
import com.wxz.ebook.tool.utils.FileHelper;
import com.wxz.ebook.view.ui.curlUI.CurlPage;
import com.wxz.ebook.view.ui.curlUI.CurlView;
import com.wxz.ebook.tool.readFactory.ReadFactory;
import com.wxz.ebook.view.view.ChapterListView;
import com.wxz.ebook.view.view.CoverView;
import com.wxz.ebook.view.view.ReadPageSetView;
import com.wxz.ebook.view.view.ReadPageTextView;

public class ReadPageActivity extends AppCompatActivity implements ReadAsyncTask.ReadInterface ,OnlineBookListener{

    private CoverView coverView;
    private CurlView curlView;
    private ReadFactory nextFactory,lastFactory,readFactory;
    private TextView book_loading;
    private ChapterListView chapterListView;
    private ReadPageSetView readPageSetView;
    private ReadPageTextView readPageTextView;
    private FileHelper helper;
    private ReadAsyncTask readAsyncTask;
    private Book book;
    private BookFactory bookFactory;
    private boolean isgetThisText = false,isgetLastText = false;
    private int pageIndex;

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
        setOnClick();
        initFactory();
        bookFactory = new BookConcreteFactory();

        Intent intent =getIntent();
        ResolveAsyncTask asyncTask = new ResolveAsyncTask(this);
        helper =new FileHelper(this);
        DocBean docBean= (DocBean) intent.getSerializableExtra("docBean");
        BookInfoBean mBookInfoBean = (BookInfoBean) intent.getSerializableExtra("bookInfoBean");

        if(docBean != null){
            asyncTask.execute(docBean.getPath(),docBean.getSize());
            asyncTask.setOnResolveAsyncTaskListener(new ResolveAsyncTask.OnResolveAsyncTaskListener() {
                @Override
                public void resolveTaskListener(BookInfoBean bookInfoBean) {
                    helper.insert(bookInfoBean);
                    initPage(bookInfoBean);
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

            @Override
            public void setTextSize(int progress) {
                setReadTextSize(DensityUtil.sp2px(getBaseContext(),progress*2+10));
                setDataFactory(book.getChapterIndex(),book.getPageIndex());
                //Log.e("TextSize",String.valueOf(progress*2+10));
                //Log.e("pageSize",String.valueOf(book.getPageNum()));
            }
        });
    }

    private void initFactory(){
        readFactory = new ReadFactory();
        nextFactory = new ReadFactory();
        lastFactory = new ReadFactory();
        float size = readFactory.getFontSize();
        int progress = ((int)DensityUtil.px2sp(this,size)-10)/2;
        readPageTextView.setProgress(progress);
    }

    private void setReadTextSize(int textSize){
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
        if (bookInfoBean.bookTybe==0){
            LocalBook localBook  = bookFactory.createRead(LocalBook.class);
            localBook.init(this,bookInfoBean);
            readAsyncTask.execute(localBook.getBuffer());
            book = localBook;
        }else {
            OnlineBook onlineBook = bookFactory.createRead(OnlineBook.class);
            onlineBook.init(this,bookInfoBean);
            onlineBook.setOnlineBookListener(this);
            book = onlineBook;
        }

    }

    private void setDateReadFactory(){
        readFactory = new ReadFactory();
        readFactory.setBookName(book.getThisChapterName());
        if (book.getBookType()==0){
            readFactory.setChapterStr(book.getThisChapterText());
        }else {
            book.getThisChapterText();
        }
    }

    private void setDateLastFactory(){
        lastFactory = new ReadFactory();
        lastFactory.setBookName(book.getLastChapterName());
        if (book.getBookType()==0){
            lastFactory.setChapterStr(book.getLastChapterText());
        }else {
            book.getLastChapterText();
        }
    }

    private void setDateNextFactory(){
        nextFactory = new ReadFactory();
        nextFactory.setBookName(book.getMextChapterName());
        if (book.getBookType()==0){
            nextFactory.setChapterStr(book.getMextChapterText());
        }else {
            book.getMextChapterText();
        }
    }

    public void setDataFactory(int index,int readIndex) {
        curlView.setNewCurl();
        book.setChapterIndex(index);
        isgetThisText = false;
        if(index <= 0){
            isgetLastText = true;
        }else {
            isgetLastText = false;
        }
        pageIndex = readIndex;
        setDateReadFactory();
        setDateLastFactory();
        setDateNextFactory();
        if (book.getBookType() == 0){
            readFactory.readDraw();
            if (book.getPageNum()==0 || readFactory.getPageSize() == book.getPageNum()){
                curlView.setCurrentIndex(pageIndex);
            }else {
                int newPageIndex = (pageIndex * readFactory.getPageSize())/book.getPageNum();
                curlView.setCurrentIndex(newPageIndex);
            }
        }
    }

    public void setDataFactory(boolean b) {//true +1 false -1
        if (b){
            lastFactory = readFactory;
            readFactory = nextFactory;
            book.setChapterIndex(book.getChapterIndex()+1);
            setDateNextFactory();
            helper.update(book.getBookInfoBean());
        }else {
            nextFactory = readFactory;
            readFactory = lastFactory;
            book.setChapterIndex(book.getChapterIndex()-1);
            setDateLastFactory();
            helper.update(book.getBookInfoBean());
        }
    }

    private void setChapterListData(ChapterListBean bean){
        chapterListView.setName(bean.name);
        chapterListView.setDate(bean.listBeans);
        chapterListView.setDurChapter(book.getChapterIndex());
        setDataFactory(book.getChapterIndex(), book.getPageIndex());
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
                book.setChapterIndex(position);
                setDataFactory(book.getChapterIndex(),0);
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
            book.setChapterIndex(book.getChapterIndex()+1);
            book.setPageIndex(0);
            book.setPageNum(nextFactory.getPageSize());
        }else {
            book.setPageIndex(curlView.getCurrentIndex());
            book.setPageNum(readFactory.getPageSize());
        }
        helper.update(book.getBookInfoBean());
        Intent intent = new Intent();
        intent.putExtra("rBookInfoBean", book.getBookInfoBean());
        setResult(2,intent);
        super.onBackPressed();
    }

    @Override
    public void setChapterList(final ChapterListBean chapterListBean) {
        LocalBook localBook = (LocalBook) book;
        localBook.setChapterListBean(chapterListBean);
        book = localBook;
        setChapterListData(chapterListBean);
    }

    @Override
    public void getThisChapterText(String text) {
        readFactory.setChapterStr(text);
        readFactory.readDraw();
        if (!isgetThisText){
            isgetThisText = true;
            if (isgetLastText){
                if (book.getPageNum()==0 || readFactory.getPageSize() == book.getPageNum()){
                    curlView.setCurrentIndex(pageIndex);
                    Log.e("pageIndex","pageIndex");
                }else {
                    int newPageIndex = (pageIndex * readFactory.getPageSize())/book.getPageNum();
                    curlView.setCurrentIndex(newPageIndex);
                    Log.e("pageIndex","newPageIndex");
                }
            }
        }
    }

    @Override
    public void getLastChapterText(String text) {
        lastFactory.setChapterStr(text);
        if (!isgetLastText){
            isgetLastText = true;
            if (isgetThisText){
                if (book.getPageNum()==0 || readFactory.getPageSize() == book.getPageNum()){
                    curlView.setCurrentIndex(pageIndex);
                    Log.e("pageIndex","pageIndex");
                }else {
                    int newPageIndex = (pageIndex * readFactory.getPageSize())/book.getPageNum();
                    curlView.setCurrentIndex(newPageIndex);
                    Log.e("pageIndex","newPageIndex");
                }
            }
        }
    }

    @Override
    public void getMestChapterText(String text) {
        nextFactory.setChapterStr(text);
    }

    @Override
    public void getChapterList(ChapterListBean bean) {
        setChapterListData(bean);
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
            chapterListView.setDurChapter(book.getChapterIndex());
            return 0;
        }

        @Override
        public int lastChapter() {
            setDataFactory(false);
            chapterListView.setDurChapter(book.getChapterIndex());
            return readFactory.getPageSize()-1;
        }

        @Override
        public boolean isNextChapter() {
            return book.getChapterIndex() + 1 >= book.getChapterNum();
        }

        @Override
        public boolean isLastChapter() {
            return book.getChapterIndex() <= 0;
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
