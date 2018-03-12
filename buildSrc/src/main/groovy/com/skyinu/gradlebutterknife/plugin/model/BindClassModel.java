package com.skyinu.gradlebutterknife.plugin.model;

import com.skyinu.gradlebutterknife.plugin.util.ClassUtils;
import java.util.Comparator;
import javassist.CtClass;

/**
 * Created by chen on 2018/3/12.
 */

public class BindClassModel {
  private CtClass injectClass;
  private String classFilePath;
  private int inheritanceDepth;

  public BindClassModel(CtClass injectClass, String classFilePath) {
    this.injectClass = injectClass;
    this.classFilePath = classFilePath;
    inheritanceDepth = ClassUtils.getClassInheritanceDepth(injectClass);
  }

  public CtClass getInjectClass() {
    return injectClass;
  }

  public String getClassFilePath() {
    return classFilePath;
  }

  public int getInheritanceDepth() {
    return inheritanceDepth;
  }

  @Override public String toString() {
    return "BindClassModel{" +
        "injectClass=" + injectClass +
        ", classFilePath='" + classFilePath + '\'' +
        ", inheritanceDepth=" + inheritanceDepth +
        '}';
  }

  public static class BindClassModelComparator implements Comparator<BindClassModel> {

    @Override public int compare(BindClassModel bindClassModel, BindClassModel t1) {
      return bindClassModel.getInheritanceDepth() - t1.getInheritanceDepth();
    }
  }
}
