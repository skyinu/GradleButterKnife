package com.skyinu.gradlebutterknife.plugin.bind

import com.skyinu.annotations.OnCheckedChanged
import com.skyinu.annotations.OnClick
import com.skyinu.annotations.OnEditorAction
import com.skyinu.annotations.OnFocusChange
import com.skyinu.annotations.OnItemClick
import com.skyinu.annotations.OnItemLongClick
import com.skyinu.annotations.OnItemSelected
import com.skyinu.annotations.OnLongClick
import com.skyinu.annotations.OnTextChanged
import com.skyinu.annotations.OnTouch
import com.skyinu.gradlebutterknife.plugin.ConstantList
import com.skyinu.gradlebutterknife.plugin.model.MethodBindFuncModel
import com.skyinu.gradlebutterknife.plugin.model.MethodBindListenClass
import com.skyinu.gradlebutterknife.plugin.model.MethodViewBind
import com.skyinu.gradlebutterknife.plugin.model.Parameter
import com.skyinu.gradlebutterknife.plugin.util.BindUtils
import com.skyinu.gradlebutterknife.plugin.util.ClassUtils
import com.skyinu.gradlebutterknife.plugin.util.Log
import javassist.CtClass
import javassist.CtMethod

import java.lang.annotation.Annotation

/**
 * Created by chen on 2018/3/13.*/

class MethodBinder {
  private Map<Integer, String> idStringMap
  private ViewBindClassBuilder commonBindClassBuilder
  private CtClass targetClass
  private String classPath
  private String buildMethodSrc
  private Map<Integer, String> idFieldMap
  private Map<String, List<MethodViewBind>> methodBindInfoList
  private int generateClassCount

  MethodBinder(Map<Integer, String> idStringMap, int generateClassCount) {
    this.idStringMap = idStringMap
    this.generateClassCount = generateClassCount
  }

  def processBindMethod(CtClass targetClass, String classPath, String buildMethodSrc,
                        Map<Integer, String> idFieldMap) {
    this.targetClass = targetClass
    this.classPath = classPath
    this.buildMethodSrc = buildMethodSrc
    this.idFieldMap = idFieldMap
    this.methodBindInfoList = new HashMap<String, List<MethodViewBind>>()
    commonBindClassBuilder = null
    generateClassCount = 0
    targetClass.declaredMethods.each {
      CtMethod targetMethod = it
      targetMethod.annotations.each {
        Annotation annotation = it
        if (!BindUtils.isAnnotationSupport(annotation)) {
          return
        }
        collectMethodBindInfo(targetMethod, annotation)
      }
    }
    if(methodBindInfoList.keySet().empty){
      return this.buildMethodSrc
    }
    processMethodBindInfo()
    return this.buildMethodSrc
  }

