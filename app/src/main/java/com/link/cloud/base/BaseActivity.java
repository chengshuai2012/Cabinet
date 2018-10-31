package com.link.cloud.base;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.link.cloud.CabinetApplication;
import com.link.cloud.R;
import com.link.cloud.utils.Utils;
import com.link.cloud.utils.Venueutils;
import com.link.cloud.widget.SimpleStyleDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zitech.framework.utils.ViewUtils;

import io.realm.Realm;
import rx.functions.Action1;

/**
 * Created by OFX002 on 2018/9/20.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener , Venueutils.VenueCallBack{

    private SimpleStyleDialog denyDialog;

    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        this.setContentView(this.getLayoutId());
        realm= Realm.getDefaultInstance();
        CabinetApplication.getVenueUtils().initVenue(this, this, false);
        initViews();
    }

    protected abstract void initViews();

    protected abstract int getLayoutId();

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void onResume() {
        super.onResume();

    }

    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        try {
            CabinetApplication.getVenueUtils().unBindService();
        } catch (Exception e) {
        }
    }


    /**
     * @param cls 目标activity
     *            跳转并finish当前activity
     * @throws ActivityNotFoundException
     */
    public void skipActivity(Class<?> cls) {
        showActivity(cls);
        ViewUtils.anima(ViewUtils.RIGHT_IN, this);
        finish();
    }

    /**
     * @param cls
     * @param extras
     */
    public void skipActivity(Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(this, cls);
        startActivity(intent);
        ViewUtils.anima(ViewUtils.RIGHT_IN, this);
        finish();
    }

    public void showActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        super.startActivityForResult(intent, requestCode);
        ViewUtils.anima(ViewUtils.RIGHT_IN, this);
    }

    public void showActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtras(bundle);
        super.startActivityForResult(intent, requestCode);
        ViewUtils.anima(ViewUtils.RIGHT_IN, this);

    }

    public void showActivity(Class<?> cls) {
        Intent intent = new Intent();


        intent.setClass(this, cls);
        super.startActivity(intent);
        ViewUtils.anima(ViewUtils.RIGHT_IN, this);

    }

    public void showActivity(Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtras(extras);
        super.startActivity(intent);
        ViewUtils.anima(ViewUtils.RIGHT_IN, this);
    }

    @Override
    public void finish() {
        super.finish();
        applyCloseTransition();
    }

    protected void applyCloseTransition() {
        ViewUtils.anima(ViewUtils.RIGHT_IN, this);
    }

    @Override
    public void onClick(View v) {

    }

    //请求权限
    protected void requestRxPermissions(final String denyText, final String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(permissions).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {
                if (granted) {
                    onPermissionGranted(permissions);
                } else {
                    showDeniedDialog(denyText, permissions);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }

    protected void onPermissionGranted(String... permissions) {

    }


    private void showDeniedDialog(final String denyText, final String... permissions) {
        if (denyDialog == null) {
            denyDialog = new SimpleStyleDialog(this, denyText);
            denyDialog.setCancelButtonText("取消");
            denyDialog.setOnPositiveButtonClickListener(new SimpleStyleDialog.OnPositiveButtonClickListener() {
                @Override
                public void onClick(Dialog dialog) {
                    requestRxPermissions(denyText, permissions);
                }
            });
            denyDialog.setPositiveButtonText("去开启");
        } else {
            denyDialog.setContent(denyText);
        }
        if (!denyDialog.isShowing()) {
            denyDialog.show();
        }
    }
//
//    public Fragment showFragment(Class<? extends Fragment> fragmentClass) {
//        return Utils.replace(getSupportFragmentManager(), R.id.content_frame, fragmentClass);
//    }

    public Context getContext() {
        return this;
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)) return;
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
