package com.yunbao.video.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.yunbao.common.bean.GoodsBean;
import com.yunbao.common.bean.UserBean;

/**
 * Created by cxf on 2017/10/25.
 */

public class VideoBean implements Parcelable {
    private String id;
    private String uid;
    private String title;
    private String thumb;
    private String thumbs;
    private String href;
    private String hrefW;
    private String likeNum;
    private String viewNum;
    private String commentNum;
    private String stepNum;
    private String shareNum;
    private String addtime;
    private String lat;
    private String lng;
    private String city;
    private UserBean userBean;
    private String datetime;
    private String distance;
    private int step;//是否踩过
    private int like;//是否赞过
    private int attent;//是否关注过作者
    private int status;//视频状态 0审核中 1通过 2拒绝
    private int musicId;
    private String mGoodsId;
    private int mType;// type  绑定的内容类型 0 没绑定 1 商品 2 付费内容
    private int mGoodsType;//0站内商品 1站外商品

    public VideoBean() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @JSONField(name = "thumb_s")
    public String getThumbs() {
        return thumbs;
    }

    @JSONField(name = "thumb_s")
    public void setThumbs(String thumbs) {
        this.thumbs = thumbs;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @JSONField(name = "href_w")
    public String getHrefW() {
        return hrefW;
    }

    @JSONField(name = "href_w")
    public void setHrefW(String hrefW) {
        this.hrefW = hrefW;
    }


    @JSONField(name = "likes")
    public String getLikeNum() {
        return likeNum;
    }

    @JSONField(name = "likes")
    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    @JSONField(name = "views")
    public String getViewNum() {
        return viewNum;
    }

    @JSONField(name = "views")
    public void setViewNum(String viewNum) {
        this.viewNum = viewNum;
    }

    @JSONField(name = "comments")
    public String getCommentNum() {
        return commentNum;
    }

    @JSONField(name = "comments")
    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    @JSONField(name = "steps")
    public String getStepNum() {
        return stepNum;
    }

    @JSONField(name = "steps")
    public void setStepNum(String stepNum) {
        this.stepNum = stepNum;
    }

    @JSONField(name = "shares")
    public String getShareNum() {
        return shareNum;
    }

    @JSONField(name = "shares")
    public void setShareNum(String shareNum) {
        this.shareNum = shareNum;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    @JSONField(name = "userinfo")
    public UserBean getUserBean() {
        return userBean;
    }

    @JSONField(name = "userinfo")
    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @JSONField(name = "isstep")
    public int getStep() {
        return step;
    }

    @JSONField(name = "isstep")
    public void setStep(int step) {
        this.step = step;
    }

    @JSONField(name = "islike")
    public int getLike() {
        return like;
    }

    @JSONField(name = "islike")
    public void setLike(int like) {
        this.like = like;
    }

    @JSONField(name = "isattent")
    public int getAttent() {
        return attent;
    }

    @JSONField(name = "isattent")
    public void setAttent(int attent) {
        this.attent = attent;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @JSONField(name = "music_id")
    public int getMusicId() {
        return musicId;
    }

    @JSONField(name = "music_id")
    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    @JSONField(name = "goodsid")
    public String getGoodsId() {
        return mGoodsId;
    }

    @JSONField(name = "goodsid")
    public void setGoodsId(String goodsId) {
        mGoodsId = goodsId;
    }

    @JSONField(name = "type")
    public int getType() {
        return mType;
    }

    @JSONField(name = "type")
    public void setType(int type) {
        mType = type;
    }

    @JSONField(name = "goods_type")
    public int getGoodsType() {
        return mGoodsType;
    }

    @JSONField(name = "goods_type")
    public void setGoodsType(int goodsType) {
        mGoodsType = goodsType;
    }


    public String getTag() {
        return "VideoBean" + this.getId() + this.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(uid);
        dest.writeString(title);
        dest.writeString(thumb);
        dest.writeString(thumbs);
        dest.writeString(href);
        dest.writeString(hrefW);
        dest.writeString(likeNum);
        dest.writeString(viewNum);
        dest.writeString(commentNum);
        dest.writeString(stepNum);
        dest.writeString(shareNum);
        dest.writeString(addtime);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(city);
        dest.writeParcelable(userBean, flags);
        dest.writeString(datetime);
        dest.writeString(distance);
        dest.writeInt(step);
        dest.writeInt(like);
        dest.writeInt(attent);
        dest.writeInt(status);
        dest.writeInt(musicId);
        dest.writeString(mGoodsId);
        dest.writeInt(mType);
        dest.writeInt(mGoodsType);
    }


    protected VideoBean(Parcel in) {
        id = in.readString();
        uid = in.readString();
        title = in.readString();
        thumb = in.readString();
        thumbs = in.readString();
        href = in.readString();
        hrefW = in.readString();
        likeNum = in.readString();
        viewNum = in.readString();
        commentNum = in.readString();
        stepNum = in.readString();
        shareNum = in.readString();
        addtime = in.readString();
        lat = in.readString();
        lng = in.readString();
        city = in.readString();
        userBean = in.readParcelable(UserBean.class.getClassLoader());
        datetime = in.readString();
        distance = in.readString();
        step = in.readInt();
        like = in.readInt();
        attent = in.readInt();
        status = in.readInt();
        musicId = in.readInt();
        mGoodsId = in.readString();
        mType = in.readInt();
        mGoodsType = in.readInt();
    }

    public static final Creator<VideoBean> CREATOR = new Creator<VideoBean>() {
        @Override
        public VideoBean createFromParcel(Parcel in) {
            return new VideoBean(in);
        }

        @Override
        public VideoBean[] newArray(int size) {
            return new VideoBean[size];
        }
    };
}
