package com.micang.baozhu.http.bean.home;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/8/20 20:18
 * @describe describe
 */
public class GameTypeBean {

    /**
     * data : [{"createTime":1552620565533,"typeName":"棋牌","pageSize":0,"id":1,"pageNum":0,"status":1},{"createTime":1552910975114,"typeName":"加班，开心","pageSize":0,"id":2,"pageNum":0,"status":1}]
     * message : 操作成功
     * statusCode : 2000
     */


    /**
     * createTime : 1552620565533
     * typeName : 棋牌
     * pageSize : 0
     * id : 1
     * pageNum : 0
     * status : 1
     */
    public boolean isSelected;  //是否选择
    public String typeName;
    public int id;
    public int status;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
