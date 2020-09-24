package com.micang.baozhu.http.bean.login;

import java.io.Serializable;

/**
 * @version 1.0
 * @Package com.dizoo.http.bean.login
 * @time 2019/3/2 16:18
 * @describe describe
 */
public class GetSmsBean implements Serializable {
    /**
     * data : {"res":true,"message":"验证码发送成功"}
     * message : 操作成功
     * statusCode : 2000
     */
    /**
     * res : true
     * message : 验证码发送成功
     * codeKey:15669085249701411
     */
    public boolean res;
    public String message;
    public String codeKey;
}
