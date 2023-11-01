package com.yunbao.live.floatwindow;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.WindowManager;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.CommonAppContext;

import java.lang.reflect.Method;

/**
 * 悬浮窗权限判断工具
 */
public class FloatWindowPermission {

    public static FloatWindowPermission sInstance;

    private FloatWindowPermission() {

    }

    public static FloatWindowPermission getInstance() {
        if (sInstance == null) {
            synchronized (FloatWindowPermission.class) {
                if (sInstance == null) {
                    sInstance = new FloatWindowPermission();
                }
            }
        }
        return sInstance;
    }


    public void requestPermission() {
        if (hasPermission()) {
            FloatWindowUtil.getInstance().show();
        } else {
            if (Miui.rom()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    req();
                } else {
                    Miui.req(CommonAppContext.getInstance());
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    req();
                } else {
                    FloatWindowUtil.getInstance().show();
                }
            }
        }

    }

    private void req() {
        Intent intent = new Intent(CommonAppContext.getInstance(), FloatPermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonAppContext.getInstance().startActivity(intent);
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(CommonAppContext.getInstance());
        } else {
            return hasPermissionBelowMarshmallow();
        }
    }

    public boolean hasPermissionOnActivityResult(Context context) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            return hasPermissionForO(context);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(CommonAppContext.getInstance());
        } else {
            return hasPermissionBelowMarshmallow();
        }
    }

    /**
     * 6.0以下判断是否有权限
     * 理论上6.0以上才需处理权限，但有的国内rom在6.0以下就添加了权限
     * 其实此方式也可以用于判断6.0以上版本，只不过有更简单的canDrawOverlays代替
     */
    private boolean hasPermissionBelowMarshmallow() {
        try {
            AppOpsManager manager = (AppOpsManager) CommonAppContext.getInstance().getSystemService(Context.APP_OPS_SERVICE);
            Method dispatchMethod = AppOpsManager.class.getMethod("checkOp", int.class, int.class, String.class);
            //AppOpsManager.OP_SYSTEM_ALERT_WINDOW = 24
            return AppOpsManager.MODE_ALLOWED == (Integer) dispatchMethod.invoke(
                    manager, 24, Binder.getCallingUid(), CommonAppConfig.PACKAGE_NAME);
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 用于判断8.0时是否有权限，仅用于OnActivityResult
     * 针对8.0官方bug:在用户授予权限后Settings.canDrawOverlays或checkOp方法判断仍然返回false
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermissionForO(Context context) {
        try {
            WindowManager mgr = (WindowManager) CommonAppContext.getInstance().getSystemService(Context.WINDOW_SERVICE);
            if (mgr == null) return false;
            View viewToAdd = new View(context);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(0, 0,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT);
            viewToAdd.setLayoutParams(params);
            mgr.addView(viewToAdd, params);
            mgr.removeView(viewToAdd);
            return true;
        } catch (Exception e) {

        }
        return false;
    }


}
