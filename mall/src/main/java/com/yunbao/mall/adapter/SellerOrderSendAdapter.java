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
 * 卖家 订单列表 待发货
 */
public class SellerOrderSendAdapter extends SellerOrderBaseAdapter {

    private View.OnClickListener mSendClickListener;
    private String mGoodsCountString;

    public SellerOrderSendAdapter(Context context) {
        super(context);
        mGoodsCountString = WordUtil.getString(R.string.mall_208);
        mSendClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onSendClick((SellerOrderBean) tag);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_seller_order_send, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }

    class Vh extends SellerOrderBaseAdapter.BaseVh {

        TextView mGoodsCount;
        View mBtnSend;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mGoodsCount = itemView.findViewById(R.id.goods_count);
            mBtnSend = itemView.findViewById(R.id.btn_send);
            mBtnSend.setOnClickListener(mSendClickListener);
        }

        @Override
        public void setData(SellerOrderBean bean) {
            mGoodsCount.setText(String.format(mGoodsCountString, bean.getGoodsNum()));
            mBtnSend.setTag(bean);
            super.setData(bean);
        }
    }
}
