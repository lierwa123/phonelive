package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 付费内容 我购买的
 */
public class PayContentBuyBean {

    private String mGoodsId;
    private String mTitle;
    private String mThumb;
    private String mVideoNum;
    private String mUserName;
    private String mAvatar;

    @JSONField(name = "object_id")
    public String getGoodsId() {
        return mGoodsId;
    }
    @JSONField(name = "object_id")
    public void setGoodsId(String goodsId) {
        mGoodsId = goodsId;
    }
    @JSONField(name = "title")
    public String getTitle() {
        return mTitle;
    }
    @JSONField(name = "title")
    public void setTitle(String title) {
        mTitle = title;
    }
    @JSONField(name = "thumb")
    public String getThumb() {
        return mThumb;
    }
    @JSONField(name = "thumb")
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
    @JSONField(name = "user_nicename")
    public String getUserName() {
        return mUserName;
    }
    @JSONField(name = "user_nicename")
    public void setUserName(String userName) {
        mUserName = userName;
    }
    @JSONField(name = "avatar")
    public String getAvatar() {
        return mAvatar;
    }
    @JSONField(name = "avatar")
    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }
}
