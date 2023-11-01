package com.yunbao.common.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.R;
import com.yunbao.common.adapter.ChooseImageAdapter;
import com.yunbao.common.bean.ChooseImageBean;
import com.yunbao.common.custom.ItemDecoration;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.interfaces.ImageResultCallback;
import com.yunbao.common.utils.ChooseImageUtil;
import com.yunbao.common.utils.MediaUtil;
import com.yunbao.common.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChooseImageActivity extends AbsActivity implements ChooseImageAdapter.ActionListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ChooseImageAdapter mAdapter;
    private ChooseImageUtil mChooseImageUtil;
    private View mBtnDone;
    private TextView mDoneText;
    private TextView mCount;
    private int mMaxCount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_image;
    }


    @Override
    protected void main() {
        mMaxCount = getIntent().getIntExtra(Constants.MAX_COUNT, 9);
        mBtnDone = findViewById(R.id.btn_done);
        mBtnDone.setOnClickListener(this);
        mBtnDone.setClickable(false);
        mDoneText = findViewById(R.id.done_text);
        mCount = findViewById(R.id.count);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 5, 5);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRecyclerView.addItemDecoration(decoration);
        mChooseImageUtil = new ChooseImageUtil();
        mChooseImageUtil.getLocalImageList(new CommonCallback<List<ChooseImageBean>>() {
            @Override
            public void callback(List<ChooseImageBean> imageList) {
                if (mContext != null && mRecyclerView != null) {
                    List<ChooseImageBean> list = new ArrayList<>();
                    list.add(new ChooseImageBean(ChooseImageBean.CAMERA));
                    if (imageList != null && imageList.size() > 0) {
                        list.addAll(imageList);
                    } else {
                        View noData = findViewById(R.id.no_data);
                        if (noData != null && noData.getVisibility() != View.VISIBLE) {
                            noData.setVisibility(View.VISIBLE);
                        }
                    }
                    mAdapter = new ChooseImageAdapter(mContext, list, mMaxCount);
                    mAdapter.setActionListener(ChooseImageActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

    @Override
    public void onCameraClick() {
        MediaUtil.getImageByCamera(this, new ImageResultCallback() {
            @Override
            public void beforeCamera() {

            }

            @Override
            public void onSuccess(File file) {
                ArrayList<String> list = new ArrayList<>();
                list.add(file.getAbsolutePath());
                Intent intent = new Intent();
                intent.putStringArrayListExtra(Constants.CHOOSE_IMG, list);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public void onCheckedCountChanged(int checkedCount) {
        if (checkedCount == 0) {
            if (mCount.getVisibility() == View.VISIBLE) {
                mCount.setVisibility(View.GONE);
            }
            mDoneText.setTextColor(0xff969696);
            mBtnDone.setClickable(false);
        } else {
            if (mCount.getVisibility() != View.VISIBLE) {
                mCount.setVisibility(View.VISIBLE);
            }
            mCount.setText(StringUtil.contact("(", String.valueOf(checkedCount), "/", String.valueOf(mMaxCount), ")"));
            mDoneText.setTextColor(0xff333333);
            mBtnDone.setClickable(true);
        }
    }

    @Override
    protected void onDestroy() {
        if (mChooseImageUtil != null) {
            mChooseImageUtil.release();
        }
        mChooseImageUtil = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_done) {
            getImagePathList();
        }
    }

    private void getImagePathList() {
        if (mAdapter == null) {
            return;
        }
        ArrayList<String> list = mAdapter.getImagePathList();
        if (list == null || list.size() == 0) {
            return;
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra(Constants.CHOOSE_IMG, list);
        setResult(RESULT_OK, intent);
        finish();
    }
}
