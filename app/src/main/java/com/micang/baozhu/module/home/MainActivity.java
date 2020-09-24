package com.micang.baozhu.module.home;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.config.TTAdManagerHolder;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.DisposeBean;
import com.micang.baozhu.http.bean.MainPositionBean;
import com.micang.baozhu.http.bean.NoviceButBean;
import com.micang.baozhu.http.bean.home.FirstVideoBean;
import com.micang.baozhu.http.bean.home.NoticeBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.http.net.RetrofitUtils;
import com.micang.baozhu.module.login.NewLoginActivity;
import com.micang.baozhu.module.task.NewTaskActivity;
import com.micang.baozhu.module.user.NewUserFragment;
import com.micang.baozhu.module.view.BottomDialog;
import com.micang.baozhu.module.view.CirclePgBar;
import com.micang.baozhu.module.view.LoadingDialog;
import com.micang.baozhu.module.view.NewCommonDialog;
import com.micang.baozhu.module.web.AdvWebActivity;
import com.micang.baozhu.module.web.GeneralizeActivity;
import com.micang.baozhu.module.web.LotteryWebActivity;
import com.micang.baozhu.module.web.New_welfareActivity;
import com.micang.baozhu.util.AppUtils;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventToHome;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.util.TLog;
import com.micang.baselibrary.util.WindowUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.functions.Consumer;
import zhy.com.highlight.HighLight;

import static android.provider.Settings.EXTRA_APP_PACKAGE;
import static android.provider.Settings.EXTRA_CHANNEL_ID;


