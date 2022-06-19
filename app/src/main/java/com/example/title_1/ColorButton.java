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

            case R.id.eSingleBig:
                colorFrom = Color.RED;
                break;

            case R.id.eCherryBig:
                colorFrom = Color.MAGENTA;
                break;

            case R.id.eSingleReg:
                colorFrom = Color.BLUE;
                break;

            case R.id.eCherryReg:
                colorFrom = Color.CYAN;
                break;

            case R.id.eCherry:
                colorFrom = Color.YELLOW;
                break;

            case R.id.eGrape:
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
