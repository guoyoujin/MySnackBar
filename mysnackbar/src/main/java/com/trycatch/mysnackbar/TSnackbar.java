package com.trycatch.mysnackbar;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * Created by baiiu on 15/12/4.
 * 信息提示类,SnackBar本质上一个View.用callBack标识他的唯一性
 */
public class TSnackbar implements TSnackbarLayout.onViewAlphaChangedListener {

    private ViewGroup mParent;

    private TSnackbarLayout mView;
    private static boolean coordinatorLayoutFitSystemWindows = false;

    public static final int LENGTH_INDEFINITE = -1;
    public static final int LENGTH_SHORT = -2;
    public static final int LENGTH_LONG = -3;
    public static final int ANIMATION_DURATION = 250;

    private static int SnackBar_layoutResID = R.layout.tsnackbar_view;

    private static int mColorPrimaryDark = -1;
    private static int endA;
    private static int endR;
    private static int endG;
    private static int endB;

    private int mDuration = LENGTH_SHORT;


    private LUtils lUtils;

    private TSnackbar(ViewGroup parent, String message, Prompt type) {
        if (parent != null) {
            mParent = parent;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            mView = (TSnackbarLayout) inflater.inflate(SnackBar_layoutResID, mParent, false);
            setPrompt(type);
            mView.setType(type);
            mView.setMessage(message);
            mView.setOnViewPositionChangedListener(this);

            if (mColorPrimaryDark == -1) {
                mColorPrimaryDark = LUtils.getDefaultStatusBarBackground(parent.getContext());
                endA = (mColorPrimaryDark >> 24) & 0xff;
                endR = (mColorPrimaryDark >> 16) & 0xff;
                endG = (mColorPrimaryDark >> 8) & 0xff;
                endB = mColorPrimaryDark & 0xff;
            }
        } else {
            mView = null;
        }
    }

    public static void setColorPrimaryDark(int mColorPrimaryDark) {
        TSnackbar.mColorPrimaryDark = mColorPrimaryDark;
    }

    @NonNull
    public static TSnackbar make(@NonNull View view, @NonNull String message, @NonNull Prompt prompt) {
        return make(view, message, prompt, LENGTH_SHORT);
    }

    @NonNull
    public static TSnackbar make(@NonNull View view, @NonNull String message, @NonNull Prompt prompt, int duration) {
        TSnackbar snackbar = new TSnackbar(findSuitableParent(view), message, prompt);
        snackbar.setDuration(duration);
        return snackbar;
    }

    @NonNull
    public TSnackbar setAction(final View.OnClickListener listener) {
        final TextView tv = (TextView) mView.findViewById(android.R.id.text1);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
                dismissView();
            }
        });
        return this;
    }

    /**
     * 跟布局为coordinatorLayout,并且添加了fitSystemWindows时,需调用该方法,调整TSnackbar高度
     */
    public static void setCoordinatorLayoutFitsSystemWindows(boolean coordinatorLayoutFitSystemWindows) {
        TSnackbar.coordinatorLayoutFitSystemWindows = coordinatorLayoutFitSystemWindows;
    }

    public static boolean isCoordinatorLayoutFitsSystemWindows() {
        return coordinatorLayoutFitSystemWindows;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
        if (mDuration == LENGTH_INDEFINITE && mView != null) {
            mView.cancelViewDragHelper();
        }
    }

    public int getDuration() {
        return mDuration;
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    return (ViewGroup) view;
                } else {
                    fallback = (ViewGroup) view;
                }
            }
            if (view != null) {
                final ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);

        return fallback;
    }

    public void show() {
        if (mView != null && mView.getContext() instanceof Activity) {
            lUtils = LUtils.instance((Activity) mView.getContext());
        }
        TSnackBarManager.instance().show(mDuration, this);
    }

    public static void setSnackBar_layoutResID(int snackBar_layoutResID) {
        SnackBar_layoutResID = snackBar_layoutResID;
    }

    public void setPrompt(Prompt prompt) {
        if (mView == null) {
            return;
        }

        startColor = mView.getContext().getResources().getColor(prompt.getBackgroundColor());
        startA = (startColor >> 24) & 0xff;
        startR = (startColor >> 16) & 0xff;
        startG = (startColor >> 8) & 0xff;
        startB = startColor & 0xff;
    }

    /***********************************/
    public void showView() {
        if (mView == null || mParent == null) {
            return;
        }
        if (mView.getParent() == null) {
            // TODO: 15/12/5 添加Bahavior
            mParent.addView(mView);
        }

        if (ViewCompat.isLaidOut(mView)) {
            animateViewIn();
        } else {
            mView.setOnLayoutChangeListener(new TSnackbarLayout.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int left, int top, int right, int bottom) {
                    animateViewIn();
                    mView.setOnLayoutChangeListener(null);
                }
            });
        }
    }

    private void animateViewIn() {
        if (mView == null) {
            return;
        }

        Animation anim = AnimationUtils.loadAnimation(mView.getContext(), R.anim.top_in);
        anim.setDuration(ANIMATION_DURATION);
        anim.setInterpolator(new FastOutSlowInInterpolator());
        mView.startAnimation(anim);
        animateStatusBarColor(true);
    }

    public void dismissView() {
        animateViewOut();
    }

    private void animateViewOut() {
        if (mView == null) {
            return;
        }

        Animation anim = AnimationUtils.loadAnimation(mView.getContext(), R.anim.top_out);
        anim.setDuration(ANIMATION_DURATION);
        anim.setInterpolator(new FastOutSlowInInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                clearView();
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        animateStatusBarColor(false);
        mView.startAnimation(anim);
    }

    private void animateStatusBarColor(boolean animateViewIn) {
        if (lUtils == null || !LUtils.hasL() || mDuration == LENGTH_INDEFINITE) {
            return;
        }

        if (animateViewIn) {
            lUtils.setStatusBarColor(startColor);
        } else {
            ObjectAnimator mStatusBarColorAnimator = ObjectAnimator.ofInt(lUtils, "statusBarColor", lUtils.getStatusBarColor(), mColorPrimaryDark)
                    .setDuration(ANIMATION_DURATION);
            mStatusBarColorAnimator.setEvaluator(new ArgbEvaluator());
            mStatusBarColorAnimator.start();
        }
    }

    int startColor;
    int startA;
    int startR;
    int startG;
    int startB;


    @Override
    public void onViewAlphaChanged(float fraction) {
        int color = (startA + (int) (fraction * (endA - startA))) << 24 |
                (startR + (int) (fraction * (endR - startR))) << 16 |
                (startG + (int) (fraction * (endG - startG))) << 8 |
                (startB + (int) (fraction * (endB - startB)));

        if (lUtils != null) {
            lUtils.setStatusBarColor(color);
        }
    }

    public void clearView() {
        if (mParent != null) {
            mParent.removeView(mView);
            mView = null;
            mParent = null;
        }
    }

    public TSnackbarLayout getView() {
        return mView;
    }
}
