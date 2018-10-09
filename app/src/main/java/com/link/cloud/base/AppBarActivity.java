package com.link.cloud.base;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.link.cloud.R;
import com.link.cloud.utils.Utils;
import com.link.cloud.widget.ToolBarHelper;
import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 * Created by lu on 2016/6/14.
 */
public abstract class AppBarActivity extends BaseActivity {
    private ToolBarHelper toolBarHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(true);
        setSystemBarTintDrawableResource(R.drawable.bg_toolbar);
    }

    public void setToolBarHelperHeight(int helperHeight) {
        toolBarHelper.setmToolBarHeight(helperHeight);

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        toolBarHelper = new ToolBarHelper(this, layoutResID);
        super.setContentView(toolBarHelper.getContentView());
        /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolBarHelper.getToolBar());
//        toolBarHelper.setTitle(getTitle());
        initToolBarEvent();
    }

    /**
     * use SytemBarTintManager
     *
     * @param tintDrawable
     */
    protected void setSystemBarTintDrawable(Drawable tintDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            if (tintDrawable != null) {
//                mTintManager.setStatusBarTintEnabled(true);
//                mTintManager.setTintDrawable(tintDrawable);
                mTintManager.setStatusBarTintEnabled(true);
                mTintManager.setTintDrawable(tintDrawable);
            } else {
                mTintManager.setStatusBarTintEnabled(false);
                mTintManager.setTintDrawable(null);
            }
        }
    }

    protected void setSystemBarTintDrawableResource(int resId) {
        setSystemBarTintDrawable(getResources().getDrawable(resId));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //在Swipebacklayout里面的attachToActivity方法里面再用SystemBarTint设置一下状态栏的颜色即可。
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.drawable.bg_toolbar);
        setSystemBarTintDrawableResource(R.drawable.bg_toolbar);
    }

//    @Override
//    public void setTitle(CharSequence title) {
////        super.setTitle(title);
//        if (toolBarHelper != null) {
//            toolBarHelper.setTitle(title);
//        }
//    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        if (toolBarHelper != null) {
            toolBarHelper.setTitle(titleId);
        }
    }


    private void initToolBarEvent() {
        toolBarHelper.setNavigationOnClickListener(onNavigationClickListener);
        toolBarHelper.setNavigationTextClickListener(onNavigationClickListener);
        toolBarHelper.setOnMenuTextClickListener(onMenuClickListener);
    }

    protected void hideNavigationIcon() {
        toolBarHelper.hideNavigationIcon();
    }

    public void hideToolbar() {
        getSupportActionBar().hide();
        toolBarHelper.hideToolBar();
    }

    protected void setMenuIconVisible(boolean visible) {
        toolBarHelper.setMenuIconVisible(visible);
        supportInvalidateOptionsMenu();
    }

    protected void setMenuText(int textResId) {
        toolBarHelper.setMenuTextVisibility(View.VISIBLE);
        toolBarHelper.setMenuText(textResId);
    }

    protected void setMenuText(String title) {
        toolBarHelper.setMenuTextVisibility(View.VISIBLE);
        toolBarHelper.setMenuText(title);
    }

    protected void setMenuTextVisibility(int visibility) {
        toolBarHelper.setMenuTextVisibility(visibility);
    }


    protected void setMenuIcon(int drawableResId) {
        toolBarHelper.setMenuIcon(drawableResId);
    }

    protected void setNavigationIcon(int imgId) {
        toolBarHelper.setNavigationTextVisibility(View.GONE);
        toolBarHelper.setNavigationIcon(imgId);
    }

    public void setNavigationText(String text) {
        toolBarHelper.setNavigationTextVisibility(View.VISIBLE);
        toolBarHelper.setNavigationText(text);
        toolBarHelper.setNavigationIcon(null);
    }


    public View.OnClickListener onNavigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onNavigationViewClick();
        }
    };

    protected void onNavigationViewClick() {
        back();
    }

    public View.OnClickListener onMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onActionMenuViewClick();
        }
    };

    protected void back() {
        try {
            onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (toolBarHelper.hasMenuIcon()) {
            menu.add(Menu.NONE, R.id.menu, 0, null)
                    .setEnabled(true)
                    .setIcon(toolBarHelper.getMenuIcon())
                    .setActionView(null)
//                    .setShowAsActionFlags()
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        Utils.ignoreMenuLongClick(this, R.id.menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (toolBarHelper.hasMenuIcon()) {
            menu.findItem(R.id.menu).setVisible(toolBarHelper.isMenuIconVisible());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toolBarHelper.hasMenuIcon()) {
            if (item.getItemId() == R.id.menu) {
                onActionMenuViewClick();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActionMenuViewClick() {

    }

    public Toolbar getToolbar() {
        return toolBarHelper.getToolBar();
    }
}
