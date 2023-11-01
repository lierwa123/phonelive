package com.yunbao.live.bean;

import android.graphics.PointF;
import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.yunbao.common.utils.StringUtil;

import java.util.List;

/**
 * Created by cxf on 2017/8/22.
 * 收到送礼物消息的实体类
 */

public class LiveReceiveGiftBean {

    private String uid;
    private String toUid;//送给人的uid
    private String toName;//送给人的昵称
    private String avatar;
    private String userNiceName;
    private int level;
    private String giftId;
    private int giftCount;
    private String giftName;
    private String giftIcon;
    private String votes;
    private int lianCount = 1;
    private int type;//0 普通礼物 1是豪华礼物 3是手绘礼物
    private int gifType;//豪华礼物类型 0是gif  1是svga
    private String gifUrl;
    private String mKey;
    private int mMark;//礼物标记 // 0 普通  1热门  2守护 3幸运
    private int mLuck;//幸运礼物中奖了
    private String mLuckTime;//中奖倍数
    private String mStickerId;//道具礼物id
    private float mPlayTime;//播放时长单位秒
    private int mIsGlobal;//是否是全站礼物
    private List<PointF> mPointList;
    private float mDrawWidth;
    private float mDrawHeight;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserNiceName() {
        return userNiceName;
    }

    public void setUserNiceName(String userNiceName) {
//        if (!TextUtils.isEmpty(userNiceName) && userNiceName.length() > 7) {
//            userNiceName = userNiceName.substring(0, 7) + "...";
//        }
        this.userNiceName = userNiceName;
    }

    @JSONField(name = "level")
    public int getLevel() {
        return level;
    }

    @JSONField(name = "level")
    public void setLevel(int level) {
        this.level = level;
    }

    @JSONField(name = "giftid")
    public String getGiftId() {
        return giftId;
    }

    @JSONField(name = "giftid")
    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    @JSONField(name = "giftcount")
    public int getGiftCount() {
        return giftCount;
    }

    @JSONField(name = "giftcount")
    public void setGiftCount(int giftCount) {
        this.giftCount = giftCount;
    }

    @JSONField(name = "giftname")
    public String getGiftName() {
        return giftName;
    }

    @JSONField(name = "giftname")
    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    @JSONField(name = "gifticon")
    public String getGiftIcon() {
        return giftIcon;
    }

    @JSONField(name = "gifticon")
    public void setGiftIcon(String giftIcon) {
        this.giftIcon = giftIcon;
    }

    @JSONField(name = "votestotal")
    public String getVotes() {
        return votes;
    }

    @JSONField(name = "votestotal")
    public void setVotes(String votes) {
        this.votes = votes;
    }

    @JSONField(name = "type")
    public int getType() {
        return type;
    }

    @JSONField(name = "type")
    public void setGif(int type) {
        this.type = type;
    }

    @JSONField(name = "swf")
    public String getGifUrl() {
        return gifUrl;
    }

    @JSONField(name = "swf")
    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    @JSONField(name = "swftype")
    public int getGifType() {
        return gifType;
    }

    @JSONField(name = "swftype")
    public void setGifType(int gitType) {
        this.gifType = gitType;
    }

    public int getLianCount() {
        return lianCount;
    }

    public void setLianCount(int lianCount) {
        this.lianCount = lianCount;
    }

    @JSONField(name = "mark")
    public int getMark() {
        return mMark;
    }

    @JSONField(name = "mark")
    public void setMark(int mark) {
        mMark = mark;
    }

    @JSONField(name = "isluck")
    public int getLuck() {
        return mLuck;
    }

    @JSONField(name = "isluck")
    public void setLuck(int luck) {
        mLuck = luck;
    }

    @JSONField(name = "lucktimes")
    public String getLuckTime() {
        return mLuckTime;
    }

    @JSONField(name = "lucktimes")
    public void setLuckTime(String luckTime) {
        mLuckTime = luckTime;
    }

    public String getKey() {
        if (TextUtils.isEmpty(mKey)) {
            mKey = StringUtil.contact(this.uid, this.toUid, this.giftId, String.valueOf(this.giftCount));
        }
        return mKey;
    }

    @JSONField(name = "sticker_id")
    public String getStickerId() {
        return mStickerId;
    }

    @JSONField(name = "sticker_id")
    public void setStickerId(String stickerId) {
        mStickerId = stickerId;
    }

    @JSONField(name = "swftime")
    public float getPlayTime() {
        return mPlayTime;
    }

    @JSONField(name = "swftime")
    public void setPlayTime(float playTime) {
        mPlayTime = playTime;
    }

    @JSONField(name = "isplatgift")
    public int getIsGlobal() {
        return mIsGlobal;
    }

    @JSONField(name = "isplatgift")
    public void setIsGlobal(int isGlobal) {
        mIsGlobal = isGlobal;
    }

    public List<PointF> getPointList() {
        return mPointList;
    }

    public void setPointList(List<PointF> pointList) {
        mPointList = pointList;
    }

    public float getDrawWidth() {
        return mDrawWidth;
    }

    public void setDrawWidth(float drawWidth) {
        mDrawWidth = drawWidth;
    }

    public float getDrawHeight() {
        return mDrawHeight;
    }

    public void setDrawHeight(float drawHeight) {
        mDrawHeight = drawHeight;
    }

    @JSONField(name = "touid")
    public String getToUid() {
        return toUid;
    }

    @JSONField(name = "touid")
    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    @JSONField(name = "to_username")
    public String getToName() {
        return toName;
    }

    @JSONField(name = "to_username")
    public void setToName(String toName) {
        this.toName = toName;
    }
}
