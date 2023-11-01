package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yunbao.mall.R;
import com.yunbao.mall.bean.BuyerOrderBean;

/**
 * 买家 订单列表 待评价
 */
public class BuyerOrderCommentAdapter extends BuyerOrderBaseAdapter {

    private View.OnClickListener mCommentClickListener;

    public BuyerOrderCommentAdapter(Context context) {
        super(context);
        mCommentClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onCommentClick((BuyerOrderBean) tag);
                }
            }
        };
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_buyer_order_comment, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }

    class Vh extends BuyerOrderBaseAdapter.BaseVh {

        View mBtnComment;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mBtnComment = itemView.findViewById(R.id.btn_comment);
            mBtnComment.setOnClickListener(mCommentClickListener);
        }

        @Override
        public void setData(BuyerOrderBean bean) {
            mBtnComment.setTag(bean);
            super.setData(bean);
        }
    }

}
