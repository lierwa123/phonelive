package com.yunbao.live.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class DailyTaskBean {
    private String mId;
    private String mTitle;
    private String mTip;
    private int mStatus;// 0代表未达标  1代表可领取  2代表已领取

    @JSONField(name = "id")
    public String getId() {
        return mId;
    }

    @JSONField(name = "id")
    public void setId(String id) {
        mId = id;
    }

    @JSONField(name = "title")
    public String getTitle() {
        return mTitle;
    }

    @JSONField(name = "title")
    public void setTitle(String title) {
        mTitle = title;
    }

    @JSONField(name = "tip_m")
    public String getTip() {
        return mTip;
    }

    @JSONField(name = "tip_m")
    public void setTip(String tip) {
        mTip = tip;
    }

    @JSONField(name = "state")
    public int getStatus() {
        return mStatus;
    }

    @JSONField(name = "state")
    public void setStatus(int status) {
        mStatus = status;
    }
}
