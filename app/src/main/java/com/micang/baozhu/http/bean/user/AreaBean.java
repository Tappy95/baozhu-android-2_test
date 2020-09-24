package com.micang.baozhu.http.bean.user;

/**
 * @author
 * @version 1.0
 * @Package com.dizoo.invite.http.bean.user
 * @time 2019/4/24 16:43
 * @describe describe
 */
public class AreaBean {

    /**
     * data : [{"spell":"shixiaqu","level":2,"shortSpell":"sxq","fullName":"天津市/市辖区","pageSize":0,"pageNum":0,"parentId":120000,"areaCode":"","name":"市辖区","postCode":"","id":120100,"shortName":"","isDirect":0},{"spell":"xian","level":2,"shortSpell":"x","fullName":"天津市/县","pageSize":0,"pageNum":0,"parentId":120000,"areaCode":"","name":"县","postCode":"","id":120200,"shortName":"","isDirect":0}]
     * message : 操作成功
     * statusCode : 2000
     * token : 4099ac907466a9a6be506560a0e84b20
     */

    /**
     * spell : shixiaqu
     * level : 2
     * shortSpell : sxq
     * fullName : 天津市/市辖区
     * pageSize : 0
     * pageNum : 0
     * parentId : 120000
     * areaCode :
     * name : 市辖区
     * postCode :
     * id : 120100
     * shortName :
     * isDirect : 0
     */

    public int level;
    public String fullName;
    public String parentId;
    public String areaCode;
    public String name;
    public String postCode;
    public String id;

}
