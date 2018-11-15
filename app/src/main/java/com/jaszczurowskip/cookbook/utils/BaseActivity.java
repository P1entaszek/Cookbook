package com.jaszczurowskip.cookbook.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jaszczurowskip on 14.11.2018
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int frameId, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameId, fragment, tag);
        fragmentTransaction.commit();
    }
}
