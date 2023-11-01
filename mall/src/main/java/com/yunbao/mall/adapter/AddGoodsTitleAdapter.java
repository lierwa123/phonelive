package com.yunbao.mall.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.dialog.ImagePreviewDialog;
import com.yunbao.common.dialog.VideoPreviewDialog;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.AddGoodsImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddGoodsTitleAdapter extends RefreshAdapter<AddGoodsImageBean> {

    private String mTip0;
    private String mTip1;
    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mDeleteClickListener;
    private File mVideoFile;
    private String mVideoUrl;
    private String mVideoImgUrl;
    private String mVideoUrlPlay;
    private String mVideoImgUrlPlay;
    private List<AddGoodsImageBean> mPreviewList;

    public AddGoodsTitleAdapter(Context context) {
        super(context);
        mTip0 = WordUtil.getString(R.string.mall_082);
        mTip1 = WordUtil.getString(R.string.mall_083);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                AddGoodsImageBean bean = mList.get(position);
                if (!bean.isEmpty()) {
                    if (position == 0) {
                        File file = bean.getFile();
                        if (file != null && file.exists()) {
                            VideoPreviewDialog dialog = new VideoPreviewDialog();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.VIDEO_PATH, file.getAbsolutePath());
                            bundle.putLong(Constants.VIDEO_DURATION, -1);
                            dialog.setArguments(bundle);
                            dialog.show(((AbsActivity) mContext).getSupportFragmentManager(), "VideoPreviewDialog");
                        } else {
                            if (!TextUtils.isEmpty(mVideoUrlPlay)) {
                                RouteUtil.forwardVideoPlay(mContext,mVideoUrlPlay, mVideoImgUrlPlay);
                            }
                        }
                        return;
                    }
                    if (mPreviewList == null) {
                        mPreviewList = new ArrayList<>();
                    }
                    if (mPreviewList.size() > 0) {
                        mPreviewList.clear();
                    }
                    for (int i = 1, size = mList.size(); i < size; i++) {
                        AddGoodsImageBean prevBean = mList.get(i);
                        if (!prevBean.isEmpty()) {
                            mPreviewList.add(prevBean);
                        }
                    }
                    if (mPreviewList.size() == 0) {
                        return;
                    }
                    int clickPosition = 0;
                    for (int i = 0, size = mPreviewList.size(); i < size; i++) {
                        if (bean == mPreviewList.get(i)) {
                            clickPosition = i;
                        }
                    }
                    ImagePreviewDialog dialog = new ImagePreviewDialog();
                    dialog.setImageInfo(mPreviewList.size(), clickPosition, false, new ImagePreviewDialog.ActionListener() {
                        @Override
                        public void loadImage(ImageView imageView, int position) {
                            AddGoodsImageBean prevBean = mPreviewList.get(position);
                            File file = prevBean.getFile();
                            if (file != null && file.exists()) {
                                ImgLoader.display(mContext, file, imageView);
                            } else {
                                String url = prevBean.getImgUrl();
                                if (!TextUtils.isEmpty(url)) {
                                    ImgLoader.display(mContext, url, imageView);
                                }
                            }
                        }

                        @Override
                        public void onDeleteClick(int position) {

                        }

                        @Override
                        public String getImageUrl(int position) {
                            return null;
                        }
                    });
                    dialog.show(((AbsActivity) mContext).getSupportFragmentManager(), "ImagePreviewDialog");
                } else {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(bean, position);
                    }
                }
            }
        };
        mDeleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                mList.get(position).setEmpty();
                if (position == 0 || position == mList.size() - 1) {
                    if (position == 0) {
                        mVideoFile = null;
                        mVideoUrl = null;
                        mVideoImgUrl = null;
                    }
                    notifyItemChanged(position);
                } else {
                    mList.remove(position);
                    if (!mList.get(mList.size() - 1).isEmpty()) {
                        mList.add(new AddGoodsImageBean());
                    }
                    notifyDataSetChanged();
                }
            }
        };
    }

    public File getVideoFile() {
        return mVideoFile;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public String getVideoImgUrl() {
        return mVideoImgUrl;
    }

    public void setVideoImgUrl(String videoImgUrl) {
        mVideoImgUrl = videoImgUrl;
    }


    public void setVideoUrlPlay(String videoUrlPlay) {
        mVideoUrlPlay = videoUrlPlay;
    }

    public void setVideoImgUrlPlay(String videoImgUrlPlay) {
        mVideoImgUrlPlay = videoImgUrlPlay;
    }

    public void setImageFile(int position, File file) {
        if (position == 0) {
            mVideoFile = file;
        }
        mList.get(position).setFile(file);
        int size = mList.size();
        if (position == size - 1 && size < 10) {
            mList.add(new AddGoodsImageBean());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_add_goods_title, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position), position);
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mTip;
        ImageView mImg;
        View mBtnDel;


        public Vh(@NonNull View itemView) {
            super(itemView);
            mTip = itemView.findViewById(R.id.tip);
            mImg = itemView.findViewById(R.id.img);
            mBtnDel = itemView.findViewById(R.id.btn_del);
            itemView.setOnClickListener(mOnClickListener);
            mBtnDel.setOnClickListener(mDeleteClickListener);
        }

        void setData(AddGoodsImageBean bean, int position) {
            itemView.setTag(position);
            mBtnDel.setTag(position);
            if (position > 1 && bean.isEmpty()) {
                mTip.setText(StringUtil.contact(String.valueOf(mList.size() - 2), "/9"));
            } else {
                if (position == 0) {
                    mTip.setText(mTip0);
                } else {
                    mTip.setText(mTip1);
                }
            }
            if (!bean.isEmpty()) {
                if (bean.getFile() != null) {
                    if (position == 0) {
                        ImgLoader.displayVideoThumb(mContext, bean.getFile(), mImg);
                    } else {
                        ImgLoader.display(mContext, bean.getFile(), mImg);
                    }
                } else {
                    ImgLoader.display(mContext, bean.getImgUrl(), mImg);
                }
                if (mBtnDel.getVisibility() != View.VISIBLE) {
                    mBtnDel.setVisibility(View.VISIBLE);
                }
            } else {
                mImg.setImageDrawable(null);
                if (mBtnDel.getVisibility() == View.VISIBLE) {
                    mBtnDel.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

}
