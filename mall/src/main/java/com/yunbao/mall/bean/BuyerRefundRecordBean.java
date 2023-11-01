package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class BuyerRefundRecordBean {

    private String mId;
    private String mToUid;
    private String mBalance;
    private String mAddTime;
    private String mResult;

    @JSONField(name = "id")
    public String getId() {
        return mId;
    }
    @JSONField(name = "id")
    public void setId(String id) {
        mId = id;
    }
    @JSONField(name = "touid")
    public String getToUid() {
        return mToUid;
    }
    @JSONField(name = "touid")
    public void setToUid(String toUid) {
        mToUid = toUid;
    }
    @JSONField(name = "balance")
    public String getBalance() {
        return mBalance;
    }
    @JSONField(name = "balance")
    public void setBalance(String balance) {
        mBalance = balance;
    }
    @JSONField(name = "addtime")
    public String getAddTime() {
        return mAddTime;
    }
    @JSONField(name = "addtime")
    public void setAddTime(String addTime) {
        mAddTime = addTime;
    }
    @JSONField(name = "result")
    public String getResult() {
        return mResult;
    }
    @JSONField(name = "result")
    public void setResult(String result) {
        mResult = result;
    }

}
