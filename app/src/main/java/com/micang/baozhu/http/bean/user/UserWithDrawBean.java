package com.micang.baozhu.http.bean.user;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/7/26 11:02
 * @describe describe
 */
public class UserWithDrawBean {
    /**
     * data : [{"isTask":2,"price":1,"pageSize":0,"orders":1,"id":1,"pageNum":0},{"isTask":2,"price":2,"pageSize":0,"orders":2,"id":2,"pageNum":0},{"isTask":2,"price":5,"pageSize":0,"orders":5,"id":3,"pageNum":0},{"isTask":2,"price":10,"pageSize":0,"orders":10,"id":4,"pageNum":0},{"isTask":2,"price":20,"pageSize":0,"orders":20,"id":5,"pageNum":0},{"isTask":2,"price":30,"pageSize":0,"orders":30,"id":6,"pageNum":0},{"isTask":2,"price":40,"pageSize":0,"orders":40,"id":7,"pageNum":0},{"isTask":2,"price":50,"pageSize":0,"orders":50,"id":8,"pageNum":0}]
     * message : 操作成功
     * statusCode : 2000
     * token : 7ba4d9d9f448b0e0e5f02b2998067cc8
     */

    public UserWithDrawBean(int isTask, String price) {
        this.isTask = isTask;
        this.price = price;
    }

    public UserWithDrawBean(int isTask, String price, boolean isSelected) {
        this.isTask = isTask;
        this.price = price;
        this.isSelected = isSelected;
    }

    /**
     * isTask : 2
     * price : 1
     * pageSize : 0
     * orders : 1
     * id : 1
     * pageNum : 0
     */

    public int isTask;       //是否需要完成任务1是2否
    public String price;     //提现金额
    public boolean isSelected;  //是否选择

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
