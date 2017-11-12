package com.alexdev.photomap.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

public final class UiUtils {

    private UiUtils() {}

    public static void switchDrawableTint(Drawable drawable, Context context, boolean switcher,
                                               int colorResId1, int colorResId2) {
        DrawableCompat.setTint(
                drawable.mutate(), ContextCompat.getColor(context, switcher ? colorResId2 : colorResId1)
        );
    }

}
