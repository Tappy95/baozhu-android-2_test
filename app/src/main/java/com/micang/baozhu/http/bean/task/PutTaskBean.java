package com.micang.baozhu.http.bean.task;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/6/28 22:28
 * @describe describe
 */
public class PutTaskBean {

    private String name;
    private String id;
    private String value;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PutTaskBean(String name, String id, String value) {
        this.name = name;
        this.id = id;
        this.value = value;
    }

    public PutTaskBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
