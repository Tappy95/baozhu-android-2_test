package com.micang.baselibrary.event;

public class EventWXBind<T> {

    public int code;
    public T data;


    public EventWXBind(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
