package com.micang.baozhu.module.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.module.view.BottomDialog;
import com.micang.baselibrary.base.BaseActivity;
import com.micang.baselibrary.event.BindEventBus;
import com.micang.baselibrary.event.EventBalance;
import com.micang.baselibrary.event.EventCode;
import com.micang.baselibrary.event.EventWXPay;
import com.micang.baselibrary.util.TLog;
import com.micang.baozhu.R;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.user.DiscountCardBean;
import com.micang.baozhu.http.bean.user.PayBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.net.HttpUtils;
import com.micang.baozhu.http.net.Observer;
import com.micang.baozhu.module.web.PayResult;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

@BindEventBus
public class BuyVipActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private TextView tvPayType;
    private TextView tvVipType;
    private TextView tvVipData;
    private TextView tvVipPrice;
    private FrameLayout flSelectDiscount;
    private FrameLayout flSelectPayType;
    private TextView tvDiscountName;
    private Button btnBuy;
    private LinearLayout llSelect;
    private TextView tvSelectLoosechagnge;
    private ImageView ivSelectLoosechagnge;
    private List<String> discountList = new ArrayList<>();
    private List<String> discountIdList = new ArrayList<>();
    private OptionsPickerView<String> genderPicker;
    private String discount;
    private List<DiscountCardBean> discountCardBeans;
    private int selectid;
    private String cardId = "0";
    private String descripte = "购买vip";
    private String payPurpose = "1";
    private String payType = "1";
    private String price;
    private String vipid;

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
                        ToastUtils.show("支付成功");
                        TLog.d("Alipay", payResult.toString());
                        paySuccess();
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
    private String outTradeNo;
    private String isBalance = "1";
    private BottomDialog typeDialog;
    private ImageView ivPic;
    private String payMode = "";

    private void paySuccess() {
        HttpUtils.paySuccess(outTradeNo).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                finish();
            }
        });
    }


    @Override
    public int layoutId() {
        return R.layout.activity_buy_vip;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setTransparent(this);
        Intent intent = getIntent();
        vipid = intent.getStringExtra("VIPID");
        price = intent.getStringExtra("PRICE");
        String data = intent.getStringExtra("DATA");
        String vipName = intent.getStringExtra("VIPNAME");
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        tvPayType = findViewById(R.id.tv_pay_type);
        ivPic = findViewById(R.id.iv_pic);
        tvVipType = findViewById(R.id.tv_vip_type);
        tvVipData = findViewById(R.id.tv_vip_data);
        tvVipPrice = findViewById(R.id.tv_vip_price);
        flSelectDiscount = findViewById(R.id.fl_select_discount);
        flSelectPayType = findViewById(R.id.fl_select_paytype);
        tvDiscountName = findViewById(R.id.tv_discount_name);
        llSelect = findViewById(R.id.ll_select);
        tvSelectLoosechagnge = findViewById(R.id.tv_select_loosechagnge);
        ivSelectLoosechagnge = findViewById(R.id.iv_select_loosechagnge);

        btnBuy = findViewById(R.id.btn_buy);
        if (EmptyUtils.isNotEmpty(price) && EmptyUtils.isNotEmpty(data) && EmptyUtils.isNotEmpty(vipName)) {
            tvVipType.setText(vipName);
            tvVipData.setText(data + "天");
            tvVipPrice.setText(price + "元");
        }
        tvTitle.setText("开通VIP");
        initSelect();
        initClick();
        discountList();
        getUserInfo();
    }

    private void discountList() {

        HttpUtils.discountList().enqueue(new Observer<BaseResult<List<DiscountCardBean>>>() {
            @Override
            public void onSuccess(BaseResult response) {
                discountCardBeans = (List<DiscountCardBean>) response.data;
                if (EmptyUtils.isNotEmpty(discountCardBeans)) {
                    discountList.clear();
                    for (int i = 0; i < discountCardBeans.size(); i++) {
                        int passbookValue = discountCardBeans.get(i).passbookValue;
                        String id = discountCardBeans.get(i).id;
                        String passbookName = discountCardBeans.get(i).passbookName;
                        discountIdList.add(id);
                        discountList.add(passbookValue + passbookName);
                    }
                    genderPicker.setPicker(discountList);
                }
            }
        });
    }

    private void initSelect() {

        genderPicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                selectid = options1;
                discount = discountList.get(options1);
            }
        })
                .setLayoutRes(R.layout.gender_picker, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                        final TextView tvCancel = v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                genderPicker.returnData();
                                genderPicker.dismiss();
                                tvDiscountName.setText(discount);
                                changeSlelect(selectid);
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tvDiscountName.setText("");
                                cardId = "0";
                                genderPicker.dismiss();
                            }
                        });
                    }
                })
                .setLineSpacingMultiplier(2.2f)
                .setDividerColor(ContextCompat.getColor(this, R.color.color_f7f7f7))
                .setContentTextSize(18)
                .setOutSideCancelable(false)
                .build();

    }

    private void changeSlelect(int discount) {
        cardId = discountIdList.get(discount);

    }

    private void getUserInfo() {

        HttpUtils.getUserInfo().enqueue(new Observer<BaseResult<UserBean>>() {
            @Override
            public void onSuccess(BaseResult response) {
                UserBean data = (UserBean) response.data;
                if (EmptyUtils.isNotEmpty(data)) {
                    String balance = data.balance;
                    if (EmptyUtils.isNotEmpty(balance)) {
                        tvSelectLoosechagnge.setText("¥ " + balance);
                    }
                }
            }
        });
    }

    private void initClick() {
        llBack.setOnClickListener(this);
        flSelectDiscount.setOnClickListener(this);
        flSelectPayType.setOnClickListener(this);
        btnBuy.setOnClickListener(this);
        llSelect.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.fl_select_discount:
                if (EmptyUtils.isNotEmpty(discountList)) {
                    genderPicker.show();
                } else {
                    ToastUtils.show("无可用优惠券");
                }
                break;
            case R.id.btn_buy:
                createOrder();
                break;
            case R.id.fl_select_paytype:
                showSelectPayType();
                break;
            case R.id.ll_select:
                if (ivSelectLoosechagnge.isSelected()) {
                    ivSelectLoosechagnge.setSelected(false);
                } else {
                    ivSelectLoosechagnge.setSelected(true);
                }
                break;
            default:
                break;
        }
    }

    private void showSelectPayType() {
        typeDialog = new BottomDialog(this, false, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.bottom_dialog_choose_paytype, null);
        typeDialog.setContentView(contentView);


        LinearLayout llZfb = typeDialog.findViewById(R.id.ll_zfb);
        LinearLayout llWx = typeDialog.findViewById(R.id.ll_wx);
//        if (openWx == 1) {
//            llWx.setVisibility(View.VISIBLE);
//        } else {
//            llWx.setVisibility(View.INVISIBLE);
//        }
//        if (openAli == 1) {
//            llZfb.setVisibility(View.VISIBLE);
//        } else {
//            llZfb.setVisibility(View.INVISIBLE);
//        }
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
                    ToastUtils.show(BuyVipActivity.this, "请先安装微信,再选择支付方式");
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

    private void createOrder() {

        if (ivSelectLoosechagnge.isSelected()) {
            isBalance = "2";
        } else {
            isBalance = "1";
        }
        if (EmptyUtils.isEmpty(payMode)) {
            ToastUtils.show("请选择支付类型");
            return;
        }
        HttpUtils.createOrder(descripte, price, cardId, payPurpose, vipid, payType, isBalance).enqueue(new Observer<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response) {
                outTradeNo = (String) response.data;
                if (outTradeNo.equals("1")) {
                    ToastUtils.show("购买成功");
                    finish();
                } else {
                    payType(outTradeNo);
                }
            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {
                super.onFailure(call, t);
            }

            @Override
            public void onFailed(String code, String msg) {
                super.onFailed(code, msg);
            }
        });

    }


    private void payType(String data) {


        HttpUtils.payType(data, payMode).enqueue(new Observer<BaseResult<PayBean>>() {
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

                        IWXAPI api = WXAPIFactory.createWXAPI(BuyVipActivity.this, null);
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventWXPay<Object> event) {
        if (event.code == EventCode.wxpay) {
            int data = (int) event.data;
            if (data == 0) {
                ToastUtils.show("支付成功");
                paySuccess();
            }
            if (data == -1) {
                ToastUtils.show("支付失败");
            }
            if (data == -2) {
                ToastUtils.show("取消支付");
            }
            EventBus.getDefault().removeStickyEvent(EventWXPay.class);
        }

    }

    private void toAlipay(String body) {
        final String orderInfo = body;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(BuyVipActivity.this);
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
}
