package com.skyinu

import org.gradle.api.Plugin
import org.gradle.api.Project

class GradleButterKnifePlugin implements Plugin<Project> {
    Project projectContext

    @Override
    void apply(Project project) {
        this.projectContext = project
    }
}
