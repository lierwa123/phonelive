package com.yunbao.mall.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.FloatWindowHelper;
import com.yunbao.common.utils.L;
import com.yunbao.mall.R;

import java.util.ArrayList;
import java.util.List;

public class GoodsTitleAdapter extends PagerAdapter {

    private List<Vh> mViewList;
    private Context mContext;
    private VideoVh mVideoVh;

    public GoodsTitleAdapter(Context context, String videoUrl, String videoImgUrl, List<String> thumbs) {
        mContext = context;
        mViewList = new ArrayList<>();
        if (!TextUtils.isEmpty(videoUrl)) {
            mVideoVh = new VideoVh(videoUrl, videoImgUrl);
            mViewList.add(mVideoVh);
        }
        for (String thumb : thumbs) {
            mViewList.add(new ImageVh(thumb));
        }
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Vh vh = mViewList.get(position);
        View view = vh.getView();
        container.addView(view);
        vh.loadData();
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Vh vh = mViewList.get(position);
        vh.release();
        container.removeView(vh.getView());
        L.e("GoodsTitleAdapter-----destroyItem----> " + position);
    }

    public void pausePlayVideo() {
        if (mVideoVh != null) {
            mVideoVh.pausePlay();
        }
    }

    public void resumePlayVideo() {
        if (mVideoVh != null) {
            mVideoVh.resumePlay();
        }
    }

    public void release() {
        if (mVideoVh != null) {
            mVideoVh.release();
        }
    }


    interface Vh {
        View getView();

        void loadData();

        void release();
    }

    class ImageVh implements Vh {
        private ImageView mImg;
        private String mImageUrl;

        public ImageVh(String url) {
            mImageUrl = url;
            mImg = new ImageView(mContext);
            mImg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        public View getView() {
            return mImg;
        }

        public void loadData() {
            ImgLoader.display(mContext, mImageUrl, mImg);
        }

        @Override
        public void release() {
            if (mImg != null) {
                mImg.setImageDrawable(null);
            }
            mImg = null;
        }

    }


    class VideoVh implements Vh, View.OnClickListener {
        private ImageView mImg;
        private TXCloudVideoView mTXCloudVideoView;
        private String mImageUrl;
        private String mVideoUrl;
        private View mView;
        private TXVodPlayer mPlayer;
        private View mIconPause;
        private ImageView mImgMute;
        private boolean mClickPaused;
        private boolean mPaused;
        private boolean mMute;


        public VideoVh(String videoUrl, String imageUrl) {
            mImageUrl = imageUrl;
            mVideoUrl = videoUrl;
            View v = LayoutInflater.from(mContext).inflate(R.layout.view_goods_title_video, null);
            mView = v;
            mImg = v.findViewById(R.id.img);
            mIconPause = v.findViewById(R.id.icon_pause);
            mImgMute = v.findViewById(R.id.img_mute);
            v.findViewById(R.id.btn_pause).setOnClickListener(this);
            mImgMute.setOnClickListener(this);
            mTXCloudVideoView = v.findViewById(R.id.video_view);
            mTXCloudVideoView.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
            mPlayer = new TXVodPlayer(mContext);
            TXVodPlayConfig playConfig = new TXVodPlayConfig();
            playConfig.setMaxCacheItems(15);
            playConfig.setProgressInterval(200);
            playConfig.setHeaders(CommonAppConfig.HEADER);
            mPlayer.setConfig(playConfig);
            mPlayer.setLoop(true);
            mPlayer.setAutoPlay(true);
            mPlayer.setVodListener(new ITXVodPlayListener() {
                @Override
                public void onPlayEvent(TXVodPlayer txVodPlayer, int e, Bundle bundle) {
                    switch (e) {
//                        case TXLiveConstants.PLAY_EVT_PLAY_BEGIN://加载完成，开始播放的回调
//
//                            break;
                        case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME://获取到视频首帧回调
                            if (mImg.getVisibility() == View.VISIBLE) {
                                mImg.setVisibility(View.INVISIBLE);
                            }
                            if (mPaused && mPlayer != null) {
                                mPlayer.pause();
                            }
                            break;
                    }
                }

                @Override
                public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

                }
            });
            mPlayer.setPlayerView(mTXCloudVideoView);
        }


        public View getView() {
            return mView;
        }

        public void loadData() {
            ImgLoader.display(mContext, mImageUrl, mImg);
            if (!TextUtils.isEmpty(mVideoUrl) && mPlayer != null) {
                mPlayer.startPlay(mVideoUrl);
            }
        }

        @Override
        public void release() {
            if (mImg != null) {
                mImg.setImageDrawable(null);
            }
            mImg = null;
            if (mPlayer != null) {
                mPlayer.stopPlay(false);
                mPlayer.setPlayListener(null);
            }
            mPlayer = null;
        }


        public void pausePlay() {
            if (mClickPaused) {
                return;
            }
            if (!mPaused) {
                mPaused = true;
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.pause();
                }
            }

        }

        public void resumePlay() {
            if (mClickPaused) {
                return;
            }
            if (mPaused) {
                mPaused = false;
                if (mPlayer != null) {
                    mPlayer.resume();
                }
            }
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_pause) {
                clickPause();
            } else if (id == R.id.img_mute) {
                clickMute();
            }
        }

        private void clickMute() {
            if (mMute) {
                if (mPlayer != null) {
                    mPlayer.setMute(false);
                }
                mMute = false;
                if (mImgMute != null) {
                    mImgMute.setImageResource(R.mipmap.icon_mall_voice_1);
                }
            } else {
                if (mPlayer != null) {
                    mPlayer.setMute(true);
                }
                mMute = true;
                if (mImgMute != null) {
                    mImgMute.setImageResource(R.mipmap.icon_mall_voice_0);
                }
            }
        }


        private void clickPause() {
            if (mClickPaused) {
                if (mPlayer != null) {
                    mPlayer.resume();
                }
                if (mIconPause != null && mIconPause.getVisibility() == View.VISIBLE) {
                    mIconPause.setVisibility(View.INVISIBLE);
                }
                if (mImgMute != null && mImgMute.getVisibility() != View.VISIBLE) {
                    mImgMute.setVisibility(View.VISIBLE);
                }
                mClickPaused = false;
            } else {
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.pause();
                }
                if (mIconPause != null && mIconPause.getVisibility() != View.VISIBLE) {
                    mIconPause.setVisibility(View.VISIBLE);
                }
                if (mImgMute != null && mImgMute.getVisibility() == View.VISIBLE) {
                    mImgMute.setVisibility(View.INVISIBLE);
                }
                mClickPaused = true;
            }
        }


    }
}