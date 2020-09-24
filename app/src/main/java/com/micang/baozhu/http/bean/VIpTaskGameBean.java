package com.micang.baozhu.http.bean;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/8/8 19:36
 * @describe describe
 */
public class VIpTaskGameBean {
    /**
     * data : {"tpGame":{"gameType":0,"orderId":0,"signinId":0,"lid":0,"icon":"http://pcdd-app.oss-cn-hangzhou.aliyuncs.com/advimg/20190711/2019071109521156729993.jpg","typeName":"","pageSize":0,"gameTitle":"麦游斗地主20期","ptype":0,"id":3668,"interfaceName":"PCDD","packageName":"com.migame.ddz","taskStatus":0,"gameTag":1,"gameId":18867,"coins":0,"introduce":"每局都能赢红包的斗地主","pageNum":0,"url":"http://ifsapp.pceggs.com/Pages/IntegralWall/IW_Awall_addetail.aspx?adid=18867&userid=userId&deviceid=imei&ptype=2&pid=11043&newversion=0","tryTag":0,"enddate":"1565711999","gameGold":6073.6,"interfaceId":2,"status":1},"res":1}
     * message : 操作成功
     * statusCode : 2000
     * token : 7ba4d9d9f448b0e0e5f02b2998067cc8
     */


    /**
     * tpGame : {"gameType":0,"orderId":0,"signinId":0,"lid":0,"icon":"http://pcdd-app.oss-cn-hangzhou.aliyuncs.com/advimg/20190711/2019071109521156729993.jpg","typeName":"","pageSize":0,"gameTitle":"麦游斗地主20期","ptype":0,"id":3668,"interfaceName":"PCDD","packageName":"com.migame.ddz","taskStatus":0,"gameTag":1,"gameId":18867,"coins":0,"introduce":"每局都能赢红包的斗地主","pageNum":0,"url":"http://ifsapp.pceggs.com/Pages/IntegralWall/IW_Awall_addetail.aspx?adid=18867&userid=userId&deviceid=imei&ptype=2&pid=11043&newversion=0","tryTag":0,"enddate":"1565711999","gameGold":6073.6,"interfaceId":2,"status":1}
     * res : 1
     */

    public TpGameBean tpGame;


    public static class TpGameBean {
        /**
         * gameType : 0
         * orderId : 0
         * signinId : 0
         * lid : 0
         * icon : http://pcdd-app.oss-cn-hangzhou.aliyuncs.com/advimg/20190711/2019071109521156729993.jpg
         * typeName :
         * pageSize : 0
         * gameTitle : 麦游斗地主20期
         * ptype : 0
         * id : 3668
         * interfaceName : PCDD
         * packageName : com.migame.ddz
         * taskStatus : 0
         * gameTag : 1
         * gameId : 18867
         * coins : 0
         * introduce : 每局都能赢红包的斗地主
         * pageNum : 0
         * url : http://ifsapp.pceggs.com/Pages/IntegralWall/IW_Awall_addetail.aspx?adid=18867&userid=userId&deviceid=imei&ptype=2&pid=11043&newversion=0
         * tryTag : 0
         * enddate : 1565711999
         * gameGold : 6073.6
         * interfaceId : 2
         * status : 1
         */

        public int gameType;
        public String icon;
        public int gameId;
        public int orderId;
        public int signinId;
        public int lid;
        public String typeName;
        public String gameTitle;
        public int ptype;
        public int id;
        public String interfaceName;
        public String packageName;
        public int taskStatus;
        public int gameTag;
        public String introduce;
        public String url;
        public int tryTag;
        public long enddate;
        public double gameGold;
        public int interfaceId;
        public int status;
    }
}
