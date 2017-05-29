package com.example.androidlib.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/** OkHttp 工具类封装
 * Created by liuyuhua on 2017/2/18.
 */

public class OkHttpManager {

    private static OkHttpManager sOkHttpManager;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;

    // 由对外公开的类方法中调用，创建OkHttpManager的类对象
    private static OkHttpManager getInstance() {
        if (sOkHttpManager == null) {
            sOkHttpManager = new OkHttpManager();
        }
        return sOkHttpManager;
    }

    private OkHttpManager() {
        mOkHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
    }

    //*****************************内部处理的方法*****************************

    /**
     * 同步get请求，返回String数据
     * @param targetUrl
     * @return
     * @throws IOException
     */
    private String pri_getSync(String targetUrl) throws IOException {
        Request request = new Request.Builder().url(targetUrl).build();
        Response response = mOkHttpClient.newCall(request).execute();
        String result = response.body().toString();
        return result;
    }

    /**
     * 异步get请求
     * @param targetUrl
     * @param dataCallBack
     */
    private void pri_getAsync(String targetUrl, final DataCallBack dataCallBack) {
        final Request request = new Request.Builder().url(targetUrl).build();
        if (request.body() != null) {
            Log.i("RequestBody", request.body().toString());
        }
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, dataCallBack);
            }

            /**
             * 成功则返回String数据
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                deliverDataSuccess(result, dataCallBack);
            }
        });
    }

    /**
     * 异步post请求
     * @param targetUrl
     * @param hashMap 请求参数
     * @param dataCallBack
     */
    private void pri_postAsync(String targetUrl, HashMap<String, String> hashMap, final DataCallBack dataCallBack) {
        FormBody.Builder builder = new FormBody.Builder();
        for (HashMap.Entry<String, String> entry: hashMap.entrySet()) {
            String key = entry.getKey();
            String value;
            if (entry.getValue() == null) {
                value = "";
            } else {
                value = entry.getValue();
            }
            builder.add(key, value);
        }
        RequestBody requestBody = builder.build();

        final Request request = new Request.Builder().url(targetUrl).post(requestBody).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, dataCallBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().toString();
                deliverDataSuccess(result, dataCallBack);
            }
        });
    }

    /**
     * 异步下载文件
     * @param targetUrl
     * @param targetDirectory 文件存储的绝对路径
     * @param dataCallBack
     */
    private void pri_downloadAsync(final String targetUrl, final String targetDirectory, final DataCallBack dataCallBack) {
        final Request request = new Request.Builder().url(targetUrl).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, dataCallBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                try {
                    inputStream = response.body().byteStream();

                    File file = new File(targetDirectory, getFileName(targetUrl));
                    fileOutputStream = new FileOutputStream(file);

                    byte[] buffer = new byte[2048];
                    int length;
                    while ( (length = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, length);
                    }
                    fileOutputStream.flush();

                    deliverDataSuccess(file.getAbsolutePath(), dataCallBack);
                } catch (IOException e) {
                    deliverDataFailure(request, e, dataCallBack);
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                }
            }
        });
    }

    /**
     * 从URL中提取文件名
     * @param url
     * @return
     */
    // 1、url格式正确且存在文件名  2、url格式正确但不存在文件名（请求可以成功，但没有文件名）  3、url格式不正确（应该在发送请求时就会出现问题）
    private String getFileName(String url) {
        String fileName;
        int separatorIndex = url.lastIndexOf("/");
        if (separatorIndex < 0) {
            fileName = null;
        } else {
            fileName = url.substring(separatorIndex + 1, url.length());
        }
//        String fileName = (separatorIndex < 0) ? url : url.substring(separatorIndex + 1, url.length());
        return  fileName;
    }

    //*****************************数据分发的方法*****************************

    /**
     * 分发请求失败的数据情况
     * @param request
     * @param e
     * @param dataCallBack
     */
    private void deliverDataFailure(final Request request, final IOException e, final DataCallBack dataCallBack ) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (dataCallBack != null) {
                    dataCallBack.requestFailure(request, e);
                }
            }
        });
    }

    /**
     * 分发请求成功的数据情况
     * @param result
     * @param dataCallBack
     */
    private void deliverDataSuccess(final String result, final DataCallBack dataCallBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (dataCallBack != null) {
                    dataCallBack.requestSuccess(result);
                }
            }
        });
    }

    //*****************************对外公开的数据回调接口*****************************
    public interface DataCallBack {

        /**
         * 请求失败
         * @param request
         * @param e
         */
        void requestFailure(Request request, IOException e);

        /**
         * 请求成功
         * @param result
         */
        void requestSuccess(String result);
    }

    //*****************************对外公开的类方法*****************************

    // 同步get
    public static String getSync(String targetUrl) throws IOException {
        return getInstance().pri_getSync(targetUrl);
    }

    // 异步get
    public static void getAsync(String targetUrl, DataCallBack dataCallBack) {
        getInstance().pri_getAsync(targetUrl, dataCallBack);
    }

    // post提交表单
    public static void postAsync(String targetUrl, HashMap<String, String> hashMap, DataCallBack dataCallBack) {
        getInstance().pri_postAsync(targetUrl, hashMap, dataCallBack);
    }

    // 文件下载
    public static void downloadAsync(String targetUrl, String targetDirectory, DataCallBack dataCallBack) {
        getInstance().pri_downloadAsync(targetUrl, targetDirectory, dataCallBack);
    }
}
