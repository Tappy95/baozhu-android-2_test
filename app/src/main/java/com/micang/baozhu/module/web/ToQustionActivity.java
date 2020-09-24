package com.micang.baozhu.module.web;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baozhu.R;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.RetrofitUtils;
import com.micang.baozhu.util.EmptyUtils;
import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


@BindEventBus
public class ToQustionActivity extends BaseActivity {
    private TextView toobarTitle;
    private ImageButton toobarBack;
    private LinearLayout layout;
    private AgentWeb agentWeb;
    private String title;
    private String link;

    private String coin = "";
    private String fightingType = "2";
    private UserBean data;
    private WebView webView;

    @Override
    public int layoutId() {
        return R.layout.activity_to_question;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setTransparent(this);
        toobarTitle = findViewById(R.id.toobar_title);
        toobarBack = findViewById(R.id.toobar_back);
        layout = findViewById(R.id.layout);
        toobarTitle.setText("对战答题");
        toobarBack.setOnClickListener(listener);

        initAgentWeb();

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventUserInfo<UserBean> event) {
        if (event.code == EventCode.USERINFO) {
            data = event.data;
        }
        if (EmptyUtils.isNotEmpty(data)) {
            coin = data.coin;
        }
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
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            agentWeb.getJsAccessEntrace().quickCallJs("back");
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
/*    @Override
    public void onBackPressed() {
        if (agentWeb.back()) {
            agentWeb.getJsAccessEntrace().quickCallJs("back");
            return;
        } else {
            super.onBackPressed();
        }
    }*/

    private void initAgentWeb() {
        String token = SPUtils.getString(this, CommonConstant.USER_TOKEN, "");
        String imei = SPUtils.getString(this, CommonConstant.MOBIL_IMEI, "");
        int progressbarColor = ContextCompat.getColor(this, R.color.colorAccent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(layout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(progressbarColor, 2)
                .createAgentWeb()
                .ready()
                .go(RetrofitUtils.H5url + "/againstTheProblemSolving.html" + "?token=" + token + "&imei=" + imei + "&coin=" + coin + "&fightingType=" + fightingType);
        agentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(agentWeb, this));
        IAgentWebSettings agentWebSettings = agentWeb.getAgentWebSettings();
        agentWebSettings.getWebSettings().setDomStorageEnabled(true);
        webView = agentWeb.getWebCreator().getWebView();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!agentWeb.back()) {
                finish();
            }
        }
    };

    public class AndroidInterface {
        public AndroidInterface(AgentWeb agentWeb, ToQustionActivity inviteApprenticeActivity) {
        }

        @JavascriptInterface
        public void over() {
            ToQustionActivity.this.finish();
        }
    }

}
