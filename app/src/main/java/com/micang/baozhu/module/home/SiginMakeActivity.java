package com.micang.baozhu.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.SignMakeBean;
import com.micang.baozhu.http.bean.SignMakeChangeBean;
import com.micang.baozhu.http.bean.SignMakeEarnMoneyBean;
import com.micang.baozhu.http.bean.SignMakeGameBean;
import com.micang.baozhu.http.bean.home.MarqueeviewMessageBean;
import com.micang.baozhu.http.bean.login.ServiceBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.home.adapter.SignMakeAdapter;
import com.micang.baozhu.module.login.NewLoginActivity;
 import com.micang.baozhu.module.web.SignMakeMYGameDetailActivity;
import com.micang.baozhu.module.web.SignMakePcddGameDetailActivity;
import com.micang.baozhu.module.web.SignMakeXWGameDetailActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baselibrary.util.TimeUtils;
import com.micang.baselibrary.view.MyCommonPopupWindow;
import com.micang.baselibrary.view.NewPopWindow;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SiginMakeActivity extends BaseActivity {

    private LinearLayout llBack;
    private LinearLayout llRetroactivecard;
    private FrameLayout llParent;
    private MarqueeView marqueeview;
    private List<String> msgList = new ArrayList<>();
    private TextView tvRetroactiveNumber;
    private RecyclerView recycle;
    private TextView tvOvertaskNumber;
    private TextView tvTodaymoney;
    public static SiginMakeActivity instance = null;
    private MyCommonPopupWindow showsignWindow;
    private Handler mHandler = new Handler();
    private List<SignMakeBean.SignBean> list = new ArrayList<>();
    private int today = 1;
    private SignMakeAdapter adapter;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private MyCommonPopupWindow moneyWindow;
    private MyCommonPopupWindow showNoCarnWindow;
    private MyCommonPopupWindow showTaskWindow;
    private int taskCount;
    private int coinTotal;
    private int bqCount;
    private int addDiscount;
    int game2 = 0;
    int count = 0;
    String gametype = "";
    int gameid = 0;
    LinearLayout llGame1;
    ImageView ivGamePic1;
    TextView tvTaskState1;
    LinearLayout llGame2;
    ImageView ivGamePic2;
    TextView tvTaskState2;
    LinearLayout llGame3;
    ImageView ivGamePic3;
    TextView tvTaskState3;
    ImageView iv1;
    ImageView iv2;
    ImageView iv3;
    ImageView ivDdddiscout;
    ConstraintLayout gamelayout;
    ImageView ivGamePic;
    TextView tvGameName;
    TextView tvTryNumber;
    TextView tvTryLastdata;
    TextView tvGameReward;
    ConstraintLayout piglayout;
    LinearLayout llChangeGame;
    TextView btBegin;
    TextView tvAddMoney;
    TextView tvGameAcout;
    private MyCommonPopupWindow showCarnWindow;
    private boolean showTask = true;

    @Override
    public int layoutId() {
        return R.layout.activity_sign_make;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setDarkMode(this);
        instance = this;
        llParent = findViewById(R.id.ll_parent);
        llBack = findViewById(R.id.ll_back);
        llRetroactivecard = findViewById(R.id.ll_retroactivecard);
        marqueeview = findViewById(R.id.marqueeview);
        tvRetroactiveNumber = findViewById(R.id.tv_retroactive_number);
        recycle = findViewById(R.id.recycle);
        tvOvertaskNumber = findViewById(R.id.tv_overtask_number);
        tvTodaymoney = findViewById(R.id.tv_todaymoney);
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tv4 = findViewById(R.id.tv_4);
        tv5 = findViewById(R.id.tv_5);
        tv6 = findViewById(R.id.tv_6);
        tv7 = findViewById(R.id.tv_7);
        final boolean firstsignmake = SPUtils.getBoolean(this, "firstsignmake", true);
        getMarqueeviewMessage();
//        todayFinish();
        initClick();
        initText();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firstsignmake) {
                    showPop();
                    SPUtils.saveBoolean(SiginMakeActivity.this, "firstsignmake", false);
                }
            }
        }, 700);
        initRecycleView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EmptyUtils.isTokenEmpty(SiginMakeActivity.this)) {
            todayFinish();
            getlist();
        }
    }

    private void initText() {
        String str1 = "每日签到成功可获得<font color='#FA2E41'>" + "1-8元奖励" + "</font>" + "，如若忘记可在第二天进行补签，补签日期需从第一个未签到的红包开始，当日可正常签到；";
        tv1.setText(Html.fromHtml(str1));
        String str2 = "连续签到天数越多，奖励越丰厚，每5天派发一次大额现金红包；签满15次至少可得<font color='#FA2E41'>" + "26元奖励" + "</font>" + "！";
        tv2.setText(Html.fromHtml(str2));
        String str3 = "签到获取的奖励，实际以金币形式发放至金币账户<font color='#FA2E41'>" + "（可提现）" + "</font>" + ";";
        tv3.setText(Html.fromHtml(str3));
        String str4 = "有任何疑问，请联系<font color='#FA2E41'>" + "官方客服QQ" + "</font>" + "或者进" + "<font color='#FA2E41'>" + "宝猪试玩QQ群155496279" + "</font>" + "交流;";
        tv4.setText(Html.fromHtml(str4));
        String str5 = "<font color='#FA2E41'>" + "同一账号连续签到必须同一手机" + "</font>" + ",否则" + "<font color='#FA2E41'>" + "有可能无法领取红包" + "</font>" + ";";
        tv5.setText(Html.fromHtml(str5));
        String str6 = "若未及时签到，可通过点击<font color='#FA2E41'>" + "【补签】" + "</font>" + "样式的红包进行获取补签卡；若未完成所有任务隔天需重新补签;";
        tv6.setText(Html.fromHtml(str6));
        HttpUtils.contactInformation().enqueue(new Observer<BaseResult<ServiceBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    ServiceBean bean = ((ServiceBean) response.data);
                    String str = "有任何疑问，请联系<font color='#FA2E41'>" + "官方客服QQ" + bean.serviceQq + "</font>" + "或者进" + "<font color='#FA2E41'>" + "宝猪试玩QQ群" + bean.qqGroup + "</font>" + "交流;";
                    tv4.setText(Html.fromHtml(str));
                }

            }
        });
    }

    private void initRecycleView() {
        recycle.setLayoutManager(new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false));
        adapter = new SignMakeAdapter(R.layout.item_sign_make, list, today);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (EmptyUtils.isTokenEmpty(SiginMakeActivity.this)) {
                    startActivity(new Intent(SiginMakeActivity.this, NewLoginActivity.class));
                } else {
                    SignMakeBean.SignBean signBean = list.get(position);

                    if (signBean.status == 1) {
                        //补签
                        if (bqCount == 0) {
                            noRetroactive();
                        } else {
                            toUseCard(signBean.id);
                        }

                    }
                    if (signBean.status == 2) {
                        goTask(signBean.id, signBean);

                    }
                    if (signBean.status == 3) {
                        getReward(signBean.id, signBean.reward);

                    }
                }
            }
        });
        recycle.setAdapter(adapter);
