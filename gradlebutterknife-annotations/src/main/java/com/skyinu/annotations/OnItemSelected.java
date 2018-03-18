package com.skyinu.annotations;

import android.support.annotation.IdRes;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * copy from butterKnife-annotation
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface OnItemSelected {
  @IdRes int[] value() default { View.NO_ID };
  Callback callback() default Callback.ON_ITEM_SELECTED;

  enum Callback {
    ON_ITEM_SELECTED,
    ON_NOTHING_SELECTED
  }
}
