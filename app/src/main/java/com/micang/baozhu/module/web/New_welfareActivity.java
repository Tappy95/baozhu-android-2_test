package com.micang.baozhu.module.web;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.AdditionBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.http.net.RetrofitUtils;
import com.micang.baozhu.module.home.EveryDayTaskActivity;
import com.micang.baozhu.module.view.CommonDialog;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baselibrary.util.TLog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 新手福利界面
 */
@BindEventBus
public class New_welfareActivity extends BaseActivity {
    private LinearLayout llBack;
    private TextView tvTitle;
    private LinearLayout layout;
    private AgentWeb agentWeb;
    private CommonDialog commonDialog;
    private String mobile;


    @Override
    public int layoutId() {
        return R.layout.activity_new_welfare;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        layout = findViewById(R.id.layout);
        tvTitle.setText("新手福利");
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

        String token = SPUtils.token(this);
        String imei = SPUtils.imei(this);
        TLog.d("log", token + imei);
        int progressbarColor = ContextCompat.getColor(this, R.color.colorAccent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(layout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(progressbarColor, 2)
                .createAgentWeb()
                .ready()
                .go(RetrofitUtils.H5url + "/newWelfare.html" + "?token=" + token + "&imei=" + imei);
        agentWeb.getJsInterfaceHolder().addJavaObject("android", new New_welfareActivity.AndroidInterface(agentWeb, this));
        IAgentWebSettings agentWebSettings = agentWeb.getAgentWebSettings();
        agentWebSettings.getWebSettings().setDomStorageEnabled(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventUserInfo<UserBean> event) {
        if (event.code == EventCode.USERINFO) {
            mobile = event.data.mobile;
        }
    }

    private void showDialog(AdditionBean data) {
        if (this.isFinishing()) {
            return;
        }

        int passbookValue = data.passbookValue;
        commonDialog = new CommonDialog(this, true, true, Gravity.CENTER);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_new_welfare_2, null);
        commonDialog.setContentView(contentView);
        TextView tvvalue = (TextView) commonDialog.findViewById(R.id.tv_value);
        tvvalue.setText("+" + passbookValue + "%");
        TextView tvContent = (TextView) commonDialog.findViewById(R.id.content);
        //首先是拼接字符串
        String content = "小猪猪赠送你一张" + "<font color=\"#FF2B4B\">" + "+" + passbookValue + "%" + "</font>"
                + "速速前往可赚取" + "<font color=\"#FF2B4B\">" + "8000" + "</font>" + "左右金币";
        //然后直接setText()
        tvContent.setText(Html.fromHtml(content));
        commonDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recommendGame = AppContext.recommendGame;

                HttpUtils.toPlay(mobile, recommendGame).enqueue(new Observer<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult response) {
                        String url = (String) response.data;
                        Intent intent = new Intent(New_welfareActivity.this, GameDetailsActivity.class);
                        intent.putExtra("URLS", url);
                        startActivity(intent);
                    }
                });
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

    private class AndroidInterface {
        public AndroidInterface(AgentWeb agentWeb, New_welfareActivity activity) {

        }

        @JavascriptInterface
        public void over() {
            New_welfareActivity.this.finish();
        }

        @JavascriptInterface
        public void toWelfare() {
            Intent intent = new Intent(New_welfareActivity.this, WelfareActivity.class);
            intent.putExtra("CODE", 1);
            startActivity(intent);
        }

        @JavascriptInterface
        public void toPlay() {
            //游戏弹窗,网络请求
            getAddCoupon();
        }


        @JavascriptInterface
        public void toOtherTask() {
            startActivity(new Intent(New_welfareActivity.this, EveryDayTaskActivity.class));
        }
    }

    private void getAddCoupon() {
        HttpUtils.getAddCoupon().enqueue(new Observer<BaseResult<AdditionBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                AdditionBean data = (AdditionBean) response.data;
                if (EmptyUtils.isNotEmpty(data)) {
                    showDialog(data);
                }
            }
        });
    }
}
