package com.yunbao.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;
import com.yunbao.video.bean.VideoBean;

/**
 * Created by cxf on 2018/12/14.
 */

public class VideoHomeAdapter extends RefreshAdapter<VideoBean> {

    private View.OnClickListener mOnClickListener;
    private String mStatusShenHe;
    private String mStatusJuJue;

    public VideoHomeAdapter(Context context) {
        super(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                int position = (int) tag;
                VideoBean bean = mList.get(position);
                if (bean != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(bean, position);
                }
            }
        };
        mStatusShenHe = WordUtil.getString(R.string.mall_117);
        mStatusJuJue = WordUtil.getString(R.string.mall_342);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_video_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position), position);
    }

    /**
     * 删除视频
     */
    public void deleteVideo(String videoId) {
        if (TextUtils.isEmpty(videoId)) {
            return;
        }
//        for (int i = 0, size = mList.size(); i < size; i++) {
//            if (videoId.equals(mList.get(i).getId())) {
//                notifyItemRemoved(i);
//                break;
//            }
//        }
        notifyDataSetChanged();
    }

    class Vh extends RecyclerView.ViewHolder {
        ImageView mImg;
        TextView mNum;
        TextView mStatus;

        public Vh(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.img);
            mNum = (TextView) itemView.findViewById(R.id.num);
            mStatus = itemView.findViewById(R.id.status);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(VideoBean bean, int position) {
            itemView.setTag(position);
            ImgLoader.display(mContext, bean.getThumb(), mImg);
            mNum.setText(bean.getViewNum());
            int status = bean.getStatus();//视频状态 0审核中 1通过 2拒绝
            if (status == 1) {
                if (mStatus.getVisibility() == View.VISIBLE) {
                    mStatus.setVisibility(View.INVISIBLE);
                }
            } else {
                if (mStatus.getVisibility() != View.VISIBLE) {
                    mStatus.setVisibility(View.VISIBLE);
                }
                if (status == 0) {
                    mStatus.setText(mStatusShenHe);
                } else if (status == 2) {
                    mStatus.setText(mStatusJuJue);
                }
            }
        }

    }
}
