package com.yunbao.mall.bean;

import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.File;

public class PayContentVideoBean  {

    protected String mId = "";
    protected String mUploadResultUrl = "";
    protected String mTitle = "";
    protected int mIsSee;
    protected String mDuration = "";
    protected String mFilePath = "";
    protected File mFile;

    public PayContentVideoBean() {
    }

    @JSONField(name = "video_id")
    public String getId() {
        return mId;
    }

    @JSONField(name = "video_id")
    public void setId(String id) {
        mId = id;
    }

    @JSONField(name = "video_url")
    public String getUploadResultUrl() {
        return mUploadResultUrl;
    }

    @JSONField(name = "video_url")
    public void setUploadResultUrl(String uploadResultUrl) {
        mUploadResultUrl = uploadResultUrl;
    }

    @JSONField(name = "video_title")
    public String getTitle() {
        return mTitle;
    }

    @JSONField(name = "video_title")
    public void setTitle(String title) {
        mTitle = title;
    }

    @JSONField(name = "is_see")
    public int getIsSee() {
        return mIsSee;
    }

    @JSONField(name = "is_see")
    public void setIsSee(int isSee) {
        mIsSee = isSee;
    }

    @JSONField(name = "video_length")
    public String getDuration() {
        return mDuration;
    }

    @JSONField(name = "video_length")
    public void setDuration(String duration) {
        mDuration = duration;
    }


    @JSONField(serialize = false)
    public String getFilePath() {
        return mFilePath;
    }

    @JSONField(serialize = false)
    public void setFilePath(String filePath) {
        mFilePath = filePath;
        if (TextUtils.isEmpty(filePath)) {
            mFile = null;
        } else {
            mFile = new File(filePath);
        }

    }

    @JSONField(serialize = false)
    public File getFile() {
        return mFile;
    }

    @JSONField(serialize = false)
    public void clear() {
        mFilePath = "";
        mFile = null;
        mDuration = "";
        mIsSee = 0;
        mTitle = "";
    }

    @JSONField(serialize = false)
    public boolean hasFile() {
        return mFile != null && mFile.exists();
    }

}
