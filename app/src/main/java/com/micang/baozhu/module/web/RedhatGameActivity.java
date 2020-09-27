package com.micang.baozhu.module.web;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.webkit.WebView;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;

import com.micang.baozhu.module.view.CommonDialog;
import com.micang.baozhu.module.view.NewCommonDialog;
import com.micang.baozhu.util.AppUtils;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.SystemUtil;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.TLog;

import java.util.ArrayList;
import java.util.List;


public class RedhatGameActivity extends BaseActivity {

    private String packagenameLocal = "";
    private WebView webView;

    private LinearLayout llBack;
    private TextView tvTitle;
    private LinearLayout layout;
    private AgentWeb agentWeb;
    private int code;
    private List<String> mPermissionList = new ArrayList<>();
    //配置需要动态申请的权限
    private static final int REQUEST_CODE = 0;//请求码
    //配置需要动态申请的权限
    private static final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };
    private AlertDialog mPermissionDialog;
    private String imei;
    private CommonDialog commonDialog;
    private NewCommonDialog noticeDialog;

    @Override
    public int layoutId() {
        return R.layout.activity_redhat_game;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initPermission();
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        String url = "https://h5.gamedachen.com/?channelId=9f5940b8704eadf0af60f5421e6c45354ba699eb&deviceId=" + getIMEI();

        Intent intent = getIntent();
        code = intent.getIntExtra("CODE", 0);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("小红帽游戏");
        layout = findViewById(R.id.layout);
        llBack.setOnClickListener(listener);
        initAgentWeb(url);
        if (AppContext.fuLiHuiisfirst) {
            // showNotice();
        }
    }


    @Override
    protected void onResume() {
        agentWeb.getWebLifeCycle().onResume();

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        agentWeb.clearWebCache();
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (code == 1) {
                showDialog();
            } else {
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initAgentWeb(String url) {
        int progressbarColor = ContextCompat.getColor(this, R.color.colorAccent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(layout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(progressbarColor, 2)
                .createAgentWeb()
                .ready()
                .go(url);
        agentWeb.getJsInterfaceHolder().addJavaObject("roid", new AndroidInterface(agentWeb, this));
//        agentWeb.getJsInterfaceHolder().addJavaObject("android",new AndroidInterface( agentWeb,this));
        //    mAgentWeb.getJsInterfaceHolder().addJavaObject("android",new AndroidInterface(mAgentWeb,getActivity()));
        IAgentWebSettings agentWebSettings = agentWeb.getAgentWebSettings();
        agentWebSettings.getWebSettings().setDomStorageEnabled(true);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (code == 1) {
                showDialog();
            } else {
                finish();
            }
        }
    };

    private void showDialog() {
        commonDialog = new CommonDialog(this, true, true, Gravity.CENTER);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_new_welfare_1, null);
        commonDialog.setContentView(contentView);
        //首先是拼接字符串
        String content = "是否确定离开？如果没有抽取转盘，" + "<font color=\"#FF2B4B\">" + "将不能获取奖励哦～" + "</font>";
        //然后直接setText()
        TextView tvContent = (TextView) commonDialog.findViewById(R.id.content);
        tvContent.setText(Html.fromHtml(content));
        commonDialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                commonDialog.dismiss();
            }
        });

        commonDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commonDialog.dismiss();
                finish();
            }
        });
        commonDialog.show();
    }

    private void showNotice() {
        if (this.isFinishing()) {
            return;
        }
        noticeDialog = new NewCommonDialog(this, false, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_show_welfare_notice, null);
        final TextView tvTime = contentView.findViewById(R.id.tv_time);
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noticeDialog != null && noticeDialog.isShowing()) {
                    noticeDialog.dismiss();
                }
            }
        });
        noticeDialog.setContentView(contentView);
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText("知道了(" + millisUntilFinished / 1000 + "S)");
            }

            @Override
            public void onFinish() {
                if (noticeDialog != null && noticeDialog.isShowing()) {
                    tvTime.setText("知道了");
                    noticeDialog.setCancelable(true);
                    noticeDialog.setCanceledOnTouchOutside(true);
                }
            }
        }.start();
        AppContext.fuLiHuiisfirst = false;
        noticeDialog.show();
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
            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (TelephonyMgr.getDeviceId()==""){
                imei = TelephonyMgr.getDeviceId();
            }else{
                //   imei =  idSupplier.getOAID();
            }



        }
    }
    @SuppressLint("MissingPermission")
    public String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
        if (EmptyUtils.isEmpty(imei)) {
            //由于Android 10系统限制，无法获取IMEI等标识，如果为空再去获取Android ID作为标识进行传递
            String deviceId = Settings.System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            imei = deviceId;
        }
        return imei;
    }

    private class AndroidInterface {
        public AndroidInterface(AgentWeb agentWeb, RedhatGameActivity activity) {

        }

        @JavascriptInterface
        public void toDownLoad(String name,String packageName,String iconUrl,String downUrl,String gameSize,Integer versionCode,String versionName,String classCode,String source) {
            TLog.d("error", downUrl);
            if (AppUtils.isAppInstalled(packageName)) {//判断是否安装app
                AppUtils.launchApp(packageName);
            } else {//没有安装跳转下载
                Uri uri = Uri.parse(downUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }

        /**
         * @param packageName
         */
        @JavascriptInterface
        public void getAPP(final String packageName) {

            Log.i("CheckInstall:", packageName + "...");

            packagenameLocal = packageName;

            final boolean isInstalled = SystemUtil.isAppInstalled(RedhatGameActivity.this, packageName);
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
        public void over() {
            RedhatGameActivity.this.finish();
        }
    }


}


