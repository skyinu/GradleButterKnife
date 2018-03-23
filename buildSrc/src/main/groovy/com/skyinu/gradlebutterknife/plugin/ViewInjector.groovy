package com.skyinu.gradlebutterknife.plugin


import com.skyinu.gradlebutterknife.plugin.bind.FieldBinder
import com.skyinu.gradlebutterknife.plugin.bind.MethodBinder
import com.skyinu.gradlebutterknife.plugin.util.ClassUtils
import com.skyinu.gradlebutterknife.plugin.util.Log
import javassist.CtClass
import javassist.CtMethod
import javassist.CtNewMethod

/**
 * Created by chen on 2018/3/9.*/

public class ViewInjector {

  def injectMethod
  Map<Integer, String> idStringMap
  Map<Integer, String> idFieldMap
  FieldBinder fieldBinder
  MethodBinder methodBinder
  int generateClassCount

  ViewInjector(Map<Integer, String> idStringMap) {
    this.idStringMap = idStringMap
    fieldBinder = new FieldBinder(idStringMap)
    methodBinder = new MethodBinder(idStringMap, generateClassCount)
    idFieldMap = new HashMap<>()
  }

  def injectClass(CtClass injectClass, String classPath) {
    startInject(injectClass)
    injectMethod = fieldBinder.processBindField(injectClass, injectMethod, idFieldMap)
    injectMethod = methodBinder.processBindMethod(injectClass, classPath, injectMethod, idFieldMap)
    endInject(injectClass, classPath)
  }

  def startInject(CtClass injectClass) {
    Log.info("start handle class ${injectClass.name}" )
    injectMethod =
        "public void ${ConstantList.NAME_INJECT_METHOD}" + "(android.view.View ${ConstantList.VIEW_SOURCE}){\n"
    def injectInterface = injectClass.classPool.get(ConstantList.NAME_FLAG_INTERFACE)
    if (ClassUtils.containSpecficInterface(injectClass.getSuperclass(), injectInterface)) {
      injectMethod += "super.${ConstantList.NAME_INJECT_METHOD}(${ConstantList.VIEW_SOURCE});\n"
    }
    injectMethod +=
        "${ConstantList.NAME_CLASS_CONTEXT} ${ConstantList.NAME_CONTEXT_FIELD}" + " = ${ConstantList.VIEW_SOURCE}.getContext();\n"
    injectMethod +=
        "${ConstantList.NAME_CLASS_RESOURCES} ${ConstantList.NAME_RESOURCE_FIELD}" + " = ${ConstantList.NAME_CONTEXT_FIELD}.getResources();\n"
    injectMethod += "android.view.View ${ConstantList.NAME_TEMP_VIEW};\n"
    idFieldMap.clear()
    if (injectClass.isFrozen()) {
      injectClass.defrost()
    }
    if(!ClassUtils.containSpecficInterface(injectClass, injectInterface)) {
      injectClass.addInterface(injectInterface)
    }
  }

  def endInject(CtClass injectClass, String classPath) {
    injectMethod += "}"
    Log.info("--------------------inject method src to target class ${injectClass.name}-----------------------")
    Log.info(injectMethod)
    Log.info("--------------------inject method src to target class ${injectClass.name}-----------------------")
    if (injectClass.isFrozen()) {
      injectClass.defrost()
    }
    CtMethod ctMethod = CtNewMethod.make(injectMethod, injectClass)
    if (ClassUtils.containSpecficMethod(injectClass, ctMethod)) {
      CtMethod existMethod = injectClass.getDeclaredMethod(ctMethod.name, ctMethod.parameterTypes)
      existMethod.setBody(ctMethod, null)
    } else {
      injectClass.addMethod(ctMethod)
    }
    injectClass.writeFile(classPath)
    injectClass.detach()
  }
}
