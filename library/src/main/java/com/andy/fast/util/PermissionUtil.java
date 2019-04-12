package com.andy.fast.util;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;


import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.io.File;
import java.util.List;

public class PermissionUtil {

    public interface PermissionListener{
        void onSuccess();
    }

    /**
     * android 6.0以上获取运行时权限
     * @param context
     * @param rationale
     * @param permission
     */
    public static void requestPermission(Context context, Rationale<List<String>> rationale, String... permission) {
        AndPermission.with(context).runtime().permission(permission).rationale(rationale).start();
    }
    /**
     * 安装未知来源 app，需要外部存储的读写权限
     * @param context
     * @param apkFile
     */
    public static void requestInstallApk(Context context,File apkFile){

        AndPermission.with(context)
                .install()
                .file(apkFile)
                .rationale(new Rationale<File>() {
                    @Override
                    public void showRationale(final Context context, File data,final RequestExecutor executor) {
                        // 没有权限会调用该访问，开发者可以在这里弹窗告知用户无权限。
                        // 启动设置：e.execute();
                        // 取消启动：e.cancel();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("提示");
                        builder.setMessage("出于安全考虑，已禁止您的手机安装来自此来源的未知应用");
                        builder.setCancelable(true);
                        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                executor.cancel();
                            }
                        });
                        builder.setNegativeButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                executor.execute();
                            }
                        });
                        builder.show();

                    }

                })
                .onGranted(new Action<File>() {
                    @Override
                    public void onAction(File data) {
                    }
                })
                .onDenied(new Action<File>() {
                    @Override
                    public void onAction(File data) {
                    }
                })
                .start();
    }

    /**
     * android 6.0以上获取在其他 app 顶部绘制权限
     * @param context
     * @param listener
     */
    public static void requestAlertDialog(Context context,final PermissionListener listener){
        AndPermission.with(context)
                .overlay()
                .onGranted(new Action<Void>() {
                    @Override
                    public void onAction(Void data) {
                        listener.onSuccess();
                    }
                })
                .onDenied(new Action<Void>() {
                    @Override
                    public void onAction(Void data) {

                    }
                })
                .start();
    }

    /**
     * android 7.0以上获取 contentUri 的方式，需要外部存储的读写权限
     * @param context 上下文参数
     * @param filePath 要分享的私有文件路径
     * @return content uri
     */
    public static Uri requestUri(Context context, String filePath){
        File file = new File(filePath);
        Uri uri = AndPermission.getFileUri(context, file);
        return uri;
    }

    /**
     * android 8.0以上申请显示通知
     * @param context
     */
    public static void requestNotification(Context context){
        AndPermission.with(context)
                .notification()
                .permission()
                .rationale(new Rationale<Void>() {
                    @Override
                    public void showRationale(Context c, Void d,final RequestExecutor executor) {
                        // 没有权限会调用该访问，开发者可以在这里弹窗告知用户无权限。
                        // 启动设置：e.execute();
                        // 取消启动：e.cancel();
                        AlertDialog.Builder builder = new AlertDialog.Builder(c);
                        builder.setTitle("提示");
                        builder.setMessage("已禁止该应用显示通知，请设置是否显示？");
                        builder.setCancelable(true);
                        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                executor.cancel();
                            }
                        });
                        builder.setNegativeButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                executor.execute();
                            }
                        });
                        builder.show();
                    }
                })
                .onGranted(new Action<Void>() {
                    @Override
                    public void onAction(Void data) {
                        // 可以发送通知。
                    }
                })
                .onDenied(new Action<Void>() {
                    @Override
                    public void onAction(Void data) {
                        // App不能发送通知。
                    }
                })
                .start();
    }

}
