package com.link.cloud.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zitech.framework.BaseApplication;
import com.zitech.framework.utils.NetworkUtil;
import com.zitech.framework.utils.ViewUtils;

/**
 * 基本类
 *
 * @author Ludaiqian
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private BaseApplication mApplicationContext;
    private View mLayoutView;
    private boolean mCompleted = false;
    private LayoutInflater mInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplicationContext = BaseApplication.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mLayoutView == null) {
            mLayoutView = inflate(inflater);
            onInflateView(mLayoutView);
            onPrepareData();
            mCompleted = true;
        } else {
            ViewGroup parent = (ViewGroup) mLayoutView.getParent();
            if (parent != null)
                parent.removeView(mLayoutView);
            onRefreshData();
        }
        mInflater = inflater;
        return mLayoutView;

    }


    protected View inflate(LayoutInflater inflater) {
        return inflater.inflate(getContentViewId(), null);
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    protected LayoutInflater getInflater() {
        return mInflater;
    }

    public View getContentView() {
        return mLayoutView;
    }

    /**
     * 只运行一次
     */
    public void onInflateView(View contentView) {

    }

    /**
     * 只运行一次
     */
    public void onPrepareData() {
    }

    public void onRefreshData() {

    }

    protected abstract int getContentViewId();

    public final boolean isNetworkAvailable() {
        return NetworkUtil.isNetworkAvailable(getActivity());
    }



    protected void back() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
    }


    /**
     * Fragment跳转， 将一个layout替换为新的fragment。
     *
     * @param fm
     * @param fragmentClass
     * @param replaceLayoutId
     * @param args
     */
    public static void replace(FragmentManager fm, Class<? extends Fragment> fragmentClass, int replaceLayoutId, Bundle args) {
        replace(fm, fragmentClass, replaceLayoutId, fragmentClass.getSimpleName(), args);
    }

    /**
     * Fragment跳转， 将一个layout替换为新的fragment。
     *
     * @param fm
     * @param replaceLayoutId
     * @param fragmentClass
     * @return
     */
    public static Fragment replace(FragmentManager fm, int replaceLayoutId, Class<? extends Fragment> fragmentClass) {
        return replace(fm, fragmentClass, replaceLayoutId, fragmentClass.getSimpleName(), null);
    }

    /**
     * Fragment跳转， 将一个layout替换为新的fragment。
     *
     * @param fm
     * @param fragmentClass
     * @param tag
     * @param args
     * @return
     */
    public static Fragment replace(FragmentManager fm, Class<? extends Fragment> fragmentClass, int replaceLayoutId, String tag,
                                   Bundle args) {
        // mIsCanEixt = false;
        Fragment fragment = fm.findFragmentByTag(tag);
        boolean isFragmentExist = true;
        if (fragment == null) {
            try {
                isFragmentExist = false;
                fragment = fragmentClass.newInstance();
                if (args != null)
                    fragment.setArguments(args);
                else {
                    fragment.setArguments(new Bundle());
                }

            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            if (args != null) {
                if (fragment.getArguments() != null)
                    fragment.getArguments().putAll(args);
                else
                    fragment.setArguments(args);
            }
        }
        if (fragment == null)
            return null;
        if (fragment.isAdded()) {
            // fragment.onResume();
            return fragment;
        }
        FragmentTransaction ft = fm.beginTransaction();
        if (isFragmentExist) {
            ft.replace(replaceLayoutId, fragment);
        } else {
            ft.replace(replaceLayoutId, fragment, tag);
        }

        ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
        return fragment;
    }


    public void showActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        super.startActivity(intent);
        ViewUtils.anima(ViewUtils.RIGHT_IN, getActivity());

    }

    public void showActivity(Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        intent.putExtras(extras);
        super.startActivity(intent);
        ViewUtils.anima(ViewUtils.RIGHT_IN, getActivity());
    }

    public void showActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        super.startActivityForResult(intent, requestCode);
        ViewUtils.anima(ViewUtils.RIGHT_IN, getActivity());
    }

    public void showActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        intent.putExtras(bundle);
        super.startActivityForResult(intent, requestCode);
        ViewUtils.anima(ViewUtils.RIGHT_IN, getActivity());
    }
    @Override
    public void onClick(View v) {

    }

}
