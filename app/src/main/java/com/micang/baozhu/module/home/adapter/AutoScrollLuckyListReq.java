package com.micang.baozhu.module.home.adapter;

import java.util.List;

public class AutoScrollLuckyListReq {

    /**
     * ret : 0
     * msg : success
     * datas : [{"num":1,"createTime":"2019-05-24 19:13:13","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"20个积分","giftPhoto":"/article/201808/6968486350784e76a377f364a7bf00cd.png"},{"num":1,"createTime":"2019-05-24 19:09:26","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"20个积分","giftPhoto":"/article/201808/6968486350784e76a377f364a7bf00cd.png"},{"num":1,"createTime":"2019-05-24 18:58:08","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"满10减1元","giftPhoto":"/article/201810/fffa68bbd6894db581bf8e371ab3128a.png"},{"num":1,"createTime":"2019-05-24 18:52:41","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"20个积分","giftPhoto":"/article/201808/6968486350784e76a377f364a7bf00cd.png"},{"num":1,"createTime":"2019-05-24 18:47:02","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"满10减1元","giftPhoto":"/article/201810/fffa68bbd6894db581bf8e371ab3128a.png"},{"num":1,"createTime":"2019-05-24 18:44:24","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"满10减1元","giftPhoto":"/article/201810/fffa68bbd6894db581bf8e371ab3128a.png"},{"num":1,"createTime":"2019-05-24 18:02:35","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"三花便签（20枚）","giftPhoto":"/article/201812/c04669564c454437b201634bf667da52.png"},{"num":1,"createTime":"2019-05-24 17:58:57","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"三花便签（20枚）","giftPhoto":"/article/201812/c04669564c454437b201634bf667da52.png"},{"num":1,"createTime":"2019-05-24 17:57:09","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"满10减1元","giftPhoto":"/article/201810/fffa68bbd6894db581bf8e371ab3128a.png"},{"num":1,"createTime":"2019-05-24 17:51:38","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"20个积分","giftPhoto":"/article/201808/6968486350784e76a377f364a7bf00cd.png"},{"num":1,"createTime":"2019-05-24 17:51:29","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"满10减1元","giftPhoto":"/article/201810/fffa68bbd6894db581bf8e371ab3128a.png"},{"num":1,"createTime":"2019-05-24 17:46:41","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"20个积分","giftPhoto":"/article/201808/6968486350784e76a377f364a7bf00cd.png"},{"num":1,"createTime":"2019-05-24 17:46:32","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"三花便签（20枚）","giftPhoto":"/article/201812/c04669564c454437b201634bf667da52.png"},{"num":1,"createTime":"2019-05-24 15:50:02","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"三花便签（20枚）","giftPhoto":"/article/201812/c04669564c454437b201634bf667da52.png"},{"num":1,"createTime":"2019-05-24 15:46:16","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"满10减1元","giftPhoto":"/article/201810/fffa68bbd6894db581bf8e371ab3128a.png"},{"num":1,"createTime":"2019-05-24 15:39:30","userId":43431,"integral":-20,"nickname":"showly","phone":"****","giftName":"20个积分","giftPhoto":"/article/201808/6968486350784e76a377f364a7bf00cd.png"},{"num":1,"createTime":"2019-05-15 14:12:02","userId":43436,"integral":-20,"nickname":"kkk","phone":"****","giftName":"20个积分","giftPhoto":"/article/201808/6968486350784e76a377f364a7bf00cd.png"},{"num":1,"createTime":"2019-05-15 14:11:18","userId":43436,"integral":-20,"nickname":"kkk","phone":"****","giftName":"离型纸卷（4cm）","giftPhoto":"/article/201812/4c0ba10f0de444369412289451cbcb77.jpg"},{"num":1,"createTime":"2019-05-07 16:18:26","userId":4758,"integral":-20,"nickname":"个人觉得好多好多话好的还减肥回到家就打架好的就放假U盾觉得几点","phone":"****","giftName":"满10减1元","giftPhoto":"/article/201810/fffa68bbd6894db581bf8e371ab3128a.png"},{"num":1,"createTime":"2019-05-07 16:18:20","userId":4758,"integral":-20,"nickname":"个人觉得好多好多话好的还减肥回到家就打架好的就放假U盾觉得几点","phone":"****","giftName":"离型纸卷（4cm）","giftPhoto":"/article/201812/4c0ba10f0de444369412289451cbcb77.jpg"}]
     */

    private int ret;
    private String msg;
    private List<DatasBean> datas;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * num : 1
         * createTime : 2019-05-24 19:13:13
         * userId : 43431
         * integral : -20
         * nickname : showly
         * phone : ****
         * giftName : 20个积分
         * giftPhoto : /article/201808/6968486350784e76a377f364a7bf00cd.png
         */

        private int num;
        private String createTime;
        private int userId;
        private int integral;
        private String nickname;
        private String phone;
        private String giftName;
        private String giftPhoto;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public String getGiftPhoto() {
            return giftPhoto;
        }

        public void setGiftPhoto(String giftPhoto) {
            this.giftPhoto = giftPhoto;
        }
    }
}
