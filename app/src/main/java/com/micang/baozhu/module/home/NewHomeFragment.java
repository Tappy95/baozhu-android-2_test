package com.micang.baozhu.module.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.NestedScrollView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.component.dly.xzzq_ywsdk.YwSDK;
import com.component.dly.xzzq_ywsdk.YwSDK_WebActivity;
import com.iBookStar.views.YmConfig;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.config.TTAdManagerHolder;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.BannerBean;
import com.micang.baozhu.http.bean.ChangeBean;
import com.micang.baozhu.http.bean.home.ConductTaskBean;
import com.micang.baozhu.http.bean.home.MarqueeviewMessageBean;
import com.micang.baozhu.http.bean.home.VideoCountBean;
import com.micang.baozhu.http.bean.home.VideoTimeUserBean;
import com.micang.baozhu.http.bean.home.VideoswitchBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.http.net.RetrofitUtils;
import com.micang.baozhu.module.earlyclock.EarlyClockActivity;
import com.micang.baozhu.module.login.NewLoginActivity;
import com.micang.baozhu.module.task.NewTaskActivity;
import com.micang.baozhu.module.user.NewWithdrawActivity;
import com.micang.baozhu.module.view.CountdownView1;
import com.micang.baozhu.module.view.DragFloatActionLayout;
import com.micang.baozhu.module.view.NewCommonDialog;
import com.micang.baozhu.module.web.AccountDataActivity;
import com.micang.baozhu.module.web.AdvWebActivity;
import com.micang.baozhu.module.web.GeneralizeActivity;
import com.micang.baozhu.module.web.LotteryWebActivity;
import com.micang.baozhu.module.web.RedhatGameActivity;
import com.micang.baozhu.module.web.NextMYGameDetailsActivity;
import com.micang.baozhu.module.web.NextPCddGameDetailActivity;
import com.micang.baozhu.module.web.NextXWGameDetailActivity;

import com.micang.baozhu.module.web.TaskActivity;
import com.micang.baozhu.module.web.WebActivity;
import com.micang.baozhu.module.web.WelfareActivity;
import com.micang.baozhu.util.CoordinatesBean;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.GyrosensorUtils;
import com.micang.baozhu.util.OnMultiClickListener;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseFragment;
import com.micang.baselibrary.event.EventBalance;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.util.DensityUtil;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baselibrary.util.TLog;
import com.micang.baselibrary.util.TimeUtils;
import com.micang.baselibrary.view.MyCommonPopupWindow;
import com.micang.baselibrary.view.NewPopWindow;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/7/30 10:35
 * @describe describe
 */
