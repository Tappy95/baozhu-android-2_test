package com.micang.baozhu.http.bean.task;

public class SelectReasonBean {

    private String reason;
    private boolean isSelected;

    public SelectReasonBean(String reason) {
        this.reason = reason;
    }

    public SelectReasonBean(String reason, boolean isSelected) {
        this.reason = reason;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
