package com.micang.baozhu.http.net;


import android.content.Context;

import com.google.gson.Gson;
import com.micang.baozhu.AppContext;
import com.micang.baozhu.constant.CommonConstant;
import com.micang.baozhu.http.BaseResult;
import com.micang.baozhu.http.bean.AdditionBean;
import com.micang.baozhu.http.bean.BannerBean;
import com.micang.baozhu.http.bean.ChangeBean;
import com.micang.baozhu.http.bean.DisposeBean;
import com.micang.baozhu.http.bean.InformationRewardCoinBean;
import com.micang.baozhu.http.bean.NoviceButBean;
import com.micang.baozhu.http.bean.SignMakeBean;
import com.micang.baozhu.http.bean.SignMakeChangeBean;
import com.micang.baozhu.http.bean.SignMakeEarnMoneyBean;
import com.micang.baozhu.http.bean.SignMakeGameBean;
import com.micang.baozhu.http.bean.VIpTaskGameBean;
import com.micang.baozhu.http.bean.VipBean;
import com.micang.baozhu.http.bean.anwer.QuestionTopResult;
import com.micang.baozhu.http.bean.anwer.QuestionTypeBean;
import com.micang.baozhu.http.bean.earlyclock.EarlyClockBean;
import com.micang.baozhu.http.bean.earlyclock.EarlyClockLuckBean;
import com.micang.baozhu.http.bean.earlyclock.EarlyClockcardBean;
import com.micang.baozhu.http.bean.earlyclock.EarlyClockinfo;
import com.micang.baozhu.http.bean.home.BaoquGamereward;
import com.micang.baozhu.http.bean.home.BaoquGamerewardinformation;
import com.micang.baozhu.http.bean.home.ConductTaskBean;
import com.micang.baozhu.http.bean.home.EverydayRedGameBean;
import com.micang.baozhu.http.bean.home.EverydayRedListBean;
import com.micang.baozhu.http.bean.home.EverydayRedTaskinfoBean;
import com.micang.baozhu.http.bean.home.FirstVideoBean;
import com.micang.baozhu.http.bean.home.GameBean;
import com.micang.baozhu.http.bean.home.GameCompanyBean;
import com.micang.baozhu.http.bean.home.GameTypeBean;
import com.micang.baozhu.http.bean.home.HasGameRightBean;
import com.micang.baozhu.http.bean.home.MarqueeviewMessageBean;
import com.micang.baozhu.http.bean.home.NewUserTaskBean;
import com.micang.baozhu.http.bean.home.NoticeBean;
import com.micang.baozhu.http.bean.home.SelectVipNewsBean;
import com.micang.baozhu.http.bean.home.SignBean;
import com.micang.baozhu.http.bean.home.VideoCountBean;
import com.micang.baozhu.http.bean.home.VideoTimeUserBean;
import com.micang.baozhu.http.bean.home.VideoswitchBean;
import com.micang.baozhu.http.bean.home.VipInfoBean;
import com.micang.baozhu.http.bean.home.VipListBean;
import com.micang.baozhu.http.bean.login.GetSmsBean;
import com.micang.baozhu.http.bean.login.MobilExistBean;
import com.micang.baozhu.http.bean.login.Res;
import com.micang.baozhu.http.bean.login.ServiceBean;
import com.micang.baozhu.http.bean.pay.OpenPayTypeBean;
import com.micang.baozhu.http.bean.pay.WithDrawBean;
import com.micang.baozhu.http.bean.task.GetTaskSuccessBean;
import com.micang.baozhu.http.bean.task.SubmitaskBean;
import com.micang.baozhu.http.bean.task.TaskBean;
import com.micang.baozhu.http.bean.task.TaskDetailBean;
import com.micang.baozhu.http.bean.task.TaskProgressBean;
import com.micang.baozhu.http.bean.user.AddressBean;
import com.micang.baozhu.http.bean.user.AreaBean;
import com.micang.baozhu.http.bean.user.COOBean;
import com.micang.baozhu.http.bean.user.CashNumBean;
import com.micang.baozhu.http.bean.user.DiscountCardBean;
import com.micang.baozhu.http.bean.user.EncourageBean;
import com.micang.baozhu.http.bean.user.HaveReadBean;
import com.micang.baozhu.http.bean.user.MyCardBean;
import com.micang.baozhu.http.bean.user.PayBean;
import com.micang.baozhu.http.bean.user.UserAddressBean;
import com.micang.baozhu.http.bean.user.UserBean;
import com.micang.baozhu.http.bean.user.UserWithDrawBean;
import com.micang.baozhu.http.bean.user.UserWithdrawTaskinfoBean;
import com.micang.baozhu.http.bean.user.WithDrawTaskGameBean;
import com.micang.baozhu.module.information.HippoNewsBean;
import com.micang.baozhu.module.information.NewsListBean;
import com.micang.baozhu.util.AppUtils;
import com.micang.baozhu.util.DeviceUtils;
import com.micang.baozhu.util.EmptyUtils;
import com.micang.baozhu.util.NetworkUtils;
import com.micang.baozhu.util.ScreenUtils;
import com.micang.baselibrary.util.Base64;
import com.micang.baselibrary.util.MD5Util;
import com.micang.baselibrary.util.SPUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class HttpUtils {
    /**
     * @return
     */
    private static HashMap<String, Object> getBaseMap() {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("token", SPUtils.token(AppContext.getInstance()));
        resultMap.put("imei", SPUtils.imei(AppContext.getInstance()));
        resultMap.put("equipmentType", "1");//安卓用户:1
        return resultMap;
    }

    public static Call<BaseResult<MobilExistBean>> mobileExist(String moblie) {
        return RetrofitUtils.loginApi().mobileExist(moblie);
    }

    public static Call<BaseResult<GetSmsBean>> getSmscode(String accountNum, String ruleNum, String sendMode) {
        String umeng_channel = AppUtils.getAppMetaData(AppContext.getInstance(), "UMENG_CHANNEL");
        return RetrofitUtils.getSmsCodeApi().getcode(accountNum, ruleNum, sendMode, umeng_channel);
    }

    public static Call<BaseResult<GetSmsBean>> checkSmscode(String accountNum, String ruleNum, String sendMode, String code) {
        return RetrofitUtils.getSmsCodeApi().checkcode(accountNum, ruleNum, sendMode, code);
    }

    //注册
    public static Call<BaseResult<UserBean>> register(String mobile, String password, String qrCode, String codeKey, String imei, String channelCode, String equipmentType, String registrationId) {
        return RetrofitUtils.loginApi().register(mobile, password, qrCode, codeKey, imei, channelCode, equipmentType, registrationId);
    }

    //绑定手机号
    public static Call<BaseResult<UserBean>> bindMobile(String mobile, String codeKey, String imei, String channelCode, String equipmentType, String registrationId, String openId, String aliasName, String profile) {
        return RetrofitUtils.loginApi().bindMobile(mobile, codeKey, imei, channelCode, equipmentType, registrationId, openId, aliasName, profile);
    }

    public static Call<BaseResult<GetSmsBean>> changePsw(String mobile, String codeKey, String password, String equipmentType) {
        return RetrofitUtils.loginApi().resetPassword(mobile, codeKey, password, equipmentType);
    }

    public static Call<BaseResult<UserBean>> loginPsw(String mobile, String password, String imei, String equipmentType, String registrationId) {
        return RetrofitUtils.loginApi().loginByPassword(mobile, password, imei, equipmentType, registrationId);
    }

    public static Call<BaseResult<UserBean>> loginBySms(String mobile, String codeKey, String imei, String equipmentType, String registrationId) {
        return RetrofitUtils.loginApi().loginBySms(mobile, codeKey, imei, equipmentType, registrationId);
    }

    public static Call<BaseResult<List<MarqueeviewMessageBean>>> getListMessge() {
        return RetrofitUtils.loginApi().listMessge();
    }

    public static Call<BaseResult<UserBean>> getUserInfo() {
        HashMap<String, Object> treeMap = getBaseMap();
        return RetrofitUtils.loginApi().getUserInfo(treeMap);
    }

    public static Call<BaseResult<SignBean>> sign() {
        HashMap<String, Object> treeMap = getBaseMap();
        return RetrofitUtils.loginApi().sign(treeMap);
    }

    public static Call<BaseResult<List<QuestionTypeBean>>> questionType() {
        HashMap<String, Object> treeMap = getBaseMap();
        return RetrofitUtils.loginApi().questionType(treeMap);
    }

    public static Call<BaseResult<QuestionTopResult>> checkTopResult() {
        HashMap<String, Object> treeMap = getBaseMap();
        return RetrofitUtils.loginApi().checkTopResult(treeMap);
    }

    public static Call<BaseResult> setQuestion(String questionType, String question, String answers1, String answers2, String answers3, String answers4) {
        HashMap<String, Object> treeMap = getBaseMap();
        treeMap.put("questionType", questionType);
        treeMap.put("question", question);
        treeMap.put("answer1", answers1);
        treeMap.put("answer2", answers2);
        treeMap.put("answer3", answers3);
        treeMap.put("answer4", answers4);
        return RetrofitUtils.loginApi().setQuestion(treeMap);
    }

    public static Call<BaseResult> changeName(String name) {
        HashMap<String, Object> map = getBaseMap();
        map.put("aliasName", name);
        return RetrofitUtils.loginApi().changeUserInfo(map);
    }

    public static Call<BaseResult> changesex(String sex) {
        HashMap<String, Object> map = getBaseMap();
        map.put("sex", sex);
        return RetrofitUtils.loginApi().changeUserInfo(map);
    }

    public static Call<BaseResult> changebirthday(String birthday) {
        HashMap<String, Object> map = getBaseMap();
        map.put("birthday", birthday);
        return RetrofitUtils.loginApi().changeUserInfo(map);
    }

    public static Call<BaseResult> changePic(String profile) {
        HashMap<String, Object> map = getBaseMap();
        map.put("profile", profile);
        return RetrofitUtils.loginApi().changeUserInfo(map);
    }

    public static Call<BaseResult> changeQQ(String qqNum) {
        HashMap<String, Object> map = getBaseMap();
        map.put("qqNum", qqNum);
        return RetrofitUtils.loginApi().changeUserInfo(map);
    }

    public static Call<BaseResult> withdrawalsapply(String codeKey, String amount, String account, int accountType) {
        HashMap<String, Object> map = getBaseMap();
        map.put("codeKey", codeKey);
        map.put("amount", amount);
        map.put("account", account);
        map.put("accountType", accountType);
        return RetrofitUtils.loginApi().withdrawalsapply(map);
    }


    public static Call<BaseResult> getNotRoom(String entryCode) {
        HashMap<String, Object> map = getBaseMap();
        map.put("entryCode", entryCode);
        return RetrofitUtils.loginApi().getNotRoom(map);
    }

    public static Call<BaseResult<String>> getUploadTokenload() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().getUploadToken(map);
    }

    public static Call<BaseResult<MyCardBean>> getPassbookList(String pageSize, String pageNum) {
        HashMap<String, Object> map = getBaseMap();
        map.put("pageSize", pageSize);
        map.put("pageNum", pageNum);

        return RetrofitUtils.loginApi().getPassbookList(map);
    }

    public static Call<BaseResult> bindingAlipay(String type, String aliNum,
                                                 String userName, String wxCode, String codeKey) {
        HashMap<String, Object> map = getBaseMap();
        map.put("type", type);
        map.put("aliNum", aliNum);
        map.put("userName", userName);
        map.put("wxCode", wxCode);
        map.put("codeKey", codeKey);
        return RetrofitUtils.loginApi().bindingAlipay(map);
    }

    public static Call<BaseResult> changeALIPAY(String type, String aliNum,
                                                String userName, String codeKey) {
        HashMap<String, Object> map = getBaseMap();
        map.put("type", type);
        map.put("aliNum", aliNum);
        map.put("userName", userName);
        map.put("codeKey", codeKey);
        return RetrofitUtils.loginApi().changeALIPAY(map);
    }

    public static Call<BaseResult<GameBean>> tryPlayList(int pageSize, int pageNum) {
        HashMap<String, Object> map = getBaseMap();
        map.put("pageSize", pageSize);
        map.put("pageNum", pageNum);

        return RetrofitUtils.loginApi().tryPlayList(map);
    }

    public static Call<BaseResult<List<VipBean>>> queryMyVips() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().queryMyVips(map);
    }

    public static Call<BaseResult<GameBean>> queryGameList(int pageSize, int pageNum) {
        HashMap<String, Object> map = getBaseMap();
        map.put("pageSize", pageSize);
        map.put("pageNum", pageNum);
//        map.put("gameTag", gameTag);
        return RetrofitUtils.loginApi().queryGameList(map);
    }


    public static Call<BaseResult> toPlay(String mobile, String id) {
        HashMap<String, Object> map = getBaseMap();
        map.put("mobile", EmptyUtils.isEmpty(mobile) ? "" : mobile);
        map.put("id", id);
        return RetrofitUtils.loginApi().toPlay(map);
    }

    public static Call<BaseResult> bindQrCode(String bindQrCode) {
        HashMap<String, Object> map = getBaseMap();
        map.put("qrCode", bindQrCode);
        return RetrofitUtils.loginApi().bindQrCode(map);
    }

    public static Call<BaseResult<List<DiscountCardBean>>> discountList() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().discountList(map);
    }

    public static Call<BaseResult> createOrder(String descripte, String price, String couponId, String payPurpose, String vipId,
                                               String payType, String isBalance) {
        HashMap<String, Object> map = getBaseMap();
        map.put("descripte", descripte);
        map.put("price", price);
        map.put("couponId", couponId);
        map.put("payPurpose", payPurpose);
        map.put("vipId", vipId);
        map.put("payType", payType);
        map.put("isBalance", isBalance);
        return RetrofitUtils.loginApi().createOrder(map);
    }

    public static Call<BaseResult<PayBean>> payType(String outTradeNo, String payMode) {
        HashMap<String, Object> map = getBaseMap();
        map.put("outTradeNo", outTradeNo);
        map.put("payMode", payMode);
        return RetrofitUtils.loginApi().payType(map);
    }

    public static Call<BaseResult> paySuccess(String outTradeNo) {
        HashMap<String, Object> map = getBaseMap();
        map.put("outTradeNo", outTradeNo);
        return RetrofitUtils.loginApi().paySuccess(map);
    }


    public static Call<BaseResult<WithDrawBean>> readyWithdrawals() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().readyWithdrawals(map);
    }

    public static Call<BaseResult> getPCDDUrl(String shortName) {
        HashMap<String, Object> map = getBaseMap();
        map.put("shortName", shortName);
        return RetrofitUtils.loginApi().getPCDDUrl(map);
    }

    public static Call<BaseResult<GameCompanyBean>> queryFList(int pageSize, int pageNum) {
        HashMap<String, Object> map = getBaseMap();
        map.put("pageSize", pageSize);
        map.put("pageNum", pageNum);
        return RetrofitUtils.loginApi().queryFList(map);
    }


    public static Call<BaseResult<List<AddressBean>>> userAddressList() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().userAddressList(map);
    }

    public static Call<BaseResult> addAddress(String receiver, String mobile, String areaId, String detailAddress, String isDefault) {
        HashMap<String, Object> map = getBaseMap();
        map.put("receiver", receiver);
        map.put("mobile", mobile);
        map.put("areaId", areaId); //区id
        map.put("detailAddress", detailAddress);//地址详情
        map.put("isDefault", isDefault);//是否是默认地址1是2不是
        return RetrofitUtils.loginApi().addAddress(map);
    }

    public static Call<BaseResult<List<AreaBean>>> areaList(String level, String parentId) {
        HashMap<String, Object> map = getBaseMap();
        map.put("level", level);//0.国家 1.省份 2.城市 3.区县
        map.put("parentId", parentId);//父级ID
        return RetrofitUtils.loginApi().areaList(map);
    }

    public static Call<BaseResult<UserAddressBean>> getAddress(String addressId) {
        HashMap<String, Object> map = getBaseMap();
        map.put("addressId", addressId);
        return RetrofitUtils.loginApi().getAddress(map);
    }

    public static Call<BaseResult> editList(String addressId, String receiver, String mobile, String areaId, String detailAddress, String isDefault) {
        HashMap<String, Object> map = getBaseMap();
        map.put("addressId", addressId);
        map.put("receiver", receiver);
        map.put("mobile", mobile);
        map.put("areaId", areaId); //区id
        map.put("detailAddress", detailAddress);//地址详情
        map.put("isDefault", isDefault);//是否是默认地址1是2不是
        return RetrofitUtils.loginApi().editList(map);

    }

    public static Call<BaseResult> removeAddress(String addressId) {
        HashMap<String, Object> map = getBaseMap();
        map.put("addressId", addressId);
        return RetrofitUtils.loginApi().removeAddress(map);
    }

    //支付密码
    public static Call<BaseResult> updatePayPassword(String codeKey, String payPassword) {
        HashMap<String, Object> map = getBaseMap();
        map.put("codeKey", codeKey);
        map.put("payPassword", payPassword);
        return RetrofitUtils.loginApi().updatePayPassword(map);
    }


    //参数配置
    public static Call<BaseResult<DisposeBean>> homePageDispose(String channelCode, String versionNo) {
        return RetrofitUtils.loginApi().homePageDispose(channelCode, versionNo, "1");
    }

    //资讯金币上限
    public static Call<BaseResult<InformationRewardCoinBean>> getRewardConifg(String type, String content) {
        HashMap<String, Object> map = getBaseMap();
        map.put("type", type);
        map.put("content", content);
        return RetrofitUtils.loginApi().getRewardConifg(map);
    }

    //获取hippo资讯列表
    public static Call<BaseResult<List<HippoNewsBean>>> getHIPPONews(String category,
                                                                     String page, String lon, String lat) {
        HashMap<String, Object> map = new HashMap<>();
        String androidID = DeviceUtils.getAndroidID();//安卓 id
        String manufacturer = DeviceUtils.getManufacturer();//设备厂商
        String model1 = DeviceUtils.getModel();//获取设备型号
        String sdkVersionName = DeviceUtils.getSDKVersionName();//系统版本号
        String screenHeight = String.valueOf(ScreenUtils.getScreenHeight()); //屏幕高
        String screenWidth = String.valueOf(ScreenUtils.getScreenWidth());  //屏幕宽
        String networkOperatortype = NetworkUtils.getNetworkOperatorType();//网络运营商
        String appName = AppUtils.getAppName();
        String appVersionName = AppUtils.getAppVersionName();
        String appPackageName = AppUtils.getAppPackageName();
        String networkType = NetworkUtils.getNetType();//网络类型
        map.put("imei", SPUtils.imei(AppContext.getInstance()));
        map.put("category", category);//频道
        map.put("make", manufacturer);        //设备厂商
        map.put("model", model1);      //设备型号
        map.put("os", "1");            //操作系统 1,安卓
        map.put("anid", androidID);         //安卓 id
        map.put("conn", networkType);         //网络链接类型
        map.put("carrier", networkOperatortype);     //网络运营商
        map.put("devicetype", "1");         //设备类型 1,手机
        map.put("sh", screenHeight);                  //屏幕高
        map.put("sw", screenWidth);                  //屏幕宽
        map.put("osv", sdkVersionName);                 //系统版本
        map.put("page", page);                 //分页 ,刷新,-1,二次-2,类推
        map.put("appname", appName);             //app名
        map.put("app_version", appVersionName); //版本名
        map.put("pkgname", appPackageName);  //包名
        map.put("lon", lon);  //经度
        map.put("lat", lat);  //纬度
        return RetrofitUtils.loginApi().getHIPPONews(map);

    }


    public static Call<BaseResult<AdditionBean>> getAddCoupon() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().getAddCoupon(map);
    }

    /***
     * 新手引导,新手福利
     * @param versionNo
     * @param channelCode
     * @return
     */
    public static Call<BaseResult<NoviceButBean>> noviceBut(String versionNo, String channelCode) {
        HashMap<String, Object> map = getBaseMap();
        map.put("versionNo", versionNo);
        map.put("channelCode", channelCode);
        return RetrofitUtils.loginApi().noviceBut(map);
    }

    /***
     * 新手引导,新手福利
     * @return
     */
    public static Call<BaseResult> userFirstLog() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().userFirstLog(map);
    }

    /***
     *28banner
     * @return
     */
    public static Call<BaseResult<List<BannerBean>>> queryBanner(String position) {
        return RetrofitUtils.loginApi().queryBanner(position);
    }


    //读资讯获得金币
    public static Call<BaseResult> readNewZXReward(String type, String content) {

        HashMap<String, Object> map = getBaseMap();
        long currentTimeMillis = System.currentTimeMillis();
        String token = SPUtils.token(AppContext.getInstance());
        String md5 = MD5Util.getMd5(token + "bzlyInformationMd5" + currentTimeMillis);
        map.put("sign", md5);
        map.put("type", type);
        map.put("time", currentTimeMillis + "");
        map.put("content", content);
        return RetrofitUtils.loginApi().readNewZXReward(map);
    }


    //用户反馈
    public static Call<BaseResult> feedback(String opinionType, String opinionContent, String contentImg, String email) {
        HashMap<String, Object> map = getBaseMap();
        map.put("opinionType", opinionType);
        map.put("opinionContent", opinionContent);
        map.put("contentImg", contentImg);
        map.put("email", email);
        return RetrofitUtils.loginApi().feedback(map);
    }


    //猪猪用户游戏权限
    public static Call<BaseResult<HasGameRightBean>> hasGameRight(String id) {
        HashMap<String, Object> map = getBaseMap();
        map.put("id", id);
        return RetrofitUtils.loginApi().hasGameRight(map);
    }

    //删除试玩任务
    public static Call<BaseResult> remove(String id) {
        HashMap<String, Object> map = getBaseMap();
        map.put("id", id);
        return RetrofitUtils.loginApi().remove(map);
    }

    //随机试玩
    public static Call<BaseResult<ChangeBean>> recommendGame() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().recommendGame(map);
    }

    //猪猪用户游戏权限
    public static Call<BaseResult<ServiceBean>> contactInformation() {

        return RetrofitUtils.loginApi().contactInformation();
    }

    //购买运营总监生订单
    public static Call<BaseResult<COOBean>> tradeTame(String descripte, String price, String payType) {
        HashMap<String, Object> map = getBaseMap();
        map.put("descripte", descripte);
        map.put("price", price);
        map.put("payType", payType);
        return RetrofitUtils.loginApi().tradeTame(map);
    }

    //签到赚消息
    public static Call<BaseResult<List<MarqueeviewMessageBean>>> qdzList() {
        return RetrofitUtils.loginApi().qdzList();
    }

    //获取补签卡
    public static Call<BaseResult> retroactivecard() {

        HashMap<String, Object> map = getBaseMap();
        long currentTimeMillis = System.currentTimeMillis();
        String token = SPUtils.token(AppContext.getInstance());
        String md5 = MD5Util.getMd5(token + "bzlyInformationMd5" + currentTimeMillis);
        map.put("sign", md5);
        map.put("time", currentTimeMillis + "");
        return RetrofitUtils.loginApi().retroactivecard(map);
    }

    //获取今日完成任务数，今日赚到金币,补签卡数
    public static Call<BaseResult<SignMakeEarnMoneyBean>> todayFinish() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().todayFinish(map);
    }

    //签到赚列表
    public static Call<BaseResult<SignMakeBean>> signmake() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().signmake(map);
    }

    //签到赚获取游戏列表
    public static Call<BaseResult<List<SignMakeGameBean>>> signinGames(int signinId) {
        HashMap<String, Object> map = getBaseMap();
        map.put("signinId", signinId);
        return RetrofitUtils.loginApi().signinGames(map);
    }

    //签到赚获取游戏列表
    public static Call<BaseResult<SignMakeChangeBean>> recommendGameNew() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().recommendGameNew(map);
    }

    //使用补签卡
    public static Call<BaseResult> useCard(int signinId) {
        HashMap<String, Object> map = getBaseMap();
        map.put("signinId", signinId);
        return RetrofitUtils.loginApi().useCard(map);
    }

    //获取签到赚游戏链接
    public static Call<BaseResult> getqdGameUrl(int signinId, int id, String mobile) {
        HashMap<String, Object> map = getBaseMap();
        map.put("signinId", signinId);
        map.put("id", id);
        map.put("mobile", mobile);
        return RetrofitUtils.loginApi().getqdGameUrl(map);
    }

    //获取签到赚奖励
    public static Call<BaseResult> receiveReward(int signinId) {
        HashMap<String, Object> map = getBaseMap();
        map.put("id", signinId);
        return RetrofitUtils.loginApi().receiveReward(map);
    }

    //删除签到赚试玩
    public static Call<BaseResult> removeQDZ(String id) {
        HashMap<String, Object> map = getBaseMap();
        map.put("lid", id);
        return RetrofitUtils.loginApi().removeQDZ(map);
    }

    //购买运营总监开启的支付方式
    public static Call<BaseResult<OpenPayTypeBean>> openPay() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().openPay(map);
    }

    //赚钱任务
    public static Call<BaseResult<TaskBean>> getFList(String typeId, String isUpper, int pageNum, int pageSize) {
        HashMap<String, Object> map = getBaseMap();
        map.put("typeId", typeId);//1- 高额收益 2-下载注册 3-实名认证 4-其他 不传表示全部
        map.put("isUpper", isUpper);//1-已上架3-可预约
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        return RetrofitUtils.loginApi().getFList(map);
    }

    //赚钱任务详情
    public static Call<BaseResult<TaskDetailBean>> tptaskInfo(int id) {
        HashMap<String, Object> map = getBaseMap();
        map.put("id", id);
        return RetrofitUtils.loginApi().tptaskInfo(map);
    }

    //领取任务
    public static Call<BaseResult<GetTaskSuccessBean>> userTptask(int tpTaskId) {
        HashMap<String, Object> map = getBaseMap();
        map.put("tpTaskId", tpTaskId);
        return RetrofitUtils.loginApi().userTptask(map);
    }

    //提交任务
    public static Call<BaseResult> userTptaskSubmit(int tpTaskId, int lTpTaskId, List<SubmitaskBean> submitaskBeanList) {
        HashMap<String, Object> map = getBaseMap();
        map.put("tpTaskId", tpTaskId);
        map.put("lTpTaskId", lTpTaskId);
        Gson gson = new Gson();
        String order = gson.toJson(submitaskBeanList);
        try {
            map.put("submits", URLEncoder.encode(Base64.encode(order.getBytes("utf-8")), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return RetrofitUtils.loginApi().userTptaskSubmit(map);
    }

    //任务进度列表
    public static Call<BaseResult<TaskProgressBean>> queryFlist(String status, int pageNum, int pageSize) {
        HashMap<String, Object> map = getBaseMap();
        map.put("status", status);
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        return RetrofitUtils.loginApi().queryFlist(map);
    }

    //任务进度消息列表
    public static Call<BaseResult<List<MarqueeviewMessageBean>>> kszList() {
        return RetrofitUtils.loginApi().kszList();
    }

    //任务进度列表
    public static Call<BaseResult> givein(int id, String remark) {
        HashMap<String, Object> map = getBaseMap();
        map.put("id", id);
        map.put("remark", remark);
        return RetrofitUtils.loginApi().givein(map);
    }

    //预约任务
    public static Call<BaseResult> addYY(int tpTaskId) {
        HashMap<String, Object> map = getBaseMap();
        map.put("tpTaskId", tpTaskId);
        return RetrofitUtils.loginApi().addYY(map);
    }

    //用户每日活跃记录
    public static Call<BaseResult> uerActiveDd() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().uerActiveDd(map);
    }

    //是否存在未读消息
    public static Call<BaseResult<HaveReadBean>> haveUnRead() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().haveUnRead(map);
    }

    //弹窗公告
    public static Call<BaseResult<NoticeBean>> noticeFrame() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().noticeFrame(map);
    }

    //用户读公告
    public static Call<BaseResult> readnoticeFrame(int id) {
        HashMap<String, Object> map = getBaseMap();
        map.put("noticeId", id);
        return RetrofitUtils.loginApi().readnoticeFrame(map);
    }

    //提现金额
    public static Call<BaseResult<List<UserWithDrawBean>>> cashPrice() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().cashPrice(map);
    }


    //绑定微信
    public static Call<BaseResult> bindingWxNumber(String type, String wxCode, String userName) {
        HashMap<String, Object> map = getBaseMap();
        map.put("type", type);
        map.put("wxCode", wxCode);
        map.put("userName", userName);
        return RetrofitUtils.loginApi().bindingWxNumber(map);
    }

    //修改微信
    public static Call<BaseResult> changeWxNumber(String type, String wxCode, String userName) {
        HashMap<String, Object> map = getBaseMap();
        map.put("type", type);
        map.put("wxCode", wxCode);
        map.put("userName", userName);
        return RetrofitUtils.loginApi().changeWxNumber(map);
    }

    //vip购买滚动消息
    public static Call<BaseResult<List<SelectVipNewsBean>>> selectVipNews(String channelCode) {
        HashMap<String, Object> map = getBaseMap();
        map.put("channelCode", channelCode);
        return RetrofitUtils.loginApi().selectVipNews(map);
    }

    //vip列表
    public static Call<BaseResult<List<VipListBean>>> vipList() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().vipList(map);
    }

    //VIP详情
    public static Call<BaseResult<VipInfoBean>> vipinfo(int id) {
        HashMap<String, Object> map = getBaseMap();
        map.put("id", id);
        return RetrofitUtils.loginApi().vipinfo(map);
    }

    //VIP领取活跃金匹配任务
    public static Call<BaseResult<VIpTaskGameBean>> recommendGameVip(int vipId) {
        HashMap<String, Object> map = getBaseMap();
        map.put("vipId", vipId);
        return RetrofitUtils.loginApi().recommendGameVip(map);
    }

    //VIP任务获取链接
    public static Call<BaseResult> getHyjGameUrl(int vipId, int id, String mobile) {
        HashMap<String, Object> map = getBaseMap();
        map.put("vipId", vipId);
        map.put("id", id);
        map.put("mobile", mobile);
        return RetrofitUtils.loginApi().getHyjGameUrl(map);
    }

    //VIP任务领取活跃金
    public static Call<BaseResult<EncourageBean>> receiveActiveNews(int vipId) {
        HashMap<String, Object> map = getBaseMap();
        map.put("vipId", vipId);

        return RetrofitUtils.loginApi().receiveActiveNews(map);
    }

    //删除vip任务
    public static Call<BaseResult> deleteActiveTask(int gameId) {
        HashMap<String, Object> map = getBaseMap();
        map.put("gameId", gameId);
        return RetrofitUtils.loginApi().deleteActiveTask(map);
    }

    //获取游戏类型
    public static Call<BaseResult<List<GameTypeBean>>> tpGameType() {
        return RetrofitUtils.loginApi().tpGameType();
    }

    //删除vip任务
    public static Call<BaseResult<GameBean>> tpGameList(int pageSize, int pageNum, int typeId) {
        HashMap<String, Object> map = getBaseMap();
        map.put("pageSize", pageSize);
        map.put("pageNum", pageNum);
        map.put("typeId", typeId);
        return RetrofitUtils.loginApi().tpGameList(map);
    }

    //获取提现次数
    public static Call<BaseResult<CashNumBean>> cashNum() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().cashNum(map);
    }

    //提现任务--选择提现金额
    public static Call<BaseResult> userCash(int mode, String money) {
        HashMap<String, Object> map = getBaseMap();
        map.put("mode", mode);
        map.put("money", money);
        return RetrofitUtils.loginApi().userCash(map);
    }

    //提现任务--任务完成进度
    public static Call<BaseResult<UserWithdrawTaskinfoBean>> userCashtaskInfo() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().userCashtaskInfo(map);
    }

    //提现任务--匹配游戏任务
    public static Call<BaseResult<WithDrawTaskGameBean>> recommendGameCash(int cashId) {
        HashMap<String, Object> map = getBaseMap();
        map.put("cashId", cashId);
        return RetrofitUtils.loginApi().recommendGameCash(map);
    }

    //提现任务--获取游戏链接
    public static Call<BaseResult> getTxGameUrl(int id, int cashId, String mobile) {
        HashMap<String, Object> map = getBaseMap();
        map.put("id", id);
        map.put("cashId", cashId);
        map.put("mobile", mobile);
        return RetrofitUtils.loginApi().getTxGameUrl(map);
    }

    //今日完成任务数
    public static Call<BaseResult> queryTodayFinish() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().queryTodayFinish(map);
    }

    //提现任务--发起提现
    public static Call<BaseResult> cashLaunch(int cashId) {
        HashMap<String, Object> map = getBaseMap();
        map.put("cashId", cashId);
        return RetrofitUtils.loginApi().cashLaunch(map);
    }

    //每日签到--奖励列表
    public static Call<BaseResult<List<EverydayRedListBean>>> appList() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().appList(map);
    }

    //每日签到--查询当天签到情况
    public static Call<BaseResult<EverydayRedTaskinfoBean>> getDaySign() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().getDaySign(map);
    }

    //每日签到--匹配游戏
    public static Call<BaseResult<EverydayRedGameBean>> recommendGameSign() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().recommendGameSign(map);
    }

    //每日签到--绑定任务
    public static Call<BaseResult> getMrhbGameUrl(int id, String mobile) {
        HashMap<String, Object> map = getBaseMap();
        map.put("id", id);
        map.put("mobile", mobile);
        return RetrofitUtils.loginApi().getMrhbGameUrl(map);
    }

    //每日签到--领取任务奖励
    public static Call<BaseResult> receiveCoin() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().receiveCoin(map);
    }

    //每日签到--推送
    public static Call<BaseResult> noticeReady(int noticeType, int status) {
        HashMap<String, Object> map = getBaseMap();
        map.put("noticeType", noticeType);
        map.put("status", status);
        return RetrofitUtils.loginApi().noticeReady(map);
    }

    //新人任务
    public static Call<BaseResult<List<NewUserTaskBean>>> appUserList() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().appUserList(map);
    }

    //查询用户未完成的任务
    public static Call<BaseResult<ConductTaskBean>> getConductTask() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().getConductTask(map);
    }

    //随机配游戏和应用
    public static Call<BaseResult<ChangeBean>> recommendGameTask() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().recommendGameTask(map);
    }

    //获取快速赚链接
    public static Call<BaseResult> buildUrl(String id) {
        HashMap<String, Object> map = getBaseMap();
        map.put("id", id);
        return RetrofitUtils.loginApi().buildUrl(map);
    }


    //视频入口开关
    public static Call<BaseResult<VideoswitchBean>> videoCount() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().videoCount(map);
    }

    //查看用户观看视频结果
    public static Call<BaseResult<VideoCountBean>> videoUser() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().videoUser(map);
    }

    //用户点击看视频
    public static Call<BaseResult<VideoTimeUserBean>> videoTimeUser() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().videoTimeUser(map);
    }


    //早起打卡首页
    public static Call<BaseResult<EarlyClockinfo>> earlycheckin() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().earlycheckin(map);
    }

    //报名
    public static Call<BaseResult> earlycheckinjoin() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().earlycheckinjoin(map);
    }

    //打卡
    public static Call<BaseResult<EarlyClockBean>> earlycheckinclock() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().earlycheckinclock(map);
    }

    //查询补签卡
    public static Call<BaseResult<EarlyClockcardBean>> earlycheckincard() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().earlycheckincard(map);
    }

    //幸运榜
    public static Call<BaseResult<List<EarlyClockLuckBean>>> earlycheckinappList() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().earlycheckinappList(map);
    }

    //查询小游戏奖励配置
    public static Call<BaseResult<BaoquGamerewardinformation>> xyxRewardConfig() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().xyxRewardConfig(map);
    }

    //小游戏  领取奖励
    public static Call<BaseResult<BaoquGamereward>> xyxReward(int seconds) {
        HashMap<String, Object> map = getBaseMap();
        long currentTimeMillis = System.currentTimeMillis();
        String token = SPUtils.token(AppContext.getInstance());
        String md5 = MD5Util.getMd5(token + "bzlyInformationMd5" + currentTimeMillis);
        map.put("sign", md5);
        map.put("time", currentTimeMillis + "");
        map.put("seconds", seconds);
        return RetrofitUtils.loginApi().xyxReward(map);
    }

    //验证通讯录,通话记录
    public static Call<BaseResult<Res>> checkingMailList(int addressBook, int callRecord) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("addressBook", addressBook);
        map.put("callRecord", callRecord);
        return RetrofitUtils.loginApi().checkingMailList(map);
    }

    //陀螺仪
    public static Call<BaseResult> addGyro(String gyroX, String gyroY, String gyroZ, int pageType) {
        HashMap<String, Object> map = getBaseMap();
        map.put("gyroX", gyroX);    //陀螺仪x轴
        map.put("gyroY", gyroY);    //陀螺仪y轴
        map.put("gyroZ", gyroZ);    //陀螺仪z轴
        map.put("pageType", pageType);  //页面类型：1主页，2游戏列表，3游戏详情
        return RetrofitUtils.loginApi().addGyro(map);
    }

    //查看新用户看视频次数
    public static Call<BaseResult<FirstVideoBean>> newUserVideo() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().newUserVideo(map);
    }

    //查看新用户看视频金币
    public static Call<BaseResult<FirstVideoBean>> newUserVideoCoin() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().newUserVideoCoin(map);
    }


    //微信登陆
    public static Call<BaseResult<UserBean>> wechatLogin(String imei, String registrationId, String wxCode) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("equipmentType", 1);
        map.put("imei", imei);
        map.put("registrationId", registrationId);
        map.put("wxCode", wxCode);
        return RetrofitUtils.loginApi().wechatLogin(map);
    }

    //搜索游戏
    public static Call<BaseResult<GameBean>> lenovoList(String gameTitle, int pageNum, int pageSize) {
        HashMap<String, Object> map = getBaseMap();
        map.put("gameTitle", gameTitle);
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        return RetrofitUtils.loginApi().lenovoList(map);
    }

    //获取历史搜索记录
    public static Call<BaseResult> userSearchLogs() {
        HashMap<String, Object> map = getBaseMap();
        map.put("searchType", 1);
        return RetrofitUtils.loginApi().userSearchLogs(map);
    }

    //获取推荐游戏
    public static Call<BaseResult> tjList() {
        HashMap<String, Object> map = getBaseMap();
        return RetrofitUtils.loginApi().tjList(map);
    }

    //删除历史搜索记录
    public static Call<BaseResult> removeAll() {
        HashMap<String, Object> map = getBaseMap();
        map.put("searchType", 1);
        return RetrofitUtils.loginApi().removeAll(map);
    }

}
