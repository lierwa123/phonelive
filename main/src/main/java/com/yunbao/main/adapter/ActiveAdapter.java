package com.yunbao.main.adapter;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.custom.ActiveVoiceLayout;
import com.yunbao.common.custom.NineGridLayout;
import com.yunbao.common.dialog.ImagePreviewDialog;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.PermissionCallback;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.common.utils.CommonIconUtil;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.DownloadUtil;
import com.yunbao.common.utils.MD5Util;
import com.yunbao.common.utils.PermissionUtil;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.im.utils.VoiceMediaPlayerUtil;
import com.yunbao.main.R;
import com.yunbao.main.activity.ActiveDetailActivity;
import com.yunbao.main.activity.ActiveReportActivity;
import com.yunbao.main.activity.ActiveTopicActivity;
import com.yunbao.main.bean.ActiveBean;
import com.yunbao.main.bean.ActiveUserBean;
import com.yunbao.main.custom.ActiveLikeImage;
import com.yunbao.main.event.ActiveDeleteEvent;
import com.yunbao.main.event.ActiveLikeEvent;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

public class ActiveAdapter extends RefreshAdapter<ActiveBean> {

    private View.OnClickListener mItemClickListener;
    private View.OnClickListener mAvatarClickListener;
    private View.OnClickListener mFollowClickListener;
    private View.OnClickListener mLikeClickListener;
    private View.OnClickListener mMoreClickListener;
    private NineGridLayout.ActionListener mNineGridListener;
    private ActiveVoiceLayout.ActionListener mVoiceListener;
    private View.OnClickListener mVideoClickListener;
    private Drawable[] mLikeDrawables;
    private String mFollowString;
    private String mUnFollowString;
    private String mStatusString0;
    private String mStatusString2;
    private int mStatusColor0;
    private int mStatusColor2;
    private ActiveVoiceLayout mNowPlayVoiceLayout;//当前正在播放的语音控件
    private VoiceMediaPlayerUtil mPlayerUtil;
    private DownloadUtil mDownloadUtil;
    private Drawable mFollowDrawable;
    private Drawable mUnFollowDrawable;
    private int mFollowColor;
    private int mUnFollowColor;

    public ActiveAdapter(Context context) {
        super(context);
        init(context);
    }

    public ActiveAdapter(Context context, List<ActiveBean> list) {
        super(context, list);
        init(context);
    }

