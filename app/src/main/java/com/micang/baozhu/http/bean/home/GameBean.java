package com.micang.baozhu.http.bean.home;

import java.io.Serializable;
import java.util.List;

/**
 * @version 1.0
 * @Package com.dizoo.invite.http.bean.home_item_1_bg
 * @time 2019/3/19 20:34
 * @describe describe
 */
public class GameBean implements Serializable {

    /**
     * data : {"lastPage":1,"navigatepageNums":[1],"startRow":1,"hasNextPage":false,"prePage":0,"nextPage":0,"endRow":1,"pageSize":20,"list":[{"gameId":90,"gameType":0,"coins":100000,"introduce":"1)欢乐红包场每局赢红包券，红包券可兑换1元、10元、30元红包；\r\n2)新手免费送1元红包，领满注册送+新手7天登录奖红包券立即兑换；\r\n3)完成所有每日任务送960万金币，完成所有每周任务送6600万金币；","icon":"https://www.yolebox.com/upload/1534477237479ray3kkoi6af4hd17vuryi7pehnkvs8cqogkh20yj.png","typeName":"棋牌","pageSize":0,"gameTitle":"麦游斗地主6期","pageNum":0,"url":"http://www.yolebox.com/wap/android/detail.jsp?gameid=90&chid=1&devicecode=[imei]&apid=[appid]&sign=[sign]","enddate":"20190328000000","gameGold":7212.25,"id":47,"interfaceId":1}],"pageNum":1,"navigatePages":8,"navigateFirstPage":1,"total":1,"pages":1,"firstPage":1,"size":1,"isLastPage":true,"hasPreviousPage":false,"navigateLastPage":1,"isFirstPage":true}
     * message : 操作成功
     * statusCode : 2000
     * token : 106cb0c3bc97035a187012a098b7158f
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
     * list : [{"gameId":90,"gameType":0,"coins":100000,"introduce":"1)欢乐红包场每局赢红包券，红包券可兑换1元、10元、30元红包；\r\n2)新手免费送1元红包，领满注册送+新手7天登录奖红包券立即兑换；\r\n3)完成所有每日任务送960万金币，完成所有每周任务送6600万金币；","icon":"https://www.yolebox.com/upload/1534477237479ray3kkoi6af4hd17vuryi7pehnkvs8cqogkh20yj.png","typeName":"棋牌","pageSize":0,"gameTitle":"麦游斗地主6期","pageNum":0,"url":"http://www.yolebox.com/wap/android/detail.jsp?gameid=90&chid=1&devicecode=[imei]&apid=[appid]&sign=[sign]","enddate":"20190328000000","gameGold":7212.25,"id":47,"interfaceId":1}]
     * pageNum : 1
     * navigatePages : 8
     * navigateFirstPage : 1
     * total : 1
     * pages : 1
     * <p>
     * firstPage : 1
     * size : 1
     * isLastPage : true
     * hasPreviousPage : false
     * navigateLastPage : 1
     * isFirstPage : true
     */

    public List<ListBean> list;

    public static class ListBean implements Serializable {
        /**
         * gameId : 90
         * gameType : 0
         * coins : 100000
         * introduce : 1)欢乐红包场每局赢红包券，红包券可兑换1元、10元、30元红包；
         * 2)新手免费送1元红包，领满注册送+新手7天登录奖红包券立即兑换；
         * 3)完成所有每日任务送960万金币，完成所有每周任务送6600万金币；
         * icon : https://www.yolebox.com/upload/1534477237479ray3kkoi6af4hd17vuryi7pehnkvs8cqogkh20yj.png
         * typeName : 棋牌
         * pageSize : 0
         * gameTitle : 麦游斗地主6期
         * pageNum : 0
         * url : http://www.yolebox.com/wap/android/detail.jsp?gameid=90&chid=1&devicecode=[imei]&apid=[appid]&sign=[sign]
         * enddate : 20190328000000
         * gameGold : 7212.25
         * id : 47
         * interfaceId : 1
         * shortIntro :冲10返13
         * labelStr: 标签, 高返 简单
         */

        public int gameId;
        public int gameType;
        public int coins;
        public String introduce;
        public String icon;
        public String typeName;
        public int signinId;
        public int pageSize;
        public int tryTag;
        public String gameTitle;
        public int pageNum;
        public String url;
        public String enddate; //截止日期
        public String gameGold;
        public String id;
        public String lid;
        public int interfaceId;
        public String interfaceName;
        public String shortIntro;
        public String labelStr;
    }

}
