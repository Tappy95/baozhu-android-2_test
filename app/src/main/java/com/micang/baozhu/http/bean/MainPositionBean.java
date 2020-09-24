package com.micang.baozhu.http.bean;

/**
  * @version 1.0
 * @Package com.dizoo.http.bean
 * @time 2019/3/7 22:02
 * @describe describe
 */
public class MainPositionBean {

    private int code;
    private int position;

    public MainPositionBean(int position){
        this.position = position;
    }
    public MainPositionBean(int code,int position){
        this.code = code;
        this.position = position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
