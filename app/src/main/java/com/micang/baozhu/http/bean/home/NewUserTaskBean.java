package com.micang.baozhu.http.bean.home;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/8/27 10:03
 * @describe describe
 */
public class NewUserTaskBean {

    /**
      * message : 操作成功
     * statusCode : 2000
     * token : c67325ff1c593249de4f052743cb46d3
     */

    /**
     * task_name : 参与5轮答题
     * reward : 500
     * create_time : 1553060654910
     * remark : 还可以邀请好友一起对战
     * id : 5
     * reward_unit : 1
     * sort : 13
     * task_type : 1
     * status : 1
     * icon_type : 1
     * isComplete : 0
     * fulfil_task_img : https://image.bzlyplay.com/pro_20190513172159758.png
     * task_img : https://image.bzlyplay.com/pro_20190513172156354.png
     * remarks : 如遇游戏页面等不能领取奖励请尝试刷新页面
     */

    public String task_name;            //任务名称
    public int reward;                  //奖励数量
    public int id;                      //id
    public int reward_unit;             //奖励数量单位（1-金币  3-金币百分比）
    public int isComplete;              //是否完成0未完成1完成
    public String task_img;             //任务图标
}
