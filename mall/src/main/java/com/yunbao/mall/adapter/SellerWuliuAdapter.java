package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.WuliuBean;

import java.util.List;

public class SellerWuliuAdapter extends RefreshAdapter<WuliuBean> {

    private View.OnClickListener mOnClickListener;


    public SellerWuliuAdapter(Context context, List<WuliuBean> list) {
        super(context, list);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick((WuliuBean) tag, 0);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_seller_wuliu, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mThumb;
        TextView mName;


        public Vh(@NonNull View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.thumb);
            mName = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(WuliuBean bean) {
            itemView.setTag(bean);
            ImgLoader.display(mContext, bean.getThumb(), mThumb);
            mName.setText(bean.getName());
        }
    }
}
