package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.BuyerAddressBean;

public class BuyerAddressAdapter extends RefreshAdapter<BuyerAddressBean> {

    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mOnEditClickListener;
    private ActionListener mActionListener;

    public BuyerAddressAdapter(Context context) {
        super(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onItemClick((BuyerAddressBean) tag);
                }
            }
        };
        mOnEditClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onEditClick((BuyerAddressBean) tag);
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
        return new Vh(mInflater.inflate(R.layout.item_buyer_address, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mName;
        TextView mAddress;
        View mTagDefault;
        View mBtnEdit;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mAddress = itemView.findViewById(R.id.address);
            mTagDefault = itemView.findViewById(R.id.tag_default);
            mBtnEdit = itemView.findViewById(R.id.btn_edit);
            itemView.setOnClickListener(mOnClickListener);
            mBtnEdit.setOnClickListener(mOnEditClickListener);
        }

        void setData(BuyerAddressBean bean) {
            mBtnEdit.setTag(bean);
            itemView.setTag(bean);
            mName.setText(StringUtil.contact(bean.getName(), "  ", bean.getPhoneNum()));
            mAddress.setText(StringUtil.contact(bean.getProvince(), " ", bean.getCity(), " ", bean.getZone(), " ", bean.getAddress()));
            if (bean.getIsDefault() == 1) {
                if (mTagDefault.getVisibility() != View.VISIBLE) {
                    mTagDefault.setVisibility(View.VISIBLE);
                }
            } else {
                if (mTagDefault.getVisibility() == View.VISIBLE) {
                    mTagDefault.setVisibility(View.INVISIBLE);
                }
            }
        }
    }


    public interface ActionListener {
        void onItemClick(BuyerAddressBean bean);

        void onEditClick(BuyerAddressBean bean);
    }
}
