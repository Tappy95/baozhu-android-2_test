package com.micang.baozhu.http.bean.home;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/10/24 19:01
 * @describe describe
 */
public class BaoquGamerewardinformation {
    /**
     * data : {"get_limit":2,"xyx_times_limit_second":"20","xyx_times_reward_coin":"300","xyx_day_reward_times":5}
     * message : 操作成功
     * statusCode : 2000
     * token : 7ba4d9d9f448b0e0e5f02b2998067cc8
     */

    /**
     * get_limit : 2
     * xyx_times_limit_second : 20
     * xyx_times_reward_coin : 300
     * xyx_day_reward_times : 5
     */

    public int get_limit;                       //是否可领（1-不可领 2-可领）
    public int xyx_times_limit_second;       //游戏时长(s)
    public String xyx_times_reward_coin;        //小游戏奖励金币数
    public int xyx_day_reward_times;            //小游戏奖励次数

}
