package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.SellerOrderBean;

/**
 * 卖家 订单列表 待退款
 */
public class SellerOrderRefundAdapter extends SellerOrderBaseAdapter {

    private View.OnClickListener mBuyerClickListener;
    private View.OnClickListener mRefundClickListener;
    private String mRefundString;
    private String mRefundString0;
    private String mRefundString1;

    public SellerOrderRefundAdapter(Context context) {
        super(context);
        mMoneySymbol = WordUtil.getString(R.string.money_symbol);
        mRefundString = WordUtil.getString(R.string.mall_212);
        mRefundString0 = WordUtil.getString(R.string.mall_255);
        mRefundString1 = WordUtil.getString(R.string.mall_256);
        mBuyerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onContactBuyerClick((SellerOrderBean) tag);
                }
            }
        };
        mRefundClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onRefundClick((SellerOrderBean) tag);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_seller_order_refund, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }

    class Vh extends BaseVh {

        TextView mRefundTip;
        View mBtnBuyer;
        View mBtnRefund;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mRefundTip = itemView.findViewById(R.id.refund_tip);
            mBtnBuyer = itemView.findViewById(R.id.btn_contact_buyer);
            mBtnRefund = itemView.findViewById(R.id.btn_refund_detail);
            mBtnBuyer.setOnClickListener(mBuyerClickListener);
            mBtnRefund.setOnClickListener(mRefundClickListener);
        }

        @Override
        public void setData(SellerOrderBean bean) {
            mRefundTip.setText(String.format(mRefundString, bean.getRefundType() == 0 ? mRefundString0 : mRefundString1,mMoneySymbol, bean.getTotalPrice()));
            mBtnBuyer.setTag(bean);
            mBtnRefund.setTag(bean);
            super.setData(bean);
        }
    }
}
