package com.skyinu.gradlebutterknife.plugin

import com.skyinu.annotations.BindView
import com.skyinu.annotations.OnClick
import com.skyinu.annotations.OnLongClick
import com.skyinu.gradlebutterknife.plugin.model.MethodBindListenClass
import com.skyinu.gradlebutterknife.plugin.model.MethodCallModel
import com.skyinu.gradlebutterknife.plugin.util.BindUtils
import com.skyinu.gradlebutterknife.plugin.util.ClassUtils
import com.skyinu.gradlebutterknife.plugin.util.Log
import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor
import javassist.CtField
import javassist.CtMethod
import javassist.CtNewConstructor
import javassist.CtNewMethod

import java.lang.annotation.Annotation
import java.lang.reflect.Modifier

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
    def methodCallMap = new HashMap<String, List<MethodCallModel>>()

    injectClass.declaredMethods.each {
      CtMethod ctMethod = it
      ctMethod.annotations.each {
        Annotation annotation = it
        if (!BindUtils.isAnnotationSupport(annotation)) {
          return
        }
        injectMethod += methodBinder.buildSetCodeBlock(injectClass, annotation)
        def callModelList = methodCallMap.get(annotation.annotationType().name)
        if (!callModelList) {
          callModelList = new ArrayList<MethodCallModel>()
          methodCallMap.put(annotation.annotationType().name, callModelList)
        }
        methodBinder.buildMethodCallCodeBlock(callModelList, ctMethod, annotation)
      }
    }

    if (!methodCallMap.keySet().empty) {
      String fullName = "${injectClass.name}_${ConstantList.NAME_CLASS_EVENT_DISPATCHER}"
      def dispatcherClass = geneate(injectClass, fullName)
      methodCallMap.keySet().each {
        processMethodCallModel(dispatcherClass, it, methodCallMap.get(it))
      }
      dispatcherClass.writeFile(classPath)
    }
    endInject(injectClass, classPath)
  }

  CtClass geneate(CtClass injectClass, String fullName){
    CtClass tmpClass = classPool.makeClass(fullName);
    CtField outClassField = new CtField(injectClass, ConstantList.NAME_FIELD_OUTER_CLASS, tmpClass);
    outClassField.setModifiers(Modifier.PRIVATE);
    tmpClass.addField(outClassField);
    CtConstructor constructor =
        CtNewConstructor.make(buildConstructorCodeBlock(ConstantList.NAME_CLASS_EVENT_DISPATCHER, injectClass.getName()),
            tmpClass);
    tmpClass.addConstructor(constructor)
    return tmpClass
  }

  private String buildConstructorCodeBlock(String name, String paramsType) {
    StringBuilder builder = new StringBuilder(name);
    builder.append("(")
        .append(paramsType)
        .append(" ")
        .append(ConstantList.NAME_FIELD_OUTER_CLASS)
        .append(")")
        .append("{\n")
        .append("this.")
        .append(ConstantList.NAME_FIELD_OUTER_CLASS)
        .append(" = ")
        .append(ConstantList.NAME_FIELD_OUTER_CLASS)
        .append(";\n")
        .append("}\n");
    return builder.toString();
  }

  def processMethodCallModel(CtClass ctClass, String name, List<MethodCallModel> callModelList) {
    if (name == OnClick.name) {
      MethodBindListenClass.OnClick.fillClass(ctClass)
      callModelList.each {
        MethodBindListenClass.OnClick.fillMethod("onClick", it)
      }
      MethodBindListenClass.OnClick.endInject(ctClass)
    }
    else if(name == OnLongClick.name){
      MethodBindListenClass.OnLongClick.fillClass(ctClass)
      callModelList.each {
        MethodBindListenClass.OnLongClick.fillMethod("onLongClick", it)
      }
      MethodBindListenClass.OnLongClick.endInject(ctClass)
    }
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
