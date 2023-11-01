package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class GoodsRecordItemBean extends GoodsRecordBean {

    private String mId;
    private String mUid;
    private String mGoodsId;
    private String mName;
    private String mPrice;
    private String mThumb;
    private int mStatus;//1正常 0商品被下架或删除
    private GoodsRecordTitleBean mParent;
    private int mType;
    private String mOriginPrice;

    @JSONField(name = "id")
    public String getId() {
        return mId;
    }

    @JSONField(name = "id")
    public void setId(String id) {
        mId = id;
    }

    @JSONField(name = "uid")
    public String getUid() {
        return mUid;
    }

    @JSONField(name = "uid")
    public void setUid(String uid) {
        mUid = uid;
    }

    @JSONField(name = "goodsid")
    public String getGoodsId() {
        return mGoodsId;
    }

    @JSONField(name = "goodsid")
    public void setGoodsId(String goodsId) {
        mGoodsId = goodsId;
    }

    @JSONField(name = "goods_name")
    public String getName() {
        return mName;
    }

    @JSONField(name = "goods_name")
    public void setName(String name) {
        mName = name;
    }

    @JSONField(name = "goods_price")
    public String getPrice() {
        return mPrice;
    }

    @JSONField(name = "goods_price")
    public void setPrice(String price) {
        mPrice = price;
    }

    @JSONField(name = "goods_thumb")
    public String getThumb() {
        return mThumb;
    }

    @JSONField(name = "goods_thumb")
    public void setThumb(String thumb) {
        mThumb = thumb;
    }

    @JSONField(name = "goods_status")
    public int getStatus() {
        return mStatus;
    }

    @JSONField(name = "goods_status")
    public void setStatus(int status) {
        mStatus = status;
    }

    @JSONField(serialize = false)
    public GoodsRecordTitleBean getParent() {
        return mParent;
    }

    @JSONField(serialize = false)
    public void setParent(GoodsRecordTitleBean parent) {
        mParent = parent;
    }

    @JSONField(name = "type")
    public int getType() {
        return mType;
    }

    @JSONField(name = "type")
    public void setType(int type) {
        mType = type;
    }
    @JSONField(name = "original_price")
    public String getOriginPrice() {
        return mOriginPrice;
    }
    @JSONField(name = "original_price")
    public void setOriginPrice(String originPrice) {
        mOriginPrice = originPrice;
    }
}
