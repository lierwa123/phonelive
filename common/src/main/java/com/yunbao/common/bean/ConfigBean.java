package com.yunbao.common.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by cxf on 2017/8/5.
 */

public class ConfigBean {
    private String version;//Android apk安装包 版本号
    private String downloadApkUrl;//Android apk安装包 下载地址
    private String updateDes;//版本更新描述
    private String liveWxShareUrl;//直播间微信分享地址
    private String liveShareTitle;//直播间分享标题
    private String liveShareDes;//直播间分享描述
    private String videoShareTitle;//短视频分享标题
    private String videoShareDes;//短视频分享描述
    private int videoAuditSwitch;//短视频审核是否开启
    private int videoCloudType;//短视频云储存类型 1七牛云 2腾讯云
    private String videoQiNiuHost;//短视频七牛云域名
    private String txCosAppId;//腾讯云存储appId
    private String txCosRegion;//腾讯云存储区域
    private String txCosBucketName;//腾讯云存储桶名字
    private String txCosVideoPath;//腾讯云存储视频文件夹
    private String txCosImagePath;//腾讯云存储图片文件夹
    private String coinName;//钻石名称
    private String votesName;//映票名称
    private String scoreName;//积分名称
    private String[] liveTimeCoin;//直播间计时收费规则
    private String[] loginType;//三方登录类型
    private String[][] liveType;//直播间开播类型
    private String[] shareType;//分享类型
    private List<LiveClassBean> liveClass;//直播分类
    private String videoClass;//短视频分类
    private int maintainSwitch;//维护开关
    private String maintainTips;//维护提示
    private String mAdInfo;//引导页 广告信息
    private int priMsgSwitch;//私信开关
    private int forceUpdate;//强制更新
    private String mWaterMarkUrl;//水印
    private String mShopSystemName;//商店名称

    private String beautyKey;//美颜鉴权码


    @JSONField(name = "apk_ver")
    public String getVersion() {
        return version;
    }

    @JSONField(name = "apk_ver")
    public void setVersion(String version) {
        this.version = version;
    }

    @JSONField(name = "apk_url")
    public String getDownloadApkUrl() {
        return downloadApkUrl;
    }

    @JSONField(name = "apk_url")
    public void setDownloadApkUrl(String downloadApkUrl) {
        this.downloadApkUrl = downloadApkUrl;
    }

    @JSONField(name = "apk_des")
    public String getUpdateDes() {
        return updateDes;
    }

    @JSONField(name = "apk_des")
    public void setUpdateDes(String updateDes) {
        this.updateDes = updateDes;
    }

    @JSONField(name = "wx_siteurl")
    public void setLiveWxShareUrl(String liveWxShareUrl) {
        this.liveWxShareUrl = liveWxShareUrl;
    }

    @JSONField(name = "wx_siteurl")
    public String getLiveWxShareUrl() {
        return liveWxShareUrl;
    }

    @JSONField(name = "share_title")
    public String getLiveShareTitle() {
        return liveShareTitle;
    }

    @JSONField(name = "share_title")
    public void setLiveShareTitle(String liveShareTitle) {
        this.liveShareTitle = liveShareTitle;
    }

    @JSONField(name = "share_des")
    public String getLiveShareDes() {
        return liveShareDes;
    }

    @JSONField(name = "share_des")
    public void setLiveShareDes(String liveShareDes) {
        this.liveShareDes = liveShareDes;
    }

    @JSONField(name = "name_coin")
    public String getCoinName() {
        return coinName;
    }

