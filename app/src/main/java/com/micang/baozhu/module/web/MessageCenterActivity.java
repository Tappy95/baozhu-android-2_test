package com.micang.baozhu.module.web;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.micang.baozhu.R;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.net.RetrofitUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.SPUtils;


public class MessageCenterActivity extends BaseActivity {
    private LinearLayout llBack;
    private TextView tvTitle;
    private LinearLayout webView;

    private AgentWeb agentWeb;
    private String title;
    private String link;


    @Override
    public int layoutId() {
        return R.layout.activity_message_center;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        webView = findViewById(R.id.layout);
        Intent intent = getIntent();
        title = intent.getStringExtra(CommonConstant.WEB_TITLE);
        link = intent.getStringExtra(CommonConstant.WEB_LINK);
        tvTitle.setText("消息中心");
        llBack.setOnClickListener(listener);

        initAgentWeb();
    }

    @Override
    protected void onResume() {
        agentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        agentWeb.clearWebCache();
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (agentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initAgentWeb() {
        String token = SPUtils.getString(this, CommonConstant.USER_TOKEN, "");
        String imei = SPUtils.getString(this, CommonConstant.MOBIL_IMEI, "");
        int progressbarColor = ContextCompat.getColor(this, R.color.colorAccent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(webView, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(progressbarColor, 2)// TODO: 2019/2/25 网页进度条颜色
                .createAgentWeb()
                .ready()
                .go(RetrofitUtils.H5url + "/messageCenter.html?token=" + token + "&imei=" + imei);
        agentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(agentWeb, this));
        IAgentWebSettings agentWebSettings = agentWeb.getAgentWebSettings();
        agentWebSettings.getWebSettings().setDomStorageEnabled(true);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    class AndroidInterface {
        public AndroidInterface(AgentWeb agentWeb, MessageCenterActivity messageCenterActivity) {

        }

        @JavascriptInterface
        public void over() {
            MessageCenterActivity.this.finish();
        }

    }

}
