package com.micang.baozhu.module.earlyclock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.earlyclock.EarlyClockBean;
import com.micang.baozhu.http.bean.earlyclock.EarlyClockLuckBean;
import com.micang.baozhu.http.bean.earlyclock.EarlyClockcardBean;
import com.micang.baozhu.http.bean.earlyclock.EarlyClockinfo;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.earlyclock.adapter.EarlyClockLuckListAdapter;
import com.micang.baozhu.module.task.NewTaskActivity;
import com.micang.baozhu.module.view.NewCommonDialog;
import com.micang.baozhu.module.view.NoScrollRecyclerView;
import com.micang.baozhu.module.view.ObserverScrollView;
import com.micang.baozhu.module.web.EarlyClockMyRecodActivity;
import com.micang.baozhu.module.web.EarlyClockRulesActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.NumberUtils;
import com.micang.baozhu.util.OnMultiClickListener;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.util.WindowUtils;

import java.util.ArrayList;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

/**
 * 早起打卡
 */
public class EarlyClockActivity extends BaseActivity implements View.OnClickListener, ObserverScrollView.OnScrollChangedListener {
    private LinearLayout llBack;
    private View view;
    private ImageView ivBack;
    private TextView tvTitle;
    private RelativeLayout rlLayout;
    private LinearLayout llEarlyClocklayout;
    private ObserverScrollView scrollView;
    private TextView tvTips;
    private TextView tvJoincoin;
    private TextView tvJoinpeople;
    private RelativeLayout rlRecordLog;
    private RelativeLayout rlRecordRules;
    private RelativeLayout rlRecordRetroactive;
    private RelativeLayout rlRecordInvite;
    private TextView tvResult;
    private TextView tvJoin;
    private LinearLayout llCountdown;
    private CountdownView countdownView;
    private TextView tvGetSum;
    private TextView tvWinPeople;
    private TextView tvLosePeople;
    private NoScrollRecyclerView recycleview;

    private int state = 0;
    private NewCommonDialog clockNoticeDialog;
    private NewCommonDialog noMoneyDialog;
    private NewCommonDialog joinDialog;
    private NewCommonDialog noCardeDialog;
    private NewCommonDialog clockDialog;
    private NewCommonDialog showCardeRulesDialog;
    private NewCommonDialog showCardeDialog;
    private List<EarlyClockLuckBean> list = new ArrayList<>();
    private EarlyClockLuckListAdapter adapter;

    @Override
    public int layoutId() {
        return R.layout.activity_early_clock;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        WindowUtils.setPicTranslucentToStatus(this);
        view = findViewById(R.id.view);
        llBack = findViewById(R.id.ll_back);
        scrollView = findViewById(R.id.scrollView);
        countdownView = findViewById(R.id.countdown);
        llEarlyClocklayout = findViewById(R.id.ll_early_clock_layout);
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        rlLayout = findViewById(R.id.rl_layout);
        tvTips = findViewById(R.id.tv_tips);
        tvJoincoin = findViewById(R.id.tv_joincoin);
        tvJoinpeople = findViewById(R.id.tv_joinpeople);
        rlRecordLog = findViewById(R.id.rl_record_log);
        rlRecordRules = findViewById(R.id.rl_record_rules);
        rlRecordRetroactive = findViewById(R.id.rl_record_retroactive);
        rlRecordInvite = findViewById(R.id.rl_record_invite);
        tvJoin = findViewById(R.id.tv_join);
        llCountdown = findViewById(R.id.ll_countdown);
        tvResult = findViewById(R.id.tv_result);
        tvGetSum = findViewById(R.id.tv_get_sum);
        tvWinPeople = findViewById(R.id.tv_win_people);
        tvLosePeople = findViewById(R.id.tv_lose_people);
        recycleview = findViewById(R.id.recycleview);
        initClick();
        setHight();
        setRed(1, "1w");
        scrollView.AddOnScrollChangedListener(this);
        initRecycle();
        getList();
    }


    private void initRecycle() {
        recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new EarlyClockLuckListAdapter(R.layout.item_earlyclock_lucklist, list);
        recycleview.setAdapter(adapter);
    }

