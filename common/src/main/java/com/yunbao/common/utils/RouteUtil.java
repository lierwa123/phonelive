package com.yunbao.common.utils;

import android.content.Context;
import android.content.Intent;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.CommonAppContext;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.http.HttpCallback;

/**
 * Created by cxf on 2019/2/25.
 */

public class RouteUtil {
    /**
     * 启动页
     */
    public static void forwardLauncher() {
        Intent intent = new Intent();
        intent.setClassName(CommonAppConfig.PACKAGE_NAME, "com.yunbao.phonelive.activity.LauncherActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        CommonAppContext.getInstance().startActivity(intent);
    }

    /**
     * 登录
     */
    public static void forwardLogin(String tip) {
        Intent intent = new Intent();
        intent.setClassName(CommonAppConfig.PACKAGE_NAME, "com.yunbao.main.activity.LoginActivity");
        intent.putExtra(Constants.TIP, tip);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        CommonAppContext.getInstance().startActivity(intent);
    }

    /**
     * 登录过期
     */
    public static void forwardLoginInvalid(String tip) {
        Intent intent = new Intent();
        intent.setClassName(CommonAppConfig.PACKAGE_NAME, "com.yunbao.main.activity.LoginInvalidActivity");
        intent.putExtra(Constants.TIP, tip);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonAppContext.getInstance().startActivity(intent);
    }


    /**
     * 跳转到个人主页
     */
    public static void forwardUserHome(Context context, String toUid) {
        forwardUserHome(context, toUid, false, null);
    }

    /**
     * 跳转到个人主页
     */
    public static void forwardUserHome(Context context, String toUid, boolean fromLiveRoom, String fromLiveUid) {
        Intent intent = new Intent();
        intent.setClassName(CommonAppConfig.PACKAGE_NAME, "com.yunbao.main.activity.UserHomeActivity");
        intent.putExtra(Constants.TO_UID, toUid);
        intent.putExtra(Constants.FROM_LIVE_ROOM, fromLiveRoom);
        intent.putExtra(Constants.LIVE_UID, fromLiveUid);
        context.startActivity(intent);
    }

    /**
     * 跳转到充值页面
     */
    public static void forwardMyCoin(Context context) {
        Intent intent = new Intent();
        intent.setClassName(CommonAppConfig.PACKAGE_NAME, "com.yunbao.main.activity.MyCoinActivity");
        context.startActivity(intent);
    }


    /**
     * 商品详情页面
     */
    public static void forwardGoodsDetail(final Context context, final String goodsId, final boolean fromShop, final String liveUid) {
        CommonHttpUtil.checkGoodsExist(goodsId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    Intent intent = new Intent();
                    intent.setClassName(CommonAppConfig.PACKAGE_NAME, "com.yunbao.mall.activity.GoodsDetailActivity");
                    intent.putExtra(Constants.MALL_GOODS_ID, goodsId);
                    intent.putExtra(Constants.LIVE_UID, liveUid);
                    intent.putExtra(Constants.MALL_GOODS_FROM_SHOP, fromShop);
                    context.startActivity(intent);
                } else {
                    ToastUtil.show(msg);
                }
            }
        });

    }


    /**
     * 站外商品详情页面
     */
    public static void forwardGoodsDetailOutSide(final Context context, final String goodsId, final boolean fromShop) {
        CommonHttpUtil.checkGoodsExist(goodsId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    Intent intent = new Intent();
                    intent.setClassName(CommonAppConfig.PACKAGE_NAME, "com.yunbao.mall.activity.GoodsOutSideDetailActivity");
                    intent.putExtra(Constants.MALL_GOODS_ID, goodsId);
                    intent.putExtra(Constants.LIVE_UID, "0");
                    intent.putExtra(Constants.MALL_GOODS_FROM_SHOP, fromShop);
                    context.startActivity(intent);
                } else {
                    ToastUtil.show(msg);
                }
            }
        });

    }

    /**
     * 付费内容详情页面
     */
    public static void forwardPayContentDetail(Context context, String goodsId) {
        Intent intent = new Intent();
        intent.setClassName(CommonAppConfig.PACKAGE_NAME, "com.yunbao.mall.activity.PayContentDetailActivity");
        intent.putExtra(Constants.MALL_GOODS_ID, goodsId);
        context.startActivity(intent);
    }


    /**
     * 跳转到视频播放
     */
    public static void forwardVideoPlay(Context context, String videoUrl, String coverImgUrl) {
        Intent intent = new Intent();
        intent.setClassName(CommonAppConfig.PACKAGE_NAME, "com.yunbao.main.activity.ActiveVideoPlayActivity");
        intent.putExtra(Constants.VIDEO_PATH, videoUrl);
        intent.putExtra(Constants.URL, coverImgUrl);
        context.startActivity(intent);
    }

    /**
     * 跳转到认证页面
     */
    public static void forwardAuth(AbsActivity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setClassName(CommonAppConfig.PACKAGE_NAME, "com.yunbao.main.activity.AuthActivity");
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 跳转到家族页面
     */
    public static void forwardFamily(AbsActivity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setClassName(CommonAppConfig.PACKAGE_NAME, "com.yunbao.main.activity.FamilyApplyActivity");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转到app内部的指定类名的activity
     *
     * @param context   出发的Activity
     * @param className 目标Activity的全类名
     */
    public static void forward(Context context, String className) {
        Intent intent = new Intent();
        intent.setClassName(CommonAppConfig.PACKAGE_NAME, className);
        context.startActivity(intent);
    }
}
