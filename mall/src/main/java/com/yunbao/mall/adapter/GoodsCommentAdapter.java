package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.custom.NineGridLayout2;
import com.yunbao.common.custom.StarCountView;
import com.yunbao.common.dialog.ImagePreviewDialog;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.GoodsCommentBean;

import java.util.List;

public class GoodsCommentAdapter extends RefreshAdapter<GoodsCommentBean> {

    private static final int TYPE_0_0_0 = 0;//评论没有图片，没有追评
    private static final int TYPE_0_1_0 = 2;//评论没有图片，有追评，追评没有图片
    private static final int TYPE_0_1_1 = 3;//评论没有图片，有追评，追评有图片
    private static final int TYPE_1_0_0 = 4;//评论有图片，没有追评
    private static final int TYPE_1_1_0 = 5;//评论有图片，有追评，追评没有图片
    private static final int TYPE_1_1_1 = 6;//评论有图片，有追评，追评有图片
    private boolean mShowAppend;//是否显示追评
    private NineGridLayout2.ActionListener mNineGridListener;

    public GoodsCommentAdapter(Context context, boolean showAppend) {
        super(context);
        mShowAppend = showAppend;
        init();
    }

    public GoodsCommentAdapter(Context context, List<GoodsCommentBean> list, boolean showAppend) {
        super(context, list);
        mShowAppend = showAppend;
        init();
    }

