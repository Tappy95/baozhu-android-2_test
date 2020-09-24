package com.micang.baozhu.module.web;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
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
import com.micang.baozhu.http.bean.home.GameBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.CoordinatesBean;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.GyrosensorUtils;
import com.micang.baozhu.util.SystemUtil;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.util.TLog;
import com.micang.baselibrary.view.MyCommonPopupWindow;
import com.micang.baselibrary.view.NewPopWindow;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

@BindEventBus
public class PCddGameDetailActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private TextView tvTitle;
    private WebView webview;
    private TextView tvStartDownload;

    private String urls;
    private String downUrlLocal = "";
    private String packagenameLocal = "";
    private WebView webView;
    private TextView tv_start_download;
    private List<String> mPermissionList = new ArrayList<>();
    private static final int REQUEST_CODE = 0;//请求码
    //配置需要动态申请的权限
    private static final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };
    private GameBean.ListBean bean;
    private MyCommonPopupWindow backPop;
    ImageView ivGamePic;
    TextView tvDetails;
    TextView tvMoney;
    Button btBegin;
    private int count = 0;
    private int go = 0;
    private String gameId;
    private String interfaceName;
    private UserBean data;
    private String moblie;
    private ChangeBean gameBean;
    private boolean isFirst = true;
    private AlertDialog mPermissionDialog;
    private GyrosensorUtils gyrosensor;

    @Override
    public int layoutId() {
        return R.layout.activity_pcdd_game_detail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);


        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tv_start_download = findViewById(R.id.tv_start_download);
        webView = findViewById(R.id.webview);
        Intent intent = getIntent();
        urls = intent.getStringExtra("URLS");
        bean = (GameBean.ListBean) intent.getSerializableExtra("bean");
        tvTitle.setText("游戏详情");
        initWebView();
        llBack.setOnClickListener(this);
        //Android6.0需要动态获取权限
        initPermission();
        gyrosensor = GyrosensorUtils.getInstance(this);
        //下载按钮触发事件
        tv_start_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //异常判断
                if (TextUtils.isEmpty(downUrlLocal)) {
                    ToastUtils.show("下载连接异常");
                    return;
                }

                //存在立即打开
                final boolean isInstalled = SystemUtil.isAppInstalled(PCddGameDetailActivity.this, packagenameLocal);
                if (isInstalled) {
                    doStartApplicationWithPackageName(packagenameLocal);
                    return;
                }

                //执行下载
                int last = downUrlLocal.lastIndexOf("/") + 1;
                String apkName = downUrlLocal.substring(last);
                if (!apkName.contains(".apk")) {
                    if (apkName.length() > 10) {
                        apkName = apkName.substring(apkName.length() - 10);
                    }
                    apkName += ".apk";
                }
                String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "pceggs" + File.separator + apkName;
                downLoadApp(apkName, downloadPath, downUrlLocal, tv_start_download);

                //下载通知后台，js交互
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:startDownApp()");
                    }
                });
            }
        });

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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventUserInfo<UserBean> event) {
        if (event.code == EventCode.USERINFO) {
            data = event.data;
        }
        if (EmptyUtils.isNotEmpty(data)) {
            moblie = data.mobile;
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebView() {
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.addJavascriptInterface(PCddGameDetailActivity.this, "android");
    }

    /**
     * @param packageName
     */
    @JavascriptInterface
    public void CheckInstall(final String packageName) {

        Log.i("CheckInstall:", packageName + "...");

        packagenameLocal = packageName;

        final boolean isInstalled = SystemUtil.isAppInstalled(PCddGameDetailActivity.this, packageName);
        if (isInstalled) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:CheckInstall_Return(1)");
                    Log.i("CheckInstall:", packageName + "...1");
                }
            });
        } else {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:CheckInstall_Return(0)");
                    Log.i("CheckInstall:", packageName + "...2");
                }
            });
        }
    }

    @JavascriptInterface
    public void AwallOpen(String packageName) {
        doStartApplicationWithPackageName(packageName);
    }

    @JavascriptInterface
    public void popout(String message) {
        if (!TextUtils.isEmpty(message)) {
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            ToastUtils.show(message);
        }
    }

    @JavascriptInterface
    public void refresh() {
        if (EmptyUtils.isNotEmpty(webView)) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.reload();
                }
            });
        }
    }

    @JavascriptInterface
    public void openBrowser(String url) {
        openBrowser(PCddGameDetailActivity.this, url);
    }

    public static void openBrowser(Context context, String url) {
        final Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(url)); // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名 // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            // 打印Log   ComponentName到底是什么 L.d("componentName = " + componentName.getClassName());
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            Toast.makeText(context.getApplicationContext(), "请下载浏览器", Toast.LENGTH_SHORT).show();
        }
    }

    @JavascriptInterface
    public void initPceggsData(final String showType, final String buttonType, final String buttonName, String downUlr) {

        Log.i("initPceggsData:", showType + "...." + buttonType + "..." + buttonName + "..." + downUlr);

        downUrlLocal = downUlr;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ("0".equals(showType)) {
                    tv_start_download.setVisibility(View.GONE);
                } else {
                    tv_start_download.setVisibility(View.VISIBLE);
                }

                tv_start_download.setText(buttonName);

                if ("0".equals(buttonType)) {
                    tv_start_download.setEnabled(false);
                } else {
                    tv_start_download.setEnabled(true);
                }
            }
        });
    }

    /**
     * @param url
     */
    @JavascriptInterface
    public void AwallDownLoad(String url) {
        Log.i("open:", url + "...");
        //另一种下载方式
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (count == 0) {
            initbackPopWindow();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        webView = null;
        super.onDestroy();
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
                Glide.with(PCddGameDetailActivity.this).load(R.drawable.icon_bg_changegame).into(ivwelfare);
                tvDetails.setText(bean.gameTitle);
                String str = "当前任务最高可奖励 <font color='#FF3C2E'>" + bean.gameGold + "</font>" + " 元";
                tvMoney.setText(Html.fromHtml(str));
                Glide.with(PCddGameDetailActivity.this).load(bean.icon).into(ivGamePic);
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
                            Intent intent = new Intent(PCddGameDetailActivity.this, WelfareActivity.class);
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
                    Intent intent = new Intent(PCddGameDetailActivity.this, NextPCddGameDetailActivity.class);
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
                    Intent intent = new Intent(PCddGameDetailActivity.this, NextMYGameDetailsActivity.class);
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
                    Intent intent = new Intent(PCddGameDetailActivity.this, NextXWGameDetailActivity.class);
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
                    if (PCddGameDetailActivity.this.isFinishing()) {
                        return;
                    }
                    Glide.with(PCddGameDetailActivity.this).load(gameBean.icon).into(ivGamePic);
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

    private void initPermission() {
        mPermissionList.clear();

        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) !=
                    PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }

        if (mPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
        } else {
            openUrl();
        }
    }

    private void openUrl() {
        webView.setWebChromeClient(new WebChromeClient() {
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
        });
        webView.setWebViewClient(new WebViewClient());
        //加载链接
        webView.loadUrl(urls);
        Log.i("url:", "");
    }

    /**
     * 请求权限后回调的方法
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (REQUEST_CODE == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                    break;
                }
            }
        }
        if (hasPermissionDismiss) {//如果有没有被允许的权限
            showPermissionDialog();
        } else {
            //权限已经都通过了，可以将程序继续打开了
            openUrl();
        }
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
    /**
     * 展示权限弹框
     */
    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();
                            Uri packageURI = Uri.parse("package:" + getPackageName(PCddGameDetailActivity.this));
                            Intent intent = new Intent(Settings.
                                    ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();
                            finish();
                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }

    /**
     * 取消弹框
     */
    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }

    /**
     * 获取应用程序版本名称信息
     *
     * @param context
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param packagename
     */
    private void doStartApplicationWithPackageName(String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        //  CATEGORY_LAUNCHER  Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        //
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;

            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);


            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            startActivity(intent);
        }
    }

    public void downLoadApp(final String apkName, final String path, final String url, final TextView tv) {
        BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(url)
                .setPath(path)
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
                            tv.setText("正在下载");
                        } else {
                            int progress = (int) ((Long.valueOf(soFarBytes) * 100) / Long.valueOf(totalBytes));
                            tv.setText("正在下载" + "(" + progress + "%)");
                        }

                        tv.setEnabled(false);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        tv.setText("立即试玩");
                        installAPK(new File(path), apkName);
                        tv.setEnabled(true);
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
        if (PCddGameDetailActivity.this == null || PCddGameDetailActivity.this.isFinishing()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            new RxPermissions(PCddGameDetailActivity.this).request(Manifest.permission.REQUEST_INSTALL_PACKAGES).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if (aBoolean) {


                    } else {

                        Toast.makeText(PCddGameDetailActivity.this, "未授权权限，部分功能不能使用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //坑 http://www.jianshu.com/p/c58d17073e65
            File newPath = new File(PCddGameDetailActivity.this.getFilesDir().getPath() + "/downloads");
            if (!newPath.exists()) {
                //通过file的mkdirs()方法创建 目录中包含却不存在的文件夹
                newPath.mkdirs();
            }
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
}
