package com.yunbao.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yunbao.common.R;

import java.util.ArrayList;
import java.util.List;

public class NineGridLayout2 extends FrameLayout {

    private Context mContext;
    private int mItemWidth;
    private int mDividerWidth;
    private int mPlayIconRes;
    private List<ImageView> mViewList;
    private ActionListener mActionListener;
    private LayoutParams[] mLayoutParamsArray;
    private OnClickListener mOnClickListener;
    private List<?> mDataList;
    private String mVideoUrl;
    private ImageView mPlayIcon;
    private float mScale;


    public NineGridLayout2(Context context) {
        this(context, null);
    }

    public NineGridLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NineGridLayout);
        mItemWidth = (int) ta.getDimension(R.styleable.NineGridLayout_ngl_item_width, 0);
        mDividerWidth = (int) ta.getDimension(R.styleable.NineGridLayout_ngl_divider_width, 0);
        mPlayIconRes = ta.getResourceId(R.styleable.NineGridLayout_ngl_play_icon, 0);
        ta.recycle();
        mScale = context.getResources().getDisplayMetrics().density;
        mViewList = new ArrayList<>();
        mOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionListener != null) {
                    for (int i = 0, size = mViewList.size(); i < size; i++) {
                        if (v == mViewList.get(i)) {
                            mActionListener.onItemClick(NineGridLayout2.this, mDataList, i);
                            break;
                        }
                    }
                }
            }
        };
    }

    private int dp2px(int dpVal) {
        return (int) (mScale * dpVal + 0.5f);
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }


    public void setData(List<?> list, String videoUrl) {
        mVideoUrl = videoUrl;
        if (list == null) {
            hideItem(0);
            setHeight(0);
            return;
        }
        int dataSize = list.size();
        if (dataSize == 0) {
            hideItem(0);
            setHeight(0);
            return;
        }
        mDataList = list;
        int viewSize = mViewList.size();
        if (viewSize < dataSize) {
            for (int i = 0, size = dataSize - viewSize; i < size; i++) {
                addItem();
            }
        } else if (viewSize > dataSize) {
            hideItem(dataSize);
        }
        if (TextUtils.isEmpty(videoUrl)) {
            if (mPlayIcon != null && mPlayIcon.getVisibility() == VISIBLE) {
                mPlayIcon.setVisibility(INVISIBLE);
            }
        } else {
            if (mPlayIcon == null) {
                mPlayIcon = new ImageView(mContext);
                int dp30 = dp2px(30);
                int dp15 = dp2px(15);
                LayoutParams iconLp = new LayoutParams(dp30, dp30);
                iconLp.leftMargin = dp15;
                iconLp.topMargin = dp15;
                mPlayIcon.setLayoutParams(iconLp);
                mPlayIcon.setImageResource(mPlayIconRes);
                addView(mPlayIcon);
            } else {
                if (mPlayIcon.getVisibility() != VISIBLE) {
                    mPlayIcon.setVisibility(VISIBLE);
                }
            }
        }
        if (mLayoutParamsArray == null) {
            mLayoutParamsArray = new LayoutParams[6];
        }
        int rowCount = dataSize / 3;
        if (dataSize % 3 != 0) {
            rowCount++;
        }
        setHeight(mItemWidth * rowCount + mDividerWidth * (rowCount - 1));
        for (int i = 0; i < dataSize; i++) {
            ImageView imageView = mViewList.get(i);
            if (mLayoutParamsArray[i] == null) {
                mLayoutParamsArray[i] = new LayoutParams(mItemWidth, mItemWidth);
                int row = i / 3;//行
                int col = i % 3;//列
                int margin = mItemWidth + mDividerWidth;
                mLayoutParamsArray[i].leftMargin = margin * col;
                mLayoutParamsArray[i].topMargin = margin * row;
            }
            if (imageView.getLayoutParams() != mLayoutParamsArray[i]) {
                imageView.setLayoutParams(mLayoutParamsArray[i]);
            }
            if (imageView.getVisibility() != VISIBLE) {
                imageView.setVisibility(VISIBLE);
            }
            if (mActionListener != null) {
                mActionListener.displayImage(list.get(i), imageView);
            }
        }

    }


    private void setHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (height != layoutParams.height) {
            layoutParams.height = height;
            requestLayout();
        }
    }


    private void hideItem(int fromIndex) {
        for (int i = 0, size = mViewList.size(); i < size; i++) {
            if (i >= fromIndex) {
                ImageView imageView = mViewList.get(i);
                imageView.setImageDrawable(null);
                if (imageView.getVisibility() == VISIBLE) {
                    imageView.setVisibility(INVISIBLE);
                }
            }
        }
    }

    private void addItem() {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(mOnClickListener);
        mViewList.add(imageView);
        addView(imageView);
    }


    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface ActionListener {

        void onItemClick(NineGridLayout2 layout, List<?> dataList, int position);

        void displayImage(Object path, ImageView imageView);
    }


}
