package com.yunbao.mall.views;

import android.content.Context;
import android.view.ViewGroup;

import com.yunbao.mall.adapter.BuyerOrderBaseAdapter;
import com.yunbao.mall.adapter.BuyerOrderCommentAdapter;

/**
 * 买家 订单列表 待评价
 */
public class BuyerOrderCommentViewHolder extends AbsBuyerOrderViewHolder {

    public BuyerOrderCommentViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public String getOrderType() {
        return "wait_evaluate";
    }

    @Override
    public BuyerOrderBaseAdapter getBuyerOrderAdapter() {
        return new BuyerOrderCommentAdapter(mContext);
    }


}
