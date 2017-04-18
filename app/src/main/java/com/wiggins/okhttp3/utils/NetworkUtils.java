package com.wiggins.okhttp3.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.wiggins.okhttp3.app.MyApplication;

/**
 * @Description 检查当前网络是否可用
 * @Author 一花一世界
 */

public class NetworkUtils {

    public static Context getContext() {
        return MyApplication.getContext();
    }

    public static boolean isNetworkAvailable() {
        // 获取手机所有连接管理对象（包括对wi-fi、net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
