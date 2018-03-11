package com.skyinu.gradlebutterknife;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

/**
 * Created by chen on 2018/3/9.
 */

public class GradleButterKnife {
    @UiThread
    public static void bind(@NonNull Activity source) {
        Injectable injectable = (Injectable) source;
        injectable.inject(source.getWindow().getDecorView());

    }
}
