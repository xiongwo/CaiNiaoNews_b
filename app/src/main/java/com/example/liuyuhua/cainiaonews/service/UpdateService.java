package com.example.liuyuhua.cainiaonews.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.androidlib.utils.CalculateMemory;
import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.application.CNKApplication;
import com.example.liuyuhua.cainiaonews.utils.UpdateInformation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * “版本更新”服务
 * Created by liuyuhua on 2017/5/11.
 */

public class UpdateService extends Service {

    private static final String TAG = UpdateService.class.getSimpleName();

    private static int sDownloadNotifyId = 0;

    private static final int DOWNLOAD_COMPLETE = 1; // 下载完成
    private static final int DOWNLOAD_MEMORY_INSUF = -1; // 内存不足，停止下载
    private static final int DOWNLOAD_FAIL = -2; // 下载失败

    private String mAppName;
    private String mDownloadUrl;
    private Intent mUpdateIntent;
    private PendingIntent mUpdatePendingIntent;
    private NotificationManager mUpdateNotificationManager;
    private NotificationCompat.Builder mUpdateNotificationBuilder;

    private File mUpdateDir = null; // 文件目录
    private File mUpdateFile = null; // 升级文件

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mAppName = intent.getStringExtra("app_name");
        mDownloadUrl = intent.getStringExtra("download_url");

        mUpdateIntent = new Intent(this, CNKApplication.class);
        mUpdatePendingIntent = PendingIntent.getActivity(this, 0, mUpdateIntent, 0);
        mUpdatePendingIntent.cancel();

        mUpdateNotificationBuilder = new NotificationCompat.Builder(this).
                setSmallIcon(R.mipmap.ic_launcher).
                setContentTitle(mAppName + " 正在下载").
                setContentText("Download in progress").
                setContentInfo("0%");

        mUpdateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        new DownloadUpdateThread().execute();

        return START_REDELIVER_INTENT;
    }

    private class DownloadUpdateThread extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            return executeDownload(mDownloadUrl);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            mUpdateNotificationBuilder.setProgress(0, 0, false);

            if (integer == DOWNLOAD_COMPLETE) {
                Log.d(TAG, "下载成功");

                mUpdateNotificationBuilder.setContentText("下载完成");
                mUpdateNotificationManager.notify(sDownloadNotifyId, mUpdateNotificationBuilder.build());

                String cmd = "chmod 777 " + mUpdateFile.getPath();
                try {
                    Runtime.getRuntime().exec(cmd);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Uri uri = Uri.fromFile(mUpdateFile);
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                installIntent.setDataAndType(uri, "application/vnd.android.package-archive");

                UpdateService.this.startActivity(installIntent);
                stopSelf();
            } else if (integer == DOWNLOAD_MEMORY_INSUF) {
                Log.d(TAG, "内存不够，下载失败");

                mUpdateNotificationBuilder.setContentText("内存不够，请清理空间后重新下载");
                mUpdateNotificationManager.notify(sDownloadNotifyId, mUpdateNotificationBuilder.build());
                stopSelf();
            } else if (integer == DOWNLOAD_FAIL) {
                Log.d(TAG, "下载失败");

                mUpdateNotificationBuilder.setContentText("下载过程中出错，请重新下载");
                mUpdateNotificationManager.notify(sDownloadNotifyId, mUpdateNotificationBuilder.build());
                stopSelf();
            }
        }
    }

    private int executeDownload(String downloadUrl) {
        // int 最大可存储大约1.99G
        int totalSize = 0; // 文件总大小，单位为byte
        int downloadSize = 0; // 下载了多少，单位为byte
        HttpURLConnection httpURLConnection = null;
        BufferedInputStream bufferedIn = null;
        BufferedOutputStream bufferedOut = null;
        try {
            URL url = new URL(downloadUrl);
            URLConnection urlConnection = url.openConnection();
            httpURLConnection = (HttpURLConnection) urlConnection;

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 获取文件总大小
                totalSize = httpURLConnection.getContentLength(); // 可能会出错！！！！！！！！！！！！！！！！！
                // 判断SD卡或内存是否够用
                if (MemoryAvailable(totalSize)) {
                    // 缓冲输入流
                    bufferedIn = new BufferedInputStream(httpURLConnection.getInputStream());
                    // 缓冲输出流
                    bufferedOut = new BufferedOutputStream(new FileOutputStream(mUpdateFile));

                    byte[] buffer = new byte[1024];
                    int length = 0;
                    while ((length = bufferedIn.read(buffer)) != -1) {
                        bufferedOut.write(buffer, 0, length);
                        // 记录下载了多少字节
                        downloadSize += length;
                        // 修改通知，让通知显示下载进度
                        int percent = downloadSize / totalSize;
                        String s = String.valueOf(percent);
                        mUpdateNotificationBuilder.setContentInfo(s + "%");
                        mUpdateNotificationBuilder.setProgress(totalSize, downloadSize, false);
                        mUpdateNotificationManager.notify(sDownloadNotifyId, mUpdateNotificationBuilder.build());
                    }
                    bufferedOut.flush();
                    // 比较下载和总的文件的大小
                    if (downloadSize >= totalSize) {
                        return DOWNLOAD_COMPLETE;
                    } else {
                        return DOWNLOAD_FAIL;
                    }
                } else {
                    // 内存不足，停止下载
                    return DOWNLOAD_MEMORY_INSUF;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedOut != null) {
                    bufferedOut.close();
                }
                if (bufferedIn != null) {
                    bufferedIn.close();
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return DOWNLOAD_FAIL;
    }

    private boolean MemoryAvailable(long fileSize) {
        fileSize += (1024 << 10);
        // 判断SD卡是否可用
        if (CalculateMemory.IsExternalMemoryAvailable()) {
            // 判断SD卡内存是否够用
            if (CalculateMemory.getAvailableExternalMemorySize() <= fileSize) {
                // SD卡内存不够用，判断内存是否够用
                if (CalculateMemory.getAvailableInternalMemorySize() > fileSize) {
                    createFile(false);
                    return true;
                } else {
                    return false;
                }
            } else {
                // SD卡内存够用
                createFile(true);
                return true;
            }
        } else {
            // SD卡不可用，判断内存是否够用
            if (CalculateMemory.getAvailableInternalMemorySize() > fileSize) {
                createFile(false);
                return true;
            } else {
                return false;
            }
        }
    }

    private void createFile(boolean isSDAvailable) {
        // 创建目录对象
        if (isSDAvailable) {
            mUpdateDir = new File(Environment.getExternalStorageDirectory(), UpdateInformation.sDownloadDir);
        } else {
            mUpdateDir = getFilesDir();
        }

        // 创建文件对象
        if (mUpdateDir.exists()) {
            mUpdateFile = new File(mUpdateDir.getPath(), mAppName + ".apk");
        } else {
            if (mUpdateDir.mkdirs()) {
                mUpdateFile = new File(mUpdateDir.getPath(), mAppName + ".apk");
            }
        }

        // 创建文件
        if (!mUpdateFile.exists()) {
            try {
                mUpdateFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (mUpdateFile.delete()) {
                try {
                    mUpdateFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
