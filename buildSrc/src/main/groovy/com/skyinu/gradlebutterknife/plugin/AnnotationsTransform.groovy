package com.skyinu.gradlebutterknife.plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.LibraryPlugin
import com.skyinu.gradlebutterknife.plugin.model.TargetClassInfo
import com.skyinu.gradlebutterknife.plugin.util.BindUtils
import javassist.ClassPath
import javassist.ClassPool
import javassist.CtClass
import org.gradle.api.Project
import com.google.common.collect.ImmutableSet
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.build.api.transform.Format
import org.apache.commons.io.FileUtils
import org.gradle.api.tasks.compile.JavaCompile

/**
 * Created by chen on 2018/3/8.*/

public class AnnotationsTransform extends Transform {
  Project project
  ClassPool classPool
  List<String> classPathList
  def collector = new StringIdCollector()
  Queue<TargetClassInfo> injectClassQueue
  ViewInjector injector

  AnnotationsTransform(Project project) {
    this.project = project
    injectClassQueue = new PriorityQueue<>(new TargetClassInfo.BindClassModelComparator())
  }

  @Override
  String getName() {
    return "bindView"
  }

  @Override
  Set<QualifiedContent.ContentType> getInputTypes() {
    return ImmutableSet.of(QualifiedContent.DefaultContentType.CLASSES)
  }

  @Override
  Set<? super QualifiedContent.Scope> getScopes() {
    if (project.plugins.hasPlugin(LibraryPlugin)) {
      return ImmutableSet.of(QualifiedContent.Scope.PROJECT)
    }
    return TransformManager.SCOPE_FULL_PROJECT
  }

  @Override
  boolean isIncremental() {
    return false
  }

  @Override
  void transform(TransformInvocation transformInvocation)
      throws TransformException, InterruptedException, IOException {
    super.transform(transformInvocation)
    TransformOutputProvider outputProvider = transformInvocation.outputProvider
    outputProvider.deleteAll()
    def config = project.extensions.findByName(GradleButterKnifeExtension.DSL_DOMAIN_NAME)
    config = config as GradleButterKnifeExtension
    classPool = new ClassPool(true)
    if(config?.dumpAble && config?.dumpDir != null && !config?.dumpDir?.isEmpty()){
        CtClass.debugDump = config.dumpDir.trim()
    }
    def inputs = new File(project.buildDir.absolutePath, "inputs_log.txt")
    inputs << "compile task's input:\n-----------------------------------------\n"
    collectClassPath()
    classPool.insertClassPath(project.android.bootClasspath[0].toString())
    List<ClassPath> classPaths = new ArrayList<>()
    classPathList.each {
      try {
        inputs << it << "\n"
        classPaths.add(classPool.insertClassPath(it))
      } catch (Exception e) {
      }
    }
    inputs << "transform task inputs:\n------------------------------------------\n"
    transformInvocation.inputs.each {
      it.jarInputs.each {
        File out = outputProvider.getContentLocation(it.name, it.contentTypes, it.scopes,
            Format.JAR)
        FileUtils.copyFile(it.file, out)
        inputs << out.path << "\n"
        classPathList.add(out.path)
        classPaths.add(classPool.appendClassPath(out.path))
      }
      it.directoryInputs.each {
        File out = outputProvider.getContentLocation(it.name, it.contentTypes, it.scopes,
            Format.DIRECTORY)
        FileUtils.copyDirectory(it.file, out)
        inputs << out.path << "\n"
        classPathList.add(out.path)
        classPaths.add(classPool.insertClassPath(out.path))
        handleDirectory(out)
      }
    }

    try {
      injector = new ViewInjector(collector.idStringMap, config)
      injectClassQueue.each {
        inject(it.injectClass, it.classFilePath)
      }

    } finally {
      injector = null
      classPool.clearImportedPackages()
      classPaths.each {
        classPool.removeClassPath(it)
      }
      CtClass.debugDump = null
    }
  }

  def inject(CtClass injectClass, String classPath) {
    injector.injectClass(injectClass, classPath)
  }

  def handleDirectory(File input) {
    input.eachFileRecurse {
      if (!it.absolutePath.endsWith(".class")) {
        return
      }
      def className = it.absolutePath.substring(input.absolutePath.length() + 1).
          replace(File.separator, ".").
          replace(".class", "")

      CtClass ctClass = classPool.getOrNull(className)
      if (ctClass == null) {
        return
      }

      if ((className) ==~ ConstantList.MATCHER_R) {
        collector.collectIdStringMapInR(ctClass)
        return
      }
      if (BindUtils.shouldBindView(ctClass)) {
        TargetClassInfo model = new TargetClassInfo(ctClass, input.absolutePath)
        injectClassQueue.add(model)
      }
    }
  }

  def collectClassPath() {
    classPathList = new ArrayList<>()
    def variants
    if (project.plugins.hasPlugin(LibraryPlugin)) {
      variants = project.android.libraryVariants
    } else {
      variants = project.android.applicationVariants
    }
    variants.all { variant ->
      JavaCompile javaCompile = variant.getJavaCompiler()
      javaCompile.classpath.each {
        classPathList.add(it.path)
      }
    }
  }
}
