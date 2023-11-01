package com.yunbao.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.common.R;
import com.yunbao.common.bean.TxLocationPoiBean;

public class ChooseLocationAdapter extends RefreshAdapter<TxLocationPoiBean> {

    private View.OnClickListener mOnClickListener;


    public ChooseLocationAdapter(Context context) {
        super(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (mOnItemClickListener != null) {
                    if (tag == null) {
                        mOnItemClickListener.onItemClick(null, 0);
                    } else {
                        mOnItemClickListener.onItemClick((TxLocationPoiBean) tag, 0);
                    }
                }

            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return -1;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == -1) {
            return new HeadVh(mInflater.inflate(R.layout.item_choose_location_0, viewGroup, false));
        }
        return new Vh(mInflater.inflate(R.layout.item_choose_location, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof Vh) {
            ((Vh) vh).setData(mList.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }


    class HeadVh extends RecyclerView.ViewHolder {

        public HeadVh(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(mOnClickListener);
        }

    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mAddress;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mAddress = itemView.findViewById(R.id.address);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(TxLocationPoiBean bean) {
            itemView.setTag(bean);
            mTitle.setText(bean.getTitle());
            mAddress.setText(bean.getAddress());
        }
    }

}
