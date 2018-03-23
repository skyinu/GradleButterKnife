package com.skyinu.gradlebutterknife.plugin

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.skyinu.gradlebutterknife.plugin.util.Log
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import groovy.json.StringEscapeUtils

class GradleButterKnifePlugin implements Plugin<Project> {
  Project projectContext

  @Override
  void apply(Project project) {
    if (!(project.plugins.hasPlugin(LibraryPlugin) || project.plugins.hasPlugin(AppPlugin))) {
      throw new IllegalStateException('plugin can only be applied to android projects')
    }
    this.projectContext = project
    project.extensions.create(GradleButterKnifeExtension.DSL_DOMAIN_NAME, GradleButterKnifeExtension)
    Log.init(project)
    registerResourceProcess(project)
    project.android.registerTransform(new AnnotationsTransform(project))
  }

  def registerResourceProcess(Project project) {
    def variants
    if (project.plugins.hasPlugin(LibraryPlugin)) {
      variants = project.android.libraryVariants
    } else {
      variants = project.android.applicationVariants
    }

    project.afterEvaluate {
      variants.all { BaseVariant variant ->
        variant.outputs.each { BaseVariantOutput output ->
          output.processResources.doLast {
            File rDir = new File(sourceOutputDir, packageForR.replaceAll('\\.',
                StringEscapeUtils.escapeJava(File.separator)))
            File R = new File(rDir, 'R.java')
            FinalRClassBuilder.brewJava(R, sourceOutputDir, packageForR, 'R2')
          }
        }
      }
    }
  }
}
