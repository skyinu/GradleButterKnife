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
          new Parameter("android.text.Editable", "var1"))),

  OnCheckedChanged("android.widget.CompoundButton", "setOnCheckedChangeListener",
      "android.widget.CompoundButton$OnCheckedChangeListener", false,
      new MethodBindFuncModel("void", "onCheckedChanged", "",
          new Parameter("android.widget.CompoundButton", "view"),
          new Parameter("boolean", "var1"))),

  OnEditorAction("android.widget.TextView", "setOnEditorActionListener",
      "android.widget.TextView$OnEditorActionListener", false,
      new MethodBindFuncModel("boolean", "onEditorAction", "false",
          new Parameter("android.widget.TextView", "view"),
          new Parameter("int", "var1"), new Parameter("android.view.KeyEvent", "var2"))),

  OnFocusChange("android.view.View", "setOnFocusChangeListener",
      "android.view.View$OnFocusChangeListener", false,
      new MethodBindFuncModel("void", "onFocusChange", "",
          new Parameter("android.view.View", "view"), new Parameter("boolean", "var1"))),

  OnItemClick("android.widget.AdapterView", "setOnItemClickListener",
      "android.widget.AdapterView$OnItemClickListener", false,
      new MethodBindFuncModel("void", "onItemClick", "",
          new Parameter("android.widget.AdapterView", "view"),
          new Parameter("android.view.View", "var1"),
          new Parameter("int", "var2"), new Parameter("long", "var3"))),

  OnItemLongClick("android.widget.AdapterView", "setOnItemLongClickListener",
                  "android.widget.AdapterView$OnItemLongClickListener", false,
                  new MethodBindFuncModel("boolean", "onItemLongClick", "false",
                  new Parameter("android.widget.AdapterView", "view"),
          new Parameter("android.view.View", "var1"),
          new Parameter("int", "var2"), new Parameter("long", "var3"))),

  OnItemSelected("android.widget.AdapterView", "setOnItemSelectedListener",
      "android.widget.AdapterView$OnItemSelectedListener", false,
      new MethodBindFuncModel("void", "onItemSelected", "",
          new Parameter("android.widget.AdapterView", "view"),
          new Parameter("android.view.View", "var1"),
          new Parameter("int", "var2"), new Parameter("long", "var3")),
      new MethodBindFuncModel("void", "onNothingSelected", "",
          new Parameter("android.widget.AdapterView", "view"))),

  OnTouch("android.view.View", "setOnTouchListener","android.view.View$OnTouchListener", false,
      new MethodBindFuncModel("boolean", "onTouch", "false",
          new Parameter("android.view.View", "view"),
          new Parameter("android.view.MotionEvent", "var1")));

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
    if(funcModelList.size() == 1){
      return funcModelList.get(0).getName();
    }
    key = key.replace("_", "");
    for (MethodBindFuncModel model : funcModelList) {
      if(model.getName().equalsIgnoreCase(key)){
        return model.getName();
      }
    }
    return null;
  }
}
