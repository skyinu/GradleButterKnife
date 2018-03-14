package com.skyinu.gradlebutterknife.plugin

import com.skyinu.annotations.BindAnim
import com.skyinu.annotations.BindBitmap
import com.skyinu.annotations.BindBool
import com.skyinu.annotations.BindColor
import com.skyinu.annotations.BindDimen
import com.skyinu.annotations.BindInt
import com.skyinu.annotations.BindString
import com.skyinu.annotations.BindView
import com.skyinu.annotations.BindViews
import com.skyinu.gradlebutterknife.plugin.util.BindUtils
import com.skyinu.gradlebutterknife.plugin.util.ClassUtils
import javassist.CtField

import java.lang.annotation.Annotation;

/**
 * Created by chen on 2018/3/13.
 */

public class FieldBinder {

  Map<Integer, String> idStringMap

  FieldBinder(Map<Integer, String> idStringMap) {
    this.idStringMap = idStringMap
  }

  String buildBindFieldStatement(CtField injectField, Annotation annotation) {
    if (!BindUtils.isAnnotationSupport(annotation)) {
      return ""
    }
    if (annotation instanceof BindViews) {
      return buildBindFieldsStatement(injectField, annotation)
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

  String buildBindFieldsStatement(CtField injectField, Annotation annotation) {
    def values = annotation.value()
    def idStrings = new ArrayList<String>()
    for (id in values) {
      idStrings.add(idStringMap.get(id))
    }
    def injectType = injectField.signature
    def fieldName = injectField.name
    def statement = "$fieldName = "
    def count = idStrings.size()
    def isArray = true
    if (injectType.startsWith("[")) {
      injectType = injectType.substring(2, injectType.length() - 1).replace("/", ".")
      statement += "new $injectType[$count];\n"
    } else {
      isArray = false
      statement += "new java.util.ArrayList($count);\n"
    }
    for (int i = 0; i < count; i++) {
      if (isArray) {
        statement +=
            "${fieldName}[$i] = ${ConstantList.VIEW_SOURCE}.findViewById(${idStrings.get(i)});\n"
      } else {
        statement +=
            "${fieldName}.add(${ConstantList.VIEW_SOURCE}.findViewById(${idStrings.get(i)}));\n"
      }
    }
    return statement
  }
}