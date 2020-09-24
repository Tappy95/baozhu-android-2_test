package com.micang.baozhu.http.bean;

/**
  * @version 1.0
 * @Package com.dizoo.invite.http
 * @time 2019/5/11 17:31
 * @describe describe
 */
public class BannerBean {

    /**
     * data : [{"bannerId":24,"createTime":1556511013988,"imageUrl":"https://image.bzlyplay.com/pro_20190429121009919.jpg","linkUrl":"test","pageSize":0,"startTime":1556510860000,"endTime":1588780800000,"position":1,"title":"抽奖banner","pageNum":0,"status":1}]
     * message : 操作成功
     * statusCode : 2000
     */


        /**
         * bannerId : 24
         * createTime : 1556511013988
         * imageUrl : https://image.bzlyplay.com/pro_20190429121009919.jpg
         * linkUrl : test
         * pageSize : 0
         * startTime : 1556510860000
         * endTime : 1588780800000
         * position : 1
         * title : 抽奖banner
         * pageNum : 0
         * status : 1
         */

        public int bannerId;
        public long createTime;
        public String imageUrl;
        public String linkUrl;
        public int pageSize;
        public long startTime;
        public long endTime;
        public int position;
        public String title;
        public int pageNum;
        public int status;

}
