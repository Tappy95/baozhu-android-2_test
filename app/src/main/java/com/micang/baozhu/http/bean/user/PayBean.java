package com.micang.baozhu.http.bean.user;

/**
 * @author
 * @version 1.0
 * @Package com.dizoo.invite.http.bean.user
 * @time 2019/3/20 19:23
 * @describe describe
 */
public class PayBean {

    /**
     * statusCode : 2000
     * message : 操作成功
     * data : {"msg":1,"res":{"code":null,"msg":null,"subCode":null,"subMsg":null,"body":"alipay_sdk=alipay-sdk-java-3.3.49.ALL&app_id=2019031363519454&biz_content=%7B%22body%22%3A%22%E8%B4%AD%E4%B9%B0vip%22%2C%22out_trade_no%22%3A%2220190320192158ye23qgv%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E8%B4%AD%E4%B9%B0vip%22%2C%22timeout_express%22%3A%22144m%22%2C%22total_amount%22%3A%220.00%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=https%3A%2F%2Fapi.baozhu8.com%2Falipaycallback&sign=k%2FDNnknmj0cHE1CtqQ0llm67andG2INGDtemkqUpL7qQqjBVAeu6t3pLDgK4NFI48zYEAVL5iL7NqXGreX6XKveum%2FkE74ToCNo0GtzETImPTurCScZV4b0c3fWdlXbT3Bw%2BkozQvEoPoGyn2Rr9hsjBF82fIFq%2FBNgIBNi2UkMM5wRj55hQh5gxJnvt2zFrMV0MiyJot3%2F98VVntjaWo9vfnb03nJKRHeeZ8bg%2FrvxgFIL%2BLwToCvCSavMFrEUs9WKt%2FRssi5qBZgme1F92leRvvoN8EiMtNo9aNfsNFAXxiTyUE0TcX1%2FctV49g6Oyc9ZsOD3V6fzxIQ6evU4moA%3D%3D&sign_type=RSA2&timestamp=2019-03-20+19%3A21%3A58&version=1.0","params":null,"outTradeNo":null,"sellerId":null,"totalAmount":null,"tradeNo":null,"errorCode":null,"success":true}}
     * token : null
     * respType : JSON
     * changeMap : null
     * needChange : false
     * success : true
     */


        /**
         * msg : 1
         * res : {"code":null,"msg":null,"subCode":null,"subMsg":null,"body":"alipay_sdk=alipay-sdk-java-3.3.49.ALL&app_id=2019031363519454&biz_content=%7B%22body%22%3A%22%E8%B4%AD%E4%B9%B0vip%22%2C%22out_trade_no%22%3A%2220190320192158ye23qgv%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E8%B4%AD%E4%B9%B0vip%22%2C%22timeout_express%22%3A%22144m%22%2C%22total_amount%22%3A%220.00%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=https%3A%2F%2Fapi.baozhu8.com%2Falipaycallback&sign=k%2FDNnknmj0cHE1CtqQ0llm67andG2INGDtemkqUpL7qQqjBVAeu6t3pLDgK4NFI48zYEAVL5iL7NqXGreX6XKveum%2FkE74ToCNo0GtzETImPTurCScZV4b0c3fWdlXbT3Bw%2BkozQvEoPoGyn2Rr9hsjBF82fIFq%2FBNgIBNi2UkMM5wRj55hQh5gxJnvt2zFrMV0MiyJot3%2F98VVntjaWo9vfnb03nJKRHeeZ8bg%2FrvxgFIL%2BLwToCvCSavMFrEUs9WKt%2FRssi5qBZgme1F92leRvvoN8EiMtNo9aNfsNFAXxiTyUE0TcX1%2FctV49g6Oyc9ZsOD3V6fzxIQ6evU4moA%3D%3D&sign_type=RSA2&timestamp=2019-03-20+19%3A21%3A58&version=1.0","params":null,"outTradeNo":null,"sellerId":null,"totalAmount":null,"tradeNo":null,"errorCode":null,"success":true}
         */

        public int msg;
        public ResBean res;

        public static class ResBean {
            /**
             * code : null
             * msg : null
             * subCode : null
             * subMsg : null
             * body : alipay_sdk=alipay-sdk-java-3.3.49.ALL&app_id=2019031363519454&biz_content=%7B%22body%22%3A%22%E8%B4%AD%E4%B9%B0vip%22%2C%22out_trade_no%22%3A%2220190320192158ye23qgv%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E8%B4%AD%E4%B9%B0vip%22%2C%22timeout_express%22%3A%22144m%22%2C%22total_amount%22%3A%220.00%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=https%3A%2F%2Fapi.baozhu8.com%2Falipaycallback&sign=k%2FDNnknmj0cHE1CtqQ0llm67andG2INGDtemkqUpL7qQqjBVAeu6t3pLDgK4NFI48zYEAVL5iL7NqXGreX6XKveum%2FkE74ToCNo0GtzETImPTurCScZV4b0c3fWdlXbT3Bw%2BkozQvEoPoGyn2Rr9hsjBF82fIFq%2FBNgIBNi2UkMM5wRj55hQh5gxJnvt2zFrMV0MiyJot3%2F98VVntjaWo9vfnb03nJKRHeeZ8bg%2FrvxgFIL%2BLwToCvCSavMFrEUs9WKt%2FRssi5qBZgme1F92leRvvoN8EiMtNo9aNfsNFAXxiTyUE0TcX1%2FctV49g6Oyc9ZsOD3V6fzxIQ6evU4moA%3D%3D&sign_type=RSA2&timestamp=2019-03-20+19%3A21%3A58&version=1.0
             * params : null
             * outTradeNo : null
             * sellerId : null
             * totalAmount : null
             * tradeNo : null
             * errorCode : null
             * success : true
             */
            public Object code;
            public Object msg;
            public Object subMsg;
            public String body;
            public Object params;
            public Object sellerId;
            public Object totalAmount;
            public Object tradeNo;
            public Object errorCode;
            public boolean success;
            public String bzPackage;
            public String appid;
            public String outTradeNo;
            public String sign;
            public String partnerid;
            public String prepayid;
            public String noncestr;
            public String timestamp;
        }
}
