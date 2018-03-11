package com.skyinu.annotations;

import android.support.annotation.ArrayRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * copy from butterKnife-annotation
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface BindArray {
    @ArrayRes int value();
}