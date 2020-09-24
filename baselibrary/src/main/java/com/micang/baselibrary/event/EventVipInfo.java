package com.micang.baselibrary.event;

public class EventVipInfo<T> {

    public int code;
    public T data;


    public EventVipInfo(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
