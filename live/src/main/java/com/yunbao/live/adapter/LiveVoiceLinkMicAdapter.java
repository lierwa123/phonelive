package com.yunbao.live.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.live.R;
import com.yunbao.live.activity.LiveActivity;
import com.yunbao.live.bean.LiveVoiceLinkMicBean;
import com.yunbao.live.utils.LiveIconUtil;

import java.util.List;

public class LiveVoiceLinkMicAdapter extends RefreshAdapter<LiveVoiceLinkMicBean> {

    private String mNoString;
    private int mColor0;
    private int mColor1;
    private Drawable mDrawable0;
    private Drawable mDrawable1;
    private View.OnClickListener mOnClickListener;


    public LiveVoiceLinkMicAdapter(Context context, List<LiveVoiceLinkMicBean> list) {
        super(context, list);
        mNoString = WordUtil.getString(R.string.a_38);
        mColor0 = ContextCompat.getColor(context, R.color.gray3);
        mColor1 = ContextCompat.getColor(context, R.color.white);
        mDrawable0 = ContextCompat.getDrawable(context, R.mipmap.ic_live_voice_0);
        mDrawable1 = ContextCompat.getDrawable(context, R.mipmap.ic_live_voice_1);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveVoiceLinkMicBean bean = (LiveVoiceLinkMicBean) v.getTag();
                if (!bean.isEmpty()) {
                    ((LiveActivity) mContext).showUserDialog(bean.getUid());
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_live_voice_link_mic, viewGroup, false));
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

        ImageView mImgStatus;
        ImageView mAvatar;
        TextView mName;
        ImageView mImgMute;
        ImageView mFace;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mImgStatus = itemView.findViewById(R.id.img_status);
            mAvatar = itemView.findViewById(R.id.avatar);
            mName = itemView.findViewById(R.id.name);
            mImgMute = itemView.findViewById(R.id.img_mute);
            mFace = itemView.findViewById(R.id.face);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(LiveVoiceLinkMicBean bean, Object payload) {
            if (Constants.VOICE_FACE.equals(payload)) {
                if (bean.getFaceIndex() == -1) {
                    mFace.setImageDrawable(null);
                } else {
                    int res = LiveIconUtil.getVoiceRoomFaceRes(bean.getFaceIndex());
                    if (res > 0) {
                        mFace.setImageResource(res);
                    } else {
                        mFace.setImageDrawable(null);
                    }
                }
                return;
            }
            if (bean.isEmpty()) {
                if (payload == null) {
                    itemView.setTag(bean);
                    mAvatar.setImageDrawable(null);
                    mName.setText(mNoString);
                    mName.setTextColor(mColor0);
                    mFace.setImageDrawable(null);
                }
                if (bean.getStatus() == Constants.VOICE_CTRL_BAN) {
                    mImgStatus.setImageDrawable(mDrawable1);
                } else {
                    mImgStatus.setImageDrawable(mDrawable0);
                }
                if (mImgMute.getVisibility() == View.VISIBLE) {
                    mImgMute.setVisibility(View.INVISIBLE);
                }
            } else {
                if (payload == null) {
                    itemView.setTag(bean);
                    ImgLoader.displayAvatar(mContext, bean.getAvatar(), mAvatar);
                    mName.setText(bean.getUserName());
                    mName.setTextColor(mColor1);
                    mFace.setImageDrawable(null);
                }
                mImgStatus.setImageDrawable(mDrawable0);
                if (bean.getStatus() == Constants.VOICE_CTRL_CLOSE) {
                    if (mImgMute.getVisibility() != View.VISIBLE) {
                        mImgMute.setVisibility(View.VISIBLE);
                    }
                } else if (bean.getStatus() == Constants.VOICE_CTRL_OPEN) {
                    if (mImgMute.getVisibility() == View.VISIBLE) {
                        mImgMute.setVisibility(View.INVISIBLE);
                    }
                }
            }


        }
    }
}
