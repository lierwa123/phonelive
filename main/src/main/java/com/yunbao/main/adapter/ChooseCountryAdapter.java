package com.yunbao.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.main.R;
import com.yunbao.main.bean.ChooseCountryBean;

import java.util.List;

public class ChooseCountryAdapter extends RefreshAdapter<ChooseCountryBean> {

    private View.OnClickListener mOnClickListener;

    public ChooseCountryAdapter(Context context) {
        super(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick((ChooseCountryBean) v.getTag(), 0);
                }
            }
        };
    }

    public ChooseCountryAdapter(Context context, List<ChooseCountryBean> list) {
        super(context, list);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick((ChooseCountryBean) v.getTag(), 0);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_choose_country, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }

    class Vh extends RecyclerView.ViewHolder {

//        TextView mTel;
        TextView mName;

        public Vh(@NonNull View itemView) {
            super(itemView);
//            mTel = itemView.findViewById(R.id.tel);
            mName = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(ChooseCountryBean bean) {
            itemView.setTag(bean);
//            mTel.setText(StringUtil.contact("+", bean.getTel()));
            mName.setText(bean.getName());
        }
    }
}
