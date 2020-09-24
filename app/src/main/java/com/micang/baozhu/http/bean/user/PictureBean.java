package com.micang.baozhu.http.bean.user;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.File;

/**
 * @author
 * @version 1.0
 * @Package com.dizoo.invite.http.bean.user
 * @time 2019/5/14 11:14
 * @describe describe
 */
public class PictureBean implements MultiItemEntity {
    public File path;
    public  int code;

    public void setPath(File path) {
        this.path = path;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public File getPath() {
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
