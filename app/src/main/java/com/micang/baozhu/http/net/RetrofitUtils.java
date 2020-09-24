package com.micang.baozhu.http.net;

import com.micang.baselibrary.util.TLog;


import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    //cuijianqiang.2020/8/22
//    private static String BASE_SMS_CODE_URL = "http://106.53.85.158:8080"; //内网
//    private static String BASE_LOGIN_URL = "http://106.53.85.158:8082";   //内网
//    public static String H5url = "";//内网

    private static String BASE_SMS_CODE_URL = "http://lottery.shouzhuan518.com/push/";   //正式
    private static String BASE_LOGIN_URL = "http://lottery.shouzhuan518.com";   //正式
    public static String H5url = "http://web.shouzhuan518.com";//正式

    private static Retrofit getInstance(String url) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                TLog.d("MiCang", "retrofitBack = " + message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        client.retryOnConnectionFailure();
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * @return
     */
    public static ApiService getSmsCodeApi() {
        return getInstance(BASE_SMS_CODE_URL).create(ApiService.class);
    }

    /**
     * @return
     */
    public static ApiService loginApi() {
        return getInstance(BASE_LOGIN_URL).create(ApiService.class);
    }

    public static String getBaseWebUrl(String url) {
        if (!H5url.endsWith("/") && !url.startsWith("/")) {
            return H5url + "/" + url;
        }
        return H5url + url;
    }

    public static String setBaseH5Url(String url) {
        H5url = url;
        return H5url;
    }

}
