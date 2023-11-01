package com.yunbao.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.main.R;
import com.yunbao.main.activity.ActiveTopicActivity;
import com.yunbao.main.bean.ActiveTopicBean;

public class ActiveChooseTopicAdapter extends RefreshAdapter<ActiveTopicBean> {

    private View.OnClickListener mOnClickListener;

    public ActiveChooseTopicAdapter(Context context) {
        super(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActiveTopicBean bean = (ActiveTopicBean) v.getTag();
                if (bean != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(bean, 0);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_active_choose_topic, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }


    class Vh extends RecyclerView.ViewHolder {

        ImageView mThumb;
        TextView mName;
        TextView mNum;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.thumb);
            mName = itemView.findViewById(R.id.name);
            mNum = itemView.findViewById(R.id.num);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(ActiveTopicBean bean) {
            ImgLoader.display(mContext, bean.getThumb(), mThumb);
            mName.setText(bean.getName());
            mNum.setText(bean.getNumString());
            itemView.setTag(bean);
        }
    }
}
