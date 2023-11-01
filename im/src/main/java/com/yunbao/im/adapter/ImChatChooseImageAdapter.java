package com.yunbao.im.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yunbao.common.Constants;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.ChooseImageBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.im.R;

import java.io.File;
import java.util.List;

/**
 * Created by cxf on 2018/6/20.
 * 聊天时候选择图片的Adapter
 */

public class ImChatChooseImageAdapter extends RefreshAdapter<ChooseImageBean> {

    private Drawable mCheckedDrawable;
    private Drawable mUnCheckedDrawable;
    private View.OnClickListener mOnClickListener;
    private int mCheckedPosition = -1;

    public ImChatChooseImageAdapter(Context context, List<ChooseImageBean> list) {
        super(context, list);
        mCheckedDrawable = ContextCompat.getDrawable(context, R.mipmap.icon_checked);
        mUnCheckedDrawable = ContextCompat.getDrawable(context, R.mipmap.icon_checked_none);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                if (position == mCheckedPosition) {
                    return;
                }
                ChooseImageBean bean = mList.get(position);
                bean.setChecked(true);
                notifyItemChanged(position, Constants.PAYLOAD);
                if (mCheckedPosition >= 0) {
                    mList.get(mCheckedPosition).setChecked(false);
                    notifyItemChanged(mCheckedPosition, Constants.PAYLOAD);
                }
                mCheckedPosition = position;
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_chat_choose_img, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    public File getSelectedFile() {
        if (mCheckedPosition >= 0) {
            return mList.get(mCheckedPosition).getImageFile();
        }
        return null;
    }


    class Vh extends RecyclerView.ViewHolder {

        ImageView mCover;
        ImageView mImg;

        public Vh(View itemView) {
            super(itemView);
            mCover = itemView.findViewById(R.id.cover);
            mImg = itemView.findViewById(R.id.img);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(ChooseImageBean bean, int position, Object payload) {
            if (payload == null) {
                itemView.setTag(position);
                ImgLoader.display(mContext, bean.getImageFile(), mCover);
            }
            mImg.setImageDrawable(bean.isChecked() ? mCheckedDrawable : mUnCheckedDrawable);
        }
    }

}
