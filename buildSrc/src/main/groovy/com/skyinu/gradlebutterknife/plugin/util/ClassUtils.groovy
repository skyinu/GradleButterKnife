package com.skyinu.gradlebutterknife.plugin.util

import javassist.CtClass;

/**
 * Created by chen on 2018/3/12.*/

public class ClassUtils {
  static SIGNATURE_PRIMITIVE = ["B", "C", "D", "F", "I", "S", "J", "Z", "V"]

  static boolean containSpecficInterface(CtClass sourceClass, CtClass desInterface) {
    def implInterfaces = sourceClass.getInterfaces()
    for (inter in implInterfaces) {
      if (inter == desInterface) {
        return true
      }
    }
    return false
  }

  static int getClassInheritanceDepth(CtClass ctClass) {
    int depth = 0
    while ((ctClass = ctClass.getSuperclass()) != null) [depth++]
    return depth
  }

  static boolean isPrimitive(String signature) {
    return SIGNATURE_PRIMITIVE.contains(signature)
  }
}
