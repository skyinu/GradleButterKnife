package com.skyinu.gradlebutterknife;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.View;

/**
 * Created by chen on 2018/3/9.
 */

public class GradleButterKnife {
  @UiThread
  public static void bind(@NonNull Activity source) {
    Injectable injectable = (Injectable) source;
    injectable.inject(source.getWindow().getDecorView());
  }

  @UiThread
  public static void bind(@NonNull Object target, @NonNull View source) {
    Injectable injectable = (Injectable) target;
    injectable.inject(source);
  }

}
