package com.micang.baozhu.http.bean.pay;

/*** @version 1.0
 * @Package com.dizoo.invite.http.bean.pay
 * @time 2019/3/25 20:29
 * @describe describe
 */
public class WithDrawBean {

    /**
     * data : {"balance":1045,"one_withdrawals":true}
     * message : 操作成功
     * statusCode : 2000
     * token : 4099ac907466a9a6be506560a0e84b20
     */

    /**
     * balance : 1045
     * one_withdrawals : true
     */
    public String balance;          //提现余额
    public boolean one_withdrawals; //是否可输入 true可以输入false不可输入
    public boolean isName;          //true有姓名。false没有姓名
    public boolean countWx;         //true可修改。false不可修改
    public int daren;               //	是否是达人：1是2不是
    public int openActivity;        //是否展示活跃度：1不展示2展示
    public String activityScore;       //到账金额百分比
    public String qualityScore;        //活跃度

}