  def collectMethodBindInfo(CtMethod targetMethod, Annotation annotation){
    String annoName = annotation.annotationType().name
    Log.info("start collect info about method ${targetMethod.name} annotation ${annoName}")
    List<MethodViewBind> viewBindList = methodBindInfoList.get(annoName)
    if(!viewBindList){
      viewBindList = new ArrayList<>()
      methodBindInfoList.put(annoName, viewBindList)
    }
    annotation.value().each{
      def value = it
      def viewId = idStringMap.get(value)
      def viewFieldName = idFieldMap.get(value)
      def methodKey = null
      MethodBindListenClass listenClass = null
      if(annoName == OnClick.name){
        listenClass = MethodBindListenClass.OnClick
      }
      else if(annoName == OnLongClick.name){
        listenClass = MethodBindListenClass.OnLongClick
      }
      else if(annoName == OnTextChanged.name){
        listenClass = MethodBindListenClass.OnTextChanged
        methodKey = annotation.callback()
      }
      else if(annoName == OnCheckedChanged.name){
        listenClass = MethodBindListenClass.OnCheckedChanged
      }
      else if(annoName == OnEditorAction.name){
        listenClass = MethodBindListenClass.OnEditorAction
      }
      else if(annoName == OnFocusChange.name){
        listenClass = MethodBindListenClass.OnFocusChange
      }
      else if(annoName == OnItemClick.name){
        listenClass = MethodBindListenClass.OnItemClick
      }
      else if(annoName == OnItemLongClick.name){
        listenClass = MethodBindListenClass.OnItemLongClick
      }
      else if(annoName == OnItemSelected.name){
        listenClass = MethodBindListenClass.OnItemSelected
        methodKey = annotation.callback()
      }
      else if(annoName == OnTouch.name){
        listenClass = MethodBindListenClass.OnTouch
      }

      def bindMethodName = listenClass.convertToTargetMethod(methodKey)
      def viewClassType = listenClass.aimClassType
      def targetMethodName = targetMethod.name
      def methodParams  = targetMethod.parameterTypes
      def withReturnValue = !ClassUtils.isVoidType(targetMethod.returnType)
      MethodViewBind viewBind = new MethodViewBind(bindMethodName, targetMethodName, viewFieldName,
          viewId, viewClassType, methodParams, withReturnValue)
      viewBindList.add(viewBind)
    }
  }

  def processMethodBindInfo(){
    methodBindInfoList.keySet().each {
      String key = it
      List<MethodViewBind> viewBindList = methodBindInfoList.get(key)
      //TODO convert data
      MethodBindListenClass listenClass = null
      ViewBindClassBuilder classBuilder = null
      if(key == OnClick.name){
        listenClass = MethodBindListenClass.OnClick
        classBuilder = ensureBindClassExist()
        realProcessMethodBindInfo(listenClass, classBuilder, viewBindList)

      }
      else if(key == OnLongClick.name){
        listenClass = MethodBindListenClass.OnLongClick
        classBuilder = ensureBindClassExist()
        realProcessMethodBindInfo(listenClass, classBuilder, viewBindList)
      }
      else if(key == OnTextChanged.name){
        listenClass = MethodBindListenClass.OnTextChanged
        Map<String, List<MethodViewBind>> groupBindMap = viewBindList.groupBy {
          it.viewId
        }
        groupBindMap.keySet().each {
          def bindList = groupBindMap.get(it)
          ViewBindClassBuilder builder = new ViewBindClassBuilder(classPath,
              targetClass, ConstantList.NAME_CLASS_EVENT_DISPATCHER + generateClassCount)
          generateClassCount++
          realProcessMethodBindInfo(listenClass, builder, bindList)
          builder.build()
        }
      }
      else if(key == OnCheckedChanged.name){
        listenClass = MethodBindListenClass.OnCheckedChanged
        classBuilder = ensureBindClassExist()
        realProcessMethodBindInfo(listenClass, classBuilder, viewBindList)
      }
      else if(key == OnEditorAction.name){
        listenClass = MethodBindListenClass.OnEditorAction
        classBuilder = ensureBindClassExist()
        realProcessMethodBindInfo(listenClass, classBuilder, viewBindList)
      }
      else if(key == OnFocusChange.name){
        listenClass = MethodBindListenClass.OnFocusChange
        classBuilder = ensureBindClassExist()
        realProcessMethodBindInfo(listenClass, classBuilder, viewBindList)
      }
      else if(key == OnItemClick.name){
        listenClass = MethodBindListenClass.OnItemClick
        classBuilder = ensureBindClassExist()
        realProcessMethodBindInfo(listenClass, classBuilder, viewBindList)
      }
      else if(key == OnItemLongClick.name){
        listenClass = MethodBindListenClass.OnItemLongClick
        classBuilder = ensureBindClassExist()
        realProcessMethodBindInfo(listenClass, classBuilder, viewBindList)
      }
      else if(key == OnItemSelected.name){
        listenClass = MethodBindListenClass.OnItemSelected
        classBuilder = ensureBindClassExist()
        realProcessMethodBindInfo(listenClass, classBuilder, viewBindList)
      }
      else if(key == OnTouch.name){
        listenClass = MethodBindListenClass.OnTouch
        classBuilder = ensureBindClassExist()
        realProcessMethodBindInfo(listenClass, classBuilder, viewBindList)
      }


    }
    if(commonBindClassBuilder != null){
      commonBindClassBuilder.build()

    }
  }

