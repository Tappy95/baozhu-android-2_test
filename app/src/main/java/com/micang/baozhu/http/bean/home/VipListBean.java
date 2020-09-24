package com.micang.baozhu.http.bean.home;

import java.io.Serializable;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/8/7 17:39
 * @describe describe
 */
public class VipListBean implements Serializable {


    /**
     * data : [{"isBuy":0,"isPay":0,"price":0.01,"name":"试玩加成VIP","logo":"https://image.baozhu8.com/test_20190319192747840.png","id":10,"isRenew":1},{"isBuy":0,"isPay":0,"price":0.01,"name":"VIP体验卡","logo":"https://image.baozhu8.com/test_20190319192801594.png","id":9,"isRenew":1},{"isBuy":0,"isPay":0,"price":0.01,"name":"白银卡","logo":"https://image.baozhu8.com/test_2019031919281124.png","id":8,"isRenew":1},{"isBuy":0,"isPay":0,"price":0.01,"name":"黄金卡","logo":"https://image.baozhu8.com/test_20190319192827900.png","id":7,"isRenew":1}]
     * message : 操作成功
     * statusCode : 2000
     * token : 7ba4d9d9f448b0e0e5f02b2998067cc8
     */


    /**
     * isBuy : 0
     * isPay : 0
     * price : 0.01
     * name : 试玩加成VIP
     * logo : https://image.baozhu8.com/test_20190319192747840.png
     * id : 10
     * isRenew : 1
     */

    public int isBuy;            //是否有效， 0失效，其他有效
    public int isPay;           //是否购买过，0没有，其他购买过
    public String price;        //价格
    public String name;         //vip名称
    public String backgroundImg; //vip背景图
    public String logo;         //小图标
    public int id;              //id
    public int isRenew;         //	是否可以续费1是2否
    public int highVip;         //高额赚vip	2是 1否

}
