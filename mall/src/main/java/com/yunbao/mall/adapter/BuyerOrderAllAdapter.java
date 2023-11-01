package com.yunbao.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.interfaces.ActivityResultCallback;
import com.yunbao.common.utils.ActivityResultUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.activity.BuyerCommentAppendActivity;
import com.yunbao.mall.bean.BuyerOrderBean;

/**
 * 买家 订单列表 全部
 */
public class BuyerOrderAllAdapter extends BuyerOrderBaseAdapter {

    private static final int ITEM_CLOSE = -1;//已关闭
    private static final int ITEM_WAIT_PAY = 0;//待付款
    private static final int ITEM_WAIT_SEND = 1;//待发货
    private static final int ITEM_WAIT_RECEIVE = 2;//已发货 待收货
    private static final int ITEM_WAIT_COMMENT = 3;//已收货 待评价
    private static final int ITEM_COMMENT_FINISH = 40;////已评价 不可追评
    private static final int ITEM_COMMENT_APPEND = 4;//已评价 可追评
    private static final int ITEM_REFUND = 5;//退款

    private View.OnClickListener mDeleteClickListener;//删除订单
    private View.OnClickListener mWuliuClickListener;//物流
    private View.OnClickListener mAppendClickListener;//追评
    private View.OnClickListener mCancelClickListener;//取消订单
    private View.OnClickListener mPayClickListener;//支付
    private View.OnClickListener mConfirmClickListener;//确认收货
    private View.OnClickListener mCommentClickListener;//评价
    private View.OnClickListener mRefundClickListener;//退款

    public BuyerOrderAllAdapter(Context context) {
        super(context);
        mDeleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onDeleteClick((BuyerOrderBean) tag);
                }
            }
        };
        mWuliuClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onWuLiuClick((BuyerOrderBean) tag);
                }
            }
        };
        mAppendClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final int position = (int) v.getTag();
                final BuyerOrderBean bean = mList.get(position);
                Intent intent = new Intent(mContext, BuyerCommentAppendActivity.class);
                intent.putExtra(Constants.MALL_ORDER_ID, bean.getId());
                ActivityResultUtil.startActivityForResult((AbsActivity) mContext, intent, new ActivityResultCallback() {
                    @Override
                    public void onSuccess(Intent intent) {
                        bean.setIsAppend(0);
                        notifyItemChanged(position);
                    }
                });
            }
        };

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
        mConfirmClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onConfirmClick((BuyerOrderBean) tag);
                }
            }
        };
        mCommentClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onCommentClick((BuyerOrderBean) tag);
                }
            }
        };
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

    @Override
    public int getItemViewType(int position) {
        BuyerOrderBean bean = mList.get(position);
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
            return ITEM_WAIT_COMMENT;
        } else if (status == Constants.MALL_ORDER_STATUS_COMMENT) {
            if (bean.getIsAppend() == 1) {//可追评
                return ITEM_COMMENT_APPEND;
            } else {
                return ITEM_COMMENT_FINISH;
            }
        } else if (status == Constants.MALL_ORDER_STATUS_REFUND) {
            return ITEM_REFUND;
        }
        return ITEM_CLOSE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == ITEM_CLOSE) {
            return new Vh(mInflater.inflate(R.layout.item_buyer_order_all, viewGroup, false));
        } else if (i == ITEM_WAIT_PAY) {
            return new PayVh(mInflater.inflate(R.layout.item_buyer_order_pay, viewGroup, false));
        } else if (i == ITEM_WAIT_SEND) {
            return new BaseVh(mInflater.inflate(R.layout.item_buyer_order_send, viewGroup, false));
        } else if (i == ITEM_WAIT_RECEIVE) {
            return new WaitReceiveVh(mInflater.inflate(R.layout.item_buyer_order_receive, viewGroup, false));
        } else if (i == ITEM_WAIT_COMMENT) {
            return new WaitCommentVh(mInflater.inflate(R.layout.item_buyer_order_comment, viewGroup, false));
        } else if (i == ITEM_COMMENT_FINISH) {
            return new FinishVh(mInflater.inflate(R.layout.item_buyer_order_all_finish, viewGroup, false));
        } else if (i == ITEM_COMMENT_APPEND) {
            return new AppendVh(mInflater.inflate(R.layout.item_buyer_order_all_append, viewGroup, false));
        } else if (i == ITEM_REFUND) {
            return new RefundVh(mInflater.inflate(R.layout.item_buyer_order_refund, viewGroup, false));
        }
        return new Vh(mInflater.inflate(R.layout.item_buyer_order_all, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        if (vh instanceof AppendVh) {
            ((AppendVh) vh).setData(mList.get(i), i);
        } else {
            ((BaseVh) vh).setData(mList.get(i));
        }
    }

    class Vh extends BuyerOrderBaseAdapter.BaseVh {

        View mBtnDelete;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mBtnDelete = itemView.findViewById(R.id.btn_delete_order);
            mBtnDelete.setOnClickListener(mDeleteClickListener);
        }

        @Override
        public void setData(BuyerOrderBean bean) {
            mBtnDelete.setTag(bean);
            super.setData(bean);
        }
    }

    class AppendVh extends Vh {

        View mBtnWuliu;
        View mBtnAppend;


        public AppendVh(@NonNull View itemView) {
            super(itemView);
            mBtnWuliu = itemView.findViewById(R.id.btn_wuliu);
            mBtnAppend = itemView.findViewById(R.id.btn_append);
            mBtnWuliu.setOnClickListener(mWuliuClickListener);
            mBtnAppend.setOnClickListener(mAppendClickListener);
        }

        public void setData(BuyerOrderBean bean, int position) {
            mBtnWuliu.setTag(bean);
            mBtnAppend.setTag(position);
            super.setData(bean);
        }
    }


    class PayVh extends BuyerOrderBaseAdapter.BaseVh {

        View mBtnCancel;
        View mBtnPay;

        public PayVh(@NonNull View itemView) {
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


    class WaitReceiveVh extends BuyerOrderBaseAdapter.BaseVh {

        View mBtnWuLiu;
        View mBtnConfirm;

        public WaitReceiveVh(@NonNull View itemView) {
            super(itemView);
            mBtnWuLiu = itemView.findViewById(R.id.btn_wuliu);
            mBtnConfirm = itemView.findViewById(R.id.btn_confirm);
            mBtnWuLiu.setOnClickListener(mWuliuClickListener);
            mBtnConfirm.setOnClickListener(mConfirmClickListener);
        }

        @Override
        public void setData(BuyerOrderBean bean) {
            mBtnWuLiu.setTag(bean);
            mBtnConfirm.setTag(bean);
            super.setData(bean);
        }
    }


    class FinishVh extends Vh {

        View mBtnWuLiu;

        public FinishVh(@NonNull View itemView) {
            super(itemView);
            mBtnWuLiu = itemView.findViewById(R.id.btn_wuliu);
            mBtnWuLiu.setOnClickListener(mWuliuClickListener);
        }

        @Override
        public void setData(BuyerOrderBean bean) {
            mBtnWuLiu.setTag(bean);
            super.setData(bean);
        }
    }

    class WaitCommentVh extends BuyerOrderBaseAdapter.BaseVh {

        View mBtnComment;

        public WaitCommentVh(@NonNull View itemView) {
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

    class RefundVh extends BuyerOrderBaseAdapter.BaseVh {

        View mBtnRefund;

        public RefundVh(@NonNull View itemView) {
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
