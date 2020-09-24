package com.xianwan.sdklibrary.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xianwan.sdklibrary.util.AppUtil;
import com.xianwan.sdklibrary.util.MResource;
import com.xianwan.sdklibrary.util.StatusBarUtil;
import com.xianwan.sdklibrary.util.XWUtils;

import java.io.File;

public class AdListActivity extends AppCompatActivity {

    private static final int REQUEST_PHONE_STATE = 100;
    private static final int REQUEST_INSTALL_STATE = 101;
    private static final int REQUEST_WRITE_STORAGE = 102;
    private static final int REQUEST_INSTALL_UNKNOW_SOURCE = 1001;

    private String imei;
    private WebView webView;
    private View actionBarView;
    private ImageView backView;
    private TextView titleView;
    private String appid, secret, appsign, adId;
    private int mode;
    private SwipeRefreshLayout swipeLayout;
    private String downloadPath;
    private String apkName;
    private boolean isFirst = true;
    private String actionTitle;
    private Context mContext;
    private boolean canInstall = true;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        appid = XWUtils.getInstance(mContext).getAppId();
        secret = XWUtils.getInstance(mContext).getAppSecret();
        appsign = XWUtils.getInstance(mContext).getAppSign();
        mode = XWUtils.getInstance(mContext).getMode();
        if (TextUtils.isEmpty(appid) || TextUtils.isEmpty(secret) || TextUtils.isEmpty(appsign)) {
            finish();
        }
        if (mode == 1) {
            adId = XWUtils.getInstance(mContext).getAdId();
            if (TextUtils.isEmpty(adId)) {
                finish();
            }
        }
        setContentView(MResource.getIdByName(getApplication(), "layout", "activity_xw_ad_list"));
        initView();
        initWebView();

