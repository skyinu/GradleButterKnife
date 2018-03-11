package com.skyinu.gradlebutterknife;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyinu.annotations.BindAnim;
import com.skyinu.annotations.BindBitmap;
import com.skyinu.annotations.BindBool;
import com.skyinu.annotations.BindColor;
import com.skyinu.annotations.BindDimen;
import com.skyinu.annotations.BindInt;
import com.skyinu.annotations.BindString;
import com.skyinu.annotations.BindView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text)
    private TextView textView;
    @BindString(R.string.app_test_string)
    private String injectString;
    @BindInt(R.integer.app_test_interger)
    private int injectInt;
    @BindBool(R.bool.app_test_bool)
    private boolean injectBool;
    @BindColor(R.color.app_test_color)
    private int injectColor;
    @BindDimen(R.dimen.app_test_dimen)
    private float injectDimen;
    @BindBitmap(R.mipmap.ic_launcher)
    private Drawable injectBitmap;
    @BindAnim(R.anim.app_test_anim)
    private Animation injectAnimation;
    @BindView(R.id.image)
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GradleButterKnife.bind(this);
        textView.setText(injectString + injectInt + injectBool
                + injectColor + injectDimen);
        textView.setBackgroundColor(injectColor);
        imageView.setBackgroundDrawable(injectBitmap);
        imageView.startAnimation(injectAnimation);
    }
}
