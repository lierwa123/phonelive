package com.yunbao.mall.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.dialog.ActiveVideoPreviewDialog;
import com.yunbao.common.dialog.ImagePreviewDialog;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.AddGoodsCommentImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 买家对商品进行评价
 */
public class AddGoodsCommentAdapter extends RefreshAdapter<AddGoodsCommentImageBean> {

    private String mTipString;
    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mDeleteClickListener;
    private File mVideoFile;
    private String mVideoUrl;
    private String mVideoImgUrl;

    public AddGoodsCommentAdapter(Context context) {
        super(context);
        mList.add(new AddGoodsCommentImageBean());
        mList.add(new AddGoodsCommentImageBean());
        mTipString = WordUtil.getString(R.string.mall_242);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                AddGoodsCommentImageBean bean = mList.get(position);
                if (!bean.isEmpty()) {
                    if (position == mList.size() - 1) {//视频
                        if (mVideoFile != null && mVideoFile.exists()) {
                            ActiveVideoPreviewDialog dialog = new ActiveVideoPreviewDialog();
                            dialog.setActionListener(new ActiveVideoPreviewDialog.ActionListener() {
                                @Override
                                public void onDeleteClick() {
                                    int position = mList.size() - 1;
                                    mList.get(position).setEmpty();
                                    notifyItemChanged(position);
                                    mVideoFile = null;
                                    mVideoUrl = null;
                                    mVideoImgUrl = null;
                                }
                            });
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.VIDEO_PATH, mVideoFile.getAbsolutePath());
                            dialog.setArguments(bundle);
                            dialog.show(((AbsActivity) mContext).getSupportFragmentManager(), "ActiveVideoPreviewDialog");
                        }
                    } else {//图片
                        final List<File> imageFileList = new ArrayList<>();
                        int imgPosition = 0;
                        for (int i = 0, size = mList.size() - 1; i < size; i++) {
                            AddGoodsCommentImageBean imgBean = mList.get(i);
                            if (!imgBean.isEmpty()) {
                                if (bean == imgBean) {
                                    imgPosition = imageFileList.size();
                                }
                                imageFileList.add(imgBean.getFile());
                            }
                        }
                        if (imageFileList.size() > 0) {
                            ImagePreviewDialog dialog = new ImagePreviewDialog();
                            dialog.setImageInfo(imageFileList.size(), imgPosition, false, new ImagePreviewDialog.ActionListener() {
                                @Override
                                public void loadImage(ImageView imageView, int position) {
                                    ImgLoader.display(mContext, imageFileList.get(position), imageView);
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
                        }

                    }
                    return;
                }
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(bean, position);
                }
            }
        };
        mDeleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                mList.get(position).setEmpty();
                if (position == mList.size() - 1) {
                    mVideoFile = null;
                    mVideoUrl = null;
                    mVideoImgUrl = null;
                    notifyItemChanged(position);
                } else {
                    if (mList.size() > 2 && position < mList.size() - 2) {
                        mList.remove(position);
                        int size = mList.size();
                        if (size > 2 && !mList.get(size - 2).isEmpty()) {
                            mList.add(size - 1, new AddGoodsCommentImageBean());
                        }
                        notifyDataSetChanged();
                    } else {
                        notifyItemChanged(position);
                    }
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

    public void setImageFile(int position, File file) {
        if (position == mList.size() - 1) {
            mVideoFile = file;
        }
        mList.get(position).setFile(file);
        int size = mList.size();
        if (position != size - 1 && size < 6) {
            mList.add(size - 1, new AddGoodsCommentImageBean());
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == mList.size() - 1) {
            return -1;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == -1) {
            return new VideoVh(mInflater.inflate(R.layout.item_add_goods_comment_video, viewGroup, false));
        }
        return new ImageVh(mInflater.inflate(R.layout.item_add_goods_comment_img, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position), position);
    }


    class Vh extends RecyclerView.ViewHolder {

        ImageView mImg;
        View mBtnDel;


        public Vh(@NonNull View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img);
            mBtnDel = itemView.findViewById(R.id.btn_del);
            itemView.setOnClickListener(mOnClickListener);
            mBtnDel.setOnClickListener(mDeleteClickListener);
        }

        void setData(AddGoodsCommentImageBean bean, int position) {
            itemView.setTag(position);
            mBtnDel.setTag(position);
        }
    }

    class ImageVh extends Vh {

        TextView mTip;

        public ImageVh(@NonNull View itemView) {
            super(itemView);
            mTip = itemView.findViewById(R.id.tip);
        }

        void setData(AddGoodsCommentImageBean bean, int position) {
            super.setData(bean, position);
            if (position > 0 && bean.isEmpty()) {
                mTip.setText(StringUtil.contact(String.valueOf(mList.size() - 2), "/5"));
            } else {
                mTip.setText(mTipString);
            }
            if (!bean.isEmpty()) {
                if (bean.getFile() != null) {
                    ImgLoader.display(mContext, bean.getFile(), mImg);
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


    class VideoVh extends Vh {

        View mVideoTag;

        public VideoVh(@NonNull View itemView) {
            super(itemView);
            mVideoTag = itemView.findViewById(R.id.video_tag);
        }

        void setData(AddGoodsCommentImageBean bean, int position) {
            super.setData(bean, position);
            if (!bean.isEmpty()) {
                if (bean.getFile() != null) {
                    ImgLoader.displayVideoThumb(mContext, bean.getFile(), mImg);
                }
                if (mBtnDel.getVisibility() != View.VISIBLE) {
                    mBtnDel.setVisibility(View.VISIBLE);
                }
                if (mVideoTag.getVisibility() != View.VISIBLE) {
                    mVideoTag.setVisibility(View.VISIBLE);
                }
            } else {
                mImg.setImageDrawable(null);
                if (mBtnDel.getVisibility() == View.VISIBLE) {
                    mBtnDel.setVisibility(View.INVISIBLE);
                }
                if (mVideoTag.getVisibility() == View.VISIBLE) {
                    mVideoTag.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
