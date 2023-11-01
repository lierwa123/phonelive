package com.yunbao.main.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;
import com.yunbao.mall.bean.GoodsSimpleBean;


public class MainMallAdapter extends RefreshAdapter<GoodsSimpleBean> {

    private static final int HEAD = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private View.OnClickListener mOnClickListener;
    private View mHeadView;
    private String mSaleString;
    private String mMoneySymbol;


    public MainMallAdapter(Context context) {
        super(context);
        mSaleString = WordUtil.getString(R.string.mall_114);
        mMoneySymbol = WordUtil.getString(R.string.money_symbol);
        mHeadView = mInflater.inflate(R.layout.item_main_mall_head, null, false);
        mHeadView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canClick()) {
                    return;
                }
                Object tag = v.getTag();
                if (tag != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick((GoodsSimpleBean) tag, 0);
                }
            }
        };
    }

    public View getHeadView() {
        return mHeadView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD;
        } else if (position % 2 == 0) {
            return RIGHT;
        }
        return LEFT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == HEAD) {
            ViewParent viewParent = mHeadView.getParent();
            if (viewParent != null) {
                ((ViewGroup) viewParent).removeView(mHeadView);
            }
            HeadVh headVh = new HeadVh(mHeadView);
            headVh.setIsRecyclable(false);
            return headVh;
        } else if (viewType == LEFT) {
            return new Vh(mInflater.inflate(R.layout.item_shop_home_left, viewGroup, false));
        }
        return new Vh(mInflater.inflate(R.layout.item_shop_home_right, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof Vh) {
            ((Vh) vh).setData(mList.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    class HeadVh extends RecyclerView.ViewHolder {

        public HeadVh(View itemView) {
            super(itemView);
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