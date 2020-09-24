package com.micang.baselibrary.event;

public class EventQuestionResult<T> {

    public int code;
    public T data;


    public EventQuestionResult(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
