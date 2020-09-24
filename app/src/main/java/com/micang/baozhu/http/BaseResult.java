package com.micang.baozhu.http;


public class BaseResult<T> {


    /**
     * data : {"flag":false,"mobile":"15755171015"}
     * message : 操作成功
     * statusCode : 2000
     */

    public T data;
    public String message;
    public String statusCode;
    public String token;


}
