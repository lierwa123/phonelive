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

public class SellerXiaJiaAdapter extends RefreshAdapter<GoodsManageBean> {

    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mEditClickListener;
    private View.OnClickListener mDeleteClickListener;
    private View.OnClickListener mShangJiaClickListener;
    private ActionListener mActionListener;
    private String mSaleString;
    private String mMoneySymbol;

    public SellerXiaJiaAdapter(Context context) {
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
        mEditClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onEditClick((GoodsManageBean) tag);
                }
            }
        };
        mDeleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onDeleteClick((GoodsManageBean) tag);
                }
            }
        };
        mShangJiaClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onShangJiaClick((GoodsManageBean) tag);
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
        return new Vh(mInflater.inflate(R.layout.item_seller_xiajia, viewGroup, false));
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
        View mBtnEdit;
        View mBtnDel;
        View mBtnShangJia;
        View mOutSide;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.thumb);
            mName = itemView.findViewById(R.id.name);
            mSaleNum = itemView.findViewById(R.id.sale_num);
            mPrice = itemView.findViewById(R.id.price);
            mOriginPrice = itemView.findViewById(R.id.origin_price);
            mOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mBtnEdit = itemView.findViewById(R.id.btn_edit);
            mBtnDel = itemView.findViewById(R.id.btn_del);
            mBtnShangJia = itemView.findViewById(R.id.btn_shangjia);
            mOutSide = itemView.findViewById(R.id.out_side);
            itemView.setOnClickListener(mOnClickListener);
            mBtnEdit.setOnClickListener(mEditClickListener);
            mBtnDel.setOnClickListener(mDeleteClickListener);
            mBtnShangJia.setOnClickListener(mShangJiaClickListener);
        }


        void setData(GoodsManageBean bean) {
            itemView.setTag(bean);
            mBtnEdit.setTag(bean);
            mBtnDel.setTag(bean);
            mBtnShangJia.setTag(bean);
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
            } else {
                mSaleNum.setText(String.format(mSaleString, bean.getSaleNum()));
                if (mOriginPrice.getVisibility() == View.VISIBLE) {
                    mOriginPrice.setVisibility(View.INVISIBLE);
                }
                if (mOutSide.getVisibility() != View.GONE) {
                    mOutSide.setVisibility(View.GONE);
                }
            }
        }
    }

    public interface ActionListener {
        void onItemClick(GoodsManageBean bean);

        void onEditClick(GoodsManageBean bean);

        void onDeleteClick(GoodsManageBean bean);

        void onShangJiaClick(GoodsManageBean bean);
    }

}
