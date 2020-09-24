package com.micang.baozhu.http.bean.user;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户信息模型
 */
public class MUserInfo   implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userId; // 用户id
	
	private String userName; // 真实姓名
	
	private Integer sex; // 性别 1-男 2-女
	
	private Long birthday; // 生日
	
	private String mobile; // 手机号
	
	private String aliasName; // 昵称
	
	private String identity; // 登陆账号
	
	private String socialDigitalNum; // 证件号
	
	private Integer digitalNumType; // 证件类型
	
	private String profile; // 头像地址
	
	private BigDecimal balance; // 余额
	
	private BigDecimal jadeCabbage; // 代币
	
	private Integer coin; // 金币
	
	private BigDecimal reward; // 获取的总奖励金额
	
	private Integer apprentice; // 学徒数量
	
	private String qrCode; // 邀请二维码地址
	
	private String level; // 等级名称 L0 L1

	private Integer levelValue; // 等级值，经验值
	
	private String password; // 登陆密码
	
	private Long createTime; // 注册时间
	
	private Long updateTime; // 最后一次修改时间
	
	private String referrer; // 邀请人
	
	private String referrerName; // 邀请人姓名

	private String referrerMobile; // 邀请人手机号
	
	private Long recommendedTime; // 推荐时间
	
	private String imei;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Long getBirthday() {
		return birthday;
	}

	public void setBirthday(Long birthday) {
		this.birthday = birthday;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getSocialDigitalNum() {
		return socialDigitalNum;
	}

	public void setSocialDigitalNum(String socialDigitalNum) {
		this.socialDigitalNum = socialDigitalNum;
	}

	public Integer getDigitalNumType() {
		return digitalNumType;
	}

	public void setDigitalNumType(Integer digitalNumType) {
		this.digitalNumType = digitalNumType;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getJadeCabbage() {
		return jadeCabbage;
	}

	public void setJadeCabbage(BigDecimal jadeCabbage) {
		this.jadeCabbage = jadeCabbage;
	}

	public Integer getCoin() {
		return coin;
	}

	public void setCoin(Integer coin) {
		this.coin = coin;
	}

	public BigDecimal getReward() {
		return reward;
	}

	public void setReward(BigDecimal reward) {
		this.reward = reward;
	}

	public Integer getApprentice() {
		return apprentice;
	}

	public void setApprentice(Integer apprentice) {
		this.apprentice = apprentice;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Integer getLevelValue() {
		return levelValue;
	}

	public void setLevelValue(Integer levelValue) {
		this.levelValue = levelValue;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public Long getRecommendedTime() {
		return recommendedTime;
	}

	public void setRecommendedTime(Long recommendedTime) {
		this.recommendedTime = recommendedTime;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getReferrerName() {
		return referrerName;
	}

	public void setReferrerName(String referrerName) {
		this.referrerName = referrerName;
	}

	public String getReferrerMobile() {
		return referrerMobile;
	}

	public void setReferrerMobile(String referrerMobile) {
		this.referrerMobile = referrerMobile;
	}
}
