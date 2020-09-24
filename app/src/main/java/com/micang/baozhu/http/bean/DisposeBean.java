package com.micang.baozhu.http.bean;

/**
  * @version 1.0
 * @Package com.dizoo.invite.http.bean
 * @time 2019/4/25 15:53
 * @describe describe
 */
public class DisposeBean {

    /**
     * zx_coin_reward_limit : 5000
     * noviceTask : 1        	新手任务开关1打开2关闭
     * fuLiHui : https://engine.lvehaisen.com/index/activity?appKey=28B5JrMZ3PXR1oAXHnwqkzZsHM3N&adslotId=276978
     * recommendGame : 1        推荐游戏id
     * zx_stop_second : 15     奖励倒计时
     * zx_coin_reward : 50         将来金币数字
      */

    public String fuLiHui;
    public String h5_location_new;
    public String recommendGame;
    public PVersionBean pVersion;


    public static class PVersionBean {
        /**
         * openNoviceTask : 1
         * open28 : 1
         * createTime : 1557289449391
         * needUpdate : 2
         * updateUrl :
         * versionNo : 1.1.2
         * pageSize : 0
         * id : 2
         * pageNum : 0
         * "updateRemark": "",
         * channelCode : oppo
         * status : 1
         */
        public int needUpdate;          //1,更新, 2,否
        public String updateUrl;         //更新的下载链接
        public String versionNo;
        public String channelCode;
        public String updateRemark;       //更新描述

    }
}
