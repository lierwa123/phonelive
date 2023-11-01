package com.yunbao.live.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class VoiceRoomAccPullBean {
    private String mUid;
    private String mPull;
    private int mIsAnchor;

    @JSONField(name = "uid")
    public String getUid() {
        return mUid;
    }

    @JSONField(name = "uid")
    public void setUid(String uid) {
        mUid = uid;
    }

    @JSONField(name = "pull")
    public String getPull() {
        return mPull;
    }

    @JSONField(name = "pull")
    public void setPull(String pull) {
        mPull = pull;
    }

    @JSONField(name = "isanchor")
    public int getIsAnchor() {
        return mIsAnchor;
    }

    @JSONField(name = "isanchor")
    public void setIsAnchor(int isAnchor) {
        mIsAnchor = isAnchor;
    }
}
