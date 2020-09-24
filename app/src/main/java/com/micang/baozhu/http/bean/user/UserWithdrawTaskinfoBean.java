package com.micang.baozhu.http.bean.user;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/8/21 19:16
 * @describe describe
 */
public class UserWithdrawTaskinfoBean {


    /**
     * data : {"res":1,"lUserCash":{"mode":2,"money":2,"outTradeNo":"2019082117265921p58sf","pageSize":0,"creatorTime":1566379619356,"id":4,"state":1,"pageNum":0,"userId":"1043572b61b945ed81c21d9c1120d34a"},"games":[{"gameType":0,"orderId":1,"signinId":0,"lid":0,"icon":"https://xianwanimgs.oss-cn-hangzhou.aliyuncs.com/201907/636991268607533876.png","typeName":"","pageSize":0,"gameTitle":"魔神乱世-2期","ptype":2,"shortIntro":"充值返10","id":3504,"interfaceName":"","packageName":"com.qjzjlq.union.leqi.duoyou","taskStatus":0,"gameTag":1,"gameId":7268,"coins":0,"introduce":"屠龙斗兽、收集神宠，觉醒神装！","labelStr":"简单,好赚","pageNum":0,"url":"https://h5.51xianwan.com/try/try_cpl_plus.aspx?ptype=[ptype]&deviceid=[imei]&appid=3580&appsign=[userid]&adid=7268&keycode=[keycode]","tryTag":0,"cashStatus":1,"enddate":"1566748740","gameGold":1049,"typeId":0,"interfaceId":3,"status":1}]}
     * message : 操作成功
     * statusCode : 2000
     * token : 4099ac907466a9a6be506560a0e84b20
     */

    /**
     * res : 1
     * lUserCash : {"mode":2,"money":2,"outTradeNo":"2019082117265921p58sf","pageSize":0,"creatorTime":1566379619356,"id":4,"state":1,"pageNum":0,"userId":"1043572b61b945ed81c21d9c1120d34a"}
     * games : [{"gameType":0,"orderId":1,"signinId":0,"lid":0,"icon":"https://xianwanimgs.oss-cn-hangzhou.aliyuncs.com/201907/636991268607533876.png","typeName":"","pageSize":0,"gameTitle":"魔神乱世-2期","ptype":2,"shortIntro":"充值返10","id":3504,"interfaceName":"","packageName":"com.qjzjlq.union.leqi.duoyou","taskStatus":0,"gameTag":1,"gameId":7268,"coins":0,"introduce":"屠龙斗兽、收集神宠，觉醒神装！","labelStr":"简单,好赚","pageNum":0,"url":"https://h5.51xianwan.com/try/try_cpl_plus.aspx?ptype=[ptype]&deviceid=[imei]&appid=3580&appsign=[userid]&adid=7268&keycode=[keycode]","tryTag":0,"cashStatus":1,"enddate":"1566748740","gameGold":1049,"typeId":0,"interfaceId":3,"status":1}]
     */

    public int res;
    public LUserCashBean lUserCash;
    public List<GamesBean> games;

    public static class LUserCashBean {
        /**
         * mode : 2
         * money : 2
         * outTradeNo : 2019082117265921p58sf
         * pageSize : 0
         * creatorTime : 1566379619356
         * id : 4
         * state : 1
         * pageNum : 0
         * userId : 1043572b61b945ed81c21d9c1120d34a
         */

        public int mode;                //提现方式1-微信 2-支付宝
        public int money;               //提现金额
        public int id;                  //提现id
        public int state;             //状态：1未完成2已完成3审核中4提现成功
    }

    public static class GamesBean {
        /**
         * gameType : 0
         * orderId : 1
         * signinId : 0
         * lid : 0
         * icon : https://xianwanimgs.oss-cn-hangzhou.aliyuncs.com/201907/636991268607533876.png
         * typeName :
         * pageSize : 0
         * gameTitle : 魔神乱世-2期
         * ptype : 2
         * shortIntro : 充值返10
         * id : 3504
         * interfaceName :
         * packageName : com.qjzjlq.union.leqi.duoyou
         * taskStatus : 0
         * gameTag : 1
         * gameId : 7268
         * coins : 0
         * introduce : 屠龙斗兽、收集神宠，觉醒神装！
         * labelStr : 简单,好赚
         * pageNum : 0
         * url : https://h5.51xianwan.com/try/try_cpl_plus.aspx?ptype=[ptype]&deviceid=[imei]&appid=3580&appsign=[userid]&adid=7268&keycode=[keycode]
         * tryTag : 0
         * cashStatus : 1
         * enddate : 1566748740
         * gameGold : 1049
         * typeId : 0
         * interfaceId : 3
         * status : 1
         */

        public int gameType;
        public int orderId;
        public int signinId;
        public int lid;
        public String icon;
        public String typeName;

        public String gameTitle;
        public int ptype;
        public String shortIntro;
        public int id;
        public String interfaceName;
        public String packageName;
        public int taskStatus;
        public int gameTag;
        public int gameId;
        public int coins;
        public String introduce;
         public int pageNum;
        public String url;
        public int tryTag;
        public int cashStatus;      //提现任务状态（1-未完成 2-已完成）
        public long enddate;
        public double gameGold;
        public int typeId;
        public int interfaceId;
        public int status;
    }
}
