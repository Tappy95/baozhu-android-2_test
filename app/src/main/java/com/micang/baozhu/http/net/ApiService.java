package com.micang.baozhu.http.net;

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

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    @GET(Urls.MOBILEEXIST)
    Call<BaseResult<MobilExistBean>> mobileExist(@Query("mobile") String mobile);

    @GET(Urls.GETSMSCODE)
    Call<BaseResult<GetSmsBean>> getcode(@Query("accountNum") String accountNum,
                                         @Query("ruleNum") String ruleNum,
                                         @Query("sendMode") String sendMode,
                                         @Query("channelCode") String channelCode);

    @GET(Urls.CHECKSMSCODE)
    Call<BaseResult<GetSmsBean>> checkcode(@Query("accountNum") String accountNum,
                                           @Query("ruleNum") String ruleNum,
                                           @Query("sendMode") String sendMode,
                                           @Query("code") String code);

    /**
     *
     */
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.REGISTER)
    @FormUrlEncoded
    Call<BaseResult<UserBean>> register(@Field("mobile") String mobile,
                                        @Field("password") String password,
                                        @Field("qrCode") String qrCode,
                                        @Field("codeKey") String codeKey,
                                        @Field("imei") String imei,
                                        @Field("channelCode") String channelCode,
                                        @Field("equipmentType") String equipmentType,
                                        @Field("registrationId") String registrationId);

    /**
     * 绑定手机号
     */
    @GET(Urls.bindMobile)
    Call<BaseResult<UserBean>> bindMobile(@Query("mobile") String mobile,
                                          @Query("codeKey") String codeKey,
                                          @Query("imei") String imei,
                                          @Query("channelCode") String channelCode,
                                          @Query("equipmentType") String equipmentType,
                                          @Query("registrationId") String registrationId,
                                          @Query("openId") String openId,
                                          @Query("aliasName") String aliasName,
                                          @Query("profile") String profile);

    //
    @GET(Urls.LOGINBYPASSWORD)
    Call<BaseResult<UserBean>> loginByPassword(@Query("mobile") String mobile,
                                               @Query("password") String password,
                                               @Query("imei") String imei,
                                               @Query("equipmentType") String equipmentType,
                                               @Query("registrationId") String registrationId
    );

    //
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.RESETPASSWORD)
    @FormUrlEncoded
    Call<BaseResult<GetSmsBean>> resetPassword(@Field("mobile") String mobile,
                                               @Field("codeKey") String codeKey,
                                               @Field("password") String password,
                                               @Field("equipmentType") String equipmentType);

    //
    @GET(Urls.LOGINBYSMS)
    Call<BaseResult<UserBean>> loginBySms(@Query("mobile") String mobile,
                                          @Query("codeKey") String codeKey,
                                          @Query("imei") String imei,
                                          @Query("equipmentType") String equipmentType,
                                          @Query("registrationId") String registrationId);

    //
    @GET(Urls.LISTMESSGE)
    Call<BaseResult<List<MarqueeviewMessageBean>>> listMessge();


    //
    @GET(Urls.GETUSERINFO)
    Call<BaseResult<UserBean>> getUserInfo(@QueryMap Map<String, Object> map);

    //
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.CHANGEUSERINFO)
    @FormUrlEncoded
    Call<BaseResult> changeUserInfo(@FieldMap Map<String, Object> map);
    //

    @GET(Urls.WITHDRAWALSAPPLY)
    Call<BaseResult> withdrawalsapply(@QueryMap Map<String, Object> map);

    //
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.SIGN)
    @FormUrlEncoded
    Call<BaseResult<SignBean>> sign(@FieldMap Map<String, Object> map);


    //
    @GET(Urls.QUESTIONTYPE)
    Call<BaseResult<List<QuestionTypeBean>>> questionType(@QueryMap Map<String, Object> map);

    //
    @GET(Urls.CHECKTOPRESULT)
    Call<BaseResult<QuestionTopResult>> checkTopResult(@QueryMap Map<String, Object> map);


    //
    @GET(Urls.getNotRoom)
    Call<BaseResult> getNotRoom(@QueryMap Map<String, Object> map);

    //
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.SETQUESTION)
    @FormUrlEncoded
    Call<BaseResult> setQuestion(@FieldMap Map<String, Object> map);


    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.bindQrCode)
    @FormUrlEncoded
    Call<BaseResult> bindQrCode(@FieldMap Map<String, Object> map);


    @GET(Urls.GETUPLOADTOKEN)
    Call<BaseResult<String>> getUploadToken(@QueryMap Map<String, Object> map);


    @GET(Urls.USERPASSBOOK)
    Call<BaseResult<MyCardBean>> getPassbookList(@QueryMap Map<String, Object> map);


    @GET(Urls.BINDINGALIPAY)
    Call<BaseResult> bindingAlipay(@QueryMap Map<String, Object> map);


    @GET(Urls.changeALIPAY)
    Call<BaseResult> changeALIPAY(@QueryMap Map<String, Object> map);


    @GET(Urls.TRYPLAYLIST)
    Call<BaseResult<GameBean>> tryPlayList(@QueryMap Map<String, Object> map);


    @GET(Urls.QUERYMYVIPS)
    Call<BaseResult<List<VipBean>>> queryMyVips(@QueryMap Map<String, Object> map);


    @GET(Urls.tpGame)
    Call<BaseResult<GameBean>> queryGameList(@QueryMap Map<String, Object> map);

    @GET(Urls.discountList)
    Call<BaseResult<List<DiscountCardBean>>> discountList(@QueryMap Map<String, Object> map);


    @GET(Urls.toPlay)
    Call<BaseResult> toPlay(@QueryMap Map<String, Object> map);


    @GET(Urls.createOrder)
    Call<BaseResult> createOrder(@QueryMap Map<String, Object> map);


    @GET(Urls.payType)
    Call<BaseResult<PayBean>> payType(@QueryMap Map<String, Object> map);


    @GET(Urls.paySuccess)
    Call<BaseResult> paySuccess(@QueryMap Map<String, Object> map);


    @GET(Urls.getEncourage)
    Call<BaseResult<EncourageBean>> getEncourage(@QueryMap Map<String, Object> map);


    @GET(Urls.getNews)
    Call<BaseResult<List<NewsListBean>>> getNews(@QueryMap Map<String, Object> map);


    @GET(Urls.readyWithdrawals)
    Call<BaseResult<WithDrawBean>> readyWithdrawals(@QueryMap Map<String, Object> map);


    @GET(Urls.getPCDDUrl)
    Call<BaseResult> getPCDDUrl(@QueryMap Map<String, Object> map);


    @GET(Urls.queryFList)
    Call<BaseResult<GameCompanyBean>> queryFList(@QueryMap Map<String, Object> map);


    @GET(Urls.userAddressList)
    Call<BaseResult<List<AddressBean>>> userAddressList(@QueryMap Map<String, Object> map);

    //    添加地址
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.addAddress)
    @FormUrlEncoded
    Call<BaseResult> addAddress(@FieldMap Map<String, Object> map);

    //    区域列表
    @GET(Urls.areaList)
    Call<BaseResult<List<AreaBean>>> areaList(@QueryMap Map<String, Object> map);

    //    查询地址
    @GET(Urls.addressDetails)
    Call<BaseResult<UserAddressBean>> getAddress(@QueryMap Map<String, Object> map);

    //    修改地址
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.editList)
    @FormUrlEncoded
    Call<BaseResult> editList(@FieldMap Map<String, Object> map);

    //    删除地址
    @GET(Urls.removeAddress)
    Call<BaseResult> removeAddress(@QueryMap Map<String, Object> map);

    //    支付密码
    @GET(Urls.updatePayPassword)
    Call<BaseResult> updatePayPassword(@QueryMap Map<String, Object> map);


    //    参数配置
    @GET(Urls.homePageDispose)
    Call<BaseResult<DisposeBean>> homePageDispose(@Query("channelCode") String channelCode,
                                                  @Query("versionNo") String versionNo,
                                                  @Query("equipmentType") String equipmentType);

    //    资讯金币上线
    @GET(Urls.getRewardConifg)
    Call<BaseResult<InformationRewardCoinBean>> getRewardConifg(@QueryMap Map<String, Object> map);

    //    瑞狮资讯
    @GET(Urls.getHIPPONews)
    Call<BaseResult<List<HippoNewsBean>>> getHIPPONews(@QueryMap Map<String, Object> map);

    //    新手任务游戏加成券
    @GET(Urls.getAddCoupon)
    Call<BaseResult<AdditionBean>> getAddCoupon(@QueryMap Map<String, Object> map);

    //    新手任务 新手引导
    @GET(Urls.noviceBut)
    Call<BaseResult<NoviceButBean>> noviceBut(@QueryMap Map<String, Object> map);

    //    新手任务游戏加成券
    @GET(Urls.userFirstLog)
    Call<BaseResult> userFirstLog(@QueryMap Map<String, Object> map);

    @GET(Urls.queryBanner)
    Call<BaseResult<List<BannerBean>>> queryBanner(@Query("position") String position);

    //   读资讯获取金币
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.readNewZXReward)
    @FormUrlEncoded
    Call<BaseResult> readNewZXReward(@FieldMap Map<String, Object> map);


    //用户反馈
    @GET(Urls.feedback)
    Call<BaseResult> feedback(@QueryMap Map<String, Object> map);

    //猪猪用户游戏权限
    @GET(Urls.hasGameRight)
    Call<BaseResult<HasGameRightBean>> hasGameRight(@QueryMap Map<String, Object> map);

    //删除试玩任务
    @GET(Urls.remove)
    Call<BaseResult> remove(@QueryMap Map<String, Object> map);

    //随机试玩
    @GET(Urls.recommendGame)
    Call<BaseResult<ChangeBean>> recommendGame(@QueryMap Map<String, Object> map);

    //官方客服
    @GET(Urls.contactInformation)
    Call<BaseResult<ServiceBean>> contactInformation();

    //购买运营总监生订单
    @GET(Urls.tradeTame)
    Call<BaseResult<COOBean>> tradeTame(@QueryMap Map<String, Object> map);

    //签到赚消息
    @GET(Urls.qdzList)
    Call<BaseResult<List<MarqueeviewMessageBean>>> qdzList();

    //获取补签卡
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.retroactivecard)
    @FormUrlEncoded
    Call<BaseResult> retroactivecard(@FieldMap Map<String, Object> map);

    //获取今日完成任务数，今日赚到金币,补签卡数
    @GET(Urls.todayFinish)
    Call<BaseResult<SignMakeEarnMoneyBean>> todayFinish(@QueryMap Map<String, Object> map);

    //签到赚列表
    @GET(Urls.signmake)
    Call<BaseResult<SignMakeBean>> signmake(@QueryMap Map<String, Object> map);

    //签到赚获取游戏列表
    @GET(Urls.signinGames)
    Call<BaseResult<List<SignMakeGameBean>>> signinGames(@QueryMap Map<String, Object> map);

    //签到赚获取更换游戏
    @GET(Urls.recommendGameNew)
    Call<BaseResult<SignMakeChangeBean>> recommendGameNew(@QueryMap Map<String, Object> map);

    //使用补签卡
    @GET(Urls.useCard)
    Call<BaseResult> useCard(@QueryMap Map<String, Object> map);

    //获取签到赚游戏链接
    @GET(Urls.getqdGameUrl)
    Call<BaseResult> getqdGameUrl(@QueryMap Map<String, Object> map);

    //获取签到赚奖励
    @GET(Urls.receiveReward)
    Call<BaseResult> receiveReward(@QueryMap Map<String, Object> map);

    //删除签到赚试玩
    @GET(Urls.removeQDZ)
    Call<BaseResult> removeQDZ(@QueryMap Map<String, Object> map);

    //购买运营总监开启的支付方式
    @GET(Urls.openPay)
    Call<BaseResult<OpenPayTypeBean>> openPay(@QueryMap Map<String, Object> map);

    //赚钱任务
    @GET(Urls.getFList)
    Call<BaseResult<TaskBean>> getFList(@QueryMap Map<String, Object> map);

    //赚钱任务详情
    @GET(Urls.tptaskInfo)
    Call<BaseResult<TaskDetailBean>> tptaskInfo(@QueryMap Map<String, Object> map);

    //领取任务
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.userTptask)
    @FormUrlEncoded
    Call<BaseResult<GetTaskSuccessBean>> userTptask(@FieldMap Map<String, Object> map);

    //    //提交任务
