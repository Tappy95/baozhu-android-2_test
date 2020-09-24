package com.micang.baozhu.http.bean.task;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/25 20:09
 * @describe describe
 */
public class TaskBean {
    /**
     * data : {"lastPage":1,"navigatepageNums":[1],"startRow":0,"hasNextPage":false,"prePage":0,"nextPage":0,"endRow":2,"pageSize":3,"list":[{"reward":0.01,"pageSize":0,"taskChannel":"1","label":"1","pageNum":0,"isSignin":1,"createTime":1,"isUpper":1,"surplusChannelTaskNumber":1,"name":"1","channelTaskNumber":1,"logo":"1","typeId":1,"id":1},{"reward":0.01,"pageSize":0,"taskChannel":"1","label":"1","pageNum":0,"isSignin":1,"createTime":1,"isUpper":1,"surplusChannelTaskNumber":1,"name":"1","channelTaskNumber":1,"logo":"1","typeId":1,"id":2},{"reward":0.01,"pageSize":0,"taskChannel":"1","label":"1","pageNum":0,"isSignin":1,"createTime":1,"isUpper":1,"surplusChannelTaskNumber":1,"name":"1","channelTaskNumber":1,"logo":"1","typeId":1,"id":3}],"pageNum":1,"navigatePages":8,"navigateFirstPage":1,"total":3,"pages":1,"firstPage":1,"size":3,"isLastPage":true,"hasPreviousPage":false,"navigateLastPage":1,"isFirstPage":true}
     * message : 操作成功
     * statusCode : 2000
     * token : 7ba4d9d9f448b0e0e5f02b2998067cc8
     */


    /**
     * lastPage : 1
     * navigatepageNums : [1]
     * startRow : 0
     * hasNextPage : false
     * prePage : 0
     * nextPage : 0
     * endRow : 2
     * pageSize : 3
     * list : [{"reward":0.01,"pageSize":0,"taskChannel":"1","label":"1","pageNum":0,"isSignin":1,"createTime":1,"isUpper":1,"surplusChannelTaskNumber":1,"name":"1","channelTaskNumber":1,"logo":"1","typeId":1,"id":1},{"reward":0.01,"pageSize":0,"taskChannel":"1","label":"1","pageNum":0,"isSignin":1,"createTime":1,"isUpper":1,"surplusChannelTaskNumber":1,"name":"1","channelTaskNumber":1,"logo":"1","typeId":1,"id":2},{"reward":0.01,"pageSize":0,"taskChannel":"1","label":"1","pageNum":0,"isSignin":1,"createTime":1,"isUpper":1,"surplusChannelTaskNumber":1,"name":"1","channelTaskNumber":1,"logo":"1","typeId":1,"id":3}]
     * pageNum : 1
     * navigatePages : 8
     * navigateFirstPage : 1
     * total : 3
     * pages : 1
     * firstPage : 1
     * size : 3
     * isLastPage : true
     * hasPreviousPage : false
     * navigateLastPage : 1
     * isFirstPage : true
     */

    public List<ListBean> list;

    public static class ListBean {
        /**
         "reward": 0,
         "pageSize": 0,
         "taskChannel": "llzllz7485",
         "label": "",
         "pageNum": 0,
         "userId": "",
         "isSignin": 2,
         "fulfilTime": 4,
         "orderTime": 1563292800000,
         "createTime": 1561951510029,
         "isUpper": 3,
         "surplusChannelTaskNumber": 4,
         "name": "RRR",
         "channelTaskNumber": 4,
         "logo": "https://image.bzlyplay.com/pro_20190701112445946.jpg",
         "typeId": 3,
         "id": 19,
         "isOrder": 1,
         "status": 0,
         "timeUnit": 2
         */

        public double reward;
        public double drReward;     //达人奖励
        public String taskChannel;
        public String label;
        public int pageNum;
        public int isSignin;
        public int isUpper;
        public int isOrder;
        public long orderTime;
        public int surplusChannelTaskNumber;
        public String name;
        public int status; //为null,没有预约
        public int channelTaskNumber;
        public String logo;
        public int typeId;
        public int id;
    }

}
