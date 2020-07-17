package com.xiaxinyu.thread.pool.core.impl;


import com.xiaxinyu.thread.pool.core.SessionExecutorProcessor;
import com.xiaxinyu.thread.pool.core.SessionScheduledExecutorService;
import com.xiaxinyu.thread.pool.core.wrapper.WrappingScheduledExecutorService;
import org.springframework.core.Ordered;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

/**
 * 调度会话传递执行线程实现类
 * @author LiuYang
 * @date 2020/7/6
 */
public class SessionScheduledExecutorServiceImpl extends WrappingScheduledExecutorService implements SessionScheduledExecutorService {

    @NotNull
    private final List<SessionExecutorProcessor> sessionExecutorProcessors = new ArrayList<>();

    public SessionScheduledExecutorServiceImpl(@NotNull ScheduledExecutorService delegate) {
        super(delegate);
    }

    public SessionScheduledExecutorServiceImpl(@NotNull ScheduledExecutorService delegate, @NotNull SessionExecutorProcessor... sessionExecutorProcessors) {
        super(delegate);
        this.sessionExecutorProcessors.addAll(Arrays.asList(sessionExecutorProcessors));
    }

    public SessionScheduledExecutorServiceImpl(@NotNull ScheduledExecutorService delegate, @NotNull List<SessionExecutorProcessor> sessionExecutorProcessors) {
        super(delegate);
        this.sessionExecutorProcessors.addAll(sessionExecutorProcessors);
    }

    @Override
    protected <T> Callable<T> wrapTask(@NotNull Callable<T> callable) {
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
