package com.micang.baozhu.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.ChangeBean;
import com.micang.baozhu.http.bean.user.UserWithdrawTaskinfoBean;
import com.micang.baozhu.http.bean.user.WithDrawTaskGameBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.home.NewVipActivity;
import com.micang.baozhu.module.view.NewCommonDialog;
import com.micang.baozhu.module.web.VipTaskMYGameDetailActivity;
import com.micang.baozhu.module.web.VipTaskPcddGameDetailActivity;
import com.micang.baozhu.module.web.VipTaskXWGameDetailActivity;
import com.micang.baozhu.module.web.WelfareActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.OnMultiClickListener;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.DensityUtil;
import com.micang.baselibrary.util.TimeUtils;
import com.micang.baselibrary.view.MyCommonPopupWindow;
import com.micang.baselibrary.view.NewPopWindow;

import java.util.List;

public class WithDrawTaskActivity extends BaseActivity {
    private RelativeLayout head;
    private LinearLayout llBack;
    private TextView tvTitle;
    private LinearLayout llGame1;
    private ImageView ivGamePic1;
    private TextView tvTaskState1;
    private LinearLayout llGame2;
    private ImageView ivGamePic2;
    private TextView tvTaskState2;
    private LinearLayout llGame3;
    private ImageView ivGamePic3;
    private TextView tvTaskState3;
    private Button btWithdraw;
    private MyCommonPopupWindow gamePop;
    private ImageView ivGamePic;
    private TextView tvGameName;
    private TextView tvEndtime;
    private TextView tvGameMoney;
    private int cashId;
    private String interfaceName = "";
    private int gameId;
    boolean firstClick = false;
    boolean secondClick = false;
    boolean thirdClick = false;
    private int state = 0;
    private NewCommonDialog noticeDialog;
    private UserWithdrawTaskinfoBean.GamesBean gamesBean;

    @Override
    public int layoutId() {
        return R.layout.activity_with_draw_task;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
        head = findViewById(R.id.head);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("解锁任务");

        llGame1 = findViewById(R.id.ll_game1);
        ivGamePic1 = findViewById(R.id.iv_game_pic1);
        tvTaskState1 = findViewById(R.id.tv_task_state1);
        llGame2 = findViewById(R.id.ll_game2);
        ivGamePic2 = findViewById(R.id.iv_game_pic2);
        tvTaskState2 = findViewById(R.id.tv_task_state2);
        llGame3 = findViewById(R.id.ll_game3);
        ivGamePic3 = findViewById(R.id.iv_game_pic3);
        tvTaskState3 = findViewById(R.id.tv_task_state3);
        btWithdraw = findViewById(R.id.bt_withdraw);
        initClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userCashtaskInfo();
    }

    private void recommendGameCash() {
        HttpUtils.recommendGameCash(cashId).enqueue(new Observer<BaseResult<WithDrawTaskGameBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    WithDrawTaskGameBean data = (WithDrawTaskGameBean) response.data;
                    if (EmptyUtils.isNotEmpty(data.tpGame)) {
                        WithDrawTaskGameBean.TpGameBean tpGame = data.tpGame;
                        interfaceName = tpGame.interfaceName;
                        gameId = tpGame.id;
                        if (WithDrawTaskActivity.this.isFinishing()) {
                            return;
                        }
                        if (EmptyUtils.isNotEmpty(gamePop) && gamePop.getPopupWindow().isShowing()) {
                            Glide.with(WithDrawTaskActivity.this).load(tpGame.icon).into(ivGamePic);
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
            }
        });
    }

