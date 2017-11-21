package mpower.org.elearning_module.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by sabbir on 11/21/17.
 *
 * based on
 * https://stackoverflow.com/questions/41656902/disable-swipe-in-fragmentpageradapter-android
 */

public class LockableViewPager extends ViewPager {

    private boolean swipeAble;

    public LockableViewPager(Context context) {
        super(context);
    }

    public LockableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.swipeAble=true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return this.swipeAble && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.swipeAble && super.onInterceptTouchEvent(ev);
    }

    public void setSwipeAble(boolean shouldSwipe){
        this.swipeAble=shouldSwipe;
    }
}
