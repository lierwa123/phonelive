package com.yunbao.common;

/**
 * Created by cxf on 2018/6/7.
 */

public class Constants {
    public static final String URL = "url";
    public static final String PAYLOAD = "payload";
    public static final String SEX = "sex";
    public static final String NICK_NAME = "nickname";
    public static final String AVATAR = "avatar";
    public static final String SIGN = "sign";
    public static final String TO_UID = "toUid";
    public static final String FROM_LIVE_ROOM = "fromLiveRoom";
    public static final String TO_NAME = "toName";
    public static final String STREAM = "stream";
    public static final String LIMIT = "limit";
    public static final String UID = "uid";
    public static final String TIP = "tip";
    public static final String FIRST_LOGIN = "firstLogin";
    public static final String USER_BEAN = "userBean";
    public static final String CLASS_ID = "classID";
    public static final String CLASS_NAME = "className";
    public static final String CHECKED_ID = "checkedId";
    public static final String CHECKED_COIN = "checkedCoin";
    public static final String LIVE_DANMU_PRICE = "danmuPrice";
    public static final String COIN_NAME = "coinName";
    public static final String AT_NAME = "atName";
    public static final String LIVE_BEAN = "liveBean";
    public static final String LIVE_TYPE = "liveType";
    public static final String LIVE_KEY = "liveKey";
    public static final String LIVE_POSITION = "livePosition";
    public static final String LIVE_TYPE_VAL = "liveTypeVal";
    public static final String LIVE_UID = "liveUid";
    public static final String LIVE_STREAM = "liveStream";
    public static final String LIVE_HOME = "liveHome";
    public static final String LIVE_FOLLOW = "liveFollow";
    public static final String LIVE_NEAR = "liveNear";
    public static final String LIVE_CLASS_PREFIX = "liveClass_";
    public static final String LIVE_CLASS_RECOMMEND = "liveRecommend";
    public static final String LIVE_ADMIN_ROOM = "liveAdminRoom";
    public static final String HAS_GAME = "hasGame";
    public static final String OPEN_FLASH = "openFlash";
    public static final String SHARE_QR_CODE_FILE = "shareQrCodeFile.png";
    public static final String ANCHOR = "anchor";
    public static final String FOLLOW = "follow";
    public static final String DIAMONDS = "钻石";
    public static final String VOTES = "映票";
    public static final String SCORE = "积分";
    public static final String PAY_TYPE_ALI = "ali";
    public static final String PAY_TYPE_WX = "wx";
    public static final String PAY_TYPE_PAYPAL = "paypal";
    public static final String PAY_TYPE_BALANCE = "balance";
    public static final String PAY_BUY_COIN_ALI = "Charge.getAliOrder";
    public static final String PAY_BUY_COIN_WX = "Charge.getWxOrder";
    public static final String PAY_BUY_COIN_PAYPAL = "Charge.getBraintreePaypalOrder";

    public static final String PACKAGE_NAME_ALI = "com.eg.android.AlipayGphone";//支付宝的包名
    public static final String PACKAGE_NAME_WX = "com.tencent.mm";//微信的包名
    public static final String PACKAGE_NAME_QQ = "com.tencent.mobileqq";//QQ的包名
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String ADDRESS = "address";
    public static final String SCALE = "scale";
    public static final String SELECT_IMAGE_PATH = "selectedImagePath";
    public static final String COPY_PREFIX = "copy://";
    public static final String TEL_PREFIX = "tel://";
    public static final String AUTH_PREFIX = "auth://";
    public static final int GUARD_TYPE_NONE = 0;
    public static final int GUARD_TYPE_MONTH = 1;
    public static final int GUARD_TYPE_YEAR = 2;

    public static final String DOWNLOAD_MUSIC = "downloadMusic";
    public static final String LINK = "link";
    public static final String REPORT = "report";
    public static final String SAVE = "save";
    public static final String DELETE = "delete";
    public static final String GOODS = "goods";
    public static final String MAX_COUNT = "maxCount";
    public static final String USE_CAMERA = "useCamera";
    public static final String USE_PREVIEW = "usePreview";


