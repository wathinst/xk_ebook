package com.wxz.ebook.base;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*********************************************************************************
 *                    The 996ICU License (996ICU)
 *                      Version 0.1, March 2019
 *
 *   PACKAGE is distributed under LICENSE with the following restriction:
 *
 *   The above license is only granted to entities that act in concordance
 *   with local labor laws. In addition, the following requirements must be
 *   observed:
 *
 *   * The licensee must not, explicitly or implicitly, request or schedule
 *     their employees to work more than 45 hours in any single week.
 *   * The licensee must not, explicitly or implicitly, request or schedule
 *     their employees to be at work consecutively for 10 hours.
 *   *********************************************************************************
 *                             类信息
 *
 *   开发者：  wxz
 *   日  期：  2019年04月11日
 *   描  述：
 *   *********************************************************************************/
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //集成bugly
        Context context = getApplicationContext();// 获取当前包名
        String packageName = context.getPackageName();// 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());// 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));// 初始化Bugly
        CrashReport.initCrashReport(getApplicationContext());
        // 如果通过“AndroidManifest.xml”来配置APP信息，初始化方法如下
        // CrashReport.initCrashReport(context, strategy);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
