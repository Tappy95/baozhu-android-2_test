package com.micang.baozhu.http.bean.home;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/7/18 19:05
 * @describe describe
 */
public class NoticeBean {

    /**
     * data : {"isNotice":2,"appNewsNotice":{"releaserTime":1563379200000,"ranges":1,"isRead":0,"pageSize":0,"linkAddress":"","noticeType":2,"isPublish":1,"pageNum":0,"userId":"","createrName":"","noticeTitle":"Arm与联通合作出现新进展：成功部署物联网平台","isRelease":1,"imgUrl":"https://image.bzlyplay.com/pro_20190718175458692.jpg","noticeContent":"Arm与联通合作出现新进展：中关村在线消息：7月18日消息，今天上午中国联通与Arm公司在北京召开了Arm＋中国联通物联网平台部署成功发布会。此次发布会上，中国联通和Arm不仅分享了双方合作的新进展，还公布了双方部成功署的物联网设备管理平台解决方案。\n\n","createrTime":1563443704750,"cancelTime":1563552000000,"id":11,"createrMobile":"18268890930"}}
     * message : 操作成功
     * statusCode : 2000
     * token : 7ba4d9d9f448b0e0e5f02b2998067cc8
     */

    /**
     * isNotice : 2
     * appNewsNotice : {"releaserTime":1563379200000,"ranges":1,"isRead":0,"pageSize":0,"linkAddress":"","noticeType":2,"isPublish":1,"pageNum":0,"userId":"","createrName":"","noticeTitle":"Arm与联通合作出现新进展：成功部署物联网平台","isRelease":1,"imgUrl":"https://image.bzlyplay.com/pro_20190718175458692.jpg","noticeContent":"Arm与联通合作出现新进展：中关村在线消息：7月18日消息，今天上午中国联通与Arm公司在北京召开了Arm＋中国联通物联网平台部署成功发布会。此次发布会上，中国联通和Arm不仅分享了双方合作的新进展，还公布了双方部成功署的物联网设备管理平台解决方案。\n\n","createrTime":1563443704750,"cancelTime":1563552000000,"id":11,"createrMobile":"18268890930"}
     */

    public int isNotice;                    //是否有公告1没有2有
    public AppNewsNoticeBean appNewsNotice;

    public static class AppNewsNoticeBean {
        /**
         * releaserTime : 1563379200000
         * ranges : 1
         * isRead : 0
         * pageSize : 0
         * linkAddress :
         * noticeType : 2
         * isPublish : 1
         * pageNum : 0
         * userId :
         * createrName :
         * noticeTitle : Arm与联通合作出现新进展：成功部署物联网平台
         * isRelease : 1
         * imgUrl : https://image.bzlyplay.com/pro_20190718175458692.jpg
         * noticeContent : Arm与联通合作出现新进展：中关村在线消息：7月18日消息，今天上午中国联通与Arm公司在北京召开了Arm＋中国联通物联网平台部署成功发布会。此次发布会上，中国联通和Arm不仅分享了双方合作的新进展，还公布了双方部成功署的物联网设备管理平台解决方案。
         * createrTime : 1563443704750
         * cancelTime : 1563552000000
         * id : 11
         * createrMobile : 18268890930
         */
        public int ranges;              //	通知范围1全部
        public int isRead;
        public String linkAddress;     //链接地址
        public int noticeType;          //公告类型1文字公告2首页活动3消息活动
        public String noticeTitle;     //公告标题
        public String imgUrl;            //公告图片
        public String noticeContent;       //	通知范围1全部
        public int id;                   //公告id
    }

}
