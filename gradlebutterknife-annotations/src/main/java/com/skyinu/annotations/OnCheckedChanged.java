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

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface OnCheckedChanged {
  @IdRes int[] value() default { View.NO_ID };
}
