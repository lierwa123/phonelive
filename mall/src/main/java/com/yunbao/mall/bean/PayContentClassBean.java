package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 付费内容类别
 */
public class PayContentClassBean {

    private String mId;
    private String mName;
    private boolean mChecked;

    @JSONField(name = "id")
    public String getId() {
        return mId;
    }

    @JSONField(name = "id")
    public void setId(String id) {
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

    @JSONField(serialize = false)
    public boolean isChecked() {
        return mChecked;
    }

    @JSONField(serialize = false)
    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
