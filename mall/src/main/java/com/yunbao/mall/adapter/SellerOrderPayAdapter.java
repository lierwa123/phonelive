package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yunbao.mall.R;
import com.yunbao.mall.bean.SellerOrderBean;

/**
 * 卖家 订单列表 待付款
 */
public class SellerOrderPayAdapter extends SellerOrderBaseAdapter {

    private View.OnClickListener mBuyerClickListener;

    public SellerOrderPayAdapter(Context context) {
        super(context);
        mBuyerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onContactBuyerClick((SellerOrderBean) tag);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_seller_order_pay, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }

    class Vh extends BaseVh {

        View mBtnBuyer;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mBtnBuyer = itemView.findViewById(R.id.btn_contact_buyer);
            mBtnBuyer.setOnClickListener(mBuyerClickListener);
        }

        @Override
        public void setData(SellerOrderBean bean) {
            mBtnBuyer.setTag(bean);
            super.setData(bean);
        }
    }
}