//    @Headers("Content-Type: application/x-www-form-urlencoded")
//    @POST(Urls.userTptaskSubmit)
//    @FormUrlEncoded
//    Call<BaseResult> userTptaskSubmit(@FieldMap Map<String, Object> map);
    // 提交任务
    @GET(Urls.userTptaskSubmit)
    Call<BaseResult> userTptaskSubmit(@QueryMap Map<String, Object> map);

    // 任务进度列表
    @GET(Urls.queryFlist)
    Call<BaseResult<TaskProgressBean>> queryFlist(@QueryMap Map<String, Object> map);

    // 任务进度消息列表
    @GET(Urls.kszList)
    Call<BaseResult<List<MarqueeviewMessageBean>>> kszList();

    // 取消任务
    @GET(Urls.givein)
    Call<BaseResult> givein(@QueryMap Map<String, Object> map);

    // 预约任务
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.addYY)
    @FormUrlEncoded
    Call<BaseResult> addYY(@FieldMap Map<String, Object> map);

    // 用户每日活跃记录
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.uerActiveDd)
    @FormUrlEncoded
    Call<BaseResult> uerActiveDd(@FieldMap Map<String, Object> map);

    //兑换商品
    @GET(Urls.exchangeGoods)
    Call<BaseResult> exchangeGoods(@QueryMap Map<String, Object> map);

    //奖品绑定邮寄地址
    @GET(Urls.addressBinding)
    Call<BaseResult> addressBinding(@QueryMap Map<String, Object> map);


    //是否存在未读消息
    @GET(Urls.haveUnRead)
    Call<BaseResult<HaveReadBean>> haveUnRead(@QueryMap Map<String, Object> map);

    //弹窗公告
    @GET(Urls.noticeFrame)
    Call<BaseResult<NoticeBean>> noticeFrame(@QueryMap Map<String, Object> map);

    //用户读公告
    @GET(Urls.readnoticeFrame)
    Call<BaseResult> readnoticeFrame(@QueryMap Map<String, Object> map);

    //获取卡密
    @GET(Urls.getCardPassword)
    Call<BaseResult> getCardPassword(@QueryMap Map<String, Object> map);

    //提现金额
    @GET(Urls.cashPrice)
    Call<BaseResult<List<UserWithDrawBean>>> cashPrice(@QueryMap Map<String, Object> map);

    //绑定微信
    @GET(Urls.bindingWxNumber)
    Call<BaseResult> bindingWxNumber(@QueryMap Map<String, Object> map);

    //修改微信
    @GET(Urls.changeWxNumber)
    Call<BaseResult> changeWxNumber(@QueryMap Map<String, Object> map);

    //vip购买滚动消息
    @GET(Urls.selectVipNews)
    Call<BaseResult<List<SelectVipNewsBean>>> selectVipNews(@QueryMap Map<String, Object> map);

    //vip列表
    @GET(Urls.vipList)
    Call<BaseResult<List<VipListBean>>> vipList(@QueryMap Map<String, Object> map);

    ////VIP详情
    @GET(Urls.vipinfo)
    Call<BaseResult<VipInfoBean>> vipinfo(@QueryMap Map<String, Object> map);

    //VIP领取活跃金匹配任务
    @GET(Urls.recommendGameVip)
    Call<BaseResult<VIpTaskGameBean>> recommendGameVip(@QueryMap Map<String, Object> map);

    //VIP任务获取链接
    @GET(Urls.getHyjGameUrl)
    Call<BaseResult> getHyjGameUrl(@QueryMap Map<String, Object> map);

    //VIP任务领取活跃金
    @GET(Urls.receiveActiveNews)
    Call<BaseResult<EncourageBean>> receiveActiveNews(@QueryMap Map<String, Object> map);

    //删除vip任务
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.deleteActiveTask)
    @FormUrlEncoded
    Call<BaseResult> deleteActiveTask(@FieldMap Map<String, Object> map);

    //获取游戏类型
    @GET(Urls.tpGameType)
    Call<BaseResult<List<GameTypeBean>>> tpGameType();

    //获取游戏列表
    @GET(Urls.tpGameList)
    Call<BaseResult<GameBean>> tpGameList(@QueryMap Map<String, Object> map);

    //获取提现次数
    @GET(Urls.cashNum)
    Call<BaseResult<CashNumBean>> cashNum(@QueryMap Map<String, Object> map);

    //提现任务--选择提现金额
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.userCash)
    @FormUrlEncoded
    Call<BaseResult> userCash(@FieldMap Map<String, Object> map);

    //提现任务--任务完成进度
    @GET(Urls.userCashtaskInfo)
    Call<BaseResult<UserWithdrawTaskinfoBean>> userCashtaskInfo(@QueryMap Map<String, Object> map);

    //提现任务--匹配游戏任务
    @GET(Urls.recommendGameCash)
    Call<BaseResult<WithDrawTaskGameBean>> recommendGameCash(@QueryMap Map<String, Object> map);

    //提现任务--获取游戏链接
    @GET(Urls.getTxGameUrl)
    Call<BaseResult> getTxGameUrl(@QueryMap Map<String, Object> map);

    //今日完成任务数
    @GET(Urls.queryTodayFinish)
    Call<BaseResult> queryTodayFinish(@QueryMap Map<String, Object> map);

    //提现任务--发起提现
    @GET(Urls.cashLaunch)
    Call<BaseResult> cashLaunch(@QueryMap Map<String, Object> map);

    //每日签到--奖励列表
    @GET(Urls.appList)
    Call<BaseResult<List<EverydayRedListBean>>> appList(@QueryMap Map<String, Object> map);

    //每日签到--查询当天签到情况
    @GET(Urls.getDaySign)
    Call<BaseResult<EverydayRedTaskinfoBean>> getDaySign(@QueryMap Map<String, Object> map);

    //每日签到--匹配游戏
    @GET(Urls.recommendGameSign)
    Call<BaseResult<EverydayRedGameBean>> recommendGameSign(@QueryMap Map<String, Object> map);

    //每日签到--绑定任务
    @GET(Urls.getMrhbGameUrl)
    Call<BaseResult> getMrhbGameUrl(@QueryMap Map<String, Object> map);

    //每日签到--领取任务奖励
    @GET(Urls.receiveCoin)
    Call<BaseResult> receiveCoin(@QueryMap Map<String, Object> map);

    //每日签到--推送
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.noticeReady)
    @FormUrlEncoded
    Call<BaseResult> noticeReady(@FieldMap Map<String, Object> map);

    //新人任务
    @GET(Urls.appUserList)
    Call<BaseResult<List<NewUserTaskBean>>> appUserList(@QueryMap Map<String, Object> map);

    //查询用户未完成的任务
    @GET(Urls.getConductTask)
    Call<BaseResult<ConductTaskBean>> getConductTask(@QueryMap Map<String, Object> map);

    //随机配游戏和应用
    @GET(Urls.recommendGameTask)
    Call<BaseResult<ChangeBean>> recommendGameTask(@QueryMap Map<String, Object> map);

    //获取快速赚链接
    @GET(Urls.buildUrl)
    Call<BaseResult> buildUrl(@QueryMap Map<String, Object> map);

    //视频入口开关
    @GET(Urls.videoCount)
    Call<BaseResult<VideoswitchBean>> videoCount(@QueryMap Map<String, Object> map);

    //查看用户观看视频结果
    @GET(Urls.videoUser)
    Call<BaseResult<VideoCountBean>> videoUser(@QueryMap Map<String, Object> map);

    //用户点击看视频
    @GET(Urls.videoTimeUser)
    Call<BaseResult<VideoTimeUserBean>> videoTimeUser(@QueryMap Map<String, Object> map);

    //早起打卡首页
    @GET(Urls.earlycheckin)
    Call<BaseResult<EarlyClockinfo>> earlycheckin(@QueryMap Map<String, Object> map);

    //报名
    @GET(Urls.earlycheckinjoin)
    Call<BaseResult> earlycheckinjoin(@QueryMap Map<String, Object> map);

    //打卡
    @GET(Urls.earlycheckinclock)
    Call<BaseResult<EarlyClockBean>> earlycheckinclock(@QueryMap Map<String, Object> map);

    //查询补签卡
    @GET(Urls.earlycheckincard)
    Call<BaseResult<EarlyClockcardBean>> earlycheckincard(@QueryMap Map<String, Object> map);

    //幸运榜
    @GET(Urls.earlycheckinappList)
    Call<BaseResult<List<EarlyClockLuckBean>>> earlycheckinappList(@QueryMap Map<String, Object> map);

    //查询小游戏奖励配置
    @GET(Urls.xyxRewardConfig)
    Call<BaseResult<BaoquGamerewardinformation>> xyxRewardConfig(@QueryMap Map<String, Object> map);

    //小游戏  领取奖励
    @GET(Urls.xyxReward)
    Call<BaseResult<BaoquGamereward>> xyxReward(@QueryMap Map<String, Object> map);

    //验证通讯录,通话记录
    @GET(Urls.checkingMailList)
    Call<BaseResult<Res>> checkingMailList(@QueryMap Map<String, Object> map);

    //陀螺仪数据
    @GET(Urls.addGyro)
    Call<BaseResult> addGyro(@QueryMap Map<String, Object> map);

    //查看新用户看视频次数
    @GET(Urls.newUserVideo)
    Call<BaseResult<FirstVideoBean>> newUserVideo(@QueryMap Map<String, Object> map);

    //查看新用户看视频金币
    @GET(Urls.newUserVideoCoin)
    Call<BaseResult<FirstVideoBean>> newUserVideoCoin(@QueryMap Map<String, Object> map);

    //微信登陆
    @GET(Urls.wechatLogin)
    Call<BaseResult<UserBean>> wechatLogin(@QueryMap Map<String, Object> map);

    //游戏联想查询
    @GET(Urls.lenovoList)
    Call<BaseResult<GameBean>> lenovoList(@QueryMap Map<String, Object> map);

    //获取历史搜索记录
    @GET(Urls.userSearchLogs)
    Call<BaseResult> userSearchLogs(@QueryMap Map<String, Object> map);

    //获取推荐游戏
    @GET(Urls.tjList)
    Call<BaseResult> tjList(@QueryMap Map<String, Object> map);

    //删除历史搜索记录
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST(Urls.removeAll)
    @FormUrlEncoded
    Call<BaseResult> removeAll(@FieldMap Map<String, Object> map);
}
