package com.micang.baozhu.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.toomee.mengplus.common.utils.LogUtils;
import com.toomee.mengplus.common.utils.SPUtils;

import java.io.File;

public class ApkInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.i("pq","ApkInstallReceiver onReceive download success msg!");
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            LogUtils.i("pq","ApkInstallReceiver:downloadApkId"+downloadApkId);
            DownLoadManager.get().clear(downloadApkId);
            installApk(context, downloadApkId);
        }
    }

    /**
     * 安装apk
     */
    private void installApk(Context context, long downloadApkId) {
        // 获取存储ID
        try {
            long downId = Long.parseLong(SPUtils.getInstance().getString(String.valueOf(downloadApkId), "-1"));
            LogUtils.i("pq","ApkInstallReceiver:cacheId"+downId);
            if (downloadApkId == downId) {
                DownloadManager downManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri downloadFileUri = downManager.getUriForDownloadedFile(downloadApkId);
                File file = new File(getRealFilePath(context, downloadFileUri));
                AppUtils.installApp(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getLastFile(boolean isSuccessRename, File file, String lastName) {
        File lastFile = file;
        if (isSuccessRename) {
            //重命名成功
            lastFile = new File(file.getParent() + File.separator + lastName);
        }
        return lastFile;
    }


    public String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
