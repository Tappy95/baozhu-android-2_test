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
import com.micang.baselibrary.base.BaseActivity;

public class ADWebActivity extends BaseActivity {

    private LinearLayout llBack;
    private TextView tvTitle;
    private LinearLayout layout;
    private AgentWeb agentWeb;

    @Override
    public int layoutId() {
        return R.layout.activity_adweb;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(title);
        layout = findViewById(R.id.layout);
        llBack.setOnClickListener(listener);
        initAgentWeb(url);
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
            Intent intent = getIntent();
            setResult(45, intent);
            finish();
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
        agentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(agentWeb, this));
        IAgentWebSettings agentWebSettings = agentWeb.getAgentWebSettings();
        agentWebSettings.getWebSettings().setDomStorageEnabled(true);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = getIntent();
            setResult(45, intent);
            finish();
        }
    };

    private class AndroidInterface {
        public AndroidInterface(AgentWeb agentWeb, ADWebActivity activity) {

        }

        @JavascriptInterface
        public void over() {
            finish();
        }
    }
}
