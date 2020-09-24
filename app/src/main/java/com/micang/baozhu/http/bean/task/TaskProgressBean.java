package com.micang.baozhu.http.bean.task;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/7/1 13:51
 * @describe describe
 */
public class TaskProgressBean {
    /**
     * data : {"lastPage":1,"navigatepageNums":[1],"startRow":0,"hasNextPage":false,"prePage":0,"nextPage":0,"endRow":1,"pageSize":2,"list":[{"reward":0.87,"pageSize":0,"remark":"","updateTime":0,"label":"很多金币的","pageNum":0,"expireTime":1561971577444,"name":"718斗地主app","logo":"https://image.bzlyplay.com/pro_20190701103626945.jpg","tpTaskId":17,"id":0,"status":1},{"reward":0.4,"pageSize":0,"remark":"","updateTime":0,"label":"高额回报,今天完成,可以的","pageNum":0,"expireTime":1561963052552,"name":"平安app观看任务","logo":"https://image.bzlyplay.com/pro_20190701100356924.png","tpTaskId":14,"id":0,"status":1}],"pageNum":1,"navigatePages":8,"navigateFirstPage":1,"total":2,"pages":1,"firstPage":1,"size":2,"isLastPage":true,"hasPreviousPage":false,"navigateLastPage":1,"isFirstPage":true}
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
     * endRow : 1
     * pageSize : 2
     * list : [{"reward":0.87,"pageSize":0,"remark":"","updateTime":0,"label":"很多金币的","pageNum":0,"expireTime":1561971577444,"name":"718斗地主app","logo":"https://image.bzlyplay.com/pro_20190701103626945.jpg","tpTaskId":17,"id":0,"status":1},{"reward":0.4,"pageSize":0,"remark":"","updateTime":0,"label":"高额回报,今天完成,可以的","pageNum":0,"expireTime":1561963052552,"name":"平安app观看任务","logo":"https://image.bzlyplay.com/pro_20190701100356924.png","tpTaskId":14,"id":0,"status":1}]
     * pageNum : 1
     * navigatePages : 8
     * navigateFirstPage : 1
     * total : 2
     * pages : 1
     * firstPage : 1
     * size : 2
     * isLastPage : true
     * hasPreviousPage : false
     * navigateLastPage : 1
     * isFirstPage : true
     */

    public List<ListBean> list;

    public static class ListBean {
        /**
         * reward : 0.87
         * pageSize : 0
         * remark :
         * updateTime : 0
         * label : 很多金币的
         * pageNum : 0
         * expireTime : 1561971577444
         * name : 718斗地主app
         * logo : https://image.bzlyplay.com/pro_20190701103626945.jpg
         * tpTaskId : 17
         * id : 0
         * status : 1
         */

        public String reward;
        public String remark;
        public String label;      //标签
        public long expireTime;
        public long orderTime;
        public String name;
        public String logo;
        public int tpTaskId;
        public int id;
        public int status;

    }
}
