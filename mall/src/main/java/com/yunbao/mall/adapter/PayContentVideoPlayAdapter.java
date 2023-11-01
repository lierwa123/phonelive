package com.yunbao.mall.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.PayContentVideoPlayBean;

public class PayContentVideoPlayAdapter extends RefreshAdapter<PayContentVideoPlayBean> {

    private boolean mHasBuy;
    private String mTipString;
    private String mDurationString;
    private View.OnClickListener mOnClickListener;

    public PayContentVideoPlayAdapter(Context context) {
        super(context);
        mDurationString = WordUtil.getString(R.string.mall_349);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                PayContentVideoPlayBean bean = (PayContentVideoPlayBean) tag;
                if (mHasBuy || bean.getIsSee() == 1) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(bean, 0);
                    }
                }
            }
        };
    }

    public void setHasBuy(boolean hasBuy) {
        mHasBuy = hasBuy;
        mTipString = WordUtil.getString(hasBuy ? R.string.mall_350 : R.string.mall_332);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_pay_content_video_play, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i), i);
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mId;
        TextView mTitle;
        TextView mDuration;
        View mBtnPlay;
        TextView mSeeTip;
        View mLine;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mId = itemView.findViewById(R.id.id_val);
            mTitle = itemView.findViewById(R.id.title);
            mDuration = itemView.findViewById(R.id.duration);
            mBtnPlay = itemView.findViewById(R.id.btn_play);
            mSeeTip = itemView.findViewById(R.id.see_tip);
            mLine = itemView.findViewById(R.id.line);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(PayContentVideoPlayBean bean, int position) {
            itemView.setTag(bean);
            mId.setText(bean.getId());
            mTitle.setText(bean.getTitle());
            mDuration.setText(String.format(mDurationString, bean.getDurationString()));
            if (mHasBuy) {
                if (mBtnPlay.getVisibility() != View.VISIBLE) {
                    mBtnPlay.setVisibility(View.VISIBLE);
                }
                mSeeTip.setText(mTipString);
            } else {
                if (bean.getIsSee() == 1) {
                    if (mBtnPlay.getVisibility() != View.VISIBLE) {
                        mBtnPlay.setVisibility(View.VISIBLE);
                    }
                    mSeeTip.setText(mTipString);
                } else {
                    if (mBtnPlay.getVisibility() == View.VISIBLE) {
                        mBtnPlay.setVisibility(View.INVISIBLE);
                    }
                }
            }
            if (position == mList.size() - 1) {
                if (mLine.getVisibility() == View.VISIBLE) {
                    mLine.setVisibility(View.INVISIBLE);
                }
            } else {
                if (mLine.getVisibility() != View.VISIBLE) {
                    mLine.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
