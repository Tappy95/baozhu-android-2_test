package com.micang.baselibrary.event;

public class Event<T> {

    public int code;
    public T data;


    public Event(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
