package com.skyinu.gradlebutterknife.plugin

/**
 * Created by chen on 2018/3/9.
 */

public class ConstantList {
    public static String MATCHER_R = ".+R2\\\$.*|.+R2"
    public static String VIEW_SOURCE = "source"
    public static String NAME_INJECT_METHOD = "inject"
    public static String NAME_TEMP_VIEW = "_tmp_view_"
    public static String NAME_FLAG_INTERFACE = "com.skyinu.gradlebutterknife.Injectable"
    public static String NAME_ONCLICK_INTERFACE = "android.view.View\$OnClickListener"
    public static String NAME_ONLONGCLICK_INTERFACE = "android.view.View\$OnLongClickListener"
    public static String NAME_INJECT_INNER_CLASS = "GradleKnifeClickDispatcher"
    public static String NAME_INJECT_LONG_CLICK_CLASS = "GradleKnifeLongClickDispatcher"
    public static String NAME_FIELD_OUTER_CLASS = "out"
    public static String NAME_CLASS_CONTEXT = "android.content.Context"
    public static String NAME_CONTEXT_FIELD = "context"
    public static String NAME_CLASS_RESOURCES = "android.content.res.Resources"
    public static String NAME_RESOURCE_FIELD = "resource"
    public static String NAME_CLASS_ANIMATIONUTILS = "android.view.animation.AnimationUtils"
}