    private void init(Context context) {
        mLikeDrawables = new Drawable[6];
        mLikeDrawables[0] = ContextCompat.getDrawable(context, R.mipmap.icon_active_like_0);
        mLikeDrawables[1] = ContextCompat.getDrawable(context, R.mipmap.icon_active_like_1);
        mLikeDrawables[2] = ContextCompat.getDrawable(context, R.mipmap.icon_active_like_2);
        mLikeDrawables[3] = ContextCompat.getDrawable(context, R.mipmap.icon_active_like_3);
        mLikeDrawables[4] = ContextCompat.getDrawable(context, R.mipmap.icon_active_like_4);
        mLikeDrawables[5] = ContextCompat.getDrawable(context, R.mipmap.icon_active_like_5);
        mFollowDrawable = ContextCompat.getDrawable(context, R.drawable.btn_active_follow_1);
        mUnFollowDrawable = ContextCompat.getDrawable(context, R.drawable.btn_active_follow_0);
        mFollowColor = ContextCompat.getColor(context, R.color.gray5);
        mUnFollowColor = ContextCompat.getColor(context, R.color.global);
        mFollowString = WordUtil.getString(R.string.following);
        mUnFollowString = WordUtil.getString(R.string.follow);
        mStatusString0 = WordUtil.getString(R.string.active_status_0);
        mStatusString2 = WordUtil.getString(R.string.active_status_2);
        mStatusColor0 = ContextCompat.getColor(mContext, R.color.gray1);
        mStatusColor2 = ContextCompat.getColor(mContext, R.color.global);
        mItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtil.canClick()) {
                    return;
                }
                Object tag = v.getTag();
                if (tag != null) {
                    if (!(mContext instanceof ActiveDetailActivity)) {
                        ActiveDetailActivity.forward(mContext, (ActiveBean) tag);
                    }
                }
            }
        };
        mAvatarClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtil.canClick()) {
                    return;
                }
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                RouteUtil.forwardUserHome(mContext, ((ActiveBean) tag).getUid());
            }
        };
        mFollowClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtil.canClick()) {
                    return;
                }
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                ActiveUserBean u = ((ActiveBean) tag).getUserBean();
                if (u != null) {
                    CommonHttpUtil.setAttention(u.getId(), null);
                }
            }
        };
        mLikeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtil.canClick()) {
                    return;
                }
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                final Vh vh = (Vh) tag;
                ActiveBean activeBean = vh.mBean;
                if (activeBean == null) {
                    return;
                }
                final String activeId = activeBean.getId();
                MainHttpUtil.activeAddLike(activeId, new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0) {
                            if (info.length > 0) {
                                JSONObject obj = JSON.parseObject(info[0]);
                                int isLike = obj.getIntValue("islike");
                                int likeNum = obj.getIntValue("likes");
                                vh.changeLike(isLike, likeNum);
                                EventBus.getDefault().post(new ActiveLikeEvent(ActiveAdapter.this.hashCode(), activeId, isLike, likeNum));
                            }
                        } else {
                            ToastUtil.show(msg);
                        }
                    }
                });

            }
        };
        mMoreClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtil.canClick()) {
                    return;
                }
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                final ActiveBean bean = (ActiveBean) tag;
                if (bean.isSelf()) {
                    DialogUitl.showStringArrayDialog(mContext, new Integer[]{R.string.delete}, new DialogUitl.StringArrayDialogCallback() {
                        @Override
                        public void onItemClick(String text, int tag) {
                            MainHttpUtil.activeDelete(bean.getId(), new HttpCallback() {
                                @Override
                                public void onSuccess(int code, String msg, String[] info) {
                                    if (code == 0) {
                                        EventBus.getDefault().post(new ActiveDeleteEvent(bean.getId()));
                                    }
                                    ToastUtil.show(msg);
                                }
                            });
                        }
                    });
                } else {
                    DialogUitl.showStringArrayDialog(mContext, new Integer[]{R.string.report}, new DialogUitl.StringArrayDialogCallback() {
                        @Override
                        public void onItemClick(String text, int tag) {
                            ActiveReportActivity.forward(mContext, bean.getId());
                        }
                    });
                }
            }
        };
        mNineGridListener = new NineGridLayout.ActionListener() {
            @Override
            public void onItemClick(final List<?> dataList, int position) {
                ImagePreviewDialog dialog = new ImagePreviewDialog();
                dialog.setImageInfo(dataList.size(), position, false, new ImagePreviewDialog.ActionListener() {
                    @Override
                    public void loadImage(ImageView imageView, int position) {
                        ImgLoader.display(mContext, (String) (dataList.get(position)), imageView);
                    }

                    @Override
                    public void onDeleteClick(int position) {

                    }

                    @Override
                    public String getImageUrl(int position) {
                        return (String) (dataList.get(position));
                    }
                });
                dialog.show(((AbsActivity) mContext).getSupportFragmentManager(), "ImagePreviewDialog");
            }

            @Override
            public void displayImage(Object path, ImageView imageView) {
                ImgLoader.display(mContext, (String) path, imageView);
            }
        };
        mVoiceListener = new ActiveVoiceLayout.ActionListener() {

            @Override
            public void onPlayStart(ActiveVoiceLayout voiceLayout, File file) {
                if (mNowPlayVoiceLayout != null && mNowPlayVoiceLayout != voiceLayout) {
                    mNowPlayVoiceLayout.stopPlay();
                }
                mNowPlayVoiceLayout = voiceLayout;
                playVoiceFile(file);
            }

            @Override
            public void onPlayStop() {
                mNowPlayVoiceLayout = null;
                stopPlayVoiceFile();
            }

            @Override
            public void onNeedDownload(final ActiveVoiceLayout voiceLayout, final String voiceUrl) {
                if (mNowPlayVoiceLayout != null) {
                    mNowPlayVoiceLayout.stopPlay();
                }
                mNowPlayVoiceLayout = null;
                final File dir = new File(CommonAppConfig.MUSIC_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                final String fileName = MD5Util.getMD5(voiceUrl);
                File voiceFile = new File(dir, fileName);
                if (voiceFile.exists()) {
                    voiceLayout.starPlay();
                } else {
                    PermissionUtil.request((AbsActivity) mContext,
                            new PermissionCallback() {
                                @Override
                                public void onAllGranted() {
                                    if (mDownloadUtil == null) {
                                        mDownloadUtil = new DownloadUtil();
                                    }
                                    mDownloadUtil.download(Constants.DOWNLOAD_MUSIC, dir, fileName, voiceUrl, new DownloadUtil.Callback() {
                                        @Override
                                        public void onSuccess(File file) {
                                            voiceLayout.starPlay();
                                        }

                                        @Override
                                        public void onProgress(int progress) {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                        }
                                    });
                                }
                            },
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }

            @Override
            public File getLocalFile() {
                return null;
            }
        };

        mVideoClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                ActiveBean bean = (ActiveBean) tag;
                RouteUtil.forwardVideoPlay(mContext, bean.getVideoUrl(), bean.getVideoImage());
            }
        };
    }


    /**
     * 播放语音
     */
    private void playVoiceFile(File file) {
        if (file == null) {
            return;
        }
        if (mPlayerUtil == null) {
            mPlayerUtil = new VoiceMediaPlayerUtil(mContext);
            mPlayerUtil.setActionListener(new VoiceMediaPlayerUtil.ActionListener() {
                @Override
                public void onPlayEnd() {
                    if (mNowPlayVoiceLayout != null) {
                        mNowPlayVoiceLayout.stopPlay();
                    }
                }
            });
        }
        mPlayerUtil.startPlay(file.getAbsolutePath());
    }

    /**
     * 停止播放语音
     */
    private void stopPlayVoiceFile() {
        if (mPlayerUtil != null) {
            mPlayerUtil.stopPlay();
        }
    }

    /**
     * 关注 取消关注
     */
    public void onFollowChanged(String toUid, int isAttention) {
        if (TextUtils.isEmpty(toUid)) {
            return;
        }
        boolean hasChangged = false;
        for (ActiveBean bean : mList) {
            if (toUid.equals(bean.getUid())) {
                ActiveUserBean u = bean.getUserBean();
                if (u != null) {
                    u.setIsAttention(isAttention);
                }
                hasChangged = true;
            }
        }
        if (hasChangged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 删除动态
     */
    public void onActiveDeleted(String activeId) {
        if (TextUtils.isEmpty(activeId)) {
            return;
        }
        for (ActiveBean bean : mList) {
            if (activeId.equals(bean.getId())) {
                mList.remove(bean);
                notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 评论数发生变化
     */
    public void onCommentNumChanged(String activeId, int commentNum) {
        if (TextUtils.isEmpty(activeId)) {
            return;
        }
        for (int i = 0, size = mList.size(); i < size; i++) {
            ActiveBean activeBean = mList.get(i);
            if (activeId.equals(activeBean.getId())) {
                activeBean.setCommentNum(commentNum);
                notifyItemChanged(i, Constants.PAYLOAD);
                break;
            }
        }
    }


    /**
     * 评论数发生变化
     */
    public void onLikeChanged(int from, String activeId, int likeNum, int isLike) {
        if (from == this.hashCode() || TextUtils.isEmpty(activeId)) {
            return;
        }
        for (int i = 0, size = mList.size(); i < size; i++) {
            ActiveBean activeBean = mList.get(i);
            if (activeId.equals(activeBean.getId())) {
                activeBean.setLikeNum(likeNum);
                activeBean.setIsLike(isLike);
                notifyItemChanged(i, Constants.PAYLOAD);
                break;
            }
        }
    }


    @Override
    public void refreshData(List<ActiveBean> list) {
        if (mNowPlayVoiceLayout != null) {
            mNowPlayVoiceLayout.stopPlay();
        }
        super.refreshData(list);
    }

    @Override
    public void clearData() {
        if (mNowPlayVoiceLayout != null) {
            mNowPlayVoiceLayout.stopPlay();
        }
        super.clearData();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getActiveType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemViewType) {
        if (itemViewType == Constants.ACTIVE_TYPE_IMAGE) {
            return new ImageVh(mInflater.inflate(R.layout.item_active_vh_image, viewGroup, false));
        } else if (itemViewType == Constants.ACTIVE_TYPE_VOICE) {
            return new VoiceVh(mInflater.inflate(R.layout.item_active_vh_voice, viewGroup, false));
        } else if (itemViewType == Constants.ACTIVE_TYPE_VIDEO) {
            return new VideoVh(mInflater.inflate(R.layout.item_active_vh_video, viewGroup, false));
        }
        return new Vh(mInflater.inflate(R.layout.item_active_vh_text, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mAvatar;
        View mBtnAvatar;
        TextView mName;
        ImageView mSex;
        TextView mBtnFollow;
        TextView mStatus;
        TextView mText;
        View mAddressGroup;
        TextView mAddress;
        TextView mCity;
        View mLine;
        TextView mCommentNum;
        View mBtnLike;
        TextView mLikeNum;
        ActiveLikeImage mLikeIcon;
        View mBtnMore;
        ActiveBean mBean;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.avatar);
            mBtnAvatar = itemView.findViewById(R.id.btn_avatar);
            mName = itemView.findViewById(R.id.name);
            mSex = itemView.findViewById(R.id.sex);
            mBtnFollow = itemView.findViewById(R.id.btn_follow);
            mStatus = itemView.findViewById(R.id.status);
            mText = itemView.findViewById(R.id.text);
            mText.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
            mText.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
            mAddressGroup = itemView.findViewById(R.id.address_group);
            mAddress = itemView.findViewById(R.id.address);
            mCity = itemView.findViewById(R.id.city);
            mLine = itemView.findViewById(R.id.line);
            mCommentNum = itemView.findViewById(R.id.comment_num);
            mBtnLike = itemView.findViewById(R.id.btn_like);
            mLikeNum = itemView.findViewById(R.id.like_num);
            mLikeIcon = itemView.findViewById(R.id.like_icon);
            mBtnMore = itemView.findViewById(R.id.btn_more);
            itemView.setOnClickListener(mItemClickListener);
            mBtnAvatar.setOnClickListener(mAvatarClickListener);
            mBtnFollow.setOnClickListener(mFollowClickListener);
            mBtnLike.setOnClickListener(mLikeClickListener);
            mBtnMore.setOnClickListener(mMoreClickListener);
            mLikeIcon.setDrawables(mLikeDrawables);
        }

        void setData(ActiveBean bean, int position, Object payload) {
            if (payload == null) {
                mBean = bean;
                mBtnAvatar.setTag(bean);
                itemView.setTag(bean);
                mBtnLike.setTag(this);
                mBtnFollow.setTag(bean);
                mBtnMore.setTag(bean);
                UserBean u = bean.getUserBean();
                if (u != null) {
                    ImgLoader.display(mContext, u.getAvatar(), mAvatar);
                    mName.setText(u.getUserNiceName());
                    mSex.setImageResource(CommonIconUtil.getSexIcon(u.getSex()));
                }
                if (TextUtils.isEmpty(bean.getText()) && TextUtils.isEmpty(bean.getTopicName())) {
                    if (mText.getVisibility() != View.GONE) {
                        mText.setVisibility(View.GONE);
                    }
                } else {
                    if (mText.getVisibility() != View.VISIBLE) {
                        mText.setVisibility(View.VISIBLE);
                    }
                    if (TextUtils.isEmpty(bean.getTopicName())) {
                        mText.setText(bean.getText());
                    } else {
                        final String topicId = bean.getTopicId();
                        final String topicName = bean.getTopicName();
                        String content = StringUtil.contact(topicName, " ", bean.getText());
                        SpannableString spannableString = new SpannableString(content);
                        ClickableSpan clickableSpan = new ClickableSpan() {

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setColor(mUnFollowColor);
                                ds.setUnderlineText(false);
                            }

                            @Override
                            public void onClick(View widget) {
                                ActiveTopicActivity.forward(mContext, topicId, topicName);
                            }
                        };
                        spannableString.setSpan(clickableSpan, 0, topicName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mText.setText(spannableString);
                    }
                }
                if (TextUtils.isEmpty(bean.getAddress())) {
                    if (mAddressGroup.getVisibility() != View.GONE) {
                        mAddressGroup.setVisibility(View.GONE);
                    }
                } else {
                    if (mAddressGroup.getVisibility() != View.VISIBLE) {
                        mAddressGroup.setVisibility(View.VISIBLE);
                    }
                    mAddress.setText(bean.getAddress());
                }
                mCity.setText(StringUtil.contact(bean.getCity(), " | ", bean.getDateTime()));
                if (bean.isSelf()) {
                    if (mBtnFollow.getVisibility() == View.VISIBLE) {
                        mBtnFollow.setVisibility(View.INVISIBLE);
                    }
                    if (bean.getStatus() == 1) {
                        if (mStatus.getVisibility() == View.VISIBLE) {
                            mStatus.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if (mStatus.getVisibility() != View.VISIBLE) {
                            mStatus.setVisibility(View.VISIBLE);
                        }
                        if (bean.getStatus() == 0) {
                            mStatus.setText(mStatusString0);
                            mStatus.setTextColor(mStatusColor0);
                        } else {
                            mStatus.setText(mStatusString2);
                            mStatus.setTextColor(mStatusColor2);
                        }
                    }
                } else {
                    if (mBtnFollow.getVisibility() != View.VISIBLE) {
                        mBtnFollow.setVisibility(View.VISIBLE);
                    }
                }
            }
            mCommentNum.setText(bean.getCommentNumString());
            mLikeIcon.setLike(bean.isLike(), false);
            mLikeNum.setText(bean.getLikeNumString());
            if (mBtnFollow.getVisibility() == View.VISIBLE) {
                if (bean.isFollow()) {
                    mBtnFollow.setText(mFollowString);
                    mBtnFollow.setTextColor(mFollowColor);
                    mBtnFollow.setBackground(mFollowDrawable);
                } else {
                    mBtnFollow.setText(mUnFollowString);
                    mBtnFollow.setTextColor(mUnFollowColor);
                    mBtnFollow.setBackground(mUnFollowDrawable);
                }

            }
            if (position == 0) {
                if (mLine.getVisibility() != View.GONE) {
                    mLine.setVisibility(View.GONE);
                }
            } else {
                if (mLine.getVisibility() != View.VISIBLE) {
                    mLine.setVisibility(View.VISIBLE);
                }
            }
        }


        void changeLike(int isLike, int likeNum) {
            if (mBean != null) {
                mBean.setIsLike(isLike);
                mBean.setLikeNum(likeNum);
                if (mLikeNum != null) {
                    mLikeNum.setText(mBean.getLikeNumString());
                }
                if (mLikeIcon != null) {
                    mLikeIcon.setLike(isLike == 1, true);
                }
            }
        }

    }


    class ImageVh extends Vh {

        NineGridLayout mNineGridLayout;

        public ImageVh(@NonNull View itemView) {
            super(itemView);
            mNineGridLayout = itemView.findViewById(R.id.nine_grid_layout);
            mNineGridLayout.setActionListener(mNineGridListener);
        }

        void setData(ActiveBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                mNineGridLayout.setData(bean.getImageList());
            }
        }

    }


    class VoiceVh extends Vh {

        ActiveVoiceLayout mVoiceLayout;

        public VoiceVh(@NonNull View itemView) {
            super(itemView);
            mVoiceLayout = itemView.findViewById(R.id.voice_layout);
            mVoiceLayout.setActionListener(mVoiceListener);
        }

        void setData(ActiveBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                mVoiceLayout.setSecondsMax(bean.getVoiceDuration());
                mVoiceLayout.setVoiceUrl(bean.getVoiceUrl());
            }
        }

        boolean isPlaying() {
            return mVoiceLayout != null && mVoiceLayout.isPlaying();
        }

        public void stopPlay() {
            if (mVoiceLayout != null) {
                mVoiceLayout.stopPlay();
            }
        }

    }


    class VideoVh extends Vh {

        View mBtnVideo;
        ImageView mVideoCover;

        public VideoVh(@NonNull View itemView) {
            super(itemView);
            mBtnVideo = itemView.findViewById(R.id.btn_video);
            mVideoCover = itemView.findViewById(R.id.video_cover);
            mBtnVideo.setOnClickListener(mVideoClickListener);
        }

        void setData(ActiveBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                mBtnVideo.setTag(bean);
                ImgLoader.display(mContext, bean.getVideoImage(), mVideoCover);
            }
        }

    }


    public void release() {
        if (mNowPlayVoiceLayout != null) {
            mNowPlayVoiceLayout.stopPlay();
        }
        if (mPlayerUtil != null) {
            mPlayerUtil.destroy();
        }
        mPlayerUtil = null;
        MainHttpUtil.cancel(MainHttpConsts.ACTIVE_ADD_LIKE);
        MainHttpUtil.cancel(MainHttpConsts.ACTIVE_DELETE);
        MainHttpUtil.cancel(Constants.DOWNLOAD_MUSIC);
    }


//    @Override
//    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder vh) {
//    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder vh) {
        if (vh instanceof VoiceVh) {
            VoiceVh voiceVh = (VoiceVh) vh;
            if (voiceVh.isPlaying()) {
                voiceVh.stopPlay();
            }
        }
    }


    /**
     * 停止播放动态声音
     */
    public void stopActiveVoice() {
        if (mNowPlayVoiceLayout != null) {
            mNowPlayVoiceLayout.stopPlay();
        }
    }
}
