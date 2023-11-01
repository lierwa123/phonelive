package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 商品管理
 */
public class GoodsManageBean {

    private String mId;
    private String mName;
    private String mSaleNum;
    private String mThumb;
    private String mPrice;
    private String mOriginPrice;
    private int mType;//0 站内商品  1 外链商品
    private int mStatus;
    private String mPriceYong;//佣金


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

    @JSONField(name = "sale_nums")
    public String getSaleNum() {
        return mSaleNum;
    }

    @JSONField(name = "sale_nums")
    public void setSaleNum(String saleNum) {
        mSaleNum = saleNum;
    }

    @JSONField(name = "thumb")
    public String getThumb() {
        return mThumb;
    }

    @JSONField(name = "thumb")
    public void setThumb(String thumb) {
        mThumb = thumb;
    }

    @JSONField(name = "price")
    public String getPrice() {
        return mPrice;
    }

    @JSONField(name = "price")
    public void setPrice(String price) {
        mPrice = price;
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
    @JSONField(name = "status")
    public int getStatus() {
        return mStatus;
    }
    @JSONField(name = "status")
    public void setStatus(int status) {
        mStatus = status;
    }
    @JSONField(name = "commission")
    public String getPriceYong() {
        return mPriceYong;
    }
    @JSONField(name = "commission")
    public void setPriceYong(String priceYong) {
        mPriceYong = priceYong;
    }
}
