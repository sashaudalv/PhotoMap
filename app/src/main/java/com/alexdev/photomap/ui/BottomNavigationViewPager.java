package com.alexdev.photomap.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;


public class BottomNavigationViewPager extends ViewPager {
    private boolean mIsPagingEnabled = false;

    public BottomNavigationViewPager(Context context) {
        super(context);
    }

    public BottomNavigationViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mIsPagingEnabled && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mIsPagingEnabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean executeKeyEvent(KeyEvent event) {
        return mIsPagingEnabled && super.executeKeyEvent(event);
    }

    public void setPagingEnabled(boolean isEnabled) {
        mIsPagingEnabled = isEnabled;
    }

    public boolean isPagingEnabled() {
        return mIsPagingEnabled;
    }
}
