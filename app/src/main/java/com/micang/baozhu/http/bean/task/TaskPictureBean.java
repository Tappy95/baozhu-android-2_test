package com.micang.baozhu.http.bean.task;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.File;

/**
 * @version 1.0
 * @Package com.dizoo.invite.http.bean.user
 * @time 2019/5/14 11:14
 * @describe describe
 */
public class TaskPictureBean implements MultiItemEntity {
    public String path;
    public int code;

    public void setPath(String path) {
        this.path = path;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getPath() {
        return path;
    }

    public int getCode() {
        return code;
    }

    @Override
    public int getItemType() {
        if (code == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}
