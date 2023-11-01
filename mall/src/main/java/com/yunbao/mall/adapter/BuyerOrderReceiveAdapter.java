package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yunbao.mall.R;
import com.yunbao.mall.bean.BuyerOrderBean;

/**
 * 买家 订单列表 待收货
 */
public class BuyerOrderReceiveAdapter extends BuyerOrderBaseAdapter {

    private View.OnClickListener mWuLiuClickListener;
    private View.OnClickListener mConfirmClickListener;

    public BuyerOrderReceiveAdapter(Context context) {
        super(context);
        mWuLiuClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onWuLiuClick((BuyerOrderBean) tag);
                }
            }
        };
        mConfirmClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onConfirmClick((BuyerOrderBean) tag);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_buyer_order_receive, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }

    class Vh extends BuyerOrderBaseAdapter.BaseVh {

        View mBtnWuLiu;
        View mBtnConfirm;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mBtnWuLiu = itemView.findViewById(R.id.btn_wuliu);
            mBtnConfirm = itemView.findViewById(R.id.btn_confirm);
            mBtnWuLiu.setOnClickListener(mWuLiuClickListener);
            mBtnConfirm.setOnClickListener(mConfirmClickListener);
        }

        @Override
        public void setData(BuyerOrderBean bean) {
            mBtnWuLiu.setTag(bean);
            mBtnConfirm.setTag(bean);
            super.setData(bean);
        }
    }

}
