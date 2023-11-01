package com.yunbao.live.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.live.R;
import com.yunbao.live.bean.LiveBean;

import java.util.List;

/**
 * Created by cxf on 2018/12/13.
 */

public class LiveRoomScrollAdapter extends RecyclerView.Adapter<LiveRoomScrollAdapter.Vh> {

    private Context mContext;
    private List<LiveBean> mList;
    private LayoutInflater mInflater;
    private int mCurPosition;
    private boolean mFirstLoad;
    private boolean mFirstAttached;
    private SparseArray<Vh> mMap;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ActionListener mActionListener;
    private Handler mHandler;

    public LiveRoomScrollAdapter(Context context, List<LiveBean> list, int curPosition) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
        mCurPosition = curPosition;
        mFirstLoad = true;
        mFirstAttached = true;
        mMap = new SparseArray<>();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Vh vh = mMap.get(mCurPosition);
                if (vh != null) {
                    vh.onPageSelected(false);
                }
            }
        };
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_room, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position) {
        if (mList != null) {
            vh.setData(mList.get(position), position);
            if (mFirstLoad) {
                mFirstLoad = false;
                vh.hideCover();
                vh.onPageSelected(true);
            }
        }
    }


    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    class Vh extends RecyclerView.ViewHolder {

        ViewGroup mContainer;
        ImageView mCover;
        LiveBean mLiveBean;

        public Vh(View itemView) {
            super(itemView);
            mContainer = itemView.findViewById(R.id.container);
            mCover = itemView.findViewById(R.id.cover);
        }

        void setData(LiveBean bean, int position) {
            mLiveBean = bean;
            mMap.put(position, this);
            ImgLoader.displayBlur(mContext, bean.getThumb(), mCover);
        }

        /**
         * 滑入屏幕
         */
        void onPageInWindow() {
            if (mCover != null) {
                if (mCover.getVisibility() != View.VISIBLE) {
                    mCover.setVisibility(View.VISIBLE);
                }
                mCover.setImageDrawable(null);
                if (mLiveBean != null) {
                    ImgLoader.displayBlur(mContext, mLiveBean.getThumb(), mCover);
                    if (mActionListener != null) {
                        mActionListener.onPageInWindow(mLiveBean.getThumb());
                    }
                }
            }
        }

        void onPageOutWindow() {
            if (mCover != null && mCover.getVisibility() != View.VISIBLE) {
                mCover.setVisibility(View.VISIBLE);
            }
            if (mActionListener != null) {
                mActionListener.onPageOutWindow(mLiveBean.getUid());
            }
        }

        void onPageSelected(boolean first) {
            if (mActionListener != null) {
                mActionListener.onPageSelected(mLiveBean, mContainer, first);
            }
        }

        void hideCover() {
            if (mCover != null && mCover.getVisibility() == View.VISIBLE) {
                mCover.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void hideCover() {
        Vh vh = mMap.get(mCurPosition);
        if (vh != null) {
            vh.hideCover();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull Vh vh) {
        vh.onPageOutWindow();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull Vh vh) {
        if (mFirstAttached) {
            mFirstAttached = false;
        } else {
            vh.onPageInWindow();
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.scrollToPosition(mCurPosition);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int position = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (position >= 0 && mCurPosition != position) {
                    Vh vh = mMap.get(position);
                    if (vh != null) {
                        vh.onPageSelected(false);
                    }
                    mCurPosition = position;
                }
            }
        });
    }

    public void scrollNextPosition() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mRecyclerView != null && mList != null) {
            if (mCurPosition < mList.size() - 1) {
                mCurPosition++;
                mRecyclerView.scrollToPosition(mCurPosition);
                if (mHandler != null) {
                    mHandler.sendEmptyMessageDelayed(0, 200);
                }
            } else {
                ToastUtil.show(R.string.live_room_last);
            }
        }
    }

    public interface ActionListener {
        void onPageSelected(LiveBean liveBean, ViewGroup container, boolean first);

        void onPageOutWindow(String liveUid);

        void onPageInWindow(String liveThumb);
    }


    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }


    public void release() {
        mActionListener = null;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
    }
}
