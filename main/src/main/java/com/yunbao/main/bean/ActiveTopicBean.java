package com.yunbao.main.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class ActiveTopicBean {

    private String mId;
    private String mName;
    private String mThumb;
    private int mNum;
    private String mNumString;

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

    @JSONField(name = "use_nums")
    public int getNum() {
        return mNum;
    }

    @JSONField(name = "use_nums")
    public void setNum(int num) {
        mNum = num;
    }

    @JSONField(name = "use_nums_msg")
    public String getNumString() {
        return mNumString;
    }

    @JSONField(name = "use_nums_msg")
    public void setNumString(String numString) {
        mNumString = numString;
    }


}