    private void init() {
        mNineGridListener = new NineGridLayout2.ActionListener() {
            @Override
            public void onItemClick(NineGridLayout2 layout, final List<?> dataList, int position) {
                String videoUrl = layout.getVideoUrl();
                if (TextUtils.isEmpty(videoUrl)) {
                    ImagePreviewDialog dialog = new ImagePreviewDialog();
                    dialog.setImageInfo(dataList.size(), position, false, new ImagePreviewDialog.ActionListener() {
                        @Override
                        public void loadImage(ImageView imageView, int position) {
                            ImgLoader.display(mContext, (String) (dataList.get(position)), imageView);
                        }

                        @Override
                        public void onDeleteClick(int position) {

                        }

                        @Override
                        public String getImageUrl(int position) {
                            return (String) (dataList.get(position));
                        }
                    });
                    dialog.show(((AbsActivity) mContext).getSupportFragmentManager(), "ImagePreviewDialog");
                } else {
                    if (position == 0) {
                        RouteUtil.forwardVideoPlay(mContext, videoUrl, (String) dataList.get(0));
                    } else {
                        ImagePreviewDialog dialog = new ImagePreviewDialog();
                        dialog.setImageInfo(dataList.size() - 1, position - 1, false, new ImagePreviewDialog.ActionListener() {
                            @Override
                            public void loadImage(ImageView imageView, int position) {
                                ImgLoader.display(mContext, (String) (dataList.get(position + 1)), imageView);
                            }

                            @Override
                            public void onDeleteClick(int position) {

                            }

                            @Override
                            public String getImageUrl(int position) {
                                return (String) (dataList.get(position + 1));
                            }
                        });
                        dialog.show(((AbsActivity) mContext).getSupportFragmentManager(), "ImagePreviewDialog");
                    }
                }
            }

            @Override
            public void displayImage(Object path, ImageView imageView) {
                ImgLoader.display(mContext, (String) path, imageView);
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        GoodsCommentBean bean = mList.get(position);
        if (mShowAppend) {
            if (bean.getHasAppend() == 1) {
                GoodsCommentBean appendBean = bean.getAppendCommentBean();
                if (appendBean != null) {
                    if (bean.hasImgOrVideo()) {
                        if (appendBean.hasImgOrVideo()) {
                            return TYPE_1_1_1;
                        } else {
                            return TYPE_1_1_0;
                        }
                    } else {
                        if (appendBean.hasImgOrVideo()) {
                            return TYPE_0_1_1;
                        } else {
                            return TYPE_0_1_0;
                        }
                    }
                } else {
                    if (bean.hasImgOrVideo()) {
                        return TYPE_1_0_0;
                    } else {
                        return TYPE_0_0_0;
                    }
                }
            } else {
                if (bean.hasImgOrVideo()) {
                    return TYPE_1_0_0;
                } else {
                    return TYPE_0_0_0;
                }
            }
        } else {
            if (bean.hasImgOrVideo()) {
                return TYPE_1_0_0;
            } else {
                return TYPE_0_0_0;
            }
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == TYPE_0_1_0) {
            return new Vh010(mInflater.inflate(R.layout.item_goods_comment_010, viewGroup, false));
        } else if (i == TYPE_0_1_1) {
            return new Vh011(mInflater.inflate(R.layout.item_goods_comment_011, viewGroup, false));
        } else if (i == TYPE_1_0_0) {
            return new Vh100(mInflater.inflate(R.layout.item_goods_comment_100, viewGroup, false));
        } else if (i == TYPE_1_1_0) {
            return new Vh110(mInflater.inflate(R.layout.item_goods_comment_110, viewGroup, false));
        } else if (i == TYPE_1_1_1) {
            return new Vh111(mInflater.inflate(R.layout.item_goods_comment_111, viewGroup, false));
        }
        return new Vh(mInflater.inflate(R.layout.item_goods_comment_000, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mAvatar;
        TextView mName;
        StarCountView mStarCountView;
        TextView mContent;
        TextView mDateTime;
        TextView mGoodsSpecName;


        public Vh(@NonNull View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.avatar);
            mName = itemView.findViewById(R.id.name);
            mStarCountView = itemView.findViewById(R.id.star);
            mContent = itemView.findViewById(R.id.content);
            mDateTime = itemView.findViewById(R.id.date_time);
            mGoodsSpecName = itemView.findViewById(R.id.goods_spec_name);
        }

        void setData(GoodsCommentBean bean) {
            ImgLoader.displayAvatar(mContext, bean.getAvatar(), mAvatar);
            mName.setText(bean.getBuyerName());
            mStarCountView.setFillCount(bean.getStarCount());
            mContent.setText(bean.getContent());
            mDateTime.setText(bean.getDateTime());
            mGoodsSpecName.setText(bean.getGoodsSpecName());
        }
    }

    class Vh010 extends Vh {

        TextView mAppendDateTip;
        TextView mAppendContent;

        public Vh010(@NonNull View itemView) {
            super(itemView);
            mAppendDateTip = itemView.findViewById(R.id.append_date_tip);
            mAppendContent = itemView.findViewById(R.id.append_content);
        }

        @Override
        void setData(GoodsCommentBean bean) {
            super.setData(bean);
            GoodsCommentBean appendBean = bean.getAppendCommentBean();
            if (appendBean != null) {
                mAppendDateTip.setText(appendBean.getDateTip());
                mAppendContent.setText(appendBean.getContent());
            }
        }
    }

    class Vh011 extends Vh010 {

        NineGridLayout2 mAppendNineGridLayout;

        public Vh011(@NonNull View itemView) {
            super(itemView);
            mAppendNineGridLayout = itemView.findViewById(R.id.nine_grid_layout_append);
            mAppendNineGridLayout.setActionListener(mNineGridListener);
        }

        @Override
        void setData(GoodsCommentBean bean) {
            super.setData(bean);
            GoodsCommentBean appendBean = bean.getAppendCommentBean();
            if (appendBean != null) {
                List<String> list = appendBean.getImageList();
                if (list != null && mList.size() > 0) {
                    mAppendNineGridLayout.setData(list, appendBean.getVideoUrl());
                }
            }
        }
    }

    class Vh100 extends Vh {

        NineGridLayout2 mNineGridLayout;

        public Vh100(@NonNull View itemView) {
            super(itemView);
            mNineGridLayout = itemView.findViewById(R.id.nine_grid_layout);
            mNineGridLayout.setActionListener(mNineGridListener);
        }

        @Override
        void setData(GoodsCommentBean bean) {
            super.setData(bean);
            List<String> list = bean.getImageList();
            if (list != null && mList.size() > 0) {
                mNineGridLayout.setData(list, bean.getVideoUrl());
            }
        }
    }


    class Vh110 extends Vh100 {

        TextView mAppendDateTip;
        TextView mAppendContent;

        public Vh110(@NonNull View itemView) {
            super(itemView);
            mAppendDateTip = itemView.findViewById(R.id.append_date_tip);
            mAppendContent = itemView.findViewById(R.id.append_content);
        }

        @Override
        void setData(GoodsCommentBean bean) {
            super.setData(bean);
            GoodsCommentBean appendBean = bean.getAppendCommentBean();
            if (appendBean != null) {
                mAppendDateTip.setText(appendBean.getDateTip());
                mAppendContent.setText(appendBean.getContent());
            }
        }
    }

    class Vh111 extends Vh110 {

        NineGridLayout2 mAppendNineGridLayout;

        public Vh111(@NonNull View itemView) {
            super(itemView);
            mAppendNineGridLayout = itemView.findViewById(R.id.nine_grid_layout_append);
            mAppendNineGridLayout.setActionListener(mNineGridListener);
        }

        @Override
        void setData(GoodsCommentBean bean) {
            super.setData(bean);
            GoodsCommentBean appendBean = bean.getAppendCommentBean();
            if (appendBean != null) {
                List<String> list = appendBean.getImageList();
                if (list != null && mList.size() > 0) {
                    mAppendNineGridLayout.setData(list, appendBean.getVideoUrl());
                }
            }
        }
    }
}
