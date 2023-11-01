package com.yunbao.mall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.PayContentClassBean;

import java.util.List;

public class PayContentClassAdapter extends RefreshAdapter<PayContentClassBean> {

    private Drawable mCheckedDrawable;
    private View.OnClickListener mOnClickListener;

    public PayContentClassAdapter(Context context) {
        super(context);
        mCheckedDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_check_1);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick((PayContentClassBean) tag, 0);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_pay_content_class, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), payload);
    }


    class Vh extends RecyclerView.ViewHolder {

        TextView mName;
        ImageView mImg;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mImg = itemView.findViewById(R.id.img);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(PayContentClassBean bean, Object payload) {
            if (payload == null) {
                itemView.setTag(bean);
                mName.setText(bean.getName());
            }
            mImg.setImageDrawable(bean.isChecked() ? mCheckedDrawable : null);
        }
    }
}
