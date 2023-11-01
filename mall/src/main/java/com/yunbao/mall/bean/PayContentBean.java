package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 付费内容 我上传的
 */
public class PayContentBean {

    private String mId;
    private String mTitle;
    private String mSaleNum;
    private int mStatus;// 0 审核中  -1 拒绝  1 通过
    private String mMoney;
    private String mThumb;
    private String mVideoNum;

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

    @JSONField(name = "sale_nums")
    public String getSaleNum() {
        return mSaleNum;
    }

    @JSONField(name = "sale_nums")
    public void setSaleNum(String saleNum) {
        mSaleNum = saleNum;
    }

    @JSONField(name = "status")
    public int getStatus() {
        return mStatus;
    }

    @JSONField(name = "status")
    public void setStatus(int status) {
        mStatus = status;
    }

    @JSONField(name = "money")
    public String getMoney() {
        return mMoney;
    }

    @JSONField(name = "money")
    public void setMoney(String money) {
        mMoney = money;
    }

    @JSONField(name = "thumb_format")
    public String getThumb() {
        return mThumb;
    }

    @JSONField(name = "thumb_format")
    public void setThumb(String thumb) {
        mThumb = thumb;
    }

    @JSONField(name = "video_num")
    public String getVideoNum() {
        return mVideoNum;
    }

    @JSONField(name = "video_num")
    public void setVideoNum(String videoNum) {
        mVideoNum = videoNum;
    }
}
