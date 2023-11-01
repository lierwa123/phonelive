package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 物流公司
 */
public class WuliuBean {

    private String mId;
    private String mName;
    private String mThumb;

    @JSONField(name = "id")
    public String getId() {
        return mId;
    }

    @JSONField(name = "id")
    public void setId(String id) {
        mId = id;
    }

    @JSONField(name = "express_name")
    public String getName() {
        return mName;
    }

    @JSONField(name = "express_name")
    public void setName(String name) {
        mName = name;
    }

    @JSONField(name = "express_thumb")
    public String getThumb() {
        return mThumb;
    }

    @JSONField(name = "express_thumb")
    public void setThumb(String thumb) {
        mThumb = thumb;
    }
}
