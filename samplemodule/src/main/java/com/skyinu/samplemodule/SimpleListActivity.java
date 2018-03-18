package com.skyinu.samplemodule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.skyinu.annotations.BindView;
import com.skyinu.annotations.OnItemClick;
import com.skyinu.annotations.OnItemLongClick;
import com.skyinu.annotations.OnItemSelected;
import com.skyinu.gradlebutterknife.GradleButterKnife;

/**
 * Created by chen on 2018/3/13.
 */

public class SimpleListActivity extends AppCompatActivity {
  @BindView(R2.id.list_of_things) @NonNull ListView listOfThings;
  @BindView(R2.id.list_of_things1) @NonNull ListView listOfThings1;
  @NonNull
  private SimpleAdapter adapter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.simple_activity);
    GradleButterKnife.bind(this);
    adapter = new SimpleAdapter(this);
    listOfThings.setAdapter(adapter);
    listOfThings1.setAdapter(adapter);
  }

  @OnItemClick({R2.id.list_of_things, R2.id.list_of_things1})
  public boolean handleItemClick(AdapterView<?> parent, View view){
    Toast.makeText(this, "item click " + parent, Toast.LENGTH_SHORT).show();
    return false;
  }

  @OnItemLongClick({R2.id.list_of_things})
  public boolean handleItemLongClick(AdapterView<?> parent, View view,int position){
    Toast.makeText(this, "item long click " + position + "  " + parent, Toast.LENGTH_SHORT).show();
    return false;
  }

  @OnItemSelected(value = {R2.id.list_of_things, R2.id.list_of_things1})
  public boolean handleItemSelect(AdapterView<?> parent, View view,int position){
    Toast.makeText(this, "item select " + position + "  " + parent, Toast.LENGTH_SHORT).show();
    return false;
  }

  @OnItemSelected(value = {R2.id.list_of_things}, callback = OnItemSelected.Callback.ON_NOTHING_SELECTED)
  public boolean handleItemNothingSelect(AdapterView<?> parent){
    Toast.makeText(this, "item nothing " + parent, Toast.LENGTH_SHORT).show();
    return false;
  }
}
