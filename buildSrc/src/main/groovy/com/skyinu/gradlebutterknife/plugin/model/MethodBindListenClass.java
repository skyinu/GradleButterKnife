package com.skyinu.gradlebutterknife.plugin.model;

import com.skyinu.gradlebutterknife.plugin.ConstantList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

/**
 * Created by chen on 2018/3/14.
 */

public enum MethodBindListenClass {
  OnClick("android.view.View", "setOnClickListener", "android.view.View$OnClickListener",
      new MethodBindFuncModel("void", "public", "onClick", "",
          new Parameter("android.view.View", "view"))),

  OnLongClick("android.view.View", "setOnLongClickListener",
      "android.view.View$OnLongClickListener",
      new MethodBindFuncModel("boolean", "public", "onLongClick", "false",
          new Parameter("android.view.View", "view")));

  private final String aimClassType;
  private final String setMethod;
  private final String bindInterface;
  private final List<MethodBindFuncModel> funcModelList;

  MethodBindListenClass(String aimClassType, String setMethod, String bindInterface,
      MethodBindFuncModel... bindFuncModels) {
    this.aimClassType = aimClassType;
    this.setMethod = setMethod;
    this.bindInterface = bindInterface;
    this.funcModelList = new ArrayList<>(bindFuncModels.length);
    Collections.addAll(funcModelList, bindFuncModels);
  }

  public String buildCodeBlock(CtClass ctClass, String classPath, CtMethod annoMethod,
      String newClassName, String... methodCallList)
      throws CannotCompileException, IOException, NotFoundException {
    ClassPool classPool = ctClass.getClassPool();
    //step 1:构造一个新类
    String fullName = ctClass.getName() + "$" + newClassName;
    CtClass injectClass = classPool.makeClass(fullName);
    //step 2:添加调用方法需要的字段
    CtField outClassField = new CtField(ctClass, ConstantList.NAME_FIELD_OUTER_CLASS, injectClass);
    outClassField.setModifiers(Modifier.PRIVATE);
    injectClass.addField(outClassField);
    //step 3:注入构造方法
    CtConstructor constructor =
        CtNewConstructor.make(buildConstructorCodeBlock(newClassName, ctClass.getName()),
            injectClass);
    injectClass.addConstructor(constructor);
    //step 4:注入接口
    injectClass.addInterface(classPool.get(bindInterface));
    //step 5:注入相应方法
    for (int i = 0; i < methodCallList.length; i++) {
      MethodBindFuncModel funcModel = funcModelList.get(i);
      String methodCall = methodCallList[i];
      CtMethod ctMethod = CtNewMethod.make(funcModel.buildMethodCodeBlock(annoMethod, methodCall),
          injectClass);
      injectClass.addMethod(ctMethod);
    }
    injectClass.writeFile(classPath);
    //step 6:set listen class
    StringBuilder setCodeBlock = new StringBuilder("((");
    setCodeBlock.append(aimClassType)
        .append(")")
        .append(ConstantList.NAME_TEMP_VIEW)
        .append(")")
        .append(".")
        .append(setMethod)
        .append("(new")
        .append(" ")
        .append(fullName)
        .append("(this));\n");
    return setCodeBlock.toString();
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
}
