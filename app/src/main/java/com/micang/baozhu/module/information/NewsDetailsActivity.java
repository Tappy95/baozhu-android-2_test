package com.micang.baozhu.module.information;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.util.TLog;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.InformationRewardCoinBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.view.BottomDialog;
import com.micang.baozhu.module.view.CountDownProgressBar;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.OnMultiClickListener;
import com.micang.baozhu.util.ShareUtils;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class NewsDetailsActivity extends BaseActivity {

    private LinearLayout llBack;
    private TextView tvTitle;
    private LinearLayout layout;
    private AgentWeb agentWeb;
    private String urls;
    private ImageView ivShare;
    private CountDownProgressBar cpbCountdown;
    private int isAdd;
    private BottomDialog mBottomDialog;
    private String title;

    private String coin_reward;
    private boolean isFirst = true;
    private String fs_coin_reward;
    private String gg_coin_reward;
    private String zx_coin_reward;
    private String content;

    @Override
    public int layoutId() {
        return R.layout.activity_news_details;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        layout = findViewById(R.id.layout);
        layout = findViewById(R.id.layout);
        llBack.setOnClickListener(listener);
        cpbCountdown = findViewById(R.id.cpb_countdown);
        ivShare = findViewById(R.id.iv_share);
        Intent intent = getIntent();
        urls = intent.getStringExtra("URLS");
        isAdd = intent.getIntExtra("isAdd", 0);
        content = intent.getStringExtra("content");
        title = intent.getStringExtra("title");
        tvTitle.setText("资讯");
        initAgentWeb();

        ivShare.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                showShare();
            }
        });
    }


    private void initData() {
        if (isAdd == 0) {
            getReward("19");
        } else {
            getReward("13");
        }
    }

    private void getReward(final String type) {
        HttpUtils.getRewardConifg(type, content).enqueue(new Observer<BaseResult<InformationRewardCoinBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                InformationRewardCoinBean data = (InformationRewardCoinBean) response.data;
                isFirst = false;
                int get_limit = data.get_limit;
                int can_send = data.can_send;
                int zx_stop_second = data.stop_second;
                fs_coin_reward = data.fs_coin_reward;
                gg_coin_reward = data.gg_coin_reward;
                zx_coin_reward = data.zx_coin_reward;
                if (can_send == 2) {
                    if (get_limit == 1) {
                        cpbCountdown.setVisibility(View.GONE);
                        return;
                    } else {
                        cpbCountdown.setVisibility(View.VISIBLE);
                        coin_reward = data.coin_reward;
                        cpbCountdown.setDuration(zx_stop_second * 1000, new CountDownProgressBar.OnFinishListener() {
                            @Override
                            public void onFinish() {
                                cpbCountdown.setVisibility(View.GONE);
                                getCoin(type);
                            }
                        });
                    }
                }
            }
        });
    }

    private void getCoin(final String type) {
        HttpUtils.readNewZXReward(type, content).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                if ("20".equals(type)) {
                    ToastUtils.show(NewsDetailsActivity.this, "金币 + " + fs_coin_reward);
                }
                if ("19".equals(type)) {
                    ToastUtils.show(NewsDetailsActivity.this, "金币 + " + gg_coin_reward);
                }
                if ("13".equals(type)) {
                    ToastUtils.show(NewsDetailsActivity.this, "金币 + " + zx_coin_reward);
                }
            }
        });
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
        cpbCountdown.setOnFinishListener(null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (agentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initAgentWeb() {
        int progressbarColor = ContextCompat.getColor(this, R.color.colorAccent);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(layout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(progressbarColor, 2)
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()
                .ready()
                .go(urls);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!agentWeb.back()) {
                finish();
            }
        }
    };

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (isFirst) {
                ivShare.setVisibility(View.VISIBLE);
                initData();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void showShare() {
        mBottomDialog = new BottomDialog(this, true, true);
        View contentView = LayoutInflater.from(this).inflate(R.layout.share_pop, null);
        mBottomDialog.setContentView(contentView);

        mBottomDialog.findViewById(R.id.rl_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.share(SHARE_MEDIA.QQ, NewsDetailsActivity.this, R.drawable.icon_share_ic, urls + "%26state%3dshare", title, "邀请你来宝猪乐园看资讯,红包领不停", shareListener);
                mBottomDialog.dismiss();
            }
        });


        mBottomDialog.findViewById(R.id.rl_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.share(SHARE_MEDIA.WEIXIN, NewsDetailsActivity.this, R.drawable.icon_share_ic, urls + "%26state%3dshare", title, "邀请你来宝猪乐园看资讯,红包领不停", shareListener);

                mBottomDialog.dismiss();
            }
        });
        mBottomDialog.findViewById(R.id.friends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.share(SHARE_MEDIA.WEIXIN_CIRCLE, NewsDetailsActivity.this, R.drawable.icon_share_ic, urls + "%26state%3dshare", title, "邀请你来宝猪乐园看资讯,红包领不停", shareListener);

                mBottomDialog.dismiss();
            }
        });

        mBottomDialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomDialog.dismiss();
            }
        });

        mBottomDialog.show();
    }

    public UMShareListener shareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA platform) {
            TLog.d("SHARE", "开始了");
        }


        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtils.show("分享成功");
            getCoin("20");
        }


        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            TLog.d("SHARE", t.getMessage());
            ToastUtils.show(t.getMessage());
        }


        @Override
        public void onCancel(SHARE_MEDIA platform) {
//                    ToastUtils.show(InviteApprenticeActivity.this, "取消了分享");
        }
    };
}
