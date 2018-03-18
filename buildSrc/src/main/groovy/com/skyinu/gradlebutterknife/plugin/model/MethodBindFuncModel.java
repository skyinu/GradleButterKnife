package com.skyinu.gradlebutterknife.plugin.model;

import com.skyinu.gradlebutterknife.plugin.bind.ViewBindClassBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javassist.CannotCompileException;

/**
 * Created by chen on 2018/3/14.
 */

public class MethodBindFuncModel {
  private StringBuilder methodBuildString;
  private String returnType;
  private String returnValue;
  private String modify = "public";
  private String name;
  private List<Parameter> parameterList;

  public MethodBindFuncModel(String returnType, String name, String returnValue,
                             Parameter... parameters) {
    this.returnType = returnType;
    this.name = name;
    this.returnValue = returnValue;
    this.parameterList = new ArrayList<>(parameters.length);
    Collections.addAll(parameterList, parameters);
  }

  public void buildCtMethod(boolean noId) {
    methodBuildString = new StringBuilder(modify);
    methodBuildString.append(" ")
        .append(returnType)
        .append(" ")
        .append(name)
        .append("(");
    if (!parameterList.isEmpty()) {
      for (Parameter parameter : parameterList) {
        methodBuildString.append(parameter.buildCodeBlock())
            .append(",");
      }
      methodBuildString.deleteCharAt(methodBuildString.length() - 1);
    }
    methodBuildString.append(")\n")
        .append("{\n");
    if(!noId) {
      methodBuildString.append("int id = view.getId();\n");
    }

  }

  public void endBuildMethod(ViewBindClassBuilder classBuilder) throws CannotCompileException {
    if(methodBuildString == null){
      return;
    }
    methodBuildString.append("return ")
        .append(returnValue)
        .append(";\n")
        .append("}\n");
    classBuilder.addMethod(methodBuildString.toString());
  }

  public String getName() {
    return name;
  }

  public List<Parameter> getParameterList() {
    return parameterList;
  }

  public void fillCtMethod(String methodCallCode) {
    methodBuildString.append(methodCallCode);
  }
}
