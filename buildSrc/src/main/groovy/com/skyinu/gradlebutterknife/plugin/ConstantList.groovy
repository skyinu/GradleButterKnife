package com.skyinu.gradlebutterknife.plugin

/**
 * Created by chen on 2018/3/9.
 */

public class ConstantList {
    public static String MATCHER_R = ".+R2\\\$.*|.+R2"
    public static String VIEW_SOURCE = "source"
    public static String NAME_INJECT_METHOD = "_inject_"
    public static String NAME_TEMP_VIEW = "_tmp_view_"
    public static String NAME_FLAG_INTERFACE = "com.skyinu.gradlebutterknife.Injectable"
    public static String NAME_FIELD_OUTER_CLASS = "out"
    public static String NAME_CLASS_CONTEXT = "android.content.Context"
    public static String NAME_CONTEXT_FIELD = "context"
    public static String NAME_CLASS_RESOURCES = "android.content.res.Resources"
    public static String NAME_RESOURCE_FIELD = "resource"
    public static String NAME_CLASS_ANIMATIONUTILS = "android.view.animation.AnimationUtils"
    public static String NAME_CLASS_EVENT_DISPATCHER = "GradleKnifeEventDispatcher"
    public static String NAME_LISTENER_INSTANCE = "_tmp_listener_"
}
