package com.yunbao.mall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.GoodsCommentTypeBean;

import java.util.List;

public class GoodsCommentTypeAdapter extends RefreshAdapter<GoodsCommentTypeBean> {

    private int mCheckedColor;
    private int mUnCheckedColor;
    private View.OnClickListener mOnClickListener;
    private int mCheckedPosition;

    public GoodsCommentTypeAdapter(Context context) {
        super(context);
        GoodsCommentTypeBean bean = new GoodsCommentTypeBean("all", WordUtil.getString(R.string.all));
        bean.setChecked(true);
        mList.add(bean);
        mList.add(new GoodsCommentTypeBean("img", WordUtil.getString(R.string.mall_290)));
        mList.add(new GoodsCommentTypeBean("video", WordUtil.getString(R.string.video)));
        mList.add(new GoodsCommentTypeBean("append", WordUtil.getString(R.string.mall_291)));
        mCheckedColor = ContextCompat.getColor(context, R.color.white);
        mUnCheckedColor = ContextCompat.getColor(context, R.color.global);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                if (mCheckedPosition == position) {
                    return;
                }
                GoodsCommentTypeBean bean = mList.get(position);
                bean.setChecked(true);
                mList.get(mCheckedPosition).setChecked(false);
                notifyItemChanged(position, Constants.PAYLOAD);
                notifyItemChanged(mCheckedPosition, Constants.PAYLOAD);
                mCheckedPosition = position;
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(bean, position);
                }
            }
        };
    }

    public void setTypeCount(String allCount, String imgCount, String videoCount, String appendCount) {
        mList.get(0).setCount(allCount);
        mList.get(1).setCount(imgCount);
        mList.get(2).setCount(videoCount);
        mList.get(3).setCount(appendCount);
        notifyDataSetChanged();
    }

    public String getCheckedType() {
        return mList.get(mCheckedPosition).getType();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_goods_comment_type, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    class Vh extends RecyclerView.ViewHolder {

        Drawable mCheckedDrawable;
        Drawable mUnCheckedDrawable;
        TextView mTextView;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mCheckedDrawable = ContextCompat.getDrawable(mContext, R.drawable.goods_03);
            mUnCheckedDrawable = ContextCompat.getDrawable(mContext, R.drawable.goods_04);
            itemView.setOnClickListener(mOnClickListener);
            mTextView = (TextView) itemView;
        }

        void setData(GoodsCommentTypeBean bean, int position, Object payload) {
            if (payload == null) {
                itemView.setTag(position);
                mTextView.setText(bean.getText());
            }
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