    @JSONField(name = "name_coin")
    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    @JSONField(name = "name_score")
    public String getScoreName() {
        return scoreName;
    }
    @JSONField(name = "name_score")
    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }

    @JSONField(name = "name_votes")
    public String getVotesName() {
        return votesName;
    }

    @JSONField(name = "name_votes")
    public void setVotesName(String votesName) {
        this.votesName = votesName;
    }

    @JSONField(name = "live_time_coin")
    public String[] getLiveTimeCoin() {
        return liveTimeCoin;
    }

    @JSONField(name = "live_time_coin")
    public void setLiveTimeCoin(String[] liveTimeCoin) {
        this.liveTimeCoin = liveTimeCoin;
    }

    @JSONField(name = "login_type")
    public String[] getLoginType() {
        return loginType;
    }

    @JSONField(name = "login_type")
    public void setLoginType(String[] loginType) {
        this.loginType = loginType;
    }

    @JSONField(name = "live_type")
    public String[][] getLiveType() {
        return liveType;
    }

    @JSONField(name = "live_type")
    public void setLiveType(String[][] liveType) {
        this.liveType = liveType;
    }

    @JSONField(name = "share_type")
    public String[] getShareType() {
        return shareType;
    }

    @JSONField(name = "share_type")
    public void setShareType(String[] shareType) {
        this.shareType = shareType;
    }

    @JSONField(name = "liveclass")
    public List<LiveClassBean> getLiveClass() {
        return liveClass;
    }

    @JSONField(name = "liveclass")
    public void setLiveClass(List<LiveClassBean> liveClass) {
        this.liveClass = liveClass;
    }


    @JSONField(name = "videoclass")
    public String getVideoClass() {
        return videoClass;
    }

    @JSONField(name = "videoclass")
    public void setVideoClass(String videoClass) {
        this.videoClass = videoClass;
    }

    @JSONField(name = "maintain_switch")
    public int getMaintainSwitch() {
        return maintainSwitch;
    }

    @JSONField(name = "maintain_switch")
    public void setMaintainSwitch(int maintainSwitch) {
        this.maintainSwitch = maintainSwitch;
    }

    @JSONField(name = "maintain_tips")
    public String getMaintainTips() {
        return maintainTips;
    }

    @JSONField(name = "maintain_tips")
    public void setMaintainTips(String maintainTips) {
        this.maintainTips = maintainTips;
    }

    @JSONField(name = "sprout_key")
    public String getBeautyKey() {
        return beautyKey;
    }

    @JSONField(name = "sprout_key")
    public void setBeautyKey(String beautyKey) {
        this.beautyKey = beautyKey;
    }


    public String[] getVideoShareTypes() {
        return shareType;
    }

    @JSONField(name = "video_share_title")
    public String getVideoShareTitle() {
        return videoShareTitle;
    }

    @JSONField(name = "video_share_title")
    public void setVideoShareTitle(String videoShareTitle) {
        this.videoShareTitle = videoShareTitle;
    }

    @JSONField(name = "video_share_des")
    public String getVideoShareDes() {
        return videoShareDes;
    }

    @JSONField(name = "video_share_des")
    public void setVideoShareDes(String videoShareDes) {
        this.videoShareDes = videoShareDes;
    }

    @JSONField(name = "video_audit_switch")
    public int getVideoAuditSwitch() {
        return videoAuditSwitch;
    }

    @JSONField(name = "video_audit_switch")
    public void setVideoAuditSwitch(int videoAuditSwitch) {
        this.videoAuditSwitch = videoAuditSwitch;
    }

    @JSONField(name = "cloudtype")
    public int getVideoCloudType() {
        return videoCloudType;
    }

    @JSONField(name = "cloudtype")
    public void setVideoCloudType(int videoCloudType) {
        this.videoCloudType = videoCloudType;
    }

    @JSONField(name = "qiniu_domain")
    public String getVideoQiNiuHost() {
        return videoQiNiuHost;
    }

    @JSONField(name = "qiniu_domain")
    public void setVideoQiNiuHost(String videoQiNiuHost) {
        this.videoQiNiuHost = videoQiNiuHost;
    }

    @JSONField(name = "txcloud_appid")
    public String getTxCosAppId() {
        return txCosAppId;
    }

    @JSONField(name = "txcloud_appid")
    public void setTxCosAppId(String txCosAppId) {
        this.txCosAppId = txCosAppId;
    }

    @JSONField(name = "txcloud_region")
    public String getTxCosRegion() {
        return txCosRegion;
    }

    @JSONField(name = "txcloud_region")
    public void setTxCosRegion(String txCosRegion) {
        this.txCosRegion = txCosRegion;
    }

    @JSONField(name = "txcloud_bucket")
    public String getTxCosBucketName() {
        return txCosBucketName;
    }

    @JSONField(name = "txcloud_bucket")
    public void setTxCosBucketName(String txCosBucketName) {
        this.txCosBucketName = txCosBucketName;
    }

    @JSONField(name = "video_watermark")
    public String getWaterMarkUrl() {
        return mWaterMarkUrl;
    }

    @JSONField(name = "video_watermark")
    public void setWaterMarkUrl(String waterMarkUrl) {
        mWaterMarkUrl = waterMarkUrl;
    }

    @JSONField(name = "txvideofolder")
    public String getTxCosVideoPath() {
        return txCosVideoPath;
    }

    @JSONField(name = "txvideofolder")
    public void setTxCosVideoPath(String txCosVideoPath) {
        this.txCosVideoPath = txCosVideoPath;
    }

    @JSONField(name = "tximgfolder")
    public String getTxCosImagePath() {
        return txCosImagePath;
    }

    @JSONField(name = "tximgfolder")
    public void setTxCosImagePath(String txCosImagePath) {
        this.txCosImagePath = txCosImagePath;
    }

    @JSONField(name = "guide")
    public String getAdInfo() {
        return mAdInfo;
    }

    @JSONField(name = "guide")
    public void setAdInfo(String adInfo) {
        mAdInfo = adInfo;
    }

    @JSONField(name = "letter_switch")
    public int getPriMsgSwitch() {
        return priMsgSwitch;
    }

    @JSONField(name = "letter_switch")
    public void setPriMsgSwitch(int priMsgSwitch) {
        this.priMsgSwitch = priMsgSwitch;
    }

    @JSONField(name = "isup")
    public int getForceUpdate() {
        return forceUpdate;
    }

    @JSONField(name = "isup")
    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }


    @JSONField(name = "shop_system_name")
    public String getShopSystemName() {
        return mShopSystemName;
    }
    @JSONField(name = "shop_system_name")
    public void setShopSystemName(String shopSystemName) {
        mShopSystemName = shopSystemName;
    }

}
