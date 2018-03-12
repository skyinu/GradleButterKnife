package com.skyinu.gradlebutterknife.plugin

import com.skyinu.annotations.BindAnim
import com.skyinu.annotations.BindBitmap
import com.skyinu.annotations.BindBool
import com.skyinu.annotations.BindColor
import com.skyinu.annotations.BindDimen
import com.skyinu.annotations.BindInt
import com.skyinu.annotations.BindString
import com.skyinu.annotations.BindView
import javassist.CtClass
import javassist.CtField
import javassist.CtMethod
import javassist.CtNewMethod
import org.gradle.api.Project

/**
 * Created by chen on 2018/3/9.
 */

public class ViewInjector {

    def injectMethod
    CtClass injectInterface
    Map<Integer, String> idStringMap
    static Project project

    ViewInjector(CtClass injectInterface, Map<Integer, String> idStringMap) {
        this.injectInterface = injectInterface
        this.idStringMap = idStringMap

    }

    def injectClass(CtClass injectClass, String classPath) {
        startInject()
        injectClass.declaredFields.each {
            CtField ctField = it
            ctField.annotations.each {
                selectProcessor(ctField, it)
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
        if (annotation instanceof BindView) {
            processInjectView(injectField, annotation as BindView)
            return
        }
        if (annotation instanceof BindString) {
            processInjectString(injectField, annotation as BindString)
            return
        }
        if (annotation instanceof BindInt) {
            processInjectInt(injectField, annotation as BindInt)
            return
        }
        if (annotation instanceof BindBool) {
            processInjectBool(injectField, annotation as BindBool)
            return
        }
        if (annotation instanceof BindColor) {
            processInjectColor(injectField, annotation as BindColor)
            return
        }
        if (annotation instanceof BindDimen) {
            processInjectDimen(injectField, annotation as BindDimen)
            return
        }
        if (annotation instanceof BindBitmap) {
            processInjectBitmap(injectField, annotation as BindBitmap)
            return
        }

        if (annotation instanceof BindAnim) {
            processInjectAnim(injectField, annotation as BindAnim)
            return
        }
    }

    def processInjectAnim(CtField injectField, BindAnim bindAnim) {
        def value = bindAnim.value()
        def idString = idStringMap.get(value)
        def viewName = injectField.name
        def injectType = injectField.signature
        injectType = injectType.substring(1, injectType.length() - 1).replace("/", ".")
        def statements = "$viewName = ($injectType)${ConstantList.NAME_CLASS_ANIMATIONUTILS}" +
                ".loadAnimation(${ConstantList.NAME_CONTEXT_FIELD},$idString);\n"
        injectMethod += statements
    }

    def processInjectBitmap(CtField injectField, BindBitmap bindBitmap) {
        def value = bindBitmap.value()
        def idString = idStringMap.get(value)
        def viewName = injectField.name
        def injectType = injectField.signature
        injectType = injectType.substring(1, injectType.length() - 1).replace("/", ".")
        def statements = "$viewName = ($injectType)${ConstantList.NAME_RESOURCE_FIELD}.getDrawable($idString);\n"
        injectMethod += statements
    }

    def processInjectView(CtField injectField, BindView bindView) {
        def value = bindView.value()
        def idString = idStringMap.get(value)
        def viewName = injectField.name
        def injectType = injectField.signature
        injectType = injectType.substring(1, injectType.length() - 1).replace("/", ".")
        def statements = "$viewName = ($injectType)${ConstantList.VIEW_SOURCE}.findViewById($idString);\n"
        injectMethod += statements
    }

    def processInjectString(CtField injectField, BindString bindString) {
        def value = bindString.value()
        def idString = idStringMap.get(value)
        def viewName = injectField.name
        def injectType = injectField.signature
        injectType = injectType.substring(1, injectType.length() - 1).replace("/", ".")
        def statements = "$viewName = ($injectType)${ConstantList.NAME_RESOURCE_FIELD}.getString($idString);\n"
        injectMethod += statements
    }

    def processInjectInt(CtField injectField, BindInt bindInt) {
        def value = bindInt.value()
        def idString = idStringMap.get(value)
        def viewName = injectField.name
        def statements = "$viewName = ${ConstantList.NAME_RESOURCE_FIELD}.getInteger($idString);\n"
        injectMethod += statements
    }

    def processInjectBool(CtField injectField, BindBool bindBool) {
        def value = bindBool.value()
        def idString = idStringMap.get(value)
        def viewName = injectField.name
        def statements = "$viewName = ${ConstantList.NAME_RESOURCE_FIELD}.getBoolean($idString);\n"
        injectMethod += statements
    }

    def processInjectColor(CtField injectField, BindColor bindColor) {
        def value = bindColor.value()
        def idString = idStringMap.get(value)
        def viewName = injectField.name
        def statements = "$viewName = ${ConstantList.NAME_RESOURCE_FIELD}.getColor($idString, null);\n"
        injectMethod += statements
    }

    def processInjectDimen(CtField injectField, BindDimen bindDimen) {
        def value = bindDimen.value()
        def idString = idStringMap.get(value)
        def viewName = injectField.name
        def statements = "$viewName = ${ConstantList.NAME_RESOURCE_FIELD}.getDimension($idString);\n"
        injectMethod += statements
    }

    def startInject() {
        injectMethod = "public void ${ConstantList.NAME_INJECT_METHOD}" +
                "(android.view.View ${ConstantList.VIEW_SOURCE}){\n"
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
        injectClass.addInterface(injectInterface)
        CtMethod injectMethod = CtNewMethod.make(injectMethod, injectClass)
        injectClass.addMethod(injectMethod)
        injectClass.writeFile(classPath)
        injectClass.detach()
    }
}
