package com.yunbao.video.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.HtmlConfig;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.bean.ConfigBean;
import com.yunbao.common.http.CommonHttpConsts;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.ActivityResultCallback;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.mob.MobShareUtil;
import com.yunbao.common.mob.ShareData;
import com.yunbao.common.upload.UploadBean;
import com.yunbao.common.upload.UploadCallback;
import com.yunbao.common.upload.UploadStrategy;
import com.yunbao.common.upload.UploadUtil;
import com.yunbao.common.utils.ActivityResultUtil;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.video.R;
import com.yunbao.video.adapter.VideoPubShareAdapter;
import com.yunbao.video.http.VideoHttpConsts;
import com.yunbao.video.http.VideoHttpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;

/**
 * Created by cxf on 2018/12/10.
 * 视频发布
 */

public class VideoPublishActivity extends AbsActivity implements ITXVodPlayListener, View.OnClickListener {

    public static void forward(Context context, String videoPath, String videoWaterPath, int saveType, int musicId) {
        Intent intent = new Intent(context, VideoPublishActivity.class);
        intent.putExtra(Constants.VIDEO_PATH, videoPath);
        intent.putExtra(Constants.VIDEO_PATH_WATER, videoWaterPath);
        intent.putExtra(Constants.VIDEO_SAVE_TYPE, saveType);
        intent.putExtra(Constants.VIDEO_MUSIC_ID, musicId);
        context.startActivity(intent);
    }

