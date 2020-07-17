package com.xiaxinyu.thread.pool.core.impl;


import com.xiaxinyu.thread.pool.core.SessionExecutorProcessor;
import com.xiaxinyu.thread.pool.core.SessionExecutorService;
import com.xiaxinyu.thread.pool.core.wrapper.WrappingExecutorService;
import org.springframework.core.Ordered;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * 会话传递执行线程实现类
 * @author LiuYang
 * @date 2020/7/6
 */
public class SessionExecutorServiceImpl extends WrappingExecutorService implements SessionExecutorService {
    @NotNull
    private final List<SessionExecutorProcessor> sessionExecutorProcessors = new ArrayList<>();

    public SessionExecutorServiceImpl(@NotNull ExecutorService delegate) {
        super(delegate);
    }

    public SessionExecutorServiceImpl(@NotNull ExecutorService delegate, @NotNull SessionExecutorProcessor... sessionExecutorProcessors) {
        this(delegate);
        this.sessionExecutorProcessors.addAll(Arrays.asList(sessionExecutorProcessors));
    }

    public SessionExecutorServiceImpl(@NotNull ExecutorService delegate, @NotNull List<SessionExecutorProcessor> sessionExecutorProcessors) {
        this(delegate);
        this.sessionExecutorProcessors.addAll(sessionExecutorProcessors);
    }

    @Override
    protected <T> Callable<T> wrapTask(Callable<T> callable) {
        return () -> {
            try {
                getSessionExecutorProcessors().forEach(SessionExecutorProcessor::preHandle);
                return callable.call();
            } finally {
                getSessionExecutorProcessors().forEach(SessionExecutorProcessor::afterCompletion);
            }
        };
    }

    /**
     * 获取所有的拦截器
     * @return 返回排序之后的拦截器
     */
    @NotNull
    public List<SessionExecutorProcessor> getSessionExecutorProcessors() {
        return sessionExecutorProcessors.stream().sorted(Comparator.nullsLast(Comparator.comparingInt(Ordered::getOrder))).collect(Collectors.toList());
    }
}
