package com.skyinu.samplemodule;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.skyinu.annotations.BindView;
import com.skyinu.annotations.BindViews;
import com.skyinu.annotations.OnTouch;
import com.skyinu.gradlebutterknife.GradleButterKnife;

import java.util.List;

/**
 * Created by chen on 2018/3/12.
 */

public class SampleModuleActivity extends AppCompatActivity {
  @BindView(R2.id.text)
  private TextView textView;
  @BindViews({R2.id.text1, R2.id.text2})
  private TextView[] textViews;
  @BindViews({R2.id.text3, R2.id.text4})
  private List<TextView> textViewList;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample_module_layout);
    GradleButterKnife.bind(this);
    textView.setBackgroundColor(Color.BLUE);
    int count = 0;
    for (TextView tv : textViews) {
      tv.setText("tv " + count);
      count++;
    }
    for (TextView tv : textViewList) {
      tv.setText("tv " + count);
      count++;
    }
  }

  @OnTouch({R2.id.text, R2.id.text1, R2.id.text2})
  public void handlerTouch(TextView textView) {
    Toast.makeText(this, "view touched", Toast.LENGTH_SHORT).show();
  }
}
