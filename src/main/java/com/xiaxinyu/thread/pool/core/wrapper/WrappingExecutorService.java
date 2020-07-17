package com.xiaxinyu.thread.pool.core.wrapper;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * An abstract {@code ExecutorService} that allows subclasses to
 * {@linkplain #wrapTask(Callable) wrap} tasks before they are submitted
 * to the underlying executor.
 *
 * <p>Note that task wrapping may occur even if the task is never executed.
 * <p>see com.google.common.util.concurrent.WrappingExecutorService</p>
 * @author LiuYang
 *
 */
public abstract class WrappingExecutorService implements ExecutorService {
    private final ExecutorService delegate;

    protected WrappingExecutorService(ExecutorService delegate) {
        Assert.notNull(delegate, "delegate is null");
        this.delegate = delegate;
    }

    /**
     * Wraps a {@code Callable} for submission to the underlying executor. This
     * method is also applied to any {@code Runnable} passed to the default
     */
    protected abstract <T> Callable<T> wrapTask(Callable<T> callable);

    /**
     * Wraps a {@code Runnable} for submission to the underlying executor. The
     * default implementation delegates to {@link #wrapTask(Callable)}.
     */
    protected Runnable wrapTask(Runnable command) {
        final Callable<Object> wrapped = wrapTask(
                Executors.callable(command, null));
        return new Runnable() {
            @Override
            public void run() {
                try {
                    wrapped.call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    /**
     * Wraps a collection of tasks.
     *
     * @throws NullPointerException if any element of {@code tasks} is null
     */
    private final <T> List<Callable<T>> wrapTasks(
            Collection<? extends Callable<T>> tasks) {

        final List<Callable<T>> wrapTasks = new ArrayList<>();
        for (Callable<T> task : tasks) {
            wrapTasks.add(wrapTask(task));
        }
        return Collections.unmodifiableList(wrapTasks);
    }

    // These methods wrap before delegating.
    @Override
    public final void execute(Runnable command) {
        delegate.execute(wrapTask(command));
    }

    @Override
    public final <T> Future<T> submit(Callable<T> task) {
        Assert.notNull(task, "task is null");
        return delegate.submit(wrapTask(task));
    }

    @Override
    public final Future<?> submit(Runnable task) {
        return delegate.submit(wrapTask(task));
    }

    @Override
    public final <T> Future<T> submit(Runnable task, T result) {
        return delegate.submit(wrapTask(task), result);
    }

    @Override
    public final <T> List<Future<T>> invokeAll(
            Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return delegate.invokeAll(wrapTasks(tasks));
    }

    @Override
    public final <T> List<Future<T>> invokeAll(
            Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException {
        return delegate.invokeAll(wrapTasks(tasks), timeout, unit);
    }

    @Override
    public final <T> T invokeAny(Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException {
        return delegate.invokeAny(wrapTasks(tasks));
    }

    @Override
    public final <T> T invokeAny(
            Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.invokeAny(wrapTasks(tasks), timeout, unit);
    }

    // The remaining methods just delegate.

    @Override
    public final void shutdown() {
        delegate.shutdown();
    }

    @Override
    public final List<Runnable> shutdownNow() {
        return delegate.shutdownNow();
    }

    @Override
    public final boolean isShutdown() {
        return delegate.isShutdown();
    }

    @Override
    public final boolean isTerminated() {
        return delegate.isTerminated();
    }

    @Override
    public final boolean awaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException {
        return delegate.awaitTermination(timeout, unit);
    }
}