    public static final int SETTING_MODIFY_PWD = 15;
    public static final int SETTING_UPDATE_ID = 16;
    public static final int SETTING_CLEAR_CACHE = 18;
    public static final int SEX_MALE = 1;
    public static final int SEX_FEMALE = 2;
    public static final int FOLLOW_FROM_FOLLOW = 1002;
    public static final int FOLLOW_FROM_FANS = 1003;
    public static final int FOLLOW_FROM_SEARCH = 1004;
    public static final String IM_FROM_HOME = "imFromHome";
    //直播房间类型
    public static final int LIVE_TYPE_NORMAL = 0;//普通房间
    public static final int LIVE_TYPE_PWD = 1;//密码房间
    public static final int LIVE_TYPE_PAY = 2;//收费房间
    public static final int LIVE_TYPE_TIME = 3;//计时房间
    //主播直播间功能
    public static final int LIVE_FUNC_BEAUTY = 2001;//美颜
    public static final int LIVE_FUNC_CAMERA = 2002;//切换摄像头
    public static final int LIVE_FUNC_FLASH = 2003;//闪光灯
    public static final int LIVE_FUNC_MUSIC = 2004;//伴奏
    public static final int LIVE_FUNC_SHARE = 2005;//分享
    public static final int LIVE_FUNC_GAME = 2006;//游戏
    public static final int LIVE_FUNC_RED_PACK = 2007;//红包
    public static final int LIVE_FUNC_LINK_MIC = 2008;//连麦
    public static final int LIVE_FUNC_MIRROR = 2009;//镜像
    public static final int LIVE_FUNC_TASK = 2010;//每日任务
    public static final int LIVE_FUNC_LUCK = 2011;//幸运奖池
    public static final int LIVE_FUNC_PAN = 2012;//转盘
    //socket
    public static final String SOCKET_CONN = "conn";
    public static final String SOCKET_BROADCAST = "broadcastingListen";
    public static final String SOCKET_SEND = "broadcast";
    public static final String SOCKET_STOP_PLAY = "stopplay";//超管关闭直播间
    public static final String SOCKET_STOP_LIVE = "stopLive";//超管关闭直播间
    public static final String SOCKET_SEND_MSG = "SendMsg";//发送文字消息，点亮，用户进房间  PS:这种混乱的设计是因为服务器端逻辑就是这样设计的,客户端无法自行修改
    public static final String SOCKET_LIGHT = "light";//飘心
    public static final String SOCKET_SEND_GIFT = "SendGift";//送礼物
    public static final String SOCKET_SEND_BARRAGE = "SendBarrage";//发弹幕
    public static final String SOCKET_LEAVE_ROOM = "disconnect";//用户离开房间
    public static final String SOCKET_LIVE_END = "StartEndLive";//主播关闭直播
    public static final String SOCKET_SYSTEM = "SystemNot";//系统消息
    public static final String SOCKET_KICK = "KickUser";//踢人
    public static final String SOCKET_SHUT_UP = "ShutUpUser";//禁言
    public static final String SOCKET_SET_ADMIN = "setAdmin";//设置或取消管理员
    public static final String SOCKET_CHANGE_LIVE = "changeLive";//切换计时收费类型
    public static final String SOCKET_UPDATE_VOTES = "updateVotes";//门票或计时收费时候更新主播的映票数
    public static final String SOCKET_FAKE_FANS = "requestFans";//僵尸粉
    public static final String SOCKET_LINK_MIC = "ConnectVideo";//连麦
    public static final String SOCKET_LINK_MIC_ANCHOR = "LiveConnect";//主播连麦
    public static final String SOCKET_LINK_MIC_PK = "LivePK";//主播PK
    public static final String SOCKET_BUY_GUARD = "BuyGuard";//购买守护
    public static final String SOCKET_RED_PACK = "SendRed";//红包
    public static final String SOCKET_LUCK_WIN = "luckWin";//幸运礼物中奖
    public static final String SOCKET_PRIZE_POOL_WIN = "jackpotWin";//奖池中奖
    public static final String SOCKET_PRIZE_POOL_UP = "jackpotUp";//奖池升级
    public static final String SOCKET_GIFT_GLOBAL = "Sendplatgift";//全站礼物
    public static final String SOCKET_LIVE_GOODS_SHOW = "goodsLiveShow";//直播间展示商品
    public static final String SOCKET_LIVE_GOODS_FLOAT = "shopGoodsLiveFloat";//直播间商品飘屏
    public static final String SOCKET_VOICE_ROOM = "voiceRoom";//语音聊天室
    //游戏socket
    public static final String SOCKET_GAME_ZJH = "startGame";//炸金花
    public static final String SOCKET_GAME_HD = "startLodumaniGame";//海盗船长
    public static final String SOCKET_GAME_NZ = "startCattleGame";//开心牛仔
    public static final String SOCKET_GAME_ZP = "startRotationGame";//幸运转盘
    public static final String SOCKET_GAME_EBB = "startShellGame";//二八贝

