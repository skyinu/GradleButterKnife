package com.skyinu.gradlebutterknife.plugin

import com.skyinu.annotations.OnClick
import com.skyinu.annotations.OnLongClick
import com.skyinu.gradlebutterknife.plugin.util.BindUtils
import com.skyinu.gradlebutterknife.plugin.util.ClassUtils
import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor
import javassist.CtField
import javassist.CtMethod
import javassist.CtNewConstructor
import javassist.CtNewMethod
import javassist.Modifier

import java.lang.annotation.Annotation
import com.skyinu.gradlebutterknife.plugin.util.Log

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
        methodCall += "((${injectMethod.parameterTypes[0].name})view);\n"
      } else {
        methodCall += "();\n"
      }
      if (annotation instanceof OnClick) {
        def listenStatement = injectIntoInnerClickClass(injectClass, methodCall)
        statement +=
            "${ConstantList.NAME_TEMP_VIEW}.setOnClickListener(new ${listenStatement}(this));\n"
      } else if (annotation instanceof OnLongClick) {
        if(ClassUtils.isVoidType(injectMethod.returnType)){
          methodCall += "return false;\n"
        }
        else{
          methodCall = "return $methodCall"
        }
        def listenStatement = injectIntoLongClickClass(injectClass,  methodCall)
        statement +=
            "${ConstantList.NAME_TEMP_VIEW}.setOnLongClickListener(new ${listenStatement}(this));\n"
      }
    }
    return statement
  }

  String buildConstructureBody(String methodName, String outType) {
    return ("$methodName($outType ${ConstantList.NAME_FIELD_OUTER_CLASS}){\n" + "this.${ConstantList.NAME_FIELD_OUTER_CLASS} = ${ConstantList.NAME_FIELD_OUTER_CLASS};\n" +
        "}")
  }


  String injectIntoLongClickClass(CtClass ctClass, String methodCall){
    def innerClickClassName = "${ctClass.name}\$${ConstantList.NAME_INJECT_LONG_CLICK_CLASS}${innerClassLongClickCount}"
    CtClass innerClass = classPool.makeClass(innerClickClassName)
    CtField outClassField = new CtField(ctClass, ConstantList.NAME_FIELD_OUTER_CLASS, innerClass)
    outClassField.setModifiers(Modifier.PRIVATE)
    innerClass.addField(outClassField)
    def constructureBody = buildConstructureBody(
        "${ConstantList.NAME_INJECT_LONG_CLICK_CLASS}${innerClassClickCount}", ctClass.name)
    CtConstructor constructure = CtNewConstructor.make(constructureBody, innerClass)
    innerClass.addConstructor(constructure)
    innerClass.addInterface(classPool.get(ConstantList.NAME_ONLONGCLICK_INTERFACE))
    def onClickBody = " public boolean onLongClick(android.view.View view){\n$methodCall\n}"
    CtMethod ctMethod = CtNewMethod.make(onClickBody, innerClass)
    innerClass.addMethod(ctMethod)
    innerClass.writeFile(classPath)
    innerClassLongClickCount++
    return innerClickClassName
  }

  String injectIntoInnerClickClass(CtClass ctClass, String methodCall) {
    def innerClickClassName = "${ctClass.name}\$${ConstantList.NAME_INJECT_INNER_CLASS}${innerClassClickCount}"
    CtClass innerClass = classPool.makeClass(innerClickClassName)
    CtField outClassField = new CtField(ctClass, ConstantList.NAME_FIELD_OUTER_CLASS, innerClass)
    outClassField.setModifiers(Modifier.PRIVATE)
    innerClass.addField(outClassField)
    def constructureBody = buildConstructureBody(
        "${ConstantList.NAME_INJECT_INNER_CLASS}${innerClassClickCount}", ctClass.name)
    CtConstructor constructure = CtNewConstructor.make(constructureBody, innerClass)
    innerClass.addConstructor(constructure)
    innerClass.addInterface(classPool.get(ConstantList.NAME_ONCLICK_INTERFACE))
    def onClickBody = " public void onClick(android.view.View view){\n$methodCall\n}"
    CtMethod injectMethod = CtNewMethod.make(onClickBody, innerClass)
    innerClass.addMethod(injectMethod)
    innerClass.writeFile(classPath)
    innerClassClickCount++
    return innerClickClassName
  }
}
