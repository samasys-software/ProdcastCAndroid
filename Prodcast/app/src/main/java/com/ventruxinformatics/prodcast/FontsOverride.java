package com.ventruxinformatics.prodcast;

/**
 * Created by kdsdh on 9/9/2017.
 */

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class FontsOverride {

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,final Typeface newTypeface) {
       {
            Map<String, Typeface> newMap = new HashMap<>();
            newMap.put("sans-serif", newTypeface);
            try {
                final Field staticField = Typeface.class.getDeclaredField("sSystemFontMap");
                staticField.setAccessible(true);
                staticField.set(null, newMap);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

}