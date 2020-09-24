package com.micang.baozhu.http.bean.home;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/8/22 20:16
 * @describe describe
 */
public class EverydayRedGameBean {

    /**
     * data : {"tpGame":{"gameType":0,"orderId":0,"signinId":0,"lid":0,"icon":"https://xianwanimgs.oss-cn-hangzhou.aliyuncs.com/201907/636994998192690670.jpg","typeName":"","pageSize":0,"gameTitle":"真龙传奇-2期","ptype":0,"shortIntro":"","id":3455,"interfaceName":"xw-Android","packageName":"com.fastfinger.zlcq.yy","taskStatus":0,"gameTag":1,"gameId":7324,"coins":0,"introduce":"苍穹之巅，剑锋凌云！","labelStr":"","pageNum":0,"url":"https://h5.51xianwan.com/try/try_cpl_plus.aspx?ptype=[ptype]&deviceid=[imei]&appid=3580&appsign=[userid]&adid=7324&keycode=[keycode]","tryTag":0,"cashStatus":0,"enddate":"1567267199","gameGold":2507,"typeId":0,"interfaceId":3,"status":1},"res":1}
     * message : 操作成功
     * statusCode : 2000
     * token : 25eb7c095f841dbb6d996734e2d2e9e8
     */


    /**
     * tpGame : {"gameType":0,"orderId":0,"signinId":0,"lid":0,"icon":"https://xianwanimgs.oss-cn-hangzhou.aliyuncs.com/201907/636994998192690670.jpg","typeName":"","pageSize":0,"gameTitle":"真龙传奇-2期","ptype":0,"shortIntro":"","id":3455,"interfaceName":"xw-Android","packageName":"com.fastfinger.zlcq.yy","taskStatus":0,"gameTag":1,"gameId":7324,"coins":0,"introduce":"苍穹之巅，剑锋凌云！","labelStr":"","pageNum":0,"url":"https://h5.51xianwan.com/try/try_cpl_plus.aspx?ptype=[ptype]&deviceid=[imei]&appid=3580&appsign=[userid]&adid=7324&keycode=[keycode]","tryTag":0,"cashStatus":0,"enddate":"1567267199","gameGold":2507,"typeId":0,"interfaceId":3,"status":1}
     * res : 1
     */

    public TpGameBean tpGame;
    public int res;

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
