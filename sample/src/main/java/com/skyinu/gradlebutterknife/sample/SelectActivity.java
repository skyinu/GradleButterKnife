package com.skyinu.gradlebutterknife.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.skyinu.annotations.BindView;
import com.skyinu.gradlebutterknife.GradleButterKnife;
import com.skyinu.samplemodule.SampleModuleActivity;

/**
 * Created by chen on 2018/3/12.
 */

public class SelectActivity extends AppCompatActivity {

  @BindView(R2.id.button1)
  private Button button1;
  @BindView(R2.id.button2)
  private Button button2;
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_layout);
    GradleButterKnife.bind(this);
    button1.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(SelectActivity.this, MainActivity.class);
        startActivity(intent);
      }
    });

    button2.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(SelectActivity.this, SampleModuleActivity.class);
        startActivity(intent);
      }
    });
  }
}
