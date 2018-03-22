package com.skyinu.annotations;

import android.support.annotation.IdRes;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * copy from butterKnife-annotation
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface OnTextChanged {
  String ON_TEXT_CHANGED = "ON_TEXT_CHANGED";
  String BEFORE_TEXT_CHANGED = "BEFORE_TEXT_CHANGED";
  String AFTER_TEXT_CHANGED = "AFTER_TEXT_CHANGED";
  @IdRes int[] value();

  String callback() default ON_TEXT_CHANGED;
}