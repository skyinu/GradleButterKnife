package com.skyinu.samplemodule;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.skyinu.annotations.BindView;
import com.skyinu.gradlebutterknife.GradleButterKnife;

/**
 * Created by chen on 2018/3/12.
 */

public class SampleModuleActivity extends AppCompatActivity {
  @BindView(R2.id.text)
  private TextView textView;
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample_module_layout);
    GradleButterKnife.bind(this);
    textView.setBackgroundColor(Color.BLUE);
  }
}
