package com.jaszczurowskip.cookbook.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by jaszczurowskip on 15.11.2018
 */
public class Utils {
    private static String typeOfImage = "data:image/jpeg;base64,";

    public static void startAnotherActivity(final @NonNull Context context, final @NonNull Class mClass) {
        Intent i = new Intent(context, mClass);
        context.startActivity(i);
    }

    public static String encodeToBase64(final @NonNull Bitmap image, final @NonNull Bitmap.CompressFormat compressFormat, final @NonNull int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return typeOfImage.concat(Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP));
    }
}