        //Android6.0需要动态获取权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AdListActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
        } else {
            imei = AppUtil.getDeviceId(mContext);
            openUrl();
            onCallPermission();
        }
    }

    private void initView() {
//        setContentView(R.layout.activity_xw_ad_list);
//        actionBarView = (View) findViewById(R.id.rl_action_bar);
//        backView = (ImageView) findViewById(R.id.action_bar_back);
//        titleView = (TextView) findViewById(R.id.action_bar_title);

//        webView = findViewById(R.id.webview);
//        swipeLayout = findViewById(R.id.swipe_container);
//        swipeLayout.setColorSchemeResources(R.color.colorPrimary);//R.color.colorPrimary
//        swipeLayout.setColorSchemeResources(MResource.getIdByName(getApplication(), "color", "colorPrimary"));//R.color.colorPrimary
        actionBarView = (View) findViewById(MResource.getIdByName(getApplication(), "id", "rl_action_bar"));
        backView = (ImageView) findViewById(MResource.getIdByName(getApplication(), "id", "action_bar_back"));
        titleView = (TextView) findViewById(MResource.getIdByName(getApplication(), "id", "action_bar_title"));

        webView = (WebView) findViewById(MResource.getIdByName(getApplication(), "id", "webview"));
        swipeLayout = findViewById(MResource.getIdByName(getApplication(), "id", "swipe_container"));
        swipeLayout.setColorSchemeColors(XWUtils.getInstance(mContext).getTileBGColor());//Color.parseColor("#FA6B24")   XWUtils.getInstance(mContext).getTileBGColor()

        actionBarView.setBackgroundColor(XWUtils.getInstance(mContext).getTileBGColor());
        titleView.setTextColor(XWUtils.getInstance(mContext).getTileTextColor());
        actionTitle = XWUtils.getInstance(mContext).getTitle();
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, actionBarView);
        StatusBarUtil.setPaddingSmart(this, webView);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //重新刷新页面
                webView.loadUrl(webView.getUrl());
            }
        });
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
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
        webView.addJavascriptInterface(AdListActivity.this, "android");
    }

    private void openUrl() {
        String key;
        if (mode == 0) {
            key = appid + imei + "2" + appsign + secret;
        } else {
            key = "2" + imei + appid + appsign + adId + secret;
        }
        String keycode = AppUtil.md5(key);
        String baseUrl = XWUtils.getInstance(mContext).getBaseUrl();
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        urlBuilder.append("ptype=2&deviceid=").append(imei)
                .append("&appid=").append(appid)
                .append("&appsign=").append(appsign)
                .append("&keycode=").append(keycode);
        if (mode == 1) {
            urlBuilder.append("&adid=").append(adId);
        }
        String allUrl = urlBuilder.toString();

        WebChromeClient webchromeclient = new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                Toast.makeText(AdListActivity.this, message, Toast.LENGTH_LONG).show();
                result.confirm();

                return true;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (webView.canGoBack() || mode == 1) {
                    titleView.setText(view.getTitle());
                } else {
                    titleView.setText(actionTitle);//闲玩
                }
                super.onReceivedTitle(view, title);
            }

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
        };
        webView.setWebChromeClient(webchromeclient);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (Build.VERSION.SDK_INT < 26) {
                    view.loadUrl(url);
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (webView.canGoBack() || mode == 1) {
                    titleView.setText(view.getTitle());
                } else {
                    titleView.setText(actionTitle);//闲玩
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });

        webView.loadUrl(allUrl);

    }

    // 定义JS需要调用的方法 被JS调用的方法必须加入@JavascriptInterface注解

    /**
     * 检测是否安装APP
     *
     * @param packageName 包名
     */
    @JavascriptInterface
    public void CheckInstall(String packageName) {
        boolean isInstalled = AppUtil.isApkInstalled(AdListActivity.this, packageName);
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
                        ActivityCompat.requestPermissions(AdListActivity.this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, REQUEST_INSTALL_STATE);
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
        AppUtil.NetState netState = AppUtil.getNetWorkType(mContext.getApplicationContext());
        if (netState == AppUtil.NetState.NET_NO) {
            Toast.makeText(mContext, "现在还没有联网哦，请链接网络后重新尝试！", Toast.LENGTH_SHORT).show();
        } else if (netState == AppUtil.NetState.NET_MOBILE) {
            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(mContext);
            normalDialog.setTitle("温馨提醒");
            normalDialog.setMessage("您现在使用的是非WiFi流量,是否继续?");
            normalDialog.setPositiveButton("继续下载",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();
                            DownLoadService.startActionFoo(AdListActivity.this, url);
                        }
                    });
            normalDialog.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            normalDialog.show();
        } else {
            Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();
            DownLoadService.startActionFoo(AdListActivity.this, url);
        }
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
                ActivityCompat.requestPermissions(AdListActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
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
                    Toast.makeText(this, "你不给权限我就不好干事了啦", Toast.LENGTH_SHORT).show();
                }
                openUrl();
                onCallPermission();
                break;
            }
            case REQUEST_INSTALL_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AppUtil.installAPK(mContext, new File(downloadPath));
                } else {
                    if (Build.VERSION.SDK_INT >= 26) {
                        AppUtil.jumpInstallPermissionSetting(AdListActivity.this, REQUEST_INSTALL_UNKNOW_SOURCE);
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


    /**
     * 检查 下载状态
     *
     * @param url
     * @param apkName
     */
    private void checkDownloadStatus(final String url, String apkName) {
        //        apkName = AppUtil.getUrlName(url);
//        if (TextUtils.isEmpty(apkName) || !canInstall) {
//            return;
//        }
//        canInstall = false;
//        apkName = apkName + ".apk";
//
//        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//            checkDownloadStatus(url, apkName);
//        }else {
//            openAppDetails();
//        }
//        canInstall = true;
        boolean isLoading = false;
        DownloadManager.Query query = new DownloadManager.Query();
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Cursor c = downloadManager.query(query);
        if (c.getCount() == 0) {

            c.moveToFirst();
            while (c.isLast()) {
                String LoadingUrl = c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI));
                if (url.equals(LoadingUrl)) {
                    isLoading = true;
                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (status) {
                        case DownloadManager.STATUS_PAUSED:
                            Log.i("DownLoadService", ">>>下载暂停");
                        case DownloadManager.STATUS_PENDING:
                            Log.i("DownLoadService", ">>>下载延迟");
                        case DownloadManager.STATUS_RUNNING:
                            long bytes_downloaded = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            long bytes_total = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            int progress = (int) (bytes_downloaded * 100 / bytes_total);
                            Toast.makeText(AdListActivity.this, "正在下载，已完成" + progress + "%", Toast.LENGTH_SHORT).show();
                            Log.i("DownLoadService", ">>>正在下载");
                            break;
                        case DownloadManager.STATUS_SUCCESSFUL:
                            Log.i("DownLoadService", ">>>下载完成");
                            //下载完成安装APK
                            downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "51xianwan" + File.separator + apkName;
                            File file = new File(downloadPath);
                            if (file.exists()) {
                                int sdkVersion = this.getApplicationInfo().targetSdkVersion;
                                if (sdkVersion >= 26 && Build.VERSION.SDK_INT >= 26) {
                                    boolean b = false;
                                    b = getPackageManager().canRequestPackageInstalls();
                                    if (b) {
                                        AppUtil.installAPK(mContext, new File(downloadPath));
                                    } else {
                                        ActivityCompat.requestPermissions(AdListActivity.this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, REQUEST_INSTALL_STATE);
                                    }
                                } else {
                                    AppUtil.installAPK(mContext, new File(downloadPath));
                                }
                            } else {
                                isLoading = false;
                            }
                            break;
                        case DownloadManager.STATUS_FAILED:
                            Log.i("DownLoadService", ">>>下载失败");
                            break;
                    }
                    break;
                }
                c.moveToNext();
            }
            c.close();
        }

        if (!isLoading) {
            if (!AppUtil.canDownloadState(mContext)) {
                AppUtil.jumpSetting(mContext);
                return;
            }

            AppUtil.NetState netState = AppUtil.getNetWorkType(mContext.getApplicationContext());
            if (netState == AppUtil.NetState.NET_NO) {
                Toast.makeText(mContext, "现在还没有网哦！", Toast.LENGTH_SHORT).show();
            } else if (netState == AppUtil.NetState.NET_MOBILE) {
                final AlertDialog.Builder normalDialog = new AlertDialog.Builder(mContext);
                normalDialog.setTitle("温馨提醒");
                normalDialog.setMessage("您现在使用的是非WiFi流量,是否继续?");
                normalDialog.setPositiveButton("继续下载",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();
                                DownLoadService.startActionFoo(AdListActivity.this, url);
                            }
                        });
                normalDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                normalDialog.show();
            } else {
                downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "51xianwan" + File.separator + apkName;
                File file = new File(downloadPath);
                Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();
                DownLoadService.startActionFoo(AdListActivity.this, url);
            }
        }

    }


}
