package com.xianwan.sdklibrary.view;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.xianwan.sdklibrary.util.AppUtil;

import java.io.File;

public class DownLoadService extends IntentService {
    private static final String ACTION_FOO = "com.xw.xianwan.action.FOO";
    private static final String EXTRA_PARAM1 = "com.xw.xianwan.extra.PARAM1";

    public static String mCurDownUrl;
    private String downloadUrl;
    private DownloadManager downloadManager;
    private String apkName = "";
    private long mTaskId;

    public DownLoadService() {
        super("DownLoadService");
    }

    public static void startActionFoo(Context context, String param1) {
        Intent intent = new Intent(context, DownLoadService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                downloadUrl = intent.getStringExtra(EXTRA_PARAM1);
                if (!TextUtils.isEmpty(downloadUrl)) {
                    handleActionFoo(downloadUrl);
                }
            }
        }
    }

    private void handleActionFoo(String param1) {
        downloadAPK(param1);
    }


    //使用系统下载器下载
    private void downloadAPK(String versionUrl) {
        //新建一个File，传入文件夹目录
        apkName = AppUtil.getUrlName(versionUrl);
        if (TextUtils.isEmpty(apkName)) {
            return;
        }
        apkName = apkName + ".apk";
        String downloadPath;

        if (AppUtil.hasSD()) {
            downloadPath = Environment.getExternalStorageDirectory() + "/51xianwan";
        } else {
            Toast.makeText(getApplicationContext(), "您还没有没有内存卡哦!", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(downloadPath);
        if (file.exists() && versionUrl.equals(mCurDownUrl)) {
            return;
        }
        if (!file.exists()) {
            file.mkdirs();
        }

        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionUrl));

        request.setAllowedOverRoaming(false);//漫游网络是否可以下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(versionUrl));
        request.setMimeType(mimeString);

        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true); //显示下载进度

        //sdcard的目录下的download文件夹，必须设置
        request.setDestinationInExternalPublicDir("/51xianwan/", apkName);

        //将下载请求加入下载队列
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        //加入下载队列后会给该任务返回一个long型的id，
        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        mTaskId = downloadManager.enqueue(request);

        mCurDownUrl = versionUrl;

        SharedPreferences sp = getSharedPreferences("xw", MODE_PRIVATE);
        sp.edit().putLong("taskid", mTaskId).commit();//保存此次下载ID
        sp.edit().putString("apkname", apkName).commit();
    }

}
