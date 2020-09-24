package com.micang.baozhu.module.web;

import android.content.Intent;
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
import com.micang.baselibrary.util.TLog;
import com.micang.baozhu.R;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.RetrofitUtils;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ShareUtils;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@BindEventBus
public class CreatedRoomActivity extends BaseActivity {

    private TextView toobarTitle;
    private ImageButton toobarBack;
    private LinearLayout layout;
    private AgentWeb agentWeb;
    private String title;
    private String link;
    private String fightingType = "1";
    private UserBean data;
    private String coin;
    private WebView webView;

    @Override
    public int layoutId() {
        return R.layout.activity_created_room;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setTransparent(this);
        toobarTitle = findViewById(R.id.toobar_title);
        toobarBack = findViewById(R.id.toobar_back);
        layout = findViewById(R.id.layout);
        Intent intent = getIntent();
        title = intent.getStringExtra(CommonConstant.WEB_TITLE);
        link = intent.getStringExtra(CommonConstant.WEB_LINK);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            agentWeb.getJsAccessEntrace().quickCallJs("back");
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    @Override
    protected void onDestroy() {
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();

    }

    private void initAgentWeb() {
        String token = SPUtils.getString(this, CommonConstant.USER_TOKEN, "");
        String imei = SPUtils.getString(this, CommonConstant.MOBIL_IMEI, "");
        int progressbarColor = ContextCompat.getColor(this, R.color.colorAccent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(layout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(progressbarColor, 2)// TODO: 2019/2/25 网页进度条颜色
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
        public AndroidInterface(AgentWeb agentWeb, CreatedRoomActivity createdRoomActivity) {
        }

        @JavascriptInterface
        public void over() {
            CreatedRoomActivity.this.finish();
        }

        @JavascriptInterface
        public String inviteFriend(String s) {

            ShareUtils.shareUrl(CreatedRoomActivity.this, R.drawable.icon_share_ic, s, "一起来对战", "答题赢金币", new UMShareListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {
                    TLog.d("SHARE", "开始了");
                }

                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    ToastUtils.show(CreatedRoomActivity.this, "分享成功");
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    TLog.d("SHARE", throwable.getMessage());

//                        String[] split = throwable.getMessage().split(" 错误信息：");
//                        TLog.d("SHARE", split.length+"");
                    ToastUtils.show(CreatedRoomActivity.this, throwable.getMessage());
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {
//                    ToastUtils.show(CreatedRoomActivity.this, "取消了分享");
                }
            });

            return s;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
