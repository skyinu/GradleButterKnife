package com.skyinu.gradlebutterknife.plugin.bind

import com.skyinu.annotations.BindAnim
import com.skyinu.annotations.BindBitmap
import com.skyinu.annotations.BindBool
import com.skyinu.annotations.BindColor
import com.skyinu.annotations.BindDimen
import com.skyinu.annotations.BindInt
import com.skyinu.annotations.BindString
import com.skyinu.annotations.BindView
import com.skyinu.annotations.BindViews
import com.skyinu.gradlebutterknife.plugin.ConstantList
import com.skyinu.gradlebutterknife.plugin.util.BindUtils
import com.skyinu.gradlebutterknife.plugin.util.ClassUtils
import com.skyinu.gradlebutterknife.plugin.util.Log
import javassist.CtClass
import javassist.CtField
import java.lang.annotation.Annotation

/**
 * Created by chen on 2018/3/13.*/

class FieldBinder {
  private Map<Integer, String> idStringMap
  private boolean hasInjectNullCheck

  FieldBinder(Map<Integer, String> idStringMap) {
    this.idStringMap = idStringMap
  }

  def processBindField(CtClass targetClass, String bindMethodSrc, Map<Integer, String> idFieldMap) {
    hasInjectNullCheck = false
    targetClass.declaredFields.each {
      CtField ctField = it
      ctField.annotations.each {
        bindMethodSrc += buildBindFieldStatement(ctField, it as Annotation, idFieldMap)
      }
    }

    return bindMethodSrc
  }

  String buildBindFieldStatement(CtField bindField, Annotation annotation,
      Map<Integer, String> idFieldMap) {
    if (!BindUtils.isAnnotationSupport(annotation)) {
      return ""
    }
    def annoName = annotation.annotationType().name
    Log.info("start handle field ${bindField.name} annotation ${annoName}")
    if (annoName == BindViews.name) {
      return buildBindFieldsStatement(bindField, annotation)
    }
    def value = annotation.value()
    def idString = idStringMap.get(value)
    def fieldName = bindField.name
    def injectType = ""
    if (!ClassUtils.isPrimitive(bindField.signature)) {
      injectType = bindField.signature
      injectType = injectType.substring(1, injectType.length() - 1).replace("/", ".")
    }
    def statement = "$fieldName = "
    if (!injectType.isEmpty()) {
      statement += "($injectType)"
    }

    if (annoName == BindView.name) {
      statement += "${ConstantList.VIEW_SOURCE}.findViewById($idString);\n"
      idFieldMap.put(value, fieldName)
      statement += insertNullCheck(fieldName)
      return statement
    }
    if (annoName == BindString.name) {
      statement += "${ConstantList.NAME_RESOURCE_FIELD}.getString($idString);\n"
      return statement
    }
    if (annoName == BindInt.name) {
      statement += "${ConstantList.NAME_RESOURCE_FIELD}.getInteger($idString);\n"
      return statement
    }
    if (annoName == BindBool.name) {
      statement += "${ConstantList.NAME_RESOURCE_FIELD}.getBoolean($idString);\n"
      return statement
    }
    if (annoName == BindColor.name) {
      statement += "${ConstantList.NAME_RESOURCE_FIELD}.getColor($idString, null);\n"
      return statement
    }
    if (annoName == BindDimen.name) {
      statement += "${ConstantList.NAME_RESOURCE_FIELD}.getDimension($idString);\n"
      return statement
    }
    if (annoName == BindBitmap.name) {
      statement += "${ConstantList.NAME_RESOURCE_FIELD}.getDrawable($idString);\n"
      return statement
    }
    if (annoName == BindAnim.name) {
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
        statement += insertNullCheck("${fieldName}[$i]")
      } else {
        statement +=
            "${fieldName}.add(${ConstantList.VIEW_SOURCE}.findViewById(${idStrings.get(i)}));\n"
        statement += insertNullCheck("${fieldName}.get($i)")
      }
    }
    return statement
  }

  private def insertNullCheck(field) {
    if (hasInjectNullCheck) {
      return ""
    }
    hasInjectNullCheck = true
    return "if($field == null) return;"
  }
}
