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
  String ON_ITEM_SELECTED = "ON_ITEM_SELECTED";
  String  ON_NOTHING_SELECTED = "ON_NOTHING_SELECTED";
  @IdRes int[] value() default { View.NO_ID };
  String callback() default ON_ITEM_SELECTED;
}
