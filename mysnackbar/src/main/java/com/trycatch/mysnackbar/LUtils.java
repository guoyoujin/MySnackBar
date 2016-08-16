/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trycatch.mysnackbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(Build.VERSION_CODES.LOLLIPOP) public class LUtils {

  protected Activity mActivity;
  private volatile static LUtils instance;

  private LUtils(Activity activity) {
    mActivity = activity;
  }

  public static LUtils instance(Activity activity) {
    if (instance == null) {
      synchronized (LUtils.class) {
        if (instance == null) {
          instance = new LUtils(activity);
        }
      }
    }
    instance.setActivity(activity);
    return instance;
  }

  public void setActivity(Activity activity) {
    this.mActivity = activity;
  }

  public static void clear() {
    resetColorPrimaryDark();

    if (instance != null) {
      instance.mActivity = null;
      instance = null;
    }

  }

  public static boolean hasL() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
  }

  public static boolean hasKitKat() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
  }

  public static boolean belowKitKat() {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT;
  }

  public int getStatusBarColor() {
    if (belowKitKat()) {
      // On pre-kitKat devices, you can have any status bar color so long as it's black.
      return Color.BLACK;
    }

    if (hasL()) {
      return mActivity.getWindow().getStatusBarColor();
    }

    if (hasKitKat()) {
      ViewGroup contentView = (ViewGroup) mActivity.findViewById(android.R.id.content);
      View statusBarView = contentView.getChildAt(0);
      if (statusBarView != null && statusBarView.getMeasuredHeight() == ScreenUtil.getStatusHeight(
          mActivity)) {
        Drawable drawable = statusBarView.getBackground();
        if (drawable != null) {
          return ((ColorDrawable) drawable).getColor();
        }
      }
    }

    return -1;
  }

  public void setStatusBarColor(int color) {
    if (belowKitKat()) {
      return;
    }

    if (hasL()) {
      mActivity.getWindow().setStatusBarColor(color);
      return;
    }

    if (hasKitKat()) {
      ViewGroup contentView = (ViewGroup) mActivity.findViewById(android.R.id.content);

      View statusBarView = contentView.getChildAt(0);
      //改变颜色时避免重复添加statusBarView
      if (statusBarView != null && statusBarView.getMeasuredHeight() == ScreenUtil.getStatusHeight(
          mActivity)) {
        statusBarView.setBackgroundColor(color);
        return;
      }
      statusBarView = new View(mActivity);
      ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ScreenUtil.getStatusHeight(mActivity));
      statusBarView.setBackgroundColor(color);
      contentView.addView(statusBarView, 0, lp);
    }
  }

  private static final int[] THEME_ATTRS = {
      android.R.attr.colorPrimaryDark, android.R.attr.windowTranslucentStatus
  };

  public static int getDefaultStatusBarBackground(Context context) {
    final TypedArray a = context.obtainStyledAttributes(THEME_ATTRS);
    try {
      return a.getColor(0, Color.TRANSPARENT);
    } finally {
      a.recycle();
    }
  }

  public static boolean getWindowTranslucentStatus(Context context) {
    final TypedArray a = context.obtainStyledAttributes(THEME_ATTRS);

    try {
      return a.getBoolean(1, false);
    } finally {
      a.recycle();
    }
  }


  public static void resetColorPrimaryDark() {
    TSnackbar.setColorPrimaryDark(-1);
  }
}
