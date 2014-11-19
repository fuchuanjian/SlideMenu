package com.linonly.livewallpaper.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class GalleryScrollView extends LinearLayout {
    private Scroller mScrollerHelper;

    private VelocityTracker mVelocityTracker;

    private boolean mIsBeingDragged;

    private int mActivePointerId = INVALID_POINTER;

    private float mLastMotionX;

    private static final int INVALID_POINTER = -1;

    private int[] mXy = {
        0, 0
    };

    private boolean mIsUp;


    private int mTouchSlop;

    private int mMinimumVelocity;

    private int mMaximumVelocity;

    private OnBannerStopScroll mBannerOnStopScroll;

    public interface OnBannerStopScroll {
        public abstract void onStopScroll(int x);
    }

    public GalleryScrollView(Context context) {
        this(context, null);
    }

    public GalleryScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScrollerHelper = new Scroller(context);
        setFocusable(true);
        final ViewConfiguration configuration = ViewConfiguration.get(context.getApplicationContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity() * 10;
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    // If we don't have a valid id, the touch down wasn't on
                    // content.
                    break;
                }

                int pointerIndex = ev.findPointerIndex(activePointerId);
                final int pointerCount = ev.getPointerCount();
                try {
                    if (pointerIndex < 0) {
                        pointerIndex = 0;
                    } else if (pointerIndex >= pointerCount) {
                        pointerIndex = pointerIndex == 0 ? 0 : pointerCount - 1;
                    }

                    final float x = ev.getX(pointerIndex);
                    final int xDiff = (int) Math.abs(x - mLastMotionX);
                    if (xDiff > mTouchSlop) {
                        mIsBeingDragged = true;
                        mLastMotionX = x;
                        initVelocityTrackerIfNotExists();
                        mVelocityTracker.addMovement(ev);
                        if (mParent != null)
                            mParent.requestDisallowInterceptTouchEvent(true);
                    }
                } catch (IllegalArgumentException e) {
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                if (getChildCount() == 0) {
                    return false;
                }
                mIsUp = false;
                requestViewPagerDisallowInterceptTouchEvent();
                mLastMotionX = ev.getX();
                mActivePointerId = ev.getPointerId(0);
                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);
                mIsBeingDragged = !mScrollerHelper.isFinished();
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                /* Release the drag */
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = ev.getActionIndex();
                mLastMotionX = ev.getX(index);
                mActivePointerId = ev.getPointerId(index);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                mLastMotionX = ev.getX(ev.findPointerIndex(mActivePointerId));
                break;
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(ev);

        final int action = ev.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                mIsUp = false;
                if (getChildCount() == 0) {
                    return false;
                }

                if ((mIsBeingDragged = !mScrollerHelper.isFinished())) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                requestViewPagerDisallowInterceptTouchEvent();

                if (!mScrollerHelper.isFinished()) {
                    mScrollerHelper.abortAnimation();
                }

                // Remember where the motion event started
                mLastMotionX = ev.getX();
                mActivePointerId = ev.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                final int pointerCount = ev.getPointerCount();
                try {
                    if (activePointerIndex < 0) {
                        activePointerIndex = 0;
                    } else if (activePointerIndex >= pointerCount) {
                        activePointerIndex = activePointerIndex == 0 ? 0 : pointerCount - 1;
                    }

                    final float x = ev.getX(activePointerIndex);
                    int deltaX = (int) (mLastMotionX - x);
                    if (!mIsBeingDragged && Math.abs(deltaX) > mTouchSlop) {
                        final ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                        mIsBeingDragged = true;
                        if (deltaX > 0) {
                            deltaX -= mTouchSlop;
                        } else {
                            deltaX += mTouchSlop;
                        }
                    }

                    if (mIsBeingDragged) {
                        mLastMotionX = x;
                        // Scroll to follow the motion event

                        final int oldX = mScrollX;
                        final int oldY = mScrollY;
                        final int range = getScrollRange();
                        if (overScrollBy(deltaX, 0, mScrollX, 0, range, 0, 0, 0, true)) {
                            // Break our velocity if we hit a scroll barrier.
                            mVelocityTracker.clear();
                        }
                        onScrollChanged(mScrollX, mScrollY, oldX, oldY);
                    }
                } catch (IllegalArgumentException e) {
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    mIsUp = true;
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = (int) velocityTracker.getXVelocity(mActivePointerId);

                    if (getChildCount() > 0) {
                        if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                            fling(-initialVelocity);
                        } else {
                            revertBannerLayout();
                        }
                    }

                    mActivePointerId = INVALID_POINTER;
                    mIsBeingDragged = false;
                    recycleVelocityTracker();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged && getChildCount() > 0) {
                    mIsUp = true;
                    revertBannerLayout();
                    mActivePointerId = INVALID_POINTER;
                    mIsBeingDragged = false;
                    recycleVelocityTracker();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
        }
        return true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionX = (int) ev.getX(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    private void requestViewPagerDisallowInterceptTouchEvent() {

    	//todo
//        ViewParent parent = getParent();
//        while (parent != null) {
//            if (parent instanceof ViewPager) {
//                parent.requestDisallowInterceptTouchEvent(true);
//                break;
//            }
//            parent = parent.getParent();
//        }
    }

    private int getScrollRange() {
        int scrollRange = 0;
        int count = getChildCount();
        if (count > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0, child.getWidth() * count - (getWidth() - mPaddingLeft - mPaddingRight));
        }
        return scrollRange;
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public void computeScroll() {
        // Log.d("@@@@", "---- compute scroll!");
        if (mScrollerHelper.computeScrollOffset()) {
            scrollTo(mScrollerHelper.getCurrX(), mScrollerHelper.getCurrY());
            postInvalidate();
            awakenScrollBars();
        } else if (mIsUp) {
            revertBannerLayout();
        }
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        int newScrollX = scrollX + deltaX;
        // Clamp values if at the limits and record
        final int left = -maxOverScrollX;
        final int right = maxOverScrollX + scrollRangeX;

        boolean clampedX = false;
        if (newScrollX > right) {
            newScrollX = scrollX + deltaX / 2;
            clampedX = true;
        } else if (newScrollX < left) {
            newScrollX = scrollX + deltaX / 2;
            clampedX = true;
        }

        boolean clampedY = false;

        onOverScrolled(newScrollX, scrollY, clampedX, clampedY);

        return clampedX || clampedY;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (!mScrollerHelper.isFinished()) {
            mScrollX = scrollX;
            mScrollY = scrollY;
            if (clampedX) {
            }
        } else {
        }
        scrollTo(scrollX, scrollY);
        invalidate();
        awakenScrollBars();
    }

    private void revertBannerLayout() {
        getChildAt(0).getLocationOnScreen(mXy);
        int[] xy = new int[2];
        getLocationOnScreen(xy);
        int x = mXy[0];
        int width = getChildAt(0).getWidth();

        if (x < xy[0] + 3 && x > (xy[0] + getWidth() - width * getChildCount() - 3)) {
            mIsUp = false;
            if (mBannerOnStopScroll != null) {
                mBannerOnStopScroll.onStopScroll(Math.abs((Math.abs(x) + 3) / width));
            }
        } else if (mIsUp) {
            revertBannerLayout(x);
        }
    }

    private void revertBannerLayout(int x) {
        if (x > 0) {
            startScrolTo(0, 300);
        } else {
            int width = getChildAt(0).getWidth();
            int newX = Math.abs(x);
            int maxX = width * getChildCount() - getWidth();
            if (newX > maxX) {
                newX = maxX;
            }
            startScrolTo(newX, 300);
        }

    }

    private void startScrolTo(int newX, int duration) {
        int oldX = getScrollX();
        int moveX = newX - oldX;
        if (duration != 0) {
            mScrollerHelper.startScroll(oldX, 0, moveX, 0, duration);
        } else {
            mScrollerHelper.startScroll(oldX, 0, moveX, 0);
        }
        invalidate();
    }

    public void fling(int velocityX) {

        getChildAt(0).getLocationOnScreen(mXy);
        int oldX = mXy[0];
        int move = velocityX / 10;
        int newX = oldX - move;
        int width = getChildAt(0).getWidth() * getChildCount();
        final int right = width;
        if (newX < -right) {
            newX = newX - (newX - right) / 3;
        } else if (newX > 0) {
            newX = newX / 3;
        }
        revertBannerLayout(newX);
    }

    public void setBannerOnStopScrollListener(OnBannerStopScroll l) {
        mBannerOnStopScroll = l;
    }
}
