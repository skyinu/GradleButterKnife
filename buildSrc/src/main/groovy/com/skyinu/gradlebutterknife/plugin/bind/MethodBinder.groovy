package com.skyinu.gradlebutterknife.plugin.bind

import com.skyinu.annotations.OnClick
import com.skyinu.annotations.OnLongClick
import com.skyinu.gradlebutterknife.plugin.ConstantList
import com.skyinu.gradlebutterknife.plugin.model.MethodBindListenClass
import com.skyinu.gradlebutterknife.plugin.model.MethodCallModel
import com.skyinu.gradlebutterknife.plugin.util.BindUtils
import com.skyinu.gradlebutterknife.plugin.util.ClassUtils
import javassist.CtClass
import javassist.CtMethod
import java.lang.annotation.Annotation

/**
 * Created by chen on 2018/3/13.*/

class MethodBinder {
  private Map<Integer, String> idStringMap

  MethodBinder(Map<Integer, String> idStringMap) {
    this.idStringMap = idStringMap
  }

  def processBindMethod(CtClass targetClass, String classPath, String buildMethodSrc,
                        Map<Integer, String> idFieldMap){
    def methodCallMap = new HashMap<String, List<MethodCallModel>>()
    targetClass.declaredMethods.each {
      CtMethod ctMethod = it
      ctMethod.annotations.each {
        Annotation annotation = it
        if (!BindUtils.isAnnotationSupport(annotation)) {
          return
        }
        buildMethodSrc += buildSetCodeBlock(targetClass, annotation, idFieldMap)
        def callModelList = methodCallMap.get(annotation.annotationType().name)
        if (!callModelList) {
          callModelList = new ArrayList<MethodCallModel>()
          methodCallMap.put(annotation.annotationType().name, callModelList)
        }
        buildMethodCallCodeBlock(callModelList, ctMethod, annotation)
      }
    }

    ViewBindClassBuilder bindClassBuilder = new ViewBindClassBuilder(classPath, targetClass)

    if (!methodCallMap.keySet().empty) {
      methodCallMap.keySet().each {
        processMethodCallModel(bindClassBuilder, it, methodCallMap.get(it))
      }
      bindClassBuilder.build()
    }
    return buildMethodSrc
  }

  def processMethodCallModel(ViewBindClassBuilder bindClassBuilder, String name, List<MethodCallModel> callModelList) {
    if (name == OnClick.name) {
      MethodBindListenClass.OnClick.fillClass(bindClassBuilder)
      callModelList.each {
        MethodBindListenClass.OnClick.fillMethod("onClick", it)
      }
      MethodBindListenClass.OnClick.endInject(bindClassBuilder)
    }
    else if(name == OnLongClick.name){
      MethodBindListenClass.OnLongClick.fillClass(bindClassBuilder)
      callModelList.each {
        MethodBindListenClass.OnLongClick.fillMethod("onLongClick", it)
      }
      MethodBindListenClass.OnLongClick.endInject(bindClassBuilder)
    }
  }

  String buildSetCodeBlock(CtClass injectClass, Annotation annotation,
                           Map<Integer, String> idFieldMap) {
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
