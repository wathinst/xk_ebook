package com.wxz.ebook.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.wxz.ebook.R;
import com.wxz.ebook.ui.curlUI.CurlPage;
import com.wxz.ebook.ui.curlUI.CurlView;

public class PageProvider implements CurlView.PageProvider {

    private Context context;

    public PageProvider(Context context) {
        this.context = context;
    }

    @Override
    public int getPageCount() {
        return 0;
    }

    @Override
    public void updatePage(CurlPage page, int width, int height, int index) {

    }

    @Override
    public int nextChapter() {
        return 0;
    }

    @Override
    public int lastChapter() {
        return 0;
    }

    @Override
    public boolean isNextChapter() {
        return false;
    }

    @Override
    public boolean isLastChapter() {
        return false;
    }

    @Override
    public void defaultPage(CurlPage page, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Bitmap background = BitmapFactory.decodeResource(context.getResources(), R.drawable.paper);
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
