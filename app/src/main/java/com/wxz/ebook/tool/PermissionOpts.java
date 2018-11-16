package com.wxz.ebook.tool;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

/**
 * Created by ${新根} on 2016/11/5.
 * 博客：http://blog.csdn.net/hexingen
 *
 * 用途：权限的操作类
 *
 * 这里是检查联系的权限，且请求相应的权限的案例
 *
 */
public class PermissionOpts {
    public static final String TAG=PermissionOpts.class.getSimpleName();
    private final  Object object=new Object();
    public final static int PHONE_REQUEST_CODE=101;
    public final static String PERMISSION_PHONE= Manifest.permission.CALL_PHONE;

    private Context context;
    public PermissionOpts(Context context){
        this.context=context;
    }
    //-----------------------以下几种检查运用程序权限的方法，效果是一样的。
    /**
     * PackageManager.checkPermission()：检查指定包是否被授予特定权限。（API 1 中出现）
     * 返回结果：授权（PackageManager.PERMISSION_GRANTED），不授权（PERMISSION_DENIED）。
     * 处理的权限被禁止的做法： try catch语句处理
     *
     * @param context
     * @param permission
     * @param packageName
     * @return
     */
    public static  boolean  applyAppPermission(Context context, String permission, String packageName){
        boolean apply=false;
        PackageManager packageManager= context.getPackageManager();
        try{
            int result= packageManager.checkPermission(permission,packageName);
            if(result==PackageManager.PERMISSION_GRANTED){
                apply=true;
            }

        }catch ( Exception e){
            e.printStackTrace();
        }

        return apply;
    }

    /**
     * Context.checkSelfPermission():用于确定是否授予权限，在android api23 框架出现。
     *
     * 返回结果：授权（PackageManager.PERMISSION_GRANTED），不授权（PERMISSION_DENIED）。
     *
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean  checkContextPermission(Context context,String permission){
        boolean checkResult=false;
        try{
            if(Build.VERSION.SDK_INT>=23){//当前手机的系统 的api>M(23)
                //app的目标版本大于23，才使用Context.checkSelfPermission()
                if(getAppTargetVerson(context)>=Build.VERSION_CODES.M){
                    int result=context.checkSelfPermission(permission);
                    if(result==PackageManager.PERMISSION_GRANTED){
                        checkResult=true;
                    }
                }else{//借助v4包下PermissionChecker.checkSelfPermission()
                    checkResult=(
                            PermissionChecker.checkSelfPermission(context,permission)
                                    ==PackageManager.PERMISSION_GRANTED);
                }

            }else{//借助android系统下  PackageManager.checkPermission()
                checkResult=applyAppPermission(context,permission,context.getPackageName());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  checkResult;
    }

    /**
     *  ContextCompat.checkSelfPermission():确定是否已经获取到指定权限，出现在v4包中。
     *  返回结果：授权（PackageManager.PERMISSION_GRANTED），不授权（PERMISSION_DENIED）。
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermissionFromSupportLibrary(Context context,String permission){
        boolean checkResult=false;
        try{
            int result=   ContextCompat.checkSelfPermission(context,permission);
            if(result==PackageManager.PERMISSION_GRANTED){
                checkResult=true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return checkResult;
    }



    /**
     * 获取app的目标版本
     * @param context
     * @return
     */
    public static  int getAppTargetVerson(Context context){
        int verson=0;
        try{
            PackageManager packageManager= context.getPackageManager();
            PackageInfo packageInfo=   packageManager.getPackageInfo(context.getPackageName(),0);
            ApplicationInfo applicationInfo= packageInfo.applicationInfo;
            verson=applicationInfo.targetSdkVersion;
        }catch (Exception e){
            e.printStackTrace();
        }
        return verson;
    }

    /**
     *  ActivityCompat.shouldShowRequestPermissionRationale(): 是否应该显示一个UI控件，来显示请求权限的理由。
     *
     *  使用情况：
     *  1.当某个权限之前已经请求，但是被拒绝，这时调用shouldShowRequestPermissionRationale()返回true.
     *
     *  2.当用户拒绝某个权限，且在权限授权窗口设置不再提醒时，调用shouldShowRequestPermissionRationale()返回false.
     *
     *  3.当设备禁止运用程序获取某个权限时，调用shouldShowRequestPermissionRationale()返回false.
     * @param activity
     * @param permissionName
     * @return
     */
    public static  boolean  checkIfShowPermissionRationale(Activity activity, String permissionName){
        boolean should=false;
        try{
            should= ActivityCompat.shouldShowRequestPermissionRationale(activity,permissionName);
        }catch ( Exception e){
            e.printStackTrace();
        }
        return should;
    }

    /**
     * 未知电话权限的下，进行电话功能
     * @param activity
     * @param phone
     */
    public void unsafetyCallPhone(Activity activity,String phone){
        synchronized (object){
            //检查是否授权
            boolean isGranted=checkContextPermission(context, PERMISSION_PHONE);
            Log.i(TAG," 是否授权 "+isGranted);
            if (isGranted) {
                safertyCallPhone(activity,phone);
            } else {
                //需显示授权的原因： 这里可以显示dialog
                /* 注意点：
                 * 1.要请求的权限，必须在Manifests中注册过的权限。
                 * 2.手机系统必须大于或等于android 6.0(即API23以上)，
                 *  才能使用Context#requestPermissions(activity, permissions, requestCode);
                 */
                boolean isShouldShowRationale=checkIfShowPermissionRationale(activity
                        , PERMISSION_PHONE);
                Log.i(TAG," 是否弹窗解释 "+isShouldShowRationale);

                if (isShouldShowRationale) {
                    //这里弹出一个解释运用程序需要权限原因的弹窗，但是这里忽略这步。
                    ActivityCompat.requestPermissions(activity,
                            new String[]{PERMISSION_PHONE}, PHONE_REQUEST_CODE);

                } else {  //直接请求权限

                    /**因FragmentActivity已经实现了
                     ActivityCompat.OnRequestPermissionsResultCallback，
                     onRequestPermissionsResult ()中可以知道请求权限结果。**/

                    ActivityCompat.requestPermissions(activity,
                            new String[]{PERMISSION_PHONE}, PHONE_REQUEST_CODE);
                }
            }

        }
    }

    /**
     * 已经获取电话权限，进行电话功能
     *
     * 检查手机上程序的是否能解析intent.
     * 1.通过Intent#resolveActivity（）， 返回不为空代表能解析。
     *
     * 2.通过PackageManager#queryIntentXXX():返回一个list,list中item不为零，代表能解析。
     *
     *实际上， PackageManager#queryIntentXXX()返回的list,list中的item 是调用resolveActivity的内容
     *
     *
     * @param activity
     * @param phone
     */
    public void safertyCallPhone(Activity activity,String phone){
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"
                    +  phone));
            if(intent.resolveActivity(activity.getPackageManager())!=null){
                activity.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}