package com.micang.baozhu.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.login.GetSmsBean;
import com.micang.baozhu.http.bean.pay.WithDrawBean;
import com.micang.baozhu.http.bean.user.CashNumBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.bean.user.UserWithDrawBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.home.NewVipActivity;
import com.micang.baozhu.module.user.adapter.WithdrawAdapter;
import com.micang.baozhu.module.view.CommonDialog;
import com.micang.baozhu.module.view.NewCommonDialog;
import com.micang.baozhu.module.web.AccountDataActivity;
import com.micang.baozhu.module.web.FriendActiviateActivity;
import com.micang.baozhu.module.web.WithDrawlogActivity;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.OnMultiClickListener;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventWXBind;
import com.micang.baselibrary.util.DensityUtil;
import com.micang.baselibrary.util.TLog;
import com.micang.baselibrary.view.countdown.CountDownTimerButton;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/7/26 22:57
 * @describe 提现
 */

@BindEventBus
public class NewWithdrawActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private LinearLayout llZhifubao;
    private LinearLayout llWeixin;
    private LinearLayout ll_select;
    private Button bt_withdraw;
    private TextView tvwithdrawstring;
    private ImageView ivPic;
    private TextView tvWxname;
    private TextView tvNotGetMessage;
    private TextView tvWithdrawFailed;
    private LinearLayout llClikBind;
    private LinearLayout llDrawLog;
    private TextView tvBalance;
    private TextView tvChangeAlipay;
    private LinearLayout llNotBind;
    private LinearLayout llInput;
    private EditText etInputMoney;
    private TextView tvMoblie;
    private EditText etCode;
    private CountDownTimerButton getCode;
    private LinearLayout llRecycle;
    private RecyclerView recycleview;
    private TextView tvAlipayName;
    private TextView tvAlipayNumber;
    private ConstraintLayout conBinded;
    private TextView tvNotbind;
    private EditText etName;
    private LinearLayout llName;
    private TextView tvCashnum;
    private LinearLayout llFriendActivity;
    private TextView tvActivity;
    private TextView tvAccountPercent;

    private int ffffColor;
    private int SELECT_ALIPAY = 2;//默认提现到支付宝
    int accountType = 2;          //默认提现到支付宝
    private String mobile = "";
    private String ruleNum = "10060";
    private String sendMode = "1";
    private String aliNum;
    private String userName;
    private boolean one_withdrawals;
    private List<UserWithDrawBean> list = new ArrayList<>();
    private int mSelectedPos = -1;
    private String resultBalance;
    private WithdrawAdapter adapter;
    private String openId;
    private String profile;
    private String aliasName;
    private CommonDialog showWithdrawFailedDialog;
    private CommonDialog getMessageFailedDialog;
    private CommonDialog moreMoneyDialog;
    private NewCommonDialog bindPhoneDialog;
    private boolean countWx;
    private boolean isName;
    private String balance;
    private int daren;


    @Override
    public int layoutId() {
        return R.layout.activity_withdraw_sure;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setDarkMode(this);
        ffffColor = ContextCompat.getColor(this, R.color.color_ffffff);
        llBack = findViewById(R.id.ll_back);
        llDrawLog = findViewById(R.id.ll_draw_log);
        tvBalance = findViewById(R.id.tv_balance);
        bt_withdraw = findViewById(R.id.bt_withdraw);
        ll_select = findViewById(R.id.ll_select);
        llNotBind = findViewById(R.id.ll_not_bind);
        tvChangeAlipay = findViewById(R.id.tv_change_alipay);
        conBinded = findViewById(R.id.con_binded);
        tvNotbind = findViewById(R.id.tv_notbind);
        tvAlipayName = findViewById(R.id.tv_alipay_name);
        tvAlipayNumber = findViewById(R.id.tv_alipay_number);


        llFriendActivity = findViewById(R.id.ll_friend_activity);
        tvActivity = findViewById(R.id.tv_activity);
        tvAccountPercent = findViewById(R.id.tv_account_percent);


        tvCashnum = findViewById(R.id.tv_cashnum);

        llRecycle = findViewById(R.id.ll_recycle);
        recycleview = findViewById(R.id.recycleview);

        llInput = findViewById(R.id.ll_input);
        etInputMoney = findViewById(R.id.et_input_money);
        tvMoblie = findViewById(R.id.tv_moblie);
        etCode = findViewById(R.id.et_code);
        getCode = findViewById(R.id.get_code);

        llZhifubao = findViewById(R.id.ll_zhifubao);
        llWeixin = findViewById(R.id.ll_weixin);
        tvwithdrawstring = findViewById(R.id.tv_withdraw_string);
        ivPic = findViewById(R.id.iv_pic);
        tvWxname = findViewById(R.id.tv_wxname);
        llClikBind = findViewById(R.id.ll_clik_bind);
        tvNotGetMessage = findViewById(R.id.tv_not_get_message);
        tvWithdrawFailed = findViewById(R.id.tv_withdraw_failed);
        etName = findViewById(R.id.et_name);

        llName = findViewById(R.id.ll_name);

        setBalance("0.00");
        initClick();
        initRecycle();
        getwithdrawList();
        getcashNum();

    }

    private void getcashNum() {
        HttpUtils.cashNum().enqueue(new Observer<BaseResult<CashNumBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    CashNumBean data = (CashNumBean) response.data;
                    tvCashnum.setText("小额秒到！每日限提" + data.cashNum + "笔！" + '\n' + "大额人工审核，24H到账，节假日顺延。");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        readyWithdrawals();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventWXBind<Object> event) {
        if (event.code == EventCode.wxcode) {
            String wxcode = event.data.toString();
            if (EmptyUtils.isEmpty(openId)) {
                bindling(wxcode);
            } else {
                changeWx(wxcode);
            }
            TLog.d("wxcode", wxcode);
            EventBus.getDefault().removeStickyEvent(EventWXBind.class);
        }
    }

    private void bindling(String wxcode) {
        String name = etName.getText().toString().trim();
        HttpUtils.bindingWxNumber("2", wxcode, name).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                ToastUtils.show("绑定成功");
                getUserInfo();
            }
        });
    }

    /**
     * 没有openid
     *
     * @param wxcode
     */
    private void changeWx(String wxcode) {
        String name = etName.getText().toString().trim();
        HttpUtils.changeWxNumber("2", wxcode, name).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                ToastUtils.show("绑定成功");
                getUserInfo();
            }
        });
    }

    /**
     * 已有openid
     */
    private void changeWx() {
        String name = etName.getText().toString().trim();
        HttpUtils.changeWxNumber("2", "", name).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                ToastUtils.show("绑定成功");
                getUserInfo();
            }
        });
    }

    private void initRecycle() {
        recycleview.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        adapter = new WithdrawAdapter(R.layout.item_withdraw_balance, list);
        recycleview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mSelectedPos != position) {
                    if (mSelectedPos != -1) {
                        list.get(mSelectedPos).setSelected(false);
                        adapter.notifyItemChanged(mSelectedPos);
                    }
                    mSelectedPos = position;
                    list.get(mSelectedPos).setSelected(true);
                    adapter.notifyItemChanged(mSelectedPos);
                    resultBalance = list.get(position).price;
                }
                if (list.get(position).isTask == 1) {
                    if (accountType == 2) {
                        if (EmptyUtils.isEmpty(aliNum) || EmptyUtils.isEmpty(userName)) {
                            if (mSelectedPos != -1) {
                                list.get(mSelectedPos).setSelected(false);
                                adapter.notifyItemChanged(mSelectedPos);
                                mSelectedPos = -1;
                                resultBalance = null;
                            }
                            ToastUtils.show("请先绑定支付宝");
                            return;
                        }
                    } else {
                        if (EmptyUtils.isEmpty(openId)) {
                            if (mSelectedPos != -1) {
                                list.get(mSelectedPos).setSelected(false);
                                adapter.notifyItemChanged(mSelectedPos);
                                mSelectedPos = -1;
                                resultBalance = null;
                            }
                            ToastUtils.show("请先绑定微信");
                            return;
                        }
                    }
                    userCash();
                }
                if (position == 8) {
                    showMoreDialog();
                }
            }
        });
    }

    private void userCash() {
        HttpUtils.userCash(accountType, resultBalance).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    String data = (String) response.data;
                    if ("1".equals(data)) {
                        startActivity(new Intent(NewWithdrawActivity.this, WithDrawTaskActivity.class));
                        if (mSelectedPos != -1) {
                            list.get(mSelectedPos).setSelected(false);
                            adapter.notifyItemChanged(mSelectedPos);
                            mSelectedPos = -1;
                            resultBalance = null;
                        }
                    }
                }
            }
        });
    }

    private void getwithdrawList() {
        HttpUtils.cashPrice().enqueue(new Observer<BaseResult<List<UserWithDrawBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                if (EmptyUtils.isNotEmpty(response.data)) {
                    List<UserWithDrawBean> data = (List<UserWithDrawBean>) response.data;
                    list.clear();
                    list.addAll(data);
                    list.add(new UserWithDrawBean(2, "其他金额"));
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initClick() {
        llBack.setOnClickListener(this);
        llDrawLog.setOnClickListener(this);
        llZhifubao.setOnClickListener(this);
        llWeixin.setOnClickListener(this);
        bt_withdraw.setOnClickListener(this);
        llClikBind.setOnClickListener(this);
        tvChangeAlipay.setOnClickListener(this);
        getCode.setOnClickListener(this);
        llFriendActivity.setOnClickListener(this);
        tvNotGetMessage.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                showgetMessageFailedDialog();
            }
        });
        tvWithdrawFailed.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                showWithdrawFailedDialog();
            }
        });
        bt_withdraw.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (daren != 1) {
                    if (one_withdrawals) {
                        String trim = etInputMoney.getText().toString().trim();
                        if (EmptyUtils.isEmpty(trim)) {
                            ToastUtils.show("请输入金额");
                            return;
                        }
                    }
                }

                String code = etCode.getText().toString().trim();
                if (EmptyUtils.isEmpty(code)) {
                    ToastUtils.show("请输入验证码");
                    return;
                }

                //验证验证码
                checkSmsCode(code);
            }
        });
    }

    private void showWithdrawFailedDialog() {
        showWithdrawFailedDialog = new CommonDialog(this, true, true, Gravity.CENTER);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_withdraw_failed, null);
        showWithdrawFailedDialog.setContentView(contentView);
        showWithdrawFailedDialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                showWithdrawFailedDialog.dismiss();
            }
        });
        showWithdrawFailedDialog.show();
    }

    private void showgetMessageFailedDialog() {
        getMessageFailedDialog = new CommonDialog(this, true, true, Gravity.CENTER);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_not_getmessage_failed, null);
        getMessageFailedDialog.setContentView(contentView);
        getMessageFailedDialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                getMessageFailedDialog.dismiss();
            }
        });
        getMessageFailedDialog.show();
    }

    private void showMoreDialog() {
        moreMoneyDialog = new CommonDialog(this, true, true, Gravity.CENTER);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_more_money, null);
        moreMoneyDialog.setContentView(contentView);
        moreMoneyDialog.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                moreMoneyDialog.dismiss();
                startActivity(new Intent(NewWithdrawActivity.this, NewVipActivity.class));
            }
        });
        moreMoneyDialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                moreMoneyDialog.dismiss();
            }
        });
        moreMoneyDialog.show();
    }

    private void showBindPhoneDialog() {
        bindPhoneDialog = new NewCommonDialog(this, true, false, Gravity.CENTER, 1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_bind_phone, null);
        bindPhoneDialog.setContentView(contentView);
        bindPhoneDialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                bindPhoneDialog.dismiss();
//                startActivity(new Intent(NewWithdrawActivity.this, VipActivity.class));
            }
        });
        bindPhoneDialog.show();
    }

    /**
     * 绑定微信
     */
    public void wxLogin() {
        String name = etName.getText().toString().trim();
        if (EmptyUtils.isEmpty(name)) {
            ToastUtils.show("请输入微信实名认证姓名");
            return;
        }
        if (EmptyUtils.isNotEmpty(openId)) {
            changeWx();
        } else {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "diandi_wx_login";
            AppContext.api.sendReq(req);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.get_code:
                if (accountType == 2) {
                    if (EmptyUtils.isEmpty(aliNum) || EmptyUtils.isEmpty(userName)) {
                        ToastUtils.show("请先绑定支付宝");
                        return;
                    }
                } else {
                    if (EmptyUtils.isEmpty(openId)) {
                        ToastUtils.show("请先绑定微信");
                        return;
                    }
                }
                if (daren == 1) {
                    if (EmptyUtils.isEmpty(resultBalance) || "其他金额".contains(resultBalance)) {
                        ToastUtils.show("请选择提现金额");
                        return;
                    }
                    showWithDraw(resultBalance);
                } else {
                    if (one_withdrawals) {
                        String trim = etInputMoney.getText().toString().trim();
                        if (EmptyUtils.isEmpty(trim)) {
                            ToastUtils.show("请输入金额");
                            return;
                        }
                        showWithDraw(trim);
                    } else {
                        if (EmptyUtils.isEmpty(resultBalance) || "其他金额".contains(resultBalance)) {
                            ToastUtils.show("请选择提现金额");
                            return;
                        }
                        showWithDraw(resultBalance);
                    }
                }

                break;
            case R.id.ll_draw_log:
                startActivity(new Intent(this, WithDrawlogActivity.class));
                break;
            case R.id.tv_change_alipay:
                Intent intent = new Intent(this, ChangeAlipayActivity.class);
                intent.putExtra("Mobile", mobile);
                intent.putExtra("aliNum", aliNum);
                intent.putExtra("userName", userName);
                startActivityForResult(intent, 3);
                break;
            case R.id.ll_zhifubao:
                withdrawType(2);
                break;
            case R.id.ll_weixin:
                withdrawType(1);
                break;
            case R.id.ll_friend_activity:

                startActivity(new Intent(this, FriendActiviateActivity.class));
                break;

            case R.id.ll_clik_bind:
                if (accountType == 1) {
                    if (EmptyUtils.isEmpty(openId) || countWx) {
                        if (AppContext.api.isWXAppInstalled()) {
                            wxLogin();
                        } else {
                            ToastUtils.show("请先安装微信");
                        }
                    }
                } else {
                    if (EmptyUtils.isEmpty(aliNum) || EmptyUtils.isEmpty(userName)) {
                        Intent intentBind = new Intent(NewWithdrawActivity.this, BindlingAlipayActivity.class);
                        intentBind.putExtra("Mobile", mobile);
                        intentBind.putExtra("userName", userName);
                        startActivity(intentBind);
                    }
                }
                break;
        }
    }

    private void showWithDraw(String resultBalance) {
        long selectBa = Long.parseLong(resultBalance);
        double ba = Double.parseDouble(balance);
        if (selectBa > ba) {
            ToastUtils.show("余额不足");
            return;
        } else {
            getCode();
            getCode.setEnableCountDown(false);
        }
    }

    private void getCode() {
        getCode.startAction();
        getCode.setColor(ffffColor);
        HttpUtils.getSmscode(mobile, ruleNum, sendMode).enqueue(new Observer<BaseResult<GetSmsBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                GetSmsBean data = (GetSmsBean) response.data;
                if (data.res) {
                    ToastUtils.show(data.message);
                } else {
                    getCode.removeCountDown();
                    ToastUtils.show(data.message);
                }
            }

            @Override
            public void onFailed(String code, String msg) {
                super.onFailed(code, msg);
                getCode.removeCountDown();

            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                getCode.removeCountDown();
            }
        });
    }

    private void checkSmsCode(String code) {
        HttpUtils.checkSmscode(mobile, ruleNum, sendMode, code).enqueue(new Observer<BaseResult<GetSmsBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                GetSmsBean data = (GetSmsBean) response.data;
                if (data.res) {
                    String codeKey = data.codeKey;
                    WithDraw(codeKey);
                } else {
                    ToastUtils.show(data.message);
                }
            }
        });
    }

    private void WithDraw(String codeKey) {
        if (accountType == 2) {
            if (EmptyUtils.isEmpty(aliNum) || EmptyUtils.isEmpty(userName)) {
                ToastUtils.show("请先绑定支付宝");
                return;
            }
        } else {
            if (EmptyUtils.isEmpty(openId)) {
                ToastUtils.show("请先绑定微信");
                return;
            }
        }
        String amount;
        if (daren == 1) {
            amount = resultBalance;
        } else {
            if (one_withdrawals) {
                String trim = etInputMoney.getText().toString().trim();
                amount = trim;
            } else {
                amount = resultBalance;
            }
        }

        String account = aliNum;
        HttpUtils.withdrawalsapply(codeKey, amount, account, accountType).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                ToastUtils.show(response.message);
                startActivity(new Intent(NewWithdrawActivity.this, AccountDataActivity.class));
                finish();
            }
        });
    }

    private void getUserInfo() {
        if (EmptyUtils.isTokenEmpty(this)) {
            return;
        }
        HttpUtils.getUserInfo().enqueue(new Observer<BaseResult<UserBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                UserBean data = (UserBean) response.data;
                aliNum = data.aliNum;
                userName = data.userName;
                aliasName = data.aliasName;
                mobile = data.mobile;
                openId = data.openId;
                tvMoblie.setText(mobile);
                profile = data.profile;
                setWithdrawType(accountType);
            }
        });
    }

    private void readyWithdrawals() {
        HttpUtils.readyWithdrawals().enqueue(new Observer<BaseResult<WithDrawBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                WithDrawBean data = (WithDrawBean) response.data;
                balance = data.balance;
                setBalance(balance);
                one_withdrawals = data.one_withdrawals;
                countWx = data.countWx;
                daren = data.daren;
                isName = data.isName;
                if (daren == 1) {
                    llInput.setVisibility(View.GONE);
                    llRecycle.setVisibility(View.VISIBLE);
                    SpannableString qualityScore = getColorText("好友活跃度:", data.qualityScore + "分");
                    SpannableString activityScore = getColorText("提现到账 ", data.activityScore + "%");
                    tvActivity.setText(qualityScore);
                    tvAccountPercent.setText(activityScore);
                } else {
                    if (one_withdrawals) {
                        llInput.setVisibility(View.VISIBLE);
                        llRecycle.setVisibility(View.GONE);
                    } else {
                        llInput.setVisibility(View.GONE);
                        llRecycle.setVisibility(View.VISIBLE);
                    }
                }

                if (data.openActivity == 2) {
                    llFriendActivity.setVisibility(View.VISIBLE);
                } else {
                    llFriendActivity.setVisibility(View.GONE);

                }
            }
        });
    }

    private void withdrawType(int state) {
        accountType = state;
        ll_select.setBackgroundResource(SELECT_ALIPAY == state ? R.drawable.bg_withdraw_select_alipay : R.drawable.bg_withdraw_select_wxpay);
        tvwithdrawstring.setText(SELECT_ALIPAY == state ? "提现至【支付宝-余额】：" : "提现至【微信-钱包】：");
        setWithdrawType(state);
    }

    private void setWithdrawType(int state) {
        if (this.isFinishing()) {
            return;
        }
        if (state == 1) {
            conBinded.setVisibility(View.GONE);
            llNotBind.setVisibility(View.VISIBLE);
            llName.setVisibility(View.VISIBLE);
            if (EmptyUtils.isEmpty(openId) || EmptyUtils.isEmpty(userName)) {
                RequestOptions requestOptions = RequestOptions.circleCropTransform();
                Glide.with(NewWithdrawActivity.this).load(R.drawable.icon_wxpay_pic).apply(requestOptions).into(ivPic);
                tvNotbind.setVisibility(View.VISIBLE);
                tvWxname.setVisibility(View.GONE);

            } else {
                RequestOptions requestOptions = RequestOptions.circleCropTransform();
                Glide.with(NewWithdrawActivity.this).load(profile).apply(requestOptions).into(ivPic);
                tvNotbind.setVisibility(View.GONE);
                tvWxname.setVisibility(View.VISIBLE);
                tvWxname.setText(aliasName);
            }
            if (EmptyUtils.isNotEmpty(userName)) {
                etName.setText(userName);
            }
            if (countWx) {
                etName.setEnabled(true);
            } else {
                etName.setEnabled(false);
            }

        } else {
            llName.setVisibility(View.GONE);
            if (EmptyUtils.isEmpty(aliNum) || EmptyUtils.isEmpty(userName)) {
                conBinded.setVisibility(View.GONE);
                RequestOptions requestOptions = RequestOptions.circleCropTransform();
                Glide.with(NewWithdrawActivity.this).load(R.drawable.icon_alipay_pic).apply(requestOptions).into(ivPic);
                llNotBind.setVisibility(View.VISIBLE);
                tvNotbind.setVisibility(View.VISIBLE);
                tvWxname.setVisibility(View.GONE);
            } else {
                conBinded.setVisibility(View.VISIBLE);
                llNotBind.setVisibility(View.GONE);
                tvAlipayNumber.setText(aliNum);
                tvAlipayName.setText(userName);
            }
        }
    }


    private void setBalance(String balance) {
        String text = "¥ " + balance;
        SpannableString spannableString = new SpannableString(text);
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(DensityUtil.sp2px(NewWithdrawActivity.this, 14), false);
        spannableString.setSpan(absoluteSizeSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvBalance.setText(spannableString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getCode.removeCountDown();
    }

    private SpannableString getColorText(String text, String percent) {
        SpannableString ss1 = new SpannableString(text + percent);
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.FF3352)), text.length(), ss1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss1;
    }
}
