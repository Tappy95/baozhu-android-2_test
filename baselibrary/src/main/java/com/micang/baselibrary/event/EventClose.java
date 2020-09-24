package com.micang.baselibrary.event;

/**
 * 用来传递分享
 */
public class EventClose {

    public int code;
    public int data;


    public EventClose(int code, int data) {
        this.code = code;
        this.data = data;
    }
}