    public static final int SOCKET_WHAT_CONN = 0;
    public static final int SOCKET_WHAT_DISCONN = 2;
    public static final int SOCKET_WHAT_BROADCAST = 1;
    //socket 用户类型
    public static final int SOCKET_USER_TYPE_NORMAL = 30;//普通用户
    public static final int SOCKET_USER_TYPE_ADMIN = 40;//房间管理员
    public static final int SOCKET_USER_TYPE_ANCHOR = 50;//主播
    public static final int SOCKET_USER_TYPE_SUPER = 60;//超管

    //提现账号类型，1表示支付宝，2表示微信，3表示银行卡
    public static final int CASH_ACCOUNT_ALI = 1;
    public static final int CASH_ACCOUNT_WX = 2;
    public static final int CASH_ACCOUNT_BANK = 3;
    public static final String CASH_ACCOUNT_ID = "cashAccountID";
    public static final String CASH_ACCOUNT = "cashAccount";
    public static final String CASH_ACCOUNT_NAME = "cashAccountName";
    public static final String CASH_ACCOUNT_TYPE = "cashAccountType";


    public static final int RED_PACK_TYPE_AVERAGE = 0;//平均红包
    public static final int RED_PACK_TYPE_SHOU_QI = 1;//拼手气红包
    public static final int RED_PACK_SEND_TIME_NORMAL = 0;//立即发放
    public static final int RED_PACK_SEND_TIME_DELAY = 1;//延时发放

    public static final int JPUSH_TYPE_NONE = 0;
    public static final int JPUSH_TYPE_LIVE = 1;//直播
    public static final int JPUSH_TYPE_MESSAGE = 2;//消息

    public static final String VIDEO_HOME = "videoHome";
    public static final String VIDEO_USER = "videoUser_";
    public static final String VIDEO_KEY = "videoKey";
    public static final String VIDEO_POSITION = "videoPosition";
    public static final String VIDEO_SINGLE = "videoSingle";
    public static final String VIDEO_PAGE = "videoPage";
    public static final String VIDEO_BEAN = "videoBean";
    public static final String VIDEO_ID = "videoId";
    public static final String VIDEO_COMMENT_BEAN = "videoCommnetBean";
    public static final String VIDEO_FACE_OPEN = "videoOpenFace";
    public static final String VIDEO_FACE_HEIGHT = "videoFaceHeight";
    public static final String VIDEO_DURATION = "videoDuration";
    public static final String VIDEO_PATH = "videoPath";
    public static final String VIDEO_PATH_WATER = "videoPathWater";
    public static final String VIDEO_FROM_RECORD = "videoFromRecord";
    public static final String VIDEO_MUSIC_ID = "videoMusicId";
    public static final String VIDEO_HAS_BGM = "videoHasBgm";
    public static final String VIDEO_MUSIC_NAME_PREFIX = "videoMusicName_";
    public static final String VIDEO_SAVE_TYPE = "videoSaveType";
    public static final int VIDEO_SAVE_SAVE_AND_PUB = 1;//保存并发布
    public static final int VIDEO_SAVE_SAVE = 2;//仅保存
    public static final int VIDEO_SAVE_PUB = 3;//仅发布

    public static final String MOB_QQ = "qq";
    public static final String MOB_QZONE = "qzone";
    public static final String MOB_WX = "wx";
    public static final String MOB_WX_PYQ = "wchat";
    public static final String MOB_FACEBOOK = "facebook";
    public static final String MOB_TWITTER = "twitter";
    public static final String MOB_PHONE = "phone";

    public static final String LIVE_SDK = "liveSdk";
    public static final String LIVE_CONFIG = "liveConfig";
    public static final int LIVE_SDK_KSY = 0;//金山推流
    public static final int LIVE_SDK_TX = 1;//腾讯推流


    public static final int LINK_MIC_TYPE_NORMAL = 0;//观众与主播连麦
    public static final int LINK_MIC_TYPE_ANCHOR = 1;//主播与主播连麦

