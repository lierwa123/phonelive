package com.yunbao.live.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.GoodsBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.live.R;
import com.yunbao.live.http.LiveHttpUtil;

/**
 * Created by cxf on 2019/8/29.
 */

public class LiveShopAdapter extends RefreshAdapter<GoodsBean> {

    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mShowClickListener;
    private ActionListener mActionListener;
    private String mMoneySymbol;
    private int mColor0;
    private int mColor1;
    private String mStringYong;

    public LiveShopAdapter(Context context) {
        super(context);
        mMoneySymbol = WordUtil.getString(R.string.money_symbol);
        mStringYong = WordUtil.getString(R.string.mall_408);
        mColor0 = ContextCompat.getColor(context, R.color.global);
        mColor1 = ContextCompat.getColor(context, R.color.gray3);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canClick()) {
                    return;
                }
                final int position = (int) v.getTag();
                final GoodsBean bean = mList.get(position);
                HttpCallback callback = new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0) {
                            mList.remove(position);
                            notifyDataSetChanged();
                            if (mActionListener != null) {
                                mActionListener.onDeleteSuccess();
                                if (bean.getIsLiveShow() == 1) {
                                    mActionListener.onGoodsShowChanged(null);
                                }
                            }
                        } else {
                            ToastUtil.show(msg);
                        }
                    }
                };
                if (bean.getType() == 2) {
                    LiveHttpUtil.setPlatGoodsSale(bean.getId(), 0, callback);
                } else {
                    LiveHttpUtil.shopSetSale(bean.getId(), 0, callback);
                }
            }
        };

        mShowClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canClick()) {
                    return;
                }
                final GoodsBean bean = (GoodsBean) v.getTag();
                LiveHttpUtil.setLiveGoodsShow(bean.getId(), new HttpCallback() {
                            @Override
                            public void onSuccess(int code, String msg, String[] info) {
                                if (code == 0) {
                                    if (info.length > 0) {
                                        JSONObject obj = JSON.parseObject(info[0]);
                                        if (mActionListener != null) {
                                            mActionListener.onGoodsShowChanged(obj.getIntValue("status") == 1 ? bean : null);
                                        }
                                    }
                                }
                                ToastUtil.show(msg);
                            }
                        }
                );
            }
        };

    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position), position);
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mThumb;
        TextView mDes;
        TextView mPrice;
        TextView mPriceOrigin;
        TextView mPriceYong;
        View mBtnDel;
        TextView mBtnShow;

        public Vh(View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.thumb);
            mDes = itemView.findViewById(R.id.des);
            mPrice = itemView.findViewById(R.id.price);
            mPriceOrigin = itemView.findViewById(R.id.price_origin);
            mPriceOrigin.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mPriceYong = itemView.findViewById(R.id.price_yong);
            mBtnDel = itemView.findViewById(R.id.btn_delete);
            mBtnDel.setOnClickListener(mOnClickListener);
            mBtnShow = itemView.findViewById(R.id.btn_goods_show);
            mBtnShow.setOnClickListener(mShowClickListener);
        }

        void setData(GoodsBean bean, int position) {
            mBtnDel.setTag(position);
            mBtnShow.setTag(bean);
            ImgLoader.display(mContext, bean.getThumb(), mThumb);
            mPrice.setText(StringUtil.contact(mMoneySymbol, bean.getPriceNow()));
            if (bean.getType() == 1) {
                if (mPriceOrigin.getVisibility() != View.VISIBLE) {
                    mPriceOrigin.setVisibility(View.VISIBLE);
                }
                mPriceOrigin.setText(StringUtil.contact(mMoneySymbol, bean.getOriginPrice()));
                mPriceYong.setText(null);
            } else {
                if (mPriceOrigin.getVisibility() != View.GONE) {
                    mPriceOrigin.setVisibility(View.GONE);
                }
                if (bean.getType() == 2) {
                    mPriceYong.setText(StringUtil.contact(mStringYong, mMoneySymbol, bean.getPriceYong()));
                } else {
                    mPriceYong.setText(null);
                }
            }
            mDes.setText(bean.getName());
            mBtnShow.setTextColor(bean.getIsLiveShow() == 1 ? mColor1 : mColor0);

        }
    }


    public interface ActionListener {
        void onDeleteSuccess();

        void onGoodsShowChanged(GoodsBean bean);
    }


}
