package com.skyinu.gradlebutterknife.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import com.skyinu.annotations.BindView;
import com.skyinu.annotations.OnClick;
import com.skyinu.annotations.OnLongClick;
import com.skyinu.gradlebutterknife.GradleButterKnife;
import com.skyinu.samplemodule.SampleModuleActivity;
import com.skyinu.samplemodule.SimpleListActivity;

/**
 * Created by chen on 2018/3/12.
 */

public class SelectActivity extends AppCompatActivity {

  @BindView(R2.id.button1)
  private Button button1;
  @BindView(R2.id.button2)
  private Button button2;
  @BindView(R2.id.button3)
  private Button button3;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_layout);
    GradleButterKnife.bind(this);
  }

  @OnLongClick(R2.id.button1)
  boolean gotoMainActivity() {
    Intent intent = new Intent(SelectActivity.this, MainActivity.class);
    startActivity(intent);
    return true;
  }

  @OnLongClick(R2.id.button2)
  void gotoSampleModuleActivity(Button button) {
    Intent intent = new Intent(button.getContext(), SampleModuleActivity.class);
    startActivity(intent);
  }

  @OnClick({ R2.id.button3, R2.id.button4 })
  void gotoChildActivity(Button button) {
    if (button.getId() == R2.id.button3) {
      Intent intent = new Intent(SelectActivity.this, ChildActivity.class);
      startActivity(intent);
    } else if (button.getId() == R2.id.button4) {
      Intent intent = new Intent(SelectActivity.this, SimpleListActivity.class);
      startActivity(intent);
    }
  }
}
