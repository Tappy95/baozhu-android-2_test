package com.micang.baozhu.http.bean.home;

import java.util.List;

/**
 * @version 1.0
 * @Package com.dizoo.invite.http.bean.home
 * @time 2019/4/8 15:12
 * @describe describe
 */
public class GameCompanyBean {
    /**
     * data : {"lastPage":1,"navigatepageNums":[1],"startRow":1,"hasNextPage":false,"prePage":0,"nextPage":0,"endRow":1,"pageSize":20,"list":[{"createTime":1554707085545,"imageUrl":"https://image.baozhu8.com/pro_20190408150820280.png","name":"PC蛋蛋游戏","pageSize":0,"remark":"PC蛋蛋游戏PC蛋蛋游戏PC蛋蛋游戏","id":7,"shortName":"PCDD","h5Type":2,"pageNum":0,"status":1}],"pageNum":1,"navigatePages":8,"navigateFirstPage":1,"total":1,"pages":1,"firstPage":1,"size":1,"isLastPage":true,"hasPreviousPage":false,"navigateLastPage":1,"isFirstPage":true}
     * message : 操作成功
     * statusCode : 2000
     * token : 4099ac907466a9a6be506560a0e84b20
     */

    /**
     * lastPage : 1
     * navigatepageNums : [1]
     * startRow : 1
     * hasNextPage : false
     * prePage : 0
     * nextPage : 0
     * endRow : 1
     * pageSize : 20
     * list : [{"createTime":1554707085545,"imageUrl":"https://image.baozhu8.com/pro_20190408150820280.png","name":"PC蛋蛋游戏","pageSize":0,"remark":"PC蛋蛋游戏PC蛋蛋游戏PC蛋蛋游戏","id":7,"shortName":"PCDD","h5Type":2,"pageNum":0,"status":1}]
     * pageNum : 1
     * navigatePages : 8
     * navigateFirstPage : 1
     * total : 1
     * pages : 1
     * firstPage : 1
     * size : 1
     * isLastPage : true
     * hasPreviousPage : false
     * navigateLastPage : 1
     * isFirstPage : true
     */


    public int pageSize;
    public int pageNum;

    public List<ListBean> list;

    public static class ListBean {
        /**
         * createTime : 1554707085545
         * imageUrl : https://image.baozhu8.com/pro_20190408150820280.png
         * name : PC蛋蛋游戏
         * pageSize : 0
         * remark : PC蛋蛋游戏PC蛋蛋游戏PC蛋蛋游戏
         * id : 7
         * shortName : PCDD
         * h5Type : 2
         * pageNum : 0
         * status : 1
         */

        public long createTime;
        public String imageUrl;
        public String name;
        public int pageSize;
        public String remark;
        public String id;
        public String shortName;
        public int h5Type;
        public int pageNum;
        public int status;

    }
}
