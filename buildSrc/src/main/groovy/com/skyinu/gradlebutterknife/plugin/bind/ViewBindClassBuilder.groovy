package com.skyinu.gradlebutterknife.plugin.bind

import com.skyinu.gradlebutterknife.plugin.ConstantList
import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor
import javassist.CtField
import javassist.CtMethod
import javassist.CtNewConstructor
import javassist.CtNewMethod
import java.lang.reflect.Modifier

/**
 * Created by chen on 2018/3/17.
 * generate all view bind class
 */

class ViewBindClassBuilder {
  private ClassPool classPool
  private CtClass bindClass
  private String classPath
  private String fullName

  ViewBindClassBuilder(String classPath, CtClass targetClass, String bindClassName){
    this.classPath = classPath
    this.classPool = targetClass.classPool
    fullName = "${targetClass.name}_${bindClassName}"
    bindClass = classPool.getOrNull(fullName)
    if(bindClass != null && bindClass.frozen){
      bindClass.defrost()
    }
    bindClass = classPool.makeClass(fullName)
    //add target class instance
    CtField outClassField = new CtField(targetClass, ConstantList.NAME_FIELD_OUTER_CLASS, bindClass)
    outClassField.setModifiers(Modifier.PRIVATE)
    bindClass.addField(outClassField)
    String conSrc = buildConstructorCodeBlock(bindClassName, targetClass.getName())
    CtConstructor constructor = CtNewConstructor.make(conSrc, bindClass)
    bindClass.addConstructor(constructor)
  }

  private String buildConstructorCodeBlock(String name, String paramsType) {
    def builder = new StringBuilder(name)
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
        .append("}\n")
    return builder.toString()
  }

  def addInterface(String name){
    bindClass.addInterface(classPool.get(name))
  }

  String addMethod(String methodSrc){
    CtMethod ctMethod = CtNewMethod.make(methodSrc, bindClass)
    bindClass.addMethod(ctMethod)
  }

  def getFullName() {
    return fullName
  }

  def build(){
    bindClass.writeFile(classPath)
  }
}
