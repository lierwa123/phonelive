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

import com.yunbao.common.Constants;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.SearchPayBean;

import java.util.List;

public class SearchPayAdapter extends RefreshAdapter<SearchPayBean> {

    private Drawable mCheckedDrawable;
    private View.OnClickListener mOnClickListener;
    private int mCheckedPosition = -1;

    public SearchPayAdapter(Context context) {
        super(context);
        mCheckedDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_check_1);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                if (position == mCheckedPosition) {
                    cancelChecked();
                    return;
                }
                if (mCheckedPosition >= 0) {
                    mList.get(mCheckedPosition).setChecked(false);
                    notifyItemChanged(mCheckedPosition, Constants.PAYLOAD);
                }
                SearchPayBean bean = mList.get(position);
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
        return new Vh(mInflater.inflate(R.layout.item_search_pay, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    public SearchPayBean getCheckedBean() {
        if (mCheckedPosition >= 0) {
            return mList.get(mCheckedPosition);
        }
        return null;
    }

    public void cancelChecked() {
        if (mCheckedPosition >= 0) {
            mList.get(mCheckedPosition).setChecked(false);
            notifyItemChanged(mCheckedPosition, Constants.PAYLOAD);
        }
        mCheckedPosition = -1;
    }

    @Override
    public void refreshData(List<SearchPayBean> list) {
        super.refreshData(list);
        mCheckedPosition = -1;
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mThumb;
        TextView mName;
        TextView mNum;
        TextView mPrice;
        ImageView mImgCheck;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.thumb);
            mName = itemView.findViewById(R.id.name);
            mNum = itemView.findViewById(R.id.num);
            mPrice = itemView.findViewById(R.id.price);
            mImgCheck = itemView.findViewById(R.id.img_check);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(SearchPayBean bean, int position, Object payload) {
            if (payload == null) {
                itemView.setTag(position);
                ImgLoader.display(mContext, bean.getThumb(), mThumb);
                mName.setText(bean.getName());
                mNum.setText(bean.getVideoNum());
                mPrice.setText(bean.getPrice());
            }
            mImgCheck.setImageDrawable(bean.isChecked() ? mCheckedDrawable : null);
        }

    }


}
