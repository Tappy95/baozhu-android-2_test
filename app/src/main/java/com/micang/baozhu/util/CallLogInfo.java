package com.micang.baozhu.util;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/10/30 15:33
 * @describe 通话记录bean
 */
public class CallLogInfo {
    public String number;
    public long date;
    public int type;

    public CallLogInfo() {
        super();
    }

    public CallLogInfo(String number, long date, int type) {
        super();
        this.number = number;
        this.date = date;
        this.type = type;
    }
}
