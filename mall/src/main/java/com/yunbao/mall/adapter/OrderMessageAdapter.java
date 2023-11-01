package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.activity.BuyerOrderDetailActivity;
import com.yunbao.mall.activity.BuyerRefundDetailActivity;
import com.yunbao.mall.activity.SellerOrderDetailActivity;
import com.yunbao.mall.activity.SellerRefundDetailActivity;
import com.yunbao.mall.activity.SellerSendActivity;
import com.yunbao.mall.bean.OrderMsgBean;

public class OrderMessageAdapter extends RefreshAdapter<OrderMsgBean> {

    private View.OnClickListener mOnClickListener;
    private SpannableString mSpan;

    public OrderMessageAdapter(Context context) {
        super(context);
        String tip = WordUtil.getString(R.string.mall_366);
        mSpan = new SpannableString(tip);
        mSpan.setSpan(new ForegroundColorSpan(0xff0072ff), 0, tip.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderMsgBean bean = (OrderMsgBean) v.getTag();
                if(bean.getIsYong()==1){
                    return;
                }
                if (bean.getType() == 0) {//0 买家 1卖家
                    if (bean.getStatus() == Constants.MALL_ORDER_STATUS_REFUND) {
                        BuyerRefundDetailActivity.forward(mContext, bean.getOrderId());
                    } else {
                        BuyerOrderDetailActivity.forward(mContext, bean.getOrderId());
                    }
                } else {
                    int status = bean.getStatus();
                    if (status == Constants.MALL_ORDER_STATUS_REFUND) {
                        SellerRefundDetailActivity.forward(mContext, bean.getOrderId());
                    } else if (status == Constants.MALL_ORDER_STATUS_WAIT_SEND) {
                        SellerSendActivity.forward(mContext, bean.getOrderId());
                    } else {
                        SellerOrderDetailActivity.forward(mContext, bean.getOrderId());
                    }
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_order_msg, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mTime;
        ImageView mAvatar;
        TextView mText;
        View mBtnBubble;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mTime = itemView.findViewById(R.id.time);
            mAvatar = itemView.findViewById(R.id.avatar);
            mText = itemView.findViewById(R.id.text);
            mBtnBubble = itemView.findViewById(R.id.btn_bubble);
            mBtnBubble.setOnClickListener(mOnClickListener);
        }

        void setData(OrderMsgBean bean) {
            mBtnBubble.setTag(bean);
            mTime.setText(bean.getAddTime());
            ImgLoader.displayAvatar(mContext, bean.getAvatar(), mAvatar);
            mText.setText(bean.getTitle());
            if (bean.getIsYong() == 0) {
                mText.append(mSpan);
            }

        }
    }
}
