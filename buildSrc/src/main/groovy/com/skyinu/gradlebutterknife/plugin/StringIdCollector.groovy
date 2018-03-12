package com.skyinu.gradlebutterknife.plugin

import javassist.CtClass
import org.gradle.api.Project

/**
 * Created by chen on 2018/3/9.
 */

public class StringIdCollector {
    Project project
    Map<Integer, String> idStringMap

    StringIdCollector(Project project) {
        this.project = project
        idStringMap = new HashMap<>()
    }

    def collectIdStringMapInR(CtClass rClass) {
        def idFields = rClass.declaredFields
        if (idFields.size() < 1) {
            return
        }
        def prefix = rClass.name.replace("\$", ".").replace(".R2", ".R")
        idFields.each {
            def fieldName = it.name
            def value = it.constantValue
            idStringMap.put(value, prefix + "." + fieldName)
        }
    }
}
