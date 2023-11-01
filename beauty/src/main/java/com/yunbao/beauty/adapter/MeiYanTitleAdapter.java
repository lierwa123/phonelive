package com.yunbao.beauty.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.beauty.R;
import com.yunbao.beauty.bean.MeiYanTypeBean;
import com.yunbao.beauty.constant.Constants;
import com.yunbao.beauty.interfaces.OnItemClickListener;
import com.yunbao.beauty.utils.MhDataManager;
import com.yunbao.beauty.utils.WordUtil;

import java.util.List;

public class MeiYanTitleAdapter extends RecyclerView.Adapter {

    private LayoutInflater mInflater;
    private List<MeiYanTypeBean> mList;
    private int mColor0;
    private int mColor1;
    private int mCheckedPosition;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<MeiYanTypeBean> mOnItemClickListener;

    public MeiYanTitleAdapter(Context context, List<MeiYanTypeBean> list) {
        mList = list;
        mInflater = LayoutInflater.from(context);
        mColor0 = ContextCompat.getColor(context, R.color.textColor3);
        mColor1 = ContextCompat.getColor(context, R.color.global);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mList.get(position), position);
                }
            }
        };
    }


    public void setCheckedPosition(int position) {
        if (position != mCheckedPosition) {
            mList.get(position).setChecked(true);
            mList.get(mCheckedPosition).setChecked(false);
            notifyItemChanged(position, Constants.PAYLOAD);
            notifyItemChanged(mCheckedPosition, Constants.PAYLOAD);
            mCheckedPosition = position;
        }
    }

    public void setOnItemClickListener(OnItemClickListener<MeiYanTypeBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_meiyan_title, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class Vh extends RecyclerView.ViewHolder {

        TextView mName;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mName = (TextView) itemView;
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(MeiYanTypeBean bean, int position, Object payload) {
            if (payload == null) {
                itemView.setTag(position);
                mName.setText(WordUtil.getString(MhDataManager.getInstance().getContext(),bean.getName()));
            }
            mName.setTextColor(bean.isChecked() ? mColor1 : mColor0);

        }
    }

}
