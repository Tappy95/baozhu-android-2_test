package com.micang.baozhu.module.web;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baselibrary.util.TLog;
import com.micang.baozhu.R;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.net.RetrofitUtils;
import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;


public class PigRuleActivity extends BaseActivity {

    private LinearLayout llBack;
    private TextView tvTitle;

    private LinearLayout layout;
    private AgentWeb agentWeb;


    @Override
    public int layoutId() {
        return R.layout.activity_pig_rule;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("宝猪协议");
        layout = findViewById(R.id.layout);
        llBack.setOnClickListener(listener);
        initAgentWeb();
    }

    @Override
    protected void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        agentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();

    }


    private void initAgentWeb() {
        String token = SPUtils.getString(this, CommonConstant.USER_TOKEN, "");
        String imei = SPUtils.getString(this, CommonConstant.MOBIL_IMEI, "");
        TLog.d("log", token + imei);
        int progressbarColor = ContextCompat.getColor(this, R.color.colorAccent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(layout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(progressbarColor, 2)
                .createAgentWeb()
                .ready()
                .go(RetrofitUtils.H5url + "/userAgreement.html");


    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
