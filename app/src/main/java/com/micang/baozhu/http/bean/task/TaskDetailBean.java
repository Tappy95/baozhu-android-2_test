package com.micang.baozhu.http.bean.task;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/26 17:46
 * @describe describe
 */
public class TaskDetailBean implements Serializable {
    /**
     * data : {"taskSubmit":[{"createTime":1561521012302,"name":"截图","pageSize":0,"id":12,"type":2,"pageNum":0,"taskId":7,"paceholder":""},{"createTime":1561520987061,"name":"手机号","pageSize":0,"id":11,"type":1,"pageNum":0,"taskId":7,"paceholder":""},{"createTime":1561520978511,"name":"姓名","pageSize":0,"id":10,"type":1,"pageNum":0,"taskId":7,"paceholder":""}],"taskInfo":{"channel":"","typeName":"","pageSize":0,"isSignin":0,"remind":"ssssssssss","fulfilTime":1,"orderTime":1561564800000,"isUpper":1,"logo":"https://image.bzlyplay.com/pro_2019062611473281.jpg","id":7,"state":2,"explains":"ssssssssssssssss","examineTime":1561520886613,"timeUnit":1,"reward":10,"channelExplain":"ggz123","surplusTaskNumber":100,"taskChannel":"baozhu","label":"测试,测试赛","surplusMax":0,"pageNum":0,"unit":"","createTime":1561520886613,"pageIndex":0,"taskUrl":"http://192.168.1.27:3030/#/publish-task-list?id=65","surplusChannelTaskNumber":100,"name":"签到任务","channelTaskNumber":100,"typeId":1,"serviceRate":20,"taskNumber":100,"isOrder":1,"remarks":"","surplusMin":0},"taskStep":[{"stepType":1,"uploadUrl":"","createTime":1561520904168,"isMust":0,"pageSize":0,"step":1,"id":12,"isUpload":2,"pageNum":0,"content":"点击签到","url":"","taskId":7},{"stepType":2,"uploadUrl":"","createTime":1561520920260,"isMust":1,"pageSize":0,"step":2,"id":13,"isUpload":2,"pageNum":0,"content":"签到截图如下","url":"https://image.bzlyplay.com/pro_20190626114831804.jpg","taskId":7},{"stepType":4,"uploadUrl":"","createTime":1561520959932,"isMust":0,"pageSize":0,"step":3,"id":15,"isUpload":2,"pageNum":0,"content":"点击链接","url":"","taskId":7},{"stepType":1,"uploadUrl":"","createTime":1561520946537,"isMust":0,"pageSize":0,"step":4,"id":14,"isUpload":2,"pageNum":0,"content":"提交证明材料","url":"","taskId":7}]}
     * message : 操作成功
     * statusCode : 2000
     */


    /**
     * taskSubmit : [{"createTime":1561521012302,"name":"截图","pageSize":0,"id":12,"type":2,"pageNum":0,"taskId":7,"paceholder":""},{"createTime":1561520987061,"name":"手机号","pageSize":0,"id":11,"type":1,"pageNum":0,"taskId":7,"paceholder":""},{"createTime":1561520978511,"name":"姓名","pageSize":0,"id":10,"type":1,"pageNum":0,"taskId":7,"paceholder":""}]
     * taskInfo : {"channel":"","typeName":"","pageSize":0,"isSignin":0,"remind":"ssssssssss","fulfilTime":1,"orderTime":1561564800000,"isUpper":1,"logo":"https://image.bzlyplay.com/pro_2019062611473281.jpg","id":7,"state":2,"explains":"ssssssssssssssss","examineTime":1561520886613,"timeUnit":1,"reward":10,"channelExplain":"ggz123","surplusTaskNumber":100,"taskChannel":"baozhu","label":"测试,测试赛","surplusMax":0,"pageNum":0,"unit":"","createTime":1561520886613,"pageIndex":0,"taskUrl":"http://192.168.1.27:3030/#/publish-task-list?id=65","surplusChannelTaskNumber":100,"name":"签到任务","channelTaskNumber":100,"typeId":1,"serviceRate":20,"taskNumber":100,"isOrder":1,"remarks":"","surplusMin":0}
     * taskStep : [{"stepType":1,"uploadUrl":"","createTime":1561520904168,"isMust":0,"pageSize":0,"step":1,"id":12,"isUpload":2,"pageNum":0,"content":"点击签到","url":"","taskId":7},{"stepType":2,"uploadUrl":"","createTime":1561520920260,"isMust":1,"pageSize":0,"step":2,"id":13,"isUpload":2,"pageNum":0,"content":"签到截图如下","url":"https://image.bzlyplay.com/pro_20190626114831804.jpg","taskId":7},{"stepType":4,"uploadUrl":"","createTime":1561520959932,"isMust":0,"pageSize":0,"step":3,"id":15,"isUpload":2,"pageNum":0,"content":"点击链接","url":"","taskId":7},{"stepType":1,"uploadUrl":"","createTime":1561520946537,"isMust":0,"pageSize":0,"step":4,"id":14,"isUpload":2,"pageNum":0,"content":"提交证明材料","url":"","taskId":7}]
     */

