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
import com.skyinu.annotations.OnCheckedChanged
import com.skyinu.annotations.OnClick
import com.skyinu.annotations.OnEditorAction
import com.skyinu.annotations.OnFocusChange
import com.skyinu.annotations.OnItemClick
import com.skyinu.annotations.OnItemLongClick
import com.skyinu.annotations.OnItemSelected
import com.skyinu.annotations.OnLongClick
import com.skyinu.annotations.OnTextChanged
import com.skyinu.annotations.OnTouch
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
      BindBitmap.class, BindAnim.class, OnClick.class, OnLongClick.class, OnTextChanged.class,
      OnCheckedChanged.class, OnEditorAction.class, OnFocusChange.class, OnItemClick.class,
      OnItemLongClick.class, OnItemSelected.class, OnTouch.class)

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

  static boolean isAnnotationSupport(Annotation annotation) {
    def name = annotation.annotationType().name
    return SUPPORTANNOTATIONS.find {
      return name == it.name
    }
  }
}
