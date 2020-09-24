package com.micang.baozhu.http.bean;

/**
  * @version 1.0
 * @Package com.dizoo.invite.http.bean
 * @time 2019/4/25 19:14
 * @describe describe
 */
public class InformationRewardCoinBean {


    /**
     * get_limit : 1
     * coin_reward : 50
     * has_get : 900
     * stop_second : 15
     */

    public int get_limit;
    public String coin_reward;
    public String gg_coin_reward;//阅读广告
    public String zx_coin_reward;//资讯
    public String fs_coin_reward;//分享
    public String has_get;
    public int can_send; // 1-已经获取过奖励 2-未获取
    public int stop_second;

}
