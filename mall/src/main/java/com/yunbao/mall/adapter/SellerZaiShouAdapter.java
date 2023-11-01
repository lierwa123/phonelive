package com.yunbao.mall.adapter;

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
import com.yunbao.mall.R;
import com.yunbao.mall.bean.GoodsManageBean;

public class SellerZaiShouAdapter extends RefreshAdapter<GoodsManageBean> {

    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mPirceNumClickListener;
    private View.OnClickListener mXaiJiaClickListener;
    private ActionListener mActionListener;
    private String mSaleString;
    private String mMoneySymbol;


    public SellerZaiShouAdapter(Context context) {
        super(context);
        mSaleString = WordUtil.getString(R.string.mall_114);
        mMoneySymbol = WordUtil.getString(R.string.money_symbol);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onItemClick((GoodsManageBean) tag);
                }
            }
        };
        mPirceNumClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onPriceNumClick((GoodsManageBean) tag);
                }
            }
        };
        mXaiJiaClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onXiaJiaClick((GoodsManageBean) tag);
                }
            }
        };
    }


    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_seller_zaishou, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position));
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mThumb;
        TextView mName;
        TextView mSaleNum;
        TextView mPrice;
        TextView mOriginPrice;
        View mBtnPriceNum;
        View mBtnXiaJia;
        View mOutSide;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.thumb);
            mName = itemView.findViewById(R.id.name);
            mSaleNum = itemView.findViewById(R.id.sale_num);
            mPrice = itemView.findViewById(R.id.price);
            mOriginPrice = itemView.findViewById(R.id.origin_price);
            mOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mBtnPriceNum = itemView.findViewById(R.id.btn_price_num);
            mOutSide = itemView.findViewById(R.id.out_side);
            mBtnXiaJia = itemView.findViewById(R.id.btn_xiajia);
            itemView.setOnClickListener(mOnClickListener);
            mBtnPriceNum.setOnClickListener(mPirceNumClickListener);
            mBtnXiaJia.setOnClickListener(mXaiJiaClickListener);
        }


        void setData(GoodsManageBean bean) {
            itemView.setTag(bean);
            mBtnPriceNum.setTag(bean);
            mBtnXiaJia.setTag(bean);
            ImgLoader.display(mContext, bean.getThumb(), mThumb);
            mName.setText(bean.getName());
            mSaleNum.setText(String.format(mSaleString, bean.getSaleNum()));
            mPrice.setText(StringUtil.contact(mMoneySymbol, bean.getPrice()));
            if (bean.getType() == 1) {
                mSaleNum.setText(null);
                if (mOriginPrice.getVisibility() != View.VISIBLE) {
                    mOriginPrice.setVisibility(View.INVISIBLE);
                }
                mOriginPrice.setText(StringUtil.contact(mMoneySymbol, bean.getOriginPrice()));
                if (mOutSide.getVisibility() != View.VISIBLE) {
                    mOutSide.setVisibility(View.VISIBLE);
                }
                if (mBtnPriceNum.getVisibility() == View.VISIBLE) {
                    mBtnPriceNum.setVisibility(View.INVISIBLE);
                }
            } else {
                mSaleNum.setText(String.format(mSaleString, bean.getSaleNum()));
                if (mOriginPrice.getVisibility() == View.VISIBLE) {
                    mOriginPrice.setVisibility(View.INVISIBLE);
                }
                if (mOutSide.getVisibility() != View.GONE) {
                    mOutSide.setVisibility(View.GONE);
                }
                if (mBtnPriceNum.getVisibility() != View.VISIBLE) {
                    mBtnPriceNum.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public interface ActionListener {
        void onItemClick(GoodsManageBean bean);

        void onPriceNumClick(GoodsManageBean bean);

        void onXiaJiaClick(GoodsManageBean bean);
    }

}
