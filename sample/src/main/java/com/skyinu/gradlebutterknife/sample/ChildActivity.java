package com.skyinu.gradlebutterknife.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import com.skyinu.annotations.BindView;

/**
 * Created by chen on 2018/3/12.
 */

public class ChildActivity extends BaseActivity {
  @BindView(R2.id.child_text)
  protected TextView childTitle;
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_child_layout);
    childTitle.setText("child text");
  }
}