@BindEventBus
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private RelativeLayout rlHome;
    private ImageView ivHome;
    private TextView tvHome;
    private RelativeLayout rlPig;
    private ImageView ivPig;
    private TextView tvPig;
    private RelativeLayout rlTask;
    private ImageView ivTask;
    private TextView tvTask;
    private RelativeLayout rlUser;
    private RelativeLayout rlo;
    private ImageView ivUser;
    private TextView tvUser;

    private static boolean isExit = false;
    private NewHomeFragment homeFragment;
    private SiginMakeFragment treasurePigFragment;
    private NewUserFragment userFragment;
    private static final int STATE_HOME = 0;
    private static final int STATE_TREASUREPIG = 1;
    private static final int STATE_USER = 3;
    private FragmentManager mFragmentManager;
    private int selectTextColor = 0xffFF2751;
    private int unSelectTextColor = 0xff9EA9BC;
    private int selectPosition = -1;
    private BottomDialog mBottomDialog;
    private ImageView imgVip;
    private HighLight mHighLight;
    private ImageView imgNewWelfare;
    private NewCommonDialog commonDialog;
    private boolean isFirst = true;
    private NewCommonDialog downloadDialog;
    private LoadingDialog dialog;
    private boolean uerActiveDd = true;
    private NewCommonDialog noticeDialog;
    private NewCommonDialog firstVideoDialog;
    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;
    private boolean mHasShowDownloadActive;
    private UserBean data;
    private String userId;

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        WindowUtils.setPicTranslucentToStatus(this);
        initView();
        mFragmentManager = getSupportFragmentManager();
        homeFragment = (NewHomeFragment) mFragmentManager.findFragmentByTag("home_item_1_bg");
        treasurePigFragment = (SiginMakeFragment) mFragmentManager.findFragmentByTag("treasurepig");
        userFragment = (NewUserFragment) mFragmentManager.findFragmentByTag("user");
        initFragment();
        initViewClick();
        checkNotifySetting();
        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(this);
    }


    /**
     * 响应所有R.id.iv_known的控件的点击事件
     * <p>
     * 移除高亮布局
     * </p>
     *
     * @param view
     */
    public void clickKnown(View view) {
        if (mHighLight.isShowing() && mHighLight.isNext())//如果开启next模式
        {
            mHighLight.next();
        } else {
            remove(null);
        }
    }

    public void remove(View view) {
        mHighLight.remove();
    }

    private void initViewClick() {
        rlHome.setOnClickListener(new TabClick());
        rlPig.setOnClickListener(new TabClick());
        rlTask.setOnClickListener(new TabClick());
        rlUser.setOnClickListener(new TabClick());
        rlo.setOnClickListener(new TabClick());
        imgVip.setOnClickListener(new TabClick());
        imgNewWelfare.setOnClickListener(new TabClick());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (selectPosition != -1) {
            isToLogin(selectPosition);
            selectPosition = -1;
        }
        getHomePageDispose();
        if (uerActiveDd) {
            uerActiveDd();
        }
        DisposeBean.PVersionBean bean = AppContext.bean;
        if (EmptyUtils.isNotEmpty(bean)) {
            int needUpdate = bean.needUpdate;//1更新,2不用
            String updateUrl = bean.updateUrl;
            String updateRemark = bean.updateRemark;
            //            cuijianqiang.2020/8/23
            if (needUpdate == 1) {
//                showUpdata(updateUrl, updateRemark);
//                newUserVideo();
            } else {
                newUserVideo();
            }
        }
    }

    private void newUserVideo() {
        if (EmptyUtils.isTokenEmpty(this)) {
            return;
        }
        HttpUtils.newUserVideo().enqueue(new Observer<BaseResult<FirstVideoBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                FirstVideoBean bean = (FirstVideoBean) response.data;
                if (bean.cou == -1) {
                    noviceBut();
                    return;
                }
                if (bean.cou >= 1) {
//                    noviceBut();
                    startActivity(new Intent(MainActivity.this, New_welfareActivity.class));
                } else {
                    if (firstVideoDialog != null && firstVideoDialog.isShowing()) {
                    } else {
                        showVideoReward();
                    }
                }

            }
        });
    }

    private void showVideoReward() {
        firstVideoDialog = new NewCommonDialog(this, false, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_first_video_reward, null);
        contentView.findViewById(R.id.bt_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd();
                firstVideoDialog.dismiss();
            }
        });
        firstVideoDialog.setContentView(contentView);
        firstVideoDialog.show();

    }

    private void loadAd() {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("930729897")
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
                mttRewardVideoAd.showRewardVideoAd(MainActivity.this, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");
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
                        if (rewardVerify) {
                            newUserVideoCoin();
                        }
                        TLog.d(TAG, "verify:" + rewardVerify + " amount:" + rewardAmount +
                                " name:" + rewardName);

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

    private void newUserVideoCoin() {
        HttpUtils.newUserVideoCoin().enqueue(new Observer<BaseResult<FirstVideoBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                FirstVideoBean bean = (FirstVideoBean) response.data;
                if (EmptyUtils.isNotEmpty(bean.rewardAmount) && 0 != bean.rewardAmount) {
                    ToastUtils.show("恭喜获得" + bean.rewardAmount + "金币");
                }
            }
        });
    }

    private void showPicnoticeFrame(final NoticeBean.AppNewsNoticeBean appNewsNotice) {
        noticeDialog = new NewCommonDialog(this, true, true, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_show_main_pic_notice, null);
        contentView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭
//                if (!EmptyUtils.isTokenEmpty(MainActivity.this)) {
//                    setShow(appNewsNotice.id);
//                }
                noticeDialog.dismiss();
            }
        });
        final ImageView imageView = contentView.findViewById(R.id.iv_pic);
        Glide.with(MainActivity.this).load(appNewsNotice.imgUrl).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String linkAddress = appNewsNotice.linkAddress;
                if (EmptyUtils.isNotEmpty(linkAddress)) {
                    if (linkAddress.startsWith("http")) {
                        startActivity(new Intent(MainActivity.this, LotteryWebActivity.class).putExtra("url", linkAddress));
                    }

                    if (linkAddress.equals("快速赚")) {
                        startActivity(new Intent(MainActivity.this, NewTaskActivity.class));
                    }
                    if (linkAddress.equals("邀请赚")) {
                        startActivity(new Intent(MainActivity.this, GeneralizeActivity.class));
                    }

                    if (linkAddress.equals("签到赚")) {
                        isToLogin(STATE_TREASUREPIG);
                    }
                    if (linkAddress.equals("会员")) {
                        startActivity(new Intent(MainActivity.this, NewVipActivity.class));
                    }
                }
                noticeDialog.dismiss();
            }
        });
        noticeDialog.setContentView(contentView);
        noticeDialog.show();
        TLog.d("shownoticeFrame()", "shownoticeFrame()");

    }

    private void shownoticeFrame(final NoticeBean.AppNewsNoticeBean appNewsNotice) {
        noticeDialog = new NewCommonDialog(this, true, true, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_show_main_notice, null);
        final TextView tvTitle = contentView.findViewById(R.id.tvTitle);
        final TextView tvDetails = contentView.findViewById(R.id.tv_details);
        tvTitle.setText(appNewsNotice.noticeTitle);
        tvDetails.setText(appNewsNotice.noticeContent);

        contentView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                if (!EmptyUtils.isTokenEmpty(MainActivity.this)) {
                    setShow(appNewsNotice.id);
                }
                noticeDialog.dismiss();
            }
        });

        contentView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noticeDialog.dismiss();
            }
        });
        noticeDialog.setContentView(contentView);
        noticeDialog.show();

        TLog.d("shownoticeFrame()", "shownoticeFrame()");
    }


    private void uerActiveDd() {
        if (EmptyUtils.isTokenEmpty(this)) {
            return;
        }
        HttpUtils.uerActiveDd().enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                uerActiveDd = false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    //未登陆状态,只显示一次公告弹窗
    int num = 1;

    private void noviceBut() {
        if (EmptyUtils.isTokenEmpty(this)) {
            return;
        }
        if (!AppContext.showNovice) {
            getNotice();
            return;
        }
        AppContext.showNovice = false;
        dialog = new LoadingDialog(this);
        dialog.getWindow().setDimAmount(0f);
        dialog.show();
        String appVersionName = AppUtils.getAppVersionName("com.micang.baozhu");
        String umeng_channel = AppUtils.getAppMetaData(this, "UMENG_CHANNEL");
        HttpUtils.noviceBut(appVersionName, umeng_channel).enqueue(new Observer<BaseResult<NoviceButBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                NoviceButBean data = (NoviceButBean) response.data;
                int noviceGuide = data.noviceGuide;//新手引导开关1开2关
                int noviceTask = data.noviceTask;//新手任务开关1开2关
                if (noviceGuide == 1) {
                    //initGuideView();
                    dialog.dismiss();
                    selecteTab(0);
                } else {
                    getNotice();
                    dialog.dismiss();
                }
                if (noviceTask == 1) {
                    imgNewWelfare.setVisibility(View.VISIBLE);
                } else {
                    imgNewWelfare.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed(String code, String msg) {
                super.onFailed(code, msg);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                dialog.dismiss();
            }
        });
    }

    private void getNotice() {
        if (EmptyUtils.isTokenEmpty(this)) {
            return;
        }
        if (!AppContext.notice) {
            return;
        }
        HttpUtils.noticeFrame().enqueue(new Observer<BaseResult<NoticeBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    if (!EmptyUtils.isTokenEmpty(MainActivity.this)) {
                        AppContext.notice = false;
                    }
                    NoticeBean data = (NoticeBean) response.data;
                    if (data.isNotice == 2) {
                        NoticeBean.AppNewsNoticeBean appNewsNotice = data.appNewsNotice;
                        if (appNewsNotice.noticeType == 1) {
                            shownoticeFrame(appNewsNotice);
                        }
                        if (appNewsNotice.noticeType == 2) {
                            showPicnoticeFrame(appNewsNotice);
                        }
                    }
                }
            }
        });
    }

