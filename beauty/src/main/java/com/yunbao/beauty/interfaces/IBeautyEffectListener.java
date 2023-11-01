package com.yunbao.beauty.interfaces;

public interface IBeautyEffectListener {
    void onMeiYanChanged(int meiBai, boolean meiBaiChanged, int moPi, boolean moPiChanged, int hongRun, boolean hongRunChanged);

    void onFilterChanged(int filterName);

    /**
     * 是否使用美狐的滤镜
     */
    boolean isUseMhFilter();

    boolean isTieZhiEnable();
}
