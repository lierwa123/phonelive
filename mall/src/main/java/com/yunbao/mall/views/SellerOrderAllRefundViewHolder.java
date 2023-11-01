package com.yunbao.mall.views;

import android.content.Context;
import android.view.ViewGroup;

import com.yunbao.mall.adapter.SellerOrderAllRefundAdapter;
import com.yunbao.mall.adapter.SellerOrderBaseAdapter;

/**
 * 卖家 订单列表 全部退款
 */
public class SellerOrderAllRefundViewHolder extends AbsSellerOrderViewHolder {

    public SellerOrderAllRefundViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public String getOrderType() {
        return "all_refund";
    }

    @Override
    public SellerOrderBaseAdapter getSellerOrderAdapter() {
        return new SellerOrderAllRefundAdapter(mContext);
    }

}
