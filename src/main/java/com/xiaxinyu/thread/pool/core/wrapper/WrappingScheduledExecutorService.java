package com.xiaxinyu.thread.pool.core.wrapper;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * An abstract {@code ScheduledExecutorService} that allows subclasses to
 * {@linkplain #wrapTask(Callable) wrap} tasks before they are submitted to the underlying executor.
 *
 * <p>Note that task wrapping may occur even if the task is never executed.
 * <p>com.google.common.util.concurrent.WrappingScheduledExecutorService</p>
 * @author LiuYang
 */
public abstract class WrappingScheduledExecutorService extends WrappingExecutorService
        implements ScheduledExecutorService {
    final ScheduledExecutorService delegate;

    protected WrappingScheduledExecutorService(ScheduledExecutorService delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    @Override
    public final ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return delegate.schedule(wrapTask(command), delay, unit);
    }

    @Override
    public final <V> ScheduledFuture<V> schedule(Callable<V> task, long delay, TimeUnit unit) {
        return delegate.schedule(wrapTask(task), delay, unit);
    }

    @Override
    public final ScheduledFuture<?> scheduleAtFixedRate(
            Runnable command, long initialDelay, long period, TimeUnit unit) {
        return delegate.scheduleAtFixedRate(wrapTask(command), initialDelay, period, unit);
    }

    @Override
    public final ScheduledFuture<?> scheduleWithFixedDelay(
            Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return delegate.scheduleWithFixedDelay(wrapTask(command), initialDelay, delay, unit);
    }
}
