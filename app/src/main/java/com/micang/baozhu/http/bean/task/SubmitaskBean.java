package com.micang.baozhu.http.bean.task;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/28 22:28
 * @describe describe
 */
public class SubmitaskBean {

//    private String lTpTaskId; // 领取记录id
//
//    private String tpTaskId; // 任务id

    private String submitId; // 提交材料id
    private String submitName; // 提交材料名称

    private String submitValue; // 提交材料值

//    public String getlTpTaskId() {
//        return lTpTaskId;
//    }
//
//    public void setlTpTaskId(String lTpTaskId) {
//        this.lTpTaskId = lTpTaskId;
//    }
//
//    public String getTpTaskId() {
//        return tpTaskId;
//    }
//
//    public void setTpTaskId(String tpTaskId) {
//        this.tpTaskId = tpTaskId;
//    }

    public String getSubmitId() {
        return submitId;
    }

    public void setSubmitId(String submitId) {
        this.submitId = submitId;
    }

    public String getSubmitName() {
        return submitName;
    }

    public void setSubmitName(String submitName) {
        this.submitName = submitName;
    }

    public String getSubmitValue() {
        return submitValue;
    }

    public void setSubmitValue(String submitValue) {
        this.submitValue = submitValue;
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SubmitaskBean() {
    }

}
