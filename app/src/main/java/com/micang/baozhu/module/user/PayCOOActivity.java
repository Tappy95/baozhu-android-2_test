package com.micang.baozhu.module.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.http.bean.pay.OpenPayTypeBean;
import com.micang.baozhu.module.view.BottomDialog;
import com.micang.baozhu.module.view.NewCommonDialog;
import com.micang.baozhu.module.web.PayResult;
import com.micang.baozhu.util.ToastUtils;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventWXPay;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.user.COOBean;
import com.micang.baozhu.http.bean.user.PayBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.OnMultiClickListener;
import com.jaeger.library.StatusBarUtil;
import com.micang.baselibrary.util.TLog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

@BindEventBus
public class PayCOOActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private TextView tvPayType;
    private ImageView ivPic;
    private TextView tvType;
    private TextView tvDate;
    private TextView tvPrice;
    private Button btApply;
    private String outTradeNo;
    private int openAli;
    private int openWx;
    private String payMode = "";
    private FrameLayout flSelectPaytype;
    private BottomDialog typeDialog;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);

                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        EventBus.getDefault().postSticky(new EventWXPay<>(EventCode.wxpay, 0));
                        finish();
                        TLog.d("Alipay", payResult.toString());

                    } else {
                        TLog.d("Alipay", payResult.toString());

                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {

                    } else {

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    public int layoutId() {
        return R.layout.activity_pay_coo;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        COOBean cooBean = (COOBean) intent.getSerializableExtra("COOBean");
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparent(this);
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvPayType = findViewById(R.id.tv_pay_type);
        ivPic = findViewById(R.id.iv_pic);
        tvType = findViewById(R.id.tv_type);
        tvDate = findViewById(R.id.tv_date);
        tvPrice = findViewById(R.id.tv_price);
        flSelectPaytype = findViewById(R.id.fl_select_paytype);

        if (EmptyUtils.isNotEmpty(cooBean)) {
            if ("3".equals(cooBean.payPurpose)) {
                tvTitle.setText("续费运营总监");
                tvType.setText("续费运营总监");

            } else {
                tvTitle.setText("升级为运营总监");
                tvType.setText("升级为运营总监");

            }
            tvPrice.setText(cooBean.price);
            tvDate.setText(cooBean.effectiveDay);
            outTradeNo = cooBean.outTradeNo;
        }
        btApply = findViewById(R.id.bt_apply);
        flSelectPaytype.setOnClickListener(this);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btApply.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                payType(outTradeNo);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPayType();
    }

    private void getPayType() {
        HttpUtils.openPay().enqueue(new Observer<BaseResult<OpenPayTypeBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                OpenPayTypeBean data = (OpenPayTypeBean) response.data;
                if (EmptyUtils.isNotEmpty(data)) {
                    openAli = data.openAli;
                    openWx = data.openWx;
                }
            }
        });

    }

    private void payType(String outTradeNo) {
        if (EmptyUtils.isEmpty(payMode)) {
            ToastUtils.show("请选择支付方式");
            return;
        }
        HttpUtils.payType(outTradeNo, payMode).enqueue(new Observer<BaseResult<PayBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                PayBean data1 = (PayBean) response.data;
                PayBean.ResBean res = data1.res;
                if (EmptyUtils.isNotEmpty(res)) {
                    if ("2".equals(payMode)) {
                        String bzPackage = res.bzPackage;
                        String appid = res.appid;
                        String outTradeNo = res.outTradeNo;
                        String prepayid = res.prepayid;
                        String sign = res.sign;
                        String partnerid = res.partnerid;
                        String noncestr = res.noncestr;
                        String timestamp = res.timestamp;

                        IWXAPI api = WXAPIFactory.createWXAPI(PayCOOActivity.this, null);
                        api.registerApp(appid);
                        PayReq req = new PayReq();
                        req.appId = appid;//你的微信appid
                        req.partnerId = partnerid;//商户号
                        req.prepayId = prepayid;//预支付交易会话ID
                        req.nonceStr = noncestr;//随机字符串
                        req.timeStamp = timestamp;//时间戳
                        req.packageValue = bzPackage;
                        req.sign = sign;//签名
                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                        api.sendReq(req);
                    } else {
                        String body = res.body;
                        toAlipay(body);
                    }

                }

            }
        });
    }

    private void toAlipay(String body) {
        final String orderInfo = body;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayCOOActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventWXPay<Object> event) {
        if (event.code == EventCode.wxpay) {
            int data = (int) event.data;
            if (data == 0) {
                finish();
            }
            if (data == -1) {
                ToastUtils.show("支付失败");
            }
            if (data == -2) {
                ToastUtils.show("取消支付");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        showSelectPayType();
    }

    private void showSelectPayType() {
        typeDialog = new BottomDialog(this, false, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.bottom_dialog_choose_paytype, null);
        typeDialog.setContentView(contentView);


        LinearLayout llZfb = typeDialog.findViewById(R.id.ll_zfb);
        LinearLayout llWx = typeDialog.findViewById(R.id.ll_wx);
        if (openWx == 1) {
            llWx.setVisibility(View.VISIBLE);
        } else {
            llWx.setVisibility(View.INVISIBLE);
        }
        if (openAli == 1) {
            llZfb.setVisibility(View.VISIBLE);
        } else {
            llZfb.setVisibility(View.INVISIBLE);
        }
        TextView tvCancel = typeDialog.findViewById(R.id.tv_cancel);
        llZfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payMode = "1";
                tvPayType.setText("支付宝");
                ivPic.setBackgroundResource(R.drawable.icon_zhifubao_type);
                typeDialog.dismiss();
            }
        });
        llWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.api.isWXAppInstalled()) {
                    payMode = "2";
                    tvPayType.setText("微信");
                    ivPic.setBackgroundResource(R.drawable.icon_weixin_type);
                } else {
                    ToastUtils.show(PayCOOActivity.this, "请先安装微信,再选择支付方式");
                }
                typeDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeDialog.dismiss();
            }
        });
        typeDialog.show();
    }
}
