package com.yunbao.common.upload;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.http.CommonHttpConsts;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.CommonCallback;

public class UploadUtil {

    private static UploadStrategy sStrategy;

    public static void startUpload(final CommonCallback<UploadStrategy> commonCallback) {
        CommonHttpUtil.getUploadInfo(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String cloudType = obj.getString("cloudtype");
                    if ("qiniu".equals(cloudType)) {//七牛云存储
                        JSONObject qiniuInfo = obj.getJSONObject("qiniuInfo");
                        sStrategy = new UploadQnImpl(qiniuInfo.getString("qiniuToken"), qiniuInfo.getString("qiniu_zone"), cloudType);
                        if (commonCallback != null) {
                            commonCallback.callback(sStrategy);
                        }
                    } else if ("aws".equals(cloudType)) {//亚马逊存储
                        JSONObject awsInfo = obj.getJSONObject("awsInfo");
                        sStrategy = new AwsUploadImpl(awsInfo.getString("aws_region"), awsInfo.getString("aws_identitypoolid"), awsInfo.getString("aws_bucket"), cloudType);
                        if (commonCallback != null) {
                            commonCallback.callback(sStrategy);
                        }
                    }
                }
            }
        });
    }

    public static void cancelUpload() {
        CommonHttpUtil.cancel(CommonHttpConsts.GET_UPLOAD_INFO);
        if (sStrategy != null) {
            sStrategy.cancelUpload();
        }
    }
}
