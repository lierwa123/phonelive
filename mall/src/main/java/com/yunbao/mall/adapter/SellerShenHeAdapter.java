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

public class SellerShenHeAdapter extends RefreshAdapter<GoodsManageBean> {

    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mEditClickListener;
    private View.OnClickListener mDeleteClickListener;
    private ActionListener mActionListener;
    private String mSaleString;
    private String mMoneySymbol;
    private String mStatusShenHe;//审核中
    private String mStatusRefuse;//已拒绝


    public SellerShenHeAdapter(Context context) {
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
        mStatusShenHe = WordUtil.getString(R.string.mall_117);
        mStatusRefuse = WordUtil.getString(R.string.mall_380);
    }


    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_seller_shenhe, viewGroup, false));
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
        View mOutSide;
        TextView mStatus;

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
            mOutSide = itemView.findViewById(R.id.out_side);
            mStatus = itemView.findViewById(R.id.status);
            itemView.setOnClickListener(mOnClickListener);
            mBtnEdit.setOnClickListener(mEditClickListener);
            mBtnDel.setOnClickListener(mDeleteClickListener);
        }


        void setData(GoodsManageBean bean) {
            itemView.setTag(bean);
            mBtnEdit.setTag(bean);
            mBtnDel.setTag(bean);
            ImgLoader.display(mContext, bean.getThumb(), mThumb);
            mName.setText(bean.getName());
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
            if (bean.getStatus() == 0) {
                mStatus.setText(mStatusShenHe);
            } else if (bean.getStatus() == 2) {
                mStatus.setText(mStatusRefuse);
            } else {
                mStatus.setText(null);
            }
        }
    }

    public interface ActionListener {
        void onItemClick(GoodsManageBean bean);

        void onEditClick(GoodsManageBean bean);

        void onDeleteClick(GoodsManageBean bean);
    }

}