  def realProcessMethodBindInfo(MethodBindListenClass listenClass, ViewBindClassBuilder classBuilder,
                                List<MethodViewBind> viewBindList){
    // 构造class 类

    fillClass(listenClass, classBuilder)
    viewBindList.each {
      buildMethodSrc += buildSetCodeBlock(listenClass, it, classBuilder.fullName)

      buildMethodCallCodeBlock(listenClass, it)
    }
    endInject(listenClass, classBuilder)
  }

  def buildMethodCallCodeBlock(MethodBindListenClass listenClass, MethodViewBind methodViewBind) {
    def bindFuncModel = listenClass.findMethodBindFuncModelByMethod(methodViewBind.bindMethodName)
    def  parameterList = bindFuncModel.parameterList
    def methodParams = methodViewBind.paramsTypes
    def inputParamsCount = methodParams.size()
    def code = ""
    if(!listenClass.isNoId()){
      code = "if (id == ${methodViewBind.viewId}) {\n"
    }
    if(methodViewBind.withReturnValue() && bindFuncModel.hasReturnValue()){
      code += "return "
    }
    code += "${ConstantList.NAME_FIELD_OUTER_CLASS}.${methodViewBind.targetMethodName}("
    for (int i = 0; i < inputParamsCount; i++) {
      Parameter param = parameterList.get(i)
      code += "(${methodParams.get(i)})${param.name}"
      if(i != inputParamsCount -1){
        code += ","
      }
    }
    code += ");\n"
    if(!listenClass.isNoId()){
      code += "}\n"
    }
    bindFuncModel.fillCtMethod(code)
  }

  def fillClass(MethodBindListenClass listenClass, ViewBindClassBuilder classBuilder) {
    //step 1: 插入接口
    classBuilder.addInterface(listenClass.bindInterface)
    //step 2:插入方法
    for (MethodBindFuncModel funcModel : listenClass.funcModelList) {
      funcModel.buildCtMethod(listenClass.isNoId())
    }
  }

  def endInject(MethodBindListenClass listenClass, ViewBindClassBuilder classBuilder) {
    for (MethodBindFuncModel funcModel: listenClass.funcModelList) {
      funcModel.endBuildMethod(classBuilder)
    }
  }

  /**
   * build code block like ((android.view.View)view).setOnClickLister(new Dispatcher(this))
   * @param listenClass
   * @param viewBind
   * @param listenerName
   * @return
   */
  def buildSetCodeBlock(MethodBindListenClass listenClass, MethodViewBind viewBind,
                        String listenerName){
    //step 1 : set view
    def code = ConstantList.NAME_TEMP_VIEW
    if(!viewBind.viewFieldName || viewBind.viewFieldName.empty){
      code += "= (${viewBind.viewClassType})" +
          "${ConstantList.VIEW_SOURCE}.findViewById(${viewBind.viewId});\n"
    }
    else{
      code += "= (${viewBind.viewClassType})${viewBind.viewFieldName};\n"
    }
    //step 2: set listener
    code += "((${viewBind.viewClassType})${ConstantList.NAME_TEMP_VIEW})" +
        ".${listenClass.setMethod}(new $listenerName(this));\n"
    return code
  }

  def ensureBindClassExist(){
    if(commonBindClassBuilder != null){
      return commonBindClassBuilder
    }
    commonBindClassBuilder = new ViewBindClassBuilder(classPath, targetClass, ConstantList.NAME_CLASS_EVENT_DISPATCHER)
    return commonBindClassBuilder
  }
}
