package com.skyinu.gradlebutterknife.plugin.model;

import com.skyinu.gradlebutterknife.plugin.ConstantList;
import com.skyinu.gradlebutterknife.plugin.bind.ViewBindClassBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * Created by chen on 2018/3/14.
 */

public enum MethodBindListenClass {
  OnClick("android.view.View", "setOnClickListener", "android.view.View$OnClickListener",
      new MethodBindFuncModel("void", "onClick", "",
          new Parameter("android.view.View", "view"))),

  OnLongClick("android.view.View", "setOnLongClickListener",
      "android.view.View$OnLongClickListener",
      new MethodBindFuncModel("boolean", "onLongClick", "false",
          new Parameter("android.view.View", "view")));

//  OnTextChanged("android.widget.EditText", "addTextChangedListener",
//      "android.text.TextWatcher",
//      new MethodBindFuncModel("void", "beforeTextChanged", "",
//      new Parameter("java.lang.CharSequence", "var1"), new Parameter("int", "var2"),
//          new Parameter("int", "var3"), new Parameter("int", "var4")),
//      new MethodBindFuncModel("void", "onTextChanged", "",
//          new Parameter("java.lang.CharSequence", "var1"), new Parameter("int", "var2"),
//          new Parameter("int", "var3"), new Parameter("int", "var4")),
//      new MethodBindFuncModel("void", "afterTextChanged", "",
//          new Parameter("android.text.Editable", "var1")));

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

  public void fillClass(ViewBindClassBuilder classBuilder) throws NotFoundException, CannotCompileException {
    //step 1: 插入接口
    classBuilder.addInterface(bindInterface);
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

  public void endInject(ViewBindClassBuilder classBuilder) throws CannotCompileException {
    for (MethodBindFuncModel funcModel:funcModelList) {
        funcModel.endBuildMethod(classBuilder);
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
