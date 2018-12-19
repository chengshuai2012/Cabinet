package com.zitech.framework;


import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.zitech.framework.utils.Utils;

import java.io.File;

/**
 * Created by lu on 2016/6/12.
 */
public abstract class BaseApplication extends MultiDexApplication {
    private static BaseApplication mInstance;

    private static final String DOWNLOAD_DIR_NAME = "common_cache";
    private static final String UPLOAD_DIR_NAME = "upload_dir";

    public static BaseApplication getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public File getUploadCacheDir() {
        return Utils.getExistCacheDir(UPLOAD_DIR_NAME);

    }

    public File getDownloadCacheDir() {
        return Utils.getExistCacheDir(DOWNLOAD_DIR_NAME);
    }

}
