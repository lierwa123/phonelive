package com.yunbao.live.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.LevelBean;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.CommonIconUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.live.R;
import com.yunbao.live.bean.LiveBean;
import com.yunbao.live.bean.LiveVoiceControlBean;

import java.util.List;

public class LiveVoiceControlAdapter extends RefreshAdapter<LiveVoiceControlBean> {

    private View.OnClickListener mControlClickListener;
    private View.OnClickListener mDownMicClickListener;
    private ActionListener mActionListener;
    private Drawable mDrawable0;
    private Drawable mDrawable1;
    private int mColor0;
    private int mColor1;
    private Drawable mNoAvatar;

    public LiveVoiceControlAdapter(Context context, List<LiveVoiceControlBean> list) {
        super(context, list);
        mDrawable0 = ContextCompat.getDrawable(context, R.drawable.bg_btn_live_voice_2);
        mDrawable1 = ContextCompat.getDrawable(context, R.drawable.bg_btn_live_voice_3);
        mNoAvatar = ContextCompat.getDrawable(context, R.mipmap.icon_avatar_none);
        mColor0 = ContextCompat.getColor(context, R.color.blue2);
        mColor1 = ContextCompat.getColor(context, R.color.white);
        mControlClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionListener != null) {
                    mActionListener.onControlClick((LiveVoiceControlBean) v.getTag());
                }
            }
        };
        mDownMicClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionListener != null) {
                    mActionListener.onDownMicClick((LiveVoiceControlBean) v.getTag());
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_live_voice_control, viewGroup, false));
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

        TextView mIndex;
        ImageView mAvatar;
        TextView mName;
        ImageView mSex;
        ImageView mLevel;
        TextView mBtnControl;
        View mBtnDownMic;


        public Vh(@NonNull View itemView) {
            super(itemView);
            mIndex = itemView.findViewById(R.id.index);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mName = (TextView) itemView.findViewById(R.id.name);
            mSex = (ImageView) itemView.findViewById(R.id.sex);
            mLevel = (ImageView) itemView.findViewById(R.id.level);
            mBtnControl = itemView.findViewById(R.id.btn_control);
            mBtnDownMic = itemView.findViewById(R.id.btn_down_mic);
            mBtnControl.setOnClickListener(mControlClickListener);
            mBtnDownMic.setOnClickListener(mDownMicClickListener);
        }

        void setData(LiveVoiceControlBean bean, int position, Object payload) {
            mBtnControl.setTag(bean);
            mBtnDownMic.setTag(bean);
            int status = bean.getStatus();
            mIndex.setText(String.valueOf(position + 1));
            if (status == Constants.VOICE_CTRL_EMPTY || status == Constants.VOICE_CTRL_BAN) {//0 无人 2 禁麦；
                if (payload == null) {
                    mAvatar.setImageDrawable(mNoAvatar);
                    mName.setText(null);
                    mSex.setImageDrawable(null);
                    mLevel.setImageDrawable(null);
                }
                if (mBtnDownMic.getVisibility() == View.VISIBLE) {
                    mBtnDownMic.setVisibility(View.INVISIBLE);
                }
                if (status == Constants.VOICE_CTRL_BAN) {//禁麦
                    mBtnControl.setBackground(mDrawable1);
                    mBtnControl.setTextColor(mColor1);
                    mBtnControl.setText(R.string.a_052);
                } else {//0 无人
                    mBtnControl.setBackground(mDrawable0);
                    mBtnControl.setTextColor(mColor0);
                    mBtnControl.setText(R.string.a_051);
                }
            } else {//-1 关麦；1开麦 ；
                if (payload == null) {
                    ImgLoader.displayAvatar(mContext, bean.getAvatar(), mAvatar);
                    mName.setText(bean.getUserName());
                    mSex.setImageResource(CommonIconUtil.getSexIcon(bean.getSex()));
                    LevelBean levelBean = CommonAppConfig.getInstance().getLevel(bean.getLevel());
                    if (levelBean != null) {
                        ImgLoader.display(mContext, levelBean.getThumb(), mLevel);
                    }
                }
                if (mBtnDownMic.getVisibility() != View.VISIBLE) {
                    mBtnDownMic.setVisibility(View.VISIBLE);
                }
                if (status == Constants.VOICE_CTRL_CLOSE) {//关麦
                    mBtnControl.setBackground(mDrawable0);
                    mBtnControl.setTextColor(mColor0);
                    mBtnControl.setText(R.string.a_053);
                } else {//1 开麦
                    mBtnControl.setBackground(mDrawable1);
                    mBtnControl.setTextColor(mColor1);
                    mBtnControl.setText(R.string.a_050);
                }
            }

        }
    }

    public void changeStatus(int position, int status) {
        if (position >= 0 && position < mList.size()) {
            LiveVoiceControlBean bean = mList.get(position);
            if (bean != null) {
                bean.setStatus(status);
                if (status == Constants.VOICE_CTRL_EMPTY) {
                    notifyItemChanged(position);
                } else {
                    notifyItemChanged(position, Constants.PAYLOAD);
                }
            }
        }
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface ActionListener {
        void onControlClick(LiveVoiceControlBean bean);

        void onDownMicClick(LiveVoiceControlBean bean);
    }

}
