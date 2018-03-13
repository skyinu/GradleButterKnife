package com.skyinu.samplemodule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import com.skyinu.annotations.BindView;
import com.skyinu.gradlebutterknife.GradleButterKnife;

/**
 * Created by chen on 2018/3/13.
 */

public class SimpleListActivity extends AppCompatActivity {
  @BindView(R2.id.list_of_things) @NonNull ListView listOfThings;
  @NonNull
  private SimpleAdapter adapter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.simple_activity);
    GradleButterKnife.bind(this);
    adapter = new SimpleAdapter(this);
    listOfThings.setAdapter(adapter);
  }
}
