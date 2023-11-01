package com.yunbao.common.dialog;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.R;

/**
 * Created by cxf on 2018/11/28.
 * 视频预览弹窗
 */

public class ActiveVideoPreviewDialog extends AbsDialogFragment implements View.OnClickListener, ITXLivePlayListener {

    private TXCloudVideoView mTXCloudVideoView;
    private TXVodPlayer mPlayer;
    private String mVideoPath;
    private ObjectAnimator mPlayBtnAnimator;//暂停按钮的动画
    private View mPlayBtn;
    private boolean mPlayStarted;//播放是否开始了
    private boolean mPaused;//生命周期暂停
    private boolean mClickPaused;//点击暂停
//    private boolean mVideoFileFromRecord;
    private ActionListener mActionListener;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_active_video_preview;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.video_container).setOnClickListener(this);
        mPlayBtn = findViewById(R.id.btn_play);
        //暂停按钮动画
        mPlayBtnAnimator = ObjectAnimator.ofPropertyValuesHolder(mPlayBtn,
                PropertyValuesHolder.ofFloat("scaleX", 4f, 0.8f, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 4f, 0.8f, 1f),
                PropertyValuesHolder.ofFloat("alpha", 0f, 1f));
        mPlayBtnAnimator.setDuration(150);
        mPlayBtnAnimator.setInterpolator(new AccelerateInterpolator());

        mTXCloudVideoView = findViewById(R.id.video_view);
        mPlayer = new TXVodPlayer(mContext);
        mPlayer.setConfig(new TXVodPlayConfig());
        mPlayer.setPlayerView(mTXCloudVideoView);
        mPlayer.enableHardwareDecode(false);
        mPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mPlayer.setPlayListener(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mVideoPath = bundle.getString(Constants.VIDEO_PATH);
            if (!TextUtils.isEmpty(mVideoPath)) {
//                mVideoFileFromRecord = mVideoPath.contains(CommonAppConfig.VIDEO_PATH_RECORD);
                int result = mPlayer.startPlay(mVideoPath);
                if (result == 0) {
                    mPlayStarted = true;
                }
            }
        }
    }


    @Override
    public void onPlayEvent(int e, Bundle bundle) {
        switch (e) {
            case TXLiveConstants.PLAY_EVT_PLAY_END://播放结束
                onReplay();
                break;
            case TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION:
                //onVideoSizeChanged(bundle.getInt("EVT_PARAM1", 0), bundle.getInt("EVT_PARAM2", 0));
                onVideoSizeChanged();
                break;
        }
    }


    @Override
    public void onNetStatus(Bundle bundle) {

    }

    /**
     * 获取到视频宽高回调
     */
    public void onVideoSizeChanged() {
        double videoWidth = 0;
        double videoHeight = 0;
        MediaMetadataRetriever mmr = null;
        try {
            mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mVideoPath);
            String width = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);//宽
            String height = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);//高
            String rotation = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            if ("0".equals(rotation)) {
                if (!TextUtils.isEmpty(width)) {
                    videoWidth = Double.parseDouble(width);
                }
                if (!TextUtils.isEmpty(height)) {
                    videoHeight = Double.parseDouble(height);
                }
            } else {
                if (!TextUtils.isEmpty(height)) {
                    videoWidth = Double.parseDouble(height);
                }
                if (!TextUtils.isEmpty(width)) {
                    videoHeight = Double.parseDouble(width);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mmr != null) {
                mmr.release();
            }
        }
        if (mTXCloudVideoView != null && videoWidth > 0 && videoHeight > 0) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mTXCloudVideoView.getLayoutParams();
            if (videoWidth / videoHeight > 0.5625f) {//横屏 9:16=0.5625
                params.height = (int) (mTXCloudVideoView.getWidth() / videoWidth * videoHeight);
                params.gravity = Gravity.CENTER;
                mTXCloudVideoView.requestLayout();
            }
        }
    }

    /**
     * 获取到视频宽高回调
     */
    public void onVideoSizeChanged(float videoWidth, float videoHeight) {
        if (mTXCloudVideoView != null && videoWidth > 0 && videoHeight > 0) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mTXCloudVideoView.getLayoutParams();
            if (videoWidth / videoHeight > 0.5625f) {//横屏 9:16=0.5625
                params.height = (int) (mTXCloudVideoView.getWidth() / videoWidth * videoHeight);
                params.gravity = Gravity.CENTER;
                mTXCloudVideoView.requestLayout();
            }
        }
    }

    /**
     * 显示开始播放按钮
     */
    private void showPlayBtn() {
        if (mPlayBtn != null && mPlayBtn.getVisibility() != View.VISIBLE) {
            mPlayBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏开始播放按钮
     */
    private void hidePlayBtn() {
        if (mPlayBtn != null && mPlayBtn.getVisibility() == View.VISIBLE) {
            mPlayBtn.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 点击切换播放和暂停
     */
    private void clickTogglePlay() {
        if (!mPlayStarted) {
            return;
        }
        if (mPlayer != null) {
            if (mClickPaused) {
                mPlayer.resume();
            } else {
                mPlayer.pause();
            }
        }
        mClickPaused = !mClickPaused;
        if (mClickPaused) {
            showPlayBtn();
            if (mPlayBtnAnimator != null) {
                mPlayBtnAnimator.start();
            }
        } else {
            hidePlayBtn();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        mPaused = true;
        if (!mClickPaused && mPlayer != null) {
            mPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPaused) {
            if (!mClickPaused && mPlayer != null) {
                mPlayer.resume();
            }
        }
        mPaused = false;
    }


    /**
     * 循环播放
     */
    private void onReplay() {
        if (mPlayStarted && mPlayer != null) {
            mPlayer.seek(0);
            mPlayer.resume();
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_close) {
            dismiss();
        } else if (i == R.id.btn_delete) {
            if (mActionListener != null) {
                mActionListener.onDeleteClick();
            }
            dismiss();
        } else if (i == R.id.video_container) {
            clickTogglePlay();
        }
    }

    @Override
    public void onDestroy() {
        if (mPlayer != null) {
            mPlayer.stopPlay(false);
            mPlayer.setPlayListener(null);
        }
        mContext = null;
        mPlayer = null;
        mActionListener = null;
        super.onDestroy();
    }


    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface ActionListener {
        void onDeleteClick();
    }

}
