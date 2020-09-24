package com.micang.baozhu.module.web;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baozhu.module.view.NewCommonDialog;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.module.view.CommonDialog;
import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;


public class WelfareActivity extends BaseActivity {


    private LinearLayout llBack;
    private TextView tvTitle;
    private LinearLayout layout;
    private AgentWeb agentWeb;
    private int code;
    private CommonDialog commonDialog;
    private NewCommonDialog noticeDialog;

    @Override
    public int layoutId() {
        return R.layout.activity_welfare;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        String url = AppContext.fuLiHui;
        Intent intent = getIntent();
        code = intent.getIntExtra("CODE", 0);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("福利荟");
        layout = findViewById(R.id.layout);
        llBack.setOnClickListener(listener);
        initAgentWeb(url);
        if (AppContext.fuLiHuiisfirst) {
            showNotice();
        }
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
            if (code == 1) {
                showDialog();
            } else {
                finish();
            }
            return false;
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
            if (code == 1) {
                showDialog();
            } else {
                finish();
            }
        }
    };

    private void showDialog() {
        commonDialog = new CommonDialog(this, true, true, Gravity.CENTER);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_new_welfare_1, null);
        commonDialog.setContentView(contentView);
        //首先是拼接字符串
        String content = "是否确定离开？如果没有抽取转盘，" + "<font color=\"#FF2B4B\">" + "将不能获取奖励哦～" + "</font>";
        //然后直接setText()
        TextView tvContent = (TextView) commonDialog.findViewById(R.id.content);
        tvContent.setText(Html.fromHtml(content));
        commonDialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                commonDialog.dismiss();
            }
        });

        commonDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commonDialog.dismiss();
                finish();
            }
        });
        commonDialog.show();
    }

    private void showNotice() {
        if (this.isFinishing()) {
            return;
        }
        noticeDialog = new NewCommonDialog(this, false, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_show_welfare_notice, null);
        final TextView tvTime = contentView.findViewById(R.id.tv_time);
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noticeDialog != null && noticeDialog.isShowing()) {
                    noticeDialog.dismiss();
                }
            }
        });
        noticeDialog.setContentView(contentView);
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText("知道了(" + millisUntilFinished / 1000 + "S)");
            }

            @Override
            public void onFinish() {
                if (noticeDialog != null && noticeDialog.isShowing()) {
                    tvTime.setText("知道了");
                    noticeDialog.setCancelable(true);
                    noticeDialog.setCanceledOnTouchOutside(true);
                }
            }
        }.start();
        AppContext.fuLiHuiisfirst = false;
        noticeDialog.show();
    }

    private class AndroidInterface {
        public AndroidInterface(AgentWeb agentWeb, WelfareActivity activity) {

        }

        @JavascriptInterface
        public void over() {
            WelfareActivity.this.finish();
        }
    }

}
