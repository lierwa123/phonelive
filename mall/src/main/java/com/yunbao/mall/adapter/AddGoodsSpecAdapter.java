package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.yunbao.mall.bean.AddGoodsSpecBean;

import java.io.File;
import java.util.ArrayList;

public class AddGoodsSpecAdapter extends RefreshAdapter<AddGoodsSpecBean> {

    private String mSpecString;
    private View.OnClickListener mImageClickListener;
    private View.OnClickListener mDeleteClickListener;
    private View.OnClickListener mDeleteSpecClickListener;

    public AddGoodsSpecAdapter(Context context) {
        super(context);
        mSpecString = WordUtil.getString(R.string.mall_089);
        mImageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                final AddGoodsSpecBean bean = mList.get(position);
                if (!bean.isEmpty()) {
                    ImagePreviewDialog dialog = new ImagePreviewDialog();
                    dialog.setImageInfo(1, 0, false, new ImagePreviewDialog.ActionListener() {
                        @Override
                        public void loadImage(ImageView imageView, int position) {
                            File file = bean.getFile();
                            if (file != null && file.exists()) {
                                ImgLoader.display(mContext, file, imageView);
                            } else {
                                String url = bean.getThumb();
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
                notifyItemChanged(position);
            }
        };
        mDeleteSpecClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                mList.remove(position);
                notifyDataSetChanged();
            }
        };
    }

    public void setImageFile(int position, File file) {
        mList.get(position).setFile(file);
        notifyItemChanged(position);
    }

    public void insertItem() {
        int size = mList.size();
        mList.add(new AddGoodsSpecBean());
        notifyItemInserted(size);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_add_goods_spec, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position), position);
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mIndex;
        EditText mName;
        EditText mNum;
        EditText mPrice;
        ImageView mImg;
        View mBtnDel;
        View mBtnImg;
        View mBtnDelSpec;
        AddGoodsSpecBean mBean;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mIndex = itemView.findViewById(R.id.index);
            mName = itemView.findViewById(R.id.name);
            mNum = itemView.findViewById(R.id.num);
            mPrice = itemView.findViewById(R.id.price);
            mImg = itemView.findViewById(R.id.img);
            mBtnImg = itemView.findViewById(R.id.btn_img);
            mBtnDel = itemView.findViewById(R.id.btn_del);
            mBtnDelSpec = itemView.findViewById(R.id.btn_del_spec);
            mBtnImg.setOnClickListener(mImageClickListener);
            mBtnDel.setOnClickListener(mDeleteClickListener);
            mBtnDelSpec.setOnClickListener(mDeleteSpecClickListener);
            mName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mBean != null) {
                        mBean.setName(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            mNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mBean != null) {
                        mBean.setNum(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            mPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mBean != null) {
                        mBean.setPrice(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        void setData(AddGoodsSpecBean bean, int position) {
            mBean = bean;
            mBtnImg.setTag(position);
            mBtnDel.setTag(position);
            mBtnDelSpec.setTag(position);
            mIndex.setText(StringUtil.contact(mSpecString, String.valueOf(position + 1)));
            mName.setText(bean.getName());
            mNum.setText(bean.getNum());
            mPrice.setText(bean.getPrice());
            if (position == 0) {
                if (mBtnDelSpec.getVisibility() == View.VISIBLE) {
                    mBtnDelSpec.setVisibility(View.INVISIBLE);
                }
            } else {
                if (mBtnDelSpec.getVisibility() != View.VISIBLE) {
                    mBtnDelSpec.setVisibility(View.VISIBLE);
                }
            }
            if (!bean.isEmpty()) {
                if (bean.getFile() != null) {
                    ImgLoader.display(mContext, bean.getFile(), mImg);
                } else {
                    ImgLoader.display(mContext, bean.getThumb(), mImg);
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
