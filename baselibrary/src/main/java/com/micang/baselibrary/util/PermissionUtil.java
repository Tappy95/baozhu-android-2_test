package com.micang.baselibrary.util;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;

import java.lang.reflect.Method;

/**
 * Created by xielei on 28/03/2017.
 */
public class PermissionUtil {

  public static boolean isPermissionGranted(@NonNull Context context, @NonNull String permission) {
    return PermissionChecker.checkSelfPermission(context, permission)
        == PermissionChecker.PERMISSION_GRANTED;
  }

  public static boolean isPermissionDenied(@NonNull Context context, @NonNull String permission) {
    return PermissionChecker.checkSelfPermission(context, permission)
        == PermissionChecker.PERMISSION_DENIED;
  }

  public static boolean isPermissionDeniedByAppOp(@NonNull Context context,
      @NonNull String permission) {
    return PermissionChecker.checkSelfPermission(context, permission)
        == PermissionChecker.PERMISSION_DENIED_APP_OP;
  }

  public static boolean isPermissionGranted(@NonNull Context context, @NonNull String... perms) {
    for (String perm : perms) {
      if (!isPermissionGranted(context, perm)) {
        return false;
      }
    }

    return true;
  }

  /**
   * 判断 悬浮窗口权限是否打开
   *
   * @param context
   * @return true 允许  false禁止
   */
  public static boolean getAppOps(Context context) {
    try {
      @SuppressLint("WrongConstant") Object object = context.getSystemService("appops");
      if (object == null) {
        return false;
      }
      Class localClass = object.getClass();
      Class[] arrayOfClass = new Class[3];
      arrayOfClass[0] = Integer.TYPE;
      arrayOfClass[1] = Integer.TYPE;
      arrayOfClass[2] = String.class;
      Method method = localClass.getMethod("checkOp", arrayOfClass);
      if (method == null) {
        return false;
      }
      Object[] arrayOfObject1 = new Object[3];
      arrayOfObject1[0] = Integer.valueOf(24);
      arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
      arrayOfObject1[2] = context.getPackageName();
      int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
      return m == AppOpsManager.MODE_ALLOWED;
    } catch (Exception ex) {

    }
    return false;
  }
}