//        if (!EmptyUtils.isTokenEmpty(SiginMakeActivity.this)) {
//            getlist();
//        }
    }

    private void getReward(final int id, final int reward) {
        HttpUtils.receiveReward(id).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                showMoneyPop(reward);
            }
        });
    }

    private void toRetroactive(final int id) {
        showCarnWindow = new MyCommonPopupWindow(this, R.layout.pop_use_retroactive_card, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                TextView ok = view.findViewById(R.id.btn_ok);
                ImageView close = view.findViewById(R.id.im_back);
                TextView no = view.findViewById(R.id.btn_no);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCarnWindow.getPopupWindow().dismiss2();
                        toUseCard(id);
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCarnWindow.getPopupWindow().dismiss2();

                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCarnWindow.getPopupWindow().dismiss2();

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
        PopupWindow popupWindow = showCarnWindow.getPopupWindow();
        popupWindow.setAnimationStyle(R.style.animScale);
        if (instance != null) {
            showCarnWindow.getPopupWindow().showAtLocation(llBack, Gravity.CENTER, 0, 0);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.5f;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setAttributes(lp);
        }
    }

    private void toUseCard(int id) {
        HttpUtils.useCard(id).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                ToastUtils.show("使用一张补签卡");
                todayFinish();
                getlist();
            }
        });

    }

    private void goTask(final int id, final SignMakeBean.SignBean signBean) {
        if (!showTask) {
            return;
        }
        showTask = false;
        HttpUtils.signinGames(id).enqueue(new Observer<BaseResult<List<SignMakeGameBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    List<SignMakeGameBean> data = (List<SignMakeGameBean>) response.data;
                    showTask = true;
                    showTaskPop(id, signBean.gameCount, signBean.reward, data, signBean.signDay);

                }
            }

            @Override
            public void onFailed(String code, String msg) {
                super.onFailed(code, msg);
                showTask = true;
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                showTask = true;
            }
        });

    }

    private void noRetroactive() {
        showNoCarnWindow = new MyCommonPopupWindow(this, R.layout.pop_no_retroactive_card, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                TextView ok = view.findViewById(R.id.btn_ok);
                ImageView close = view.findViewById(R.id.im_back);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showNoCarnWindow.getPopupWindow().dismiss2();
                        startActivity(new Intent(SiginMakeActivity.this, RetroactiveActivity.class));
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showNoCarnWindow.getPopupWindow().dismiss2();

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
        PopupWindow popupWindow = showNoCarnWindow.getPopupWindow();
        popupWindow.setAnimationStyle(R.style.animScale);
        if (!instance.isFinishing()) {
            showNoCarnWindow.getPopupWindow().showAtLocation(llBack, Gravity.CENTER, 0, 0);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.5f;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setAttributes(lp);
        }
    }

    private void showMoneyPop(final int reward) {
        moneyWindow = new MyCommonPopupWindow(this, R.layout.pop_get_money, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                Button ok = view.findViewById(R.id.btn_ok);
                TextView tvMoney = view.findViewById(R.id.tv_money);
                tvMoney.setText(reward + "");
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        todayFinish();
                        getlist();
                        moneyWindow.getPopupWindow().dismiss2();
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
        PopupWindow popupWindow = moneyWindow.getPopupWindow();
        popupWindow.setAnimationStyle(R.style.animScale);
        if (!instance.isFinishing()) {
            moneyWindow.getPopupWindow().showAtLocation(llParent, Gravity.CENTER, 0, 0);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.5f;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setAttributes(lp);
        }
    }

    private void showTaskPop(final int signid, final int gameCount, final int reward, final List<SignMakeGameBean> data, final int signDay) {
        if (showTaskWindow != null && showTaskWindow.getPopupWindow().isShowing()) {
            return;
        }
        if (this.isFinishing()) {
            return;
        }
        showTaskWindow = new MyCommonPopupWindow(this, R.layout.pop_task, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                TextView tvAskFor;
                tvAskFor = view.findViewById(R.id.tv_ask_for);
                ImageView close = view.findViewById(R.id.iv_close);
                tvGameAcout = view.findViewById(R.id.tv_game_acout);
                llGame1 = view.findViewById(R.id.ll_game1);
                ivGamePic1 = view.findViewById(R.id.iv_game_pic1);
                ivDdddiscout = view.findViewById(R.id.iv_discout);
                tvTaskState1 = view.findViewById(R.id.tv_task_state1);
                llGame2 = view.findViewById(R.id.ll_game2);
                ivGamePic2 = view.findViewById(R.id.iv_game_pic2);
                tvTaskState2 = view.findViewById(R.id.tv_task_state2);
                llGame3 = view.findViewById(R.id.ll_game3);
                ivGamePic3 = view.findViewById(R.id.iv_game_pic3);
                tvTaskState3 = view.findViewById(R.id.tv_task_state3);
                iv1 = view.findViewById(R.id.iv_1);
                iv2 = view.findViewById(R.id.iv_2);
                iv3 = view.findViewById(R.id.iv_3);
                gamelayout = view.findViewById(R.id.gamelayout);
                ivGamePic = view.findViewById(R.id.tv_1);
                tvGameName = view.findViewById(R.id.tv_game_name);
                tvTryNumber = view.findViewById(R.id.tv_try_number);
                tvTryLastdata = view.findViewById(R.id.tv_try_lastdata);
                tvGameReward = view.findViewById(R.id.tv_game_reward);
                piglayout = view.findViewById(R.id.piglayout);
                llChangeGame = view.findViewById(R.id.ll_change_game);
                btBegin = view.findViewById(R.id.bt_begin);
                tvAddMoney = view.findViewById(R.id.tv_add_money);
                tvAddMoney.setText("额外+" + reward + "元");
                if (signDay % 5 == 0) {
                    tvGameAcout.setText("当日完成" + (gameCount + 1) + "个任务解锁签到，秒领取!");
                } else {
                    tvGameAcout.setText("当日完成" + gameCount + "个任务解锁签到，秒领取!");
                }

                for (int i = 0; i < data.size(); i++) {
                    SignMakeGameBean signMakeGameBean = data.get(i);
                    if (i == 0) {
                        if (signMakeGameBean.taskStatus == 1) {
                            tvTaskState1.setText("未完成");
                            count = 1;
                            game2 = 0;
                            gameid = signMakeGameBean.id;
                            gametype = signMakeGameBean.interfaceName;

                            iv1.setVisibility(View.VISIBLE);
                        } else {
                            iv1.setVisibility(View.INVISIBLE);
                            tvTaskState1.setText("已完成");
                        }

                        Glide.with(SiginMakeActivity.this).load(signMakeGameBean.icon).into(ivGamePic1);
                        Glide.with(SiginMakeActivity.this).load(signMakeGameBean.icon).into(ivGamePic);
                        tvGameName.setText(signMakeGameBean.gameTitle);
                        Random rand = new Random();
                        int randm = rand.nextInt(1500) + 501;
                        tvTryNumber.setText(randm + "人在玩");
                        try {
                            String residueDays = TimeUtils.formatDuringDays(Long.parseLong(signMakeGameBean.enddate));
                            tvTryLastdata.setText("还剩" + residueDays + "天");
                        }catch (Exception e){
                            tvTryLastdata.setText("还剩" + signMakeGameBean.enddate + "天");
                        }
                        tvGameReward.setText("+" + signMakeGameBean.gameGold + "元");
                    }
                    if (i == 1) {
                        if (signMakeGameBean.taskStatus == 1) {
                            tvTaskState2.setText("未完成");
                            count = 2;
                            gameid = signMakeGameBean.id;
                            gametype = signMakeGameBean.interfaceName;
                            iv2.setVisibility(View.VISIBLE);
                        } else {
                            if (signDay % 5 == 0) {
                                game2 = 1;
                                iv2.setVisibility(View.INVISIBLE);
                                tvTaskState2.setText("已完成");
                            } else {
                                iv2.setVisibility(View.INVISIBLE);
                                tvTaskState2.setText("已完成");
                            }
                        }

                        Glide.with(SiginMakeActivity.this).load(signMakeGameBean.icon).into(ivGamePic2);
                        Glide.with(SiginMakeActivity.this).load(signMakeGameBean.icon).into(ivGamePic);
                        tvGameName.setText(signMakeGameBean.gameTitle);
                        Random rand = new Random();
                        int randm = rand.nextInt(1500) + 501;
                        tvTryNumber.setText(randm + "人在玩");
                        try {
                            String residueDays = TimeUtils.formatDuringDays(Long.parseLong(signMakeGameBean.enddate));
                            tvTryLastdata.setText("还剩" + residueDays + "天");
                        }catch (Exception e){
                            tvTryLastdata.setText("还剩" + signMakeGameBean.enddate + "天");
                        }
                        tvGameReward.setText("+" + signMakeGameBean.gameGold + "元");
                    }
                    if (i == 2) {
                        if (signMakeGameBean.taskStatus == 1) {
                            tvTaskState3.setText("未完成");
                            count = 3;

                            gameid = signMakeGameBean.id;
                            gametype = signMakeGameBean.interfaceName;
                            iv3.setVisibility(View.VISIBLE);
                        } else {
                            iv3.setVisibility(View.INVISIBLE);
                            tvTaskState2.setText("已完成");
                        }

                        Glide.with(SiginMakeActivity.this).load(signMakeGameBean.icon).into(ivGamePic3);
                        Glide.with(SiginMakeActivity.this).load(signMakeGameBean.icon).into(ivGamePic);
                        tvGameName.setText(signMakeGameBean.gameTitle);
                        Random rand = new Random();
                        int randm = rand.nextInt(1500) + 501;
                        tvTryNumber.setText(randm + "人在玩");
                        try {
                            String residueDays = TimeUtils.formatDuringDays(Long.parseLong(signMakeGameBean.enddate));
                            tvTryLastdata.setText("还剩" + residueDays + "天");
                        }catch (Exception e){
                            tvTryLastdata.setText("还剩" + signMakeGameBean.enddate + "天");
                        }
                        tvGameReward.setText("+" + signMakeGameBean.gameGold + "元");
                    }
                }
                if (addDiscount == 1) {
                    ivDdddiscout.setVisibility(View.GONE);
                } else {
                    ivDdddiscout.setVisibility(View.VISIBLE);
                }

                if (gameCount == 1) {
                    llGame1.setVisibility(View.VISIBLE);
                    llGame2.setVisibility(View.INVISIBLE);
                    llGame3.setVisibility(View.INVISIBLE);
                } else if (gameCount == 2) {
                    if (signDay % 5 == 0) {
                        llGame1.setVisibility(View.VISIBLE);
                        llGame2.setVisibility(View.VISIBLE);
                        llGame3.setVisibility(View.VISIBLE);
                    } else {
                        llGame1.setVisibility(View.VISIBLE);
                        llGame2.setVisibility(View.VISIBLE);
                        llGame3.setVisibility(View.INVISIBLE);
                    }

                } else {
                    llGame1.setVisibility(View.VISIBLE);
                    llGame2.setVisibility(View.VISIBLE);
                    llGame3.setVisibility(View.VISIBLE);
                }
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTaskWindow.getPopupWindow().dismiss2();

                    }
                });
                btBegin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTaskWindow.getPopupWindow().dismiss2();
                        if (signDay % 5 == 0 && game2 == 1) {
                         } else {
                            if ("PCDD".equals(gametype)) {
                                Intent intent = new Intent(SiginMakeActivity.this, SignMakePcddGameDetailActivity.class);
                                intent.putExtra("gameid", gameid);
                                intent.putExtra("signid", signid);
                                startActivity(intent);
                            }
                            if ("MY".equals(gametype) || "bz-Android".equals(gametype)) {
                                Intent intent = new Intent(SiginMakeActivity.this, SignMakeMYGameDetailActivity.class);
                                intent.putExtra("gameid", gameid);
                                intent.putExtra("signid", signid);
                                startActivity(intent);
                            }
                            if ("xw-Android".equals(gametype)) {
                                Intent intent = new Intent(SiginMakeActivity.this, SignMakeXWGameDetailActivity.class);
                                intent.putExtra("gameid", gameid);
                                intent.putExtra("signid", signid);
                                startActivity(intent);
                            }
                        }

                    }
                });
                llChangeGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeGame();
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
        PopupWindow popupWindow = showTaskWindow.getPopupWindow();
        popupWindow.setAnimationStyle(R.style.animTranslate);
        if (instance != null) {
            showTaskWindow.getPopupWindow().showAtLocation(llBack, Gravity.BOTTOM, 0, 0);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.5f;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setAttributes(lp);

        }
    }

    private void changeGame() {
        HttpUtils.recommendGameNew().enqueue(new Observer<BaseResult<SignMakeChangeBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    SignMakeChangeBean data = (SignMakeChangeBean) response.data;

                    gameid = data.id;
                    gametype = data.interfaceName;
                    if (instance.isFinishing()) {
                        return;
                    }

                    if (count == 1) {

                        Glide.with(SiginMakeActivity.this).load(data.icon).into(ivGamePic1);
                        Glide.with(SiginMakeActivity.this).load(data.icon).into(ivGamePic);
                        tvGameName.setText(data.gameTitle);
                        Random rand = new Random();
                        int randm = rand.nextInt(1500) + 501;
                        tvTryNumber.setText(randm + "人在玩");
                        String residueDays = TimeUtils.formatDuringDays(data.enddate);
                        tvTryLastdata.setText("还剩" + residueDays + "天");
                        tvGameReward.setText("+" + data.gameGold + "元");
                    }
                    if (count == 2) {

                        Glide.with(SiginMakeActivity.this).load(data.icon).into(ivGamePic2);
                        Glide.with(SiginMakeActivity.this).load(data.icon).into(ivGamePic);
                        tvGameName.setText(data.gameTitle);
                        Random rand = new Random();
                        int randm = rand.nextInt(1500) + 501;
                        tvTryNumber.setText(randm + "人在玩");
                        String residueDays = TimeUtils.formatDuringDays(data.enddate);
                        tvTryLastdata.setText("还剩" + residueDays + "天");
                        tvGameReward.setText("+" + data.gameGold + "元");
                    }
                    if (count == 3) {

                        Glide.with(SiginMakeActivity.this).load(data.icon).into(ivGamePic3);
                        Glide.with(SiginMakeActivity.this).load(data.icon).into(ivGamePic);
                        tvGameName.setText(data.gameTitle);
                        Random rand = new Random();
                        int randm = rand.nextInt(1500) + 501;
                        tvTryNumber.setText(randm + "人在玩");
                        String residueDays = TimeUtils.formatDuringDays(data.enddate);
                        tvTryLastdata.setText("还剩" + residueDays + "天");
                        tvGameReward.setText("+" + data.gameGold + "元");
                    }
                }

            }
        });
    }

    private void getlist() {
        HttpUtils.signmake().enqueue(new Observer<BaseResult<SignMakeBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    list.clear();
                    SignMakeBean data = (SignMakeBean) response.data;
                    int day = data.day;
                    if (EmptyUtils.isNotEmpty(data.list)) {
                        list.addAll(data.list);
                        adapter.setDays(day);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void todayFinish() {
        HttpUtils.todayFinish().enqueue(new Observer<BaseResult<SignMakeEarnMoneyBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    SignMakeEarnMoneyBean data = (SignMakeEarnMoneyBean) response.data;
                    bqCount = data.bqCount;
                    coinTotal = data.coinTotal;
                    taskCount = data.taskCount;
                    addDiscount = data.addDiscount;
                    tvRetroactiveNumber.setText(bqCount + "张");
                    tvOvertaskNumber.setText(taskCount + "个");
                    tvTodaymoney.setText(coinTotal + "");

                } else {
                    tvRetroactiveNumber.setText("0张");
                    tvOvertaskNumber.setText("0个");
                    tvTodaymoney.setText("0");
                }
            }
        });
    }

    private void initClick() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        llRetroactivecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SiginMakeActivity.this, RetroactiveActivity.class));
            }
        });
    }

    private void getMarqueeviewMessage() {
        HttpUtils.qdzList().enqueue(new Observer<BaseResult<List<MarqueeviewMessageBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                List<MarqueeviewMessageBean> data = (List<MarqueeviewMessageBean>) response.data;
                if (EmptyUtils.isNotEmpty(data)) {
                    for (int i = 0; i < data.size(); i++) {
                        String mobile = data.get(i).mobile;
                        String content = data.get(i).content;
                        msgList.add(mobile + content);
                    }
                    marqueeview.startWithList(msgList);
                }
            }
        });
    }

    private void showPop() {
        showsignWindow = new MyCommonPopupWindow(this, R.layout.pop_show_sign, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                Button ok = view.findViewById(R.id.btn_ok);
                ImageView close = view.findViewById(R.id.im_back);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showsignWindow.getPopupWindow().dismiss2();

                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showsignWindow.getPopupWindow().dismiss2();

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
        PopupWindow popupWindow = showsignWindow.getPopupWindow();
        popupWindow.setAnimationStyle(R.style.animScale);
        if (this.isFinishing()) {
            return;
        }
        showsignWindow.getPopupWindow().showAtLocation(llParent, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);

    }
}