    private static final String TAG = "VideoPublishActivity";
    private TextView mNum;
    private TextView mLocation;
    private TXCloudVideoView mTXCloudVideoView;
    private TXVodPlayer mPlayer;
    private String mVideoPath;
    private String mVideoPathWater;
    private boolean mPlayStarted;//播放是否开始了
    private boolean mPaused;//生命周期暂停
    private RecyclerView mRecyclerView;
    private ConfigBean mConfigBean;
    private VideoPubShareAdapter mAdapter;
    private EditText mInput;
    private String mVideoTitle;//视频标题
    private Dialog mLoading;
    private MobShareUtil mMobShareUtil;
    private int mSaveType;
    private int mMusicId;
    private View mBtnPub;
    private CheckBox mCheckBox;
    private TextView mGoodsName;
    private View mBtnGoodsAdd;
    private TextView mVideoClassName;
    private int mVideoClassId;
    private int mGoodsType;//type  绑定的内容类型 0 没绑定 1 商品 2 付费内容
    private String mGoodsId;//绑定的内容Id
    private double mVideoRatio;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_publish;
    }

    @Override
    protected void main() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setTitle(WordUtil.getString(R.string.video_pub));
        Intent intent = getIntent();
        mVideoPath = intent.getStringExtra(Constants.VIDEO_PATH);
        mVideoPathWater = intent.getStringExtra(Constants.VIDEO_PATH_WATER);
        mSaveType = intent.getIntExtra(Constants.VIDEO_SAVE_TYPE, Constants.VIDEO_SAVE_SAVE_AND_PUB);
        if (TextUtils.isEmpty(mVideoPath)) {
            return;
        }
        mMusicId = intent.getIntExtra(Constants.VIDEO_MUSIC_ID, 0);
        mBtnPub = findViewById(R.id.btn_pub);
        mBtnPub.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        CommonAppConfig.getInstance().getConfig(new CommonCallback<ConfigBean>() {
            @Override
            public void callback(ConfigBean bean) {
                mConfigBean = bean;
                if (mRecyclerView != null) {
                    mAdapter = new VideoPubShareAdapter(mContext, bean);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });
        mNum = (TextView) findViewById(R.id.num);
        mInput = (EditText) findViewById(R.id.input);
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mNum != null) {
                    mNum.setText(s.length() + "/50");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mLocation = findViewById(R.id.location);
        mLocation.setText(CommonAppConfig.getInstance().getCity());
        mCheckBox = findViewById(R.id.checkbox);
        mCheckBox.setOnClickListener(this);
        mBtnGoodsAdd = findViewById(R.id.btn_goods_add);
        mVideoClassName = findViewById(R.id.video_class_name);
        mGoodsName = findViewById(R.id.goods_name);
        findViewById(R.id.btn_video_class).setOnClickListener(this);

        mTXCloudVideoView = findViewById(R.id.video_view);
        mPlayer = new TXVodPlayer(mContext);
        mPlayer.setConfig(new TXVodPlayConfig());
        mPlayer.setPlayerView(mTXCloudVideoView);
        mPlayer.enableHardwareDecode(false);
        mPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mPlayer.setVodListener(this);
        mPlayer.setLoop(true);
        int result = mPlayer.startPlay(mVideoPath);
        if (result == 0) {
            mPlayStarted = true;
        }

        VideoHttpUtil.getConcatGoods(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    int isShop = JSON.parseObject(info[0]).getIntValue("isshop");
                    if (isShop == 1) {
                        if (mBtnGoodsAdd != null) {
                            mBtnGoodsAdd.setVisibility(View.VISIBLE);
                            mBtnGoodsAdd.setOnClickListener(VideoPublishActivity.this);
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onPlayEvent(TXVodPlayer txVodPlayer, int e, Bundle bundle) {
        switch (e) {
            case TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION:
                onVideoSizeChanged(bundle.getInt("EVT_PARAM1", 0), bundle.getInt("EVT_PARAM2", 0));
                break;
        }
    }

    @Override
    public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

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


    @Override
    protected void onPause() {
        super.onPause();
        mPaused = true;
        if (mPlayStarted && mPlayer != null) {
            mPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPaused && mPlayStarted && mPlayer != null) {
            mPlayer.resume();
        }
        mPaused = false;
    }

    public void release() {
        CommonHttpUtil.cancel(CommonHttpConsts.GET_CONFIG);
        VideoHttpUtil.cancel(VideoHttpConsts.GET_CONCAT_GOODS);
        VideoHttpUtil.cancel(VideoHttpConsts.SAVE_UPLOAD_VIDEO_INFO);
        mPlayStarted = false;
        if (mPlayer != null) {
            mPlayer.stopPlay(false);
            mPlayer.setPlayListener(null);
        }
        if (mMobShareUtil != null) {
            mMobShareUtil.release();
        }
        mPlayer = null;
        mMobShareUtil = null;
        UploadUtil.cancelUpload();
    }

    @Override
    public void onBackPressed() {
        DialogUitl.showSimpleDialog(mContext, WordUtil.getString(R.string.video_give_up_pub), new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(Dialog dialog, String content) {
                if (mSaveType == Constants.VIDEO_SAVE_PUB) {
                    if (!TextUtils.isEmpty(mVideoPath)) {
                        File file = new File(mVideoPath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    if (!TextUtils.isEmpty(mVideoPathWater)) {
                        File file = new File(mVideoPathWater);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
                release();
                VideoPublishActivity.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_pub) {
            publishVideo();
        } else if (i == R.id.checkbox) {
            clickCheckBox();
        } else if (i == R.id.btn_goods_add) {
            searchMallGoods();
        } else if (i == R.id.btn_video_class) {
            chooseVideoClass();
        }
    }

    /**
     * 添加商品
     */
    private void searchMallGoods() {
        Intent i = new Intent();
        i.setClassName(CommonAppConfig.PACKAGE_NAME, "com.yunbao.mall.activity.GoodsSearchActivity");
        ActivityResultUtil.startActivityForResult(this, i, new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent != null) {
                    mGoodsId = intent.getStringExtra(Constants.MALL_GOODS_ID);
                    mGoodsType = intent.getIntExtra(Constants.LIVE_TYPE, 0);
                    String goodsName = intent.getStringExtra(Constants.MALL_GOODS_NAME);
                    mGoodsName.setText(goodsName);
                }
            }
        });
    }

    /**
     * 选择视频分类
     */
    private void chooseVideoClass() {
        Intent i = new Intent(mContext, VideoChooseClassActivity.class);
        i.putExtra(Constants.VIDEO_ID, mVideoClassId);
        ActivityResultUtil.startActivityForResult(this, i, new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent != null) {
                    mVideoClassId = intent.getIntExtra(Constants.VIDEO_ID, 0);
                    if (mVideoClassName != null) {
                        mVideoClassName.setText(intent.getStringExtra(Constants.CLASS_NAME));
                    }
                }
            }
        });
    }


    private void clickCheckBox() {
        if (mCheckBox == null || mLocation == null) {
            return;
        }
        if (mCheckBox.isChecked()) {
            mLocation.setEnabled(true);
            mLocation.setText(CommonAppConfig.getInstance().getCity());
        } else {
            mLocation.setEnabled(false);
            mLocation.setText(WordUtil.getString(R.string.mars));
        }
    }



    /**
     * 发布视频
     */
    private void publishVideo() {
        if (mConfigBean == null || TextUtils.isEmpty(mVideoPath)) {
            return;
        }
        if (mVideoClassId == 0) {
            ToastUtil.show(R.string.video_choose_class_2);
            return;
        }
        mBtnPub.setEnabled(false);
        mVideoTitle = mInput.getText().toString().trim();
        mLoading = DialogUitl.loadingDialog(mContext, WordUtil.getString(R.string.video_pub_ing));
        mLoading.show();
        mVideoRatio = 0;
        Bitmap bitmap = null;
        //生成视频封面图
        MediaMetadataRetriever mmr = null;
        try {
            mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mVideoPath);
            bitmap = mmr.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST);
            String width = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);//宽
            String height = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);//高
            String rotation = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            double videoWidth = 0;
            double videoHeight = 0;
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
            if (videoHeight != 0 && videoWidth != 0) {
                mVideoRatio = videoHeight / videoWidth;
            }
        } catch (Exception e) {
            bitmap = null;
            e.printStackTrace();
        } finally {
            if (mmr != null) {
                mmr.release();
            }
        }
        if (bitmap == null) {
            ToastUtil.show(R.string.video_cover_img_failed);
            onFailed();
            return;
        }
        final String coverImagePath = mVideoPath.replace(".mp4", ".jpg");
        File imageFile = new File(coverImagePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            imageFile = null;
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (bitmap != null) {
            bitmap.recycle();
        }
        if (imageFile == null) {
            ToastUtil.show(R.string.video_cover_img_failed);
            onFailed();
            return;
        }
        final File finalImageFile = imageFile;
        //用鲁班压缩图片
        Luban.with(this)
                .load(finalImageFile)
                .setFocusAlpha(false)
                .ignoreBy(8)//8k以下不压缩
                .setTargetDir(CommonAppConfig.VIDEO_PATH)
                .setRenameListener(new OnRenameListener() {
                    @Override
                    public String rename(String filePath) {
                        filePath = filePath.substring(filePath.lastIndexOf("/") + 1);
                        return filePath.replace(".jpg", "_c.jpg");
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        if (!finalImageFile.getAbsolutePath().equals(file.getAbsolutePath()) && finalImageFile.exists()) {
                            finalImageFile.delete();
                        }
                        uploadVideoFile(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        uploadVideoFile(finalImageFile);
                    }
                }).launch();
    }

    private void onFailed() {
        if (mLoading != null) {
            mLoading.dismiss();
        }
        if (mBtnPub != null) {
            mBtnPub.setEnabled(true);
        }
    }

    /**
     * 上传封面图片
     */
    private void uploadVideoFile(File imageFile) {
        final List<UploadBean> list = new ArrayList<>();
        list.add(new UploadBean(imageFile, UploadBean.IMG));
        list.add(new UploadBean(new File(mVideoPath), UploadBean.VIDEO));
        if (!TextUtils.isEmpty(mVideoPathWater)) {
            File waterFile = new File(mVideoPathWater);
            if (waterFile.exists()) {
                list.add(new UploadBean(waterFile, UploadBean.VIDEO));
            }
        }
        UploadUtil.startUpload(new CommonCallback<UploadStrategy>() {
            @Override
            public void callback(UploadStrategy strategy) {
                strategy.upload(list, false, new UploadCallback() {
                    @Override
                    public void onFinish(List<UploadBean> list, boolean success) {
                        if (success) {
                            if (mSaveType == Constants.VIDEO_SAVE_PUB) {
                                File imageFile = list.get(0).getOriginFile();
                                if (imageFile != null && imageFile.exists()) {
                                    imageFile.delete();
                                }
                                File videoFile = list.get(1).getOriginFile();
                                if (videoFile != null && videoFile.exists()) {
                                    videoFile.delete();
                                }
                                if (list.size() > 2) {
                                    File videoWaterFile = list.get(2).getOriginFile();
                                    if (videoWaterFile != null && videoWaterFile.exists()) {
                                        videoWaterFile.delete();
                                    }
                                }
                            }
                            String imageRemoteName = list.get(0).getRemoteFileName();
                            String videoRemoteName = list.get(1).getRemoteFileName();
                            String waterVideoRemoteName = videoRemoteName;
                            if (list.size() > 2) {
                                waterVideoRemoteName = list.get(2).getRemoteFileName();
                            }
                            saveUploadVideoInfo(imageRemoteName, videoRemoteName, waterVideoRemoteName);
                        } else {
                            ToastUtil.show(R.string.video_pub_failed);
                            onFailed();
                        }
                    }

                });
            }
        });
    }

    /**
     * 把视频上传后的信息保存在服务器
     *
     * @param imageRemoteName      云存储上的视频封面图文件名
     * @param videoRemoteName      云存储上的视频文件名
     * @param waterVideoRemoteName 云存储上的有水印视频文件名
     */
    private void saveUploadVideoInfo(String imageRemoteName, String videoRemoteName, String waterVideoRemoteName) {
        VideoHttpUtil.saveUploadVideoInfo(
                mVideoTitle,
                imageRemoteName,
                videoRemoteName,
                waterVideoRemoteName,
                mGoodsId,
                mMusicId,
                mCheckBox != null && mCheckBox.isChecked(),
                mVideoClassId,
                mGoodsType,
                mVideoRatio,
                new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0) {
                            if (info.length > 0) {
                                if (mConfigBean != null && mConfigBean.getVideoAuditSwitch() == 1) {
                                    ToastUtil.show(R.string.video_pub_success_2);
                                } else {
                                    ToastUtil.show(R.string.video_pub_success);
                                }
                                if (mAdapter != null) {
                                    String shareType = mAdapter.getShareType();
                                    if (shareType != null) {
                                        JSONObject obj = JSON.parseObject(info[0]);
                                        shareVideoPage(shareType, obj.getString("id"), obj.getString("title"), obj.getString("thumb_s"));
                                    }
                                }
                                finish();
                            }
                        } else {
                            ToastUtil.show(msg);
                            if (mBtnPub != null) {
                                mBtnPub.setEnabled(true);
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        if (mLoading != null) {
                            mLoading.dismiss();
                        }
                    }
                });
    }

    /**
     * 分享页面链接
     */

    public void shareVideoPage(String shareType, String videoId, String videoTitle, String videoImageUrl) {
        ShareData data = new ShareData();
        String shareVideoTitle = mConfigBean.getVideoShareTitle();
        if (!TextUtils.isEmpty(shareVideoTitle) && shareVideoTitle.contains("{username}")) {
            shareVideoTitle = shareVideoTitle.replace("{username}", CommonAppConfig.getInstance().getUserBean().getUserNiceName());
        }
        data.setTitle(shareVideoTitle);
        if (TextUtils.isEmpty(videoTitle)) {
            data.setDes(mConfigBean.getVideoShareDes());
        } else {
            data.setDes(videoTitle);
        }
        data.setImgUrl(videoImageUrl);
        String webUrl = HtmlConfig.SHARE_VIDEO + videoId;
        data.setWebUrl(webUrl);
        if (mMobShareUtil == null) {
            mMobShareUtil = new MobShareUtil();
        }
        mMobShareUtil.execute(shareType, data, null);
    }


}
