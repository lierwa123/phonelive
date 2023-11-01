package com.yunbao.mall.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.GoodsClassTitleBean;

import java.util.List;

public class GoodsClassLeftAdapter extends RefreshAdapter<GoodsClassTitleBean> {

    private int mCheckedColor;
    private int mUnCheckedColor;
    private View.OnClickListener mOnClickListener;
    private int mCheckedPosition;

    public GoodsClassLeftAdapter(Context context, List<GoodsClassTitleBean> list) {
        super(context, list);
        mCheckedColor = ContextCompat.getColor(context, R.color.white);
        mUnCheckedColor = Color.parseColor("#f0f0f0");
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                int position = (int) tag;
                if (mCheckedPosition == position) {
                    return;
                }
                GoodsClassTitleBean bean = mList.get(position);
                bean.setChecked(true);
                notifyItemChanged(position, Constants.PAYLOAD);
                if (mCheckedPosition >= 0) {
                    mList.get(mCheckedPosition).setChecked(false);
                    notifyItemChanged(mCheckedPosition, Constants.PAYLOAD);
                }
                mCheckedPosition = position;
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(bean, position);
                }
            }
        };
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_goods_class_left, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mTextView;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(GoodsClassTitleBean bean, int position, Object payload) {
            if (payload == null) {
                itemView.setTag(position);
                mTextView.setText(bean.getName());
            }
            mTextView.setBackgroundColor(bean.isChecked() ? mCheckedColor : mUnCheckedColor);
        }
    }
}
