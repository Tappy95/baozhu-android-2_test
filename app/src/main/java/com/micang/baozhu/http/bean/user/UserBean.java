package com.micang.baozhu.http.bean.user;

import java.io.Serializable;

/**
 * @author
 * @version 1.0
 * @Package com.dizoo.http.bean.user
 * @time 2019/3/3 10:16
 * @describe describe
 */
public class UserBean implements Serializable {
    /**
     * data : {"birthday":0,"pageSize":0,"password":"","referrerMobile":"","balance":0,"qrCode":"c835953616","identity":"","socialDigitalNum":"","apprentice":0,"levelValue":0,"referrerName":"","reward":0,"aliasName":"","level":"1","profile":"","sex":0,"mobile":"15669085249","updateTime":1551579317448,"userName":"","pageNum":0,"userId":"","referrer":"","createTime":1551579317448,"recommendedTime":0,"digitalNumType":0,"imei":"866135038704620","jadeCabbage":0,"coin":0}
     * message : 登陆成功
     * statusCode : 2001
     * token : a2f7eba291bfcc1a3c9d73b13aab18ec
     */


    /**
     * birthday : 0          // 生日
     * pageSize : 0
     * password :
     * referrerMobile :      // 邀请人手机号
     * balance : 0
     * qrCode : c835953616      // 邀请二维码地址
     * identity :               // 登陆账号
     * socialDigitalNum :          // 证件号
     * apprentice : 0               // 学徒数量
     * levelValue : 0                   // 等级值，经验值
     * referrerName :                   // 邀请人姓名
     * reward : 0                    // 获取的总奖励金额
     * aliasName :                   // 昵称
     * level : 1                    等级名称 L0 L1
     * profile :                    头像地址
     * sex : 0                           // 性别 1-男 2-女
     * mobile : 15669085249
     * updateTime : 1551579317448
     * userName :
     * pageNum : 0
     * userId :                            // 用户id
     * referrer :                         // 邀请人
     * createTime : 1551579317448
     * recommendedTime : 0                  // 推荐时间
     * digitalNumType : 0                   // 证件类型
     * imei : 866135038704620
     * jadeCabbage : 0                      // 代币
     * coin : 0
     * payPassword        支付密码
     * "roleType           1：普通  其他高级
     * qqNum            ""
     */
    public String birthday;
    public String password;
    public String referrerMobile;
    public String balance = "";
    public String qrCode;
    public String identity;
    public String apprentice;
    public String referrerName;
    public String reward;
    public String aliasName;
    public String level;
    public String profile;
    public int sex;
    public String mobile;
    public String userName;
    public int pageNum;
    public String userId;
    public String referrer;
    public String imei;
    public String coin;
    public String referrerCode;//邀请码
    public String openId;//微信openid
    public String aliNum;//支付宝账户
    public String accountId;//id
    public String payPassword;//支付密码
    public int roleType;//1.普通，需权限判断， 其他为高级，不判断
    public String qqNum;// QQ号


}
