package com.skyinu.gradlebutterknife.sample;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import com.skyinu.gradlebutterknife.GradleButterKnife;

/**
 * Created by chen on 2018/3/12.
 */

public class BaseActivity extends AppCompatActivity {

  public void setContentView(@LayoutRes int layoutResID) {
    View view = LayoutInflater.from(this).inflate(layoutResID, null);
    super.setContentView(view);
    GradleButterKnife.bind(this);
  }
}
