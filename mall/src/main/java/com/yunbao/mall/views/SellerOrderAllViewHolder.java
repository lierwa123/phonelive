package com.yunbao.mall.views;

import android.content.Context;
import android.view.ViewGroup;

import com.yunbao.mall.adapter.SellerOrderAllAdapter;
import com.yunbao.mall.adapter.SellerOrderBaseAdapter;

/**
 * 卖家 订单列表 全部订单
 */
public class SellerOrderAllViewHolder extends AbsSellerOrderViewHolder {

    public SellerOrderAllViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public String getOrderType() {
        return "all";
    }

    @Override
    public SellerOrderBaseAdapter getSellerOrderAdapter() {
        return new SellerOrderAllAdapter(mContext);
    }

}
