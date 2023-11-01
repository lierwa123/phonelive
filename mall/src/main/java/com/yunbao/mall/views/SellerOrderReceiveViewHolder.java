package com.yunbao.mall.views;

import android.content.Context;
import android.view.ViewGroup;

import com.yunbao.mall.adapter.SellerOrderBaseAdapter;
import com.yunbao.mall.adapter.SellerOrderReceiveAdapter;
import com.yunbao.mall.adapter.SellerOrderSendAdapter;

/**
 * 卖家 订单列表 已发货，待收货
 */
public class SellerOrderReceiveViewHolder extends AbsSellerOrderViewHolder {

    public SellerOrderReceiveViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public String getOrderType() {
        return "wait_receive";
    }

    @Override
    public SellerOrderBaseAdapter getSellerOrderAdapter() {
        return new SellerOrderReceiveAdapter(mContext);
    }

}
