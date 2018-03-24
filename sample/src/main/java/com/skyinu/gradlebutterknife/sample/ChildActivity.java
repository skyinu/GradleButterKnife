package com.skyinu.gradlebutterknife.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import com.skyinu.annotations.BindView;
import com.skyinu.annotations.OnCheckedChanged;
import com.skyinu.annotations.OnEditorAction;
import com.skyinu.annotations.OnFocusChange;
import com.skyinu.annotations.OnTextChanged;

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

  @OnFocusChange(R2.id.edit_text)
  public boolean handleFoucuse(TextView textView, boolean hasFocus){
    Toast.makeText(this, "hasFocus is = " + hasFocus, Toast.LENGTH_SHORT).show();
    return true;
  }

  @OnEditorAction(R2.id.edit_text)
  public boolean handleEditorAction(TextView textView, int actionId){
    Toast.makeText(this, "actionId is = " + actionId, Toast.LENGTH_SHORT).show();
    return true;
  }
  @OnTextChanged({R2.id.edit_text,R2.id.edit_text1})
  public void onTextChanged(CharSequence var1) {
    childTitle.setText("child text " + var1);
  }

  @OnTextChanged(value = R2.id.edit_text, callback = OnTextChanged.AFTER_TEXT_CHANGED)
  public void afterText() {
    Toast.makeText(this, "changed", Toast.LENGTH_SHORT).show();
  }

  @OnCheckedChanged({R2.id.checkbox_changed, R2.id.checkbox_changed1})
  public void checnChange(CompoundButton source){
    Toast.makeText(this, "state is = " + source.isChecked() + source.getId(), Toast.LENGTH_SHORT).show();
  }

  @OnCheckedChanged(R2.id.checkbox_changed2)
  public void checkChange(CompoundButton source){
    Toast.makeText(this, "another state is = " + source.isChecked(), Toast.LENGTH_SHORT).show();
  }
}
