package com.yunbao.mall.event;

/**
 * 添加或取消 平台代卖商品
 */
public class SetPlatGoodsEvent {
    private String mGoodsId;
    private int status;

    public SetPlatGoodsEvent(String goodsId, int status) {
        mGoodsId = goodsId;
        this.status = status;
    }

    public String getGoodsId() {
        return mGoodsId;
    }

    public void setGoodsId(String goodsId) {
        mGoodsId = goodsId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
