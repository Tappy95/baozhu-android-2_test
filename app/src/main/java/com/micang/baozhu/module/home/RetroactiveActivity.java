package com.micang.baozhu.module.home;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.R;
import com.micang.baozhu.config.TTAdManagerHolder;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.view.NewCommonDialog;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.OnMultiClickListener;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.util.TLog;
import com.micang.baselibrary.view.MyCommonPopupWindow;
import com.micang.baselibrary.view.NewPopWindow;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@BindEventBus
public class RetroactiveActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RetroactiveActivity";
    private LinearLayout llBack;
    private TextView tvTitle;
    private ImageView ivVideo;
    private MyCommonPopupWindow showPopupWindow;
    public RetroactiveActivity instance = null;

    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;
    private boolean mHasShowDownloadActive = false;
    private String userId;
    private NewCommonDialog noticeDialog;

    @Override
    public int layoutId() {
        return R.layout.activity_retroactive;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setDarkMode(this);
        instance = this;
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("补签卡");
        ivVideo = findViewById(R.id.iv_video);
        initClick();
        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(this);
    }

    private void initClick() {

        llBack.setOnClickListener(this);
        ivVideo.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                loadAd();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventUserInfo<UserBean> event) {
        if (event.code == EventCode.USERINFO) {
            UserBean data = event.data;
            if (EmptyUtils.isNotEmpty(data)) {
                userId = data.userId;
            }
        }
    }

    private void loadAd() {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("930729775")
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
//                .setRewardName("金币") //奖励的名称
//                .setRewardAmount(3)  //奖励的数量
                .setUserID(userId)//用户id,必传参数
//                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(TTAdConstant.VERTICAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                TLog.d(TAG, message);

            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                TLog.d(TAG, "rewardVideoAd video cached");
                //展示广告，并传入广告展示的场景
                mttRewardVideoAd.showRewardVideoAd(RetroactiveActivity.this, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");
                mttRewardVideoAd = null;
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                TLog.d(TAG, "rewardVideoAd loaded");
                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        TLog.d(TAG, "rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        TLog.d(TAG, "rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        TLog.d(TAG, "rewardVideoAd close");
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        TLog.d(TAG, "rewardVideoAd complete");
                    }

                    @Override
                    public void onVideoError() {
                        TLog.d(TAG, "rewardVideoAd error");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
//                        showVideoReward();
                        TLog.d(TAG, "verify:" + rewardVerify + " amount:" + rewardAmount +
                                " name:" + rewardName);
                        getRetroactive();
                    }

                    @Override
                    public void onSkippedVideo() {
                        TLog.d(TAG, "rewardVideoAd has onSkippedVideo");
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            TLog.d(TAG, "下载中，点击下载区域暂停");
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        TLog.d(TAG, "下载暂停，点击下载区域继续");
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        TLog.d(TAG, "下载失败，点击下载区域重新下载");
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        TLog.d(TAG, "下载完成，点击下载区域重新下载");
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        TLog.d(TAG, "安装完成，点击下载区域打开");
                    }
                });
            }
        });
    }

    private void getRetroactive() {
        HttpUtils.retroactivecard().enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
//                showPop();
                showNotice();
            }
        });
    }

    private void showNotice() {
        if (this.isFinishing()) {
            return;
        }
        noticeDialog = new NewCommonDialog(this, true, true, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_retroactive_card, null);
        noticeDialog.setContentView(contentView);

        contentView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog.dismiss();
                finish();
            }
        });
        noticeDialog.show();
    }

    private void showPop() {
        showPopupWindow = new MyCommonPopupWindow(this, R.layout.pop_retroactive_card, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                TextView ok = view.findViewById(R.id.btn_ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupWindow.getPopupWindow().dismiss2();
                        finish();
                    }
                });

            }

            @Override
            protected void initWindow() {
                super.initWindow();
                NewPopWindow instance = getPopupWindow();
                instance.canDismiss = false;
                instance.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }
                });
            }
        };
        PopupWindow popupWindow = showPopupWindow.getPopupWindow();
        popupWindow.setAnimationStyle(R.style.animScale);
        if (!instance.isFinishing()) {
            popupWindow.showAtLocation(llBack, Gravity.CENTER, 0, 0);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.5f;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setAttributes(lp);
        }
    }

}
