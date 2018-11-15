package com.jaszczurowskip.cookbook.utils;

import android.content.Context;
import android.content.Intent;

import com.jaszczurowskip.cookbook.features.mealdetails.MealDetailsActivity;

/**
 * Created by jaszczurowskip on 15.11.2018
 */
public class Utils {

    public static void startDetailActivity(Context context, Class mClass) {
        Intent i = new Intent(context, mClass);
        context.startActivity(i);
    }
}
