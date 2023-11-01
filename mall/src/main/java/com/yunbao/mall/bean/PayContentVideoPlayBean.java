package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class PayContentVideoPlayBean extends PayContentVideoBean {

    private String mDurationString;

    @JSONField(name = "video_length_format")
    public String getDurationString() {
        return mDurationString;
    }

    @JSONField(name = "video_length_format")
    public void setDurationString(String durationString) {
        mDurationString = durationString;
    }
}
