package com.example.title_1;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;

public class ColorButton {

    int colorFrom;
    int colorTo;

    public void aloneBig(View v){
        colorFrom = Color.RED;
        colorTo = Color.BLACK;
        setFlash(v,colorFrom,colorTo);
    }

    public void cherryBig(View v){
        colorFrom = Color.MAGENTA;
        colorTo = Color.BLACK;
        setFlash(v,colorFrom,colorTo);
    }

    public void aloneReg(View v){
        colorFrom = Color.BLUE;
        colorTo = Color.BLACK;
        setFlash(v,colorFrom,colorTo);
    }

    public void cherryReg(View v){
        colorFrom = Color.CYAN;
        colorTo = Color.BLACK;
        setFlash(v,colorFrom,colorTo);
    }

    public void cherry(View v){
        colorFrom = Color.YELLOW;
        colorTo = Color.BLACK;
        setFlash(v,colorFrom,colorTo);
    }

    public void grapes(View v){
        colorFrom = Color.GREEN;
        colorTo = Color.BLACK;
        setFlash(v,colorFrom,colorTo);
    }

    public void setFlash(View v, int colorFrom, int colorTo){
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
