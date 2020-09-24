package com.micang.baozhu.http.bean.home;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/10/10 15:55
 * @describe describe
 */
public class VideoCountBean {

    /**
     * data : {"res":3,"count":4,"time":1725555,"coin":500,"frequency":6}
     * message : 操作成功
     * statusCode : 2000
     * token : 7ba4d9d9f448b0e0e5f02b2998067cc8
     */

    /**
     * res : 3
     * count : 4
     * time : 1725555
     * coin : 500
     * frequency : 6
     */

    public int res;                 //1超过次数，2可以继续观看3，限制时间内
    public int count;               //用户观看次数
    public long time;                //倒计时毫秒值
    public int coin;                //奖励金币
    public int frequency;           //每天限制次数

}
