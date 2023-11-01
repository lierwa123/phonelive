package com.yunbao.live.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.LevelBean;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.CommonIconUtil;
import com.yunbao.live.R;

import java.util.List;

public class LiveVoiceApplyUpAdapter extends RefreshAdapter<UserBean> {

    private boolean mIsAnchor;
    private View.OnClickListener mAgreeClickListener;
    private View.OnClickListener mRefuseClickListener;
    private ActionListener mActionListener;

    public LiveVoiceApplyUpAdapter(Context context, List<UserBean> list, boolean isAnchor) {
        super(context, list);
        mIsAnchor = isAnchor;
        if (isAnchor) {
            mAgreeClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActionListener != null) {
                        mActionListener.onAgreeUpMicClick((UserBean) v.getTag(), 1);
                    }
                }
            };
            mRefuseClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActionListener != null) {
                        mActionListener.onAgreeUpMicClick((UserBean) v.getTag(), 0);
                    }
                }
            };
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mIsAnchor) {
            return new VhAnchor(mInflater.inflate(R.layout.item_live_voice_apply_up_anchor, viewGroup, false));
        } else {
            return new Vh(mInflater.inflate(R.layout.item_live_voice_apply_up, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        if (mIsAnchor) {
            ((VhAnchor) vh).setData(mList.get(i), i);
        } else {
            ((Vh) vh).setData(mList.get(i), i);
        }

    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mIndex;
        ImageView mAvatar;
        TextView mName;
        ImageView mSex;
        ImageView mLevel;


        public Vh(@NonNull View itemView) {
            super(itemView);
            mIndex = itemView.findViewById(R.id.index);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mName = (TextView) itemView.findViewById(R.id.name);
            mSex = (ImageView) itemView.findViewById(R.id.sex);
            mLevel = (ImageView) itemView.findViewById(R.id.level);
        }

        void setData(UserBean bean, int position) {
            mIndex.setText(String.valueOf(position + 1));
            ImgLoader.displayAvatar(mContext, bean.getAvatar(), mAvatar);
            mName.setText(bean.getUserNiceName());
            mSex.setImageResource(CommonIconUtil.getSexIcon(bean.getSex()));
            LevelBean levelBean = CommonAppConfig.getInstance().getLevel(bean.getLevel());
            if (levelBean != null) {
                ImgLoader.display(mContext, levelBean.getThumb(), mLevel);
            }
        }
    }


    class VhAnchor extends RecyclerView.ViewHolder {

        TextView mIndex;
        ImageView mAvatar;
        TextView mName;
        ImageView mSex;
        ImageView mLevel;
        View mBtnAgree;
        View mBtnRefuse;


        public VhAnchor(@NonNull View itemView) {
            super(itemView);
            mIndex = itemView.findViewById(R.id.index);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mName = (TextView) itemView.findViewById(R.id.name);
            mSex = (ImageView) itemView.findViewById(R.id.sex);
            mLevel = (ImageView) itemView.findViewById(R.id.level);
            mBtnAgree = itemView.findViewById(R.id.btn_agree);
            mBtnRefuse = itemView.findViewById(R.id.btn_refuse);
            mBtnAgree.setOnClickListener(mAgreeClickListener);
            mBtnRefuse.setOnClickListener(mRefuseClickListener);
        }

        void setData(UserBean bean, int position) {
            mBtnAgree.setTag(bean);
            mBtnRefuse.setTag(bean);
            mIndex.setText(String.valueOf(position + 1));
            ImgLoader.displayAvatar(mContext, bean.getAvatar(), mAvatar);
            mName.setText(bean.getUserNiceName());
            mSex.setImageResource(CommonIconUtil.getSexIcon(bean.getSex()));
            LevelBean levelBean = CommonAppConfig.getInstance().getLevel(bean.getLevel());
            if (levelBean != null) {
                ImgLoader.display(mContext, levelBean.getThumb(), mLevel);
            }
        }
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface ActionListener {
        void onAgreeUpMicClick(UserBean userBean, int isAgree);
    }

}
