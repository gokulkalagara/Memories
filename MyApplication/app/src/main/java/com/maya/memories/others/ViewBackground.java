package com.maya.memories.others;

/**
 * Created by home on 7/26/2017.
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
public class ViewBackground {

    public static StateListDrawable setBackground(int normal, int pressed) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(normal));
        states.addState(new int[]{-android.R.attr.state_pressed}, new ColorDrawable(pressed));
        return states;
    }

    public static ColorStateList setTextColor(int normal, int pressed) {
        int[][] state = new int[][]{
                new int[]{-android.R.attr.state_pressed}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[]{
                normal,
                pressed
        };
        ColorStateList colorStates = new ColorStateList(state, colors);
        return colorStates;
    }

    public static int dpSize(Context context, int sizeInDp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (sizeInDp * scale + 0.5f);
    }

}
