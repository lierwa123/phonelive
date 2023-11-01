package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.AddGoodsSpecBean;

import java.util.List;

public class GoodsEditSpecAdapter extends RefreshAdapter<AddGoodsSpecBean> {

    private String mSpecString;

    public GoodsEditSpecAdapter(Context context, List<AddGoodsSpecBean> list) {
        super(context, list);
        mSpecString = WordUtil.getString(R.string.mall_089);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_goods_edit_spec, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i), i);
    }


    class Vh extends RecyclerView.ViewHolder {

        TextView mIndex;
        TextView mName;
        EditText mNum;
        EditText mPrice;
        AddGoodsSpecBean mBean;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mIndex = itemView.findViewById(R.id.index);
            mName = itemView.findViewById(R.id.name);
            mNum = itemView.findViewById(R.id.num);
            mPrice = itemView.findViewById(R.id.price);
            mNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mBean != null) {
                        mBean.setNum(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            mPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mBean != null) {
                        mBean.setPrice(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        void setData(AddGoodsSpecBean bean, int position) {
            mBean = bean;
            mIndex.setText(StringUtil.contact(mSpecString, String.valueOf(position + 1)));
            mName.setText(bean.getName());
            mNum.setText(bean.getNum());
            mPrice.setText(bean.getPrice());
        }
    }


}
