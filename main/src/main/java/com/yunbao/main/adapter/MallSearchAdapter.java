package com.yunbao.main.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;
import com.yunbao.mall.activity.GoodsDetailActivity;
import com.yunbao.mall.bean.GoodsSimpleBean;

public class MallSearchAdapter extends RefreshAdapter<GoodsSimpleBean> {

    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private View.OnClickListener mOnClickListener;
    private String mSaleString;
    private String mMoneySymbol;
    private boolean mLayoutLinear;

    public MallSearchAdapter(Context context) {
        super(context);
        mSaleString = WordUtil.getString(R.string.mall_114);
        mMoneySymbol = WordUtil.getString(R.string.money_symbol);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canClick()) {
                    return;
                }
                GoodsSimpleBean bean = (GoodsSimpleBean) v.getTag();
                GoodsDetailActivity.forward(mContext, bean.getId(), false, bean.getType());
            }
        };
    }

    public void setLayoutLinear(boolean layoutLinear) {
        mLayoutLinear = layoutLinear;
    }


    @Override
    public int getItemViewType(int position) {
        if (mLayoutLinear) {
            return 0;
        }
        if (position % 2 == 1) {
            return RIGHT;
        }
        return LEFT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (mLayoutLinear) {
            return new Vh(mInflater.inflate(R.layout.item_shop_home_linear, viewGroup, false));
        }
        if (viewType == LEFT) {
            return new Vh(mInflater.inflate(R.layout.item_shop_home_left, viewGroup, false));
        }
        return new Vh(mInflater.inflate(R.layout.item_shop_home_right, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof Vh) {
            ((Vh) vh).setData(mList.get(position));
        }
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mThumb;
        TextView mName;
        TextView mPirce;
        TextView mSaleNum;
        TextView mOriginPrice;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.thumb);
            mName = itemView.findViewById(R.id.name);
            mPirce = itemView.findViewById(R.id.price);
            mSaleNum = itemView.findViewById(R.id.sale_num);
            mOriginPrice = itemView.findViewById(R.id.origin_price);
            mOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(GoodsSimpleBean bean) {
            itemView.setTag(bean);
            ImgLoader.display(mContext, bean.getThumb(), mThumb);
            mName.setText(bean.getName());
            mPirce.setText(StringUtil.contact(mMoneySymbol, bean.getPrice()));
            if (bean.getType() == 1) {
                mSaleNum.setText(null);
                mOriginPrice.setText(bean.getOriginPrice());
            } else {
                mSaleNum.setText(String.format(mSaleString, bean.getSaleNum()));
                mOriginPrice.setText(null);
            }
        }

    }
}
