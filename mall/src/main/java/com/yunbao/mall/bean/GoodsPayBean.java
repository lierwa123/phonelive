package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class GoodsPayBean {

    private String mId;
    private String mName;
    private String mThumb;
    private String mType;
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

    @JSONField(name = "thumb")
    public String getThumb() {
        return mThumb;
    }

    @JSONField(name = "thumb")
    public void setThumb(String thumb) {
        mThumb = thumb;
    }

    @JSONField(name = "type")
    public String getType() {
        return mType;
    }

    @JSONField(name = "type")
    public void setType(String type) {
        mType = type;
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
