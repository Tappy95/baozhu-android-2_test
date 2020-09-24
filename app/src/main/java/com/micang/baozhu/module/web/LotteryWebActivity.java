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
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.module.home.GameListActivity;
import com.micang.baozhu.module.user.PayPswActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@BindEventBus
public class LotteryWebActivity extends BaseActivity {


    private LinearLayout llBack;
    private TextView tvTitle;
    private LinearLayout layout;
    private AgentWeb agentWeb;
    private UserBean data;
    private String mobile;
    private String payPassword;

    @Override
    public int layoutId() {
        return R.layout.activity_lottery_web;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("福利荟");
        layout = findViewById(R.id.layout);
        llBack.setOnClickListener(listener);
        initAgentWeb(url);
    }

    @Override
    protected void onResume() {
        agentWeb.getWebLifeCycle().onResume();

        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventUserInfo<UserBean> event) {
        if (event.code == EventCode.USERINFO) {
            data = event.data;
        }
        if (EmptyUtils.isNotEmpty(data)) {
            mobile = data.mobile;
            payPassword = data.payPassword;
        }
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
            finish();
        }
    };

    private class AndroidInterface {
        public AndroidInterface(AgentWeb agentWeb, LotteryWebActivity activity) {

        }

        @JavascriptInterface
        public void over() {
            LotteryWebActivity.this.finish();
        }

        @JavascriptInterface
        public void toForgetPayPsw() {
            Intent intentP = new Intent(LotteryWebActivity.this, PayPswActivity.class);
            intentP.putExtra("moblie", mobile);
            String key;
            if (EmptyUtils.isEmpty(payPassword)) {
                key = "0";
            } else {
                key = "1";
            }
            intentP.putExtra("release.keystore", key);
            startActivity(intentP);
        }


        @JavascriptInterface
        public void togame() {
            startActivity(new Intent(LotteryWebActivity.this, GameListActivity.class));
        }
    }
}
