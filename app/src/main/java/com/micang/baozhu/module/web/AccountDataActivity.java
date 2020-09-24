package com.micang.baozhu.module.web;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baozhu.R;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.net.RetrofitUtils;
import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;

public class AccountDataActivity extends BaseActivity {
    private LinearLayout webView;
    private LinearLayout llBack;
    private TextView tvTitle;
    private AgentWeb agentWeb;
    private String title;
    private String link;

    @Override
    public int layoutId() {
        return R.layout.activity_accountdata;
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
        tvTitle.setText("账户明细");
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

    private void initAgentWeb() {
        String token = SPUtils.getString(this, CommonConstant.USER_TOKEN, "");
        String imei = SPUtils.getString(this, CommonConstant.MOBIL_IMEI, "");
        int progressbarColor = ContextCompat.getColor(this, R.color.colorAccent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(webView, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(progressbarColor, 2)
                .createAgentWeb()
                .ready()
                .go(RetrofitUtils.H5url + "/accountDetails.html?token=" + token + "&imei=" + imei);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            finish();

        }
    };
}
