package com.micang.baozhu.util;

import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;
import com.toomee.mengplus.common.utils.FileCachePathUtil;
import com.toomee.mengplus.common.utils.SPUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DownLoadManager {
    private static DownLoadManager mDownLoadManager;
    private static Uri downLoadUri = Uri.parse("content://downloads/my_downloads");
    private static HashMap<String, Long> downloadStatus = new HashMap<>();//用于存储下载的状态
    private DownloadChangeObserver mDownloadObserver;
    private ApkInstallReceiver filter = new ApkInstallReceiver();

    private DownLoadManager() {

    }

    public static DownLoadManager get() {
        if (mDownLoadManager == null) {
            mDownLoadManager = new DownLoadManager();
        }
        return mDownLoadManager;
    }

    /**
     * 返回是否正在下载
     *
     * @param apkId
     * @return
     */
    public boolean isDownLoading(long apkId) {
        return downloadStatus.containsKey(apkId);
    }


    public void download(Context context, String downloadUrl, String apkName, OnDownLoadProgressListener listener) {
        try {
            if (downloadStatus.containsKey(downloadUrl)) {
                long downloadId = downloadStatus.get(downloadUrl);
                int downloadStatus = getDownloadStatus(context, downloadId);
                if (downloadStatus == DownloadManager.STATUS_RUNNING) {
                    Toast.makeText(context, "当前任务正在下载，请开打通知栏查看", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            String fileName = System.currentTimeMillis() + "";
            try {
                // 截取下载链接的后缀名
                fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            File file = getDownLoadTempFile(context, fileName);
            /**安卓包已经存在--直接吊起安装*/
            if (file.exists()) {
                AppUtils.installApp(file);
                return;
            }
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            //注册广播,接收下载成功的状态
            Uri uri = Uri.parse(downloadUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            //如果我们希望下载的文件可以被系统的Downloads应用扫描到并管理，
            // 我们需要调用Request对象的setVisibleInDownloadsUi方法，传递参数true。
            request.setVisibleInDownloadsUi(true);
            request.allowScanningByMediaScanner();
            //设置通知栏总是可见,除非用户手动清理
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            Uri apk = Uri.fromFile(file);
            //设置存储路径
            request.setDestinationUri(apk);
            //设置标题
            request.setTitle(TextUtils.isEmpty(fileName) ? AppUtils.getAppName() : apkName);
            long reference = downloadManager.enqueue(request);
            downloadStatus.put(downloadUrl, reference);//开始下载 把下载id和apkId存储
            //存储id
            SPUtils.getInstance().put(String.valueOf(reference), String.valueOf(reference));

            mDownloadObserver = new DownloadChangeObserver(context, reference, listener);
            context.getContentResolver().registerContentObserver(downLoadUri, true, mDownloadObserver);
            context.getApplicationContext().registerReceiver(filter, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear(long downLoadId) {
        if (downloadStatus.containsValue(downLoadId)) {
            Iterator<Map.Entry<String, Long>> iterator = downloadStatus.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Long> next = iterator.next();
                if (next.getValue() == downLoadId) {
                    iterator.remove();
                }
            }
        }
    }

    public interface OnDownLoadProgressListener {
        void onProgress(String url, int progress);

        void onDownLoadSuccess(String url);

        void onDownLoadFail(String url);
    }

    class DownloadChangeObserver extends ContentObserver {
        private Context mContext;
        private long downLoadId;
        private OnDownLoadProgressListener onDownLoadProgressListener;

        public DownloadChangeObserver(Context context, long downLoadId, OnDownLoadProgressListener listener) {
            super(new Handler());
            mContext = context.getApplicationContext();
            this.downLoadId = downLoadId;
            this.onDownLoadProgressListener = listener;
        }

        @Override
        public void onChange(boolean selfChange) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downLoadId);

            DownloadManager dManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = null;
            try{
                cursor = dManager.query(query);
                if (cursor != null && cursor.moveToFirst()) {
                    String bytesDownload = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    String descrition = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));
                    String id = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                    String localUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    String mimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
                    String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                    // 下载地址
                    String downloadUrl = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
                    int reasonIdx = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                    //String status = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    int status = cursor.getInt(columnIndex);
                    //String totalSize = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    final int totalColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                    final int currentColumn = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                    int totalSize = cursor.getInt(totalColumn);
                    int currentSize = cursor.getInt(currentColumn);
                    float percent = (float) currentSize / (float) totalSize;
                    final int progress = Math.round(percent * 100);
                    if (onDownLoadProgressListener != null) {
                        onDownLoadProgressListener.onProgress(downloadUrl, progress);
                    }

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        //下载成功
                        if (onDownLoadProgressListener != null) {
                            onDownLoadProgressListener.onDownLoadSuccess(downloadUrl);
                        }
                        downloadStatus.remove(downloadUrl);
                        cursor.close();
                        mContext.getContentResolver().unregisterContentObserver(DownloadChangeObserver.this);
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        //下载失败
                        if (onDownLoadProgressListener != null) {
                            onDownLoadProgressListener.onDownLoadFail(downloadUrl);
                        }
                        showErrorMsg(cursor.getInt(reasonIdx));
                        downloadStatus.remove(downloadUrl);
                        cursor.close();
                        mContext.getContentResolver().unregisterContentObserver(DownloadChangeObserver.this);
                        dManager.remove(downLoadId);
                    } else if (status == DownloadManager.STATUS_PAUSED) {
                        //下载暂停
                        if (onDownLoadProgressListener != null) {
                            onDownLoadProgressListener.onDownLoadFail(downloadUrl);
                        }
                        showErrorMsg(cursor.getInt(reasonIdx));
                        downloadStatus.remove(downloadUrl);
                        dManager.remove(downLoadId);
                        cursor.close();
                        mContext.getContentResolver().unregisterContentObserver(DownloadChangeObserver.this);
                    } else if (status == DownloadManager.STATUS_PENDING) {
                        //下载继续
                        //Log.i("pq", "download_STATUS_PENDING");
                    } else if (status == DownloadManager.STATUS_RUNNING) {
                        //下载中
                        //Log.i("pq", "download_STATUS_RUNNING");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    private void showErrorMsg(int code) {
        ToastUtils.show("下载异常,错误码:" + code);
    }


    public static int getDownloadStatus(Context context, long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        try {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            ;
            cursor = downloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //下载状态
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                return status;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private File getDownLoadTempFile(Context context, String name) {
        File apk1 = FileCachePathUtil.getCacheDirectory(context, "apk");
        return new File(apk1, name  + "_temp" + ".apk");
    }
}
