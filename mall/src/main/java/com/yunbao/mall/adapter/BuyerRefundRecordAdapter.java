package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.BuyerRefundRecordBean;

public class BuyerRefundRecordAdapter extends RefreshAdapter<BuyerRefundRecordBean> {

    public BuyerRefundRecordAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_buyer_refund_record, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }


    class Vh extends RecyclerView.ViewHolder {

        TextView mBalance;
        TextView mTime;
        TextView mResult;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mBalance = itemView.findViewById(R.id.balance);
            mTime = itemView.findViewById(R.id.time);
            mResult = itemView.findViewById(R.id.result);
        }

        void setData(BuyerRefundRecordBean bean) {
            mBalance.setText(bean.getBalance());
            mTime.setText(bean.getAddTime());
            mResult.setText(bean.getResult());
        }
    }
}
