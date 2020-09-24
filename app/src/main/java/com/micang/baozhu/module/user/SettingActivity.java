package com.micang.baozhu.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micang.baozhu.AppContext;
import com.micang.baozhu.module.web.AccountDataActivity;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventToHome;
import com.micang.baselibrary.event.EventWXBind;
import com.micang.baselibrary.util.SPUtils;
import com.micang.baozhu.R;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.MainPositionBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.home.MainActivity;
import com.micang.baozhu.module.view.CommonDialog;
import com.micang.baozhu.util.AppUtils;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.micang.baselibrary.util.TLog;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@BindEventBus
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private FrameLayout flBindingWeixin;
    private TextView tvWeixing;
    private FrameLayout flBindingPhone;
    private TextView tvPhone;
    private FrameLayout flBindingZhifubao;
    private TextView tvZhifubao;
    private FrameLayout flChangePsw;
    private FrameLayout flVersions;
    private TextView tvVersions;
    private Button btnLogin;
    private String mobile;
    private CommonDialog commonDialog;
    private String aliNum;
    private FrameLayout flPaypsw;
    private String payPassword;
    private String openId;


    @Override
    public int layoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("设置");
        flBindingWeixin = (FrameLayout) findViewById(R.id.fl_binding_weixin);
        tvWeixing = (TextView) findViewById(R.id.tv_weixing);
        flBindingPhone = (FrameLayout) findViewById(R.id.fl_binding_phone);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        flBindingZhifubao = (FrameLayout) findViewById(R.id.fl_binding_zhifubao);
        tvZhifubao = (TextView) findViewById(R.id.tv_zhifubao);
        flChangePsw = (FrameLayout) findViewById(R.id.fl_change_psw);
        flVersions = (FrameLayout) findViewById(R.id.fl_versions);
        flPaypsw = findViewById(R.id.fl_paypsw);
        tvVersions = findViewById(R.id.tv_versions);
        btnLogin = (Button) findViewById(R.id.btn_login);
        initClick();
        String appVersionName = AppUtils.getAppVersionName("com.micang.baozhu");
        tvVersions.setText("V" + appVersionName);
    }

    private void initClick() {
        llBack.setOnClickListener(this);
        flBindingWeixin.setOnClickListener(this);
        flBindingZhifubao.setOnClickListener(this);
        flBindingPhone.setOnClickListener(this);
        flChangePsw.setOnClickListener(this);
        flPaypsw.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        getUserInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventWXBind<Object> event) {
        if (event.code == EventCode.wxcode) {
            String wxcode = event.data.toString();
            bindling(wxcode);
            TLog.d("UMLog_Social", wxcode);
            EventBus.getDefault().removeStickyEvent(EventWXBind.class);
        }
    }

    private void bindling(String wxcode) {
        HttpUtils.bindingWxNumber("2", wxcode, "").enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                ToastUtils.show("绑定成功");
                getUserInfo();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.fl_binding_weixin:
                if (EmptyUtils.isEmpty(openId)) {
//                    if (AppContext.api.isWXAppInstalled()) {
//                        wxLogin();
//                    } else {
//                        ToastUtils.show("请先安装微信");
//                    }
                    startActivity(new Intent(SettingActivity.this, NewWithdrawActivity.class));
                }
                break;
            case R.id.fl_binding_zhifubao:
                //跳转
                if (EmptyUtils.isEmpty(aliNum)) {
                    Intent intentWithDraw = new Intent(SettingActivity.this, BindlingAlipayActivity.class);
                    intentWithDraw.putExtra("Mobile", mobile);
                    intentWithDraw.putExtra("CODE", 1);
                    startActivityForResult(intentWithDraw, 4);

                }

                break;
            case R.id.fl_change_psw:
                String s = tvPhone.getText().toString();
                Intent intent = new Intent(SettingActivity.this, ChangePswActivity.class);
                intent.putExtra("moblie", s);
                startActivity(intent);
                break;
            case R.id.fl_paypsw:
                String phone = tvPhone.getText().toString();
                Intent intentP = new Intent(SettingActivity.this, PayPswActivity.class);
                intentP.putExtra("moblie", phone);
                String key;
                if (EmptyUtils.isEmpty(payPassword)) {
                    key = "0";
                } else {
                    key = "1";
                }
                intentP.putExtra("release.keystore", key);
                startActivityForResult(intentP, 5);
                break;
            case R.id.btn_login:
                showLogout();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == 4 || resultCode == 5) {
            getUserInfo();
        }
    }

    private void getUserInfo() {
        HttpUtils.getUserInfo().enqueue(new Observer<BaseResult<UserBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                UserBean data = (UserBean) response.data;
                mobile = data.mobile;
                aliNum = data.aliNum;
                openId = data.openId;
                payPassword = data.payPassword;
                if (EmptyUtils.isNotEmpty(aliNum)) {
                    tvZhifubao.setText("已绑定");
                } else {
                    tvZhifubao.setText("未绑定");
                }
                if (EmptyUtils.isNotEmpty(openId)) {
                    tvWeixing.setText("已绑定");
                } else {
                    tvWeixing.setText("未绑定");
                }
                tvPhone.setText(mobile);
            }
        });
    }


    private void showLogout() {
        commonDialog = new CommonDialog(this, true, true, Gravity.CENTER);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_commom, null);
        commonDialog.setContentView(contentView);

        commonDialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                SPUtils.saveString(SettingActivity.this, CommonConstant.MOBIL_IMEI, null);
                SPUtils.saveString(SettingActivity.this, CommonConstant.USER_TOKEN, null);
                EventBus.getDefault().postSticky(new EventToHome<>(10099, new MainPositionBean(0)));
                Intent logoutIntent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(logoutIntent);
                commonDialog.dismiss();
                finish();
                ToastUtils.show(SettingActivity.this, "退出登录");
            }
        });

        commonDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }
}
