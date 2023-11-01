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
 * 卖家 订单列表 已发货，待收货
 */
public class SellerOrderReceiveAdapter extends SellerOrderBaseAdapter {

    private View.OnClickListener mWuLiuClickListener;
    private String mGoodsCountString;

    public SellerOrderReceiveAdapter(Context context) {
        super(context);
        mGoodsCountString = WordUtil.getString(R.string.mall_208);
        mWuLiuClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onWuLiuClick((SellerOrderBean) tag);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_seller_order_receive, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }

    class Vh extends BaseVh {

        TextView mGoodsCount;
        View mBtnWuLiu;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mGoodsCount = itemView.findViewById(R.id.goods_count);
            mBtnWuLiu = itemView.findViewById(R.id.btn_wuliu);
            mBtnWuLiu.setOnClickListener(mWuLiuClickListener);
        }

        @Override
        public void setData(SellerOrderBean bean) {
            mGoodsCount.setText(String.format(mGoodsCountString, bean.getGoodsNum()));
            mBtnWuLiu.setTag(bean);
            super.setData(bean);
        }
    }
}
