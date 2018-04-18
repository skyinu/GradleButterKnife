package com.skyinu.gradlebutterknife.sample;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.skyinu.annotations.BindView;
import com.skyinu.gradlebutterknife.GradleButterKnife;

/**
 * Created by chen on 2018/3/12.
 */

public class BaseActivity extends AppCompatActivity {

  @BindView(R2.id.title)
  protected TextView title;
  @BindView(R2.id.contentPanel)
  private FrameLayout content;
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.setContentView(R.layout.activity_base_layout);
    GradleButterKnife.bind(this);
    title.setText("Base title is tile");
  }

  public void setContentView(@LayoutRes int layoutResID) {
    View view = LayoutInflater.from(this).inflate(layoutResID, null);
    this.content.addView(view);
    GradleButterKnife.bind(this);
  }
}
