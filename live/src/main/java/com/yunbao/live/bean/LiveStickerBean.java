package com.yunbao.live.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class LiveStickerBean {
    private String mId;
    private String mName;
    private long mPlayTime;//播放时长单位毫秒

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
    public long getPlayTime() {
        return mPlayTime;
    }

    @JSONField(serialize = false)
    public void setPlayTime(long playTime) {
        mPlayTime = playTime;
    }
}
