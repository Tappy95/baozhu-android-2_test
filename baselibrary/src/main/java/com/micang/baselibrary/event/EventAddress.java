package com.micang.baselibrary.event;

public class EventAddress<T> {

    public int code;
    public T data;


    public EventAddress(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
