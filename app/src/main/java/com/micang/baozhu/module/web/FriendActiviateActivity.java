package com.micang.baozhu.module.web;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.micang.baozhu.R;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.net.RetrofitUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.SPUtils;

public class FriendActiviateActivity extends BaseActivity {

    private RelativeLayout head;
    private LinearLayout llBack;
    private TextView tvTitle;
    private LinearLayout layout;
    private AgentWeb agentWeb;

    @Override
    public int layoutId() {
        return R.layout.activity_friend_activiate;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);

        head = findViewById(R.id.head);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        layout = findViewById(R.id.layout);
        tvTitle.setText("好友活跃度");
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
                .setAgentWebParent(layout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(progressbarColor, 2)
                .createAgentWeb()
                .ready()
                .go(RetrofitUtils.H5url + "/friendActivity.html?token=" + token + "&imei=" + imei);
        agentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(agentWeb, this));
        IAgentWebSettings agentWebSettings = agentWeb.getAgentWebSettings();
        agentWebSettings.getWebSettings().setDomStorageEnabled(true);
    }

    private class AndroidInterface {
        public AndroidInterface(AgentWeb agentWeb, FriendActiviateActivity activity) {

        }

        @JavascriptInterface
        public void over() {
            finish();
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            finish();

        }
    };
}
