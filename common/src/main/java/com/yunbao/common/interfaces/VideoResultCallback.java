package com.yunbao.common.interfaces;

import java.io.File;

/**
 * Created by cxf on 2018/9/29.
 */

public interface VideoResultCallback {

    void onSuccess(File file,long duration);

    void onFailure();
}
