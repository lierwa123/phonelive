package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yunbao.mall.R;
import com.yunbao.mall.bean.BuyerOrderBean;

/**
 * 买家 订单列表 待付款
 */
public class BuyerOrderPayAdapter extends BuyerOrderBaseAdapter {

    private View.OnClickListener mCancelClickListener;
    private View.OnClickListener mPayClickListener;

    public BuyerOrderPayAdapter(Context context) {
        super(context);
        mCancelClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onCancelOrderClick((BuyerOrderBean) tag);
                }
            }
        };
        mPayClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onPayClick((BuyerOrderBean) tag);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_buyer_order_pay, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }

    class Vh extends BuyerOrderBaseAdapter.BaseVh {

        View mBtnCancel;
        View mBtnPay;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mBtnCancel = itemView.findViewById(R.id.btn_cancel_order);
            mBtnPay = itemView.findViewById(R.id.btn_pay);
            mBtnCancel.setOnClickListener(mCancelClickListener);
            mBtnPay.setOnClickListener(mPayClickListener);
        }

        @Override
        public void setData(BuyerOrderBean bean) {
            mBtnCancel.setTag(bean);
            mBtnPay.setTag(bean);
            super.setData(bean);
        }
    }

}
