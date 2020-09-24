package com.micang.baozhu.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
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
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.VIpTaskGameBean;
import com.micang.baozhu.http.bean.home.SelectVipNewsBean;
import com.micang.baozhu.http.bean.home.VipInfoBean;
import com.micang.baozhu.http.bean.home.VipListBean;
import com.micang.baozhu.http.bean.user.EncourageBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.home.adapter.AutoPollAdapter;
import com.micang.baozhu.module.home.adapter.VipFragmentAdapter;
import com.micang.baozhu.module.user.BuyVipActivity;
import com.micang.baozhu.module.user.adapter.UserEncouragePopAdapter;
import com.micang.baozhu.module.view.AutoPollRecyclerView;
import com.micang.baozhu.module.view.DragFloatActionButton;
import com.micang.baozhu.module.web.VipLogActivity;
import com.micang.baozhu.module.web.VipTaskMYGameDetailActivity;
import com.micang.baozhu.module.web.VipTaskPcddGameDetailActivity;
import com.micang.baozhu.module.web.VipTaskXWGameDetailActivity;
import com.micang.baozhu.module.web.WelfareActivity;
import com.micang.baozhu.util.AppUtils;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.OnMultiClickListener;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.DensityUtil;
import com.micang.baselibrary.util.TimeUtils;
import com.micang.baselibrary.view.MyCommonPopupWindow;
import com.micang.baselibrary.view.NewPopWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * VIP界面
 */
