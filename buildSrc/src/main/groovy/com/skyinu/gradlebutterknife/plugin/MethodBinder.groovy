package com.skyinu.gradlebutterknife.plugin

import com.skyinu.annotations.OnClick
import com.skyinu.annotations.OnLongClick
import com.skyinu.gradlebutterknife.plugin.model.MethodBindListenClass
import com.skyinu.gradlebutterknife.plugin.model.MethodCallModel
import com.skyinu.gradlebutterknife.plugin.util.BindUtils
import com.skyinu.gradlebutterknife.plugin.util.ClassUtils
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
  String classPath

  MethodBinder(ClassPool classPool, String classPath, Map<Integer, String> idStringMap,
               Map<Integer, String> idFieldMap) {
    this.classPool = classPool
    this.idStringMap = idStringMap
    this.idFieldMap = idFieldMap
    this.classPath = classPath
  }

  String buildSetCodeBlock(CtClass injectClass, Annotation annotation) {
    if (!BindUtils.isAnnotationSupport(annotation)) {
      return ""
    }
    def statement = ""
    annotation.value().each {
      String target = idFieldMap.get(it)
      if(target == null || target.empty){
        statement += "${ConstantList.NAME_TEMP_VIEW} = ${ConstantList.VIEW_SOURCE}.findViewById(${idStringMap.get(it)});\n"
      }
      else{
        statement += "${ConstantList.NAME_TEMP_VIEW} = ${target};\n"
      }
      if (annotation instanceof OnClick) {
        statement += MethodBindListenClass.OnClick.buildListenerSetBlock(injectClass)
      } else if (annotation instanceof OnLongClick) {
        statement += MethodBindListenClass.OnLongClick.buildListenerSetBlock(injectClass)
      }
    }
    return statement
  }

  def buildMethodCallCodeBlock(List<MethodCallModel> callModelList, CtMethod injectMethod,
                                  Annotation annotation) {
    def methodName = injectMethod.name
    annotation.value().each {
      def methodCallModel = new MethodCallModel(annotation)
      def methodCallCode = "${ConstantList.NAME_FIELD_OUTER_CLASS}.$methodName"
      if ((annotation instanceof OnClick) || (annotation instanceof OnLongClick)) {
        if (injectMethod.parameterTypes.length > 0) {
          methodCallCode += "((${injectMethod.parameterTypes[0].name})view);"
        } else {
          methodCallCode += "();"
        }
        if (!ClassUtils.isVoidType(injectMethod.getReturnType())) {
          methodCallCode = "return " + methodCallCode + "\n"
          methodCallModel.setWithReturnValue(true)
        }
      }
      methodCallModel.setMethodCallCode(methodCallCode)
      methodCallModel.setResponseViewId(idStringMap.get(it))
      callModelList.add(methodCallModel)
    }

  }


}
