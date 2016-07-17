package vn.com.vshome.flexibleadapter;

import android.content.Context;

import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;

/**
 * Created by anlab on 7/15/16.
 */
public class ControlLayoutManager extends SmoothScrollLinearLayoutManager{

    private boolean isScrollEnable = true;

    public void setScrollEnable(boolean scrollEnable) {
        isScrollEnable = scrollEnable;
    }

    public ControlLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnable && super.canScrollVertically();
    }
}