    public TaskInfoBean taskInfo;
    public List<TaskSubmitBean> taskSubmit;
    public List<TaskStepBean> taskStep;
    public LUserTptaskBean lUserTptask;

    public static class TaskInfoBean {
        /**
         * channel :
         * typeName :
         * pageSize : 0
         * isSignin : 0
         * remind : ssssssssss
         * fulfilTime : 1
         * orderTime : 1561564800000
         * isUpper : 1
         * logo : https://image.bzlyplay.com/pro_2019062611473281.jpg
         * id : 7
         * state : 2
         * explains : ssssssssssssssss
         * examineTime : 1561520886613
         * timeUnit : 1
         * reward : 10
         * channelExplain : ggz123
         * surplusTaskNumber : 100
         * taskChannel : baozhu
         * label : 测试,测试赛
         * surplusMax : 0
         * pageNum : 0
         * unit :
         * createTime : 1561520886613
         * pageIndex : 0
         * taskUrl : http://192.168.1.27:3030/#/publish-task-list?id=65
         * surplusChannelTaskNumber : 100
         * name : 签到任务
         * channelTaskNumber : 100
         * typeId : 1
         * serviceRate : 20
         * taskNumber : 100
         * isOrder : 1
         * remarks :
         * surplusMin : 0
         */

        public String channel;
        public String typeName;
        public int isSignin;        //是否是签到赚任务1是2否
        public String remind;       //温馨提示
        public int fulfilTime;      //完成时间
        public long orderTime;
        public int isUpper;
        public String logo;         //任务logo
        public int id;
        public int state;           //审核状态1审核中2通过3拒绝
        public String explains;     //任务说明
        public long examineTime;            //审核时间
        public int timeUnit;            //时间单位1天2小时3分钟
        public String reward;           //奖励(元)
        public String channelExplain;
        public int surplusTaskNumber;
        public String taskChannel;
        public int surplusMax;
        public String unit;
        public String taskUrl;         //任务链接
        public String name;            //任务名称
        public int channelTaskNumber;
        public int typeId;              //任务类型
        public int taskNumber;         //任务数量
        public int isOrder;
        public String remarks;
        public int surplusMin;
    }

    public static class TaskSubmitBean implements MultiItemEntity {
        /**
         * createTime : 1561521012302
         * name : 截图
         * pageSize : 0
         * id : 12
         * type : 2                 //输入框类型1文本2文件上传
         * pageNum : 0
         * taskId : 7
         * paceholder :
         */

        public String name;            //输入框内容
        public String id;
        public int type;                //输入框类型1文本2文件上传
        public int taskId;
        public String paceholder;       //提示内容

        @Override
        public int getItemType() {
            if (type == 1) {
                return 1;
            }
            if (type == 2) {
                return 2;
            }
            return 0;
        }
    }

    public static class LUserTptaskBean {
        /**
         * accountId : 10171
         * expireTime : 1561963084459           过期时间
         * createTime : 1561790284459
         * pageSize : 0
         * remark :
         * tpTaskId : 10                '任务id
         * updateTime : 0
         * id : 34
         * pageNum : 0
         * userId : 6f532b6288984bfd9ca54abafcb405ed
         * status : 1  状态（-2-已放弃 -1-已过期 1-待提交 2-已提交，待审核 3-审核通过 4-审核失败）',
         */

        public int accountId;
        public long expireTime;        // 过期时间
         public String remark;          //描述
        public int tpTaskId;            //任务id
        public int id;
        public String userId;
        public int status;              //状态（-2-已放弃 -1-已过期 1-待提交 2-已提交，待审核 3-审核通过 4-审核失败
    }

    public static class TaskStepBean {
        /**
         * stepType : 1
         * uploadUrl :
         * createTime : 1561520904168
         * isMust : 0
         * pageSize : 0
         * step : 1
         * id : 12
         * isUpload : 2
         * pageNum : 0
         * content : 点击签到
         * url :
         * taskId : 7
         */

        public int stepType;                //类型:1文字,2文字+二维码3,文字+长图 4文字+链接
        public String uploadUrl;            //上传图片的url地址，多张逗号分隔
        public int isMust;                 ////是否必须上传图片1是2否
        public int step;                   //步骤
        public int id;
        public int isUpload;                //是否有上传入口1有2没有
        public String content;              //提示内容
        public String url;                  //图片链接.逗号分隔
        public int taskId;                  //任务id
    }

}
