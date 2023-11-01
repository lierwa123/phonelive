package com.yunbao.mall.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class GoodsPlatBean extends GoodsSimpleBean {

    private String mPriceYong;//佣金
    private int mAdd;


    public GoodsPlatBean() {
    }


    @JSONField(name = "commission")
    public String getPriceYong() {
        return mPriceYong;
    }

    @JSONField(name = "commission")
    public void setPriceYong(String priceYong) {
        mPriceYong = priceYong;
    }

    @JSONField(name = "isadd")
    public int getAdd() {
        return mAdd;
    }

    @JSONField(name = "isadd")
    public void setAdd(int add) {
        mAdd = add;
    }
}
