package com.micang.baozhu.module.web;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.ChangeBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.CoordinatesBean;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.GyrosensorUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.util.TLog;
import com.micang.baselibrary.view.MyCommonPopupWindow;
import com.micang.baselibrary.view.NewPopWindow;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xianwan.sdklibrary.util.AppUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.functions.Consumer;

@BindEventBus
public class NextXWGameDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_PHONE_STATE = 100;
    private static final int REQUEST_INSTALL_STATE = 101;
    private static final int REQUEST_WRITE_STORAGE = 102;
    private static final int REQUEST_INSTALL_UNKNOW_SOURCE = 1001;
    private LinearLayout llBack;
    private TextView tvTitle;
    private WebView webView;
    private TextView tvStartDownload;
    private String urls;
    private ChangeBean bean;
    private Context mContext;
    private String apkName;
    private boolean isFirst = true;
    private boolean canInstall = true;
    private String downloadPath;
    private String imei;
    private UserBean data;
    private String moblie;
    private int count = 0;
    private int go = 0;
    private String gameId;
    private String interfaceName;
    private ChangeBean gameBean;
    private MyCommonPopupWindow backPop;
    private ImageView ivGamePic;
    TextView tvDetails;
    TextView tvMoney;
    Button btBegin;
    private GyrosensorUtils gyrosensor;
    @Override
    public int layoutId() {
        return R.layout.activity_next_xwgame_detail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mContext = this;
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("游戏详情");
        webView = findViewById(R.id.webview);
        tvStartDownload = findViewById(R.id.tv_start_download);
        Intent intent = getIntent();
        urls = intent.getStringExtra("URLS");
        bean = (ChangeBean) intent.getSerializableExtra("bean");
        llBack.setOnClickListener(this);
        initWebView();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NextXWGameDetailActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
        } else {
            openUrl();
            onCallPermission();
        }
        gyrosensor = GyrosensorUtils.getInstance(this);
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
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventUserInfo<UserBean> event) {
        if (event.code == EventCode.USERINFO) {
            data = event.data;
        }
        if (EmptyUtils.isNotEmpty(data)) {
            moblie = data.mobile;
        }
    }

    @Override
    public void onClick(View v) {
        if (count == 0) {
            initbackPopWindow();
        } else {
            finish();
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
                ActivityCompat.requestPermissions(NextXWGameDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            } else {//已经授权了就走这条分支
                Log.i("wei", "onClick granted");
            }
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
                    Toast.makeText(this, "没有权限不可以试玩游戏", Toast.LENGTH_SHORT).show();
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
                        AppUtil.jumpInstallPermissionSetting(NextXWGameDetailActivity.this, REQUEST_INSTALL_UNKNOW_SOURCE);
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

    private void openUrl() {
        WebChromeClient webchromeclient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
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
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.startsWith("http") && !url.startsWith("https") && !url.startsWith("ftp")) {
                    try {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        if (mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            return true;
                        }
                    } catch (Exception e) {
                        Log.d("setWebViewClient", "shouldOverrideUrlLoading: " + e.getMessage());
                    }
                }
                if (Build.VERSION.SDK_INT < 26) {
                    view.loadUrl(url);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        //加载链接
        webView.loadUrl(urls);
        Log.i("url:", "");
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
        webView.addJavascriptInterface(NextXWGameDetailActivity.this, "android");
    }
    // 定义JS需要调用的方法 被JS调用的方法必须加入@JavascriptInterface注解

    /**
     * 检测是否安装APP
     *
     * @param packageName 包名
     */
    @JavascriptInterface
    public void CheckInstall(String packageName) {
        boolean isInstalled = AppUtil.isApkInstalled(NextXWGameDetailActivity.this, packageName);
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
                        ActivityCompat.requestPermissions(NextXWGameDetailActivity.this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, REQUEST_INSTALL_STATE);
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
        tvStartDownload.setVisibility(View.VISIBLE);
        tvStartDownload.setText("开始下载");
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
                            tvStartDownload.setText("正在下载");
                        } else {
                            int progress = (int) ((Long.valueOf(soFarBytes) * 100) / Long.valueOf(totalBytes));
                            tvStartDownload.setText("正在下载" + "(" + progress + "%)");
                        }

                        tvStartDownload.setEnabled(false);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        tvStartDownload.setVisibility(View.GONE);
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
        if (NextXWGameDetailActivity.this == null || NextXWGameDetailActivity.this.isFinishing()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            new RxPermissions(NextXWGameDetailActivity.this).request(Manifest.permission.REQUEST_INSTALL_PACKAGES).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if (aBoolean) {


                    } else {

                        Toast.makeText(NextXWGameDetailActivity.this, "未授权权限，部分功能不能使用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //坑 http://www.jianshu.com/p/c58d17073e65
            File newPath = new File(NextXWGameDetailActivity.this.getFilesDir().getPath() + "/downloads");
            if (!newPath.exists()) {
                //通过file的mkdirs()方法创建 目录中包含却不存在的文件夹
                newPath.mkdirs();
            }
            tvStartDownload.setText("正在下载");
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
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        webView = null;
        mContext = null;
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (count == 0) {
                initbackPopWindow();
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initbackPopWindow() {
        if (this.isFinishing()) {
            return;
        }
        count++;
        backPop = new MyCommonPopupWindow(this, R.layout.game_back_pop_new, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();

                LinearLayout llChangeGame;
                LinearLayout llbacklist;
                ImageView flClose;
                ImageView ivwelfare;

                ivwelfare = view.findViewById(R.id.iv_welfare);
                llbacklist = view.findViewById(R.id.ll_back_list);
                ivGamePic = view.findViewById(R.id.tv_1);
                tvDetails = view.findViewById(R.id.tv_details);
                tvMoney = view.findViewById(R.id.tv_money);
                llChangeGame = view.findViewById(R.id.ll_change_game);
                btBegin = view.findViewById(R.id.bt_begin);
                flClose = view.findViewById(R.id.fl_close);
                Glide.with(NextXWGameDetailActivity.this).load(R.drawable.icon_bg_changegame).into(ivwelfare);
                tvDetails.setText(bean.gameTitle);
                String str = "当前任务最高可奖励 <font color='#FF3C2E'>" + bean.gameGold + "</font>" + " 元";
                tvMoney.setText(Html.fromHtml(str));
                Glide.with(NextXWGameDetailActivity.this).load(bean.icon).into(ivGamePic);
                llChangeGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeGame();

                    }
                });
                flClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getPopupWindow().dismiss2();

                    }
                });
                ivwelfare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPopupWindow().dismiss2();
                        String fuLiHui = AppContext.fuLiHui;
                        if (EmptyUtils.isEmpty(fuLiHui)) {
                            ToastUtils.show("功能待开发");
                        } else {
                            Intent intent = new Intent(NextXWGameDetailActivity.this, WelfareActivity.class);
                            intent.putExtra("url", fuLiHui);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                llbacklist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getPopupWindow().dismiss2();
                        finish();
                    }
                });
                btBegin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断赚钱还是跳别的界面
                        if (go == 0) {
                            getPopupWindow().dismiss2();
                        } else {
                            getUrl();
                        }
                    }
                });
            }

            @Override
            protected void initWindow() {
                super.initWindow();
                NewPopWindow instance = getPopupWindow();
                instance.canDismiss = false;
                instance.setOnDismissListener(new NewPopWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }
                });
            }
        };
        NewPopWindow popupWindow = backPop.getPopupWindow();
        popupWindow.setAnimationStyle(R.style.animScale);
        backPop.getPopupWindow().showAtLocation(llBack, Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    private void getUrl() {
        if ("PCDD".equals(interfaceName)) {
            HttpUtils.toPlay(moblie, gameId).enqueue(new Observer<BaseResult>() {
                @Override
                public void onSuccess(BaseResult response) {
                    String url = (String) response.data;
                    Intent intent = new Intent(NextXWGameDetailActivity.this, NextPCddGameDetailActivity.class);
                    intent.putExtra("URLS", url);
                    intent.putExtra("bean", gameBean);
                    backPop.getPopupWindow().dismiss2();
                    startActivity(intent);
                    finish();
                }
            });
        }
        if ("MY".equals(interfaceName) || "bz-Android".equals(interfaceName)) {
            HttpUtils.toPlay(moblie, gameId).enqueue(new Observer<BaseResult>() {
                @Override
                public void onSuccess(BaseResult response) {
                    String url = (String) response.data;
                    Intent intent = new Intent(NextXWGameDetailActivity.this, NextMYGameDetailsActivity.class);
                    intent.putExtra("URLS", url);
                    intent.putExtra("bean", gameBean);
                    backPop.getPopupWindow().dismiss2();
                    startActivity(intent);
                    finish();
                }
            });
        }
        if ("xw-Android".equals(interfaceName)) {
            HttpUtils.toPlay(moblie, gameId).enqueue(new Observer<BaseResult>() {
                @Override
                public void onSuccess(BaseResult response) {
                    String url = (String) response.data;
                    Intent intent = new Intent(NextXWGameDetailActivity.this, NextXWGameDetailActivity.class);
                    intent.putExtra("URLS", url);
                    intent.putExtra("bean", gameBean);
                    backPop.getPopupWindow().dismiss2();
                    startActivity(intent);
                    finish();
                }
            });
        }

    }

    private void changeGame() {
        HttpUtils.recommendGame().enqueue(new Observer<BaseResult<ChangeBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                gameBean = (ChangeBean) response.data;
                if (EmptyUtils.isNotEmpty(gameBean)) {
                    if (NextXWGameDetailActivity.this == null || NextXWGameDetailActivity.this.isFinishing()) {
                        return;
                    }
                    Glide.with(NextXWGameDetailActivity.this).load(gameBean.icon).into(ivGamePic);
                    tvDetails.setText(gameBean.gameTitle);
                    String str = "当前任务最高可奖励 <font color='#FF3C2E'>" + gameBean.gameGold + "</font>" + " 元";
                    tvMoney.setText(Html.fromHtml(str));
                    btBegin.setText("开始赚钱");
                    interfaceName = gameBean.interfaceName;
                    gameId = gameBean.id;
                    go++;
                }
            }
        });
    }
}