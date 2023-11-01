package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yunbao.mall.R;
import com.yunbao.mall.bean.BuyerOrderBean;

/**
 * 买家 订单列表 退款
 */
public class BuyerOrderRefundAdapter extends BuyerOrderBaseAdapter {

    private View.OnClickListener mRefundClickListener;

    public BuyerOrderRefundAdapter(Context context) {
        super(context);
        mRefundClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onRefundClick((BuyerOrderBean) tag);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_buyer_order_refund, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }

    class Vh extends BuyerOrderBaseAdapter.BaseVh {

        View mBtnRefund;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mBtnRefund = itemView.findViewById(R.id.btn_refund);
            mBtnRefund.setOnClickListener(mRefundClickListener);
        }

        @Override
        public void setData(BuyerOrderBean bean) {
            mBtnRefund.setTag(bean);
            super.setData(bean);
        }
    }

}
