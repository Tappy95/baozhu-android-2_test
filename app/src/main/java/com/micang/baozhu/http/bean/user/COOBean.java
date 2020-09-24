package com.micang.baozhu.http.bean.user;

import java.io.Serializable;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/5/30 19:47
 * @describe describe
 */
public class COOBean implements Serializable {
    /**
     * data : {"res":1,"payPurpose":3,"price":39,"outTradeNo":"201905301946436c18eks","effectiveDay":30}
     * message : 操作成功
     * statusCode : 2000
     * token : 4099ac907466a9a6be506560a0e84b20

        /**
         * res : 1
         * payPurpose : 3
         * price : 39
         * outTradeNo : 201905301946436c18eks
         * effectiveDay : 30
         */

        public int res;
        public int payPurpose;
        public String price;
        public String outTradeNo;
        public String effectiveDay;

}
