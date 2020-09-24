package com.micang.baozhu;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.MainThread;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.just.agentweb.AgentWeb;
import com.micang.baozhu.config.TTAdManagerHolder;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.DisposeBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.http.net.RetrofitUtils;
import com.micang.baozhu.module.home.MainActivity;
import com.micang.baozhu.module.web.WebActivity;
import com.micang.baozhu.util.AppUtils;
import com.micang.baozhu.util.WeakHandler;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baselibrary.util.TLog;
import com.micang.baselibrary.util.WindowUtils;

public class StartActivity extends BaseActivity implements WeakHandler.IHandler {
    private static final String TAG = "SplashActivity";
    private TTAdNative mTTAdNative;
    private FrameLayout mSplashContainer;
    //是否强制跳转到主页面
    private boolean mForceGoMain;

    //开屏广告加载发生超时但是SDK没有及时回调结果的时候，做的一层保护。
    private final WeakHandler mHandler = new WeakHandler(this);
    //开屏广告加载超时时间,建议大于3000,这里为了冷启动第一次加载到广告并且展示,示例设置了3000ms
    private static final int AD_TIME_OUT = 3000;
    private static final int MSG_GO_MAIN = 1;
    //开屏广告是否已经加载
    private boolean mHasLoaded;

    // 是否第一次进入app，记录key值，用来判断是否显示用户协议弹窗
    private static final String SP_IS_FIRST_ENTER_APP = "SP_IS_FIRST_ENTER_APP";
    private AlertDialog dialog;
    private AgentWeb agentWeb;

    @Override
    public int layoutId() {
        return R.layout.activity_start;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        if (isFirstEnterApp()) {
            mForceGoMain = false;
            startDialog();
        } else
            enterApp();
    }

    @Override
    protected void onResume() {
        if (isFirstEnterApp()) {
            mForceGoMain = false;
            startDialog();
        } else
            enterApp();
        //判断是否该跳转到主页面
        if (mForceGoMain) {
            mHandler.removeCallbacksAndMessages(null);
            enterHome();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mForceGoMain = true;
    }


    private void getHomePageDispose() {
        String appVersionName = AppUtils.getAppVersionName("com.micang.baozhu");
        String umeng_channel = AppUtils.getAppMetaData(this, "UMENG_CHANNEL");
        HttpUtils.homePageDispose(umeng_channel, appVersionName).enqueue(new Observer<BaseResult<DisposeBean>>() {
            @Override
            public void onSuccess(BaseResult response) {

                DisposeBean data = (DisposeBean) response.data;
                AppContext.fuLiHui = data.fuLiHui;      //福利荟
                AppContext.recommendGame = data.recommendGame;//推荐游戏id
                String h5_location_new = data.h5_location_new;
                RetrofitUtils.setBaseH5Url(h5_location_new);
                DisposeBean.PVersionBean pVersion = data.pVersion;
                AppContext.bean = pVersion;
            }
        });
    }


    private void enterHome() {
        startActivity(new Intent(StartActivity.this, MainActivity.class));
//        mSplashContainer.removeAllViews();
        finish();

    }

    @Override
    public void handleMsg(Message msg) {
        if (msg.what == MSG_GO_MAIN) {
            if (!mHasLoaded) {
                enterHome();
            }
        }
    }

    /**
     * 加载开屏广告
     */
    private void loadSplashAd() {
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("830729248")
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .build();
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                TLog.d(TAG, message);
                mHasLoaded = true;
                enterHome();
            }

            @Override
            @MainThread
            public void onTimeout() {
                mHasLoaded = true;
                enterHome();
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                TLog.d(TAG, "开屏广告请求成功");
                mHasLoaded = true;
                mHandler.removeCallbacksAndMessages(null);
                if (ad == null) {
                    return;
                }
                //获取SplashView
                View view = ad.getSplashView();
                if (view != null) {
                    mSplashContainer.removeAllViews();
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                    mSplashContainer.addView(view);
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                    //ad.setNotAllowSdkCountdown();
                } else {
                    enterHome();
                }

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        TLog.d(TAG, "onAdClicked");
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        TLog.d(TAG, "onAdShow");
                    }

                    @Override
                    public void onAdSkip() {
                        TLog.d(TAG, "onAdSkip");
                        enterHome();

                    }

                    @Override
                    public void onAdTimeOver() {
                        TLog.d(TAG, "onAdTimeOver");
                        enterHome();
                    }
                });
                if (ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ad.setDownloadListener(new TTAppDownloadListener() {
                        boolean hasShow = false;

                        @Override
                        public void onIdle() {

                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            if (!hasShow) {
                                hasShow = true;
                            }
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                            TLog.d("下载暂停...");

                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                            TLog.d("下载失败...");

                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {

                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {

                        }
                    });
                }
            }
        }, AD_TIME_OUT);

    }


    /**
     * 是否是首次进入APP
     */
    public boolean isFirstEnterApp() {
        return SPUtils.getBoolean(this, SP_IS_FIRST_ENTER_APP, true);
    }

    /**
     * 保存首次进入APP状态
     */
    public void saveFirstEnterApp() {
        SPUtils.saveBoolean(this, SP_IS_FIRST_ENTER_APP, false);
    }


    private void startDialog() {

        dialog = new AlertDialog.Builder(this).create();
        dialog.show();

        //设置点击对话框外部不可取消
        dialog.setCanceledOnTouchOutside(false);
        //对话框弹出后点击或按返回键不消失;
        dialog.setCancelable(false);

        final Window window = dialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.widget_user_dialog);
            window.setGravity(Gravity.CENTER);
