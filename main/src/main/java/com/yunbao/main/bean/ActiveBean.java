package com.yunbao.main.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.yunbao.common.CommonAppConfig;

import java.io.File;
import java.util.List;

/**
 * 动态实体类
 */
public class ActiveBean implements Parcelable {

    private int mActiveType;// 动态类型：0：纯文字；1：文字+图片；2：文字+视频；3：文字+音频
    private String mText;// 文字内容
    private List<String> mImageList;//图片地址数组
    private String mVideoImage;//视频封面
    private String mVideoUrl;//视频地址
    private String mVoiceUrl;//语音地址
    private File mVoiceFile;//语音文件
    private int mVoiceDuration;//语音时长
    private String mLng;//经度
    private String mLat;//纬度
    private String mCity;//城市
    private String mAddress;//详细地理位置
    private String mId;
    private String mUid;
    private int mLikeNum;//点赞数
    private int mCommentNum;//评论数
    private int mIsLike;//是否点赞了
    private String mDateTime;//日期
    private int mStatus;// 0未审核 1通过 2拒绝
    private String mTopicId;
    private String mTopicName;
    private ActiveUserBean mUserBean;//发布动态的人的用户信息


    public ActiveBean() {

    }


    @JSONField(name = "type")
    public int getActiveType() {
        return mActiveType;
    }

    @JSONField(name = "type")
    public void setActiveType(int activeType) {
        mActiveType = activeType;
    }

    @JSONField(name = "title")
    public String getText() {
        return mText;
    }

    @JSONField(name = "title")
    public void setText(String text) {
        mText = text;
    }

    @JSONField(name = "thumbs")
    public List<String> getImageList() {
        return mImageList;
    }

    @JSONField(name = "thumbs")
    public void setImageList(List<String> imageList) {
        mImageList = imageList;
    }

    @JSONField(name = "video_thumb")
    public String getVideoImage() {
        return mVideoImage;
    }

    @JSONField(name = "video_thumb")
    public void setVideoImage(String videoImage) {
        mVideoImage = videoImage;
    }

    @JSONField(name = "href")
    public String getVideoUrl() {
        return mVideoUrl;
    }

    @JSONField(name = "href")
    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    @JSONField(name = "voice")
    public String getVoiceUrl() {
        return mVoiceUrl;
    }

    @JSONField(name = "voice")
    public void setVoiceUrl(String voiceUrl) {
        mVoiceUrl = voiceUrl;
    }

    @JSONField(name = "length")
    public int getVoiceDuration() {
        return mVoiceDuration;
    }

    @JSONField(name = "length")
    public void setVoiceDuration(int voiceDuration) {
        mVoiceDuration = voiceDuration;
    }

    @JSONField(serialize = false)
    public File getVoiceFile() {
        return mVoiceFile;
    }

    @JSONField(serialize = false)
    public void setVoiceFile(File voiceFile) {
        mVoiceFile = voiceFile;
    }

    @JSONField(name = "lng")
    public String getLng() {
        return mLng;
    }

    @JSONField(name = "lng")
    public void setLng(String lng) {
        mLng = lng;
    }

    @JSONField(name = "lat")
    public String getLat() {
        return mLat;
    }

    @JSONField(name = "lat")
    public void setLat(String lat) {
        mLat = lat;
    }

    @JSONField(name = "city")
    public String getCity() {
        return mCity;
    }

    @JSONField(name = "city")
    public void setCity(String city) {
        mCity = city;
    }

    @JSONField(name = "address")
    public String getAddress() {
        return mAddress;
    }

    @JSONField(name = "address")
    public void setAddress(String address) {
        mAddress = address;
    }

    @JSONField(name = "id")
    public String getId() {
        return mId;
    }

    @JSONField(name = "id")
    public void setId(String id) {
        mId = id;
    }

    @JSONField(name = "uid")
    public String getUid() {
        return mUid;
    }

    @JSONField(name = "uid")
    public void setUid(String uid) {
        mUid = uid;
    }

    @JSONField(name = "likes")
    public int getLikeNum() {
        return mLikeNum;
    }

