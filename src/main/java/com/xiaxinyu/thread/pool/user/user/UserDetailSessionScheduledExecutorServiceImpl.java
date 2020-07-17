package com.xiaxinyu.thread.pool.user.user;


import com.xiaxinyu.thread.pool.core.SessionExecutorProcessor;
import com.xiaxinyu.thread.pool.core.impl.SessionScheduledExecutorServiceImpl;
import com.xiaxinyu.thread.pool.user.UserDetailSessionUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.concurrent.DelegatingSecurityContextScheduledExecutorService;

import javax.validation.constraints.NotNull;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 *
 * @author LiuYang
 * @date 2020/7/7
 */
//@Slf4j
public class UserDetailSessionScheduledExecutorServiceImpl extends SessionScheduledExecutorServiceImpl implements UserDetailSessionScheduledExecutorService {
    private final Logger log = LoggerFactory.getLogger(UserDetailSessionScheduledExecutorServiceImpl.class);
    /**
     * 原始执行类
     */
    private @NotNull ScheduledExecutorService delegate;


    public UserDetailSessionScheduledExecutorServiceImpl(@NotNull ScheduledExecutorService delegate) {
        this(delegate, DelegatingSecurityContextScheduledExecutorService::new);
    }

    private UserDetailSessionScheduledExecutorServiceImpl(@NotNull ScheduledExecutorService delegate, @NotNull Function<ScheduledExecutorService, ScheduledExecutorService> wrapper) {
        super(wrapper.apply(delegate));
        this.delegate = delegate;
    }

    @Override

    public @NotNull UserDetailSessionScheduledExecutorService as(@NotNull UserDetailSessionUserDTO detailSessionUserDTO) {
        /*
         * 设置用户上下文信息拦截器
         */
        final SessionExecutorProcessor interceptor = new UserDetailSessionExecutorProcessor(detailSessionUserDTO);
        return new UserDetailSessionScheduledExecutorServiceImpl(this.delegate
                , delegate -> new SessionScheduledExecutorServiceImpl(delegate, interceptor));
    }

    @Override
    public <T> Future<T> submit(@NotNull Callable<T> task, @NotNull UserDetailSessionUserDTO detailSessionUserDTO) {
        return this.as(detailSessionUserDTO).submit(task);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit, @NotNull UserDetailSessionUserDTO detailSessionUserDTO) {
        return this.as(detailSessionUserDTO).schedule(command, delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit, @NotNull UserDetailSessionUserDTO detailSessionUserDTO) {
        return this.as(detailSessionUserDTO).schedule(callable, delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, @NotNull UserDetailSessionUserDTO detailSessionUserDTO) {
        return this.as(detailSessionUserDTO).scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit, @NotNull UserDetailSessionUserDTO detailSessionUserDTO) {
        return this.as(detailSessionUserDTO).scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
}
