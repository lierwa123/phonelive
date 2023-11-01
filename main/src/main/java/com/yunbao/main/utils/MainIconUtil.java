package com.yunbao.main.utils;

import android.util.SparseIntArray;

import com.yunbao.common.Constants;
import com.yunbao.main.R;

/**
 * Created by cxf on 2018/10/11.
 */

public class MainIconUtil {
    private static SparseIntArray sLiveTypeMap;//直播间类型图标

    static {
        sLiveTypeMap = new SparseIntArray();
        sLiveTypeMap.put(Constants.LIVE_TYPE_NORMAL, R.mipmap.icon_main_live_type_0);
        sLiveTypeMap.put(Constants.LIVE_TYPE_PWD, R.mipmap.icon_main_live_type_1);
        sLiveTypeMap.put(Constants.LIVE_TYPE_PAY, R.mipmap.icon_main_live_type_2);
        sLiveTypeMap.put(Constants.LIVE_TYPE_TIME, R.mipmap.icon_main_live_type_3);

    }

    public static int getLiveTypeIcon(int key) {
        return sLiveTypeMap.get(key);
    }


}
