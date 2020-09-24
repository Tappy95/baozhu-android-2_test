package com.micang.baozhu.http.bean.home;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/8/22 16:27
 * @describe describe
 */
public class EverydayRedTaskinfoBean {

    /**
     * data : {"taskCoin":"5000","res":1,"isTask":1,"isRemind":0,"stickTimes":2,"isSign":2,"sumCoin":300}
     * message : 操作成功
     * statusCode : 2000
     * token : 4099ac907466a9a6be506560a0e84b20
     */
    /**
     * taskCoin : 5000
     * res : 1
     * isTask : 1
     * isRemind : 0
     * stickTimes : 2
     * isSign : 2
     * sumCoin : 300
     */

    public String taskCoin;             //任务额外奖励
    public int res;                     //是否已匹配游戏 1没有匹配游戏2匹配了游戏
    public int isTask;                  //是否领取了任务奖励1未领取2领取
    public int isRemind;                //0不提醒1提醒
    public int stickTimes;              //连续签到天数
    public int isSign;                  //今天是否签到 1今天未签到2今天已签到
    public int sumCoin;                 //累计获取金币数
    public TpGameBean tpGame;

    public static class TpGameBean {
        /**
         * gameType : 0
         * orderId : 0
         * signinId : 0
         * lid : 0
         * icon : https://xianwanimgs.oss-cn-hangzhou.aliyuncs.com/201907/636994998192690670.jpg
         * typeName :
         * pageSize : 0
         * gameTitle : 真龙传奇-2期
         * ptype : 0
         * shortIntro :
         * id : 3455
         * interfaceName : xw-Android
         * packageName : com.fastfinger.zlcq.yy
         * taskStatus : 0
         * gameTag : 1
         * gameId : 7324
         * coins : 0
         * introduce : 苍穹之巅，剑锋凌云！
         * labelStr :
         * pageNum : 0
         * url : https://h5.51xianwan.com/try/try_cpl_plus.aspx?ptype=[ptype]&deviceid=[imei]&appid=3580&appsign=[userid]&adid=7324&keycode=[keycode]
         * tryTag : 0
         * cashStatus : 0
         * enddate : 1567267199
         * gameGold : 2507
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
        public String labelStr;
        public String url;
        public int tryTag;
        public int cashStatus;
        public long enddate;
        public double gameGold;
        public int typeId;
        public int interfaceId;
        public int status;

    }
}
