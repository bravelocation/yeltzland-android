package com.bravelocation.yeltzland;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by John on 25/05/2016.
 */
public class FontManager {

    private static Map<String, Typeface> loadedTypefaces = new HashMap<String, Typeface>();

    public static final String ROOT = "fonts/",
            FONTAWESOME = ROOT + "fontawesome-webfont.ttf";

    public static Typeface getTypeface(Context context, String font) {
        Typeface typeface = loadedTypefaces.get(font);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), font);
            loadedTypefaces.put(font, typeface);
        }

        return typeface;
    }
}