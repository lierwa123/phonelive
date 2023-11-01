package com.yunbao.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.yunbao.common.R;

import java.util.ArrayList;
import java.util.List;

public class NineGridLayout extends FrameLayout {

    private Context mContext;
    private int mWidth1;
    private int mWidth2;
    private int mWidth3;
    private int mDividerWidth;
    private int mCornerRadius;
    private List<RoundedImageView> mViewList;
    private ActionListener mActionListener;
    private LayoutParams mLayoutParams00;
    private LayoutParams mLayoutParams10;
    private LayoutParams mLayoutParams11;
    private LayoutParams[] mLayoutParamsArray;
    private View.OnClickListener mOnClickListener;
    private List<?> mDataList;


    public NineGridLayout(Context context) {
        this(context, null);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int space = 0;
        int dividerWidth = 0;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NineGridLayout);
        space = (int) ta.getDimension(R.styleable.NineGridLayout_ngl_space, 0);
        dividerWidth = (int) ta.getDimension(R.styleable.NineGridLayout_ngl_divider_width, 0);
        mCornerRadius = (int) ta.getDimension(R.styleable.NineGridLayout_ngl_corner_radius, 0);
        ta.recycle();
        mContext = context;
        mViewList = new ArrayList<>();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float scale = dm.density;
        int width = dm.widthPixels - space;
        mDividerWidth = dividerWidth;
        mWidth1 = (int) (scale * 220 + 0.5f);
        mWidth2 = (width - dividerWidth) / 2;
        mWidth3 = (width - dividerWidth * 2) / 3;
        mOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionListener != null) {
                    for (int i = 0, size = mViewList.size(); i < size; i++) {
                        if (v == mViewList.get(i)) {
                            mActionListener.onItemClick(mDataList, i);
                            break;
                        }
                    }
                }
            }
        };
    }


    public void setData(List<?> list) {
        if (list == null) {
            hideItem(0);
            return;
        }
        int dataSize = list.size();
        if (dataSize == 0) {
            hideItem(0);
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
        if (dataSize == 1) {
            RoundedImageView imageView = mViewList.get(0);
            if (mLayoutParams00 == null) {
                mLayoutParams00 = new FrameLayout.LayoutParams(mWidth1, mWidth1);
            }
            if (imageView.getLayoutParams() != mLayoutParams00) {
                imageView.setLayoutParams(mLayoutParams00);
            }
            if (imageView.getVisibility() != VISIBLE) {
                imageView.setVisibility(VISIBLE);
            }
            setHeight(mWidth1);
            if (mActionListener != null) {
                mActionListener.displayImage(list.get(0), imageView);
            }
        } else if (dataSize == 2) {
            RoundedImageView imageView0 = mViewList.get(0);
            RoundedImageView imageView1 = mViewList.get(1);
            if (mLayoutParams10 == null) {
                mLayoutParams10 = new FrameLayout.LayoutParams(mWidth2, mWidth2);
            }
            if (mLayoutParams11 == null) {
                mLayoutParams11 = new FrameLayout.LayoutParams(mWidth2, mWidth2);
                mLayoutParams11.gravity = Gravity.RIGHT;
            }
            if (imageView0.getLayoutParams() != mLayoutParams10) {
                imageView0.setLayoutParams(mLayoutParams10);
            }
            if (imageView1.getLayoutParams() != mLayoutParams11) {
                imageView1.setLayoutParams(mLayoutParams11);
            }
            if (imageView0.getVisibility() != VISIBLE) {
                imageView0.setVisibility(VISIBLE);
            }
            if (imageView1.getVisibility() != VISIBLE) {
                imageView1.setVisibility(VISIBLE);
            }
            setHeight(mWidth2);
            if (mActionListener != null) {
                mActionListener.displayImage(list.get(0), imageView0);
                mActionListener.displayImage(list.get(1), imageView1);
            }
        } else {
            if (mLayoutParamsArray == null) {
                mLayoutParamsArray = new LayoutParams[9];
            }
            int rowCount = dataSize / 3;
            if (dataSize % 3 != 0) {
                rowCount++;
            }
            setHeight(mWidth3 * rowCount + mDividerWidth * (rowCount - 1));
            for (int i = 0; i < dataSize; i++) {
                RoundedImageView imageView = mViewList.get(i);
                if (mLayoutParamsArray[i] == null) {
                    mLayoutParamsArray[i] = new FrameLayout.LayoutParams(mWidth3, mWidth3);
                    int row = i / 3;//行
                    int col = i % 3;//列
                    int margin = mWidth3 + mDividerWidth;
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
                RoundedImageView imageView = mViewList.get(i);
                imageView.setImageDrawable(null);
                if (imageView.getVisibility() == VISIBLE) {
                    imageView.setVisibility(INVISIBLE);
                }
            }
        }
    }

    private void addItem() {
        RoundedImageView imageView = new RoundedImageView(mContext);
        imageView.setCornerRadius(mCornerRadius);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(mOnClickListener);
        mViewList.add(imageView);
        addView(imageView);
    }


    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface ActionListener {

        void onItemClick(List<?> dataList, int position);

        void displayImage(Object path, ImageView imageView);
    }


}