//            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);
            TextView textView = window.findViewById(R.id.tv_content);
            TextView tvCancel = window.findViewById(R.id.tv_cancel);
            TextView tvAgree = window.findViewById(R.id.tv_agree);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quitApp();
                }
            });
            tvAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SPUtils.saveBoolean(v.getContext(), SP_IS_FIRST_ENTER_APP, false);
                    dialog.cancel();
                    enterApp();
                }
            });
            String str = "请务必审慎阅读、充分理解“用户协议”和“隐私政策”各条款，" +
                    "包括但不限于：为了向您提供即时通讯、内容分享等服务，我们需要收集您的设备信息、操作日志等个人信息，您可以在\"设置\"中查看、变更、删除个人信息并管理你的授权。您可以阅读《用户协议》和《隐私政策》了解详细信息。如您同意，请点击“同意”开始使用我们的产品和服务!";
            textView.setText(str);

            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append(str);
            final int start = str.indexOf("《");//第一个出现的位置
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    String url = RetrofitUtils.H5url + "/userPrivate.html";
                    url = "file:///android_asset/userAgreement.html";
                    startActivity(new Intent(StartActivity.this, WebActivity.class).putExtra("url", url));
                }
                @Override

                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    // 去掉下划线
                    ds.setUnderlineText(false);
                }

            }, start, start + 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            final int end = str.lastIndexOf("《");//最后一个出现的位置

            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    String url = RetrofitUtils.H5url + "/userPrivate.html";
                    url = "file:///android_asset/userAgreement.html";
                    startActivity(new Intent(StartActivity.this, WebActivity.class).putExtra("url", url));
                }

                @Override

                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
//                    ds.setColor(getProxyActivity().getResources().getColor(R.color.text_click_yellow));       //设置文件颜色
                    // 去掉下划线

                    ds.setUnderlineText(false);
                }

            }, end, end + 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(ssb, TextView.BufferType.SPANNABLE);
        }
    }

    private void enterApp() {//同意并继续，进入APP\
        mForceGoMain = true;
        WindowUtils.setPicTranslucentToStatus(this);
        getHomePageDispose();
        mSplashContainer = findViewById(R.id.splash_container);
        //step2:创建TTAdNative对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        //在合适的时机申请权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题
        //在开屏时候申请不太合适，因为该页面倒计时结束或者请求超时会跳转，在该页面申请权限，体验不好
        // TTAdManagerHolder.getInstance(this).requestPermissionIfNecessary(this);
        //定时，AD_TIME_OUT时间到时执行，如果开屏广告没有加载则跳转到主页面
        mHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, AD_TIME_OUT);
        //加载开屏广告
        loadSplashAd();
    }

    private void quitApp() {//更改状态，finish APP
        SPUtils.saveBoolean(this, SP_IS_FIRST_ENTER_APP, true);
        dialog.cancel();
        finish();
        System.exit(0);
    }
}
