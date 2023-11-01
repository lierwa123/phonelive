package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.PayContentVideoBean;

import java.io.File;

public class PayContentMulAdapter extends RefreshAdapter<PayContentVideoBean> {

    private View.OnClickListener mUploadClickListener;
    private View.OnClickListener mDelImgClickListener;
    private View.OnClickListener mAddItemClickListener;
    private View.OnClickListener mDelItemClickListener;

    public PayContentMulAdapter(Context context) {
        super(context);
        mList.add(new PayContentVideoBean());
        mList.add(new PayContentVideoBean());
        mUploadClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mList.get(position), position);
                }
            }
        };

        mDelImgClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                mList.get(position).setFilePath(null);
                notifyItemChanged(position);
            }
        };

        mAddItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = mList.size();
                mList.add(new PayContentVideoBean());
                notifyItemInserted(size);
            }
        };

        mDelItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                mList.remove(position);
                notifyDataSetChanged();
            }
        };
    }

    public void setFilePath(String filePath, String duration, int position) {
        PayContentVideoBean bean = mList.get(position);
        bean.setFilePath(filePath);
        bean.setDuration(duration);
        notifyItemChanged(position);
    }


    @Override
    public int getItemViewType(int position) {
        if (position == mList.size()) {
            return -1;
        } else if (position == 0) {
            return 0;
        } else if (position == 1) {
            return 1;
        }
        return 2;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == -1) {
            return new VhLast(mInflater.inflate(R.layout.item_pay_content_mul_last, viewGroup, false));
        } else if (i == 0) {
            return new Vh0(mInflater.inflate(R.layout.item_pay_content_mul_0, viewGroup, false));
        } else if (i == 1) {
            return new Vh(mInflater.inflate(R.layout.item_pay_content_mul_1, viewGroup, false));
        }
        return new Vh2(mInflater.inflate(R.layout.item_pay_content_mul, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof Vh) {
            ((Vh) vh).setData(mList.get(position), position);
        }
    }


    class VhLast extends RecyclerView.ViewHolder {
        public VhLast(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(mAddItemClickListener);
        }
    }

    class Vh extends RecyclerView.ViewHolder {

        View mBtnUpload;
        ImageView mImg;
        View mBtnDel;
        EditText mEdit;
        PayContentVideoBean mBean;


        public Vh(@NonNull View itemView) {
            super(itemView);
            mBtnUpload = itemView.findViewById(R.id.btn_upload);
            mImg = itemView.findViewById(R.id.img);
            mBtnDel = itemView.findViewById(R.id.btn_del);
            mBtnUpload.setOnClickListener(mUploadClickListener);
            mBtnDel.setOnClickListener(mDelImgClickListener);
            mEdit = itemView.findViewById(R.id.edit);
            mEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mBean != null) {
                        mBean.setTitle(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        void setData(PayContentVideoBean bean, int position) {
            mBtnUpload.setTag(position);
            mBtnDel.setTag(position);
            mBean = bean;
            mEdit.setText(mBean.getTitle());

            File file = bean.getFile();
            if (file != null && file.exists()) {
                ImgLoader.displayVideoThumb(mContext, file, mImg);
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


    class Vh0 extends Vh {

        CheckBox mCheckBox;

        public Vh0(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.checkbox);
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mBean != null) {
                        mBean.setIsSee(isChecked ? 1 : 0);
                    }
                }
            });
        }

        @Override
        void setData(PayContentVideoBean bean, int position) {
            super.setData(bean, position);
            mCheckBox.setChecked(bean.getIsSee() == 1);
        }
    }


    class Vh2 extends Vh {

        View mBtnDelItem;

        public Vh2(@NonNull View itemView) {
            super(itemView);
            mBtnDelItem = itemView.findViewById(R.id.btn_del_item);
            mBtnDelItem.setOnClickListener(mDelItemClickListener);
        }

        @Override
        void setData(PayContentVideoBean bean, int position) {
            super.setData(bean, position);
            mBtnDelItem.setTag(position);
        }
    }

}