public class NewHomeFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "NewHomeFragment";

    private NestedScrollView srcrollview;
    private ImageView imSign;
    private TextView tvMoney;
    private TextView tvCoin;
    private TextView tvWithdraw;
    private MarqueeView marqueeview;
    private Banner banner;
    private ImageView tvGifDong;
    private ImageView ivGamePic;
    private TextView tvGameName;
    private TextView tvEndtime;
    private TextView tvGameMoney;
    private ImageView ivZqdk;
    private ImageView ivJbph;
    private ImageView ivGoing;
    private RelativeLayout rlYxz;
    private RelativeLayout rlGaoez;
    private RelativeLayout rlXsz;
    private RelativeLayout rlXyy;
    private ImageView tvGuess;

    private String gameId;
    private String interfaceName;
    private String mobile;
    private boolean hidden1;
    private List<String> msgList = new ArrayList<>();
    private MyCommonPopupWindow gamePop;
    private ChangeBean gameBean;
    private List<String> images = new ArrayList<>();
    private List<BannerBean> bannerBeans;
    private String userId = "";
    private static final int STATE_TREASUREPIG = 1;
    private NewCommonDialog continueDialog;
    private NewCommonDialog hintDialog;
    private DragFloatActionLayout floatbuttonlayout;

    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;
    private boolean mHasShowDownloadActive = false;
    private NewCommonDialog noticeVideoDialog;
    private GyrosensorUtils gyrosensor;
    private int pic[] = new int[]{R.drawable.icon_home_video_1, R.drawable.icon_home_video_2, R.drawable.icon_home_video_3, R.drawable.icon_home_video_4, R.drawable.icon_home_video_5};

    @Override
    protected int layoutId() {
        return R.layout.fragment_home_new;
    }

    @Override
    protected void init(View rootView) {
        tvGifDong = rootView.findViewById(R.id.tv_gif_dong);
        srcrollview = rootView.findViewById(R.id.srcrollview);
        imSign = rootView.findViewById(R.id.im_sign);
        tvMoney = rootView.findViewById(R.id.tv_money);
        tvCoin = rootView.findViewById(R.id.tv_Coin);
        tvWithdraw = rootView.findViewById(R.id.tv_withdraw);
        marqueeview = rootView.findViewById(R.id.marqueeview);
        ivZqdk = rootView.findViewById(R.id.iv_zqdk);
        ivJbph = rootView.findViewById(R.id.iv_jbph);
        ivGoing = rootView.findViewById(R.id.iv_going);
        rlYxz = rootView.findViewById(R.id.rl_yxz);
        rlGaoez = rootView.findViewById(R.id.rl_gaoez);
        rlXsz = rootView.findViewById(R.id.rl_xsz);
        rlXyy = rootView.findViewById(R.id.rl_xyy);
        banner = rootView.findViewById(R.id.banner);
        floatbuttonlayout = rootView.findViewById(R.id.floatbutton);

        tvGuess = rootView.findViewById(R.id.tv_guess);

        Glide.with(context).load(R.drawable.gif_home_gong).into(tvGifDong);
        Glide.with(context).load(R.drawable.gif_everyday_red).into(imSign);
        Glide.with(context).load(R.drawable.gift_guess).into(tvGuess);
        initClick();
        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(activity);
        gyrosensor = GyrosensorUtils.getInstance(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden1) {
            getUserInfo();
            getBanner();
            getMarqueeviewMessage();
            getVideo();
            addGyro();
            setVideoBg();
        }
    }

    private void setVideoBg() {
        Random random = new Random();//默认构造方法
        int i = random.nextInt(5);
        floatbuttonlayout.setBackgroundResource(pic[i]);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        hidden1 = isHidden();
        if (!hidden1) {
            getUserInfo();
            getBanner();
            getMarqueeviewMessage();
            //开始轮播
            banner.startAutoPlay();
        } else {
            //结束轮播
            banner.stopAutoPlay();
        }
    }

    @Override
    protected void initData() throws NullPointerException {

    }

    private void initClick() {
        imSign.setOnClickListener(this);
        tvWithdraw.setOnClickListener(this);
        ivZqdk.setOnClickListener(this);
        ivJbph.setOnClickListener(this);
        ivGoing.setOnClickListener(this);
        rlGaoez.setOnClickListener(this);
        rlXyy.setOnClickListener(this);
        tvCoin.setOnClickListener(this);
        tvGuess.setOnClickListener(this);
        floatbuttonlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EmptyUtils.isTokenEmpty(activity) || EmptyUtils.isImeiEmpty(activity)) {
                    startActivity(new Intent(activity, NewLoginActivity.class));
                    return;
                }
                //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
