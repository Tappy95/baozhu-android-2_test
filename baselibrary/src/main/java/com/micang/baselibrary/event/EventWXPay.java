package com.micang.baselibrary.event;

public class EventWXPay<T> {

    public int code;
    public T data;


    public EventWXPay(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
