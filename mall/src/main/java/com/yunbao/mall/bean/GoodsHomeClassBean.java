package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class GoodsHomeClassBean {
    private String mId;
    private String mName;
    private String mIcon;


    @JSONField(name = "gc_id")
    public String getId() {
        return mId;
    }

    @JSONField(name = "gc_id")
    public void setId(String id) {
        mId = id;
    }

    @JSONField(name = "gc_name")
    public String getName() {
        return mName;
    }

    @JSONField(name = "gc_name")
    public void setName(String name) {
        mName = name;
    }

    @JSONField(name = "gc_icon")
    public String getIcon() {
        return mIcon;
    }

    @JSONField(name = "gc_icon")
    public void setIcon(String icon) {
        mIcon = icon;
    }
}
