package com.micang.baselibrary.event;

/**
 * EventBus事件Id
 */
public class EventCode {

    //first界面发送mobile
    public static final int mobile = 0x100000;
    //home界面发送账户余额
    public static final int balance = 0x100001;
    //打卡活动的id
    public static final int clockid = 0x100002;
    //传递的出题结果
    public static final int questionid = 0x100003;
    //传递用户信息
    public static final int USERINFO = 0x100004;
    //传递关闭first界面信息
    public static final int close = 0x100005;
    //传递用户选择地址
    public static final int useAddress = 0x100006;
    //传递微信支付结果
    public static final int wxpay = 0x100007;
    //传递微信授权码
    public static final int wxcode = 0x100008;
    //传递vip信息
    public static final int vipcode = 0x100009;
    //请求权限
    public static final int permissioncode = 0x1000010;
}
