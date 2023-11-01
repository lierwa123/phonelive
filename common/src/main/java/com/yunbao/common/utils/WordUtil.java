package com.yunbao.common.utils;

import android.content.res.Resources;

import com.yunbao.common.CommonAppContext;

/**
 * Created by cxf on 2017/10/10.
 * 获取string.xml中的字
 */

public class WordUtil {

    private static Resources sResources;

    static {
        sResources = CommonAppContext.getInstance().getResources();
    }

    public static String getString(int res) {
        if(res==0){
            return "";
        }
        return sResources.getString(res);
    }

}
