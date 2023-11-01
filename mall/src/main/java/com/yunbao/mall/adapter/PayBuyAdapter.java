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
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.PayContentBuyBean;

public class PayBuyAdapter extends RefreshAdapter<PayContentBuyBean> {

    private View.OnClickListener mOnClickListener;

    public PayBuyAdapter(Context context) {
        super(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayContentBuyBean bean = (PayContentBuyBean) v.getTag();
                RouteUtil.forwardPayContentDetail(mContext, bean.getGoodsId());
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_pay_buy, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mThumb;
        TextView mTitle;
        TextView mNum;
        TextView mName;
        ImageView mAvatar;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.thumb);
            mTitle = itemView.findViewById(R.id.title);
            mNum = itemView.findViewById(R.id.num);
            mName = itemView.findViewById(R.id.name);
            mAvatar = itemView.findViewById(R.id.avatar);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(PayContentBuyBean bean) {
            itemView.setTag(bean);
            ImgLoader.display(mContext, bean.getThumb(), mThumb);
            ImgLoader.displayAvatar(mContext, bean.getAvatar(), mAvatar);
            mTitle.setText(bean.getTitle());
            mNum.setText(bean.getVideoNum());
            mName.setText(bean.getUserName());
        }
    }
}
