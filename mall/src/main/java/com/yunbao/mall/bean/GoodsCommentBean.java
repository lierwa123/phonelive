package com.yunbao.mall.bean;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 商品 评论
 */
public class GoodsCommentBean {

    private String mId;
    private String mUid;
    private String mOrderId;
    private String mGoodsId;
    private String mSellerId;
    private String mContent;
    private String mVideoThumb;
    private String mVideoUrl;
    private int mStarCount;
    private String mDateTime;
    private String mThumbs;
    private String mGoodsSpecName;
    private String mBuyerName;
    private String mAvatar;
    private String mAppendComment;
    private int mIsAppend;//是不是追评
    private int mHasAppend;//有没有追评
    private String mDateTip;//追评时间提示
    private GoodsCommentBean mAppendBean;
    private List<String> mImageList;


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

    @JSONField(name = "orderid")
    public String getOrderId() {
        return mOrderId;
    }

    @JSONField(name = "orderid")
    public void setOrderId(String orderId) {
        mOrderId = orderId;
    }

    @JSONField(name = "goodsid")
    public String getGoodsId() {
        return mGoodsId;
    }

    @JSONField(name = "goodsid")
    public void setGoodsId(String goodsId) {
        mGoodsId = goodsId;
    }

    @JSONField(name = "shop_uid")
    public String getSellerId() {
        return mSellerId;
    }

    @JSONField(name = "shop_uid")
    public void setSellerId(String sellerId) {
        mSellerId = sellerId;
    }

    @JSONField(name = "content")
    public String getContent() {
        return mContent;
    }

    @JSONField(name = "content")
    public void setContent(String content) {
        mContent = content;
    }

    @JSONField(name = "video_thumb")
    public String getVideoThumb() {
        return mVideoThumb;
    }

    @JSONField(name = "video_thumb")
    public void setVideoThumb(String videoThumb) {
        mVideoThumb = videoThumb;
    }

    @JSONField(name = "video_url")
    public String getVideoUrl() {
        return mVideoUrl;
    }

    @JSONField(name = "video_url")
    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    @JSONField(name = "quality_points")
    public int getStarCount() {
        return mStarCount;
    }

    @JSONField(name = "quality_points")
    public void setStarCount(int starCount) {
        mStarCount = starCount;
    }

    @JSONField(name = "time_format")
    public String getDateTime() {
        return mDateTime;
    }

    @JSONField(name = "time_format")
    public void setDateTime(String dateTime) {
        mDateTime = dateTime;
    }

    @JSONField(name = "thumb_format")
    public String getThumbs() {
        return mThumbs;
    }

    @JSONField(name = "thumb_format")
    public void setThumbs(String thumbs) {
        mThumbs = thumbs;
    }

    @JSONField(name = "spec_name")
    public String getGoodsSpecName() {
        return mGoodsSpecName;
    }

    @JSONField(name = "spec_name")
    public void setGoodsSpecName(String goodsSpecName) {
        mGoodsSpecName = goodsSpecName;
    }

    @JSONField(name = "user_nicename")
    public String getBuyerName() {
        return mBuyerName;
    }

    @JSONField(name = "user_nicename")
    public void setBuyerName(String buyerName) {
        mBuyerName = buyerName;
    }

    @JSONField(name = "avatar")
    public String getAvatar() {
        return mAvatar;
    }

    @JSONField(name = "avatar")
    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    @JSONField(name = "append_comment")
    public String getAppendComment() {
        return mAppendComment;
    }

    @JSONField(name = "append_comment")
    public void setAppendComment(String appendComment) {
        mAppendComment = appendComment;
    }

    @JSONField(name = "is_append")
    public int getIsAppend() {
        return mIsAppend;
    }

    @JSONField(name = "is_append")
    public void setIsAppend(int isAppend) {
        mIsAppend = isAppend;
    }

    @JSONField(name = "has_append_comment")
    public int getHasAppend() {
        return mHasAppend;
    }

    @JSONField(name = "has_append_comment")
    public void setHasAppend(int hasAppend) {
        mHasAppend = hasAppend;
    }

    @JSONField(name = "date_tips")
    public String getDateTip() {
        return mDateTip;
    }

    @JSONField(name = "date_tips")
    public void setDateTip(String dataTips) {
        mDateTip = dataTips;
    }

    public boolean hasImgOrVideo() {
        return !TextUtils.isEmpty(mVideoUrl) && !TextUtils.isEmpty(mVideoThumb) || !"[]".equals(mThumbs);
    }

    public GoodsCommentBean getAppendCommentBean() {
        if (mHasAppend == 1
                && mAppendBean == null
                && !TextUtils.isEmpty(mAppendComment)
                && mAppendComment.startsWith("{")) {
            try {
                mAppendBean = JSON.parseObject(mAppendComment, GoodsCommentBean.class);
            } catch (Exception e) {

            }
        }
        return mAppendBean;
    }


    public List<String> getImageList() {
        if (mImageList == null) {
            mImageList = JSON.parseArray(mThumbs, String.class);
            if (!TextUtils.isEmpty(mVideoThumb)) {
                mImageList.add(0, mVideoThumb);
            }
        }
        return mImageList;
    }
}
