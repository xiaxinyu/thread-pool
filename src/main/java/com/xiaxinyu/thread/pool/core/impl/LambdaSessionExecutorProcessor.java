package com.xiaxinyu.thread.pool.core.impl;
import com.xiaxinyu.thread.pool.core.SessionExecutorProcessor;

import javax.validation.constraints.NotNull;

/**
 * 表达式拦截器
 * @author LiuYang
 * @date 2020/7/6
 */
public class LambdaSessionExecutorProcessor implements SessionExecutorProcessor {

    private int order;
    private Runnable preHandle;
    private Runnable afterCompletion;

    public LambdaSessionExecutorProcessor(@NotNull Runnable preHandle, @NotNull Runnable afterCompletion, int order) {
        this.order = order;
        this.preHandle = preHandle;
        this.afterCompletion = afterCompletion;
    }

    public LambdaSessionExecutorProcessor(@NotNull Runnable preHandle, @NotNull Runnable afterCompletion) {
        this(preHandle, afterCompletion, DEFAULT_ORDER);
    }

    @Override
    public void preHandle() {
        preHandle.run();
    }

    @Override
    public void afterCompletion() {
        afterCompletion.run();
    }

    @Override
    public int getOrder() {
        return order;
    }

}
