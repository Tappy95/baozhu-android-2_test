package com.micang.baozhu.http.bean.earlyclock;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/10/22 17:41
 * @describe describe
 */
public class EarlyClockinfo {


    /**
     * data : {"number":2375,"checkinResult":{"createTime":0,"actualBonus":0,"pageSize":0,"successNumber":32,"id":0,"failNumber":20,"pageNum":0,"bonusPool":520000,"createDate":"10月20日"},"dates":"明日","coin":23750000,"checkinType":2}
     * message : 操作成功
     * statusCode : 2000
     * token : 7ba4d9d9f448b0e0e5f02b2998067cc8
     */


    /**
     * number : 2375
     * checkinResult : {"createTime":0,"actualBonus":0,"pageSize":0,"successNumber":32,"id":0,"failNumber":20,"pageNum":0,"bonusPool":520000,"createDate":"10月20日"}
     * dates : 明日
     * coin : 23750000
     * checkinType : 2
     */

    public String number;                      //人数
    public CheckinResultBean checkinResult; //打卡结果bean
    public String dates;                    //今日明日
    public String coin;                        //奖金数
    public int checkinType;                 //打卡状态：
    // 1.已报名打卡，
    // 2.可报名明天打卡，
    // 3.已报名，当前时间不能打卡
    // 4.已报名，当前时间可以打卡
    // 5.已报名，当前时间可以补卡
    // 6.已报名，今日打卡失败,有弹框,可报名下次打卡
    // 7.已报名，今日打卡失败,没弹框,可报名下次打卡
    // 8.当前时间不能报名

    public long countdown;                   //倒计时

    public static class CheckinResultBean {
        /**
         * createTime : 0
         * actualBonus : 0
         * pageSize : 0
         * successNumber : 32
         * id : 0
         * failNumber : 20
         * pageNum : 0
         * bonusPool : 520000
         * createDate : 10月20日
         */

        public String successNumber;       //成功人数
        public String failNumber;          //失败人数
        public long bonusPool;          //奖金池
        public String createDate;       //日期

    }
}
