package com.yunbao.im.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.makeramen.roundedimageview.RoundedImageView;
import com.yunbao.common.custom.ZoomView;
import com.yunbao.im.bean.ImMessageBean;

import java.io.File;

/**
 * Created by cxf on 2018/6/7.
 */

public class MyImageView2 extends ZoomView {

    private File mFile;
    private ImMessageBean mImMessageBean;

    public MyImageView2(Context context) {
        super(context);
    }

    public MyImageView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        mFile = file;
    }

    public ImMessageBean getImMessageBean() {
        return mImMessageBean;
    }

    public void setImMessageBean(ImMessageBean imMessageBean) {
        mImMessageBean = imMessageBean;
    }
}
