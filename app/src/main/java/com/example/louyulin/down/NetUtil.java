package com.example.louyulin.down;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by louyulin on 2019/2/22.
 */

public class NetUtil {
    public static boolean checkNet(Context context) {
        // 判断是否具有可以用于通信渠道
        boolean mobileConnection = isMobileConnection(context);
        boolean wifiConnection = isWIFIConnection(context);
        if ( mobileConnection == false && wifiConnection == false ) {
            // 没有网络
            return false;
        } else {
            if ( mobileConnection ) {
                // IP是10.0.0.172 端口是80 010.000.000.172
                // 读取apn内正在处于使用状态的接入点的代理和端口

                // 访问联系人
                // ContentProvider
//				setApn(context);
            }
        }
        return true;
    }

    public static boolean isMobileConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ( networkInfo != null && networkInfo.isConnected() ) {
            return true;
        }
        return false;
    }

    public static boolean isWIFIConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if ( networkInfo != null && networkInfo.isConnected() ) {
            return true;
        }
        return false;
    }

}
