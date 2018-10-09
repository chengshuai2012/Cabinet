package com.zitech.framework.transition.share.callback;

import android.view.View;

import com.zitech.framework.transition.share.helper.Parent;

import java.util.List;
import java.util.Map;

/**
 * Created by fuzhipeng on 2016/11/29.
 */

public interface MapSharedElementsCallback {

    void onMapSharedElements(List<String> names, Map<String, View> sharedElements, Parent parent);
}
