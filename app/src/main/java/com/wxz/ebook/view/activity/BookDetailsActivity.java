package com.wxz.ebook.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.wxz.ebook.R;
import com.wxz.ebook.api.BookApi;
import com.wxz.ebook.api.BookImgApi;
import com.wxz.ebook.bean.BookDetail;
import com.wxz.ebook.cache.CacheProviders;
import com.wxz.ebook.tool.utils.DateUtil;
import com.wxz.ebook.tool.utils.SizeUtil;
import com.wxz.ebook.view.adapter.SectionsPagerAdapter;
import java.io.InputStream;
import java.util.Objects;
import okhttp3.ResponseBody;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import okhttp3.OkHttpClient;

public class BookDetailsActivity extends AppCompatActivity {

    private BookDetail bookDetail;
    private String book_id;
    private String book_img_url;
    private String book_title;
    private Toolbar toolbar;
    private ImageView book_image;
    private TextView book_name,book_author,book_updated,lately_follower;
    private TextView retention_ratio,word_count,serialize_word_count;

    private TabLayout tabLayout = null;
    private ViewPager viewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private SizeUtil sizeUnit;
    private DateUtil dateUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent =getIntent();
        book_id = intent.getStringExtra("book_id");
        book_img_url = intent.getStringExtra("book_img_url");
        book_title = intent.getStringExtra("book_title");
        if (!book_title.isEmpty()){
            Objects.requireNonNull(getSupportActionBar()).setTitle(book_title);
        }
        initView();
        initData();
    }

    private void initView(){
        book_image = (ImageView)findViewById(R.id.book_details_image);
        book_name = (TextView)findViewById(R.id.book_details_title);
        book_author = (TextView)findViewById(R.id.book_details_author);
        book_updated = (TextView)findViewById(R.id.book_details_updated);
        lately_follower = (TextView)findViewById(R.id.book_details_lately_follower);
        retention_ratio = (TextView)findViewById(R.id.book_details_retention_ratio);
        word_count = (TextView)findViewById(R.id.book_details_word_count);
        serialize_word_count = (TextView)findViewById(R.id.book_details_serialize_word_count);

        tabLayout = (TabLayout) findViewById(R.id.book_details_tablayout);
        viewPager = (ViewPager) findViewById(R.id.book_details_tab_viewpager);
    }

    private void initData(){
        sizeUnit = new SizeUtil();
        dateUnit = new DateUtil();
        if (!book_id.isEmpty()){
            String titleName = "bookDetails" + book_id;
            Observable<BookDetail> bookDetailObservable = BookApi.getInstance(new OkHttpClient())
                    .getBookDetail(book_id);
            CacheProviders.getUserCache(this)
                    .getBookDetails(bookDetailObservable,new DynamicKey(titleName),new EvictDynamicKey(false))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BookDetail>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(BookDetail mBookDetail) {
                            bookDetail = mBookDetail;
                            //tabLayout.setTabMode(TabLayout.MODE_FIXED);
                            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),bookDetail);
                            viewPager.setAdapter(mSectionsPagerAdapter);
                            tabLayout.setupWithViewPager(viewPager);

                            book_name.setText(bookDetail.title);
                            book_author.setText(bookDetail.author);

                            book_updated.setText(dateUnit.getTimeStr(bookDetail.updated));
                            lately_follower.setText(String.valueOf(bookDetail.latelyFollower));
                            retention_ratio.setText(bookDetail.retentionRatio);
                            word_count.setText(sizeUnit.getWordCountStr(bookDetail.wordCount));
                            serialize_word_count.setText(String.valueOf(bookDetail.serializeWordCount));
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });

        }
        if (!book_img_url.isEmpty()){
            String titleName;
            if(book_id.isEmpty()){
                titleName = "BookImage"+ book_img_url;
            }else {
                titleName = "BookImage"+ book_id;
            }
            Observable<ResponseBody> bookImg = BookImgApi.getInstance(new OkHttpClient()).getImg(book_img_url);
            CacheProviders.getUserCache(this).getImg(bookImg,new DynamicKey(titleName),new EvictDynamicKey(true))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            InputStream inputStream = responseBody.byteStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            book_image.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
