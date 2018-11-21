/*****************
 * 阅读设置
 */
package com.wxz.ebook.config;

import com.wxz.ebook.R;
import com.wxz.ebook.tool.utils.AppUtils;
import com.wxz.ebook.tool.utils.ScreenUtils;
import com.wxz.ebook.tool.utils.ToastUtils;

/**
 * @author 韦行志
 * @date 2018/7/26.
 */
public class SettingManager {

    private volatile static SettingManager manager;

    public static SettingManager getInstance() {
        return manager != null ? manager : (manager = new SettingManager());
    }

    /**
     * 保存书籍阅读字体大小
     * @param fontSizePx 文字大小
     */
    public void saveFontSize(int fontSizePx) {
        // 书籍对应
        SharedPreferencesUtil.getInstance().putInt("fontSize", fontSizePx);
    }

    public int getFontSize() {
        return SharedPreferencesUtil.getInstance().getInt("fontSize", (int)ScreenUtils.spToPx(18));
    }

    public void saveFontColor(int fontColor) {
        // 书籍对应
        SharedPreferencesUtil.getInstance().putInt("fontColor", fontColor);
    }

    public int getFontColor() {
        return SharedPreferencesUtil.getInstance().getInt("fontColor", AppUtils.getResource().getColor(R.color.fontColorPaper));
    }

    public void savePageTheme(int pageTheme) {
        // 书籍对应
        SharedPreferencesUtil.getInstance().putInt("pageTheme", pageTheme);
    }

    public int getPageTheme() {
        return SharedPreferencesUtil.getInstance().getInt("pageTheme", ReadPageTheme.THEME_PAPER);
    }

    /**
     * 保存阅读界面屏幕亮度
     * @param percent 亮度比例 0~100
     */
    public void saveReadBrightness(int percent) {
        if(percent > 100){
            ToastUtils.showToast("saveReadBrightnessErr CheckRefs");
            percent = 100;
        }
        SharedPreferencesUtil.getInstance().putInt("readLightness", percent);
    }


    /**
     * 是否可以使用音量键翻页
     * @param enable 音量键翻页
     */
    public void saveVolumeFlipEnable(boolean enable) {
        SharedPreferencesUtil.getInstance().putBoolean("volumeFlip", enable);
    }

    public boolean isVolumeFlipEnable() {
        return SharedPreferencesUtil.getInstance().getBoolean("volumeFlip", true);
    }

    public void saveAutoBrightness(boolean enable) {
        SharedPreferencesUtil.getInstance().putBoolean("autoBrightness", enable);
    }

    public boolean isAutoBrightness() {
        return SharedPreferencesUtil.getInstance().getBoolean("autoBrightness", false);
    }


    /**
     * 保存用户选择的性别
     * @param sex male female
     */
    public void saveUserChooseSex(String sex) {
        SharedPreferencesUtil.getInstance().putString("userChooseSex", sex);
    }

    /**
     * 获取用户选择性别
     * @return 性别
     */
    public String getUserChooseSex() {
        return SharedPreferencesUtil.getInstance().getString("userChooseSex");
    }

    public boolean isUserChooseSex() {
        return SharedPreferencesUtil.getInstance().exists("userChooseSex");
    }

    public boolean isNoneCover() {
        return SharedPreferencesUtil.getInstance().getBoolean("isNoneCover", false);
    }

    public void saveNoneCover(boolean isNoneCover) {
        SharedPreferencesUtil.getInstance().putBoolean("isNoneCover", isNoneCover);
    }
}
