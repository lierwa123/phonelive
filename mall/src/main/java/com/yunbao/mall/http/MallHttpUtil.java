package com.yunbao.mall.http;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.http.HttpClient;
import com.yunbao.common.utils.MD5Util;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.mall.event.SetPlatGoodsEvent;

import org.greenrobot.eventbus.EventBus;

public class MallHttpUtil {


    /**
     * 取消网络请求
     */
    public static void cancel(String tag) {
        HttpClient.getInstance().cancel(tag);
    }


    /**
     * 获取买家首页信息
     */
    public static void getBuyerHome(HttpCallback callback) {
        HttpClient.getInstance().get("Buyer.getHome", MallHttpConsts.GET_BUYER_HOME)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 获取买家收货地址列表
     */
    public static void getBuyerAddress(HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.addressList", MallHttpConsts.GET_BUYER_ADDRESS)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 买家 新增收货地址
     */
    public static void buyerAddAddress(
            String name,
            String phoneNum,
            String province,
            String city,
            String zone,
            String address,
            String isDefault,
            HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("is_default=", isDefault, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.addAddress", MallHttpConsts.BUYER_ADD_ADDRESS)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("username", name)
                .params("phone", phoneNum)
                .params("province", province)
                .params("city", city)
                .params("area", zone)
                .params("address", address)
                .params("is_default", isDefault)
                .execute(callback);
    }

    /**
     * 买家 编辑收货地址
     */
    public static void buyerModifyAddress(
            String name,
            String phoneNum,
            String province,
            String city,
            String zone,
            String address,
            String isDefault,
            String addressId,
            HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("addressid=", addressId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.editAddress", MallHttpConsts.BUYER_MODIFY_ADDRESS)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("username", name)
                .params("phone", phoneNum)
                .params("province", province)
                .params("city", city)
                .params("area", zone)
                .params("address", address)
                .params("is_default", isDefault)
                .params("addressid", addressId)
                .execute(callback);
    }


    /**
     * 买家删除收货地址
     */
    public static void buyerDeleteAddress(String addressId, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("addressid=", addressId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.delAddress", MallHttpConsts.BUYER_DELETE_ADDRESS)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("addressid", addressId)
                .execute(callback);
    }


    /**
     * 买家 增加商品浏览记录
     */
    public static void buyerAddBrowseRecord(String goodsId) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("goodsid=", goodsId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.addGoodsVisitRecord", MallHttpConsts.BUYER_ADD_BROWSE_RECORD)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("goodsid", goodsId)
                .execute(CommonHttpUtil.NO_CALLBACK);
    }


    /**
     * 买家 删除商品浏览记录
     */
    public static void buyerDeleteBrowseRecord(String recordId, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.delGoodsVisitRecord", MallHttpConsts.BUYER_DELETE_BROWSE_RECORD)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("recordids", recordId)
                .execute(callback);
    }


    /**
     * 买家  获取商品浏览记录列表
     */
    public static void getBuyerGoodsRecord(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Buyer.getGoodsVisitRecord", MallHttpConsts.GET_BUYER_GOODS_RECORD)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("p", p)
                .execute(callback);
    }


    /**
     * 买家 购买商品 下单
     */
    public static void buyerCreateOrder(
            String addressId,
            String goodsId,
            String specId,
            int num,
            String msg,
            String liveUid,
            HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact(
                "addressid=", addressId, "&goodsid=", goodsId, "&spec_id=", specId,
                "&time=", time, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.createGoodsOrder", MallHttpConsts.BUYER_CREATE_ORDER)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("addressid", addressId)
                .params("goodsid", goodsId)
                .params("spec_id", specId)
                .params("nums", num)
                .params("message", msg)
                .params("liveuid", TextUtils.isEmpty(liveUid) ? "0" : liveUid)
                .execute(callback);
    }


    /**
     * 买家 获取付款方式列表
     */
    public static void getBuyerPayList(HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.getBalance", MallHttpConsts.GET_BUYER_PAY_LIST)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 买家 订单支付
     */
    public static void buyerPayOrder(String orderId, String payType, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("orderid=", orderId, "&time=", time, "&token=", token, "&type=", payType, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.goodsOrderPay", MallHttpConsts.BUYER_PAY_ORDER)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .params("type", payType)
                .execute(callback);
    }


    /**
     * 买家 获取订单列表
     */
    public static void getBuyerOrderList(String type, int p, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.getGoodsOrderList", MallHttpConsts.GET_BUYER_ORDER_LIST)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("type", type)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 买家 删除订单
     */
    public static void buyerDeleteOrder(String orderId, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.delGoodsOrder", MallHttpConsts.BUYER_DELETE_ORDER)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .execute(callback);
    }


    /**
     * 买家 取消订单
     */
    public static void buyerCancelOrder(String orderId, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.cancelGoodsOrder", MallHttpConsts.BUYER_CANCEL_ORDER)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .execute(callback);
    }

    /**
     * 买家 确认收货
     */
    public static void buyerConfirmReceive(String orderId, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.receiveGoodsOrder", MallHttpConsts.BUYER_CONFIRM_RECEIVE)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .execute(callback);
    }


    /**
     * 买家 获取订单详情
     */
    public static void getBuyerOrderDetail(String orderId, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.getGoodsOrderInfo", MallHttpConsts.GET_BUYER_ORDER_DETAIL)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .execute(callback);
    }


    /**
     * 买家 对商品进行评论
     *
     * @param orderId     订单id
     * @param content     评论内容
     * @param thumbs      评论图片
     * @param videoUrl    视频
     * @param videoThumb  视频封面
     * @param isAnonym    是否匿名 0否 1是
     * @param goodsStar   商品星数
     * @param sendStar    送货服务星数
     * @param serviceStar 服务态度星数
     */
    public static void buyerSetComment(
            String orderId,
            String content,
            String thumbs,
            String videoUrl,
            String videoThumb,
            String isAnonym,
            int goodsStar,
            int sendStar,
            int serviceStar,
            HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("is_anonym=", isAnonym, "&orderid=", orderId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.evaluateGoodsOrder", MallHttpConsts.BUYER_SET_COMMENT)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .params("content", content)
                .params("is_anonym", isAnonym)
                .params("thumbs", TextUtils.isEmpty(thumbs) ? "" : thumbs)
                .params("video_url", TextUtils.isEmpty(videoUrl) ? "" : videoUrl)
                .params("video_thumb", TextUtils.isEmpty(videoThumb) ? "" : videoThumb)
                .params("quality_points", goodsStar)
                .params("express_points", sendStar)
                .params("service_points", serviceStar)
                .execute(callback);
    }


    /**
     * 买家 对商品进行追评
     *
     * @param orderId    订单id
     * @param content    评论内容
     * @param thumbs     评论图片
     * @param videoUrl   视频
     * @param videoThumb 视频封面
     */
    public static void buyerAppendComment(
            String orderId,
            String content,
            String thumbs,
            String videoUrl,
            String videoThumb,
            HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("orderid=", orderId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.appendEvaluateGoodsOrder", MallHttpConsts.BUYER_APPEND_COMMENT)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .params("content", content)
                .params("thumbs", TextUtils.isEmpty(thumbs) ? "" : thumbs)
                .params("video_url", TextUtils.isEmpty(videoUrl) ? "" : videoUrl)
                .params("video_thumb", TextUtils.isEmpty(videoThumb) ? "" : videoThumb)
                .execute(callback);
    }


    /**
     * 买家 获取退款原因列表
     */
    public static void getRefundReasonList(HttpCallback callback) {
        HttpClient.getInstance().get("Buyer.getRefundReason", MallHttpConsts.GET_REFUND_REASON_LIST)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 买家 发起退款
     */
    public static void buyerApplyRefund(
            String orderId,
            String content,
            String reasonId,
            int type,
            HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("orderid=", orderId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.applyRefundGoodsOrder", MallHttpConsts.BUYER_APPLY_REFUND)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .params("content", content)
                .params("reasonid", reasonId)
                .params("type", type)
                .execute(callback);
    }


    /**
     * 买家 获取退款详情
     */
    public static void getBuyerRefundDetail(String orderId, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("orderid=", orderId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.getGoodsOrderRefundInfo", MallHttpConsts.GET_BUYER_REFUND_DETAIL)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .execute(callback);
    }


    /**
     * 买家 获取退款详情
     */
    public static void buyerCancelRefund(String orderId, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("orderid=", orderId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.cancelRefundGoodsOrder", MallHttpConsts.BUYER_CANCEL_REFUND)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .execute(callback);
    }

    /**
     * 买家 获取退款申诉理由
     */
    public static void getOfficialRefundReason(HttpCallback callback) {
        HttpClient.getInstance().get("Buyer.getPlatformReasonList", MallHttpConsts.GET_OFFICIAL_REFUND_REASON)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 买家 重新申请退款
     */
    public static void buyerApplyRefundAgain(String orderId, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("orderid=", orderId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.reapplyRefundGoodsOrder", MallHttpConsts.BUYER_APPLY_REFUND_AGAIN)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .execute(callback);
    }


    /**
     * 买家 退款申请平台介入
     */
    public static void buyerApplyOfficialRefund(String orderId, String reasonId, String content, String thumb, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("orderid=", orderId, "&reasonid=", reasonId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.applyPlatformInterpose", MallHttpConsts.BUYER_APPLY_OFFICIAL_REFUND)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .params("reasonid", reasonId)
                .params("content", content)
                .params("thumb", thumb)
                .execute(callback);
    }


    /**
     * 买家 获取账户余额和退款记录
     */
    public static void getBuyerAccountInfo(int p, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Buyer.getRefundList", MallHttpConsts.GET_BUYER_ACCOUNT_INFO)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 获取卖家店铺信息
     */
    public static void getShopHome(String toUid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.getShop", MallHttpConsts.GET_SHOP_HOME)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("touid", toUid)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 获取卖家店铺的平台商品列表
     */
    public static void getPlatShop(String toUid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.getOnsalePlatformGoods", MallHttpConsts.GET_PLAT_SHOP)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("touid", toUid)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 卖家在添加平台商品时，获取所有的平台商品列表
     */
    public static void getAddPlatGoods(int p, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Seller.getPlatformGoodsLists", MallHttpConsts.GET_ADD_PLAT_GOODS)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 卖家在添加平台商品或取消添加
     */
    public static void setPlatGoods(final String goodsid) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Seller.setPlatformGoods", MallHttpConsts.SET_PLAT_GOODS)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("goodsid", goodsid)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0 && info.length > 0) {
                            int status = JSON.parseObject(info[0]).getIntValue("status");
                            EventBus.getDefault().post(new SetPlatGoodsEvent(goodsid, status));
                        }
                        ToastUtil.show(msg);
                    }
                });
    }

    /**
     * 卖家在商品管理获取平台代卖的商品列表
     */
    public static void getManagePlatGoods(int p, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Seller.getOnsalePlatformGoods", MallHttpConsts.GET_MANAGE_PLAT_GOODS)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 获取卖家经营类目列表
     */
    public static void getManageClass(HttpCallback callback) {
        HttpClient.getInstance().get("Shop.getOneGoodsClass", MallHttpConsts.GET_MANAGE_CLASS)
                .execute(callback);
    }


    /**
     * 卖家缴纳保证金
     */
    public static void setBond(HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Shop.deductBond", MallHttpConsts.SET_BOND)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .execute(callback);
    }

    /**
     * 获取卖家是否缴纳保证金
     */
    public static void getBondStatus(HttpCallback callback) {
        HttpClient.getInstance().get("Shop.getBond", MallHttpConsts.GET_MANAGE_CLASS)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 卖家店铺申请
     *
     * @param name           姓名
     * @param cardNo         身份证号
     * @param manageClassId  经营类目
     * @param manageName     经营者名字
     * @param managePhone    经营者手机号
     * @param province       经营者所在省
     * @param city           经营者所在市
     * @param zone           经营者所在区
     * @param address        经营者详细地址
     * @param kefuPhoneNum   客服电话
     * @param refundName     退货人名字
     * @param refundPhoneNum 退货人手机号
     * @param refundProvince 退货人所在省
     * @param refundCity     退货人所在市
     * @param refundZone     退货人所在区
     * @param refundAddress  退货人者详细地址
     * @param licenseFileUrl 营业执照
     * @param otherFileUrl   其他证件
     */
    public static void applyShop(
            String name,
            String cardNo,
            String manageClassId,
            String manageName,
            String managePhone,
            String province,
            String city,
            String zone,
            String address,
            String kefuPhoneNum,
            String refundName,
            String refundPhoneNum,
            String refundProvince,
            String refundCity,
            String refundZone,
            String refundAddress,
            String licenseFileUrl,
            String otherFileUrl,
            HttpCallback callback) {
        HttpClient.getInstance().get("Shop.shopApply", MallHttpConsts.APPLY_SHOP)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("username", name)
                .params("cardno", cardNo)
                .params("classid", manageClassId)
                .params("contact", manageName)
                .params("phone", managePhone)
                .params("province", province)
                .params("city", city)
                .params("area", zone)
                .params("address", address)
                .params("service_phone", kefuPhoneNum)
                .params("receiver", refundName)
                .params("receiver_phone", refundPhoneNum)
                .params("receiver_province", refundProvince)
                .params("receiver_city", refundCity)
                .params("receiver_area", refundZone)
                .params("receiver_address", refundAddress)
                .params("certificate", licenseFileUrl)
                .params("other", otherFileUrl)
                .execute(callback);
    }


    /**
     * 获取卖家店铺的审核信息
     */
    public static void getShopApplyInfo(HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Shop.getShopApplyInfo", MallHttpConsts.GET_SHOP_APPLY_INFO)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 获取卖家首页信息
     */
    public static void getSellerHome(HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Seller.getHome", MallHttpConsts.GET_SELLER_HOME)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 获取卖家经营类目
     */
    public static void getGoodsClass(HttpCallback callback) {
        HttpClient.getInstance().get("Seller.getGoodsClass", MallHttpConsts.GET_GOODS_CLASS)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 卖家添加商品
     *
     * @param oneClassId   商品分类 一级分类ID
     * @param twoClassId   商品分类 二级分类ID
     * @param threeClassId 商品分类 三级分类ID
     * @param name         商品名称
     * @param videoUrl     视频地址，可以不传值
     * @param videoThumb   视频封面，可以不传值
     * @param thumbs       商品展示图集 英文逗号分隔
     * @param content      文字内容
     * @param pictures     商品内容图集 英文逗号分隔
     * @param specs        商品规格就json字符串
     * @param postage      邮费 0-999之间
     * @param goodsId      商品id 为空添加商品，不为空修改商品
     */
    public static void setGoods(
            String oneClassId,
            String twoClassId,
            String threeClassId,
            String name,
            String content,
            String specs,
            String postage,
            String videoUrl,
            String videoThumb,
            String thumbs,
            String pictures,
            String goodsId,
            HttpCallback callback) {
        HttpClient.getInstance().get(TextUtils.isEmpty(goodsId) ? "Seller.setGoods" : "Seller.upgoods", MallHttpConsts.SET_GOODS)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("one_classid", oneClassId)
                .params("two_classid", twoClassId)
                .params("three_classid", threeClassId)
                .params("name", name)
                .params("content", content)
                .params("specs", specs)
                .params("postage", postage)
                .params("video_url", TextUtils.isEmpty(videoUrl) ? "" : videoUrl)
                .params("video_thumb", TextUtils.isEmpty(videoThumb) ? "" : videoThumb)
                .params("thumbs", TextUtils.isEmpty(thumbs) ? "" : thumbs)
                .params("pictures", TextUtils.isEmpty(pictures) ? "" : pictures)
                .params("goodsid", goodsId)
                .execute(callback);
    }


    /**
     * 获取卖家商品管理列表
     */
    public static void getManageGoodsList(String type, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Seller.getGoodsList", MallHttpConsts.GET_MANAGE_GOODS_LIST)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("type", type)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 获取商品详情
     */
    public static void getGoodsInfo(String goodsId, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.getGoodsInfo", MallHttpConsts.GET_GOODS_INFO)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("goodsid", goodsId)
                .execute(callback);
    }


    /**
     * 获取卖家商品管理的商品数量
     */
    public static void getGoodsNum(HttpCallback callback) {
        HttpClient.getInstance().get("Seller.getGoodsNums", MallHttpConsts.GET_GOODS_NUM)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 卖家删除商品
     */
    public static void goodsDelete(String goodsId, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Seller.delGoods", MallHttpConsts.GOODS_DELETE)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("goodsid", goodsId)
                .execute(callback);
    }


    /**
     * 卖家上架/下架商品
     */
    public static void goodsUpStatus(String goodsId, int status, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("goodsid=", goodsId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Seller.upStatus", MallHttpConsts.GOODS_UP_STATUS)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("goodsid", goodsId)
                .params("status", status)
                .execute(callback);
    }

    /**
     * 卖家修改商品规格
     */
    public static void goodsModifySpec(String goodsId, String specs, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Seller.upGoodsSpecs", MallHttpConsts.GOODS_MODIFY_SPEC)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("goodsid", goodsId)
                .params("specs", specs)
                .execute(callback);
    }


    /**
     * 获取商家退货地址
     */
    public static void getRefundAddress(HttpCallback callback) {
        HttpClient.getInstance().get("Seller.getReceiverAddress", MallHttpConsts.GET_REFUND_ADDRESS)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 卖家修改退货地址
     */
    public static void modifyRefundAddress(
            String refundName,
            String refundPhoneNum,
            String refundProvince,
            String refundCity,
            String refundZone,
            String refundAddress,
            HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Seller.upReceiverAddress", MallHttpConsts.MODIFY_REFUND_ADDRESS)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("receiver", refundName)
                .params("receiver_phone", refundPhoneNum)
                .params("receiver_province", refundProvince)
                .params("receiver_city", refundCity)
                .params("receiver_area", refundZone)
                .params("receiver_address", refundAddress)
                .execute(callback);
    }

    /**
     * 卖家 获取订单列表
     */
    public static void getSellerOrderList(String type, int p, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Seller.getGoodsOrderList", MallHttpConsts.GET_SELLER_ORDER_LIST)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("type", type)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 卖家 获取订单详情
     */
    public static void getSellerOrderDetail(String orderId, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Seller.getGoodsOrderInfo", MallHttpConsts.GET_SELLER_ORDER_DETAIL)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .execute(callback);
    }


    /**
     * 卖家 获取物流公司列表
     */
    public static void getWuliuList(HttpCallback callback) {
        HttpClient.getInstance().get("seller.getExpressList", MallHttpConsts.GET_WULIU_LIST)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 卖家 发货填写物流单号
     *
     * @param orderId       订单id
     * @param wuliuId       物流公司id
     * @param wuliuOrderNum 物流运单号
     */
    public static void sellerSendOrder(String orderId, String wuliuId, String wuliuOrderNum, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("expressid=", wuliuId, "&orderid=", orderId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Seller.setExpressInfo", MallHttpConsts.SELLER_SEND_ORDER)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .params("expressid", wuliuId)
                .params("express_number", wuliuOrderNum)
                .execute(callback);
    }


    /**
     * 卖家 删除商品订单
     */
    public static void sellerDeleteOrder(String orderId, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Seller.delGoodsOrder", MallHttpConsts.SELLER_DELETE_ORDER)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .execute(callback);
    }


    /**
     * 卖家 获取退款详情
     */
    public static void getSellerRefundDetail(String orderId, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("orderid=", orderId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Seller.getGoodsOrderRefundInfo", MallHttpConsts.GET_SELLER_REFUND_DETAIL)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .execute(callback);
    }


    /**
     * 卖家 获取拒绝退款理由
     */
    public static void getRejectRefundReason(HttpCallback callback) {
        HttpClient.getInstance().get("Seller.getRefundRefuseReason", MallHttpConsts.GET_REJECT_REFUND_REASON)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 卖家 处理退款
     */
    public static void sellerSetRefund(String orderId, int type, String reasonId, String content, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("orderid=", orderId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("seller.setGoodsOrderRefund", MallHttpConsts.SELLER_SET_REFUND)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("orderid", orderId)
                .params("type", type)
                .params("reasonid", reasonId)
                .params("refuse_desc", content)
                .execute(callback);
    }


    /**
     * 获取商品评论列表
     */
    public static void getGoodsCommentList(String goodsId, String type, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.getGoodsCommentList", MallHttpConsts.GET_GOODS_COMMENT_LIST)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("goodsid", goodsId)
                .params("type", type)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 卖家 获取账户余额和账单记录
     */
    public static void getSellerAccountInfo(int p, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Seller.getSettlementList", MallHttpConsts.GET_SELLER_ACCOUNT_INFO)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 提现
     */
    public static void goodsCash(String accountId, String money, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("accountid=", accountId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("User.setShopCash", MallHttpConsts.GOODS_CASH)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("accountid", accountId)
                .params("money", money)
                .execute(callback);
    }


    /**
     * 付费内容 获取申请状态
     */
    public static void getPayApplyStatus(HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Paidprogram.getApplyStatus", MallHttpConsts.GET_PAY_APPLY_STATUS)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 付费内容 申请开通付费内容
     */
    public static void applyPayOpen(HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Paidprogram.apply", MallHttpConsts.APPLY_PAY_OPEN)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 获取付费内容分类列表
     */
    public static void getPayClassList(HttpCallback callback) {
        HttpClient.getInstance().get("Paidprogram.getPaidprogramClassList", MallHttpConsts.GET_PAY_CLASS_LIST)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 付费内容 发布付费内容
     */
    public static void publishPayContent(
            String classId,
            String title,
            String des,
            String userIntro,
            String price,
            String thumb,
            String videoJson,
            int type,
            HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("classid=", classId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Paidprogram.addPaidProgram", MallHttpConsts.PUBLISH_PAY_CONTENT)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("classid", classId)
                .params("title", title)
                .params("content", des)
                .params("personal_desc", userIntro)
                .params("money", price)
                .params("thumb", thumb)
                .params("videos", videoJson)
                .params("type", type)
                .execute(callback);
    }


    /**
     * 付费内容 获取我上传的付费内容
     */
    public static void getMyPayContentList(int p, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Paidprogram.getMyPaidProgram", MallHttpConsts.GET_MY_PAY_CONTENT_LIST)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 付费内容 获取我购买的付费内容
     */
    public static void getBuyPayContentList(int p, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Paidprogram.getPaidProgramList", MallHttpConsts.GET_BUY_PAY_CONTENT_LIST)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 付费内容 获取付费内容详情
     */
    public static void getPayContentDetail(String goodsId, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("object_id=", goodsId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Paidprogram.getPaidProgramInfo", MallHttpConsts.GET_PAY_CONTENT_DETAIL)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("object_id", goodsId)
                .execute(callback);
    }


    /**
     * 通过关键词搜索用户发布的商品列表
     */
    public static void searchGoodsList(String key, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.searchShopGoods", MallHttpConsts.SEARCH_GOODS_LIST)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("keywords", key)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 通过关键词搜索用户代卖的平台商品列表
     */
    public static void searchPlatGoodsList(String key, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.searchOnsalePlatformGoods", MallHttpConsts.SEARCH_PLAT_GOODS_LIST)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("keywords", key)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 通过关键词搜索用户发布的付费内容
     */
    public static void searchPayList(String key, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Paidprogram.searchPaidProgram", MallHttpConsts.SEARCH_PAY_LIST)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("keywords", key)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 购买付费内容 获取付款方式列表
     */
    public static void getPayContentPayList(HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Paidprogram.getBalance", MallHttpConsts.GET_PAY_CONTENT_PAY_LIST)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 余额购买付费内容
     */
    public static void buyPayContent(String goodsId, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("object_id=", goodsId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Paidprogram.balancePay", MallHttpConsts.BUY_PAY_CONTENT)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("object_id", goodsId)
                .execute(callback);
    }


    /**
     * 评价付费内容
     */
    public static void commentPayContent(String goodsId, int starCount, HttpCallback callback) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String token = appConfig.getToken();
        String sign = MD5Util.getMD5(StringUtil.contact("object_id=", goodsId, "&time=", time, "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
        HttpClient.getInstance().get("Paidprogram.setComment", MallHttpConsts.COMMENT_PAY_CONTENT)
                .params("uid", uid)
                .params("token", token)
                .params("time", time)
                .params("sign", sign)
                .params("object_id", goodsId)
                .params("grade", starCount)
                .execute(callback);
    }

    /**
     * 获取个人认证信息
     */
    public static void getUserAuthInfo(HttpCallback callback) {
        HttpClient.getInstance().get("User.getAuthInfo", MallHttpConsts.GET_USER_AUTH_INFO)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 获取IM订单消息列表
     */
    public static void getOrderMsgList(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Message.getShopOrderList", MallHttpConsts.GET_ORDER_MSG_LIST)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("p", p)
                .execute(callback);
    }


    /**
     * 添加站外商品
     *
     * @param href           外链地址
     * @param name           商品名称
     * @param original_price 商品原价
     * @param present_price  商品现价
     * @param goods_desc     商品简介
     * @param thumb          商品封面
     * @param callback
     */
    public static void setOutsideGoods(
            String goodsId,
            String href,
            String name,
            String original_price,
            String present_price,
            String goods_desc,
            String thumb,
            HttpCallback callback) {
        HttpClient.getInstance().get(TextUtils.isEmpty(goodsId) ? "Seller.setOutsideGoods" : "Seller.upOutsideGoods", MallHttpConsts.SET_OUTSIDE_GOODS)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("goodsid", goodsId)
                .params("href", href)
                .params("name", name)
                .params("original_price", original_price)
                .params("present_price", present_price)
                .params("goods_desc", goods_desc)
                .params("thumb", thumb)
                .execute(callback);
    }

    /**
     * 商品收藏或取消收藏
     */
    public static void setGoodsCollect(String goodsId, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.setCollect", MallHttpConsts.SET_GOODS_COLLECT)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("goodsid", goodsId)
                .execute(callback);
    }

    /**
     * 商品收藏列表
     */
    public static void getGoodsCollect(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.getGoodsCollect", MallHttpConsts.GET_GOODS_COLLECT)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("p", p)
                .execute(callback);
    }


    /**
     * 验证是否有正在申请的经营类目
     */
    public static void checkApplyBusinessCategory(HttpCallback callback) {
        HttpClient.getInstance().get("Shop.getApplyBusinessCategory", MallHttpConsts.CHECK_APPLY_BUSINESS_CATEGORY)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 经营类目
     */
    public static void getBusinessCategory(HttpCallback callback) {
        HttpClient.getInstance().get("Shop.getBusinessCategory", MallHttpConsts.GET_BUSINESS_CATEGORY)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 经营类目
     */
    public static void applyBusinessCategory(String classId, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.applyBusinessCategory", MallHttpConsts.APPLY_BUSINESS_CATEGORY)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("classid", classId)
                .execute(callback);
    }


}
