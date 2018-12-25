package com.example.kiran.sourcemanagement;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

/**
 * Created by Kiran on 7/16/2017.
 */
public class AnimatorUtil {
    public static void animate(MyAdapter.MyViewHolder holder,boolean getDown)
    {
        AnimatorSet animatorSet=new AnimatorSet();
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(holder.tv,"translationY",getDown?200:-200,0);
        objectAnimator.setDuration(1000);
        animatorSet.playTogether(objectAnimator);
        animatorSet.start();
    }
}
