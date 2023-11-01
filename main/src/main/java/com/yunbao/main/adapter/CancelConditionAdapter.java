package com.yunbao.main.adapter;

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
import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;
import com.yunbao.main.bean.CancelConditionBean;

import java.util.List;

public class CancelConditionAdapter extends RefreshAdapter<CancelConditionBean> {

    private Drawable mDrawable0;
    private Drawable mDrawable1;
    private int mColor0;
    private int mColor1;
    private String mString0;
    private String mString1;

    public CancelConditionAdapter(Context context, List<CancelConditionBean> list) {
        super(context, list);
        mDrawable0 = ContextCompat.getDrawable(context, R.mipmap.icon_cancel_account_0);
        mDrawable1 = ContextCompat.getDrawable(context, R.mipmap.icon_cancel_account_1);
        mColor0 = ContextCompat.getColor(context, R.color.global);
        mColor1 = ContextCompat.getColor(context, R.color.gray1);
        mString0 = WordUtil.getString(R.string.cancel_account_6);
        mString1 = WordUtil.getString(R.string.cancel_account_5);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_cancel_condition, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mContent;
        ImageView mImg;
        TextView mStatus;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mContent = itemView.findViewById(R.id.content);
            mImg = itemView.findViewById(R.id.img);
            mStatus = itemView.findViewById(R.id.status);
        }

        void setData(CancelConditionBean bean) {
            mTitle.setText(bean.getTitle());
            mContent.setText(bean.getContent());
            if (bean.getIsOK() == 1) {
                mStatus.setText(mString1);
                mStatus.setTextColor(mColor1);
                mImg.setImageDrawable(mDrawable1);
            } else {
                mStatus.setText(mString0);
                mStatus.setTextColor(mColor0);
                mImg.setImageDrawable(mDrawable0);
            }
        }
    }
}
