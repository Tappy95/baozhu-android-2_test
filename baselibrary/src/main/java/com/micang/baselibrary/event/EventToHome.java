package com.micang.baselibrary.event;

public class EventToHome<T> {

    public int code;
    public T data;


    public EventToHome(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
