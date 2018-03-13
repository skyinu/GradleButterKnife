package com.skyinu.gradlebutterknife.plugin

import com.skyinu.gradlebutterknife.plugin.util.BindUtils
import com.skyinu.gradlebutterknife.plugin.util.ClassUtils
import com.skyinu.gradlebutterknife.plugin.util.Log
import javassist.CtClass
import javassist.CtField
import javassist.CtMethod
import javassist.CtNewMethod

import java.lang.annotation.Annotation

/**
 * Created by chen on 2018/3/9.
 */

public class ViewInjector {

    def injectMethod
    CtClass injectInterface
    Map<Integer, String> idStringMap

    ViewInjector(CtClass injectInterface, Map<Integer, String> idStringMap) {
        this.injectInterface = injectInterface
        this.idStringMap = idStringMap

    }

    def injectClass(CtClass injectClass, String classPath) {
        startInject(injectClass)
        injectClass.declaredFields.each {
            CtField ctField = it
            ctField.annotations.each {
                def statement = BindUtils.buildBindFieldStatement(ctField, it as Annotation, idStringMap)
                Log.error(statement)
                injectMethod += statement
            }
        }

        injectClass.declaredMethods.each {
            CtMethod ctMethod = it
            ctMethod.annotations.each {
                selectProcessor(ctField, it)
            }
        }
        endInject(injectClass, classPath)
    }

    def selectProcessor(CtField injectField, Object annotation) {

    }


    def startInject(CtClass injectClass) {
        injectMethod = "public void ${ConstantList.NAME_INJECT_METHOD}" +
                "(android.view.View ${ConstantList.VIEW_SOURCE}){\n"
        if(ClassUtils.containSpecficInterface(injectClass.getSuperclass(), injectInterface)){
            injectMethod += "super.${ConstantList.NAME_INJECT_METHOD}(${ConstantList.VIEW_SOURCE});\n"
        }
        injectMethod += "${ConstantList.NAME_CLASS_CONTEXT} ${ConstantList.NAME_CONTEXT_FIELD}" +
                " = ${ConstantList.VIEW_SOURCE}.getContext();\n"
        injectMethod += "${ConstantList.NAME_CLASS_RESOURCES} ${ConstantList.NAME_RESOURCE_FIELD}" +
                " = ${ConstantList.NAME_CONTEXT_FIELD}.getResources();\n"
    }

    def endInject(CtClass injectClass, String classPath) {
        injectMethod += "}"
        if (injectClass.isFrozen()) {
            injectClass.defrost()
        }
        CtMethod injectMethod = CtNewMethod.make(injectMethod, injectClass)
        injectClass.addMethod(injectMethod)
        injectClass.writeFile(classPath)
        injectClass.detach()
    }
}
