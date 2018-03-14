package com.skyinu.annotations;

import android.support.annotation.IdRes;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface OnTextChanged {
  @IdRes int[] value();

  Callback callback() default Callback.TEXT_CHANGED;

  enum Callback {
    TEXT_CHANGED,
    BEFORE_TEXT_CHANGED,
    AFTER_TEXT_CHANGED,
  }
}