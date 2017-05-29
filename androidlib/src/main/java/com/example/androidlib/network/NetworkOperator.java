package com.example.androidlib.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 网络操作
 * Created by liuyuhua on 2017/5/19.
 */

public class NetworkOperator {

    /**
     * 下载数据，保存到outputStream中
     * @param urlString
     * @param outputStream
     * @return
     */
    public static boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection httpURLConnection = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream(), 8 * 1024);
                bufferedOutputStream = new BufferedOutputStream(outputStream, 8 * 1024);
                int i;
                // 读一个字节，写一个字节
                while ((i = bufferedInputStream.read()) != -1) {
                    bufferedOutputStream.write(i);
                }
                return true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将String写到outputStream中
     * @param resultString
     * @param outputStream
     * @return
     */
    public static boolean writeStringToStream(String resultString, OutputStream outputStream) {
        ByteArrayInputStream byteArrayInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            byte[] resultBytes = resultString.getBytes();
            byteArrayInputStream = new ByteArrayInputStream(resultBytes);
            bufferedOutputStream = new BufferedOutputStream(outputStream, 8 * 1024);
            int i;
            while ((i = byteArrayInputStream.read()) != -1) {
                bufferedOutputStream.write(i);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
