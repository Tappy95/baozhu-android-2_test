package com.micang.baselibrary.event;

public class EventBalance<T> {

    public int code;
    public T data;


    public EventBalance(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
