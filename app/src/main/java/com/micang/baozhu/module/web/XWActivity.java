package com.micang.baozhu.module.web;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bun.supplier.IdSupplier;
import com.micang.baozhu.util.CoordinatesBean;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.GyrosensorUtils;
import com.micang.baselibrary.util.TLog;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xianwan.sdklibrary.util.AppUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.functions.Consumer;

public class XWActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_PHONE_STATE = 100;
    private static final int REQUEST_INSTALL_STATE = 101;
    private static final int REQUEST_WRITE_STORAGE = 102;
    private static final int REQUEST_INSTALL_UNKNOW_SOURCE = 1001;


    private Context mContext;
    private String shortName;
    private String name = "";
    private WebView webView;
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout llBack;
    private TextView tvTitle;
    private String apkName;
    private boolean isFirst = true;
    private boolean canInstall = true;
    private String downloadPath;
    private String imei;
    private String urls;
    private TextView tv_start_download;
    private GyrosensorUtils gyrosensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_xw);
        mContext = this;
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        Intent intent = getIntent();
        shortName = intent.getStringExtra("shortName");
        name = intent.getStringExtra("name");
        webView = findViewById(R.id.webview);
        swipeLayout = findViewById(R.id.swipe_container);
        tv_start_download = findViewById(R.id.tv_start_download);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(name);
        llBack.setOnClickListener(this);
        initWebView();

        //Android6.0需要动态获取权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(XWActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
        } else {
            imei = AppUtil.getDeviceId(mContext);
            getUrls();
            onCallPermission();
        }
        gyrosensor = GyrosensorUtils.getInstance(this);
        //下拉刷新
        swipeLayout.setColorSchemeColors(Color.rgb(243, 89, 41));
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //重新刷新页面
                webView.loadUrl(webView.getUrl());
            }
        });

    }

    /**
     * 加个获取权限的监听
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    imei = TelephonyMgr.getDeviceId();
                } else {
                    Toast.makeText(this, "没有权限不可以试玩游戏", Toast.LENGTH_SHORT).show();
                }
                getUrls();
                onCallPermission();
                break;
            }
            case REQUEST_INSTALL_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AppUtil.installAPK(mContext, new File(downloadPath));
                } else {
                    if (Build.VERSION.SDK_INT >= 26) {
                        AppUtil.jumpInstallPermissionSetting(XWActivity.this, REQUEST_INSTALL_UNKNOW_SOURCE);
                    }
                }
                break;
            }
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
//                    Toast.makeText(this, "你不给权限我就不好干事了啦", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    private void getUrls() {
        HttpUtils.getPCDDUrl(shortName).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                urls = (String) response.data;
                openUrl();
            }
        });
    }

    private void openUrl() {
        WebChromeClient webchromeclient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //隐藏进度条
                    swipeLayout.setRefreshing(false);
                } else {
                    if (!swipeLayout.isRefreshing())
                        swipeLayout.setRefreshing(true);
                }

                super.onProgressChanged(view, newProgress);
            }

            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                ToastUtils.show(message);
                result.confirm();
                return true;
            }
        };
        webView.setWebChromeClient(webchromeclient);
        webView.setWebViewClient(new WebViewClient());
        //加载链接
        webView.loadUrl(urls);
        Log.i("url:", "");
    }


    /**
     * 检查SD卡读写权限
     */
    public void onCallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   //判断当前系统的SDK版本是否大于23

            if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) { //如果当前申请的权限没有授权
                //第一次请求权限的时候返回false,第二次shouldShowRequestPermissionRationale返回true
                //如果用户选择了“不再提醒”永远返回false。
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.RECORD_AUDIO)) {

                    Toast.makeText(this, "Please grant the permission this time", Toast.LENGTH_LONG).show();
                }
                //请求权限
                ActivityCompat.requestPermissions(XWActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            } else {//已经授权了就走这条分支
                Log.i("wei", "onClick granted");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst) {
            webView.loadUrl(webView.getUrl());
        } else {
            isFirst = false;
        }
        addGyro();
    }

    private void addGyro() {
        if (EmptyUtils.isTokenEmpty(this)) {
            return;
        }
        CoordinatesBean coordinates = gyrosensor.getCoordinates();
        String anglex = String.format("%.2f", coordinates.anglex);
        String angley = String.format("%.2f", coordinates.angley);
        String anglez = String.format("%.2f", coordinates.anglez);
        HttpUtils.addGyro(anglex, angley, anglez, 3).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {

            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webSettings.setDomStorageEnabled(true);  //开启DOM Storage功能

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.addJavascriptInterface(XWActivity.this, "android");
    }
    // 定义JS需要调用的方法 被JS调用的方法必须加入@JavascriptInterface注解

    /**
     * 检测是否安装APP
     *
     * @param packageName 包名
     */
    @JavascriptInterface
    public void CheckInstall(String packageName) {
        boolean isInstalled = AppUtil.isApkInstalled(XWActivity.this, packageName);
        if (isInstalled) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:CheckInstall_Return(1)");
                }
            });
        } else {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:CheckInstall_Return(0)");
                }
            });
        }
    }

    /**
     * 打开 App
     *
     * @param packageName 包名
     */
    @JavascriptInterface
    public void OpenAPP(final String packageName) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                if (mContext != null) {
                    AppUtil.startAppByPackageName(mContext, packageName);
                }
            }
        });
    }

    /**
     * 下载 并安装 App
     *
     * @param url 下载地址
     */
    @JavascriptInterface
    public void InstallAPP(final String url) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                if (mContext != null) {
                    installApp(url);
                }
            }
        });
    }

    /**
     * 下载流程
     *
     * @param url
     */
    private void installApp(String url) {
        apkName = AppUtil.getUrlName(url);
        if (TextUtils.isEmpty(apkName) || !canInstall) {
            return;
        }
        canInstall = false;
        apkName = apkName + ".apk";

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "51xianwan" + File.separator + apkName;
            File file = new File(downloadPath);
            int status = AppUtil.getDownState(mContext, url);
            if (status == -1 || (status == DownloadManager.STATUS_SUCCESSFUL && !file.exists())) { // 下载  条件： 不在下载中 或者下载完成但是文件已被删除
                downApp(url);
            } else if (status == DownloadManager.STATUS_SUCCESSFUL) { //下载完成
                int sdkVersion = this.getApplicationInfo().targetSdkVersion;
                if (sdkVersion >= 26 && Build.VERSION.SDK_INT >= 26) {
                    boolean b = getPackageManager().canRequestPackageInstalls();
                    if (b) {
                        AppUtil.installAPK(mContext, new File(downloadPath));
                    } else {
                        ActivityCompat.requestPermissions(XWActivity.this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, REQUEST_INSTALL_STATE);
                    }
                } else {
                    AppUtil.installAPK(mContext, new File(downloadPath));
                }
            }
        } else {
            openAppDetails();
        }
        canInstall = true;
    }

    /**
     * 下载
     *
     * @param url
     */
    private void downApp(final String url) {
        if (!AppUtil.canDownloadState(mContext)) { //用户禁止了内部下载器
            AppUtil.jumpSetting(mContext);
            return;
        }
//        AppUtil.NetState netState = AppUtil.getNetWorkType(mContext.getApplicationContext());
//        if (netState == AppUtil.NetState.NET_NO) {
//            Toast.makeText(mContext, "现在还没有联网哦，请链接网络后重新尝试！", Toast.LENGTH_SHORT).show();
//        } else if (netState == AppUtil.NetState.NET_MOBILE) {
//            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(mContext);
//            normalDialog.setTitle("温馨提醒");
//            normalDialog.setMessage("您现在使用的是非WiFi流量,是否继续?");
//            normalDialog.setPositiveButton("继续下载",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();
//                            DownLoadService.startActionFoo(XWActivity.this, url);
//                        }
//                    });
//            normalDialog.setNegativeButton("取消",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//            normalDialog.show();
//        } else {
//            Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();
//            DownLoadService.startActionFoo(XWActivity.this, url);
//        }
        tv_start_download.setVisibility(View.VISIBLE);
        tv_start_download.setText("开始下载");
        BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(url)
                .setPath(downloadPath)
                .setCallbackProgressTimes(100)
                .setMinIntervalUpdateSpeed(100)
                .setListener(new FileDownloadSampleListener() {

                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.pending(task, soFarBytes, totalBytes);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);

                        if (totalBytes == -1) {
                            tv_start_download.setText("正在下载");
                        } else {
                            int progress = (int) ((Long.valueOf(soFarBytes) * 100) / Long.valueOf(totalBytes));
                            tv_start_download.setText("正在下载" + "(" + progress + "%)");
                        }

                        tv_start_download.setEnabled(false);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        tv_start_download.setVisibility(View.GONE);
                        installAPK(new File(downloadPath), apkName);
                        canInstall = true;
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.paused(task, soFarBytes, totalBytes);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                    }
                });

        baseDownloadTask.start();

    }

    protected void installAPK(File file, String apkName) {
        if (!file.exists())
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            new RxPermissions(XWActivity.this).request(Manifest.permission.REQUEST_INSTALL_PACKAGES).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if (aBoolean) {


                    } else {

                        Toast.makeText(XWActivity.this, "未授权权限，部分功能不能使用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //坑 http://www.jianshu.com/p/c58d17073e65
            File newPath = new File(XWActivity.this.getFilesDir().getPath() + "/downloads");
            if (!newPath.exists()) {
                //通过file的mkdirs()方法创建 目录中包含却不存在的文件夹
                newPath.mkdirs();
            }
            tv_start_download.setText("正在下载");
            String path = newPath.getPath() + "/" + apkName;
            String oldPath = file.getPath();
            copyFile(oldPath, path);
            File newfile = new File(path);
            // 即是在清单文件中配置的authorities
            uri = FileProvider.getUriForFile(AppContext.getInstance(), "com.micang.baozhu.fileProvider", newfile);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);

        } else {
            uri = Uri.parse("file://" + file.toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //在服务中开启activity必须设置flag,后面解释
        startActivity(intent);
    }

    /**
     * copyfile
     */
    public void copyFile(String oldPath, String newPath) {

        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            TLog.d("copy", "error");
            e.printStackTrace();
        }
    }

    /**
     * 打开 APP 的详情设置
     */
    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("你还未获取存储权限哦，现在去获取？");    // 取消后可到 “应用信息 -> 权限” 中授予"
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppUtil.jumpPermissionSetting(mContext);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 网页游戏
     *
     * @param url 网址
     */
    @JavascriptInterface
    public void Browser(final String url) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                if (mContext != null) {
                    AppUtil.jumpToBrowser(mContext, url);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        webView = null;
        mContext = null;
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }
}
