package com.yunbao.common.upload;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.CommonAppContext;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.StringUtil;

import java.io.File;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;

/**
 * 亚马逊存储
 * Created by Sky.L on 2020-12-22
 */
public class AwsUploadImpl implements UploadStrategy {

    private static final String TAG = "awsUploadImpl";
    private List<UploadBean> mList;
    private int mIndex;
    private boolean mNeedCompress;
    private UploadCallback mUploadCallback;
    private Luban.Builder mLubanBuilder;
    private TransferUtility mTransferUtility;
    private String mBucketName;
    private TransferListener mTransferListener;
    private String mPrefix;

    public AwsUploadImpl(String region, String poolId, String bucketName, String prefix) {
        mBucketName = bucketName;
        mPrefix = prefix + "_";
        mTransferUtility = new AWSTransferUtil().getTransferUtility(CommonAppContext.getInstance(), region, poolId);
        mTransferListener = new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    if (mList == null || mList.size() == 0) {
                        if (mUploadCallback != null) {
                            mUploadCallback.onFinish(mList, false);
                        }
                        return;
                    }
                    L.e(TAG, "onStateChanged-----上传ok---->");
                    UploadBean uploadBean = mList.get(mIndex);
                    uploadBean.setSuccess(true);
                    uploadBean.setRemoteFileName(StringUtil.contact(mPrefix, uploadBean.getRemoteFileName()));
                    if (uploadBean.getType() == UploadBean.IMG && mNeedCompress) {
                        //上传完成后把 压缩后的图片 删掉
                        File compressedFile = uploadBean.getCompressFile();
                        if (compressedFile != null && compressedFile.exists()) {
                            File originFile = uploadBean.getOriginFile();
                            if (originFile != null && !compressedFile.getAbsolutePath().equals(originFile.getAbsolutePath())) {
                                compressedFile.delete();
                            }
                        }
                    }
                    mIndex++;
                    if (mIndex < mList.size()) {
                        uploadNext();
                    } else {
                        if (mUploadCallback != null) {
                            mUploadCallback.onFinish(mList, true);
                        }
                    }
                }
//                else if (TransferState.FAILED == state) {
//                    upload(mList.get(mIndex));//上传失败后 重新上传
//                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                L.e(TAG, "onProgressChanged-----上传进度---->");
            }

            @Override
            public void onError(int id, Exception ex) {
                L.e(TAG, "onError-----上传失败---->");
            }
        };

    }

    @Override
    public void upload(List<UploadBean> list, boolean needCompress, UploadCallback callback) {
        if (callback == null) {
            return;
        }
        if (list == null || list.size() == 0) {
            callback.onFinish(list, false);
            return;
        }
        boolean hasFile = false;
        for (UploadBean bean : list) {
            if (bean.getOriginFile() != null) {
                hasFile = true;
                break;
            }
        }
        if (!hasFile) {
            callback.onFinish(list, true);
            return;
        }
        mList = list;
        mNeedCompress = needCompress;
        mUploadCallback = callback;
        mIndex = 0;
        uploadNext();

    }

    @Override
    public void cancelUpload() {
        if (mTransferUtility != null) {
            mTransferUtility.cancelAllWithType(TransferType.UPLOAD);
        }
        if (mList != null) {
            mList.clear();
        }
        mUploadCallback = null;
    }

    private void uploadNext() {
        UploadBean bean = null;
        while (mIndex < mList.size() && (bean = mList.get(mIndex)).getOriginFile() == null) {
            mIndex++;
        }
        if (mIndex >= mList.size()) {
            if (mUploadCallback != null) {
                mUploadCallback.onFinish(mList, true);
            }
            return;
        }
        if (bean.getType() == UploadBean.IMG) {
            bean.setRemoteFileName(StringUtil.contact(StringUtil.generateFileName(), ".jpg"));
        } else if (bean.getType() == UploadBean.VIDEO) {
            bean.setRemoteFileName(StringUtil.contact(StringUtil.generateFileName(), ".mp4"));
        } else if (bean.getType() == UploadBean.VOICE) {
            bean.setRemoteFileName(StringUtil.contact(StringUtil.generateFileName(), ".m4a"));
        }
        if (bean.getType() == UploadBean.IMG && mNeedCompress) {
            if (mLubanBuilder == null) {
                mLubanBuilder = Luban.with(CommonAppContext.getInstance())
                        .ignoreBy(8)//8k以下不压缩
                        .setTargetDir(CommonAppConfig.INNER_PATH)
                        .setRenameListener(new OnRenameListener() {
                            @Override
                            public String rename(String filePath) {
                                return mList.get(mIndex).getRemoteFileName();
                            }
                        }).setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(File file) {
                                UploadBean uploadBean = mList.get(mIndex);
                                uploadBean.setCompressFile(file);
                                upload(uploadBean);
                            }

                            @Override
                            public void onError(Throwable e) {
                                upload(mList.get(mIndex));
                            }
                        });
            }
            mLubanBuilder.load(bean.getOriginFile()).launch();
        } else {
            upload(bean);
        }
    }

    private void upload(UploadBean bean) {
        if (bean != null && mTransferUtility != null && mTransferListener != null) {
            File uploadFile = bean.getOriginFile();
            if (bean.getType() == UploadBean.IMG && mNeedCompress) {
                File compressedFile = bean.getCompressFile();
                if (compressedFile != null && compressedFile.exists()) {
                    uploadFile = compressedFile;
                }
            }
            try {
                TransferObserver observer = mTransferUtility.upload(mBucketName, bean.getRemoteFileName(), uploadFile);
                observer.setTransferListener(mTransferListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mUploadCallback != null) {
                mUploadCallback.onFinish(mList, false);
            }
        }
    }

}
