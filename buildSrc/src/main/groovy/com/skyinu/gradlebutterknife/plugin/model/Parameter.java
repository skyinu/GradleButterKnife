package com.skyinu.gradlebutterknife.plugin.model;

/**
 * Created by chen on 2018/3/14.
 */

public class Parameter {
  private String type;
  private String name;

  public Parameter(String type, String name) {
    this.type = type;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String buildCodeBlock() {
    return type + " " + name;
  }

  @Override public String toString() {
    return buildCodeBlock();
  }
}
