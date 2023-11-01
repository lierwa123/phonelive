package com.yunbao.main.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class CancelConditionBean {

    private String mTitle;
    private String mContent;
    private int mIsOK;

    @JSONField(name = "title")
    public String getTitle() {
        return mTitle;
    }

    @JSONField(name = "title")
    public void setTitle(String title) {
        mTitle = title;
    }

    @JSONField(name = "content")
    public String getContent() {
        return mContent;
    }

    @JSONField(name = "content")
    public void setContent(String content) {
        mContent = content;
    }

    @JSONField(name = "is_ok")
    public int getIsOK() {
        return mIsOK;
    }

    @JSONField(name = "is_ok")
    public void setIsOK(int isOK) {
        mIsOK = isOK;
    }
}
