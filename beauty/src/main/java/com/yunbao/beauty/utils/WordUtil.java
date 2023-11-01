package com.yunbao.beauty.utils;

import android.content.Context;

/**
 * Created by cxf on 2017/10/10.
 * 获取string.xml中的字
 */

public class WordUtil {


    public static String getString(Context context, int res) {
//        if(res==0){
//            return "";
//        }
//        return context.getResources().getString(res);

        return com.yunbao.common.utils.WordUtil.getString(res);
    }


//    public static String getString(Context context,int res,Object...formatArgs) {
//        if(res==0){
//            return "";
//        }
//        return context.getResources().getString(res,formatArgs);
//    }

}
