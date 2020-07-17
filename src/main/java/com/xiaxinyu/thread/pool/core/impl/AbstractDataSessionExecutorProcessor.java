package com.xiaxinyu.thread.pool.core.impl;

import com.xiaxinyu.thread.pool.core.SessionExecutorProcessor;
import org.springframework.lang.Nullable;

/**
 * 抽象数据拦截器
 * @author LiuYang
 * @date 2020/7/6
 */
public abstract class AbstractDataSessionExecutorProcessor<T> implements SessionExecutorProcessor {
    @Nullable
    private T data;

    private int order;

    public AbstractDataSessionExecutorProcessor(@Nullable T data) {
        this(data, DEFAULT_ORDER);
    }

    public AbstractDataSessionExecutorProcessor(@Nullable T data, int order) {
        this.data = data;
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    /**
     * 获取数据
     * <p>例如需要处理的数据</p>
     * @return 数据
     */
    protected T getData() {
        return data;
    }

}
