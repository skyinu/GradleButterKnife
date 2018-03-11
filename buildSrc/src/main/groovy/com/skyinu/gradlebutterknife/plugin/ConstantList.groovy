package com.skyinu.gradlebutterknife.plugin;

/**
 * Created by chen on 2018/3/9.
 */

public class ConstantList {
    static def MATCHER_R = ".+R\\\$.*|.+R"
    static def VIEW_SOURCE = "source"
    static def NAME_INJECT_METHOD = "inject"
    static def NAME_FLAG_INTERFACE = "com.skyinu.gradlebutterknife.Injectable"
    static def NAME_CLASS_CONTEXT = "android.content.Context"
    static def NAME_CONTEXT_FIELD = "context"
    static def NAME_CLASS_RESOURCES = "android.content.res.Resources"
    static def NAME_RESOURCE_FIELD = "resource"
    static def NAME_CLASS_ANIMATIONUTILS = "android.view.animation.AnimationUtils"
}