//    private void initGuideView() {
//        if (homeFragment != null) {
//            mHighLight = new HighLight(MainActivity.this)
//                    .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
//                    .intercept(true)//拦截属性默认为true 使下方ClickCallback生效
//                    .enableNext()//开启next模式并通过show方法显示 然后通过调用next()方法切换到下一个提示布局，直到移除自身
//                    .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
//                        @Override
//                        public void onLayouted() {
//                            mHighLight.addHighLight(homeFragment.getView().findViewById(R.id.con), R.layout.highlight_1, new HighLight.OnPosCallback() {
//                                @Override
//                                public void getPos(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
//                                    marginInfo.leftMargin = rectF.right - rectF.width() + DensityUtil.dip2px(MainActivity.this, 34);
//                                    marginInfo.bottomMargin = bottomMargin + rectF.height() + DensityUtil.dip2px(MainActivity.this, 34);
//                                }
//                            }, new RectLightShape())//矩形去除圆角)
//                                    .addHighLight(homeFragment.getView().findViewById(R.id.ll_function), R.layout.highlight_2, new HighLight.OnPosCallback() {
//                                        @Override
//                                        public void getPos(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
//                                            marginInfo.rightMargin = rightMargin - 45;
//                                            marginInfo.topMargin = rectF.top + rectF.height() + 45;
////
//                                        }
//                                    }, new RectLightShape())
//                                    .addHighLight(homeFragment.getView().findViewById(R.id.rl_28), R.layout.highlight_3, new HighLight.OnPosCallback() {
//                                        @Override
//                                        public void getPos(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
//                                            marginInfo.leftMargin = rectF.left + DensityUtil.dip2px(MainActivity.this, 5);
//                                            marginInfo.topMargin = rectF.top + rectF.height() + 20;
//                                        }
//                                    }, new CircleLightShape())//矩形去除圆角)
//                                    .addHighLight(homeFragment.getView().findViewById(R.id.rl_28), R.layout.highlight_4, new HighLight.OnPosCallback() {
//                                        @Override
//                                        public void getPos(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
//                                            marginInfo.leftMargin = rectF.left + DensityUtil.dip2px(MainActivity.this, 50);
//                                            marginInfo.topMargin = rectF.top + rectF.height() + 20;
//                                        }
//                                    }, new RectLightShape())
//                                    .addHighLight(findViewById(R.id.img_new_welfare), R.layout.highlight_5, new HighLight.OnPosCallback() {
//                                        @Override
//                                        public void getPos(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
//                                            marginInfo.rightMargin = rightMargin + rectF.width();
//                                            marginInfo.topMargin = rectF.top + rectF.height();
//                                            marginInfo.bottomMargin = bottomMargin - 30;
//                                        }
//                                    }, new CircleLightShape())
//                                    .setOnRemoveCallback(new HighLightInterface.OnRemoveCallback() {
//                                        @Override
//                                        public void onRemove() {
//                                            startActivity(new Intent(MainActivity.this, New_welfareActivity.class));
//                                            //引导完成,
//                                            userFirstLog();
//                                        }
//                                    })
//                                    .setOnShowCallback(new HighLightInterface.OnShowCallback() {//监听显示回调
//                                        @Override
//                                        public void onShow(HightLightView hightLightView) {
//                                        }
//                                    }).setOnNextCallback(new HighLightInterface.OnNextCallback() {
//                                @Override
//                                public void onNext(HightLightView hightLightView, View targetView, View tipView) {
//                                    // targetView 目标按钮 tipView添加的提示布局 可以直接找到'我知道了'按钮添加监听事件等处理
//                                }
//                            })
//                                    .show();
//                            dialog.dismiss();
//                        }
//                    });
//
//
//        }
//    }

    /**
     * 不再显示
     *
     * @param id
     */
    private void setShow(int id) {
        HttpUtils.readnoticeFrame(id).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {

            }
        });
    }

    /**
     * 新手引导完成
     */
    private void userFirstLog() {
        HttpUtils.userFirstLog().enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {

            }
        });
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventToHome<MainPositionBean> event) {
        if (event.code == 10099) {
            MainPositionBean positionBean = event.data;
            selectPosition = positionBean.getPosition();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventUserInfo<UserBean> event) {
        if (event.code == EventCode.USERINFO) {
            data = event.data;
        }
        if (EmptyUtils.isNotEmpty(data)) {
            userId = data.userId;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        final android.support.v4.app.FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void bottomState(int state) {
        ivHome.setImageResource(STATE_HOME == state ? R.drawable.icon_home_press : R.drawable.icon_home);
        tvHome.setTextColor(STATE_HOME == state ? selectTextColor : unSelectTextColor);

        ivPig.setImageResource(STATE_TREASUREPIG == state ? R.drawable.icon_sign_make_press : R.drawable.icon_sign_make);
        tvPig.setTextColor(STATE_TREASUREPIG == state ? selectTextColor : unSelectTextColor);

        ivUser.setImageResource(STATE_USER == state ? R.drawable.icon_my_press : R.drawable.icon_my);
        tvUser.setTextColor(STATE_USER == state ? selectTextColor : unSelectTextColor);
    }

    private void initFragment() {
        if (homeFragment == null) {
            homeFragment = new NewHomeFragment();
            addFragment(R.id.container, homeFragment, "home_item_1_bg");
        }
        if (treasurePigFragment == null) {
            treasurePigFragment = new SiginMakeFragment();
            addFragment(R.id.container, treasurePigFragment, "treasurepig");
        }


        if (userFragment == null) {
            userFragment = new NewUserFragment();
            addFragment(R.id.container, userFragment, "user");
        }

        mFragmentManager.beginTransaction().show(homeFragment).hide(treasurePigFragment)

                .hide(userFragment)
                .commitAllowingStateLoss();
    }

    public void selecteTab(int position) {
        android.support.v4.app.FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (position == STATE_HOME) {
            transaction
                    .hide(treasurePigFragment)

                    .hide(userFragment)
                    .show(homeFragment)
                    .commitAllowingStateLoss();
            transaction.addToBackStack(null);
        } else if (position == STATE_TREASUREPIG) {
            mFragmentManager.beginTransaction()
                    .hide(homeFragment)

                    .hide(userFragment)
                    .show(treasurePigFragment)
                    .commitAllowingStateLoss();
            transaction.addToBackStack(null);
        } else if (position == STATE_USER) {
            mFragmentManager.beginTransaction()
                    .hide(homeFragment)
                    .hide(treasurePigFragment)
                    .show(userFragment)
                    .commitAllowingStateLoss();
            transaction.addToBackStack(null);
        }
        bottomState(position);
    }

    private void initView() {
        rlHome = findViewById(R.id.rl_home);
        ivHome = findViewById(R.id.iv_home);
        tvHome = findViewById(R.id.tv_home);
        rlPig = findViewById(R.id.rl_pig);
        ivPig = findViewById(R.id.iv_pig);
        tvPig = findViewById(R.id.tv_pig);
        rlTask = findViewById(R.id.rl_task);
        ivTask = findViewById(R.id.iv_task);
        tvTask = findViewById(R.id.tv_task);
        rlUser = findViewById(R.id.rl_user);
        ivUser = findViewById(R.id.iv_user);
        tvUser = findViewById(R.id.tv_user);
        imgVip = findViewById(R.id.img_vip);
        rlo = findViewById(R.id.rl_o);
        imgNewWelfare = findViewById(R.id.img_new_welfare);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            ToastUtils.show(this, "再按一次退出");
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    private void isToLogin(int position) {
        if (position == 0 || position == 2) {
            selecteTab(position);
        } else {
            if (EmptyUtils.isTokenEmpty(this)) {
                startActivity(new Intent(MainActivity.this, NewLoginActivity.class));
            } else {
                selecteTab(position);
            }
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    private class TabClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_home:
                    isToLogin(STATE_HOME);
                    break;
                case R.id.rl_pig:
                    //isToLogin(STATE_TREASUREPIG);
                    if (EmptyUtils.isTokenEmpty(MainActivity.this)) {
                        startActivity(new Intent(MainActivity.this, NewLoginActivity.class));
                    } else {
                        AdvWebActivity.start(MainActivity.this,"商城","http://qilinphone.shouzhuan518.com");
                    }
                    break;
                case R.id.rl_task:
                    if (EmptyUtils.isTokenEmpty(MainActivity.this)) {
                        startActivity(new Intent(MainActivity.this, NewLoginActivity.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, GeneralizeActivity.class));
                    }
                    break;
                case R.id.rl_user:
                    isToLogin(STATE_USER);
                    break;
                case R.id.img_new_welfare:
                    if (EmptyUtils.isTokenEmpty(MainActivity.this)) {
                        startActivity(new Intent(MainActivity.this, NewLoginActivity.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, New_welfareActivity.class));
                    }
                    break;
                case R.id.img_vip:
                case R.id.rl_o:
                    if (EmptyUtils.isTokenEmpty(MainActivity.this) || EmptyUtils.isImeiEmpty(MainActivity.this)) {
                        startActivity(new Intent(MainActivity.this, NewLoginActivity.class));
                    } else {

                        startActivity(new Intent(MainActivity.this, NewVipActivity.class));
                    }
                    break;
                default:
                    isToLogin(STATE_HOME);
                    break;
            }
        }
    }


    private void checkNotifySetting() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        boolean isOpened = manager.areNotificationsEnabled();
        if (!isOpened) {
            showOpenNotify();

        }
    }

    private void showOpenNotify() {
        mBottomDialog = new BottomDialog(this, true, true);
        View contentView = LayoutInflater.from(this).inflate(R.layout.bottom_dialog_open_notify, null);
        mBottomDialog.setContentView(contentView);

        mBottomDialog.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toOpen();
                mBottomDialog.dismiss();
            }
        });

        mBottomDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomDialog.dismiss();
            }
        });

        mBottomDialog.show();
    }

    private void toOpen() {
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(EXTRA_APP_PACKAGE, getPackageName());
            intent.putExtra(EXTRA_CHANNEL_ID, getApplicationInfo().uid);
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private void showUpdata(final String updateUrl, String updateRemark) {
        if (!isFirst) {
            return;
        }
        isFirst = false;
        commonDialog = new NewCommonDialog(this, false, false, Gravity.CENTER);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_update, null);
        commonDialog.setContentView(contentView);
        TextView textView = commonDialog.findViewById(R.id.tv_details);
        textView.setText(updateRemark);
        commonDialog.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                commonDialog.dismiss();
                checkPermission(updateUrl);
            }
        });
        commonDialog.show();
    }

    private void checkPermission(final String updateUrl) {

        new RxPermissions(MainActivity.this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //下载
                    downloadApk(updateUrl);
                } else {
                    Toast.makeText(MainActivity.this, "未授权权限，部分功能不能使用", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void downloadApk(String updateUrl) {
        downloadDialog = new NewCommonDialog(this, false, false, Gravity.CENTER);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_download, null);
        downloadDialog.setContentView(contentView);
        TextView download = downloadDialog.findViewById(R.id.tv_download);
        CirclePgBar progress = downloadDialog.findViewById(R.id.progress_circular);
        //执行下载
        int last = updateUrl.lastIndexOf("/") + 1;
        String apkName = updateUrl.substring(last);
        if (!apkName.contains(".apk")) {
            if (apkName.length() > 10) {
                apkName = apkName.substring(apkName.length() - 10);
            }
            apkName += ".apk";
        }
        String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "pceggs" + File.separator + apkName;

        downLoadApp(apkName, downloadPath, updateUrl, download, progress);
        downloadDialog.show();
    }

    /**
     * @param apkName
     * @param path
     * @param url
     * @param tv
     * @param
     */
    public void downLoadApp(final String apkName, final String path, final String url, final TextView tv, final CirclePgBar progressBar) {
        BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(url)
                .setPath(path)
                .setCallbackProgressTimes(100)
                .setMinIntervalUpdateSpeed(100)
                .setListener(new FileDownloadSampleListener() {

                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.pending(task, soFarBytes, totalBytes);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);

                        if (totalBytes == -1) {
                            tv.setText("正在下载");
                        } else {
                            int progress = (int) ((Long.valueOf(soFarBytes) * 100) / Long.valueOf(totalBytes));
                            progressBar.setmProgress(progress);
                            tv.setText("正在下载" + "(" + progress + "%)");
                        }

                        tv.setEnabled(false);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        downloadDialog.dismiss();
                        installAPK(new File(path), apkName);
                        tv.setEnabled(true);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.paused(task, soFarBytes, totalBytes);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                    }
                });

        baseDownloadTask.start();
    }

    protected void installAPK(File file, String apkName) {
        if (!file.exists())
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            new RxPermissions(MainActivity.this).request(Manifest.permission.REQUEST_INSTALL_PACKAGES).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if (aBoolean) {


                    } else {

                        Toast.makeText(MainActivity.this, "未授权权限，部分功能不能使用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //坑 http://www.jianshu.com/p/c58d17073e65
            File newPath = new File(MainActivity.this.getFilesDir().getPath() + "/downloads");
            if (!newPath.exists()) {
                //通过file的mkdirs()方法创建 目录中包含却不存在的文件夹
                newPath.mkdirs();
            }
            String path = newPath.getPath() + "/" + apkName;
            String oldPath = file.getPath();
            copyFile(oldPath, path);
            File newfile = new File(path);
            // 即是在清单文件中配置的authorities
            uri = FileProvider.getUriForFile(AppContext.getInstance(), "com.micang.baozhu.fileProvider", newfile);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);

        } else {
            uri = Uri.parse("file://" + file.toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //在服务中开启activity必须设置flag,后面解释
        startActivity(intent);
    }

    /**
     * copyfile
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            TLog.d("copy", "error");
            e.printStackTrace();
        }
    }
}

