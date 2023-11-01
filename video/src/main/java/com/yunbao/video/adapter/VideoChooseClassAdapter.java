package com.yunbao.video.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.VideoClassBean;
import com.yunbao.video.R;

import java.util.List;

public class VideoChooseClassAdapter extends RefreshAdapter<VideoClassBean> {

    private Drawable mCheckedDrawable;
    private View.OnClickListener mOnClickListener;

    public VideoChooseClassAdapter(Context context, int checkedId) {
        super(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick((VideoClassBean) tag, 0);
                }
            }
        };
        mCheckedDrawable = ContextCompat.getDrawable(context, R.mipmap.icon_check_1);
        List<VideoClassBean> list = JSON.parseArray(CommonAppConfig.getInstance().getConfig().getVideoClass(), VideoClassBean.class);
        mList.addAll(list);
        for (VideoClassBean bean : mList) {
            if (checkedId == bean.getId()) {
                bean.setChecked(true);
                break;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_video_choose_class, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {

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

        void setData(VideoClassBean bean, Object payload) {
            if (payload == null) {
                itemView.setTag(bean);
                mName.setText(bean.getName());
            }
            mImg.setImageDrawable(bean.isChecked() ? mCheckedDrawable : null);
        }
    }
}
