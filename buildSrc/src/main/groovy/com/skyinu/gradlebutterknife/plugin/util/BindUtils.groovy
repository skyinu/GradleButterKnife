package com.skyinu.gradlebutterknife.plugin.util

import com.skyinu.annotations.BindAnim
import com.skyinu.annotations.BindBitmap
import com.skyinu.annotations.BindBool
import com.skyinu.annotations.BindColor
import com.skyinu.annotations.BindDimen
import com.skyinu.annotations.BindInt
import com.skyinu.annotations.BindString
import com.skyinu.annotations.BindView
import com.skyinu.annotations.BindViews
import com.skyinu.annotations.OnClick
import com.skyinu.annotations.OnLongClick
import com.skyinu.gradlebutterknife.plugin.ConstantList
import javassist.CtClass
import javassist.CtField
import javassist.CtMethod

import java.lang.annotation.Annotation

/**
 * Created by chen on 2018/3/13.
 * */

public class BindUtils {

  static List<Class> SUPPORTANNOTATIONS = Arrays.asList(BindView.class, BindViews.class,
      BindString.class, BindInt.class, BindBool.class, BindColor.class, BindDimen.class,
      BindBitmap.class, BindAnim.class, OnClick.class, OnLongClick.class)

  static boolean shouldBindView(CtClass ctClass) {
    def result = ctClass.declaredFields.find {
      for (annotation in SUPPORTANNOTATIONS) {
        Object anno = it.getAnnotation(annotation)
        if (anno) {
          return true
        }
      }
      return false
    }
    if (result) {
      return true
    }

    return ctClass.declaredMethods.find {
      for (annotation in SUPPORTANNOTATIONS) {
        Object anno = it.getAnnotation(annotation)
        if (anno) {
          return true
        }
      }
      return false
    }
  }

  static boolean isAnnotationSupport(Object annotation) {
    if (annotation instanceof BindView) {
      return true
    }
    if (annotation instanceof BindString) {
      return true
    }
    if (annotation instanceof BindInt) {
      return true
    }
    if (annotation instanceof BindBool) {
      return true
    }
    if (annotation instanceof BindColor) {
      return true
    }
    if (annotation instanceof BindDimen) {
      return true
    }
    if (annotation instanceof BindBitmap) {
      return true
    }
    if (annotation instanceof BindAnim) {
      return true
    }
    if (annotation instanceof BindViews) {
      return true
    }
    if (annotation instanceof OnClick) {
      return true
    }
    if (annotation instanceof OnLongClick) {
      return true
    }
    return false
  }
}
