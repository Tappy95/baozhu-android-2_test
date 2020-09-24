package com.micang.baozhu.http.bean.home;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/8/8 13:53
 * @describe describe
 */
public class VipInfoBean {

    /**
     * data : {"lUserGameTask":null,"mVipInfo":{"coinToPigAddition":10,"isBuy":0,"orderId":1,"everydayReliefPig":10000,"pageSize":0,"backgroundImg":"https://image.bzlyplay.com/pro_20190808115728805.png","everydayReliefPigTimes":1,"price":0.01,"firstReward":100000,"logo":"https://image.baozhu8.com/test_20190319192747840.png","id":10,"everydayActiveCoin":10000,"isRenew":1,"taskNum":0,"continueRewardUnit":1,"isPay":0,"isTask":1,"firstRewardUnit":1,"auditFirst":1,"gameAddition":40,"onetimeLimit":50000000,"pageNum":0,"everydayActivePig":10000,"oneWithdrawals":1,"continueReward":100000,"sendSms":1,"createTime":1552966850679,"name":"试玩加成VIP","status":1,"useDay":31}}
     * message : 操作成功
     * statusCode : 2000
     * token : 7ba4d9d9f448b0e0e5f02b2998067cc8
     */

    /**
     * lUserGameTask : null
     * mVipInfo : {"coinToPigAddition":10,"isBuy":0,"orderId":1,"everydayReliefPig":10000,"pageSize":0,"backgroundImg":"https://image.bzlyplay.com/pro_20190808115728805.png","everydayReliefPigTimes":1,"price":0.01,"firstReward":100000,"logo":"https://image.baozhu8.com/test_20190319192747840.png","id":10,"everydayActiveCoin":10000,"isRenew":1,"taskNum":0,"continueRewardUnit":1,"isPay":0,"isTask":1,"firstRewardUnit":1,"auditFirst":1,"gameAddition":40,"onetimeLimit":50000000,"pageNum":0,"everydayActivePig":10000,"oneWithdrawals":1,"continueReward":100000,"sendSms":1,"createTime":1552966850679,"name":"试玩加成VIP","status":1,"useDay":31}
     */
    public int isGameTask;

    public MVipInfoBean mVipInfo;
    public GameBean game;

    public static class GameBean {
        /**
         * gameType : 0
         * orderId : 1
         * signinId : 0
         * lid : 0
         * icon : https://xianwanimgs.oss-cn-hangzhou.aliyuncs.com/201906/636971673628315250.jpg
         * typeName :
         * pageSize : 0
         * gameTitle : 极武尊-8期
         * ptype : 2
         * id : 3616
         * interfaceName : xw-Android
         * packageName : com.jwz.yy
         * taskStatus : 0
         * gameTag : 1
         * gameId : 7106
         * coins : 0
         * introduce : 一起战斗吧！
         * pageNum : 0
         * url : https://h5.51xianwan.com/try/try_cpl_plus.aspx?ptype=[ptype]&deviceid=[imei]&appid=3580&appsign=[userid]&adid=7106&keycode=[keycode]
         * tryTag : 0
         * enddate : 1565366399
         * gameGold : 3671
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
        public int id;
        public String interfaceName;
        public String packageName;
        public int taskStatus;
        public int gameTag;
        public int gameId;
        public int coins;
        public String introduce;
        public String url;
        public int tryTag;
        public long enddate;
        public double gameGold;
        public int interfaceId;
        public int status;
    }

    public static class MVipInfoBean {
        /**
         * coinToPigAddition : 10
         * isBuy : 0
         * orderId : 1
         * everydayReliefPig : 10000
         * pageSize : 0
         * backgroundImg : https://image.bzlyplay.com/pro_20190808115728805.png
         * everydayReliefPigTimes : 1
         * price : 0.01
         * firstReward : 100000
         * logo : https://image.baozhu8.com/test_20190319192747840.png
         * id : 10
         * everydayActiveCoin : 10000
         * isRenew : 1
         * taskNum : 0
         * continueRewardUnit : 1
         * isPay : 0
         * isTask : 1
         * firstRewardUnit : 1
         * auditFirst : 1
         * gameAddition : 40
         * onetimeLimit : 50000000
         * pageNum : 0
         * everydayActivePig : 10000
         * oneWithdrawals : 1
         * continueReward : 100000
         * sendSms : 1
         * createTime : 1552966850679
         * name : 试玩加成VIP
         * status : 1
         * useDay : 31
         */

        public String coinToPigAddition;        //金币兑换金猪加成
        public String gameAddition;             //游戏加成（%）
        public int isBuy;                    //是否购买0未购买其他购买
        public long everydayReliefPig;        //每日救济金猪
        public String backgroundImg;         //背景图
        public int everydayReliefPigTimes;   //救济金猪每日领取次数
        public String price;                 //价格
        public String logo;                  //logo
        public long everydayActiveCoin;      //每日活跃奖励金币
        public int isRenew;                  //是否可以续费1是2否
        public int taskNum;                  //
        public int onetimeLimit;             //单笔限额（金猪）
        public int isPay;                    //是否购买过
        public String name;                  //VIP卡名称
        public int sendSms;                  //兑换是否短信提醒（1-是 2-否）
        public long everydayActivePig;        //每日活跃奖励金猪
        public int oneWithdrawals;           //是否一元提现（1-是 2-否）
        public int auditFirst;               //兑换是否优先审核（1-是 2-否）
        public int isTask;                   //是否需要完成任务 1是2否
        public long firstReward;              //首充奖励
        public int firstRewardUnit;          //首充奖励单位（1-金币 2-金猪）
        public int continueReward;           //续充奖励
        public long continueRewardUnit;      //续充奖励单位（1-金币 2-金猪）
        public int useDay;                   //有效期（单位：天）
        public int id;                       //会员id
        public int highVip;                       //高额赚会员:1否,2是
    }
}
