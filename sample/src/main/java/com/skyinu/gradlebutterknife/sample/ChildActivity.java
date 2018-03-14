package com.skyinu.gradlebutterknife.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.skyinu.annotations.BindView;
import com.skyinu.annotations.OnTextChanged;
import java.util.Random;

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
    EditText editText;
    //editText.addTextChangedListener(new TextWatcher() {
    //  @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    //
    //  }
    //
    //  @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
    //
    //  }
    //
    //  @Override public void afterTextChanged(Editable s) {
    //
    //  }
    //});
  }

  @OnTextChanged(R2.id.edit_text)
  public void onTextChanged() {
    childTitle.setText("child text " + System.currentTimeMillis());
  }

  @OnTextChanged(value = R2.id.edit_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
  public void afterText() {
    Toast.makeText(this, "changed", Toast.LENGTH_SHORT).show();
  }
}