    private void earlycheckin() {
        HttpUtils.earlycheckin().enqueue(new Observer<BaseResult<EarlyClockinfo>>() {

            private EarlyClockinfo.CheckinResultBean checkinResultBean;

            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    EarlyClockinfo data = (EarlyClockinfo) response.data;
                    setRed(2, data.number);
                    tvJoincoin.setText(NumberUtils.num2thousand(data.coin));
                    tvTips.setText(data.dates);
                    if (EmptyUtils.isNotEmpty(data.checkinResult)) {
                        checkinResultBean = data.checkinResult;
                        long bonusPool = checkinResultBean.bonusPool / 10000;
                        tvGetSum.setText(bonusPool + "万");
                        tvResult.setText(checkinResultBean.createDate + "打卡结果");
                        tvWinPeople.setText(checkinResultBean.successNumber);
                        tvLosePeople.setText(checkinResultBean.failNumber);
                    }
                    switch (data.checkinType) {
                        case 1:
                            //1.已报名,倒计时
                        case 3:
                            //3.已报名，当前时间不能打卡 已报名,倒计时
                            llEarlyClocklayout.setEnabled(false);
                            tvJoin.setVisibility(View.GONE);
                            llCountdown.setVisibility(View.VISIBLE);
                            llEarlyClocklayout.setBackgroundResource(R.drawable.bg_click_in_not);
                            showCountDown(data.countdown);
                            break;
                        case 2:
                            //2.可报名明天打卡
                        case 7:
                            //7.已报名，今日打卡失败,没弹框,可报名下次打卡
                            llEarlyClocklayout.setEnabled(true);
                            tvJoin.setVisibility(View.VISIBLE);
                            setRed(1, "1w");
                            llCountdown.setVisibility(View.GONE);
                            llEarlyClocklayout.setBackgroundResource(R.drawable.bg_click_in);
                            state = 1;
                            break;
                        case 4:
                            //4.已报名，当前时间可以打卡
                            llEarlyClocklayout.setEnabled(true);
                            tvJoin.setVisibility(View.VISIBLE);
                            tvJoin.setText("立即打卡");
                            llCountdown.setVisibility(View.GONE);
                            llEarlyClocklayout.setBackgroundResource(R.drawable.bg_click_in);
                            state = 2;
                            break;
                        case 5:
                            //5.已报名，当前时间可以补卡
                            llEarlyClocklayout.setEnabled(true);
                            tvJoin.setVisibility(View.VISIBLE);
                            tvJoin.setText("立即补签");
                            llCountdown.setVisibility(View.GONE);
                            llEarlyClocklayout.setBackgroundResource(R.drawable.bg_click_in);
                            state = 3;
                            break;
                        case 6:
                            //6.已报名，今日打卡失败,有弹框,可报名下次打卡
                            llEarlyClocklayout.setEnabled(true);
                            tvJoin.setVisibility(View.VISIBLE);
                            setRed(1, "1w");
                            llCountdown.setVisibility(View.GONE);
                            llEarlyClocklayout.setBackgroundResource(R.drawable.bg_click_in);
                            state = 1;
                            clockDialog(3, checkinResultBean.bonusPool);
                            break;
                        case 8:
                            //8.当前时间不能报名
                            llEarlyClocklayout.setEnabled(true);
                            tvJoin.setVisibility(View.VISIBLE);
                            setRed(1, "1w");
                            llCountdown.setVisibility(View.GONE);
                            llEarlyClocklayout.setBackgroundResource(R.drawable.bg_click_in);
                            state = 4;
                            break;
                    }

                }
            }
        });
    }

    private void getList() {
        HttpUtils.earlycheckinappList().enqueue(new Observer<BaseResult<List<EarlyClockLuckBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    List<EarlyClockLuckBean> data = (List<EarlyClockLuckBean>) response.data;
                    list.clear();
                    list.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        earlycheckin();
    }

    private void initClick() {
        llBack.setOnClickListener(this);
        rlRecordLog.setOnClickListener(this);
        rlRecordRules.setOnClickListener(this);
        rlRecordRetroactive.setOnClickListener(this);
        rlRecordInvite.setOnClickListener(this);
        llEarlyClocklayout.setEnabled(false);
        llEarlyClocklayout.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                toCheckin(state);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.rl_record_log:
                startActivity(new Intent(EarlyClockActivity.this, EarlyClockMyRecodActivity.class));
                break;
            case R.id.rl_record_rules:
                startActivity(new Intent(EarlyClockActivity.this, EarlyClockRulesActivity.class));
                break;
            case R.id.rl_record_retroactive:
                checkCardNum();
                break;
            case R.id.rl_record_invite:
                startActivity(new Intent(EarlyClockActivity.this, EarlyClockInviteActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 1,报名 2,立即打卡 3,补签  4,不能报名
     *
     * @param state
     */
    private void toCheckin(int state) {
        switch (state) {
            case 1:
                showJoin();
                break;
            case 2:
            case 3:
                clock();
                break;
            case 4:
                notJoinDialog();
                break;
        }
    }

    /**
     * 查看补签卡
     */
    private void checkCardNum() {
        rlRecordRetroactive.setEnabled(false);
        HttpUtils.earlycheckincard().enqueue(new Observer<BaseResult<EarlyClockcardBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    EarlyClockcardBean cardnumber = ((EarlyClockcardBean) response.data);
                    showcardDialog(cardnumber.number);
                    rlRecordRetroactive.setEnabled(true);
                }
            }

            @Override
            public void onFailed(String code, String msg) {
                super.onFailed(code, msg);
                rlRecordRetroactive.setEnabled(true);
            }
        });

    }

    /**
     * 打卡
     */
    private void clock() {
        HttpUtils.earlycheckinclock().enqueue(new Observer<BaseResult<EarlyClockBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    EarlyClockBean data = (EarlyClockBean) response.data;
                    switch (data.res) {
                        case 1:
                            //1未到打卡时间
                            break;
                        case 2:
                            //2你未参与今日的打卡活动
                            break;
                        case 3:
                            //3请勿重复参与打卡
                            break;
                        case 4:
                            //4打卡成功
                            clockDialog(1, data.coin);
                            break;
                        case 5:
                            //5没有补签卡，无法补签
                            showNocardDialog();
                            break;
                        case 6:
                            //6补签成功
                            clockDialog(2, data.coin);
                            break;
                    }
                    earlycheckin();
                }
            }
        });
    }

    /**
     * 参与
     */
    private void earlycheckinjoin() {
        HttpUtils.earlycheckinjoin().enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                showJoinDialog();
                earlycheckin();
            }

            @Override
            public void onFailed(String code, String msg) {
                showNoMoneyDialog();
            }
        });
    }

    /**
     * 不可参加dialog
     */
    private void notJoinDialog() {
        clockDialog = new NewCommonDialog(this, true, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_early_clock_cannot_join, null);
        clockDialog.setContentView(contentView);
        TextView tv_notice = clockDialog.findViewById(R.id.tv_notice);
        String str1 = "未到挑战时间，请于每天<font color='#FF2B49'>" + "06:30" + "</font>之后来挑战";
        tv_notice.setText(Html.fromHtml(str1));
        clockDialog.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                clockDialog.dismiss();
            }
        });
        clockDialog.show();
    }

    /**
     * 金币不足dialog
     */
    private void showNoMoneyDialog() {
        noMoneyDialog = new NewCommonDialog(this, true, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_early_clock_no_money, null);
        noMoneyDialog.setContentView(contentView);
        noMoneyDialog.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                noMoneyDialog.dismiss();
                finish();
                startActivity(new Intent(EarlyClockActivity.this, NewTaskActivity.class));

            }
        });
        noMoneyDialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noMoneyDialog.dismiss();
            }
        });

        noMoneyDialog.show();
    }

    /**
     * 参与dialog
     */
    private void showJoin() {
        joinDialog = new NewCommonDialog(this, true, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_early_clock_join, null);
        joinDialog.setContentView(contentView);
        TextView text = joinDialog.findViewById(R.id.tv_details);
        String str1 = "确定要支付<font color='#FF2B49'>" + "1w" + "</font>金币参加早起打卡吗？";
        text.setText(Html.fromHtml(str1));
        joinDialog.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                earlycheckinjoin();
                joinDialog.dismiss();

            }
        });
        joinDialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                joinDialog.dismiss();

            }
        });

        joinDialog.show();
    }

    /**
     * 报名成功dialog
     */
    private void showJoinDialog() {
        clockNoticeDialog = new NewCommonDialog(this, true, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_early_clock, null);
        clockNoticeDialog.setContentView(contentView);
        clockNoticeDialog.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                clockNoticeDialog.dismiss();
            }
        });
        clockNoticeDialog.show();
    }

    /**
     * 没有补签卡dialog
     */
    private void showNocardDialog() {
        noCardeDialog = new NewCommonDialog(this, true, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_early_clock_no_card, null);
        noCardeDialog.setContentView(contentView);
        noCardeDialog.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                noCardeDialog.dismiss();
                startActivity(new Intent(EarlyClockActivity.this, EarlyClockInviteActivity.class));
            }

        });
        noCardeDialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                noCardeDialog.dismiss();
            }
        });
        noCardeDialog.show();
    }

    /**
     * 打卡,补签成功,打卡失败dialog
     */
    private void clockDialog(int state, long coin) {
        clockDialog = new NewCommonDialog(this, true, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_early_clock_success, null);
        clockDialog.setContentView(contentView);
        TextView notice = clockDialog.findViewById(R.id.tv_notice);
        ImageView imageView = clockDialog.findViewById(R.id.tv_1);
        TextView tvDetails = clockDialog.findViewById(R.id.tv_details);
        String sum = coin / 10000 + "w";
        if (state == 3) {
            imageView.setBackgroundResource(R.drawable.icon_earlyrecord_failed);
            String str1 = "每日打卡时间08:30-11:30，早起打卡的人瓜分了<font color='#FF2B49'>" + sum + "</font>金币";
            tvDetails.setText(Html.fromHtml(str1));
        } else {
            imageView.setBackgroundResource(R.drawable.icon_earlyrecord_success);
            String str1 = "今日可随机瓜分:<font color='#FF2B49'>" + sum + "</font>金币；不要忘记12:00之后查看打卡记录哦";
            tvDetails.setText(Html.fromHtml(str1));
        }
        if (state == 1) {
            notice.setText("打卡成功");
        }
        if (state == 2) {
            notice.setText("补签成功");
        }
        if (state == 3) {
            notice.setText("打卡失败");
        }

        clockDialog.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                clockDialog.dismiss();
            }

        });

        clockDialog.show();
    }

    /**
     * 补签卡规则dialog
     */
    private void showcardRulesDialog() {
        showCardeRulesDialog = new NewCommonDialog(this, true, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_early_clock_show_card_rules, null);
        showCardeRulesDialog.setContentView(contentView);
        showCardeRulesDialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCardeRulesDialog.dismiss();
            }
        });
        showCardeRulesDialog.show();
    }

    /**
     * 补签卡dialog
     *
     * @param cardnumber
     */
    private void showcardDialog(int cardnumber) {
        showCardeDialog = new NewCommonDialog(this, true, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_early_clock_show_card, null);
        showCardeDialog.setContentView(contentView);
        showCardeDialog.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取补签卡
                showCardeDialog.dismiss();
                startActivity(new Intent(EarlyClockActivity.this, EarlyClockInviteActivity.class));
            }
        });
        showCardeDialog.findViewById(R.id.tv_rules).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看规则
                showcardRulesDialog();
                showCardeDialog.dismiss();
            }
        });
        showCardeDialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCardeDialog.dismiss();
            }
        });
        TextView text = showCardeDialog.findViewById(R.id.tv_card_number);
        ImageView imageView = showCardeDialog.findViewById(R.id.iv_1);
        if (cardnumber == 0) {
            text.setText("暂无补签券");
            imageView.setBackgroundResource(R.drawable.icon_earlyrecord_nocardshow);
        } else {
            imageView.setBackgroundResource(R.drawable.icon_earlyrecord_card);
            String str1 = "补签券/<font color='#FF2B49'>" + cardnumber + "张" + "</font>";
            text.setText(Html.fromHtml(str1));
        }
        showCardeDialog.show();
    }

    @Override
    public void onScrollChanged(int x, int y, int oldx, int oldy, int locationStatus) {
        int top = tvTips.getHeight();
        if (y <= top) {
            StatusBarUtil.setDarkMode(EarlyClockActivity.this);
            view.setBackgroundColor(ContextCompat.getColor(EarlyClockActivity.this, R.color.alpha));
            ivBack.setBackgroundResource(R.drawable.icon_white_back);
            tvTitle.setTextColor(ContextCompat.getColor(EarlyClockActivity.this, R.color.color_ffffff));
            rlLayout.setBackgroundColor(ContextCompat.getColor(EarlyClockActivity.this, R.color.alpha));

        } else {
            StatusBarUtil.setLightMode(EarlyClockActivity.this);
            view.setBackgroundColor(ContextCompat.getColor(EarlyClockActivity.this, R.color.color_ffffff));
            ivBack.setBackgroundResource(R.drawable.ic_back_black);
            tvTitle.setTextColor(ContextCompat.getColor(EarlyClockActivity.this, R.color.color_000000));
            rlLayout.setBackgroundColor(ContextCompat.getColor(EarlyClockActivity.this, R.color.color_ffffff));
        }
    }

    private void showCountDown(long millisecond) {
        countdownView.start(millisecond);
        countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                //倒计时结束回调
                llEarlyClocklayout.setEnabled(true);
                llEarlyClocklayout.setBackgroundResource(R.drawable.bg_click_in);
                countdownView.setVisibility(View.GONE);
                tvJoin.setText("立即打卡");
            }
        });
    }

    private void setHight() {
        int stateBarHeight = WindowUtils.getStateBarHeight(this);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) view.getLayoutParams(); // 取控件mGrid当前的布局参数
        linearParams.height = stateBarHeight;//
        view.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件
    }

    private void setRed(int i, String num) {
        if (i == 1) {
            String str1 = "支付<font color='#FF2B49'>" + num + "</font>金币参与瓜分";
            tvJoin.setText(Html.fromHtml(str1));
        }
        if (i == 2) {
            String str1 = "今日参与挑战人数<font color='#FF2B49'>" + num + "</font>人";
            tvJoinpeople.setText(Html.fromHtml(str1));
        }
    }

}
