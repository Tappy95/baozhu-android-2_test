package com.micang.baozhu.module.web;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.CoordinatesBean;
import com.micang.baozhu.util.GyrosensorUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baozhu.R;
import com.micang.baozhu.util.AppUtils;
import com.micang.baozhu.util.EmptyUtils;
import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;


public class GameDetailsActivity extends BaseActivity {

    private LinearLayout llBack;
    private TextView tvTitle;
    private LinearLayout webView;
    private AgentWeb agentWeb;
    private String urls;
    private GyrosensorUtils gyrosensor;


    @Override
    public int layoutId() {
        return R.layout.activity_game_details;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        Intent intent = getIntent();
        urls = intent.getStringExtra("URLS");
        webView = findViewById(R.id.layout);
        tvTitle.setText("游戏详情");
        llBack.setOnClickListener(listener);
        initAgentWeb();
        gyrosensor = GyrosensorUtils.getInstance(this);
    }

    @Override
    protected void onResume() {
        agentWeb.getWebLifeCycle().onResume();
        super.onResume();
        addGyro();
    }

    @Override
    protected void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();

    }

    private void addGyro() {
        if (EmptyUtils.isTokenEmpty(this)) {
            return;
        }
        CoordinatesBean coordinates = gyrosensor.getCoordinates();
        String anglex = String.format("%.2f", coordinates.anglex);
        String angley = String.format("%.2f", coordinates.angley);
        String anglez = String.format("%.2f", coordinates.anglez);
        HttpUtils.addGyro(anglex, angley, anglez, 3).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {

            }
        });
    }
    private void initAgentWeb() {
        int progressbarColor = ContextCompat.getColor(this, R.color.colorAccent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(webView, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(progressbarColor, 2)
                .createAgentWeb()
                .ready()
                .go(urls);
        agentWeb.getJsInterfaceHolder().addJavaObject("android", new GameDetailsActivity.AndroidInterface(agentWeb, this));
//        agentWeb.getJsAccessEntrace().quickCallJs("OpenApp_Return", "0");
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
        public AndroidInterface(AgentWeb agentWeb, GameDetailsActivity gameDetailsActivity) {
        }

        @JavascriptInterface
        public String CheckInstall(String s) {
            if (EmptyUtils.isNotEmpty(s)) {
                boolean appInstalled = AppUtils.isAppInstalled(s);
                if (appInstalled) {
                    s="1";
                    agentWeb.getJsAccessEntrace().quickCallJs("CheckInstall_Return", "1");
                } else {
                    s="0";
                    agentWeb.getJsAccessEntrace().quickCallJs("CheckInstall_Return", "0");
                }
            }
            return s;
        }

        @JavascriptInterface
        public String OpenApp(String s) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(s);
            if (intent != null) {
                intent.putExtra("type", "110");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            return s;
        }

        @JavascriptInterface
        public String InstallAPP(String urls) {
            openBrowser(GameDetailsActivity.this, urls);
            return urls;
        }
    }


    public static void openBrowser(Context context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url)); // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名 // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            // 打印Log   ComponentName到底是什么 L.d("componentName = " + componentName.getClassName());
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            Toast.makeText(context.getApplicationContext(), "请下载浏览器", Toast.LENGTH_SHORT).show();
        }
    }
}
