package com.yunbao.main.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.common.utils.FaceTextUtil;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.bean.ActiveBean;
import com.yunbao.main.bean.ActiveCommentBean;
import com.yunbao.main.http.MainHttpUtil;

import java.util.ArrayList;
import java.util.List;

public class ActiveCommentAdapter extends RefreshAdapter<ActiveCommentBean> {

    private static final int PARENT = 1;
    private static final int CHILD = 2;
    private Drawable mLikeDrawable;
    private Drawable mUnLikeDrawable;
    private int mLikeColor;
    private int mUnLikeColor;
    private ScaleAnimation mLikeAnimation;
    private View.OnClickListener mOnClickListener;
    private View.OnLongClickListener mOnLongClickListener;
    private View.OnClickListener mLikeClickListener;
    private View.OnClickListener mExpandClickListener;
    private View.OnClickListener mCollapsedClickListener;
    private View.OnClickListener mAvatarClickListener;
    private ActionListener mActionListener;
    private ImageView mCurLikeImageView;
    private int mCurLikeCommentPosition;
    private ActiveCommentBean mCurLikeCommentBean;
    private HttpCallback mLikeCommentCallback;
    private ActiveAdapter mActiveAdapter;
    private ActiveBean mActiveBean;

    public ActiveCommentAdapter(Context context, ActiveBean activeBean) {
        super(context);
        mActiveBean = activeBean;
        List<ActiveBean> list = new ArrayList<>();
        list.add(activeBean);
        mActiveAdapter = new ActiveAdapter(mContext, list);

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(((ActiveCommentBean) tag), 0);
                }
            }
        };
        mOnLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onItemLongClick((ActiveCommentBean) tag);
                }
                return true;
            }
        };
        mLikeDrawable = ContextCompat.getDrawable(context, R.mipmap.icon_active_like_5);
        mUnLikeDrawable = ContextCompat.getDrawable(context, R.mipmap.icon_active_like_0);
        mLikeColor = ContextCompat.getColor(context, R.color.red);
        mUnLikeColor = ContextCompat.getColor(context, R.color.gray3);
        mLikeAnimation = new ScaleAnimation(1f, 0.5f, 1f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mLikeAnimation.setDuration(200);
        mLikeAnimation.setRepeatCount(1);
        mLikeAnimation.setRepeatMode(Animation.REVERSE);
        mLikeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (mCurLikeCommentBean != null) {
                    if (mCurLikeImageView != null) {
                        mCurLikeImageView.setImageDrawable(mCurLikeCommentBean.getIsLike() == 1 ? mLikeDrawable : mUnLikeDrawable);
                    }
                }
            }
        });
        mLikeCommentCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0 && mCurLikeCommentBean != null) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    int like = obj.getIntValue("islike");
                    String likeNum = obj.getString("likes");
                    if (mCurLikeCommentBean != null) {
                        mCurLikeCommentBean.setIsLike(like);
                        mCurLikeCommentBean.setLikeNum(likeNum);
                        notifyItemChanged(mCurLikeCommentPosition, Constants.PAYLOAD);
                    }
                    if (mCurLikeImageView != null && mLikeAnimation != null) {
                        mCurLikeImageView.startAnimation(mLikeAnimation);
                    }
                }
            }
        };
        mLikeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                ActiveCommentBean bean = (ActiveCommentBean) tag;
                String uid = bean.getUid();
                if (!TextUtils.isEmpty(uid) && uid.equals(CommonAppConfig.getInstance().getUid())) {
                    ToastUtil.show(com.yunbao.video.R.string.video_comment_cannot_self);
                    return;
                }
                mCurLikeImageView = (ImageView) v;
                mCurLikeCommentPosition = bean.getPosition();
                mCurLikeCommentBean = bean;
                MainHttpUtil.setActiveCommentLike(bean.getId(), mLikeCommentCallback);
            }
        };
        mExpandClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtil.canClick()) {
                    return;
                }
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onExpandClicked((ActiveCommentBean) tag);
                }
            }
        };
        mCollapsedClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtil.canClick()) {
                    return;
                }
                Object tag = v.getTag();
                if (tag != null && mActionListener != null) {
                    mActionListener.onCollapsedClicked((ActiveCommentBean) tag);
                }
            }
        };
        mAvatarClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toUid = (String) v.getTag(R.id.avatar);
                RouteUtil.forwardUserHome(mContext, toUid);
            }
        };
    }


    public void setCommentNum(int commentNum) {
        if (mActiveBean != null) {
            mActiveBean.setCommentNum(commentNum);
        }
    }


    /**
     * 关注 取消关注
     */
    public void onFollowChanged(int isAttention) {
        if (mActiveAdapter != null) {
            mActiveAdapter.onFollowChanged(mActiveBean.getUid(), isAttention);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return -1;
        }
        ActiveCommentBean bean = getItem(position);
        if (bean != null && bean.isParentNode()) {
            return PARENT;
        }
        return CHILD;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == -1) {
            HeadVh headVh = new HeadVh(mInflater.inflate(R.layout.item_active_comment_head, parent, false));
            headVh.setIsRecyclable(false);
            return headVh;
        }
        if (viewType == PARENT) {
            return new ParentVh(mInflater.inflate(R.layout.item_active_comment_parent, parent, false));
        }
        return new ChildVh(mInflater.inflate(R.layout.item_active_comment_child, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        if (vh instanceof Vh) {
            ActiveCommentBean bean = getItem(position);
            if (bean != null) {
                bean.setPosition(position);
                Object payload = payloads.size() > 0 ? payloads.get(0) : null;
                ((Vh) vh).setData(bean, payload);
            }

        } else {
            ((HeadVh) vh).setData();
        }
    }

    @Override
    public int getItemCount() {
        int count = 1;
        for (int i = 0, size = mList.size(); i < size; i++) {
            count++;
            List<ActiveCommentBean> childList = mList.get(i).getChildList();
            if (childList != null) {
                count += childList.size();
            }
        }
        return count;
    }

    private ActiveCommentBean getItem(int position) {
        int index = 1;
        for (int i = 0, size = mList.size(); i < size; i++) {
            ActiveCommentBean parentNode = mList.get(i);
            if (index == position) {
                return parentNode;
            }
            index++;
            List<ActiveCommentBean> childList = mList.get(i).getChildList();
            if (childList != null) {
                for (int j = 0, childSize = childList.size(); j < childSize; j++) {
                    if (position == index) {
                        return childList.get(j);
                    }
                    index++;
                }
            }
        }
        return null;
    }


    class HeadVh extends RecyclerView.ViewHolder {

        RecyclerView mRecyclerView;
        View mNoData;

        public HeadVh(@NonNull View itemView) {
            super(itemView);
            mRecyclerView = itemView.findViewById(R.id.recyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }

                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            mNoData = itemView.findViewById(R.id.no_data);

        }

        void setData() {
            if (mRecyclerView.getAdapter() == null) {
                mRecyclerView.setAdapter(mActiveAdapter);
            } else {
                mActiveAdapter.notifyDataSetChanged();
            }
            if (mList != null && mList.size() > 0) {
                if (mNoData.getVisibility() != View.GONE) {
                    mNoData.setVisibility(View.GONE);
                }
            } else {
                if (mNoData.getVisibility() != View.VISIBLE) {
                    mNoData.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mName;
        TextView mContent;
        ImageView mAvatar;

        public Vh(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(com.yunbao.video.R.id.name);
            mContent = (TextView) itemView.findViewById(com.yunbao.video.R.id.content);
            mAvatar = itemView.findViewById(R.id.avatar);
            if (mAvatar != null) {
                mAvatar.setOnClickListener(mAvatarClickListener);
            }
            itemView.setOnClickListener(mOnClickListener);
            itemView.setOnLongClickListener(mOnLongClickListener);
//            mName.setOnClickListener(mAvatarClickListener);
        }

        void setData(ActiveCommentBean bean, Object payload) {
            itemView.setTag(bean);
            if (payload == null) {
                UserBean u = bean.getUserBean();
                if (u != null) {
                    if (mAvatar != null) {
                        ImgLoader.displayAvatar(mContext, u.getAvatar(), mAvatar);
                        mAvatar.setTag(R.id.avatar, u.getId());
                    }
                    mName.setText(u.getUserNiceName());
//                    mName.setTag(R.id.avatar, u.getId());
                }
                mContent.setText(FaceTextUtil.renderVideoComment(bean.getContent(), "  " + bean.getDatetime()));
            }
        }
    }


    class ParentVh extends Vh {

        ImageView mBtnLike;
        TextView mLikeNum;
        ImageView mAvatar;


        public ParentVh(View itemView) {
            super(itemView);
            mBtnLike = (ImageView) itemView.findViewById(com.yunbao.video.R.id.btn_like);
            mLikeNum = (TextView) itemView.findViewById(com.yunbao.video.R.id.like_num);
            mBtnLike.setOnClickListener(mLikeClickListener);
            mAvatar = itemView.findViewById(com.yunbao.video.R.id.avatar);
        }

        void setData(ActiveCommentBean bean, Object payload) {
            super.setData(bean, payload);
            if (payload == null) {
                UserBean u = bean.getUserBean();
                if (u != null) {
                    ImgLoader.displayAvatar(mContext, u.getAvatar(), mAvatar);
                }
            }
            mBtnLike.setTag(bean);
            boolean like = bean.getIsLike() == 1;
            mBtnLike.setImageDrawable(like ? mLikeDrawable : mUnLikeDrawable);
            mLikeNum.setText(bean.getLikeNum());
            mLikeNum.setTextColor(like ? mLikeColor : mUnLikeColor);
        }
    }

    class ChildVh extends Vh {

        View mBtnGroup;
        View mBtnExpand;//展开按钮
        View mBtnbCollapsed;//收起按钮

        public ChildVh(View itemView) {
            super(itemView);
            mBtnGroup = itemView.findViewById(com.yunbao.video.R.id.btn_group);
            mBtnExpand = itemView.findViewById(com.yunbao.video.R.id.btn_expand);
            mBtnbCollapsed = itemView.findViewById(com.yunbao.video.R.id.btn_collapsed);
            mBtnExpand.setOnClickListener(mExpandClickListener);
            mBtnbCollapsed.setOnClickListener(mCollapsedClickListener);
        }

        void setData(ActiveCommentBean bean, Object payload) {
            super.setData(bean, payload);
            mBtnExpand.setTag(bean);
            mBtnbCollapsed.setTag(bean);
            ActiveCommentBean parentNodeBean = bean.getParentNodeBean();
            if (bean.needShowExpand(parentNodeBean)) {
                if (mBtnGroup.getVisibility() != View.VISIBLE) {
                    mBtnGroup.setVisibility(View.VISIBLE);
                }
                if (mBtnbCollapsed.getVisibility() == View.VISIBLE) {
                    mBtnbCollapsed.setVisibility(View.INVISIBLE);
                }
                if (mBtnExpand.getVisibility() != View.VISIBLE) {
                    mBtnExpand.setVisibility(View.VISIBLE);
                }
            } else if (bean.needShowCollapsed(parentNodeBean)) {
                if (mBtnGroup.getVisibility() != View.VISIBLE) {
                    mBtnGroup.setVisibility(View.VISIBLE);
                }
                if (mBtnExpand.getVisibility() == View.VISIBLE) {
                    mBtnExpand.setVisibility(View.INVISIBLE);
                }
                if (mBtnbCollapsed.getVisibility() != View.VISIBLE) {
                    mBtnbCollapsed.setVisibility(View.VISIBLE);
                }
            } else {
                if (mBtnGroup.getVisibility() == View.VISIBLE) {
                    mBtnGroup.setVisibility(View.GONE);
                }
            }
        }
    }


    public void release() {
        if (mActiveAdapter != null) {
            mActiveAdapter.release();
        }
    }


    public interface ActionListener {
        void onExpandClicked(ActiveCommentBean commentBean);

        void onCollapsedClicked(ActiveCommentBean commentBean);

        void onItemLongClick(ActiveCommentBean bean);
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

}
