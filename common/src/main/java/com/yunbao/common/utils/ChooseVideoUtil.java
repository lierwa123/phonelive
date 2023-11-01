package com.yunbao.common.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.yunbao.common.CommonAppContext;
import com.yunbao.common.bean.ChooseVideoBean;
import com.yunbao.common.interfaces.CommonCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/6/20.
 */

public class ChooseVideoUtil {

    private ContentResolver mContentResolver;
    private CommonCallback<List<ChooseVideoBean>> mCallback;
    private boolean mStop;

    public ChooseVideoUtil() {
        mContentResolver = CommonAppContext.getInstance().getContentResolver();
    }

    public void getLocalVideoList(CommonCallback<List<ChooseVideoBean>> callback) {
        if (callback == null) {
            return;
        }
        mCallback = callback;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<ChooseVideoBean> videoList = getAllVideo();
                if (mCallback != null) {
                    CommonAppContext.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mCallback != null) {
                                mCallback.callback(videoList);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    private List<ChooseVideoBean> getAllVideo() {
        List<ChooseVideoBean> videoList = new ArrayList<>();
        String[] mediaColumns = new String[]{
                MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.DATE_MODIFIED
        };
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    mediaColumns, null, null, MediaStore.Video.VideoColumns.DATE_MODIFIED + " desc");
            if (cursor != null) {
                while (!mStop && cursor.moveToNext()) {
                    String videoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                    File file = new File(videoPath);
                    boolean canRead = file.canRead();
                    long length = file.length();
                    if (!canRead || length == 0) {
                        continue;
                    }
                    long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    if (duration <= 0) {
                        continue;
                    }
                    ChooseVideoBean bean = new ChooseVideoBean(ChooseVideoBean.FILE);
                    bean.setVideoFile(file);
                    bean.setDuration(duration);
                    bean.setDurationString(StringUtil.getDurationText(duration));
                    videoList.add(bean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return videoList;
    }

    public void release() {
        mStop = true;
        mCallback = null;
    }


}
