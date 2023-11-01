package com.yunbao.common.utils;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yunbao.common.fragment.ProcessFragment;
import com.yunbao.common.interfaces.ActivityResultCallback;

import java.util.List;

/**
 * startActivityForResult回调
 */
public class ActivityResultUtil {

    public static void startActivityForResult(FragmentActivity activity, Intent intent, ActivityResultCallback callback) {
        ProcessFragment processFragment = null;
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        List<Fragment> list = fragmentManager.getFragments();
        if (list != null && list.size() > 0) {
            for (Fragment fragment : list) {
                if (fragment != null && fragment instanceof ProcessFragment) {
                    processFragment = (ProcessFragment) fragment;
                    break;
                }
            }
        }
        if (processFragment == null) {
            processFragment = new ProcessFragment();
            FragmentTransaction tx = fragmentManager.beginTransaction();
            tx.add(processFragment, "ProcessFragment").commitNow();
        }
        processFragment.startActivityForResult(intent, callback);
    }

}
