package com.skyinu.gradlebutterknife.plugin.model;

import java.util.ArrayList;
import java.util.List;

import javassist.CtClass;

/**
 * Created by chen on 2018/3/17.
 */

public class MethodViewBind {
  private String targetMethodName;
  private String bindMethodName;
  private String viewFieldName;
  private String viewId;
  private String viewClassType;
  private boolean withReturnValue;
  private List<String> paramsTypes;
  public MethodViewBind(String bindMethodName, String targetMethodName, String viewFieldName,
                        String viewId, String viewClassType, CtClass[] methodParams, boolean withReturnValue) {
    this.targetMethodName = targetMethodName;
    this.bindMethodName = bindMethodName;
    this.viewFieldName = viewFieldName;
    this.viewId = viewId;
    this.viewClassType = viewClassType;
    this.withReturnValue = withReturnValue;
    paramsTypes = new ArrayList<>(methodParams.length);
    for(CtClass param : methodParams){
      paramsTypes.add(param.getName());
    }
  }



  public String getBindMethodName() {
    return bindMethodName;
  }

  public String getViewFieldName() {
    return viewFieldName;
  }

  public String getViewId() {
    return viewId;
  }

  public String getViewClassType() {
    return viewClassType;
  }

  public List<String> getParamsTypes() {
    return paramsTypes;
  }

  public boolean withReturnValue() {
    return withReturnValue;
  }

  public String getTargetMethodName(){
    return targetMethodName;
  }

  @Override
  public String toString() {
    return "MethodViewBind{" +
        "targetMethodName='" + targetMethodName + '\'' +
        ", bindMethodName='" + bindMethodName + '\'' +
        ", viewFieldName='" + viewFieldName + '\'' +
        ", viewId='" + viewId + '\'' +
        ", viewClassType='" + viewClassType + '\'' +
        ", withReturnValue=" + withReturnValue +
        ", paramsTypes=" + paramsTypes +
        '}';
  }
}
