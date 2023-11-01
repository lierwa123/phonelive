package com.yunbao.beauty.constant;

import android.os.Environment;

import com.yunbao.beauty.utils.MhDataManager;

public class Constants {

    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String DIR_NAME = "meihu";
    public static final String PAYLOAD = "payload";
    public static final String INNER_PATH = MhDataManager.getInstance().getContext().getFilesDir().getAbsolutePath();


    public static final String WATERMARK_ASSETS_FORDERNAME = "watermark";
    public static final String WATERMARK_ICON_FORDERNAME = "imgicons";
    public static final String WATERMARK_RES_FORDERNAME = "imgres";
    public static final String VIDEO_TIE_ZHI_RESOURCE_ZIP_PATH = ROOT_PATH + "/" + DIR_NAME + "/tieZhi/";

    public static final String TIEZHI_ACTION_NAME = "tieZhiActionName";

}
