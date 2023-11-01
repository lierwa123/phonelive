package com.yunbao.common;

/**
 * Created by cxf on 2018/10/15.
 */

public class HtmlConfig {

    //登录即代表同意服务和隐私条款
    public static final String LOGIN_PRIVCAY = CommonAppConfig.HOST + "/portal/page/index?id=4";
    //直播间贡献榜
    public static final String LIVE_LIST = CommonAppConfig.HOST + "/Appapi/contribute/index?uid=";
    //个人主页分享链接
    public static final String SHARE_HOME_PAGE = CommonAppConfig.HOST + "/Appapi/home/index?touid=";
    //提现记录
    public static final String CASH_RECORD = CommonAppConfig.HOST + "/Appapi/cash/index";
    //支付宝充值回调地址
    public static final String ALI_PAY_COIN_URL = CommonAppConfig.HOST + "/Appapi/Pay/notify_ali";
    //支付宝购物下单支付回调地址
    public static final String ALI_PAY_MALL_ORDER = CommonAppConfig.HOST + "/Appapi/Shoppay/notify_ali";
    //支付宝购买付费内容 下单支付回调地址
    public static final String ALI_PAY_MALL_PAY_CONTENT = CommonAppConfig.HOST + "/Appapi/Paidprogrampay/notify_ali";

    //视频分享地址
    public static final String SHARE_VIDEO = CommonAppConfig.HOST + "/appapi/video/index?videoid=";
    //直播间幸运礼物说明
    public static final String LUCK_GIFT_TIP = CommonAppConfig.HOST + "/portal/page/index?id=26";
    //直播间道具礼物说明
    public static final String DAO_GIFT_TIP = CommonAppConfig.HOST + "/portal/page/index?id=39";
    //在线商城
    public static final String SHOP = CommonAppConfig.HOST + "/Appapi/Mall/index";
    //我的明细
    public static final String DETAIL = CommonAppConfig.HOST + "/Appapi/Detail/index";
    //充值协议
    public static final String CHARGE_PRIVCAY = CommonAppConfig.HOST + "/portal/page/index?id=6";
    //直播间游戏规则
    public static final String GAME_RULE = CommonAppConfig.HOST + "/portal/page/index?id=35";
    //买家查看物流信息
    public static final String MALL_BUYER_WULIU = CommonAppConfig.HOST + "/appapi/Express/index?";
    //退款协商历史
    public static final String MALL_REFUND_HISTORY = CommonAppConfig.HOST + "/Appapi/Goodsorderrefund/index?";
    //商城提现记录
    public static final String MALL_CASH_RECORD = CommonAppConfig.HOST + "/appapi/shopcash/index?";
    //付费内容实名认证
    public static final String MALL_PAY_AUTH = CommonAppConfig.HOST + "/Appapi/Auth/index?";
    //付费内容管理规范说明
    public static final String MALL_PAY_APPLY_TIP = CommonAppConfig.HOST + "/portal/page/index?id=40";
}
