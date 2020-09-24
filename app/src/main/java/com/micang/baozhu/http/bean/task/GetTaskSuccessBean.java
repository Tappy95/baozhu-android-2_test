package com.micang.baozhu.http.bean.task;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/29 16:44
 * @describe describe
 */
public class GetTaskSuccessBean {

    /**
     * data : {"accountId":10171,"expireTime":1560958840591,"createTime":1561797807887,"pageSize":0,"remark":"","tpTaskId":5,"updateTime":0,"id":36,"pageNum":0,"userId":"6f532b6288984bfd9ca54abafcb405ed","status":1}
     * message : 操作成功
     * statusCode : 2000
     * token : 7ba4d9d9f448b0e0e5f02b2998067cc8
     */

    /**
     * accountId : 10171
     * expireTime : 1560958840591
     * createTime : 1561797807887
     * pageSize : 0
     * remark :
     * tpTaskId : 5
     * updateTime : 0
     * id : 36
     * pageNum : 0
     * userId : 6f532b6288984bfd9ca54abafcb405ed
     * status : 1
     */

    public int accountId;
    public long expireTime; //过期时间
    public String remark;      //描述
    public int tpTaskId;    //任务id'
    public int id;
    public String userId;
    public int status;      //'状态（-2-已放弃 -1-已过期 1-待提交 2-已提交，待审核 3-审核通过 4-审核失败）
}
