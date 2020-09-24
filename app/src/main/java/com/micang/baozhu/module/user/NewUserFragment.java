package com.micang.baozhu.module.user;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.ChangeBean;
import com.micang.baozhu.http.bean.VipBean;
import com.micang.baozhu.http.bean.user.HaveReadBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.home.EveryDayTaskActivity;
 import com.micang.baozhu.module.home.MainActivity;
import com.micang.baozhu.module.home.NewGoingActivity;
import com.micang.baozhu.module.home.NewVipActivity;
 import com.micang.baozhu.module.login.NewLoginActivity;
 import com.micang.baozhu.module.web.AccountDataActivity;
import com.micang.baozhu.module.web.GeneralizeActivity;
import com.micang.baozhu.module.web.GrowthValueActivity;
import com.micang.baozhu.module.web.HelpCenterActivity;
import com.micang.baozhu.module.web.MessageCenterActivity;
import com.micang.baozhu.module.web.NextMYGameDetailsActivity;
import com.micang.baozhu.module.web.NextPCddGameDetailActivity;
import com.micang.baozhu.module.web.NextXWGameDetailActivity;
import com.micang.baozhu.module.web.TaskActivity;
import com.micang.baozhu.module.web.WelfareActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.OnMultiClickListener;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseLazyFragment;
import com.micang.baselibrary.util.DensityUtil;
import com.micang.baselibrary.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class NewUserFragment extends BaseLazyFragment implements View.OnClickListener {

    private NestedScrollView srcrollview;
    private ImageView imSetting;
    private ImageView ivMessage;
    private ImageView imUserPic;
    private TextView imUserPhone;
    private ImageView ivVipType;
    private ImageView ivVipType1;
    private ImageView ivVipType2;
    private LinearLayout linearLayout;
    private TextView tvGrade;
    private TextView tvUserid;
    private ImageView ivEncourage;
    private ConstraintLayout imageView;
    private TextView tvMoney;
    private TextView tvCoin;
    private TextView tvWithdraw;
    private RelativeLayout rlAccount;
    private RelativeLayout rlInputCode;
    private RelativeLayout rlTryplay;
    private RelativeLayout rlCard;
    private LinearLayout llChangeGame;
    private ImageView ivGamePic;
    private TextView tvGameName;
    private TextView tvGameReward;
    private Button btPlay;
    private Button btInvite;
    private Button btGetEverydayReward;
    private Button btWelfare;
    private TextView tvGrowth;
    private TextView tvGrowthValue;
    private Button btGameTask;
    private Button btGoldPig;
    private RelativeLayout rlHelp;
    private RelativeLayout rlFeedback;
    private RelativeLayout rlService;
    private RelativeLayout rlAboutUs;

    private boolean hidden1;
    private boolean isMoreVip = false;
    private PopupWindow vipPopupWindow;
    private List<VipBean> listPop = new ArrayList<>();
    private String mobile;
    private ChangeBean gameBean;
    private String interfaceName;
    private String gameId;
    private String referrerCode;

    @Override
    protected int layoutId() {
        return R.layout.fragment_user_new;
    }

    @Override
    protected void init(View rootView) {

        srcrollview = rootView.findViewById(R.id.srcrollview);
        imSetting = rootView.findViewById(R.id.im_setting);
        ivMessage = rootView.findViewById(R.id.iv_message);
        imUserPic = rootView.findViewById(R.id.im_user_pic);
        imUserPhone = rootView.findViewById(R.id.im_user_phone);
        ivVipType = rootView.findViewById(R.id.iv_vip_type);
        ivVipType1 = rootView.findViewById(R.id.iv_vip_type1);
        ivVipType2 = rootView.findViewById(R.id.iv_vip_type2);
        linearLayout = rootView.findViewById(R.id.linearLayout);
        tvGrade = rootView.findViewById(R.id.tv_grade);
        tvUserid = rootView.findViewById(R.id.tv_userid);
        ivEncourage = rootView.findViewById(R.id.iv_encourage);
        imageView = rootView.findViewById(R.id.imageView);
        tvMoney = rootView.findViewById(R.id.tv_money);
        tvCoin = rootView.findViewById(R.id.tv_Coin);
        tvWithdraw = rootView.findViewById(R.id.tv_withdraw);

        rlAccount = rootView.findViewById(R.id.rl_account);
        rlInputCode = rootView.findViewById(R.id.rl_input_code);
        rlTryplay = rootView.findViewById(R.id.rl_tryplay);
        rlCard = rootView.findViewById(R.id.rl_card);

        llChangeGame = rootView.findViewById(R.id.ll_change_game);
        ivGamePic = rootView.findViewById(R.id.iv_game_pic);
        tvGameName = rootView.findViewById(R.id.tv_game_name);
        tvGameReward = rootView.findViewById(R.id.tv_game_reward);
        btPlay = rootView.findViewById(R.id.bt_play);

        btInvite = rootView.findViewById(R.id.bt_invite);
        btGetEverydayReward = rootView.findViewById(R.id.bt_get_everyday_reward);
        btWelfare = rootView.findViewById(R.id.bt_welfare);
        tvGrowth = rootView.findViewById(R.id.tv_growth);
        tvGrowthValue = rootView.findViewById(R.id.tv_growth_value);
        btGameTask = rootView.findViewById(R.id.bt_game_task);
        btGoldPig = rootView.findViewById(R.id.bt_gold_pig);
        rlHelp = rootView.findViewById(R.id.rl_help);
        rlFeedback = rootView.findViewById(R.id.rl_feedback);
        rlService = rootView.findViewById(R.id.rl_service);
        rlAboutUs = rootView.findViewById(R.id.rl_about_us);

        initClick();
        String str1 = "去首页【拱一拱】，提升<font color='#FF2B49'>" + "成长值" + "</font>";
        tvGrowthValue.setText(Html.fromHtml(str1));
    }

    private void initClick() {
        imSetting.setOnClickListener(this);
        imUserPic.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        ivEncourage.setOnClickListener(this);
        ivVipType2.setOnClickListener(this);
        tvWithdraw.setOnClickListener(this);
        rlAccount.setOnClickListener(this);
        tvCoin.setOnClickListener(this);
        rlInputCode.setOnClickListener(this);
        rlTryplay.setOnClickListener(this);
        rlCard.setOnClickListener(this);
        llChangeGame.setOnClickListener(this);

        btInvite.setOnClickListener(this);
        btGetEverydayReward.setOnClickListener(this);
        btWelfare.setOnClickListener(this);
        tvGrowthValue.setOnClickListener(this);
        btGameTask.setOnClickListener(this);
        btGoldPig.setOnClickListener(this);
        rlHelp.setOnClickListener(this);
        rlFeedback.setOnClickListener(this);
        rlAboutUs.setOnClickListener(this);

        btPlay.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (EmptyUtils.isTokenEmpty(activity) || EmptyUtils.isImeiEmpty(activity)) {
                    startActivity(new Intent(activity, NewLoginActivity.class));
                } else {
                    getUrl();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden1) {
            getUserInfo();
            queryMyVips();
            queryMessage();
            changeGame();
            queryTodayFinish();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        hidden1 = isHidden();
        if (!hidden1) {
            getUserInfo();
            queryMyVips();
            queryMessage();
            changeGame();
            queryTodayFinish();
        }
    }

    private void queryTodayFinish() {
        HttpUtils.queryTodayFinish().enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    String data = (String) response.data;
                    tvGrowth.setText("至少完成5次试玩游戏任务(" + data + "/5)");
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
                UserBean data = (UserBean) response.data;
                String coin = data.coin;
                mobile = data.mobile;
                referrerCode = data.referrerCode;
                String accountId = data.accountId;
                tvUserid.setText(accountId);
                String level = data.level;
                setLevel(level);
                String profile = data.profile;//头像

                imUserPhone.setText(mobile);
                if (EmptyUtils.isNotEmpty(profile)) {
                    RequestOptions requestOptions = RequestOptions.circleCropTransform();
                    Glide.with(context).load(profile).placeholder(R.drawable.ic_user_pic).error(R.drawable.ic_user_pic).apply(requestOptions).into(imUserPic);
                } else {
                    RequestOptions requestOptions = RequestOptions.circleCropTransform();
                    Glide.with(context).load("").placeholder(R.drawable.ic_user_pic).error(R.drawable.ic_user_pic).apply(requestOptions).into(imUserPic);
                }
                tvCoin.setText(coin);
                double percent = Long.parseLong(coin) / 11000.0 * 100;
                double v = Math.floor(percent) / 100;
                tvMoney.setText(v + "元");
            }
        });
    }

    private void queryMessage() {
        if (EmptyUtils.isTokenEmpty(activity)) {
            return;
        }
        HttpUtils.haveUnRead().enqueue(new Observer<BaseResult<HaveReadBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    HaveReadBean data = (HaveReadBean) response.data;
                    if (data.count == 0) {
                        ivMessage.setBackgroundResource(R.drawable.icon_message);
                    } else {
                        ivMessage.setBackgroundResource(R.drawable.icon_message_have);
                    }
                }
            }
        });
    }

    private void queryMyVips() {
        String token = SPUtils.token(activity);
        String imei = SPUtils.imei(activity);
        if (EmptyUtils.isEmpty(token) || EmptyUtils.isEmpty(imei)) {
            return;
        }
        HttpUtils.queryMyVips().enqueue(new Observer<BaseResult<List<VipBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                List<VipBean> data = (List<VipBean>) response.data;
                if (EmptyUtils.isNotEmpty(data)) {

                    for (int i = 0; i < data.size(); i++) {
                        if (i == 0) {
                            String logo = data.get(i).logo;
                            if (EmptyUtils.isNotEmpty(logo)) {
                                Glide.with(context).load(logo).into(ivVipType);
                            }
                        }
                        if (i == 1) {
                            String logo = data.get(i).logo;
                            if (EmptyUtils.isNotEmpty(logo)) {
                                Glide.with(context).load(logo).into(ivVipType1);
                            }
                        }
                        if (i == 2) {
                            if (data.size() > 3) {
                                isMoreVip = true;
                                Glide.with(context).load(R.drawable.icon_user_morevip).into(ivVipType2);
                                listPop.clear();
                                listPop.addAll(data);
                            } else {
                                isMoreVip = false;
                                String logo = data.get(i).logo;
                                Glide.with(context).load(logo).into(ivVipType2);
                            }
                        }
                    }
                } else {

                    listPop.clear();
                    Glide.with(context).load("").into(ivVipType);
                    Glide.with(context).load("").into(ivVipType1);
                    Glide.with(context).load("").into(ivVipType2);
                }
            }
        });
    }

    @Override
    protected void initData() throws NullPointerException {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_setting:
                startActivity(new Intent(activity, SettingActivity.class));
                break;
            case R.id.im_user_pic:
                startActivity(new Intent(activity, UserDataActivity.class));
                break;
            case R.id.iv_message:
                startActivity(new Intent(activity, MessageCenterActivity.class));
                break;
            case R.id.iv_encourage:
                startActivity(new Intent(activity, NewVipActivity.class));
                break;
            case R.id.tv_withdraw:
                startActivity(new Intent(activity, NewWithdrawActivity.class));
                break;
            case R.id.iv_vip_type2:
                if (isMoreVip) {
                    initVipPopup();
                    if (vipPopupWindow != null && !vipPopupWindow.isShowing()) {
                        vipPopupWindow.showAsDropDown(ivVipType2, -DensityUtil.dip2px(activity, 72), -DensityUtil.dip2px(activity, 47));
                    }
                }
                break;
            case R.id.tv_Coin:
            case R.id.rl_account:
                startActivity(new Intent(activity, AccountDataActivity.class));
                break;
            case R.id.rl_input_code:
                Intent intent1 = new Intent(activity, InputCodeActivity.class);
                intent1.putExtra("referrerCode", referrerCode);
                startActivity(intent1);
                break;

            case R.id.rl_tryplay:
                Intent intent = new Intent(activity, NewGoingActivity.class);
                intent.putExtra("title", "我的试玩");
                startActivity(intent);
                break;
            case R.id.rl_card:
                startActivity(new Intent(activity, MyCardActivity.class));
                break;
            case R.id.ll_change_game:
                changeGame();
                break;
            case R.id.bt_invite:
                startActivity(new Intent(activity, GeneralizeActivity.class));
                break;
            case R.id.bt_get_everyday_reward:
                startActivity(new Intent(activity, EveryDayTaskActivity.class));
                break;
            case R.id.bt_welfare:
                String fuLiHui = AppContext.fuLiHui;
                if (EmptyUtils.isEmpty(fuLiHui)) {
                    ToastUtils.show("功能待开发");
                } else {
                    Intent intentwel = new Intent(activity, WelfareActivity.class);
                    intentwel.putExtra("url", fuLiHui);
                    startActivity(intentwel);
                }
                break;
            case R.id.tv_growth_value:
                startActivity(new Intent(activity, GrowthValueActivity.class));
                break;
            case R.id.bt_game_task:
//                startActivity(new Intent(activity, GameListActivity.class));
                ((MainActivity) getActivity()).selecteTab(0);
                break;
            case R.id.rl_help:
                startActivity(new Intent(activity, HelpCenterActivity.class));
                break;
            case R.id.rl_feedback:
                startActivity(new Intent(activity, NewFeedBackActivity.class));
                break;
            case R.id.rl_about_us:
                startActivity(new Intent(activity, AboutPigActivity.class));
                break;
            default:
                break;
        }
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
                    Glide.with(activity).load(gameBean.icon).into(ivGamePic);
                    tvGameName.setText(gameBean.gameTitle);
                    tvGameReward.setText("+" + gameBean.gameGold + "元");

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

    }

    /**
     * 设置成长等级
     *
     * @param level
     */
    private void setLevel(String level) {
        switch (level) {
            case "L0":
                tvGrade.setText("LV.0");
                break;
            case "L1":
                tvGrade.setText("LV.1");
                break;
            case "L2":
                tvGrade.setText("LV.2");
                break;
            case "L3":
                tvGrade.setText("LV.3");
                break;
            case "L4":
                tvGrade.setText("LV.4");
                break;
            case "L5":
                tvGrade.setText("LV.5");
                break;
            case "L6":
                tvGrade.setText("LV.6");
                break;
            case "L7":
                tvGrade.setText("LV.7");
                break;
            case "L8":
                tvGrade.setText("LV.8");
                break;
            case "L9":
                tvGrade.setText("LV.9");
                break;
            case "L10":
                tvGrade.setText("LV.10");
                break;
            case "L11":
                tvGrade.setText("LV.11");
                break;
            case "L12":
                tvGrade.setText("LV.12");
                break;
            default:
                break;
        }
    }

    private void initVipPopup() {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.pop_vip, null, false);
        ImageView viptye1 = inflate.findViewById(R.id.iv_vip_type1);
        ImageView viptye2 = inflate.findViewById(R.id.iv_vip_type2);
        ImageView viptye3 = inflate.findViewById(R.id.iv_vip_type3);
        ImageView viptye4 = inflate.findViewById(R.id.iv_vip_type4);
        ImageView viptye5 = inflate.findViewById(R.id.iv_vip_type5);
        for (int i = 0; i < listPop.size(); i++) {
            String logo = listPop.get(i).logo;
            if (i == 2) {
                Glide.with(context).load(logo).into(viptye1);
            }
            if (i == 3) {
                Glide.with(context).load(logo).into(viptye2);
            }
            if (i == 4) {
                Glide.with(context).load(logo).into(viptye3);
            }
            if (i == 5) {
                Glide.with(context).load(logo).into(viptye4);
            }
            if (i == 6) {
                Glide.with(context).load(logo).into(viptye5);
            }
        }
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vipPopupWindow.dismiss();
            }
        });

        vipPopupWindow = new PopupWindow(inflate, DensityUtil.dip2px(activity, 120), DensityUtil.dip2px(activity, 27));

        vipPopupWindow.setFocusable(true);
        vipPopupWindow.setOutsideTouchable(true);
        vipPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                vipPopupWindow.dismiss();
            }
        });
    }
}