public class NewVipActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private LinearLayout llBack;
    private TextView tvLog;
    private TextView tvMoney;
    private TextView tvData;
    private Button btOpen;
    private ViewPager viewPager;
    private ImageView ivVipService1;
    private ImageView ivVipService2;
    private ImageView ivVipChangeService;
    private AutoPollRecyclerView recyclerViewMessage;
    private TextView tvGameAddition;
    private TextView tvPrecedenceCheck;
    private LinearLayout llVipService2;
    private LinearLayout llVipService2Task;
    private ImageView ivVipFirstAward;
    private TextView tvVipFirstAward;
    private TextView tvVipFirstAwardState;
    private TextView tvEverydayCoinAward;
    private TextView tvEverydayAward;
    private ImageView ivGamePic;
    private TextView tvGameState;
    private LinearLayout llGame;
    private RelativeLayout rlBuy;
    private LinearLayout llNoHighVip;
    private LinearLayout llHighVip;

    private AutoPollAdapter selectVipNewsAdapter;
    private VipFragmentAdapter fragmentAdapter;
    private int currentVip;
    private boolean showTask = false;
    private VipInfoBean vipInfoBean;

    private List<String> encourageList = new ArrayList<>();
    private ArrayList<VIPFragment> mFragments = new ArrayList<>();
    private List<SelectVipNewsBean> list = new ArrayList<>();
    private List<VipListBean> vipListBeans = new ArrayList<>();
    private MyCommonPopupWindow servicePop;
    private MyCommonPopupWindow gamePop;
    private TextView tvEndtime;
    private TextView tvGameName;
    private TextView tvGameMoney;
    private ImageView ivGamePicpop;
    private VIpTaskGameBean gameBean;
    private String interfaceName;
    private int gameId;
    private VipInfoBean.GameBean game;
    private int isGameTask;
    private MyCommonPopupWindow encouragePopupWindow;

    @Override
    public int layoutId() {
        return R.layout.activity_new_vip;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setDarkMode(this);
        initView();
        initData();
        initclick();

    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewMessage.start();
        if (currentVip != 0) {
            getVipIn(currentVip);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        recyclerViewMessage.stop();
    }

    private void initView() {

        llBack = findViewById(R.id.ll_back);
        tvLog = findViewById(R.id.tv_log);
        tvMoney = findViewById(R.id.tv_money);
        tvData = findViewById(R.id.tv_data);
        btOpen = findViewById(R.id.bt_open);
        ivVipService1 = findViewById(R.id.iv_vip_service1);
        ivVipService2 = findViewById(R.id.iv_vip_service2);
        ivVipChangeService = findViewById(R.id.iv_vip_change_service);
        tvGameAddition = findViewById(R.id.tv_game_addition);
        tvPrecedenceCheck = findViewById(R.id.tv_precedence_check);
        llVipService2 = findViewById(R.id.ll_vip_service2);
        llVipService2Task = findViewById(R.id.ll_vip_service2_task);

        ivGamePic = findViewById(R.id.iv_game_pic);
        tvGameState = findViewById(R.id.tv_game_state);
        llGame = findViewById(R.id.ll_game);
        rlBuy = findViewById(R.id.rl_buy);


        llNoHighVip = findViewById(R.id.ll_no_high_vip);
        llHighVip = findViewById(R.id.ll_high_vip);

        ivVipFirstAward = findViewById(R.id.iv_vip_first_award);
        tvVipFirstAward = findViewById(R.id.tv_vip_first_award);
        tvVipFirstAwardState = findViewById(R.id.tv_vip_first_award_state);


        tvEverydayCoinAward = findViewById(R.id.tv_everyday_coin_award);
        tvEverydayAward = findViewById(R.id.tv_everyday_award);


        recyclerViewMessage = findViewById(R.id.recycleview_message);
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));//设置LinearLayoutManager.HORIZONTAL  则水平滚动
        selectVipNewsAdapter = new AutoPollAdapter(this, list);
        recyclerViewMessage.setAdapter(selectVipNewsAdapter);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setPadding(DensityUtil.dip2px(this, 32), 0, DensityUtil.dip2px(this, 32), 0);

        if (viewPager.getAdapter() == null) {
            fragmentAdapter = new VipFragmentAdapter(getSupportFragmentManager(),
                    mFragments);
            viewPager.setAdapter(fragmentAdapter);
        } else {
            fragmentAdapter = (VipFragmentAdapter) viewPager.getAdapter();
            fragmentAdapter.updateFragments(mFragments);
        }
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageMargin(DensityUtil.dip2px(this, 20));
        viewPager.addOnPageChangeListener(this);
        setBalance("0.00");
    }

    private void initclick() {
        llBack.setOnClickListener(this);
        tvLog.setOnClickListener(this);
        btOpen.setOnClickListener(this);
        ivVipService1.setOnClickListener(this);
        ivVipChangeService.setOnClickListener(this);

        tvEverydayAward.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (isGameTask == 2 || isGameTask == 4) {
                    receiveActiveNews();
                }
                if (isGameTask == 1 || isGameTask == 3) {
                    ivVipService2.setBackgroundResource(R.drawable.icon_vip_task_service);
                    llVipService2.setVisibility(View.GONE);
                    llVipService2Task.setVisibility(View.VISIBLE);
                    showTask = !showTask;
                }
            }
        });
        ivVipService1.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (EmptyUtils.isNotEmpty(vipInfoBean)) {
                    servicePop(vipInfoBean);
                }
            }
        });
        llGame.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (isGameTask == 1 || isGameTask == 3) {
                    if (EmptyUtils.isNotEmpty(game)) {
                        gamePop(game.icon, game.gameGold, game.gameTitle, game.enddate);
                    } else {
                        changeGame(currentVip);
                    }
                }
            }
        });
    }


    private void initData() {
        getVIPMessage();
        getVipList();
    }

    private void receiveActiveNews() {
        HttpUtils.receiveActiveNews(currentVip).enqueue(new Observer<BaseResult<EncourageBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    EncourageBean data = ((EncourageBean) response.data);
                    String coin = data.coin;
                    int maxDay = data.maxDay;
                    initEncouragePopWindow(coin, maxDay);
                }
            }
        });
    }

    private void getVipList() {
        HttpUtils.vipList().enqueue(new Observer<BaseResult<List<VipListBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    List<VipListBean> data = (List<VipListBean>) response.data;
                    vipListBeans.clear();
                    for (int i = 0; i < data.size(); i++) {
                        VipListBean vipListBean = data.get(i);
                        if (vipListBean.isRenew == 2 && vipListBean.isBuy == 0 && vipListBean.isPay != 0) {
                            //不显示
                        } else {
                            //显示
                            vipListBeans.add(vipListBean);
                        }
                    }
                    for (int j = 0; j < vipListBeans.size(); j++) {
                        if (j == 0) {
                            if (currentVip == 0)
                                currentVip = vipListBeans.get(j).id;
                            getVipIn(currentVip);
                        }
                        mFragments.add(VIPFragment.newInstance(vipListBeans.get(j)));
                    }
                    fragmentAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getVipIn(int id) {
        HttpUtils.vipinfo(id).enqueue(new Observer<BaseResult<VipInfoBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data) && !NewVipActivity.this.isFinishing()) {
                    vipInfoBean = (VipInfoBean) response.data;
                    VipInfoBean.MVipInfoBean mVipInfo = vipInfoBean.mVipInfo;
                    isGameTask = vipInfoBean.isGameTask;
                    if (isGameTask == 1) {
                        tvGameState.setText("未完成");
                    }
                    if (isGameTask == 2) {
                        tvGameState.setText("已完成");
                    }

                    ivVipService2.setBackgroundResource(R.drawable.icon_vip_service2);
                    llVipService2.setVisibility(View.VISIBLE);
                    llVipService2Task.setVisibility(View.GONE);

                    if (EmptyUtils.isNotEmpty(vipInfoBean.game)) {
                        game = vipInfoBean.game;
                        gameId = game.id;
                        interfaceName = game.interfaceName;
                        Glide.with(NewVipActivity.this).load(game.icon).into(ivGamePic);
                    } else {
                        game = null;
                        gameId = 0;
                        interfaceName = "";
                        Glide.with(NewVipActivity.this).load("").into(ivGamePic);
                    }


                    if (EmptyUtils.isNotEmpty(mVipInfo)) {
                        if (mVipInfo.highVip == 1) {
                            llNoHighVip.setVisibility(View.VISIBLE);
                            llHighVip.setVisibility(View.GONE);
                        } else {
                            llNoHighVip.setVisibility(View.GONE);
                            llHighVip.setVisibility(View.VISIBLE);
                        }
                        if (mVipInfo.isPay == 0) {
                            if (mVipInfo.firstRewardUnit == 1) {
                                ivVipFirstAward.setBackgroundResource(R.drawable.icon_vip_1_1);
                                double v = mVipInfo.firstReward / 10000.0;
                                tvVipFirstAward.setText(v + "万金币");
                            }
                        } else {
                            if (mVipInfo.continueRewardUnit == 1) {
                                ivVipFirstAward.setBackgroundResource(R.drawable.icon_vip_1_1);
                                double v = mVipInfo.continueReward / 10000.0;
                                tvVipFirstAward.setText(v + "万金币");
                            }
                        }
                        double everydayCoin = mVipInfo.everydayActiveCoin / 10000.0;
                        tvEverydayCoinAward.setText(everydayCoin + "万");
                        if (mVipInfo.isTask == 2) {
                            ivVipChangeService.setVisibility(View.GONE);
                        } else {
                            ivVipChangeService.setVisibility(View.VISIBLE);
                        }

                        if (mVipInfo.isBuy == 0) {
                            rlBuy.setVisibility(View.VISIBLE);
                            tvVipFirstAwardState.setText("开通即送");
                            tvVipFirstAwardState.setTextColor(getResources().getColor(R.color.color_ffffff));
                            tvVipFirstAwardState.setBackgroundResource(R.drawable.bg_radius_4_color_f05a24_ff1370);
                            ivVipChangeService.setVisibility(View.GONE);
                        } else {
                            rlBuy.setVisibility(View.INVISIBLE);
                            tvVipFirstAwardState.setText("已领取");
                            tvVipFirstAwardState.setTextColor(getResources().getColor(R.color.color_a4a4a4));
                            tvVipFirstAwardState.setBackgroundResource(R.drawable.bg_radius_4_color_e4e4e4);
                        }
                        setPrecedenceCheck();
                        setGameaddition(mVipInfo.gameAddition);

                        setBalance(mVipInfo.price);
                        tvData.setText("（" + mVipInfo.useDay + "）天");
                    }
                }
            }
        });
    }

    private void changeGame(int currentVip) {
        HttpUtils.recommendGameVip(currentVip).enqueue(new Observer<BaseResult<VIpTaskGameBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                gameBean = (VIpTaskGameBean) response.data;
                if (EmptyUtils.isNotEmpty(gameBean)) {
                    if (EmptyUtils.isEmpty(NewVipActivity.this) || NewVipActivity.this.isFinishing()) {
                        return;
                    }
                    interfaceName = gameBean.tpGame.interfaceName;
                    gameId = gameBean.tpGame.id;
                    if (EmptyUtils.isNotEmpty(gamePop) && gamePop.getPopupWindow().isShowing()) {
                        Glide.with(NewVipActivity.this).load(gameBean.tpGame.icon).into(ivGamePicpop);
                        tvGameName.setText(gameBean.tpGame.gameTitle);
                        tvGameMoney.setText("+" + gameBean.tpGame.gameGold + "元");
                        long enddate = gameBean.tpGame.enddate;
                        String residueDays = TimeUtils.formatDuringDays(enddate);
                        tvEndtime.setText("还剩" + residueDays + "天");
                    } else {
                        gamePop(gameBean.tpGame.icon, gameBean.tpGame.gameGold, gameBean.tpGame.gameTitle, gameBean.tpGame.enddate);
                    }
                }
            }
        });
    }

    private void getVIPMessage() {
        String umeng_channel = AppUtils.getAppMetaData(this, "UMENG_CHANNEL");
        HttpUtils.selectVipNews(umeng_channel).enqueue(new Observer<BaseResult<List<SelectVipNewsBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    List<SelectVipNewsBean> data = (List<SelectVipNewsBean>) response.data;
                    list.clear();
                    list.addAll(data);
                    selectVipNewsAdapter.notifyDataSetChanged();
                    recyclerViewMessage.start();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_log:
                startActivity(new Intent(NewVipActivity.this, VipLogActivity.class));
                break;
            case R.id.bt_open:
                if (EmptyUtils.isEmpty(vipInfoBean)) {
                    return;
                }
                Intent intent = new Intent(NewVipActivity.this, BuyVipActivity.class);
                intent.putExtra("VIPID", vipInfoBean.mVipInfo.id + "");
                intent.putExtra("PRICE", vipInfoBean.mVipInfo.price);
                intent.putExtra("DATA", vipInfoBean.mVipInfo.useDay + "");
                intent.putExtra("VIPNAME", vipInfoBean.mVipInfo.name);
                startActivity(intent);

                break;
            case R.id.iv_vip_change_service:
                if (showTask) {
                    ivVipService2.setBackgroundResource(R.drawable.icon_vip_service2);
                    llVipService2.setVisibility(View.VISIBLE);
                    llVipService2Task.setVisibility(View.GONE);
                    showTask = !showTask;
                } else {
                    ivVipService2.setBackgroundResource(R.drawable.icon_vip_task_service);
                    llVipService2.setVisibility(View.GONE);
                    llVipService2Task.setVisibility(View.VISIBLE);
                    showTask = !showTask;
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        //position只 有当页面真正切换的时候，才会调用这个方法，如果页面index一直没有变，则不会调用改方法
        VipListBean vipListBean = vipListBeans.get(i);
        currentVip = vipListBean.id;
        getVipIn(currentVip);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void servicePop(final VipInfoBean vipInfoBean) {
        servicePop = new MyCommonPopupWindow(this, R.layout.pop_vip_service, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                TextView tvFirstAward;
                TextView tvContinueAward;
                TextView tvEverydayAward;
                TextView tvVipDay;

                tvFirstAward = view.findViewById(R.id.tv_firstAward);
                tvContinueAward = view.findViewById(R.id.tv_continueAward);
                tvEverydayAward = view.findViewById(R.id.tv_everyday_award);
                tvVipDay = view.findViewById(R.id.tv_vip_day);
                if (EmptyUtils.isNotEmpty(vipInfoBean.mVipInfo)) {
                    String s = vipInfoBean.mVipInfo.firstRewardUnit == 1 ? "万金币" : "万金币";
                    String str1 = "首次开通会员奖励<font color='#FF2B49'>" + vipInfoBean.mVipInfo.firstReward / 10000.0 + s + "</font>" + "(立即到账)；";
                    tvFirstAward.setText(Html.fromHtml(str1));

                    String s1 = vipInfoBean.mVipInfo.continueRewardUnit == 1 ? "万金币" : "万金币";
                    String str2 = "续开会员奖励<font color='#FF2B49'>" + vipInfoBean.mVipInfo.continueReward / 10000.0 + s1 + "</font>" + "(立即到账)；";
                    tvContinueAward.setText(Html.fromHtml(str2));

                    String str3 = "开通会员后次日期每日再赠送<font color='#FF2B49'>" + vipInfoBean.mVipInfo.everydayActiveCoin / 10000.0 + "万金币" + "</font>" + "(需用户主动领取)；";
                    tvEverydayAward.setText(Html.fromHtml(str3));


                    String str5 = "会员有效期<font color='#FF2B49'>" + vipInfoBean.mVipInfo.useDay + "</font>" + "天；";
                    tvVipDay.setText(Html.fromHtml(str5));
                }

                view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPopupWindow().dismiss2();

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
        NewPopWindow popupWindow = servicePop.getPopupWindow();
        popupWindow.setAnimationStyle(R.style.animScale);
        servicePop.getPopupWindow().showAtLocation(llBack, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    private void gamePop(final String url, final double gameGold, final String gameTitle, final long enddate) {
        gamePop = new MyCommonPopupWindow(this, R.layout.pop_home_botoom_game, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                LinearLayout llChangeGame;
                Button btnSure;
                ImageView ivWelfare;
                ImageView ivClose;
                ivGamePicpop = view.findViewById(R.id.iv_game_pic);
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
                Glide.with(NewVipActivity.this).load(R.drawable.gif_home_bottom).apply(options).into(ivWelfare);
                Glide.with(NewVipActivity.this).load(url).into(ivGamePicpop);
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
                            Intent intent = new Intent(NewVipActivity.this, WelfareActivity.class);
                            intent.putExtra("url", fuLiHui);
                            startActivity(intent);
                        }
                    }
                });
                llChangeGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeGame(currentVip);
                    }
                });
                btnSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPopupWindow().dismiss2();
                        if ("PCDD".equals(interfaceName)) {
                            Intent intent = new Intent(NewVipActivity.this, VipTaskPcddGameDetailActivity.class);
                            intent.putExtra("type", "1");
                            intent.putExtra("gameid", gameId);
                            intent.putExtra("vipId", currentVip);
                            startActivity(intent);
                        }
                        if ("MY".equals(interfaceName) || "bz-Android".equals(interfaceName)) {
                            Intent intent = new Intent(NewVipActivity.this, VipTaskMYGameDetailActivity.class);
                            intent.putExtra("type", "1");
                            intent.putExtra("gameid", gameId);
                            intent.putExtra("vipId", currentVip);
                            startActivity(intent);
                        }
                        if ("xw-Android".equals(interfaceName)) {
                            Intent intent = new Intent(NewVipActivity.this, VipTaskXWGameDetailActivity.class);
                            intent.putExtra("type", "1");
                            intent.putExtra("gameid", gameId);
                            intent.putExtra("vipId", currentVip);
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

    private void initEncouragePopWindow(final String coin, final int maxDay) {
        encouragePopupWindow = new MyCommonPopupWindow(this, R.layout.user_encourage_pop, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                RecyclerView userencourageRv = view.findViewById(R.id.recycleview);
                Button btGoon = view.findViewById(R.id.bt_goon);
                LinearLayout laoutNoStickDay = view.findViewById(R.id.layout_noStickDay);
                TextView tvNoStickDay = view.findViewById(R.id.tv_noStickDay);
                TextView tvEncourageCoin = view.findViewById(R.id.tv_encourageCoin);

                userencourageRv.setLayoutManager(new LinearLayoutManager(NewVipActivity.this, LinearLayoutManager.HORIZONTAL, false));
                encourageList.clear();
                int day = 1;
                if (maxDay > 5) {
                    day = 5;
                } else {
                    day = maxDay;
                }
                for (int i = 0; i < day; i++) {
                    encourageList.add("1");
                }
                if (maxDay - 1 >= 1) {

                    laoutNoStickDay.setVisibility(View.VISIBLE);
                    tvNoStickDay.setText("您已有" + (maxDay - 1) + "天未领取鼓励金！");
                } else {
                    tvNoStickDay.setVisibility(View.INVISIBLE);
                }
                tvEncourageCoin.setText("+" + coin);
                UserEncouragePopAdapter adapter = new UserEncouragePopAdapter(R.layout.user_encourage_pop_item, encourageList);
                userencourageRv.setAdapter(adapter);
                btGoon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPopupWindow().dismiss2();

                    }
                });
            }

            @Override
            protected void initWindow() {
                super.initWindow();
                NewPopWindow instance = getPopupWindow();
                instance.canDismiss = false;
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
        NewPopWindow popupWindow = this.encouragePopupWindow.getPopupWindow();
        popupWindow.setAnimationStyle(R.style.animScale);
        this.encouragePopupWindow.getPopupWindow().showAtLocation(llBack, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams lp = NewVipActivity.this.getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    private void setBalance(String balance) {
        String text = "¥ " + balance;
        SpannableString spannableString = new SpannableString(text);
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(DensityUtil.sp2px(NewVipActivity.this, 12), false);
        spannableString.setSpan(absoluteSizeSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvMoney.setText(spannableString);
    }

    private void setGameaddition(String gameaddition) {
        String str1 = "游戏<font color='#FF2B49'>" + "+" + gameaddition + "%" + "</font>" + "奖励";
        tvGameAddition.setText(Html.fromHtml(str1));
    }

    private void setPrecedenceCheck() {
        String str1 = "提成<font color='#FF2B49'>" + "+1.8倍" + "</font>";
        tvPrecedenceCheck.setText(Html.fromHtml(str1));
    }

}
