package com.micang.baozhu.http.bean;

import java.io.Serializable;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/5/30 16:01
 * @describe describe
 */
public class ChangeBean implements Serializable {
    /**
     * data : {"gameId":16220,"gameType":0,"coins":10000,"introduce":"新玩法赢大奖","lid":0,"icon":"http://pcdd-app.oss-cn-hangzhou.aliyuncs.com/advimg/20190505/2019050513530553130881.jpg","typeName":"","pageSize":0,"gameTitle":"热血飞行棋2期","pageNum":0,"url":"http://ifsapp.pceggs.com/Pages/IntegralWall/IW_Awall_adinfo.aspx?adid=16220&userid=userId&deviceid=imei&ptype=2&pid=11043&newversion=0","enddate":"1559779199","gameGold":14923.6,"id":473,"interfaceId":2,"interfaceName":"PCDD","packageName":"com.ouryoo.app.game","gameTag":1,"status":1}
     * message : 操作成功
     * statusCode : 2000
     * token : 4099ac907466a9a6be506560a0e84b20
     */


        /**
         * gameId : 16220
         * gameType : 0
         * coins : 10000
         * introduce : 新玩法赢大奖
         * lid : 0
         * icon : http://pcdd-app.oss-cn-hangzhou.aliyuncs.com/advimg/20190505/2019050513530553130881.jpg
         * typeName :
         * pageSize : 0
         * gameTitle : 热血飞行棋2期
         * pageNum : 0
         * url : http://ifsapp.pceggs.com/Pages/IntegralWall/IW_Awall_adinfo.aspx?adid=16220&userid=userId&deviceid=imei&ptype=2&pid=11043&newversion=0
         * enddate : 1559779199
         * gameGold : 14923.6
         * id : 473
         * interfaceId : 2
         * interfaceName : PCDD
         * packageName : com.ouryoo.app.game
         * gameTag : 1
         * status : 1
         */

        public String gameId;
        public int gameType;
        public int coins;
        public String introduce;
        public String lid;
        public String icon;
        public String typeName;
        public int pageSize;
        public String gameTitle;
        public int pageNum;
        public String url;
        public String enddate;
        public double gameGold;
        public String id;
        public int interfaceId;
        public String interfaceName;
        public String packageName;
        public int gameTag;
        public int status;

}
