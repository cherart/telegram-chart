package com.cherkashyn.telegramchart.utils;

import android.content.res.Resources;

public class Utils {

    public static float dpToPx(float dp) {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }
}