    public static final String HAVE_STORE = "haveStore";
    public static final String VOICE_CHAT_ROOM = "voiceChatRoom";


    public static final String MONEY_SIGN = "¥";

    public static final String CHOOSE_IMG = "chooseImage";
    public static final String CHOOSE_LOCATION = "chooseLocation";
    public static final String ACTIVE_BEAN = "activeBean";
    //动态类型
    public static final int ACTIVE_TYPE_TEXT = 0;//0：纯文字；
    public static final int ACTIVE_TYPE_IMAGE = 1;//1：文字+图片；
    public static final int ACTIVE_TYPE_VIDEO = 2;//2：文字+视频；
    public static final int ACTIVE_TYPE_VOICE = 3;//3：文字+音频


    public static final int GIFT_TYPE_NORMAL = 0;//正常礼物
    public static final int GIFT_TYPE_DAO = 1;//道具
    public static final int GIFT_TYPE_PACK = 2;//背包


    public static final String MALL_APPLY_FAILED = "mallApplyFailed";
    public static final String MALL_APPLY_MANAGE_CLASS = "mallApplyManageClass";
    public static final String MALL_APPLY_BOND = "mallApplyBond";
    public static final String MALL_GOODS_CLASS = "mallGoodsClass";
    public static final String MALL_GOODS_ID = "mallGoodsId";
    public static final String MALL_GOODS_NAME = "mallGoodsName";
    public static final String MALL_GOODS_FROM_SHOP = "mallGoodsFromShop";
    public static final String MALL_CERT_IMG = "mallCertImg";
    public static final String MALL_CERT_TEXT = "mallCertText";

    public static final String MALL_REFUND_NAME = "mallRefundName";
    public static final String MALL_REFUND_PHONE = "mallRefundPhone";
    public static final String MALL_REFUND_PROVINCE = "mallRefundProvince";
    public static final String MALL_REFUND_CITY = "mallRefundCity";
    public static final String MALL_REFUND_ZONE = "mallRefundZone";
    public static final String MALL_REFUND_ADDRESS = "mallRefundAddress";

    public static final String MALL_BUYER_ADDRESS = "mallBuyerAddress";
    public static final String MALL_GOODS_SPEC = "mallGoodsSpec";
    public static final String MALL_GOODS_COUNT = "mallGoodsCount";
    public static final String MALL_POSTAGE = "mallPostage";
    public static final String MALL_SHOP_NAME = "mallShopName";

    public static final String MALL_ORDER_ID = "mallOrderId";
    public static final String MALL_ORDER_MONEY = "mallOrderMoney";
    public static final String MALL_ORDER_INDEX = "mallOrderIndex";
    public static final String MALL_PAY_GOODS_ORDER = "Buyer.goodsOrderPay";
    public static final String MALL_PAY_CONTENT_ALI = "Paidprogram.getAliOrder";
    public static final String MALL_PAY_CONTENT_WX = "Paidprogram.getWxOrder";
    public static final String MALL_PAY_CONTENT_PAYPAL = "Paidprogram.getBraintreePaypalOrder";

    // 订单状态 -1已关闭，0待付款，1待发货，2待收货，3待评价，4已评价，5退款
    public static final int MALL_ORDER_STATUS_CLOSE = -1;
    public static final int MALL_ORDER_STATUS_WAIT_PAY = 0;
    public static final int MALL_ORDER_STATUS_WAIT_SEND = 1;
    public static final int MALL_ORDER_STATUS_WAIT_RECEIVE = 2;
    public static final int MALL_ORDER_STATUS_WAIT_COMMENT = 3;
    public static final int MALL_ORDER_STATUS_COMMENT = 4;
    public static final int MALL_ORDER_STATUS_REFUND = 5;

    public static final String MALL_CASH_BALANCE = "mallCashBalance";
    public static final String MALL_CASH_TOTAL = "mallCashTotal";
    public static final String MALL_IM_ADMIN = "goodsorder_admin";
    public static final String MALL_PLAT_UID = "1";


    public static final int VOICE_CTRL_EMPTY = 0;//无人且麦位可用
    public static final int VOICE_CTRL_BAN = 2;//无人且麦位不可用，被禁了
    public static final int VOICE_CTRL_CLOSE = -1;//有人且被关麦，无法说话
    public static final int VOICE_CTRL_OPEN = 1;//有人且正常说话
    public static final String VOICE_FACE = "voiceRoomFace";


}
