package com.jaszczurowskip.cookbook.utils.rx;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jaszczurowskip on 05.09.2018
 */
public class DefaultAppSchedulers implements AppSchedulers {

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @NonNull
    @Override
    public Scheduler newThread() {
        return Schedulers.newThread();
    }

    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }
}
