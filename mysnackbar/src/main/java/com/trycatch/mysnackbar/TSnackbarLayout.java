package com.trycatch.mysnackbar;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * author: baiiu
 * date: on 16/2/22 10:52
 * description:
 */
public class TSnackbarLayout extends LinearLayout {
    private OnLayoutChangeListener mOnLayoutChangeListener;
    private onViewAlphaChangedListener mOnViewPositionChangedListener;
    private ViewDragHelper mDragHelper;

    private float mDragDismissThreshold = 0.5f;
    private float mAlphaStartSwipeDistance = 0.1F;
    private float mAlphaEndSwipeDistance = 0.8F;

    private static boolean windowTranslucentStatus = false;

    private View container;
    private TextView textView;

    public TSnackbarLayout(Context context) {
        this(context, null);
    }

    public TSnackbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TSnackbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mDragHelper = ViewDragHelper.create(this, new TSnackbarCallBack());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        container = findViewById(R.id.ll_container);
        textView = (TextView) findViewById(android.R.id.text1);

        if (TSnackbarLayout.windowTranslucentStatus || LUtils.getWindowTranslucentStatus(getContext())) {
            TSnackbarLayout.windowTranslucentStatus = true;
            container.setPadding(0, ScreenUtil.getStatusHeight(getContext()), 0, 0);
            return;
        }

        if (TSnackbar.isCoordinatorLayoutFitsSystemWindows()) {
            container.setPadding(0, ScreenUtil.getStatusHeight(getContext()), 0, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && mOnLayoutChangeListener != null) {
            mOnLayoutChangeListener.onLayoutChange(this, l, t, r, b);
        }
    }

    public void setType(Prompt type) {
        if (type == Prompt.SUCCESS) {
            textView.setCompoundDrawablesWithIntrinsicBounds(Prompt.SUCCESS.getResIcon(), 0, 0, 0);
            container.setBackgroundColor(getResources().getColor(Prompt.SUCCESS.getBackgroundColor()));
        } else if (type == Prompt.ERROR) {
            textView.setCompoundDrawablesWithIntrinsicBounds(Prompt.ERROR.getResIcon(), 0, 0, 0);
            container.setBackgroundColor(getResources().getColor(Prompt.ERROR.getBackgroundColor()));
        } else if (type == Prompt.WARNING) {
            textView.setCompoundDrawablesWithIntrinsicBounds(Prompt.WARNING.getResIcon(), 0, 0, 0);
            container.setBackgroundColor(getResources().getColor(Prompt.WARNING.getBackgroundColor()));
        }
    }

    public void setMessage(String message) {
        textView.setText(message);
    }

    public void cancelViewDragHelper() {
        if (mDragHelper != null) {
            mDragHelper.cancel();
            mDragHelper = null;
        }
    }

    public void setOnViewPositionChangedListener(onViewAlphaChangedListener onViewPositionChangedListener) {
        mOnViewPositionChangedListener = onViewPositionChangedListener;
    }

    interface onViewAlphaChangedListener {
        void onViewAlphaChanged(float fraction);
    }

    interface OnLayoutChangeListener {
        void onLayoutChange(View view, int left, int top, int right, int bottom);
    }

    void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
        mOnLayoutChangeListener = onLayoutChangeListener;
    }


    private class TSnackbarCallBack extends ViewDragHelper.Callback {
        private int mOriginalCapturedViewLeft;

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return container == child;
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            mOriginalCapturedViewLeft = capturedChild.getLeft();//最初距离左边的距离
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final int childWidth = releasedChild.getWidth();
            int targetLeft;
            //矢量
            if (shouldDismiss(releasedChild, xvel)) {
                targetLeft = releasedChild.getLeft() < mOriginalCapturedViewLeft
                        ? mOriginalCapturedViewLeft - childWidth
                        : mOriginalCapturedViewLeft + childWidth;
            } else {
                targetLeft = mOriginalCapturedViewLeft;
            }

            smoothScrollToX(targetLeft);
        }

        private boolean shouldDismiss(View child, float xvel) {
            if (xvel != 0) {
                //滑动时松开,判断速度
                return true;
            } else {
                //静止时松开,判断距离
                final int distance = child.getLeft() - mOriginalCapturedViewLeft;
                final int thresholdDistance = Math.round(child.getWidth() * mDragDismissThreshold);
                return Math.abs(distance) >= thresholdDistance;
            }
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            final float startAlphaDistance = mOriginalCapturedViewLeft
                    + changedView.getWidth() * mAlphaStartSwipeDistance;
            final float endAlphaDistance = mOriginalCapturedViewLeft
                    + changedView.getWidth() * mAlphaEndSwipeDistance;

            left = Math.abs(left);
            float alpha;
            if (left <= startAlphaDistance) {
                alpha = 1F;
            } else if (left >= endAlphaDistance) {
                alpha = 0F;
            } else {
                final float distance = fraction(startAlphaDistance, endAlphaDistance, left);
                alpha = clamp(0f, 1f - distance, 1f);
            }

            ViewCompat.setAlpha(changedView, alpha);
            if (mOnViewPositionChangedListener != null && LUtils.hasL()) {
                mOnViewPositionChangedListener.onViewAlphaChanged(1 - alpha);
            }
        }

        @Override
        public void onViewDragStateChanged(int state) {
            switch (state) {
                case ViewDragHelper.STATE_SETTLING:
                case ViewDragHelper.STATE_DRAGGING:
                    TSnackBarManager.instance().cancelTimeout();
                    break;
                case ViewDragHelper.STATE_IDLE:
                    TSnackBarManager.instance().restoreTimeout();
                    break;
            }
        }
    }

    private void smoothScrollToX(int targetLeft) {
        if (mDragHelper != null && mDragHelper.settleCapturedViewAt(targetLeft, 0)) {
            ViewCompat.postInvalidateOnAnimation(TSnackbarLayout.this);
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper != null && mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return;
        }

        if (ViewCompat.getAlpha(container) == 0) {
            TSnackBarManager.instance().clearCurrentSnackbar();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper != null && mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDragHelper != null) {
            mDragHelper.processTouchEvent(event);
        }
        return true;
    }

    private float fraction(float startValue, float endValue, float value) {
        return (value - startValue) / (endValue - startValue);
    }

    private float clamp(float min, float value, float max) {
        return Math.min(Math.max(min, value), max);
    }
}
