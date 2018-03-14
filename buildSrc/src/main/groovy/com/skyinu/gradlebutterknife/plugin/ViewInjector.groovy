package com.skyinu.gradlebutterknife.plugin

import com.skyinu.annotations.BindView
import com.skyinu.gradlebutterknife.plugin.util.BindUtils
import com.skyinu.gradlebutterknife.plugin.util.ClassUtils
import com.skyinu.gradlebutterknife.plugin.util.Log
import javassist.ClassPool
import javassist.CtClass
import javassist.CtField
import javassist.CtMethod
import javassist.CtNewMethod
import org.gradle.api.plugins.quality.CodeNarc

import java.lang.annotation.Annotation

/**
 * Created by chen on 2018/3/9.*/

public class ViewInjector {

  ClassPool classPool
  def injectMethod
  CtClass injectInterface
  CtClass onClickInterface
  Map<Integer, String> idStringMap
  Map<Integer, String> idFieldMap
  FieldBinder fieldBinder
  MethodBinder methodBinder

  ViewInjector(ClassPool classPool, Map<Integer, String> idStringMap) {
    this.classPool = classPool
    this.injectInterface = classPool.get(ConstantList.NAME_FLAG_INTERFACE)
    this.onClickInterface = classPool.get(ConstantList.NAME_ONCLICK_INTERFACE)
    this.idStringMap = idStringMap
    fieldBinder = new FieldBinder(idStringMap)
    idFieldMap = new HashMap<>()
  }

  def injectClass(CtClass injectClass, String classPath) {
    startInject(injectClass)
    injectClass.declaredFields.each {
      CtField ctField = it
      ctField.annotations.each {
        if (it instanceof BindView) {
          def value = it.value()
          def fieldName = ctField.name
          idFieldMap.put(value, fieldName)
        }
        injectMethod += fieldBinder.buildBindFieldStatement(ctField, it as Annotation)
      }
    }
    methodBinder = new MethodBinder(classPool, classPath, idStringMap, idFieldMap)
    def hasInjectListen = false
    injectClass.declaredMethods.each {
      CtMethod ctMethod = it
      ctMethod.annotations.each {
        if (!BindUtils.isA4nnotationSupport(it as Annotation)) {
          return
        }
        if(!hasInjectListen){
          injectEventClassInstance(injectClass)
        }
        injectMethod +=
            methodBinder.buildBindMethodStatement(injectClass, ctMethod, it as Annotation)
      }
    }
    endInject(injectClass, classPath)
  }

  def injectEventClassInstance(CtClass injectClass){
    def fullName = "${injectClass.name}\$${ConstantList.NAME_CLASS_EVENT_DISPATCHER}"
    def statement = "$fullName ${ConstantList.NAME_LISTENER_INSTANCE} " +
            "= new $fullName(${ConstantList.NAME_FIELD_OUTER_CLASS})"
    injectMethod += statement
  }

  def startInject(CtClass injectClass) {
    injectMethod =
        "public void ${ConstantList.NAME_INJECT_METHOD}" + "(android.view.View ${ConstantList.VIEW_SOURCE}){\n"
    if (ClassUtils.containSpecficInterface(injectClass.getSuperclass(), injectInterface)) {
      injectMethod += "super.${ConstantList.NAME_INJECT_METHOD}(${ConstantList.VIEW_SOURCE});\n"
    }
    injectMethod +=
        "${ConstantList.NAME_CLASS_CONTEXT} ${ConstantList.NAME_CONTEXT_FIELD}" + " = ${ConstantList.VIEW_SOURCE}.getContext();\n"
    injectMethod +=
        "${ConstantList.NAME_CLASS_RESOURCES} ${ConstantList.NAME_RESOURCE_FIELD}" + " = ${ConstantList.NAME_CONTEXT_FIELD}.getResources();\n"
    injectMethod += "android.view.View ${ConstantList.NAME_TEMP_VIEW};\n"
    idFieldMap.clear()
    injectClass.addInterface(injectInterface)
  }

  def endInject(CtClass injectClass, String classPath) {
    injectMethod += "}"
    if (injectClass.isFrozen()) {
      injectClass.defrost()
    }
    CtMethod ctMethod = CtNewMethod.make(injectMethod, injectClass)
    if (ClassUtils.containSpecficMethod(injectClass, ctMethod)) {
      injectClass.removeMethod(ctMethod)
    }
    injectClass.addMethod(ctMethod)
    injectClass.writeFile(classPath)
    injectClass.detach()
  }
}
