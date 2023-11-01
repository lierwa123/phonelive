package com.yunbao.common.interfaces;

import java.util.ArrayList;
import java.util.List;

public class AppLifecycleUtil {

    private static final List<LifecycleCallback> sList;

    static {
        sList = new ArrayList<>();
    }

    public static void addLifecycleCallback(LifecycleCallback callback) {
        if (sList != null) {
            sList.add(callback);
        }
    }

    public static void removeLifecycleCallback(LifecycleCallback callback) {
        if (sList != null) {
            sList.remove(callback);
        }
    }

    /**
     * 处于前台
     */
    public static void onAppFrontGround() {
        if (sList != null && sList.size() > 0) {
            for (LifecycleCallback callback : sList) {
                callback.onAppFrontGround();
            }
        }
    }

    /**
     * 处于后台
     */
    public static void onAppBackGround() {
        if (sList != null && sList.size() > 0) {
            for (LifecycleCallback callback : sList) {
                callback.onAppBackGround();
            }
        }
    }


    public interface LifecycleCallback {

        /**
         * 处于前台
         */
        void onAppFrontGround();

        /**
         * 处于后台
         */
        void onAppBackGround();

    }

}