    private void initClick() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        llGame1.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                if (firstClick) {
                    if (EmptyUtils.isNotEmpty(gamesBean)) {
                        gamePop(gamesBean.icon, gamesBean.gameGold, gamesBean.gameTitle, gamesBean.enddate);
                    } else {
                        recommendGameCash();
                    }
                }
            }
        });
        llGame2.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                if (secondClick) {
                    if (EmptyUtils.isNotEmpty(gamesBean)) {
                        gamePop(gamesBean.icon, gamesBean.gameGold, gamesBean.gameTitle, gamesBean.enddate);
                    } else {
                        recommendGameCash();
                    }
                }
            }
        });
        llGame3.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                if (thirdClick) {
                    if (EmptyUtils.isNotEmpty(gamesBean)) {
                        gamePop(gamesBean.icon, gamesBean.gameGold, gamesBean.gameTitle, gamesBean.enddate);
                    } else {
                        recommendGameCash();
                    }
                }
            }
        });
        btWithdraw.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                switch (state) {
                    case 0:
                    case 1:
                        if (EmptyUtils.isNotEmpty(gamesBean)) {
                            gamePop(gamesBean.icon, gamesBean.gameGold, gamesBean.gameTitle, gamesBean.enddate);
                        } else {
                            recommendGameCash();
                        }
                        break;
                    case 2:
                        getCash();
                        break;
                    case 3:
                        ToastUtils.show("提现审核中");
                        break;
                }
            }
        });
    }

    private void getCash() {
        HttpUtils.cashLaunch(cashId).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                showNotice();
            }
        });

    }

    private void userCashtaskInfo() {
        HttpUtils.userCashtaskInfo().enqueue(new Observer<BaseResult<UserWithdrawTaskinfoBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    if (WithDrawTaskActivity.this.isFinishing()) {
                        return;
                    }
                    UserWithdrawTaskinfoBean data = (UserWithdrawTaskinfoBean) response.data;
                    cashId = data.lUserCash.id;
                    if (EmptyUtils.isNotEmpty(data.lUserCash)) {
                        UserWithdrawTaskinfoBean.LUserCashBean lUserCash = data.lUserCash;
                        state = lUserCash.state;
                        if (state == 1) {
                            btWithdraw.setText("解锁任务,秒提现");
                        }
                        if (state == 2) {
                            btWithdraw.setText("立即领取");
                        }
                        if (state == 3) {
                            btWithdraw.setText("审核中");
                        }
                    }
                    if (data.res == 1) {
                        List<UserWithdrawTaskinfoBean.GamesBean> games = data.games;
                        for (int i = 0; i < games.size(); i++) {
                            gamesBean = games.get(i);
                            interfaceName = gamesBean.interfaceName;
                            gameId = gamesBean.id;
                            if (i == 0) {
                                Glide.with(WithDrawTaskActivity.this).load(gamesBean.icon).into(ivGamePic1);
                                if (gamesBean.cashStatus == 1) {
                                    tvTaskState1.setText("未完成");
                                    firstClick = true;
                                    secondClick = false;
                                    thirdClick = false;
                                }
                                if (gamesBean.cashStatus == 2) {
                                    tvTaskState1.setText("已完成");
                                    firstClick = false;
                                    secondClick = true;
                                    thirdClick = false;
                                    gamesBean = null;
                                }
                            }
                            if (i == 1) {
                                Glide.with(WithDrawTaskActivity.this).load(gamesBean.icon).into(ivGamePic2);
                                if (gamesBean.cashStatus == 1) {
                                    tvTaskState2.setText("未完成");
                                    firstClick = false;
                                    secondClick = true;
                                    thirdClick = false;
                                }
                                if (gamesBean.cashStatus == 2) {
                                    tvTaskState2.setText("已完成");
                                    firstClick = false;
                                    secondClick = false;
                                    thirdClick = true;
                                    gamesBean = null;
                                }
                            }
                            if (i == 2) {
                                Glide.with(WithDrawTaskActivity.this).load(gamesBean.icon).into(ivGamePic3);
                                if (gamesBean.cashStatus == 1) {
                                    tvTaskState3.setText("未完成");
                                    firstClick = false;
                                    secondClick = false;
                                    thirdClick = true;
                                }
                                if (gamesBean.cashStatus == 2) {
                                    tvTaskState3.setText("已完成");
                                    firstClick = false;
                                    secondClick = false;
                                    thirdClick = false;
                                }
                            }
                        }
                    } else {
                        firstClick = true;
                        secondClick = false;
                        thirdClick = false;
                    }
                }
            }
        });
    }

    private void gamePop(final String icon, final double gameGold, final String gameTitle, final long enddate) {
        if (WithDrawTaskActivity.this.isFinishing()) {
            return;
        }
        gamePop = new MyCommonPopupWindow(this, R.layout.pop_home_botoom_game, ViewGroup.LayoutParams.MATCH_PARENT,
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
                Glide.with(WithDrawTaskActivity.this).load(R.drawable.gif_home_bottom).apply(options).into(ivWelfare);
                Glide.with(WithDrawTaskActivity.this).load(icon).into(ivGamePic);
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
                            startActivity(new Intent(WithDrawTaskActivity.this, WelfareActivity.class));
                        }
                    }
                });
                llChangeGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recommendGameCash();
                    }
                });
                btnSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPopupWindow().dismiss2();
                        if ("PCDD".equals(interfaceName)) {
                            Intent intent = new Intent(WithDrawTaskActivity.this, VipTaskPcddGameDetailActivity.class);
                            intent.putExtra("type", "2");
                            intent.putExtra("gameid", gameId);
                            intent.putExtra("vipId", cashId);
                            startActivity(intent);
                        }
                        if ("MY".equals(interfaceName) || "bz-Android".equals(interfaceName)) {
                            Intent intent = new Intent(WithDrawTaskActivity.this, VipTaskMYGameDetailActivity.class);
                            intent.putExtra("type", "2");
                            intent.putExtra("gameid", gameId);
                            intent.putExtra("vipId", cashId);
                            startActivity(intent);
                        }
                        if ("xw-Android".equals(interfaceName)) {
                            Intent intent = new Intent(WithDrawTaskActivity.this, VipTaskXWGameDetailActivity.class);
                            intent.putExtra("type", "2");
                            intent.putExtra("gameid", gameId);
                            intent.putExtra("vipId", cashId);
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
        gamePop.getPopupWindow().showAtLocation(head, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    private void showNotice() {
        if (this.isFinishing()) {
            return;
        }
        noticeDialog = new NewCommonDialog(this, false, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_show_begin_withdraw, null);
        noticeDialog.setContentView(contentView);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (noticeDialog != null && noticeDialog.isShowing()) {
                    noticeDialog.dismiss();
                    startActivity(new Intent(WithDrawTaskActivity.this, WelfareActivity.class));
                    finish();
                }
            }
        }.start();
        AppContext.fuLiHuiisfirst = false;
        noticeDialog.show();
    }
}
