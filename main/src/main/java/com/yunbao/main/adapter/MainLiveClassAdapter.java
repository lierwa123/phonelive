package com.yunbao.main.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.LiveClassBean;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;

import java.util.List;

public class MainLiveClassAdapter extends RefreshAdapter<LiveClassBean> {

    private int mCheckedColor;
    private int mUnCheckedColor;
    private Drawable mCheckedBg;
    private Drawable mUnCheckedBg;
    private View.OnClickListener mOnClickListener;
    private int mCheckedPosition;

    public MainLiveClassAdapter(Context context) {
        super(context);
        mList.add(new LiveClassBean(0, WordUtil.getString(R.string.live), true));
        mList.add(new LiveClassBean(1, WordUtil.getString(R.string.a_039), false));
        mCheckedColor = ContextCompat.getColor(mContext, R.color.white);
        mUnCheckedColor = ContextCompat.getColor(mContext, R.color.textColor);
        mCheckedBg = ContextCompat.getDrawable(mContext, R.drawable.text_main_live_class_1);
        mUnCheckedBg = ContextCompat.getDrawable(mContext, R.drawable.text_main_live_class_0);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                int position = (int) tag;
                if (mCheckedPosition == position) {
                    return;
                }
                mList.get(mCheckedPosition).setChecked(false);
                notifyItemChanged(mCheckedPosition, Constants.PAYLOAD);
                LiveClassBean bean = mList.get(position);
                bean.setChecked(true);
                notifyItemChanged(position, Constants.PAYLOAD);
                mCheckedPosition = position;
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(bean, position);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_main_live_class, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(i), i, payload);
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mText;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.text);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(LiveClassBean bean, int position, Object payload) {
            if (payload == null) {
                itemView.setTag(position);
                mText.setText(bean.getName());
            }
            if (bean.isChecked()) {
                mText.setTextColor(mCheckedColor);
                mText.setBackground(mCheckedBg);
            } else {
                mText.setTextColor(mUnCheckedColor);
                mText.setBackground(mUnCheckedBg);
            }
        }
    }
}
