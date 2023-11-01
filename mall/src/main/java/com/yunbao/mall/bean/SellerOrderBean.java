package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class SellerOrderBean {

    private String mId;//订单id
    private String mUid;//买家的uid
    private String mSellerId; //卖家的uid
    private String mGoodsId;//商品id
    private String mGoodsName;//商品名称
    private String mGoodsSpecName;//商品规格名称
    private String mGoodsSpecThumb;//商品规格图标
    private String mGoodsNum;//购买数量
    private String mGoodsPrice;//商品单价
    private String mTotalPrice;//总价
    private int mStatus;//订单状态  -1已关闭，0待付款，1待发货，2待收货，3待评价，4已评价，5退款
    private String mOrderNo;//订单号
    private int mRefundStatus;//退款处理结果 -1失败，0处理中，1成功
    private String mStatusTip;//交易状态说明
    private String mBuyerName;//买家姓名
    private String mBuyerPhoneNum;//买家电话
    private int mRefundType;//0仅退款 1退货退款


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

    @JSONField(name = "shop_uid")
    public String getSellerId() {
        return mSellerId;
    }

    @JSONField(name = "shop_uid")
    public void setSellerId(String sellerId) {
        mSellerId = sellerId;
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
    public String getGoodsName() {
        return mGoodsName;
    }

    @JSONField(name = "goods_name")
    public void setGoodsName(String goodsName) {
        mGoodsName = goodsName;
    }

    @JSONField(name = "spec_name")
    public String getGoodsSpecName() {
        return mGoodsSpecName;
    }

    @JSONField(name = "spec_name")
    public void setGoodsSpecName(String goodsSpecName) {
        mGoodsSpecName = goodsSpecName;
    }

    @JSONField(name = "spec_thumb")
    public String getGoodsSpecThumb() {
        return mGoodsSpecThumb;
    }

    @JSONField(name = "spec_thumb")
    public void setGoodsSpecThumb(String goodsSpecThumb) {
        mGoodsSpecThumb = goodsSpecThumb;
    }

    @JSONField(name = "nums")
    public String getGoodsNum() {
        return mGoodsNum;
    }

    @JSONField(name = "nums")
    public void setGoodsNum(String goodsNum) {
        mGoodsNum = goodsNum;
    }

    @JSONField(name = "price")
    public String getGoodsPrice() {
        return mGoodsPrice;
    }

    @JSONField(name = "price")
    public void setGoodsPrice(String goodsPrice) {
        mGoodsPrice = goodsPrice;
    }

    @JSONField(name = "total")
    public String getTotalPrice() {
        return mTotalPrice;
    }

    @JSONField(name = "total")
    public void setTotalPrice(String totalPrice) {
        mTotalPrice = totalPrice;
    }

    @JSONField(name = "status")
    public int getStatus() {
        return mStatus;
    }

    @JSONField(name = "status")
    public void setStatus(int status) {
        mStatus = status;
    }

    @JSONField(name = "refund_status")
    public int getRefundStatus() {
        return mRefundStatus;
    }

    @JSONField(name = "refund_status")
    public void setRefundStatus(int refundStatus) {
        mRefundStatus = refundStatus;
    }

    @JSONField(name = "status_name")
    public String getStatusTip() {
        return mStatusTip;
    }

    @JSONField(name = "status_name")
    public void setStatusTip(String statusTip) {
        mStatusTip = statusTip;
    }

    @JSONField(name = "orderno")
    public String getOrderNo() {
        return mOrderNo;
    }

    @JSONField(name = "orderno")
    public void setOrderNo(String orderNo) {
        mOrderNo = orderNo;
    }

    @JSONField(name = "username")
    public String getBuyerName() {
        return mBuyerName;
    }

    @JSONField(name = "username")
    public void setBuyerName(String buyerName) {
        mBuyerName = buyerName;
    }

    @JSONField(name = "phone")
    public String getBuyerPhoneNum() {
        return mBuyerPhoneNum;
    }

    @JSONField(name = "phone")
    public void setBuyerPhoneNum(String buyerPhoneNum) {
        mBuyerPhoneNum = buyerPhoneNum;
    }

    @JSONField(name = "refund_type")
    public int getRefundType() {
        return mRefundType;
    }

    @JSONField(name = "refund_type")
    public void setRefundType(int refundType) {
        mRefundType = refundType;
    }
}
