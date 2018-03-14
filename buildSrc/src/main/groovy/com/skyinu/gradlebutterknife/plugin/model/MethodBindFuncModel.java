package com.skyinu.gradlebutterknife.plugin.model;

import com.skyinu.gradlebutterknife.plugin.util.ClassUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * Created by chen on 2018/3/14.
 */

public class MethodBindFuncModel {
  private String returnType;
  private String returnValue;
  private String modify;
  private String name;
  private List<Parameter> parameterList;

  public MethodBindFuncModel(String returnType, String modify, String name, String returnValue,
      Parameter... parameters) {
    this.returnType = returnType;
    this.modify = modify;
    this.name = name;
    this.returnValue = returnValue;
    this.parameterList = new ArrayList<>(parameters.length);
    Collections.addAll(parameterList, parameters);
  }

  public MethodBindFuncModel(String returnType, String modify, String name, String returnValue) {
    this.returnType = returnType;
    this.modify = modify;
    this.name = name;
    this.returnValue = returnValue;
    this.parameterList = new ArrayList<>();
  }

  public void addParameter(Parameter parameter) {
    parameterList.add(parameter);
  }

  public String buildMethodCodeBlock(CtMethod ctMethod, String methodBody)
      throws NotFoundException {
    StringBuilder builder = new StringBuilder(modify);
    builder.append(" ")
        .append(returnType)
        .append(" ")
        .append(name)
        .append("(");
    if (!parameterList.isEmpty()) {
      for (Parameter parameter : parameterList) {
        builder.append(parameter.buildCodeBlock())
            .append(",");
      }
      builder.deleteCharAt(builder.length() - 1);
    }
    if (ClassUtils.isVoidType(ctMethod.getReturnType())) {
      methodBody += "return " + returnValue + ";\n";
    } else {
      methodBody = "return " + methodBody + "\n";
    }
    builder.append(")\n")
        .append("{\n")
        .append(methodBody)
        .append("}\n");
    return builder.toString();
  }
}
