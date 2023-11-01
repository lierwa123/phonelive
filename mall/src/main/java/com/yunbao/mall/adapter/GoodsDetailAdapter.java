package com.yunbao.mall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.ScreenDimenUtil;
import com.yunbao.mall.R;

import java.util.List;

public class GoodsDetailAdapter extends RefreshAdapter<String> {

    private boolean mHasText;

    public GoodsDetailAdapter(Context context, List<String> list, boolean hasText) {
        super(context, list);
        mHasText = hasText;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHasText && position == 0) {
            return -1;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemViewType) {
        if (itemViewType == -1) {
            return new TextVh(mInflater.inflate(R.layout.item_goods_detail_text, viewGroup, false));
        }
        return new ImageVh(mInflater.inflate(R.layout.item_goods_detail_img, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i));
    }


    abstract class Vh extends RecyclerView.ViewHolder {

        public Vh(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void setData(String data);
    }

    class TextVh extends Vh {

        TextView mTextView;

        public TextVh(@NonNull View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }

        @Override
        public void setData(String data) {
            mTextView.setText(data);
        }
    }

    class ImageVh extends Vh {

        ImageView mImageView;
        ImgLoader.DrawableCallback mDrawableCallback;

        public ImageVh(@NonNull View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView;
            mDrawableCallback = new ImgLoader.DrawableCallback() {
                @Override
                public void onLoadSuccess(Drawable drawable) {
                    float drawableW = drawable.getIntrinsicWidth();
                    float drawableH = drawable.getIntrinsicHeight();
                    ViewGroup.LayoutParams lp = mImageView.getLayoutParams();
                    int targetH = (int) (ScreenDimenUtil.getInstance().getScreenWdith() * drawableH / drawableW);
                    if (lp.height != targetH) {
                        lp.height = targetH;
                        mImageView.requestLayout();
                    }
                    mImageView.setImageDrawable(drawable);
                }

                @Override
                public void onLoadFailed() {
                    mImageView.setImageDrawable(null);
                }
            };
        }

        @Override
        public void setData(String data) {
            ImgLoader.displayDrawable(mContext, data, mDrawableCallback);
//            ImgLoader.display(mContext, data, mImageView);
        }
    }
}
