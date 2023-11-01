package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class OrderMsgBean {

    private String mOrderId;
    private String mTitle;
    private String mAvatar;
    private String mAddTime;
    private int mType;//0 买家 1卖家
    private int mStatus;
    private int mIsYong;//1 佣金结算消息  , 0订单其他消息

    @JSONField(name = "orderid")
    public String getOrderId() {
        return mOrderId;
    }

    @JSONField(name = "orderid")
    public void setOrderId(String orderId) {
        mOrderId = orderId;
    }

    @JSONField(name = "title")
    public String getTitle() {
        return mTitle;
    }

    @JSONField(name = "title")
    public void setTitle(String title) {
        mTitle = title;
    }

    @JSONField(name = "avatar")
    public String getAvatar() {
        return mAvatar;
    }

    @JSONField(name = "avatar")
    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    @JSONField(name = "addtime")
    public String getAddTime() {
        return mAddTime;
    }

    @JSONField(name = "addtime")
    public void setAddTime(String addTime) {
        mAddTime = addTime;
    }

    @JSONField(name = "type")
    public int getType() {
        return mType;
    }

    @JSONField(name = "type")
    public void setType(int type) {
        mType = type;
    }

    @JSONField(name = "status")
    public int getStatus() {
        return mStatus;
    }
    @JSONField(name = "status")
    public void setStatus(int status) {
        mStatus = status;
    }

    @JSONField(name = "is_commission")
    public int getIsYong() {
        return mIsYong;
    }
    @JSONField(name = "is_commission")
    public void setIsYong(int isYong) {
        mIsYong = isYong;
    }
}
