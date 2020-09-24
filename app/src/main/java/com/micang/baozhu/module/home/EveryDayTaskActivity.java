package com.micang.baozhu.module.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.config.TTAdManagerHolder;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.home.ConductTaskBean;
import com.micang.baozhu.http.bean.home.EverydayRedGameBean;
import com.micang.baozhu.http.bean.home.EverydayRedListBean;
import com.micang.baozhu.http.bean.home.EverydayRedTaskinfoBean;
import com.micang.baozhu.http.bean.home.NewUserTaskBean;
import com.micang.baozhu.http.bean.home.SignBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.http.net.RetrofitUtils;
import com.micang.baozhu.module.answer.AnswerActivity;
import com.micang.baozhu.module.home.adapter.EverydayRedListAdapter;
import com.micang.baozhu.module.home.adapter.NewUserTaskAdapter;
import com.micang.baozhu.module.user.NewWithdrawActivity;
import com.micang.baozhu.module.user.UserDataActivity;
import com.micang.baozhu.module.view.NewCommonDialog;
import com.micang.baozhu.module.web.LotteryWebActivity;
import com.micang.baozhu.module.web.TaskActivity;
import com.micang.baozhu.module.web.VipTaskMYGameDetailActivity;
import com.micang.baozhu.module.web.VipTaskPcddGameDetailActivity;
import com.micang.baozhu.module.web.VipTaskXWGameDetailActivity;
import com.micang.baozhu.module.web.WelfareActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.OnMultiClickListener;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventUserInfo;
import com.micang.baselibrary.util.DensityUtil;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baselibrary.util.TLog;
import com.micang.baselibrary.util.TimeUtils;
import com.micang.baselibrary.view.MyCommonPopupWindow;
import com.micang.baselibrary.view.NewPopWindow;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@BindEventBus
public class EveryDayTaskActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "EveryDayTaskActivity";
    private LinearLayout llBack;
    private TextView tvRule;
    private TextView tvCoin;
    private ImageView ivEverydayPic;
    private RecyclerView recycleview;
    private ImageView ivSign;
    private LinearLayout llTask;
    private LinearLayout llGame;
    private ImageView ivGamePic;
    private TextView tvTaskState;
    private LinearLayout llTaskRed;
    private ImageView ivRedPic;
    private TextView tvRedMoney;
    private TextView tvStickDay;
    private ImageView ivNotice;
    private TextView tvReward;
    private ImageView ivGamePopPic;
    private TextView tvGameName;
    private TextView tvEndtime;
    private TextView tvGameMoney;
    private LinearLayout llNotice;
    private RecyclerView taskRecycleview;


    List<EverydayRedListBean> list = new ArrayList<>();
    List<NewUserTaskBean> userTaskBeanList = new ArrayList<>();
    private EverydayRedListAdapter adapter;
    private NewCommonDialog noticeDialog;
    private NewCommonDialog successDialog;
    private MyCommonPopupWindow gamePop;
    private String interfaceName;
    private int gameId;
    private EverydayRedTaskinfoBean.TpGameBean tpGame;
    private String taskCoin;
    int noticeType = 1;
    int openstatus;
    private NewUserTaskAdapter taskAdapter;
    private NewCommonDialog continueDialog;
    private NewCommonDialog hintDialog;
    private boolean canSign;
    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;
    private boolean mHasShowDownloadActive = false;
    private String userId;

    @Override
    public int layoutId() {
        return R.layout.activity_every_day_task;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setDarkMode(this);
        llBack = findViewById(R.id.ll_back);
        tvRule = findViewById(R.id.tv_rule);
        tvCoin = findViewById(R.id.tv_Coin);
        ivEverydayPic = findViewById(R.id.iv_everyday_pic);
        recycleview = findViewById(R.id.recycleview);
        llNotice = findViewById(R.id.ll_notice);
        ivSign = findViewById(R.id.iv_sign);
        llTask = findViewById(R.id.ll_task);
        llGame = findViewById(R.id.ll_game);
        ivGamePic = findViewById(R.id.iv_game_pic);
        tvTaskState = findViewById(R.id.tv_task_state);
        llTaskRed = findViewById(R.id.ll_task_red);
        ivRedPic = findViewById(R.id.iv_red_pic);
        tvRedMoney = findViewById(R.id.tv_red_money);
        tvStickDay = findViewById(R.id.tv_stickDay);
        ivNotice = findViewById(R.id.iv_notice);
        tvReward = findViewById(R.id.tv_reward);
        taskRecycleview = findViewById(R.id.taskRecycleview);
        initRecycleview();
        appList();
        inClick();
        setStickeday(0);
        setReward("0");
        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(this);
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

    private void initRecycleview() {
        final String token = SPUtils.token(this);
        final String imei = SPUtils.imei(this);
        taskRecycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        taskAdapter = new NewUserTaskAdapter(R.layout.item_new_usertask, userTaskBeanList);
        taskAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (userTaskBeanList.get(position).id) {
                    case 5:
                        startActivity(new Intent(EveryDayTaskActivity.this, AnswerActivity.class));
                        break;
                    case 6:
                        getConductTask();
                        break;
                    case 9:
                        startActivity(new Intent(EveryDayTaskActivity.this, NewVipActivity.class));
                        break;
                    case 7:
                        break;
                    case 20:
                        startActivity(new Intent(EveryDayTaskActivity.this, UserDataActivity.class));
                        break;
                    case 21:
                    case 22:
                        startActivity(new Intent(EveryDayTaskActivity.this, NewWithdrawActivity.class));
                        break;
                    case 23:
                        startActivity(new Intent(EveryDayTaskActivity.this, LotteryWebActivity.class).putExtra("url", RetrofitUtils.H5url + "/choujiang.html??token=" + token + "&imei=" + imei));
                        break;
                }
            }
        });
        taskRecycleview.setAdapter(taskAdapter);

        recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new EverydayRedListAdapter(R.layout.item_everydayred_list, list);
        recycleview.setAdapter(adapter);
    }

    private void appUserList() {
        HttpUtils.appUserList().enqueue(new Observer<BaseResult<List<NewUserTaskBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    List<NewUserTaskBean> data = (List<NewUserTaskBean>) response.data;
                    userTaskBeanList.clear();
                    userTaskBeanList.addAll(data);
                    taskAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDaySign();
        appUserList();
    }

    private void inClick() {
        llBack.setOnClickListener(this);
        tvRule.setOnClickListener(this);
        llTaskRed.setOnClickListener(this);
        ivSign.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (canSign) {
                    loadAd();
                } else {
                    ToastUtils.show("已领取");
                }
            }
        });

        llGame.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (EmptyUtils.isNotEmpty(tpGame)) {
                    if (tpGame.cashStatus == 2) {
                        tvTaskState.setText("已完成");
                        return;
                    } else {
                        tvTaskState.setText("未完成");
                        gamePop(tpGame.icon, tpGame.gameGold, tpGame.gameTitle, tpGame.enddate);
                    }
                } else {
                    recommendGameSign();
                }
            }
        });

        llNotice.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (openstatus == 1) {
                    openstatus = 2;
                } else {
                    openstatus = 1;
                }
                noticeReady();
            }
        });

    }

    private void loadAd() {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("930729525")
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
                TLog.d(message);
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                TLog.d(TAG, "rewardVideoAd video cached");
                //展示广告，并传入广告展示的场景
                mttRewardVideoAd.showRewardVideoAd(EveryDayTaskActivity.this, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");
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
                        getUserSign();
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

    private void noticeReady() {
        HttpUtils.noticeReady(noticeType, openstatus).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                getDaySign();
            }
        });
    }

    private void receiveCoin() {
        HttpUtils.receiveCoin().enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                showSuccess(taskCoin, 2);
                getDaySign();
            }
        });
    }

    private void getConductTask() {
        HttpUtils.getConductTask().enqueue(new Observer<BaseResult<ConductTaskBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    if (EveryDayTaskActivity.this.isFinishing()) {
                        return;
                    }
                    ConductTaskBean conductTaskBean = ((ConductTaskBean) response.data);
                    if (conductTaskBean.isGame == 1) {
                        startActivity(new Intent(EveryDayTaskActivity.this, GameListActivity.class));
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

    private void showContinueDialog(final ConductTaskBean.Task task) {
        if (EmptyUtils.isNotEmpty(continueDialog) && continueDialog.isShowing()) {
            return;
        }
        continueDialog = new NewCommonDialog(this, false, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_continue_task, null);
        continueDialog.setContentView(contentView);
        String text = "您正在试玩" + task.name + "\n请先完成后，再试玩其他应用";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 5, 5 + task.name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) continueDialog.findViewById(R.id.tv_details)).setText(spannableString);
        continueDialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
//                Intent intent = new Intent(EveryDayTaskActivity.this, TaskDetailActivity.class);
//                intent.putExtra("id", task.id);
//                startActivity(intent);
//                continueDialog.dismiss();
                String id = String.valueOf(task.id);
                HttpUtils.buildUrl(id).enqueue(new Observer<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult response) {
                        String url = (String) response.data;
                        Intent intent = new Intent(EveryDayTaskActivity.this, TaskActivity.class);
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
        hintDialog = new NewCommonDialog(this, true, true, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_hint, null);
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

    private void recommendGameSign() {
        HttpUtils.recommendGameSign().enqueue(new Observer<BaseResult<EverydayRedGameBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                EverydayRedGameBean data = (EverydayRedGameBean) response.data;
                EverydayRedGameBean.TpGameBean tpGame = data.tpGame;
                if (EmptyUtils.isNotEmpty(tpGame)) {
                    interfaceName = tpGame.interfaceName;
                    gameId = tpGame.id;
                    if (EmptyUtils.isNotEmpty(gamePop) && gamePop.getPopupWindow().isShowing()) {
                        Glide.with(EveryDayTaskActivity.this).load(tpGame.icon).into(ivGamePopPic);
                        tvGameName.setText(tpGame.gameTitle);
                        tvGameMoney.setText("+" + tpGame.gameGold + "元");
                        long enddate = tpGame.enddate;
                        String residueDays = TimeUtils.formatDuringDays(enddate);
                        tvEndtime.setText("还剩" + residueDays + "天");
                    } else {
                        gamePop(tpGame.icon, tpGame.gameGold, tpGame.gameTitle, tpGame.enddate);
                    }
                }
            }
        });
    }

    private void getUserSign() {
        HttpUtils.sign().enqueue(new Observer<BaseResult<SignBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    SignBean data = (SignBean) response.data;

                    int res = data.res;
                    String score = data.score;
                    if (res == 0) {
                        ToastUtils.show("领取失败");
                    } else if (res == 1) {
                        showSuccess(score, 1);
                        getDaySign();
                    } else {
                        ToastUtils.show("已领取");
                    }
                }

            }
        });
    }

    private void getDaySign() {
        HttpUtils.getDaySign().enqueue(new Observer<BaseResult<EverydayRedTaskinfoBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    EverydayRedTaskinfoBean data = (EverydayRedTaskinfoBean) response.data;
                    int stickTimes = data.stickTimes;
                    tvCoin.setText(data.sumCoin + "");
                    setDay(stickTimes);
                    setStickeday(stickTimes);
                    taskCoin = data.taskCoin;
                    setReward(taskCoin);
                    if (data.isSign == 1) {
                        canSign = true;
                        ivSign.setBackgroundResource(R.drawable.icon_everyday_red_sign);
                    } else {
                        canSign = false;
                        ivSign.setBackgroundResource(R.drawable.icon_everyday_red_sign_over);
                        settask(stickTimes);
                    }
                    if (data.isRemind == 0) { //关闭
                        openstatus = 2;
                        ivNotice.setBackgroundResource(R.drawable.icon_notice_close);
                    } else {                //开启
                        openstatus = 1;
                        ivNotice.setBackgroundResource(R.drawable.icon_notice_open);
                    }
                    if (EmptyUtils.isNotEmpty(data.tpGame)) {
                        tpGame = data.tpGame;
                        interfaceName = tpGame.interfaceName;
                        gameId = tpGame.id;
                        Glide.with(EveryDayTaskActivity.this).load(tpGame.icon).into(ivGamePic);
                        if (tpGame.cashStatus == 2) {
                            tvTaskState.setText("已完成");
                        } else {
                            tvTaskState.setText("未完成");
                        }
                    }
                    if (data.isTask == 1) {
                        tvRedMoney.setText(taskCoin + "元");
                        tvRedMoney.setTextColor(ContextCompat.getColor(EveryDayTaskActivity.this, R.color.FF3352));
                        ivRedPic.setBackgroundResource(R.drawable.icon_everyday_task_red);
                    } else {
                        tvRedMoney.setText("已领取");
                        tvRedMoney.setTextColor(ContextCompat.getColor(EveryDayTaskActivity.this, R.color.color_9ea9bc));
                        ivRedPic.setBackgroundResource(R.drawable.icon_everyday_task_red_over);
                    }
                }
            }
        });
    }

    private void settask(int stickTimes) {
        if (stickTimes > 6) {
            ivSign.setVisibility(View.GONE);
            llTask.setVisibility(View.VISIBLE);
        } else {
            ivSign.setVisibility(View.VISIBLE);
            llTask.setVisibility(View.GONE);
        }
    }


    private void appList() {
        HttpUtils.appList().enqueue(new Observer<BaseResult<List<EverydayRedListBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    List<EverydayRedListBean> data = (List<EverydayRedListBean>) response.data;
                    list.clear();
                    list.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_rule:
                showNotice();
                break;
            case R.id.ll_task_red:
                receiveCoin();
                break;
        }
    }

    private void gamePop(final String icon, final double gameGold, final String gameTitle, final long enddate) {
        gamePop = new MyCommonPopupWindow(this, R.layout.pop_home_botoom_game, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                LinearLayout llChangeGame;
                Button btnSure;
                ImageView ivWelfare;
                ImageView ivClose;
                ivGamePopPic = view.findViewById(R.id.iv_game_pic);
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
                Glide.with(EveryDayTaskActivity.this).load(R.drawable.gif_home_bottom).apply(options).into(ivWelfare);
                Glide.with(EveryDayTaskActivity.this).load(icon).into(ivGamePopPic);
                tvGameMoney.setText("+" + gameGold + "元");
                tvGameName.setText(gameTitle);

                String residueDays = TimeUtils.formatDuringDays(enddate);
                tvEndtime.setText("还剩" + residueDays + "天");
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
                            startActivity(new Intent(EveryDayTaskActivity.this, WelfareActivity.class));
                        }
                    }
                });
                llChangeGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recommendGameSign();
                    }
                });
                btnSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPopupWindow().dismiss2();
                        if ("PCDD".equals(interfaceName)) {
                            Intent intent = new Intent(EveryDayTaskActivity.this, VipTaskPcddGameDetailActivity.class);
                            intent.putExtra("type", "3");
                            intent.putExtra("gameid", gameId);
                            startActivity(intent);
                        }
                        if ("MY".equals(interfaceName) || "bz-Android".equals(interfaceName)) {
                            Intent intent = new Intent(EveryDayTaskActivity.this, VipTaskMYGameDetailActivity.class);
                            intent.putExtra("type", "3");
                            intent.putExtra("gameid", gameId);
                            startActivity(intent);
                        }
                        if ("xw-Android".equals(interfaceName)) {
                            Intent intent = new Intent(EveryDayTaskActivity.this, VipTaskXWGameDetailActivity.class);
                            intent.putExtra("type", "3");
                            intent.putExtra("gameid", gameId);
                            startActivity(intent);
                        }
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
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }
                });
            }
        };
        NewPopWindow popupWindow = gamePop.getPopupWindow();
        popupWindow.setAnimationStyle(R.style.picker_view_slide_anim);
        gamePop.getPopupWindow().showAtLocation(llBack, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    private void showNotice() {
        if (this.isFinishing()) {
            return;
        }
        noticeDialog = new NewCommonDialog(this, true, true, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_everyday_red_rule, null);
        noticeDialog.setContentView(contentView);
        contentView.findViewById(R.id.im_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog.dismiss();
            }
        });
        contentView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog.dismiss();
            }
        });
        noticeDialog.show();
    }

    private void showSuccess(String score, int type) {
        if (this.isFinishing()) {
            return;
        }
        successDialog = new NewCommonDialog(this, true, true, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_everyday_red_success, null);
        successDialog.setContentView(contentView);
        TextView tvcoin = contentView.findViewById(R.id.tv_coin);
        if (type == 1) {  //签到
            tvcoin.setText("+" + score + "金币");
        } else { //任务
            tvcoin.setText("+" + score + "元");
        }
        contentView.findViewById(R.id.im_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                successDialog.dismiss();
            }
        });
        contentView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                successDialog.dismiss();
                startActivity(new Intent(EveryDayTaskActivity.this, WelfareActivity.class));
            }
        });
        successDialog.show();
    }

    private void setStickeday(int day) {
        String str1 = "您已连续<font color='#FA2E41'>" + day + "天" + "</font>" + "奖励";
        tvStickDay.setText(Html.fromHtml(str1));
    }

    private void setReward(String money) {
        String str1 = "每次连续领红包7天起，每日额外获得<font color='#FA2E41'>" + money + "元奖励" + "</font>" + "!";
        tvReward.setText(Html.fromHtml(str1));
    }

    private void setDay(int stickTimes) {
        switch (stickTimes) {
            case 0:
                ivEverydayPic.setBackgroundResource(R.drawable.icon_everyday0);
                break;
            case 1:
                ivEverydayPic.setBackgroundResource(R.drawable.icon_everyday1);
                break;
            case 2:
                ivEverydayPic.setBackgroundResource(R.drawable.icon_everyday2);
                break;
            case 3:
                ivEverydayPic.setBackgroundResource(R.drawable.icon_everyday3);
                break;
            case 4:
                ivEverydayPic.setBackgroundResource(R.drawable.icon_everyday4);
                break;
            case 5:
                ivEverydayPic.setBackgroundResource(R.drawable.icon_everyday5);
                break;
            case 6:
                ivEverydayPic.setBackgroundResource(R.drawable.icon_everyday6);
                break;
            case 7:
                ivEverydayPic.setBackgroundResource(R.drawable.icon_everyday7);
                break;
            default:
                ivEverydayPic.setBackgroundResource(R.drawable.icon_everyday7);
                break;
        }
    }
}
