package com.yunbao.mall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.GoodsChooseSpecBean;

import java.util.List;

public class GoodsChooseSpecAdapter extends RefreshAdapter<GoodsChooseSpecBean> {


    private int mCheckedColor;
    private int mUnCheckedColor;
    private View.OnClickListener mOnClickListener;
    private int mCheckedPosition;

    public GoodsChooseSpecAdapter(Context context, List<GoodsChooseSpecBean> list) {
        super(context, list);
        for (int i = 0, size = list.size(); i < size; i++) {
            if (list.get(i).isChecked()) {
                mCheckedPosition = i;
                break;
            }
        }
        mCheckedColor = ContextCompat.getColor(context, R.color.white);
        mUnCheckedColor = ContextCompat.getColor(context, R.color.textColor2);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                if (position == mCheckedPosition) {
                    return;
                }
                mList.get(position).setChecked(true);
                mList.get(mCheckedPosition).setChecked(false);
                notifyItemChanged(position);
                notifyItemChanged(mCheckedPosition);
                mCheckedPosition = position;
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mList.get(position), position);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_goods_choose_spec, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i), i);
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mTextView;
        Drawable mCheckedDrawable;
        Drawable mUnCheckedDrawable;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mCheckedDrawable = ContextCompat.getDrawable(mContext, R.drawable.goods_01);
            mUnCheckedDrawable = ContextCompat.getDrawable(mContext, R.drawable.goods_02);
            mTextView = (TextView) itemView;
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(GoodsChooseSpecBean bean, int position) {
            mTextView.setTag(position);
            mTextView.setText(bean.getName());
            if (bean.isChecked()) {
                mTextView.setTextColor(mCheckedColor);
                mTextView.setBackground(mCheckedDrawable);
            } else {
                mTextView.setTextColor(mUnCheckedColor);
                mTextView.setBackground(mUnCheckedDrawable);
            }
        }
    }
}
