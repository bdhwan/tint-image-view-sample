package com.altamirasoft.tint_image_view_sample;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.altamirasoft.tint_image_view.TintImageMaskView;

public class MainActivity extends AppCompatActivity {

    TintImageMaskView view;

    View parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (TintImageMaskView)findViewById(R.id.view);
        parent = findViewById(R.id.parent);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTint(v);
            }
        });
    }


    public void clickFadeOut(View v){

        ObjectAnimator anim =   ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        anim.start();
    }

    public void clickFadeIn(View v){
        ObjectAnimator anim =   ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        anim.start();
    }

    public void clickTint(View v){

        if(view.getCurrentColor()!= Color.RED){
            view.changeColor(Color.parseColor("#44ff0000"));
        }
        else{
            view.changeColor(Color.BLUE);
        }

    }
}
