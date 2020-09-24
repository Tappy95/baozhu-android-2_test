package com.micang.baozhu.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/9/26 20:45
 * @describe describe
 */
public class JsonUtil {



    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public static String getJson(Context var0, String var1) {
        try {
            InputStream inputStream  = var0.getResources().getAssets().open(var1);
            String var6 = getString(inputStream);
            return var6;
        } catch (Exception var7) {
            return "";
        }
    }
}
