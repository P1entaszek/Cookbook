package com.jaszczurowskip.cookbook.utils.rx;

/**
 * Created by jaszczurowskip on 07.09.2018
 */
public class AppSchedulersProvider {
    private static AppSchedulers Instance;

    public static AppSchedulers getInstance() {
        if (Instance == null) {
            Instance = new DefaultAppSchedulers();
        }
        return Instance;
    }
}