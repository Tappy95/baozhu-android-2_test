package com.micang.baozhu.http.bean;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/12 19:12
 * @describe describe
 */
public class SignMakeChangeBean {
    /**
     * data : {"gameId":16665,"gameType":0,"coins":0,"introduce":"1局1红包，最大88元","lid":0,"icon":"http://pcdd-app.oss-cn-hangzhou.aliyuncs.com/advimg/20190517/2019051714041733958324.jpg","typeName":"","pageSize":0,"gameTitle":"麻小宝9期","pageNum":0,"url":"http://ifsapp.pceggs.com/Pages/IntegralWall/IW_Awall_adinfo.aspx?adid=16665&userid=userId&deviceid=imei&ptype=2&pid=11043&newversion=0","enddate":"1560988799","gameGold":12414.4,"id":502,"interfaceId":2,"interfaceName":"","packageName":"com.chiguagame.maxiaobao","taskStatus":0,"gameTag":1,"status":1}
     * message : 操作成功
     * statusCode : 2000
     * token : 7ba4d9d9f448b0e0e5f02b2998067cc8
     */


        /**
         * gameId : 16665
         * gameType : 0
         * coins : 0
         * introduce : 1局1红包，最大88元
         * lid : 0
         * icon : http://pcdd-app.oss-cn-hangzhou.aliyuncs.com/advimg/20190517/2019051714041733958324.jpg
         * typeName :
         * pageSize : 0
         * gameTitle : 麻小宝9期
         * pageNum : 0
         * url : http://ifsapp.pceggs.com/Pages/IntegralWall/IW_Awall_adinfo.aspx?adid=16665&userid=userId&deviceid=imei&ptype=2&pid=11043&newversion=0
         * enddate : 1560988799
         * gameGold : 12414.4
         * id : 502
         * interfaceId : 2
         * interfaceName :
         * packageName : com.chiguagame.maxiaobao
         * taskStatus : 0
         * gameTag : 1
         * status : 1
         */

        public int gameId;
        public int gameType;
        public int coins;
        public String introduce;
        public int lid;
        public String icon;
        public String typeName;
        public String gameTitle;
        public String url;
        public long enddate;
        public double gameGold;
        public int id;
        public int interfaceId;
        public String interfaceName;
        public String packageName;
        public int taskStatus;
        public int gameTag;
        public int status;

}
