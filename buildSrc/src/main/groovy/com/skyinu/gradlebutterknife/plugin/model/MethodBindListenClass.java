package com.skyinu.gradlebutterknife.plugin.model;

import com.skyinu.gradlebutterknife.plugin.ConstantList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
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

  public void fillClass(CtClass ctClass) throws NotFoundException, CannotCompileException {
    //step 1: 插入接口
    ClassPool classPool = ctClass.getClassPool();
    ctClass.addInterface(classPool.get(bindInterface));
    //step 2:插入方法
    for (MethodBindFuncModel funcModel : funcModelList) {
      funcModel.buildCtMethod();
    }
  }

  public void fillMethod(String methodName, MethodCallModel methodCallModel){
    for (MethodBindFuncModel funcModel:funcModelList) {
      if(methodName.equals(funcModel.getName())){
        funcModel.fillCtMethod(methodCallModel);
        return;
      }
    }
  }

  public void endInject(CtClass ctClass) throws CannotCompileException {
    for (MethodBindFuncModel funcModel:funcModelList) {
        funcModel.endBuildMethod(ctClass);
    }
  }
  /**
   * build code block like ((android.view.View)view).setOnClickLister(new Dispatcher(this))
   *
   * @param ctClass
   * @return
   */
  public String buildListenerSetBlock(CtClass ctClass) {
    StringBuilder setCodeBlock = new StringBuilder("((");
    setCodeBlock.append(aimClassType)
        .append(")")
        .append(ConstantList.NAME_TEMP_VIEW)
        .append(")")
        .append(".")
        .append(setMethod)
        .append("(new")
        .append(" ")
        .append(ctClass.getName())
        .append("_")
        .append(ConstantList.NAME_CLASS_EVENT_DISPATCHER)
        .append("(this));\n");
    return setCodeBlock.toString();
  }
}
