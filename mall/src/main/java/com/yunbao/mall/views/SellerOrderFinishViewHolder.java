package com.yunbao.mall.views;

import android.content.Context;
import android.view.ViewGroup;

import com.yunbao.mall.adapter.SellerOrderBaseAdapter;
import com.yunbao.mall.adapter.SellerOrderFinishAdapter;

/**
 * 卖家 订单列表 已完成
 */
public class SellerOrderFinishViewHolder extends AbsSellerOrderViewHolder {

    public SellerOrderFinishViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public String getOrderType() {
        return "finished";
    }

    @Override
    public SellerOrderBaseAdapter getSellerOrderAdapter() {
        return new SellerOrderFinishAdapter(mContext);
    }

}
