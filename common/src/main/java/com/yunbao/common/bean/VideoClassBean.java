package com.yunbao.common.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class VideoClassBean {
    private int mId;
    private String mName;
    private boolean mChecked;

    public VideoClassBean() {
    }

    public VideoClassBean(int id, String name, boolean checked) {
        mId = id;
        mName = name;
        mChecked = checked;
    }

    @JSONField(name = "id")
    public int getId() {
        return mId;
    }

    @JSONField(name = "id")
    public void setId(int id) {
        mId = id;
    }

    @JSONField(name = "name")
    public String getName() {
        return mName;
    }

    @JSONField(name = "name")
    public void setName(String name) {
        mName = name;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