//                TTAdManagerHolder.get().requestPermissionIfNecessary(activity);
//                Long aLong = countdown2.getTimeMap().get(2);
//                if (canWatch) {
//                    loadAd();
//                } else {
//                    ToastUtils.show("休息一会儿再看");
//                }
                videoTimeUser();
            }
        });

        tvGifDong.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (EmptyUtils.isTokenEmpty(activity) || EmptyUtils.isImeiEmpty(activity)) {
                    startActivity(new Intent(activity, NewLoginActivity.class));
                } else {
                    changeGame();
                }
            }
        });
        rlXsz.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (EmptyUtils.isTokenEmpty(activity) || EmptyUtils.isImeiEmpty(activity)) {
                    startActivity(new Intent(activity, NewLoginActivity.class));
                } else {
//                    YmConfig.setTitleBarColors(0xffffffff, 0xff000000);
//                    YmConfig.setOutUserId(userId);            //对接金币必须事先调用此接口
//                    YmConfig.openReader();
                    Intent intentwel = new Intent(activity, RedhatGameActivity.class);
                    intentwel.putExtra("url", "fuLiHui");
                    startActivity(intentwel);
                }
            }
        });
        rlYxz.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (EmptyUtils.isTokenEmpty(activity) || EmptyUtils.isImeiEmpty(activity)) {
                    startActivity(new Intent(activity, NewLoginActivity.class));
                } else {
                    getConductTask(2);
//                    AdvWebActivity.start(activity,"高额赚","http://u.zrb.net/Task/un0401501887080554?uid="+userId);
                }
                //AdvWebActivity.start(activity,"高额赚","http://u.zrb.net/Task/un0401501887080554?uid=huangxun0452@163.com");
            }
        });
        rlXyy.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (EmptyUtils.isTokenEmpty(activity) || EmptyUtils.isImeiEmpty(activity)) {
                    startActivity(new Intent(activity, NewLoginActivity.class));
                } else {
                    AdvWebActivity.start(activity,"任务赚","http://u.zrb.net/Task/un0401501887080554?uid="+userId);
                }
            }
        });
    }

    private void addGyro() {
        if (EmptyUtils.isTokenEmpty(activity)) {
            return;
        }
        CoordinatesBean coordinates = gyrosensor.getCoordinates();
        String anglex = String.format("%.2f", coordinates.anglex);
        String angley = String.format("%.2f", coordinates.angley);
        String anglez = String.format("%.2f", coordinates.anglez);
        HttpUtils.addGyro(anglex, angley, anglez, 1).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {

            }
        });

    }


    private void getMarqueeviewMessage() {
        HttpUtils.getListMessge().enqueue(new Observer<BaseResult<List<MarqueeviewMessageBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                List<MarqueeviewMessageBean> data = (List<MarqueeviewMessageBean>) response.data;
                if (EmptyUtils.isNotEmpty(data)) {
                    for (int i = 0; i < data.size(); i++) {
                        String mobile = data.get(i).mobile;
                        String content = data.get(i).content;
                        msgList.add("恭喜" + mobile + content);
                    }
                    marqueeview.startWithList(msgList);
                }
            }
        });
    }

    private void getBanner() {
        HttpUtils.queryBanner("2").enqueue(new Observer<BaseResult<List<BannerBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                bannerBeans = (List<BannerBean>) response.data;
                if (EmptyUtils.isNotEmpty(bannerBeans)) {
                    images.clear();
                    for (int i = 0; i < bannerBeans.size(); i++) {
                        images.add(bannerBeans.get(i).imageUrl);
                    }
                    startBanner();
                }
            }
        });
    }

    private void startBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        if (activity.isFinishing()) {
            return;
        }
        banner.setImageLoader(new GlideImageLoader());
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String linkUrl = bannerBeans.get(position).linkUrl;
                if (EmptyUtils.isTokenEmpty(activity)) {
                    return;
                }
                if (linkUrl.startsWith("http")) {
                    startActivity(new Intent(activity, WebActivity.class).putExtra("url", linkUrl));
                }

                if (linkUrl.equals("快速赚")) {
                    startActivity(new Intent(activity, NewTaskActivity.class));
                }
                if (linkUrl.equals("邀请赚")) {
                    startActivity(new Intent(activity, GeneralizeActivity.class));
                }
                if (linkUrl.equals("签到赚")) {
                    ((MainActivity) getActivity()).selecteTab(STATE_TREASUREPIG);
                }
                if (linkUrl.equals("会员")) {
                    startActivity(new Intent(activity, NewVipActivity.class));
                }


            }
        });
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.NOT_INDICATOR);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_sign:
                if (EmptyUtils.isTokenEmpty(activity) || EmptyUtils.isImeiEmpty(activity)) {
                    startActivity(new Intent(activity, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(activity, EveryDayTaskActivity.class));
                }
                break;
            case R.id.iv_zqdk:
                if (EmptyUtils.isTokenEmpty(activity) || EmptyUtils.isImeiEmpty(activity)) {
                    startActivity(new Intent(activity, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(activity, EarlyClockActivity.class));
                }
                break;
            case R.id.iv_jbph:
                if (EmptyUtils.isTokenEmpty(activity) || EmptyUtils.isImeiEmpty(activity)) {
                    startActivity(new Intent(activity, NewLoginActivity.class));
                } else {
                    String token = SPUtils.token(activity);
                    String imei = SPUtils.imei(activity);
                    startActivity(new Intent(activity, LotteryWebActivity.class).putExtra("url", RetrofitUtils.H5url + "/bzState.html?token=" + token + "&imei=" + imei));
                }
                break;
            case R.id.iv_going:
                if (EmptyUtils.isTokenEmpty(activity) || EmptyUtils.isImeiEmpty(activity)) {
                    startActivity(new Intent(activity, NewLoginActivity.class));
                } else {
                    Intent intent = new Intent(activity, NewGoingActivity.class);
                    intent.putExtra("title", "正在进行中");
                    startActivity(intent);
                }
                break;

            case R.id.rl_gaoez:
                if (EmptyUtils.isTokenEmpty(activity) || EmptyUtils.isImeiEmpty(activity)) {
                    startActivity(new Intent(activity, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(activity, NewTaskActivity.class));
//                   String url = YwSDK_WebActivity.Companion.getBase_url() + YwSDK.Companion.getSupplementUrl();
//                   Log.e("跳转地址",url);
//                   YwSDK_WebActivity.Companion.openDebug(getContext(),url);
                }
//              YwSDK_WebActivity.Companion.open(getContext());
                //传入广告id，直接进入广告的详情页

                break;
            case R.id.rl_xyy:
                if (EmptyUtils.isTokenEmpty(activity) || EmptyUtils.isImeiEmpty(activity)) {
                    startActivity(new Intent(activity, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(activity, BaoquGameActivity.class));
                }
                break;
            case R.id.tv_withdraw:
                if (EmptyUtils.isTokenEmpty(activity) || EmptyUtils.isImeiEmpty(activity)) {
                    startActivity(new Intent(activity, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(activity, NewWithdrawActivity.class));
                }
                break;
            case R.id.tv_Coin:
                if (EmptyUtils.isTokenEmpty(activity) || EmptyUtils.isImeiEmpty(activity)) {
                    startActivity(new Intent(activity, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(activity, AccountDataActivity.class));
                }

                break;
            default:
                break;
        }
    }

    private void getConductTask(final int i) {
        HttpUtils.getConductTask().enqueue(new Observer<BaseResult<ConductTaskBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    ConductTaskBean conductTaskBean = ((ConductTaskBean) response.data);
                    if (conductTaskBean.isGame == 1) {
                        if (i == 1) {
                            startActivity(new Intent(activity, MoreGameTaskActivity.class));
                        } else {
                            startActivity(new Intent(activity, GameListActivity.class));
                        }
                    }
                    if (conductTaskBean.isGame == 2) {
                        showHintDialog();
                    }
                    if (conductTaskBean.isGame == 3) {
                        showContinueDialog(conductTaskBean.task);
                    }
                }
            }
        });
    }

    private void changeGame() {
        HttpUtils.recommendGameTask().enqueue(new Observer<BaseResult<ChangeBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                gameBean = (ChangeBean) response.data;
                if (EmptyUtils.isNotEmpty(gameBean)) {
                    if (EmptyUtils.isEmpty(activity) || activity.isFinishing()) {
                        return;
                    }
                    interfaceName = gameBean.interfaceName;
                    gameId = gameBean.id;
                    if (EmptyUtils.isNotEmpty(gamePop) && gamePop.getPopupWindow().isShowing()) {
                        Glide.with(activity).load(gameBean.icon).into(ivGamePic);
                        tvGameName.setText(gameBean.gameTitle);
                        tvGameMoney.setText("+" + gameBean.gameGold + "元");
                        if ("task".equals(interfaceName)) {
                            tvEndtime.setText("还剩" + gameBean.enddate + "份");
                        } else {
                            long enddate = TimeUtils.formatShortDateMillis(gameBean.enddate);
                            String residueDays = TimeUtils.formatDuringDays(enddate);
                            tvEndtime.setText("还剩" + residueDays + "天");
                        }
                    } else {
                        gamePop(gameBean);
                    }
                }
            }
        });
    }

    private void getVideo() {
        if (EmptyUtils.isTokenEmpty(activity)) {
            return;
        }
        HttpUtils.videoCount().enqueue(new Observer<BaseResult<VideoswitchBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    VideoswitchBean videoswitchBean = ((VideoswitchBean) response.data);
                    if (videoswitchBean.res == 1) {
                        floatbuttonlayout.setVisibility(View.VISIBLE);
                    } else {
                        floatbuttonlayout.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    private void getUserInfo() {
        if (EmptyUtils.isTokenEmpty(activity)) {
            tvCoin.setText("0");
            tvMoney.setText("0.00元");
            return;
        }
        HttpUtils.getUserInfo().enqueue(new Observer<BaseResult<UserBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                String token = response.token;
                SPUtils.saveString(context, CommonConstant.USER_TOKEN, token);
                UserBean data = (UserBean) response.data;
                if (EmptyUtils.isNotEmpty(data)) {
                    String balance = data.balance;
                    mobile = data.mobile;
                    userId = data.userId;
                    String coin = data.coin;
                    String imei = data.imei;
                    SPUtils.saveString(context, CommonConstant.MOBIL_IMEI, imei);
                    EventBus.getDefault().postSticky(new EventBalance<>(EventCode.balance, balance));
                    EventBus.getDefault().postSticky(new EventUserInfo<>(EventCode.USERINFO, data));
                    tvCoin.setText(coin);
                    double percent = Long.parseLong(coin) / 11000.0 * 100;
                    double v = Math.floor(percent) / 100;
                    tvMoney.setText(v + "元");
                }
            }
        });
    }

    private void getUrl() {
        if ("PCDD".equals(interfaceName)) {
            HttpUtils.toPlay(mobile, gameId).enqueue(new Observer<BaseResult>() {
                @Override
                public void onSuccess(BaseResult response) {
                    String url = (String) response.data;
                    Intent intent = new Intent(activity, NextPCddGameDetailActivity.class);
                    intent.putExtra("URLS", url);
                    intent.putExtra("bean", gameBean);
                    startActivity(intent);

                }
            });
        }
        if ("MY".equals(interfaceName) || "bz-Android".equals(interfaceName)) {
            HttpUtils.toPlay(mobile, gameId).enqueue(new Observer<BaseResult>() {
                @Override
                public void onSuccess(BaseResult response) {
                    String url = (String) response.data;
                    Intent intent = new Intent(activity, NextMYGameDetailsActivity.class);
                    intent.putExtra("URLS", url);
                    intent.putExtra("bean", gameBean);
                    startActivity(intent);

                }
            });
        }
        if ("xw-Android".equals(interfaceName)) {
            HttpUtils.toPlay(mobile, gameId).enqueue(new Observer<BaseResult>() {
                @Override
                public void onSuccess(BaseResult response) {
                    String url = (String) response.data;
                    Intent intent = new Intent(activity, NextXWGameDetailActivity.class);
                    intent.putExtra("URLS", url);
                    intent.putExtra("bean", gameBean);
                    gamePop.getPopupWindow().dismiss2();
                    startActivity(intent);

                }
            });
        }
        if ("task".equals(interfaceName)) {
            HttpUtils.buildUrl(gameId).enqueue(new Observer<BaseResult>() {
                @Override
                public void onSuccess(BaseResult response) {
                    String url = (String) response.data;
                    Intent intent = new Intent(activity, TaskActivity.class);
                    intent.putExtra("URLS", url);
                    startActivity(intent);
                }
            });
        }
        gamePop.getPopupWindow().dismiss2();
    }

    private void gamePop(final ChangeBean gameBean) {
        gamePop = new MyCommonPopupWindow(activity, R.layout.pop_home_botoom_game, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                LinearLayout llChangeGame;
                Button btnSure;
                ImageView ivWelfare;
                ImageView ivClose;
                ivGamePic = view.findViewById(R.id.iv_game_pic);
                tvGameName = view.findViewById(R.id.tv_game_name);
                tvEndtime = view.findViewById(R.id.tv_endtime);
                llChangeGame = view.findViewById(R.id.ll_change_game);
                btnSure = view.findViewById(R.id.btn_sure);
                ivWelfare = view.findViewById(R.id.iv_welfare);
                tvGameMoney = view.findViewById(R.id.tv_money);
                ivClose = view.findViewById(R.id.iv_close);
                RoundedCorners roundedCorners = new RoundedCorners(DensityUtil.dip2px(context, 6));
                //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
                Glide.with(activity).load(R.drawable.gif_home_bottom).apply(options).into(ivWelfare);
                Glide.with(activity).load(gameBean.icon).into(ivGamePic);
                tvGameMoney.setText("+" + gameBean.gameGold + "元");
                tvGameName.setText(gameBean.gameTitle);

                if ("task".equals(gameBean.interfaceName)) {
                    tvEndtime.setText("还剩" + gameBean.enddate + "份");
                } else {
                    long enddate =TimeUtils.formatShortDateMillis(gameBean.enddate);
                    String residueDays = TimeUtils.formatDuringDays(enddate);
                    tvEndtime.setText("还剩" + residueDays + "天");
                }
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPopupWindow().dismiss2();

                    }
                });
                ivWelfare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPopupWindow().dismiss2();
                        String fuLiHui = AppContext.fuLiHui;
                        if (EmptyUtils.isEmpty(fuLiHui)) {
                            ToastUtils.show("功能待开发");
                        } else {
                            startActivity(new Intent(activity, WelfareActivity.class));
                        }
                    }
                });
                llChangeGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeGame();
                    }
                });
                btnSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getUrl();
                    }
                });
            }

            @Override
            protected void initWindow() {
                super.initWindow();
                NewPopWindow instance = getPopupWindow();
                instance.canDismiss = true;
                instance.setOnDismissListener(new NewPopWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        activity.getWindow().setAttributes(lp);
                    }
                });
            }
        };
        NewPopWindow popupWindow = gamePop.getPopupWindow();
        popupWindow.setAnimationStyle(R.style.picker_view_slide_anim);
        gamePop.getPopupWindow().showAtLocation(srcrollview, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.5f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
    }

    class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //设置图片圆角角度
            RoundedCorners roundedCorners = new RoundedCorners(DensityUtil.dip2px(context, 6));
            //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
            //Glide 加载图片简单用法
            Glide.with(context).load(path).apply(options).into(imageView);
        }
    }

    private void showContinueDialog(final ConductTaskBean.Task task) {
        if (EmptyUtils.isNotEmpty(continueDialog) && continueDialog.isShowing()) {
            return;
        }
        continueDialog = new NewCommonDialog(activity, false, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.dialog_continue_task, null);
        continueDialog.setContentView(contentView);
        String text = "您正在试玩" + task.name + "\n请先完成后，再试玩其他应用";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 5, 5 + task.name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) continueDialog.findViewById(R.id.tv_details)).setText(spannableString);
        continueDialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
