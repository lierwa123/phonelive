package com.yunbao.mall.bean;

import android.text.TextUtils;

import java.io.File;

public class AddGoodsImageBean {
    private File mFile;
    private String mImgUrl;
    private String mUploadResultUrl;


    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        mFile = file;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }


    public String getUploadResultUrl() {
        if (!TextUtils.isEmpty(mUploadResultUrl)) {
            return mUploadResultUrl;
        }
        if (!TextUtils.isEmpty(mImgUrl)) {
            int index = mImgUrl.lastIndexOf("/") + 1;
            if (index > 0) {
                return mImgUrl.substring(index);
            }
        }
        return "";
    }

    public void setUploadResultUrl(String uploadResultUrl) {
        mUploadResultUrl = uploadResultUrl;
    }

    public boolean isEmpty() {
        return mFile == null && TextUtils.isEmpty(mImgUrl);
    }

    public void setEmpty() {
        mFile = null;
        mImgUrl = null;
    }
}
