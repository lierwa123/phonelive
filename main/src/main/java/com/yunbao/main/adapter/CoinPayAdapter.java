package com.yunbao.main.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.bean.CoinPayBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.main.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2019/4/11.
 */

public class CoinPayAdapter extends RecyclerView.Adapter<CoinPayAdapter.Vh> {

    private Context mContext;
    private List<CoinPayBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private int mCheckedPosition;
    private OnItemClickListener<CoinPayBean> mOnItemClickListener;
    private Drawable mCheckedBg;
    private Drawable mUnCheckedBg;
    private Drawable mCheckedDrawable;
    private Drawable mUnCheckedDrawable;

    public CoinPayAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = new ArrayList<>();
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                int position = (int) tag;
                if (mCheckedPosition != position) {
                    if (mCheckedPosition >= 0 && mCheckedPosition < mList.size()) {
                        mList.get(mCheckedPosition).setChecked(false);
                        notifyItemChanged(mCheckedPosition, Constants.PAYLOAD);
                    }
                    CoinPayBean bean = mList.get(position);
                    bean.setChecked(true);
                    notifyItemChanged(position, Constants.PAYLOAD);
                    mCheckedPosition = position;
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(bean, position);
                    }
                }
            }
        };
        mCheckedBg = ContextCompat.getDrawable(mContext, R.drawable.bg_my_coin_pay_check);
        mUnCheckedBg = ContextCompat.getDrawable(mContext, R.drawable.bg_my_coin_pay_uncheck);
        mCheckedDrawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_check_2_1);
        mUnCheckedDrawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_check_2_0);
    }


    public void setOnItemClickListener(OnItemClickListener<CoinPayBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<CoinPayBean> list) {
        mCheckedPosition = 0;
        if (list != null && list.size() > 0) {
            mList.clear();
            list.get(0).setChecked(true);
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_coin_pay, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position, @NonNull List<Object> payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        vh.setData(mList.get(position), position, payload);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mName;
        ImageView mThumb;
        View mBg;
        ImageView mCheck;

        public Vh(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mThumb = itemView.findViewById(R.id.thumb);
            mBg = itemView.findViewById(R.id.bg);
            mCheck = itemView.findViewById(R.id.check);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(CoinPayBean bean, int position, Object payload) {
            if (payload == null) {
                itemView.setTag(position);
                mName.setText(bean.getName());
                ImgLoader.display(mContext, bean.getThumb(), mThumb);
            }
            if (bean.isChecked()) {
                mBg.setBackground(mCheckedBg);
                mCheck.setImageDrawable(mCheckedDrawable);
            } else {
                mBg.setBackground(mUnCheckedBg);
                mCheck.setImageDrawable(mUnCheckedDrawable);
            }
        }
    }


    public CoinPayBean getPayCoinPayBean() {
        if (mList != null && mList.size() > 0) {
            if (mCheckedPosition >= 0 && mCheckedPosition < mList.size()) {
                CoinPayBean bean = mList.get(mCheckedPosition);
                if (bean != null) {
                    return bean;
                }
            }
        }
        return null;
    }
}
