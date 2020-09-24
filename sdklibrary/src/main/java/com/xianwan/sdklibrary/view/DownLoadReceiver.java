package com.xianwan.sdklibrary.view;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.xianwan.sdklibrary.util.AppUtil;

import java.io.File;

public class DownLoadReceiver extends BroadcastReceiver {
    long mTaskId;
    DownloadManager downloadManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {

            long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            SharedPreferences sPreferences = context.getSharedPreferences("xw", 0);
            mTaskId = sPreferences.getLong("taskid", 0);
            if (myDwonloadID != -1) {
                mTaskId = myDwonloadID;
            }
            checkDownloadStatus(context, mTaskId);
        }

    }


    //检查下载状态
    private void checkDownloadStatus(Context context, long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);//筛选下载任务，传入任务ID，可变参数
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String apkName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    Log.i("DownLoadService", ">>>下载暂停");
                case DownloadManager.STATUS_PENDING:
                    Log.i("DownLoadService", ">>>下载延迟");
                case DownloadManager.STATUS_RUNNING:
                    Log.i("DownLoadService", ">>>正在下载");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.i("DownLoadService", ">>>下载完成");
                    //下载完成安装APK
                    String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "51xianwan" + File.separator + apkName;
                    AppUtil.installAPK(context, new File(downloadPath));
                    break;
                case DownloadManager.STATUS_FAILED:
                    Log.i("DownLoadService", ">>>下载失败");
                    break;
            }
        }
    }

}
