package com.yunbao.mall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.GoodsPayBean;

import java.util.List;

public class GoodsPayAdapter extends RefreshAdapter<GoodsPayBean> {

    private View.OnClickListener mOnClickListener;
    private int mCheckedPosition;
    private String mBalanceVal;
    private Drawable mCheckedDrawable;

    public GoodsPayAdapter(Context context, List<GoodsPayBean> list, String balanceVal) {
        super(context, list);
        mBalanceVal = balanceVal;
        mCheckedDrawable = ContextCompat.getDrawable(mContext, R.mipmap.shop_08);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                if (position == mCheckedPosition) {
                    return;
                }
                mList.get(position).setChecked(true);
                mList.get(mCheckedPosition).setChecked(false);
                notifyItemChanged(position, Constants.PAYLOAD);
                notifyItemChanged(mCheckedPosition, Constants.PAYLOAD);
                mCheckedPosition = position;
            }
        };
    }


    public GoodsPayBean getCheckedPayType() {
        for (GoodsPayBean bean : mList) {
            if (bean.isChecked()) {
                return bean;
            }
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (Constants.PAY_TYPE_BALANCE.equals(mList.get(position).getId())) {
            return -1;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
        if (itemType == -1) {
            return new CoinVh(mInflater.inflate(R.layout.item_goods_pay_coin, viewGroup, false));
        }
        return new Vh(mInflater.inflate(R.layout.item_goods_pay, viewGroup, false));
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

        ImageView mThumb;
        TextView mName;
        ImageView mCheckImg;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.thumb);
            mName = itemView.findViewById(R.id.name);
            mCheckImg = itemView.findViewById(R.id.img_check);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(GoodsPayBean bean, int position, Object payload) {
            if (payload == null) {
                itemView.setTag(position);
                ImgLoader.display(mContext, bean.getThumb(), mThumb);
                mName.setText(bean.getName());
            }
            mCheckImg.setImageDrawable(bean.isChecked() ? mCheckedDrawable : null);
        }
    }

    class CoinVh extends Vh {

        TextView mBalance;

        public CoinVh(@NonNull View itemView) {
            super(itemView);
            mBalance = itemView.findViewById(R.id.balance);
        }

        void setData(GoodsPayBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                mBalance.setText(StringUtil.contact("(", mBalanceVal, ")"));
            }
        }
    }

}
