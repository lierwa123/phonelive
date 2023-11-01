package com.yunbao.main.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.bean.CoinBean;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/10/23.
 */

public class CoinAdapter extends RecyclerView.Adapter {

    private String mCoinName;
    private List<CoinBean> mList;
    private String mGiveString;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<CoinBean> mOnItemClickListener;
    private Drawable mCheckedDrawable;
    private Drawable mUnCheckedDrawable;
    private int mCheckedPosition = 0;
    private boolean mIsPaypal;

    public CoinAdapter(Context context, String coinName) {
        mCoinName = coinName;
        mList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        mGiveString = WordUtil.getString(R.string.coin_give);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                int positon = (int) tag;
                CoinBean bean = mList.get(positon);
                if (mCheckedPosition != positon) {
                    if (mCheckedPosition >= 0 && mCheckedPosition < mList.size()) {
                        mList.get(mCheckedPosition).setChecked(false);
                        notifyItemChanged(mCheckedPosition, Constants.PAYLOAD);
                    }
                    bean.setChecked(true);
                    notifyItemChanged(positon, Constants.PAYLOAD);
                    mCheckedPosition = positon;
                }
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(bean, positon);
                }
            }
        };
        mCheckedDrawable = ContextCompat.getDrawable(context, R.mipmap.bg_coin_item_1);
        mUnCheckedDrawable = ContextCompat.getDrawable(context, R.mipmap.bg_coin_item_0);
    }

    public CoinBean getCheckedBean() {
        if (mList != null && mList.size() > 0 && mCheckedPosition >= 0) {
            return mList.get(mCheckedPosition);
        }
        return null;
    }

    public void setIsPaypal(boolean isPaypal) {
        if (mIsPaypal == isPaypal) {
            return;
        }
        mIsPaypal = isPaypal;
        notifyDataSetChanged();
    }

    public void setList(List<CoinBean> list) {
        mCheckedPosition = 0;
        if (list != null && list.size() > 0) {
            mList.clear();
            list.get(0).setChecked(true);
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener<CoinBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_coin, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        if (vh instanceof Vh) {
            ((Vh) vh).setData(mList.get(position), position, payload);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class Vh extends RecyclerView.ViewHolder {

        TextView mCoin;
        TextView mMoney;
        TextView mGive;
        View mBg;

        public Vh(View itemView) {
            super(itemView);
            mCoin = itemView.findViewById(R.id.coin);
            mMoney = itemView.findViewById(R.id.money);
            mGive = itemView.findViewById(R.id.give);
            mBg = itemView.findViewById(R.id.bg);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(CoinBean bean, int position, Object payload) {
            if (payload == null) {
                itemView.setTag(position);
                mCoin.setText(mIsPaypal ? bean.getCoinPaypal() : bean.getCoin());
                mMoney.setText(StringUtil.contact(mIsPaypal ? "$" : "￥", bean.getMoney()));
                if (!"0".equals(bean.getGive())) {
                    if (mGive.getVisibility() != View.VISIBLE) {
                        mGive.setVisibility(View.VISIBLE);
                    }
                    mGive.setText(StringUtil.contact(mGiveString, bean.getGive(), mCoinName));
                } else {
                    if (mGive.getVisibility() == View.VISIBLE) {
                        mGive.setVisibility(View.INVISIBLE);
                    }
                }
            }
            mBg.setBackground(bean.isChecked() ? mCheckedDrawable : mUnCheckedDrawable);
        }
    }

}
