package com.example.title_1;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;

public class ColorButton {

    int colorFrom = Color.BLACK;
    int colorTo = Color.BLACK;

    public void setFlash(View v,int id){

        switch (id){

            case R.id.aB:
                colorFrom = Color.RED;
                break;

            case R.id.cB:
                colorFrom = Color.MAGENTA;
                break;

            case R.id.aR:
                colorFrom = Color.BLUE;
                break;

            case R.id.cR:
                colorFrom = Color.CYAN;
                break;

            case R.id.ch:
                colorFrom = Color.YELLOW;
                break;

            case R.id.gr:
                colorFrom = Color.GREEN;
                break;
            default:
        }
        flashAnimation(v,colorFrom,colorTo);
    }

    public void flashAnimation(View v, int colorFrom, int colorTo){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(100);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                v.setBackgroundColor((int)valueAnimator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }
}
