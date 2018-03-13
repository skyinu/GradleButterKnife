package com.skyinu.gradlebutterknife.plugin.util

import com.skyinu.annotations.BindAnim
import com.skyinu.annotations.BindBitmap
import com.skyinu.annotations.BindBool
import com.skyinu.annotations.BindColor
import com.skyinu.annotations.BindDimen
import com.skyinu.annotations.BindInt
import com.skyinu.annotations.BindString
import com.skyinu.annotations.BindView
import com.skyinu.gradlebutterknife.plugin.ConstantList
import javassist.CtClass
import javassist.CtField
import java.lang.annotation.Annotation

/**
 * Created by chen on 2018/3/13.*/

public class BindUtils {

  static List<Class> SUPPORTANNOTATIONS = Arrays.asList(BindView.class, BindString.class,
      BindInt.class, BindBool.class, BindColor.class, BindDimen.class, BindBitmap.class,
      BindAnim.class)

  static boolean shouldBindView(CtClass ctClass) {
    ctClass.declaredFields.find {
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
    return false
  }

  static String buildBindFieldStatement(CtField injectField, Annotation annotation,
      Map<Integer, String> idStringMap) {
    if (!isAnnotationSupport(annotation)) {
      return ""
    }
    def value = annotation.value()
    def idString = idStringMap.get(value)
    def fieldName = injectField.name
    def injectType = ""
    if (!ClassUtils.isPrimitive(injectField.signature)) {
      injectType = injectField.signature
      injectType = injectType.substring(1, injectType.length() - 1).replace("/", ".")
    }
    def statement = "$fieldName = "
    if (!injectType.isEmpty()) {
      statement += "($injectType)"
    }

    if (annotation instanceof BindView) {
      statement += "${ConstantList.VIEW_SOURCE}.findViewById($idString);\n"
      return statement
    }
    if (annotation instanceof BindString) {
      statement += "${ConstantList.NAME_RESOURCE_FIELD}.getString($idString);\n"
      return statement
    }
    if (annotation instanceof BindInt) {
      statement += "${ConstantList.NAME_RESOURCE_FIELD}.getInteger($idString);\n"
      return statement
    }
    if (annotation instanceof BindBool) {
      statement += "${ConstantList.NAME_RESOURCE_FIELD}.getBoolean($idString);\n"
      return statement
    }
    if (annotation instanceof BindColor) {
      statement += "${ConstantList.NAME_RESOURCE_FIELD}.getColor($idString, null);\n"
      return statement
    }
    if (annotation instanceof BindDimen) {
      statement += "${ConstantList.NAME_RESOURCE_FIELD}.getDimension($idString);\n"
      return statement
    }
    if (annotation instanceof BindBitmap) {
      statement += "${ConstantList.NAME_RESOURCE_FIELD}.getDrawable($idString);\n"
      return statement
    }
    if (annotation instanceof BindAnim) {
      statement +=
          "${ConstantList.NAME_CLASS_ANIMATIONUTILS}" + ".loadAnimation(${ConstantList.NAME_CONTEXT_FIELD},$idString);\n"
      return statement
    }
  }
}
