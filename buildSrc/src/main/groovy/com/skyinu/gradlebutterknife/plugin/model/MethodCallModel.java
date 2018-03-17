package com.skyinu.gradlebutterknife.plugin.model;

import java.lang.annotation.Annotation;

/**
 * Created by chen on 2018/3/15.
 */

public class MethodCallModel {
  private String methodCallCode;
  private boolean withReturnValue;
  private Annotation belongAnnotation;
  private String responseViewId;

  public MethodCallModel(Annotation belongAnnotation) {
    this.belongAnnotation = belongAnnotation;
  }

  public void setMethodCallCode(String methodCallCode) {
    this.methodCallCode = methodCallCode;
  }

  public void setWithReturnValue(boolean withReturnValue) {
    this.withReturnValue = withReturnValue;
  }

  public void setResponseViewId(String responseViewId) {
    this.responseViewId = responseViewId;
  }

  public String buildMethodCallCode(String paramsId){
    StringBuilder builder = new StringBuilder("if (")
        .append(paramsId)
        .append("==")
        .append(responseViewId)
        .append("){\n")
        .append(methodCallCode);
    builder.append("\n}\n");
    return builder.toString();
  }
}
