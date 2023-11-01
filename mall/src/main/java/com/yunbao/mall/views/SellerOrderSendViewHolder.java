package com.yunbao.mall.views;

import android.content.Context;
import android.view.ViewGroup;

import com.yunbao.mall.activity.SellerOrderDetailActivity;
import com.yunbao.mall.activity.SellerSendActivity;
import com.yunbao.mall.adapter.SellerOrderBaseAdapter;
import com.yunbao.mall.adapter.SellerOrderSendAdapter;
import com.yunbao.mall.bean.SellerOrderBean;

/**
 * 卖家 订单列表 待发货
 */
public class SellerOrderSendViewHolder extends AbsSellerOrderViewHolder {

    public SellerOrderSendViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public String getOrderType() {
        return "wait_shipment";
    }

    @Override
    public SellerOrderBaseAdapter getSellerOrderAdapter() {
        return new SellerOrderSendAdapter(mContext);
    }

    /**
     * 点击item
     */
    @Override
    public void onItemClick(SellerOrderBean bean) {
        SellerSendActivity.forward(mContext, bean.getId());
    }

}
