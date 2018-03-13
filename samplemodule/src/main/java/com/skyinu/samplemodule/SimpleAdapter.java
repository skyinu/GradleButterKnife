package com.skyinu.samplemodule;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.skyinu.annotations.BindView;
import com.skyinu.gradlebutterknife.GradleButterKnife;

public class SimpleAdapter extends BaseAdapter {
  private static final String[] CONTENTS = "The quick brown fox jumps over the lazy dog".split(" ");

  @NonNull
  private final LayoutInflater inflater;

  public SimpleAdapter(Context context) {
    inflater = LayoutInflater.from(context);
  }

  @Override public int getCount() {
    return CONTENTS.length;
  }

  @Override public String getItem(int position) {
    return CONTENTS[position];
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View view, ViewGroup parent) {
    ViewHolder holder;
    if (view != null) {
      holder = (ViewHolder) view.getTag();
    } else {
      view = inflater.inflate(R.layout.simple_list_item, parent, false);
      holder = new ViewHolder(view);
      view.setTag(holder);
    }

    String word = getItem(position);
    holder.word.setText("Word: " + word);
    holder.length.setText("Length: " + word.length());
    holder.position.setText("Position: " + position);
    // Note: don't actually do string concatenation like this in an adapter's getView.

    return view;
  }

  static final class ViewHolder {
    @BindView(R2.id.word) TextView word;
    @BindView(R2.id.length) TextView length;
    @BindView(R2.id.position) TextView position;

    ViewHolder(View view) {
      GradleButterKnife.bind(this, view);
    }
  }
}
