package com.yunbao.common.interfaces;

import java.util.HashMap;

public abstract class PermissionCallback {

    public abstract void onAllGranted();

    public void onResult(HashMap<String, Boolean> resultMap) {

    }
}
