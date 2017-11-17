package com.alexdev.photomap.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

@UiThread
public final class UiUtils {

    private UiUtils() {}

    public static void switchDrawableTint(Drawable drawable, Context context, boolean switcher,
                                          @ColorRes int colorResId1, @ColorRes int colorResId2) {
        DrawableCompat.setTint(
                drawable.mutate(), ContextCompat.getColor(context, switcher ? colorResId2 : colorResId1)
        );
    }

}
