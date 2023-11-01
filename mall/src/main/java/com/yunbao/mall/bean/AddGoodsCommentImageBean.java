package com.yunbao.mall.bean;

import java.io.File;

/**
 * 买家 商品评论 添加图片
 */
public class AddGoodsCommentImageBean {
    private File mFile;
    private String mUploadResultUrl;


    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        mFile = file;
    }


    public String getUploadResultUrl() {
        return mUploadResultUrl;
    }

    public void setUploadResultUrl(String uploadResultUrl) {
        mUploadResultUrl = uploadResultUrl;
    }

    public boolean isEmpty() {
        return mFile == null || !mFile.exists();
    }

    public void setEmpty() {
        mFile = null;
    }
}
