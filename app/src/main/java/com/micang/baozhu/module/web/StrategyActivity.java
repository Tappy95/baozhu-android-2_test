package com.micang.baozhu.module.web;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.micang.baozhu.R;
import com.micang.baozhu.http.net.RetrofitUtils;
import com.micang.baozhu.module.home.FastMakeMoneyActivity;
import com.micang.baozhu.module.home.SiginMakeActivity;
import com.micang.baozhu.module.login.NewLoginActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baselibrary.base.BaseActivity;

public class StrategyActivity extends BaseActivity {

    private LinearLayout llBack;
    private TextView tvTitle;

    private LinearLayout layout;
    private AgentWeb agentWeb;

    @Override
    public int layoutId() {
        return R.layout.activity_strategy;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("新人赚钱攻略");
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
        int progressbarColor = ContextCompat.getColor(this, R.color.colorAccent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(layout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(progressbarColor, 2)
                .createAgentWeb()
                .ready()
                .go(RetrofitUtils.H5url + "/strategy.html");
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

    public class AndroidInterface {
        public AndroidInterface(AgentWeb agentWeb, StrategyActivity strategyActivity) {
        }

        @JavascriptInterface
        public void fast() {
            if (EmptyUtils.isTokenEmpty(StrategyActivity.this)) {
                startActivity(new Intent(StrategyActivity.this, NewLoginActivity.class));
            } else {
                startActivity(new Intent(StrategyActivity.this, FastMakeMoneyActivity.class));
            }
        }

        @JavascriptInterface
        public void sigin() {
            if (EmptyUtils.isTokenEmpty(StrategyActivity.this)) {
                startActivity(new Intent(StrategyActivity.this, NewLoginActivity.class));
            } else {
                startActivity(new Intent(StrategyActivity.this, SiginMakeActivity.class));
            }
        }

        @JavascriptInterface
        public void share() {
            if (EmptyUtils.isTokenEmpty(StrategyActivity.this)) {
                startActivity(new Intent(StrategyActivity.this, NewLoginActivity.class));
            } else {
                startActivity(new Intent(StrategyActivity.this, GeneralizeActivity.class));
            }
        }
    }
}
