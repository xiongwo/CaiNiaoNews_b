package com.example.liuyuhua.cainiaonews.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v7.app.AlertDialog;

import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.application.CNKApplication;
import com.example.liuyuhua.cainiaonews.entity.UpdateInfoBean;
import com.example.liuyuhua.cainiaonews.service.UpdateService;
import com.example.liuyuhua.cainiaonews.utils.UpdateInformation;

import java.io.File;

/**
 * 接收版本更新的广播，并处理
 * Created by liuyuhua on 2017/5/10.
 */

public class UpdateBroadcastReceiver extends BroadcastReceiver {

    public static final String UPDATE_ACTION = "com.example.liuyuhua.cainiaonews";
    public static final String UPDATE_INFO = "update_info";
    private UpdateInfoBean mUpdateInfoBean;
    private AlertDialog.Builder mDialogBuilder;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            mUpdateInfoBean = (UpdateInfoBean) intent.getSerializableExtra(UpdateBroadcastReceiver.UPDATE_INFO);
            setLocalInfo();
            setServerInfo();
            checkVersion(context);
        }
    }

    // 设置本地版本信息
    private void setLocalInfo() {
        try {
            PackageInfo packageInfo = CNKApplication.getInstance().getPackageManager().
                    getPackageInfo(CNKApplication.getInstance().getPackageName(), 0);
            UpdateInformation.sLocalVersionCode = packageInfo.versionCode;
            UpdateInformation.sLocalVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 设置服务器版本信息
    private void setServerInfo() {
        UpdateInformation.sServerVersionCode = Integer.parseInt(mUpdateInfoBean.getServerVersionCode());
        UpdateInformation.sServerFlag = Integer.parseInt(mUpdateInfoBean.getServerFlag());
        UpdateInformation.sLastForceCode = Integer.parseInt(mUpdateInfoBean.getLastForceCode());
        UpdateInformation.sUpdateUrl = mUpdateInfoBean.getUpdateUrl();
        UpdateInformation.sUpdateInfo = mUpdateInfoBean.getUpdateInfo();
    }

    private void checkVersion(Context context) {
        if (UpdateInformation.sLocalVersionCode < UpdateInformation.sServerVersionCode) {
            // 有更新
            update(context);
        } else {
            // 没有更新
            // 1.自动检测 -- 什么都不做
            // 2.手动检测 -- 告知用户
            notifyUser(context);
            clearUpdateFile(context);
        }
    }

    private void update(Context context) {
        // 1.自动检测 -- 普通更新/强制更新
        // 2.手动检测 -- 普通更新
        if (UpdateInformation.sLocalVersionCode < UpdateInformation.sLastForceCode) {
            // 强制更新
            forceUpdate(context);
        } else {
            // 普通更新
            normalUpdate(context);
        }
    }

    private void forceUpdate(final Context context) {
        mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setTitle("版本更新");
        mDialogBuilder.setMessage(UpdateInformation.sUpdateInfo);

        mDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 启动下载服务
                startDownloadService(context);
            }
        }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 直接退出应用
                System.exit(0);
            }
        }).setCancelable(false).create().show();
    }

    private void normalUpdate(final Context context) {
        mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setTitle("版本更新");
        mDialogBuilder.setMessage(UpdateInformation.sUpdateInfo);

        mDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 启动下载服务
                startDownloadService(context);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void startDownloadService(Context context) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("app_name", UpdateInformation.sAppName);
        intent.putExtra("download_url", UpdateInformation.sUpdateUrl);
        context.startService(intent);
    }

    private void notifyUser(Context context) {
        mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setTitle("版本更新");
        mDialogBuilder.setMessage("当前为最新版本");
        mDialogBuilder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void clearUpdateFile(Context context) {
        File updateDir;
        File updateFile;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            updateDir = new File(Environment.getExternalStorageDirectory(), UpdateInformation.sDownloadDir);
        } else {
            updateDir = context.getFilesDir();
        }

        updateFile = new File(updateDir.getPath(), context.getResources().getString(R.string.app_name) + ".apk");
        if (updateFile.exists()) {
            updateFile.delete();
        }
    }
}
