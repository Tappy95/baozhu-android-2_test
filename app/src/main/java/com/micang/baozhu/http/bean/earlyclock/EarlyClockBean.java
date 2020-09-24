package com.micang.baozhu.http.bean.earlyclock;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/10/23 10:56
 * @describe describe
 */
public class EarlyClockBean {

    public int coin;                        //奖金数
    public int res;                            //打卡状态：1未到打卡时间，
                                                        // 2你未参与今日的打卡活动，
                                                        //3请勿重复参与打卡，
                                                        // 4打卡成功，
                                                        //5没有补签卡，无法补签，
                                                        // 6补签成功

}
