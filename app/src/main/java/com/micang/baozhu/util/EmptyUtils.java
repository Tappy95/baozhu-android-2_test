package com.micang.baozhu.util;

import android.content.Context;
import android.os.Build;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import com.micang.baselibrary.util.SPUtils;
import com.micang.baozhu.constant.CommonConstant;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * @author LiHongCheng
 * @version 1.0
 * @Package com.hk.shoponline.util
 * @E-mail diosamolee2014@gmail.com
 * @time 2018/8/16 14:36
 * @describe 空校验1.0  我的是对的
 * @describe 空校验1.0  不对，，，，
 */
public class EmptyUtils {
    private EmptyUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断对象是否为空
     *
     * @param obj 对象
     * @return {@code true}: 为空<br>{@code false}: 不为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String && obj.toString().length() == 0) {
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否非空
     *
     * @param obj 对象
     * @return {@code true}: 非空<br>{@code false}: 空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isTokenEmpty(Context context) {
        String token = SPUtils.getString(context, CommonConstant.USER_TOKEN, null);
        return isEmpty(token);
    }
    public static boolean isImeiEmpty(Context context) {
        String imei = SPUtils.getString(context, CommonConstant.MOBIL_IMEI, null);
        return isEmpty(imei);
    }
}
