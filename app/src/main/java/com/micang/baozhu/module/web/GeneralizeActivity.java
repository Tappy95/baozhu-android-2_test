package com.micang.baozhu.module.web;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.user.COOBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.http.net.RetrofitUtils;
import com.micang.baozhu.module.home.ShareQrcodeActivity;
import com.micang.baozhu.module.user.PayCOOActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ShareUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.event.EventWXPay;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baselibrary.util.TLog;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URLEncoder;


public class GeneralizeActivity extends BaseActivity {
    private LinearLayout llBack;
    private TextView tvTitle;
    private LinearLayout layout;
    private AgentWeb agentWeb;

    private String outTradeNo;

    @Override
    public int layoutId() {
        return R.layout.activity_generalize;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        layout = (LinearLayout) findViewById(R.id.layout);
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
        String token = SPUtils.token(this);
        String imei = SPUtils.imei(this);
        int progressbarColor = ContextCompat.getColor(this, R.color.colorAccent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(layout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(progressbarColor, 2)
                .createAgentWeb()
                .ready()
                .go(RetrofitUtils.H5url + "/enlighteningToMakeMoney.html?token=" + token + "&imei=" + imei);
        agentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(agentWeb, this));
        IAgentWebSettings agentWebSettings = agentWeb.getAgentWebSettings();
        agentWebSettings.getWebSettings().setDomStorageEnabled(true);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!agentWeb.back()) {
                finish();
            }
        }
    };

    private class AndroidInterface {
        public AndroidInterface(AgentWeb agentWeb, GeneralizeActivity activity) {

        }

        @JavascriptInterface
        public void over() {
            GeneralizeActivity.this.finish();
        }

        @JavascriptInterface
        public void toPay(String price, String descripte) {
            createOrder(price, descripte);
        }

        @JavascriptInterface
        public String inviteFriend(String s) {
            startActivity(new Intent(GeneralizeActivity.this, ShareQrcodeActivity.class));
            return s;
        }

    }

    private void createOrder(String price, String descripte) {
        if ("3".equals(descripte)) {
            descripte = "续费运营总监";

        } else {
            descripte = "升级为运营总监";
        }
        HttpUtils.tradeTame(descripte, price, "1").enqueue(new Observer<BaseResult<COOBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                COOBean data = (COOBean) response.data;
                if (EmptyUtils.isNotEmpty(data)) {
                    Intent intent = new Intent(GeneralizeActivity.this, PayCOOActivity.class);
                    intent.putExtra("COOBean", data);
                    outTradeNo = data.outTradeNo;
                    startActivity(intent);
                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventWXPay<Object> event) {
        if (event.code == EventCode.wxpay) {
            int data = (int) event.data;
            if (data == 0) {
                ToastUtils.show("支付成功");
                paySuccess();
            }
            EventBus.getDefault().removeStickyEvent(EventWXPay.class);
        }

    }

    private void paySuccess() {
        HttpUtils.paySuccess(outTradeNo).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                agentWeb.getWebCreator().getWebView().reload();

            }
        });
    }


}
