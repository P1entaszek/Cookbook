package com.jaszczurowskip.cookbook.utils.rx;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by jaszczurowskip on 12.12.2018
 */
public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {
    private final int maxRetries;
    private final int retryDelaySeconds;
    private int retryCount;

    public RetryWithDelay(final int maxRetries, final int retryDelaySeconds) {
        this.maxRetries = maxRetries;
        this.retryDelaySeconds = retryDelaySeconds;
        this.retryCount = 0;
    }

    @Override
    public Observable<?> apply(final Observable<? extends Throwable> attempts) {
        return attempts
                .flatMap((Function<Throwable, Observable<?>>) throwable -> {
                    if (++retryCount < maxRetries) {
                        return Observable.timer(retryDelaySeconds,
                                TimeUnit.SECONDS);
                    }
                    return Observable.error(throwable);
                });
    }
}
