package com.skyinu.gradlebutterknife.plugin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chen on 2018/3/14.
 */

public enum MethodBindListenClass {
  OnClick("android.view.View", "setOnClickListener", "android.view.View$OnClickListener", false,
       new MethodBindFuncModel("void", "onClick", "",
          new Parameter("android.view.View", "view"))),

  OnLongClick("android.view.View", "setOnLongClickListener",
      "android.view.View$OnLongClickListener", false,
      new MethodBindFuncModel("boolean", "onLongClick", "false",
          new Parameter("android.view.View", "view"))),

  OnTextChanged("android.widget.TextView", "addTextChangedListener",
      "android.text.TextWatcher", true,
      new MethodBindFuncModel("void", "beforeTextChanged", "",
      new Parameter("java.lang.CharSequence", "var1"), new Parameter("int", "var2"),
          new Parameter("int", "var3"), new Parameter("int", "var4")),
      new MethodBindFuncModel("void", "onTextChanged", "",
          new Parameter("java.lang.CharSequence", "var1"), new Parameter("int", "var2"),
          new Parameter("int", "var3"), new Parameter("int", "var4")),
      new MethodBindFuncModel("void", "afterTextChanged", "",
          new Parameter("android.text.Editable", "var1")));

  private final String aimClassType;
  private final String setMethod;
  private final String bindInterface;
  private final boolean noId;
  private final List<MethodBindFuncModel> funcModelList;

  MethodBindListenClass(String aimClassType, String setMethod, String bindInterface,
                        boolean noId, MethodBindFuncModel... bindFuncModels) {
    this.aimClassType = aimClassType;
    this.setMethod = setMethod;
    this.bindInterface = bindInterface;
    this.noId = noId;
    this.funcModelList = new ArrayList<>(bindFuncModels.length);
    Collections.addAll(funcModelList, bindFuncModels);
  }

  public String getAimClassType() {
    return aimClassType;
  }

  public String getSetMethod() {
    return setMethod;
  }

  public String getBindInterface() {
    return bindInterface;
  }

  public List<MethodBindFuncModel> getFuncModelList() {
    return funcModelList;
  }

  public boolean isNoId(){
    return noId;
  }

  public MethodBindFuncModel findMethodBindFuncModelByMethod(String name){
    for (MethodBindFuncModel model : funcModelList) {
      if(model.getName().equals(name)){
        return model;
      }
    }
    return null;
  }

  public String convertToTargetMethod(String key){
    key = key.replace("_", "");
    for (MethodBindFuncModel model : funcModelList) {
      if(model.getName().equalsIgnoreCase(key)){
        return model.getName();
      }
    }
    return null;
  }
}
