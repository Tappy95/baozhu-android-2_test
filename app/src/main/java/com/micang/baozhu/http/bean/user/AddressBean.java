package com.micang.baozhu.http.bean.user;

/**
 * @version 1.0
 * @Package com.dizoo.invite.http.bean.user
 * @time 2019/4/24 10:51
 * @describe describe
 */
public class AddressBean {

    /**
     * data : [{"isDefault":1,"receiver":"哈哈哈","mobile":2147483647,"fullName":"北京市 市辖区 东城区","detailAddress":"详细地址","addressId":1},{"isDefault":2,"receiver":"哈哈哈","mobile":2147483647,"fullName":"北京市 市辖区 西城区","detailAddress":"详细地址","addressId":3}]
     * message : 操作成功
     * statusCode : 2000
     * token : 4099ac907466a9a6be506560a0e84b20
     */


    /**
     * isDefault : 1
     * receiver : 哈哈哈
     * mobile : 2147483647
     * fullName : 北京市 市辖区 东城区
     * detailAddress : 详细地址
     * addressId : 1
     */
    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean select = false;
    public int isDefault;
    public String receiver;
    public String mobile;
    public String fullName;
    public String detailAddress;
    public String addressId;
}