//                Intent intent = new Intent(activity, TaskDetailActivity.class);
//                intent.putExtra("id", task.id);
//                startActivity(intent);
                String id = String.valueOf(task.id);
                HttpUtils.buildUrl(id).enqueue(new Observer<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult response) {
                        String url = (String) response.data;
                        Intent intent = new Intent(activity, TaskActivity.class);
                        intent.putExtra("URLS", url);
                        startActivity(intent);
                        continueDialog.dismiss();
                    }
                });
            }
        });
        continueDialog.show();
    }

    private void showHintDialog() {
        if (EmptyUtils.isNotEmpty(hintDialog) && hintDialog.isShowing()) {
            return;
        }
        hintDialog = new NewCommonDialog(activity, true, true, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.dialog_hint, null);
        hintDialog.setContentView(contentView);
        String text = "那就先从首页【拱一拱】开始吧！";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 6, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) hintDialog.findViewById(R.id.tv_details)).setText(spannableString);
        hintDialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hintDialog.dismiss();
            }
        });
        hintDialog.show();
    }

    /**
     * 倒计时弹窗
     *
     * @param countBean
     */
    private void showVideoReward(VideoCountBean countBean) {
        noticeVideoDialog = new NewCommonDialog(activity, true, true, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.dialog_show_video_reward, null);

        TextView tvReward;
        TextView tvVideoCount;
        CountdownView1 countdown;

        tvReward = contentView.findViewById(R.id.tv_reward);
        tvVideoCount = contentView.findViewById(R.id.tv_video_count);
        countdown = contentView.findViewById(R.id.countdown);
        double anInt = countBean.coin / 10000.0;
        tvReward.setText(anInt + "");
        tvVideoCount.setText("今日观看" + countBean.count + "/" + countBean.frequency + "次");

        if (countBean.count == countBean.frequency) {
            floatbuttonlayout.setVisibility(View.INVISIBLE);
        }
        if (countBean.count != countBean.frequency) {
            countdown.start(countBean.time, 1);
        }
        countdown.setOnCountdownEndListener(new CountdownView1.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView1 var1) {
                noticeVideoDialog.dismiss();
            }
        });
        contentView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeVideoDialog.dismiss();
            }
        });
        noticeVideoDialog.setContentView(contentView);
        noticeVideoDialog.show();

    }

    private void loadAd() {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("930729321")
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
                mttRewardVideoAd.showRewardVideoAd(activity, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");
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
                        getvideoReward();
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

    /**
     * 领取奖励
     */
    private void getvideoReward() {
        HttpUtils.videoUser().enqueue(new Observer<BaseResult<VideoCountBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    VideoCountBean countBean = ((VideoCountBean) response.data);
                    if (countBean.res == 3) {
                        showVideoReward(countBean);
                    }
                }
            }
        });
    }

    /**
     * 查看是否可以观看
     */
    private void videoTimeUser() {
        HttpUtils.videoTimeUser().enqueue(new Observer<BaseResult<VideoTimeUserBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    VideoTimeUserBean data = (VideoTimeUserBean) response.data;
                    if (data.res == 2) {
                        loadAd();
                    }
                    if (data.res == 3) {
                        ToastUtils.show("休息一会儿再看");
                    }
                }
            }
        });
    }
}
