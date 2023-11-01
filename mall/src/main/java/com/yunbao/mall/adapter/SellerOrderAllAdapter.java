package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.SellerOrderBean;

/**
 * 卖家 订单列表 全部
 */
public class SellerOrderAllAdapter extends SellerOrderBaseAdapter {

    private static final int ITEM_CLOSE = -1;//已关闭
    private static final int ITEM_WAIT_PAY = 0;//待付款
    private static final int ITEM_WAIT_SEND = 1;//待发货
    private static final int ITEM_WAIT_RECEIVE = 2;//已发货 待收货
    //    private static final int ITEM_WAIT_COMMENT = 3;//已收货 待评价
    //    private static final int ITEM_COMMENT = 40;////已评价 不可追评
//    private static final int ITEM_COMMENT_APPEND = 4;//已评价 可追评
    private static final int ITEM_REFUND = 5;//退款

    private View.OnClickListener mDeleteClickListener;
    private View.OnClickListener mBuyerClickListener;
    private View.OnClickListener mWuLiuClickListener;
    private View.OnClickListener mRefundClickListener;
    private View.OnClickListener mSendClickListener;
    private String mRefundString;
    private String mRefundString0;
    private String mRefundString1;
    private String mGoodsCountString;

    public SellerOrderAllAdapter(Context context) {
        super(context);
        mGoodsCountString = WordUtil.getString(R.string.mall_208);
        mRefundString = WordUtil.getString(R.string.mall_212);
        mRefundString0 = WordUtil.getString(R.string.mall_255);
        mRefundString1 = WordUtil.getString(R.string.mall_256);
        mDeleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onDeleteClick((SellerOrderBean) tag);
                }
            }
        };
        mBuyerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onContactBuyerClick((SellerOrderBean) tag);
                }
            }
        };
        mWuLiuClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onWuLiuClick((SellerOrderBean) tag);
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


    @Override
    public int getItemViewType(int position) {
        SellerOrderBean bean = mList.get(position);
        int status = bean.getStatus();
        if (status == Constants.MALL_ORDER_STATUS_CLOSE) {
            return ITEM_CLOSE;
        } else if (status == Constants.MALL_ORDER_STATUS_WAIT_PAY) {
            return ITEM_WAIT_PAY;
        } else if (status == Constants.MALL_ORDER_STATUS_WAIT_SEND) {
            return ITEM_WAIT_SEND;
        } else if (status == Constants.MALL_ORDER_STATUS_WAIT_RECEIVE) {
            return ITEM_WAIT_RECEIVE;
        } else if (status == Constants.MALL_ORDER_STATUS_WAIT_COMMENT) {
            return ITEM_WAIT_RECEIVE;
        } else if (status == Constants.MALL_ORDER_STATUS_COMMENT) {
            return ITEM_CLOSE;
        } else if (status == Constants.MALL_ORDER_STATUS_REFUND) {
            return ITEM_REFUND;
        }
        return ITEM_CLOSE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == ITEM_CLOSE) {
            return new FinishVh(mInflater.inflate(R.layout.item_seller_order_finish, viewGroup, false));
        } else if (i == ITEM_WAIT_PAY) {
            return new PayVh(mInflater.inflate(R.layout.item_seller_order_pay, viewGroup, false));
        } else if (i == ITEM_WAIT_SEND) {
            return new SendVh(mInflater.inflate(R.layout.item_seller_order_send, viewGroup, false));
        } else if (i == ITEM_WAIT_RECEIVE) {
            return new ReceiveVh(mInflater.inflate(R.layout.item_seller_order_receive, viewGroup, false));
        } else if (i == ITEM_REFUND) {
            return new RefundVh(mInflater.inflate(R.layout.item_seller_order_refund, viewGroup, false));
        }
        return new BaseVh(mInflater.inflate(R.layout.item_seller_order_all, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((BaseVh) vh).setData(mList.get(i));
    }


    class ReceiveVh extends BaseVh {

        TextView mGoodsCount;
        View mBtnWuLiu;

        public ReceiveVh(@NonNull View itemView) {
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

    class SendVh extends SellerOrderBaseAdapter.BaseVh {

        TextView mGoodsCount;
        View mBtnSend;

        public SendVh(@NonNull View itemView) {
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

    class PayVh extends BaseVh {

        View mBtnBuyer;

        public PayVh(@NonNull View itemView) {
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

    class FinishVh extends BaseVh {

        TextView mGoodsCount;
        View mBtnDelete;

        public FinishVh(@NonNull View itemView) {
            super(itemView);
            mGoodsCount = itemView.findViewById(R.id.goods_count);
            mBtnDelete = itemView.findViewById(R.id.btn_delete_order);
            mBtnDelete.setOnClickListener(mDeleteClickListener);
        }

        @Override
        public void setData(SellerOrderBean bean) {
            mGoodsCount.setText(String.format(mGoodsCountString, bean.getGoodsNum()));
            mBtnDelete.setTag(bean);
            super.setData(bean);
        }
    }


    class RefundVh extends BaseVh {

        TextView mRefundTip;
        View mBtnBuyer;
        View mBtnRefund;

        public RefundVh(@NonNull View itemView) {
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
