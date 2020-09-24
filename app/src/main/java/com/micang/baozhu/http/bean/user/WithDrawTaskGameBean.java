package com.micang.baozhu.http.bean.user;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/8/21 20:07
 * @describe describe
 */
public class WithDrawTaskGameBean {

    /**
     * data : {"tpGame":{"gameType":0,"orderId":0,"signinId":0,"lid":0,"icon":"http://pcdd-app.oss-cn-hangzhou.aliyuncs.com/advimg/20190802/2019080214060280762640.jpg","typeName":"","pageSize":0,"gameTitle":"三米斗地主","ptype":0,"shortIntro":"","id":3839,"interfaceName":"PCDD","packageName":"com.yhxk.qpgame.yhkk","taskStatus":0,"gameTag":1,"gameId":19725,"coins":0,"introduce":"轻松赚取第一步","labelStr":"","pageNum":0,"url":"http://ifsapp.pceggs.com/Pages/IntegralWall/IW_Awall_addetail.aspx?adid=19725&userid=userId&deviceid=imei&ptype=2&pid=11043&newversion=0","tryTag":0,"cashStatus":0,"enddate":"1567439999","gameGold":13495.6,"typeId":0,"interfaceId":2,"status":1},"res":1}
     * message : 操作成功
     * statusCode : 2000
     * token : 4099ac907466a9a6be506560a0e84b20
     */

    /**
     * tpGame : {"gameType":0,"orderId":0,"signinId":0,"lid":0,"icon":"http://pcdd-app.oss-cn-hangzhou.aliyuncs.com/advimg/20190802/2019080214060280762640.jpg","typeName":"","pageSize":0,"gameTitle":"三米斗地主","ptype":0,"shortIntro":"","id":3839,"interfaceName":"PCDD","packageName":"com.yhxk.qpgame.yhkk","taskStatus":0,"gameTag":1,"gameId":19725,"coins":0,"introduce":"轻松赚取第一步","labelStr":"","pageNum":0,"url":"http://ifsapp.pceggs.com/Pages/IntegralWall/IW_Awall_addetail.aspx?adid=19725&userid=userId&deviceid=imei&ptype=2&pid=11043&newversion=0","tryTag":0,"cashStatus":0,"enddate":"1567439999","gameGold":13495.6,"typeId":0,"interfaceId":2,"status":1}
     * res : 1
     */

    public TpGameBean tpGame;
    public int res;             //1成功2任务异常3任务已完成

    public static class TpGameBean {
        /**
         * gameType : 0
         * orderId : 0
         * signinId : 0
         * lid : 0
         * icon : http://pcdd-app.oss-cn-hangzhou.aliyuncs.com/advimg/20190802/2019080214060280762640.jpg
         * typeName :
         * pageSize : 0
         * gameTitle : 三米斗地主
         * ptype : 0
         * shortIntro :
         * id : 3839
         * interfaceName : PCDD
         * packageName : com.yhxk.qpgame.yhkk
         * taskStatus : 0
         * gameTag : 1
         * gameId : 19725
         * coins : 0
         * introduce : 轻松赚取第一步
         * labelStr :
         * pageNum : 0
         * url : http://ifsapp.pceggs.com/Pages/IntegralWall/IW_Awall_addetail.aspx?adid=19725&userid=userId&deviceid=imei&ptype=2&pid=11043&newversion=0
         * tryTag : 0
         * cashStatus : 0
         * enddate : 1567439999
         * gameGold : 13495.6
         * typeId : 0
         * interfaceId : 2
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
        public int taskStatus;
        public int gameTag;
        public int gameId;
        public int coins;
        public String introduce;
        public String labelStr;
        public int pageNum;
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
