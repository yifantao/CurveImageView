package com.example.taoyifan.curves;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final AnimatorSet set = new AnimatorSet();
        final View cv = findViewById(R.id.cv);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(cv, "angle", 1, 60)
                .setDuration(3000);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(cv, "rotateY", 0, 360)
                .setDuration(3000);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(cv, "angle", 60, 1)
                .setDuration(3000);
        set.play(animator3).after(animator2);
        set.play(animator2).after(animator1);
        set.start();

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (set.isRunning()) {
                    set.cancel();
                }
                set.start();
            }
        });
    }
}
