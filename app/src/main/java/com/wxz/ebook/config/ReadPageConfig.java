package com.wxz.ebook.config;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import com.wxz.ebook.R;
import com.wxz.ebook.tool.AppUtils;

public class ReadPageConfig {

    public int textSize;
    public int textColor;
    public int pageTheme;

    public ReadPageConfig() {
        getReadPageConfig();
    }

    public void setModeIndex(int index){
        switch (index){
            case ReadPageTheme.THEME_PAPER:
                pageTheme = ReadPageTheme.THEME_PAPER;
                textColor = AppUtils.getResource().getColor(R.color.fontColorPaper);
                break;
            case ReadPageTheme.THEME_WHITE:
                pageTheme = ReadPageTheme.THEME_WHITE;
                textColor = AppUtils.getResource().getColor(R.color.fontColorWhite);
                break;
            case ReadPageTheme.THEME_PURPLE:
                pageTheme = ReadPageTheme.THEME_PURPLE;
                textColor = AppUtils.getResource().getColor(R.color.fontColorPurple);
                break;
            case ReadPageTheme.THEME_MICHELIN:
                pageTheme = ReadPageTheme.THEME_MICHELIN;
                textColor = AppUtils.getResource().getColor(R.color.fontColorMichelin);
                break;
            case ReadPageTheme.THEME_GRAY:
                pageTheme = ReadPageTheme.THEME_GRAY;
                textColor = AppUtils.getResource().getColor(R.color.fontColorGray);
                break;
            default:
                pageTheme = ReadPageTheme.THEME_PAPER;
                textColor = AppUtils.getResource().getColor(R.color.fontColorPaper);
                break;
        }
    }

    public Bitmap getModeBitmap(int index){
        Bitmap bitmap;
        switch (index){
            case ReadPageTheme.THEME_PAPER:
                bitmap = BitmapFactory.decodeResource(AppUtils.getResource(), R.drawable.paper);
                break;
            case ReadPageTheme.THEME_WHITE:
                bitmap = BitmapFactory.decodeResource(AppUtils.getResource(), R.drawable.bg2);
                break;
            case ReadPageTheme.THEME_PURPLE:
                bitmap = BitmapFactory.decodeResource(AppUtils.getResource(), R.drawable.bg3);
                break;
            case ReadPageTheme.THEME_MICHELIN:
                bitmap = BitmapFactory.decodeResource(AppUtils.getResource(), R.drawable.bg5);
                break;
            case ReadPageTheme.THEME_GRAY:
                bitmap = BitmapFactory.decodeResource(AppUtils.getResource(), R.drawable.bg4);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(AppUtils.getResource(), R.drawable.paper);
                break;
        }
        return bitmap;
    }

    public Drawable getModeDrawable(int index){
        Drawable drawable;
        switch (index){
            case ReadPageTheme.THEME_PAPER:
                drawable = AppUtils.getResource().getDrawable(R.drawable.paper);
                break;
            case ReadPageTheme.THEME_WHITE:
                drawable = AppUtils.getResource().getDrawable(R.drawable.bg2);
                break;
            case ReadPageTheme.THEME_PURPLE:
                drawable = AppUtils.getResource().getDrawable(R.drawable.bg3);
                break;
            case ReadPageTheme.THEME_MICHELIN:
                drawable = AppUtils.getResource().getDrawable(R.drawable.bg5);
                break;
            case ReadPageTheme.THEME_GRAY:
                drawable = AppUtils.getResource().getDrawable(R.drawable.bg4);
                break;
            default:
                drawable = AppUtils.getResource().getDrawable(R.drawable.paper);
                break;
        }
        return drawable;
    }

    public void saveReadPageConfig(){
        SettingManager.getInstance().saveFontSize(textSize);
        SettingManager.getInstance().saveFontColor(textColor);
        SettingManager.getInstance().savePageTheme(pageTheme);
    }

    public void getReadPageConfig(){
        textSize = SettingManager.getInstance().getFontSize();
        textColor = SettingManager.getInstance().getFontColor();
        pageTheme = SettingManager.getInstance().getPageTheme();
    }
}
