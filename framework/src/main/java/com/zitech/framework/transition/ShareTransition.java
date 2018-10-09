package com.zitech.framework.transition;

import android.app.Activity;
import android.content.Intent;

import com.zitech.framework.transition.share.From;
import com.zitech.framework.transition.share.ShareHelper;
import com.zitech.framework.transition.share.To;


/**
 * Created by fuzhipeng on 2016/11/29.
 * <p>
 */

public class ShareTransition {

    public static From from(Activity activity) {
        return ShareHelper.from(activity);
    }


    public static boolean onActivityReenter(Activity activity, int resultCode, Intent data) {
        return ShareHelper.onActivityReenter(activity,resultCode,data);
    }

    public static To to(Activity activity) {
        return ShareHelper.to(activity);
    }

    public static boolean finishAfterTransition(Activity activity) {
        return ShareHelper.finishAfterTransition(activity);
    }

    public static void destory(Activity activity) {
        ShareHelper.destory(activity);
    }

}
