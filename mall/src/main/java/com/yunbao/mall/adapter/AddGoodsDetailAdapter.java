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
import com.yunbao.common.dialog.ImagePreviewDialog;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.AddGoodsImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddGoodsDetailAdapter extends RefreshAdapter<AddGoodsImageBean> {

    private String mTip0;
    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mDeleteClickListener;
    private List<AddGoodsImageBean> mPreviewList;

    public AddGoodsDetailAdapter(Context context) {
        super(context);
        mTip0 = WordUtil.getString(R.string.mall_086);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                AddGoodsImageBean bean = mList.get(position);
                if (!bean.isEmpty()) {
                    if (mPreviewList == null) {
                        mPreviewList = new ArrayList<>();
                    }
                    if (mPreviewList.size() > 0) {
                        mPreviewList.clear();
                    }
                    for (int i = 0, size = mList.size(); i < size; i++) {
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
                if (position == mList.size() - 1) {
                    mList.get(position).setEmpty();
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


    public void setImageFile(int position, File file) {
        mList.get(position).setFile(file);
        int size = mList.size();
        if (position == size - 1 && size < 20) {
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
            if (position > 0 && bean.isEmpty()) {
                mTip.setText(StringUtil.contact(String.valueOf(mList.size() - 1), "/20"));
            } else {
                mTip.setText(mTip0);
            }
            if (!bean.isEmpty()) {
                if (bean.getFile() != null) {
                    ImgLoader.display(mContext, bean.getFile(), mImg);
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