    @JSONField(name = "likes")
    public void setLikeNum(int likeNum) {
        mLikeNum = likeNum;
    }

    @JSONField(name = "comments")
    public int getCommentNum() {
        return mCommentNum;
    }

    @JSONField(name = "comments")
    public void setCommentNum(int commentNum) {
        mCommentNum = commentNum;
    }

    @JSONField(name = "islike")
    public int getIsLike() {
        return mIsLike;
    }

    @JSONField(name = "islike")
    public void setIsLike(int isLike) {
        mIsLike = isLike;
    }

    @JSONField(name = "datetime")
    public String getDateTime() {
        return mDateTime;
    }

    @JSONField(name = "datetime")
    public void setDateTime(String dateTime) {
        mDateTime = dateTime;
    }

    @JSONField(name = "status")
    public int getStatus() {
        return mStatus;
    }

    @JSONField(name = "status")
    public void setStatus(int status) {
        mStatus = status;
    }

    @JSONField(name = "labelid")
    public String getTopicId() {
        return mTopicId;
    }

    @JSONField(name = "labelid")
    public void setTopicId(String topicId) {
        mTopicId = topicId;
    }

    @JSONField(name = "label_name")
    public String getTopicName() {
        return mTopicName;
    }

    @JSONField(name = "label_name")
    public void setTopicName(String topicName) {
        mTopicName = topicName;
    }

    @JSONField(name = "userinfo")
    public ActiveUserBean getUserBean() {
        return mUserBean;
    }

    @JSONField(name = "userinfo")
    public void setUserBean(ActiveUserBean userBean) {
        mUserBean = userBean;
    }

    public String getLikeNumString() {
        return mLikeNum == 0 ? "" : String.valueOf(mLikeNum);
    }


    public String getCommentNumString() {
        return mCommentNum == 0 ? "" : String.valueOf(mCommentNum);
    }

    public boolean isLike() {
        return mIsLike == 1;
    }

    public boolean isFollow() {
        return mUserBean != null && mUserBean.getIsAttention() == 1;
    }

    public boolean isSelf() {
        return !TextUtils.isEmpty(mUid) && mUid.equals(CommonAppConfig.getInstance().getUid());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mActiveType);
        dest.writeString(mText);
        dest.writeStringList(mImageList);
        dest.writeString(mVideoImage);
        dest.writeString(mVideoUrl);
        dest.writeString(mVoiceUrl);
        dest.writeInt(mVoiceDuration);
        dest.writeString(mLng);
        dest.writeString(mLat);
        dest.writeString(mCity);
        dest.writeString(mAddress);
        dest.writeString(mId);
        dest.writeString(mUid);
        dest.writeInt(mLikeNum);
        dest.writeInt(mCommentNum);
        dest.writeInt(mIsLike);
        dest.writeString(mDateTime);
        dest.writeInt(mStatus);
        dest.writeString(mTopicId);
        dest.writeString(mTopicName);
        dest.writeParcelable(mUserBean, flags);
    }


    public ActiveBean(Parcel in) {
        mActiveType = in.readInt();
        mText = in.readString();
        mImageList = in.createStringArrayList();
        mVideoImage = in.readString();
        mVideoUrl = in.readString();
        mVoiceUrl = in.readString();
        mVoiceDuration = in.readInt();
        mLng = in.readString();
        mLat = in.readString();
        mCity = in.readString();
        mAddress = in.readString();
        mId = in.readString();
        mUid = in.readString();
        mLikeNum = in.readInt();
        mCommentNum = in.readInt();
        mIsLike = in.readInt();
        mDateTime = in.readString();
        mStatus = in.readInt();
        mTopicId = in.readString();
        mTopicName = in.readString();
        mUserBean = in.readParcelable(ActiveUserBean.class.getClassLoader());
    }

    public static final Creator<ActiveBean> CREATOR = new Creator<ActiveBean>() {
        @Override
        public ActiveBean createFromParcel(Parcel in) {
            return new ActiveBean(in);
        }

        @Override
        public ActiveBean[] newArray(int size) {
            return new ActiveBean[size];
        }
    };

}
