package com.wxz.ebook.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.wxz.ebook.R;
import com.wxz.ebook.curlUI.CurlPage;
import com.wxz.ebook.curlUI.CurlView;

public class ReadPageActivity extends AppCompatActivity{

    private CurlView curlView;
    private int[] mBitmapIds = { R.drawable.p001, R.drawable.p002,
            R.drawable.p003, R.drawable.p004, R.drawable.p005};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除title
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);//去掉Activity上面的状态栏
        setContentView(R.layout.activity_read_page);

        curlView = findViewById(R.id.read_curl_page_view);
        curlView.setPageProvider(new PageProvider());
        curlView.setSizeChangedObserver(new SizeChangedObserver());
        curlView.setCurrentIndex(0);
        curlView.setBackgroundColor(0xFF202830);
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

    private class PageProvider implements CurlView.PageProvider{

        @Override
        public int getPageCount() {
            return 5;
        }

        @Override
        public void updatePage(CurlPage page, int width, int height, int index) {
            Bitmap front = loadBitmap(width, height, index);
            page.setTexture(front, CurlPage.SIDE_BOTH);
            page.setColor(Color.argb(127, 255, 255, 255),
                    CurlPage.SIDE_BACK);
        }

        private Bitmap loadBitmap(int width, int height, int index) {
            Bitmap b = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            b.eraseColor(0xFFFFFFFF);
            Canvas c = new Canvas(b);
            Drawable d = getResources().getDrawable(mBitmapIds[index]);

            int margin = 0;
            int border = 0;
            Rect r = new Rect(margin, margin, width - margin, height - margin);

            int imageWidth = r.width() - (border * 2);
            int imageHeight = imageWidth * d.getIntrinsicHeight()
                    / d.getIntrinsicWidth();
            if (imageHeight > r.height() - (border * 2)) {
                imageHeight = r.height() - (border * 2);
                imageWidth = imageHeight * d.getIntrinsicWidth()
                        / d.getIntrinsicHeight();
            }

            r.left += ((r.width() - imageWidth) / 2) - border;
            r.right = r.left + imageWidth + border + border;
            r.top += ((r.height() - imageHeight) / 2) - border;
            r.bottom = r.top + imageHeight + border + border;

            Paint p = new Paint();
            p.setColor(0xFFC0C0C0);
            c.drawRect(r, p);
            r.left += border;
            r.right -= border;
            r.top += border;
            r.bottom -= border;

            d.setBounds(r);
            d.draw(c);

            return b;
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
                //curlView.setMargins(.1f, .1f, .1f, .1f);
                curlView.setMargins(.0f, .0f, .0f, .0f);
            }
        }
    }


}
