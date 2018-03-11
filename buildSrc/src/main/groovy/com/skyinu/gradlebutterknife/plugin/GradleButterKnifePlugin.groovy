package com.skyinu.gradlebutterknife.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class GradleButterKnifePlugin implements Plugin<Project> {
    Project projectContext

    @Override
    void apply(Project project) {
        this.projectContext = project
        project.android.registerTransform(new AnnotationsTransform(project))
    }
}
