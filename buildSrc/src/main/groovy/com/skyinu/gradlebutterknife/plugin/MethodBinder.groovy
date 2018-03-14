package com.skyinu.gradlebutterknife.plugin

import com.skyinu.annotations.OnClick
import com.skyinu.annotations.OnLongClick
import com.skyinu.gradlebutterknife.plugin.model.MethodBindListenClass
import com.skyinu.gradlebutterknife.plugin.util.BindUtils
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod

import java.lang.annotation.Annotation

/**
 * Created by chen on 2018/3/13.*/

public class MethodBinder {
  ClassPool classPool
  Map<Integer, String> idStringMap
  Map<Integer, String> idFieldMap
  int innerClassClickCount
  int innerClassLongClickCount
  String classPath

  MethodBinder(ClassPool classPool, String classPath, Map<Integer, String> idStringMap,
      Map<Integer, String> idFieldMap) {
    this.classPool = classPool
    this.idStringMap = idStringMap
    this.idFieldMap = idFieldMap
    this.classPath = classPath
  }

  String buildBindMethodCodeBlock(CtClass injectClass, CtMethod injectMethod,
                                  Annotation annotation) {
    def values = annotation.value()
    def methodName = injectMethod.name
    def statement = ""
    values.each {
      def fieldName = idFieldMap.get(it, null)
      statement += "${ConstantList.NAME_TEMP_VIEW} = "
      if (fieldName == null) {
        statement += "${ConstantList.VIEW_SOURCE}.findViewById(${idStringMap.get(it)});\n"
      } else {
        statement += "$fieldName;\n"
      }
      if (annotation instanceof OnClick) {
        statement += MethodBindListenClass.OnClick.buildListenerSetBlock(injectClass)
      } else if (annotation instanceof OnLongClick) {
        statement += MethodBindListenClass.OnLongClick.buildListenerSetBlock(injectClass)
      }
    }
    return statement
  }


  String buildBindMethodStatement(CtClass injectClass, CtMethod injectMethod,
      Annotation annotation) {
    if (!BindUtils.isAnnotationSupport(annotation)) {
      return ""
    }
    def values = annotation.value()
    def methodName = injectMethod.name
    def statement = ""
    values.each {
      def fieldName = idFieldMap.get(it, null)
      statement += "${ConstantList.NAME_TEMP_VIEW} = "
      if (fieldName == null) {
        statement += "${ConstantList.VIEW_SOURCE}.findViewById(${idStringMap.get(it)});\n"
      } else {
        statement += "$fieldName;\n"
      }
      def methodCall = "${ConstantList.NAME_FIELD_OUTER_CLASS}.$methodName"
      if (injectMethod.parameterTypes.length > 0) {
        methodCall += "((${injectMethod.parameterTypes[0].name})view);"
      } else {
        methodCall += "();"
      }
      if (annotation instanceof OnClick) {
        String newClassName = "${ConstantList.NAME_INJECT_INNER_CLASS}${innerClassClickCount}"
        innerClassClickCount++
        statement += MethodBindListenClass.OnClick.buildCodeBlock(injectClass, classPath,
            injectMethod, newClassName, methodCall)
      } else if (annotation instanceof OnLongClick) {
        String newClassName = "${ConstantList.NAME_INJECT_LONG_CLICK_CLASS}${innerClassLongClickCount}"
        innerClassLongClickCount++
        statement += MethodBindListenClass.OnLongClick.buildCodeBlock(injectClass,
            classPath, injectMethod, newClassName, methodCall)
      }
    }
    return statement
  }
}
