package com.micang.baozhu.http.bean.home;

/**
 * @version 1.0
 * @Package com.dizoo.http.bean.home_item_1_bg
 * @time 2019/3/3 16:02
 * @describe describe
 */
public class SignBean {
    /**
     * data : {"score":100022,"res":3,"stickTimes":1}
     * message : 操作成功
     * statusCode : 2000
     * token : ce3aa35d5f7265299739c82255caa0f2
     */
    /**
     * score : 100022
     * res : 3
     * stickTimes : 1
     */
    public String score;
    public int res;  //签到标志  0,失败 1,成功 ,3,已签到
    public int stickTimes; //连续签到天数

}
