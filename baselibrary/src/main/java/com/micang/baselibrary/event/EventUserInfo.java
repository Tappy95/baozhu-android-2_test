package com.micang.baselibrary.event;

public class EventUserInfo<T> {

    public int code;
    public T data;


    public EventUserInfo(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
