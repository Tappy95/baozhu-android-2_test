package com.micang.baselibrary.util;

import android.text.TextUtils;
import android.util.Log;

import com.micang.baselibrary.BuildConfig;

import java.util.List;

public class TLog {

    /**
     * 日志输出时的TAG
     */
    private static String tag = "TLog";


    /**
     * 用于记时的变量
     */
    private static long mTimestamp = 0;

    /**---------------日志输出,已固定TAG  begin---------------**/
    /**
     * 以级别为 d 的形式输出LOG
     */
    public static void v(String msg) {
        if (BuildConfig.T_LOG) {
            Log.v (tag, msg );
        }
    }

    /**
     * 以级别为 d 的形式输出LOG
     */
    public static void d(String msg) {
        if (BuildConfig.T_LOG) {
            Log.d (tag, msg );
        }
    }

    /**
     * 以级别为 i 的形式输出LOG
     */
    public static void i(String msg) {
        if (BuildConfig.T_LOG) {
            Log.i (tag, msg );
        }
    }

    /**
     * 以级别为 w 的形式输出LOG
     */
    public static void w(String msg) {
        if (BuildConfig.T_LOG) {
            Log.w (tag, msg );
        }
    }

    /**
     * 以级别为 w 的形式输出Throwable
     */
    public static void w(Throwable tr) {
        if (BuildConfig.T_LOG) {
            Log.w (tag, "", tr );
        }
    }

    /**
     * 以级别为 w 的形式输出LOG信息和Throwable
     */
    public static void w(String msg, Throwable tr) {
        if (BuildConfig.T_LOG && null != msg) {
            Log.w (tag, msg, tr );
        }
    }

    /**
     * 以级别为 e 的形式输出LOG
     */
    public static void e(String msg) {
        if (BuildConfig.T_LOG) {
            Log.e (tag, msg );
        }
    }

    /**
     * 以级别为 s 的形式输出LOG,主要是为了System.out.println,稍微格式化了一下
     */
    public static void sf(String msg) {
        if (BuildConfig.T_LOG) {
            System.out.println ( "----------" + msg + "----------" );
        }
    }

    /**
     * 以级别为 s 的形式输出LOG,主要是为了System.out.println
     */
    public static void s(String msg) {
        if (BuildConfig.T_LOG) {
            System.out.println ( msg );
        }
    }

    /**
     * 以级别为 e 的形式输出Throwable
     */
    public static void e(Throwable tr) {
        if (BuildConfig.T_LOG) {
            Log.e (tag, "", tr );
        }
    }

    /**
     * 以级别为 e 的形式输出LOG信息和Throwable
     */
    public static void e(String msg, Throwable tr) {
        if (BuildConfig.T_LOG && null != msg) {
            Log.e (tag, msg, tr );
        }
    }

    /**---------------日志输出,已固定TAG  end---------------**/

    /**---------------日志输出,未固定TAG  begin---------------**/
    /**
     * 以级别为 d 的形式输出LOG
     */
    public static void v(String tag, String msg) {
        if (BuildConfig.T_LOG) {
            Log.v ( tag, msg );
        }
    }

    /**
     * 以级别为 d 的形式输出LOG
     */
    public static void d(String tag, String msg) {
        if (BuildConfig.T_LOG) {
            Log.d ( tag, msg );
        }
    }

    /**
     * 以级别为 i 的形式输出LOG
     */
    public static void i(String tag, String msg) {
        if (BuildConfig.T_LOG) {
            Log.i ( tag, msg );
        }
    }

    /**
     * 以级别为 w 的形式输出LOG
     */
    public static void w(String tag, String msg) {
        if (BuildConfig.T_LOG) {
            Log.w ( tag, msg );
        }
    }

    /**
     * 以级别为 e 的形式输出LOG
     */
    public static void e(String tag, String msg) {
        if (BuildConfig.T_LOG) {
            Log.e ( tag, msg );
        }
    }

    /**
     * 以级别为 e 的形式输出msg信息,附带时间戳，用于输出一个时间段起始点
     *
     * @param msg 需要输出的msg
     */
    public static void msgStartTime(String msg) {
        mTimestamp = System.currentTimeMillis ();
        if (!TextUtils.isEmpty ( msg )) {
            e ( "[Started：" + mTimestamp + "]" + msg );
        }
    }

    /**
     * 以级别为 e 的形式输出msg信息,附带时间戳，用于输出一个时间段结束点* @param msg 需要输出的msg
     */
    public static void elapsed(String msg) {
        long currentTime = System.currentTimeMillis ();
        long elapsedTime = currentTime - mTimestamp;
        mTimestamp = currentTime;
        e ( "[Elapsed：" + elapsedTime + "]" + msg );
    }

    public static <T> void printList(List<T> list) {
        if (list == null || list.size () < 1) {
            return;
        }
        int size = list.size ();
        i ( "---begin---" );
        for (int i = 0; i < size; i++) {
            i ( i + ":" + list.get ( i ).toString () );
        }
        i ( "---end---" );
    }

    public static <T> void printArray(T[] array) {
        if (array == null || array.length < 1) {
            return;
        }
        int length = array.length;
        i ( "---begin---" );
        for (int i = 0; i < length; i++) {
            i ( i + ":" + array[i].toString () );
        }
        i ( "---end---" );
    }
}
