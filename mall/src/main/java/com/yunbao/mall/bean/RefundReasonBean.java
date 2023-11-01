package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 退款原因
 */
public class RefundReasonBean {

    private String mId;
    private String mName;

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
}
