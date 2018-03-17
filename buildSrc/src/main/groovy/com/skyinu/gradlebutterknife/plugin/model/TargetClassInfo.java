package com.skyinu.gradlebutterknife.plugin.model;

import com.skyinu.gradlebutterknife.plugin.util.ClassUtils;
import java.util.Comparator;
import javassist.CtClass;

/**
 * Created by chen on 2018/3/12.
 */

public class TargetClassInfo {
  private CtClass injectClass;
  private String classFilePath;
  private int inheritanceDepth;

  public TargetClassInfo(CtClass injectClass, String classFilePath) {
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
    return "TargetClassInfo{" +
        "injectClass=" + injectClass +
        ", classFilePath='" + classFilePath + '\'' +
        ", inheritanceDepth=" + inheritanceDepth +
        '}';
  }

  public static class BindClassModelComparator implements Comparator<TargetClassInfo> {

    @Override public int compare(TargetClassInfo targetClassInfo, TargetClassInfo t1) {
      return targetClassInfo.getInheritanceDepth() - t1.getInheritanceDepth();
    }
  }
}
